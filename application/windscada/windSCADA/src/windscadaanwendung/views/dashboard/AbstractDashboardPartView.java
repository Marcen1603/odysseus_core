package windscadaanwendung.views.dashboard;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.rcp.dashboard.views.DashboardPartView;

public class AbstractDashboardPartView extends DashboardPartView {

	public IFile dashboardPartFile;
	public IProject project;
	public String valueType;

	public AbstractDashboardPartView() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		project = workspaceRoot.getProject("GUI");
		try {
			if(!project.exists()) {
			project.create(null);
			}
			project.open(null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public void loadDashboardPartFile(String path) {
		dashboardPartFile = project.getFile(path);
		
		if (this.dashboardPartFile != null && dashboardPartFile.exists()) {
			/*
			if (super.isShowing()) {
				super.setShowing(false);
				System.out.println("showing auf nein gesetzt!");
			}
			//TODO: UNBEDINGT KLAEREN!!
			 */
			super.showDashboardPart(dashboardPartFile);
			System.out.println("KLAPPT");
		}
		else {
			System.out.println("Keine Datei gefunden!");
		}
	}

}
