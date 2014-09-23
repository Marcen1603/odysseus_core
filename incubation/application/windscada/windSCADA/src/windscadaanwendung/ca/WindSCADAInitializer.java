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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import windscadaanwendung.Activator;
import windscadaanwendung.db.DBConnection;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class WindSCADAInitializer {

	private final static String[] DAFileNames = { "corrected_score.qry",
			"phase_shift.qry", "pitch_angle.qry", "rotational_speed.qry",
			"wind_speed.qry", "corrected_score_wind_speed.qry" };
	private final static String[] GUIDashboardPartFileNames = {
			"corrected_score_tf.prt", "corrected_score.prt", "phase_shift.prt",
			"pitch_angle.prt", "rotational_speed.prt", "wind_speed.prt", "corrected_score_wind_speed.prt" };
	private static final String[] GUIQueriesFileNames = {
			"corrected_score.qry", "phase_shift.qry", "pitch_angle.qry",
			"rotational_speed.qry", "wind_speed.qry" , "corrected_score_wind_speed.qry"};

	/**
	 * Creates new Job that executes initialization of WindSCADA
	 */
	public static void init() {
		Job job = new Job("Init WindSCADA") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					SubMonitor progress = SubMonitor.convert(monitor,
							"Initializing WindSCADA", 10);
					progress.subTask("Resetting Odysseus");
					clearOdysseus();
					progress.worked(1);
					progress.subTask("Loading Configuration");
					loadConfig();
					progress.worked(1);
					initScripts(progress.newChild(7));
					progress.subTask("Opening perspective");
					openPerspetive("windscadaanwendung.perspective");
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
				IPerspectiveDescriptor pers = PlatformUI
						.getWorkbench()
						.getPerspectiveRegistry()
						.findPerspectiveWithId("windscadaanwendung.perspective");
				page.closePerspective(pers, false, true);
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
	 * @param monitor
	 *            the progress monitor to use for reporting progress to the
	 *            user. It is the caller's responsibility to call done() on the
	 *            given monitor. Accepts null, indicating that no progress
	 *            should be reported and that the operation cannot be cancelled.
	 */
	private static void initScripts(IProgressMonitor monitor) {
		SubMonitor progress = SubMonitor.convert(monitor, FarmList
				.getFarmList().size());
		String fileContent = loadFileContent("querypatterns/HD/connectDatabase.qry");
		executeQuery(fileContent);
		for (WindFarm farm : FarmList.getFarmList()) {
			SubMonitor subprogress = progress.newChild(1).setWorkRemaining(
					farm.getWkas().size());
			for (WKA wka : farm.getWkas()) {
				subprogress.subTask("Initializing wind turbine " + wka.getID());
				initDA(farm.getID(), wka.getID(), wka.getHost(), wka.getPort());
				initHD(farm.getID(), wka.getID());
				initAE(farm.getID(), wka.getID());
				initGUI(farm.getID(), wka.getID());
				subprogress.worked(1);
			}
		}
	}

	/**
	 * Invokes Farmlist to receive CA information form configurtation database
	 */
	private static void loadConfig() {
		DBConnection.setNewConnection();
		FarmList.setFarmList(DBConnection.getFarmList());
		try {
			DBConnection.conn.close();
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
		String adjustedQuery = query.replace("§windfarmid§",
				String.valueOf(windfarm_id));
		adjustedQuery = adjustedQuery
				.replace("§wkaid§", String.valueOf(wka_id));
		return adjustedQuery;
	}

	/**
	 * Replacing connection information host, port in a given String
	 * 
	 * @param query
	 *            query which schould be adjusted with conneciton information
	 * @param host
	 *            host name to replace the substring §host§
	 * @param port
	 *            port to replace the substring §port§
	 * @return
	 */
	private static String replaceConnectionInfo(String query, String host,
			int port) {
		String adjustedQuery = query.replace("§host§", host);
		adjustedQuery = adjustedQuery.replace("§port§", String.valueOf(port));
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
				OdysseusRCPPlugIn.getActiveSession(), "Standard",
				Context.empty());
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
			if(file.exists()) {
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
	 * @param farmId
	 *            windfarm id
	 * @param wkaId
	 *            wind turbine id
	 * @param host
	 *            host address to receive measurements
	 * @param port
	 *            port
	 */
	private static void initDA(int farmId, int wkaId, String host, int port) {
		String fileContent = loadFileContent("querypatterns/DA/bindSource.qry");
		String query = adjustQuery(fileContent, farmId, wkaId);
		query = replaceConnectionInfo(query, host, port);
		executeQuery(query);
		for (String fileName : DAFileNames) {
			fileContent = loadFileContent("querypatterns/DA/" + fileName);
			query = adjustQuery(fileContent, farmId, wkaId);
			query = replaceConnectionInfo(query, host, port);
			executeQuery(query);
		}
	}

	/**
	 * Loads and runs query for one wind turbine needed by WindSCADA Historical
	 * Data component
	 * 
	 * @param farmId
	 *            windfarm id
	 * @param wkaId
	 *            wind turbine id
	 */
	private static void initHD(int farmId, int wkaId) {
		String fileContent = loadFileContent("querypatterns/HD/archiveData.qry");
		String query = adjustQuery(fileContent, farmId, wkaId);
		storeFileInWorkspace("HD", wkaId + "archiveData.qry", query);
		executeQuery(query);
	}

	/**
	 * Loads and runs querys for one wind turbine needed by WindSCADA Alarms and
	 * Events component
	 * 
	 * @param farmId
	 *            windfarm id
	 * @param wkaId
	 *            wind turbine id
	 */
	private static void initAE(int farmId, int wkaId) {
		// TODO
	}

	/**
	 * Loads and runs querys for one wind turbine needed by WindSCADA GUI
	 * 
	 * @param farmId
	 *            windfarm id
	 * @param wkaId
	 *            wind turbine id
	 */
	private static void initGUI(int farmId, int wkaId) {
		for (String fileName : GUIQueriesFileNames) {
			String fileContent = loadFileContent("querypatterns/GUI/"
					+ fileName);
			String query = adjustQuery(fileContent, farmId, wkaId);
			storeFileInWorkspace("GUI", wkaId + fileName, query);
			executeQuery(query);
		}
		for (String fileName : GUIDashboardPartFileNames) {
			String fileContent = loadFileContent("querypatterns/GUI/"
					+ fileName);
			String query = adjustQuery(fileContent, farmId, wkaId);
			storeFileInWorkspace("GUI", wkaId + fileName, query);
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
