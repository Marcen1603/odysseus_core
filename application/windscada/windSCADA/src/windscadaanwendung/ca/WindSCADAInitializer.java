package windscadaanwendung.ca;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import windscadaanwendung.Activator;
import windscadaanwendung.db.DBConnection;

public class WindSCADAInitializer {

	public static void init() {
		clearOdysseus();
		loadConfig();
		initScripts();
	}
	
	private static void clearOdysseus() {
		// TODO Auto-generated method stub
		
	}

	private static void initScripts() {
		String fileContent = loadFileContent("querypatterns/HD/connectDatabase.qry");
		executeQuery(fileContent);
		for (WindFarm farm: FarmList.getFarmList()) {
			for (WKA wka: farm.getWkas()) {
				initDA(farm.getID(),wka.getID());
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
				content += str;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return content;
	}
	
	private static String adjustQuery(String query, int windfarm_id, int wka_id) {
		String adjustedQuery = new String(query);
		adjustedQuery.replaceAll("§windfarm_id§", String.valueOf(windfarm_id));
		adjustedQuery.replaceAll("§wka_id§", String.valueOf(wka_id));
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
	
	private static void initDA(int farmId, int wkaId) {
		
	}
	
	private static void initHD(int farmId, int wkaId) {
		String fileContent = loadFileContent("querypatterns/HD/archiveData.qry");
		String query = adjustQuery(fileContent,farmId,wkaId);
		executeQuery(query);
	}
	
	private static void initAE(int farmId, int wkaId) {
		
	}
	
	private static void initGUI(int farmId, int wkaId) {
		
	}
}
