package windscadaanwendung.ca;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.osgi.framework.Bundle;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import windscadaanwendung.Activator;
import windscadaanwendung.db.DBConnectionCA;
import windscadaanwendung.db.DBConnectionCredentials;
import windscadaanwendung.db.DBConnectionHD;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

/**
 * Initializes windSCADA. Resets Odysseus first, then starts necessary queries
 * and opens the windSCADA perspective
 * 
 * @author Dennis Nowak
 * 
 */
public class WindSCADAInitializer {

	private final static String[] DAFileNames = { "corrected_score.qry",
			"phase_shift.qry", "pitch_angle.qry", "rotational_speed.qry",
			"corrected_score_wind_speed.qry", "wind_speed.qry",
			"gier_angle.qry", "wind_direction.qry" };
	private final static String[] GUIDashboardPartFileNames = {
			"corrected_score_tf.prt", "corrected_score.prt", "phase_shift.prt",
			"pitch_angle.prt", "rotational_speed.prt", "wind_speed.prt",
			"corrected_score_wind_speed.prt", "wind_direction.prt",
			"gier_angle.prt" };
	private static final String[] GUIQueriesFileNames = {
			"corrected_score.qry", "phase_shift.qry", "pitch_angle.qry",
			"rotational_speed.qry", "wind_speed.qry",
			"corrected_score_wind_speed.qry", "wind_direction.qry",
			"gier_angle.qry" };
	private static final String[] AEQueriesFileNames = { "collectAlarms.qry" };
	private static final String PERSPECTIVE_ID_WITH_ML = "windscadaanwendung.perspective";
	private static final String PERSPECTIVE_ID_WITHOUT_ML = "windscadaanwendung.perspectiveWithoutML";

