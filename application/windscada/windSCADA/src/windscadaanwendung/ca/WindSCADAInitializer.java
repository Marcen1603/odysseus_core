package windscadaanwendung.ca;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.osgi.framework.Bundle;

import windscadaanwendung.Activator;
import windscadaanwendung.db.DBConnection;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.commands.DropAllSourcesCommand;
import de.uniol.inf.is.odysseus.rcp.commands.RemoveAllQueriesCommand;
public class WindSCADAInitializer {
	
	private final static String[] DAFileNames = {"corrected_score.qry","phase_shift.qry","pitch_angle.qry","rotational_speed.qry","wind_speed.qry"};
	private final static String[] GUIDashboardPartFileNames = {"corrected_score_tf.prt", "corrected_score.prt","phase_shift.prt","pitch_angle.prt","rotational_speed.prt","wind_speed.prt"};
	private static final String[] GUIQueriesFileNames = {"corrected_score.qry","phase_shift.qry","pitch_angle.qry","rotational_speed.qry","wind_speed.qry"};
	
	public static void init() {
		clearOdysseus();
		loadConfig();
		initScripts();
		openPerspetive();
	}

	private static void clearOdysseus() {
		RemoveAllQueriesCommand dropQueries= new RemoveAllQueriesCommand();
		DropAllSourcesCommand dropSources= new DropAllSourcesCommand();
		try {
			dropQueries.execute(new ExecutionEvent());
			dropSources.execute(new ExecutionEvent());
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		String fileContent = loadFileContent("querypatterns/HD/dropDatabase.qry");
		executeQuery(fileContent);
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IPerspectiveDescriptor pers = PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId("windscadaanwendung.perspective");
		page.closePerspective(pers, false, true);
	}

	private static void initScripts() {
		String fileContent = loadFileContent("querypatterns/HD/connectDatabase.qry");
		executeQuery(fileContent);
		for (WindFarm farm: FarmList.getFarmList()) {
			for (WKA wka: farm.getWkas()) {
				initDA(farm.getID(),wka.getID(), wka.getHost(), wka.getPort());
				initHD(farm.getID(),wka.getID());
				initAE(farm.getID(),wka.getID());
				initGUI(farm.getID(),wka.getID());
			}
		}
	}

	private static void loadConfig() {
		DBConnection.setNewConnection();
		FarmList.setFarmList(DBConnection.getFarmList());
		try {
			DBConnection.conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
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
			while((str = reader.readLine()) != null) {
				content += str + "\n";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return content;
	}
	
	private static String adjustQuery(String query, int windfarm_id, int wka_id) {
		String adjustedQuery = query.replace("§windfarmid§", String.valueOf(windfarm_id));
		adjustedQuery = adjustedQuery.replace("§wkaid§", String.valueOf(wka_id));
		return adjustedQuery;
	}
	
	private static String replaceConnectionInfo(String query, String host, int port) {
		String adjustedQuery = query.replace("§host§", host);
		adjustedQuery = adjustedQuery.replace("§port§", String.valueOf(port));
		return adjustedQuery;
	}
	
	private static void executeQuery(String query) {
		OdysseusRCPPlugIn.getExecutor().addQuery(query, "OdysseusScript", OdysseusRCPPlugIn.getActiveSession(), "Standard", Context.empty());
	}
	
	private static void storeFileInWorkspace(String projectName, String fileName, String content) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		IProject project = workspaceRoot.getProject(projectName);
		try {
			if(!project.exists()) {
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
			file.create(in, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private static void initDA(int farmId, int wkaId, String host, int port) {
		String fileContent = loadFileContent("querypatterns/DA/bindSource.qry");
		String query = adjustQuery(fileContent,farmId,wkaId);
		query = replaceConnectionInfo(query, host, port);
		executeQuery(query);
		for (String fileName: DAFileNames) {
			fileContent = loadFileContent("querypatterns/DA/" + fileName);
			query = adjustQuery(fileContent,farmId,wkaId);
			query = replaceConnectionInfo(query, host, port);
			executeQuery(query);
		}
	}
	
	private static void initHD(int farmId, int wkaId) {
		String fileContent = loadFileContent("querypatterns/HD/archiveData.qry");
		String query = adjustQuery(fileContent,farmId,wkaId);
		storeFileInWorkspace("HD",wkaId + "archiveData.qry", query);
		executeQuery(query);
	}
	
	private static void initAE(int farmId, int wkaId) {
		//TODO
	}
	
	private static void initGUI(int farmId, int wkaId) {
		for (String fileName: GUIQueriesFileNames) {
			String fileContent = loadFileContent("querypatterns/GUI/" + fileName);
			String query = adjustQuery(fileContent,farmId,wkaId);
			storeFileInWorkspace("GUI",wkaId + fileName, query);
			executeQuery(query);
		}
		for (String fileName: GUIDashboardPartFileNames) {
			String fileContent = loadFileContent("querypatterns/GUI/" + fileName);
			String query = adjustQuery(fileContent,farmId,wkaId);
			storeFileInWorkspace("GUI",wkaId + fileName, query);
		}
	}
	
	private static void openPerspetive() {
		try {
			PlatformUI.getWorkbench().showPerspective("windscadaanwendung.perspective", PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
		
	}
}