	/**
	 * Creates new Job that executes initialization of WindSCADA
	 * 
	 * @param initML
	 *            if true, the algorithms where machine learning algorithms are
	 *            nedded, are loaded, too.
	 */
	public static void init(final boolean initML) {
		Job job = new Job("Init windSCADA") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					SubMonitor progress = SubMonitor.convert(monitor,
							"Initializing windSCADA", 10);
					progress.subTask("Resetting Odysseus");
					clearOdysseus();
					progress.worked(1);
					progress.subTask("Loading Configuration");
					loadConfig();
					progress.worked(1);
					initScripts(initML, progress.newChild(7));
					progress.subTask("Opening perspective");
					if (initML) {
						openPerspetive(PERSPECTIVE_ID_WITH_ML);
					} else {
						openPerspetive(PERSPECTIVE_ID_WITHOUT_ML);
					}

					progress.worked(1);
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}

		};
		job.setUser(true);
		job.schedule();
	}

	/**
	 * Resets Odysseus, deletes all queries and sources, closes the windSCADA
	 * perspective
	 */
	private static void clearOdysseus() {
		deleteAllQueries();
		dropAllSources();
		String fileContent = loadFileContent("querypatterns/HD/dropDatabase.qry");
		executeQuery(fileContent);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IPerspectiveDescriptor pers = PlatformUI.getWorkbench()
						.getPerspectiveRegistry()
						.findPerspectiveWithId(PERSPECTIVE_ID_WITH_ML);
				IPerspectiveDescriptor pers2 = PlatformUI.getWorkbench()
						.getPerspectiveRegistry()
						.findPerspectiveWithId(PERSPECTIVE_ID_WITHOUT_ML);
				page.closePerspective(pers, false, true);
				page.closePerspective(pers2, false, true);
			}
		});
	}

	/**
	 * Deletes all Queries
	 */
	private static void deleteAllQueries() {
		IExecutor executor = Preconditions.checkNotNull(
				OdysseusRCPPlugIn.getExecutor(), "Executor must not be null!");
		Collection<Integer> ids = executor.getLogicalQueryIds(OdysseusRCPPlugIn
				.getActiveSession());
		for (int qID : ids) {
			executor.removeQuery(qID, OdysseusRCPPlugIn.getActiveSession());
		}
	}

	/**
	 * Drops all sources
	 */
	private static void dropAllSources() {
		IExecutor executor = Preconditions.checkNotNull(
				OdysseusRCPPlugIn.getExecutor(), "Executor must not be null!");
		List<Resource> sources = determineSourceIds(executor
				.getStreamsAndViewsInformation(OdysseusRCPPlugIn
						.getActiveSession()));
		ISession user = OdysseusRCPPlugIn.getActiveSession();
		try {
			for (Resource source : sources) {
				executor.removeViewOrStream(source, user);
			}
		} catch (PlanManagementException e) {
			e.printStackTrace();
		} catch (PermissionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads Scripts for DA, HD, AE and GUI component of WindSCADA for every
	 * wind turbine that is loaded by CA
	 * 
	 * @param initML
	 *            if true, the algorithms where machine learning algorithms are
	 *            nedded, are loaded, too.
	 * 
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	private static void initScripts(boolean initML, IProgressMonitor monitor) {
		SubMonitor progress = SubMonitor.convert(monitor, FarmList
				.getFarmList().size());
		String fileContent = loadFileContent("querypatterns/HD/connectDatabase.qry");
		Map<String, String> databaseConfig = DBConnectionCredentials
				.load("config/HDDBConnCredentials.txt");
		fileContent = replaceConnectionInfo(fileContent,
				databaseConfig.get("server"),
				Integer.parseInt(databaseConfig.get("port")));
		fileContent = replaceCredentialsInfo(fileContent,
				databaseConfig.get("user"), databaseConfig.get("password"),
				databaseConfig.get("database"));
		executeQuery(fileContent);
		// Start Queries which are created per wind turbine
		for (WindFarm farm : FarmList.getFarmList()) {
			SubMonitor subprogress = progress.newChild(1).setWorkRemaining(
					farm.getWkas().size() * 2);
			for (WKA wka : farm.getWkas()) {
				subprogress.subTask("Initializing wind turbine " + wka.getID());
				initDA(farm, wka);
				initHD(farm, wka);
				initAE(farm, wka);
				subprogress.worked(1);
			}
			for (WKA wka : farm.getWkas()) {
				if (initML) {
					initPrediction(farm, wka);
				}
				initGUI(farm, wka);
				subprogress.worked(1);
			}
			// Start queries which are created per wind farm
			if (initML) {
				initKohonen(farm);
			}
			initFarm(farm);
		}
		// Start queries which combine all inputs
		String query = unionStreams("AE");
		storeFileInWorkspace("AE", "collectAllStreams.qry", query);
		executeQuery(query);
		query = loadFileContent("querypatterns/AE/archiveData.qry");
		storeFileInWorkspace("AE", "archiveData.qry", query);
		executeQuery(query);
		query = loadFileContent("querypatterns/GUI/AE.qry");
		storeFileInWorkspace("GUI", "AE.qry", query);
		executeQuery(query);
		String fileContentDBP = loadFileContent("querypatterns/GUI/AEList.prt");
		storeFileInWorkspace("GUI", "AEList.prt", fileContentDBP);
	}

	/**
	 * Invokes Farmlist to receive CA information form configuration database
	 */
	private static void loadConfig() {
		DBConnectionCA.setNewConnection();
		DBConnectionHD.setNewConnection();
		FarmList.setFarmList(DBConnectionCA.getFarmList());
		try {
			DBConnectionCA.conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Loading a file that is placed in the bundle windscadaanwendung
	 * 
	 * @param path
	 *            Path to the file in Bundle windscadaanwendung
	 * @return content of the file
	 */
	private static String loadFileContent(String path) {
		FileReader fileReader;
		Bundle bundle = Activator.getDefault().getBundle();
		URL url = bundle.getResource(path);
		File file;
		String content = "";
		try {
			file = new File(FileLocator.toFileURL(url).getPath());
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			String str;
			while ((str = reader.readLine()) != null) {
				content += str + "\n";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return content;
	}

	/**
	 * Replacing wka infomartion in a given String
	 * 
	 * @param query
	 *            Query that schould be adjusted
	 * @param windfarm_id
	 *            id of the windpark that contains the wind turbine
	 * @param wka_id
	 *            id of the wind turbine
	 * @return
	 */
	private static String adjustQuery(String query, int windfarm_id, int wka_id) {
		String adjustedQuery = query.replace("xXxwindfarmidxXx",
				String.valueOf(windfarm_id));
		adjustedQuery = adjustedQuery.replace("xXxwkaidxXx",
				String.valueOf(wka_id));
		return adjustedQuery;
	}

	/**
	 * Replacing connection information host, port in a given String
	 * 
	 * @param query
	 *            query which schould be adjusted with conneciton information
	 * @param host
	 *            host name to replace the substring xXxhostxXx
	 * @param port
	 *            port to replace the substring xXxportxXx
	 * @return
	 */
	private static String replaceConnectionInfo(String query, String host,
			int port) {
		String adjustedQuery = query.replace("xXxhostxXx", host);
		adjustedQuery = adjustedQuery.replace("xXxportxXx",
				String.valueOf(port));
		return adjustedQuery;
	}

	/**
	 * Replacing credenial information username, password, database in a given
	 * String
	 * 
	 * @param query
	 *            query which schould be adjusted with conneciton information
	 * @param username
	 *            username to replace the substring xXxusernamexXx
	 * @param password
	 *            password to replace the substring xXxpasswordxXx
	 * @param databaseName
	 *            database name to replace the substring xXxdatabasexXx
	 * @return
	 */
	private static String replaceCredentialsInfo(String query, String username,
			String password, String databaseName) {
		String adjustedQuery = query.replace("xXxusernamexXx", username);
		adjustedQuery = adjustedQuery.replace("xXxpasswordxXx", password);
		adjustedQuery = adjustedQuery
				.replaceAll("xXxdatabasexXx", databaseName);
		return adjustedQuery;
	}

	/**
	 * Passes query to the Standard Exeduter
	 * 
	 * @param query
	 *            Query to execute
	 */
	private static void executeQuery(String query) {
		OdysseusRCPPlugIn.getExecutor().addQuery(query, "OdysseusScript",
				OdysseusRCPPlugIn.getActiveSession(),
				Context.empty());
	}

	/**
	 * Method that builds a query for merging datastreams for AE Component
	 * 
	 * @param suffix
	 *            name of the measurement to collect all streams of
	 * @return the query
	 */
	private static String unionStreams(String suffix) {
		String query = "#PARSER PQL\n#RUNQUERY\n";
		String unionString = suffix + " ::= MERGE(";
		for (WindFarm farm : FarmList.getFarmList()) {
			for (WKA wka : farm.getWkas()) {
				query += "Stream" + wka.getID() + " = Stream({SOURCE='System.P"
						+ farm.getID() + ":" + wka.getID() + ":" + suffix
						+ "'})\n";
				unionString += "0:Stream" + wka.getID() + ",";
			}
		}
		query += unionString.substring(0, unionString.length() - 1) + ")";
		return query;
	}

	/**
	 * Stores Text in file in specified project with specified file name.
	 * Creates Project if not already existent. Overwrites file if already
	 * existent.
	 * 
	 * @param projectName
	 *            the projectname to store the file in
	 * @param fileName
	 *            the name of the file to write
	 * @param content
	 *            content of the file to write
	 */
	private static void storeFileInWorkspace(String projectName,
			String fileName, String content) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		IProject project = workspaceRoot.getProject(projectName);
		try {
			if (!project.exists()) {
				project.create(null);
			}
			project.open(null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		IFile file = project.getFile(fileName);
		try {
			InputStream in = new ByteArrayInputStream(content.getBytes());
			// Creates or overwrites the file in workspace
			if (file.exists()) {
				file.delete(true, null);
			}
			file.create(in, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads and runs query for one wind turbine needed by WindSCADA Data Access
	 * component
	 * 
	 * @param farm
	 *            windfarm
	 * @param wka
	 *            wind turbine
	 */
	private static void initDA(WindFarm farm, WKA wka) {
		String fileContent = loadFileContent("querypatterns/DA/bindSource.qry");
		String query = adjustQuery(fileContent, farm.getID(), wka.getID());
		query = replaceConnectionInfo(query, wka.getHost(), wka.getPort());
		storeFileInWorkspace("DA", wka.getID() + "bindSource", query);
		executeQuery(query);
		for (String fileName : DAFileNames) {
			fileContent = loadFileContent("querypatterns/DA/" + fileName);
			query = adjustQuery(fileContent, farm.getID(), wka.getID());
			storeFileInWorkspace("DA", wka.getID() + fileName, query);
			executeQuery(query);
		}
	}

	/**
	 * Loads and runs query for one wind turbine needed by WindSCADA Historical
	 * Data component
	 * 
	 * @param farm
	 *            windfarm
	 * @param wka
	 *            wind turbine
	 */
	private static void initHD(WindFarm farm, WKA wka) {
		String fileContent = loadFileContent("querypatterns/HD/archiveData.qry");
		String query = adjustQuery(fileContent, farm.getID(), wka.getID());
		storeFileInWorkspace("HD", wka.getID() + "archiveData.qry", query);
		executeQuery(query);
	}

	/**
	 * Loads and runs querys for one wind turbine needed by WindSCADA Alarms and
	 * Events component
	 * 
	 * @param farm
	 *            windfarm
	 * @param wka
	 *            wind turbine
	 */
	private static void initAE(WindFarm farm, WKA wka) {
		for (String fileName : AEQueriesFileNames) {
			String fileContent = loadFileContent("querypatterns/AE/" + fileName);
			String query = adjustQuery(fileContent, farm.getID(), wka.getID());
			storeFileInWorkspace("AE", wka.getID() + fileName, query);
			executeQuery(query);
		}
	}

	/**
	 * Loads and starts queries that are neccessary to predict the corrected
	 * score of one wind turbine.
	 * 
	 * @param farm
	 *            windfarm
	 * @param wkaToPredict
	 *            wka the prediction should be calculated for
	 */
	private static void initPrediction(WindFarm farm, WKA wkaToPredict) {
		String query = loadFileContent("querypatterns/svr/svr.qry");
		// insert necessary operators for each wind turbine, marked by the
		// surrounding 'xYx'
		Joiner joiner = Joiner.on("\n").skipNulls();
		while (query.indexOf("xYx") >= 0) {
			String composite = null;
			String pattern = query
					.substring(query.indexOf("xYx") + "xYx".length(),
							query.indexOf("yYy"));
			for (WKA wka : farm.getWkas()) {
				composite = joiner.join(composite,
						adjustQuery(pattern, farm.getID(), wka.getID()));

				Bundle bundle = Activator.getDefault().getBundle();
				URL url = bundle.getResource("/data/2004/" + wka.getID()
						+ ".csv");
				try {
					String fileName = FileLocator.toFileURL(url).getPath();
					composite = composite.replace("xXxfilepathxXx", fileName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			query = query.replace("xYx" + pattern + "yYy", composite);
		}
		// insert operatorlist, one entry with surrounding 'xZx' must exist
		joiner = Joiner.on(",").skipNulls();
		while (query.indexOf("xZx") >= 0) {
			String composite = null;
			String pattern = query
					.substring(query.indexOf("xZx") + "xZx".length(),
							query.indexOf("yZy"));
			for (int i = 0; i < farm.getWkas().size(); i++) {
				composite = joiner.join(
						composite,
						adjustQuery(pattern, farm.getID(), farm.getWkas()
								.get(i).getID()));
				composite = composite.replaceAll("xXxcounterxXx",
						String.valueOf(i));
			}
			query = query.replace("xZx" + pattern + "yZy", composite);
		}
		query = adjustQuery(query, farm.getID(), wkaToPredict.getID());
		query = query.replaceAll("xXxwkaposxXx",
				String.valueOf(farm.getWkas().indexOf(wkaToPredict)));
		storeFileInWorkspace("svr", wkaToPredict.getID() + "svr.qry", query);
		executeQuery(query);
		query = loadFileContent("querypatterns/GUI/svr.qry");
		query = adjustQuery(query, farm.getID(), wkaToPredict.getID());
		storeFileInWorkspace("GUI", wkaToPredict.getID() + "svr.qry", query);
		executeQuery(query);
		query = loadFileContent("querypatterns/GUI/svr.prt");
		query = adjustQuery(query, farm.getID(), wkaToPredict.getID());
		storeFileInWorkspace("GUI", wkaToPredict.getID() + "svr.prt", query);
	}

	/**
	 * Loads and starts query which calculates the sum of the corrected scores
	 * of a windfarm
	 * 
	 * @param farm
	 *            windfarm
	 */
	private static void initFarm(WindFarm farm) {
		String query = loadFileContent("querypatterns/DA/corrected_score_sum.qry");
		Joiner joiner = Joiner.on("\n").skipNulls();
		while (query.indexOf("xYx") >= 0) {
			String composite = null;
			String pattern = query
					.substring(query.indexOf("xYx") + "xYx".length(),
							query.indexOf("yYy"));
			for (WKA wka : farm.getWkas()) {
				composite = joiner.join(composite,
						adjustQuery(pattern, farm.getID(), wka.getID()));
			}
			query = query.replace("xYx" + pattern + "yYy", composite);
		}
		// insert operatorlist, one entry with surrounding 'xZx' must exist
		joiner = Joiner.on(",").skipNulls();
		while (query.indexOf("xZx") >= 0) {
			String composite = null;
			String pattern = query
					.substring(query.indexOf("xZx") + "xZx".length(),
							query.indexOf("yZy"));
			for (int i = 0; i < farm.getWkas().size(); i++) {
				composite = joiner.join(
						composite,
						adjustQuery(pattern, farm.getID(), farm.getWkas()
								.get(i).getID()));
				composite = composite.replaceAll("xXxcounterxXx",
						String.valueOf(i));
			}
			query = query.replace("xZx" + pattern + "yZy", composite);
		}
		joiner = Joiner.on("+").skipNulls();
		while (query.indexOf("xAx") >= 0) {
			String composite = null;
			String pattern = query
					.substring(query.indexOf("xAx") + "xAx".length(),
							query.indexOf("yAy"));
			for (int i = 0; i < farm.getWkas().size(); i++) {
				composite = joiner.join(
						composite,
						adjustQuery(pattern, farm.getID(), farm.getWkas()
								.get(i).getID()));
				composite = composite.replaceAll("xXxcounterxXx",
						String.valueOf(i));
			}
			query = query.replace("xAx" + pattern + "yAy", composite);
		}
		query = adjustQuery(query, farm.getID(), farm.getWkas().get(0).getID());
		storeFileInWorkspace("DA", farm.getID() + "corrected_score_sum.qry",
				query);
		executeQuery(query);
		query = loadFileContent("querypatterns/GUI/corrected_score_sum.qry");
		query = adjustQuery(query, farm.getID(), 0);
		storeFileInWorkspace("GUI", farm.getID() + "corrected_score_sum.qry",
				query);
		executeQuery(query);
		query = loadFileContent("querypatterns/GUI/corrected_score_sum.prt");
		query = adjustQuery(query, farm.getID(), 0);
		storeFileInWorkspace("GUI", farm.getID() + "corrected_score_sum.prt",
				query);
	}

	/**
	 * Loads and starts scripts that are necessary to visualize the corrected
	 * score of a whole windpark
	 * 
	 * @param farm
	 *            windfarm
	 */
	private static void initKohonen(WindFarm farm) {
		String query = loadFileContent("querypatterns/kohonen/computeKohonenMap.qry");
		// insert necessary operators for each wind turbine, marked by the
		// surrounding 'xYx'
		Joiner joiner = Joiner.on("\n").skipNulls();
		while (query.indexOf("xYx") >= 0) {
			String composite = null;
			String pattern = query
					.substring(query.indexOf("xYx") + "xYx".length(),
							query.indexOf("yYy"));
			for (WKA wka : farm.getWkas()) {
				composite = joiner.join(composite,
						adjustQuery(pattern, farm.getID(), wka.getID()));

				Bundle bundle = Activator.getDefault().getBundle();
				URL url = bundle.getResource("/data/2004/" + wka.getID()
						+ ".csv");
				try {
					String fileName = FileLocator.toFileURL(url).getPath();
					composite = composite.replace("xXxfilepathxXx", fileName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			query = query.replace("xYx" + pattern + "yYy", composite);
		}
		// insert operatorlist, one entry with surrounding 'xZx' must exist
		joiner = Joiner.on(",").skipNulls();
		while (query.indexOf("xZx") >= 0) {
			String composite = null;
			String pattern = query
					.substring(query.indexOf("xZx") + "xZx".length(),
							query.indexOf("yZy"));
			for (int i = 0; i < farm.getWkas().size(); i++) {
				composite = joiner.join(
						composite,
						adjustQuery(pattern, farm.getID(), farm.getWkas()
								.get(i).getID()));
				composite = composite.replaceAll("xXxcounterxXx",
						String.valueOf(i));
			}
			query = query.replace("xZx" + pattern + "yZy", composite);
		}
		query = adjustQuery(query, farm.getID(), farm.getWkas().get(0).getID());
		storeFileInWorkspace("kohonen", farm.getID() + "computeKohonenMap.qry",
				query);
		executeQuery(query);

		String fileContent = loadFileContent("querypatterns/kohonen/ColorList.qry");
		query = adjustQuery(fileContent, farm.getID(), 0);
		storeFileInWorkspace("GUI", farm.getID() + "ColorList.qry", query);
		executeQuery(query);

		fileContent = loadFileContent("querypatterns/kohonen/ColorList.prt");
		query = adjustQuery(fileContent, farm.getID(), 0);
		storeFileInWorkspace("GUI", farm.getID() + "ColorList.prt", query);

	}

	/**
	 * Loads and runs querys for one wind turbine needed by WindSCADA GUI
	 * 
	 * @param farm
	 *            windfarm
	 * @param wka
	 *            wind turbine
	 */
	private static void initGUI(WindFarm farm, WKA wka) {
		for (String fileName : GUIQueriesFileNames) {
			String fileContent = loadFileContent("querypatterns/GUI/"
					+ fileName);
			String query = adjustQuery(fileContent, farm.getID(), wka.getID());
			storeFileInWorkspace("GUI", wka.getID() + fileName, query);
			executeQuery(query);
		}
		for (String fileName : GUIDashboardPartFileNames) {
			String fileContent = loadFileContent("querypatterns/GUI/"
					+ fileName);
			String query = adjustQuery(fileContent, farm.getID(), wka.getID());
			storeFileInWorkspace("GUI", wka.getID() + fileName, query);
		}
	}

	/**
	 * Opens a perspective, callable from any thread as this method delegates
	 * invocation to the default display
	 * 
	 * @param perspectiveId
	 *            Id of the perspective to open
	 */
	private static void openPerspetive(final String perspectiveId) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					PlatformUI.getWorkbench().showPerspective(
							perspectiveId,
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow());
				} catch (WorkbenchException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Extracts names of sources
	 * 
	 * @param sources
	 *            list of sources
	 * @return a list of the names of sources
	 */
	private static ImmutableList<Resource> determineSourceIds(
			List<ViewInformation> sources) {
		if (sources == null || sources.isEmpty()) {
			return ImmutableList.of();
		}

		ImmutableList.Builder<Resource> builder = ImmutableList.builder();
		for (ViewInformation source : sources) {
			builder.add(source.getName());
		}
		return builder.build();
	}
}
