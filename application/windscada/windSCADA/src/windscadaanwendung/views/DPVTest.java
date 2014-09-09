package windscadaanwendung.views;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.rcp.dashboard.views.DashboardPartView;

public class DPVTest extends DashboardPartView {

	public IFile dashboardPartFile;

	public DPVTest() {
	}
	
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		IProject project = workspaceRoot.getProject("GUI");
		try {
			if(!project.exists()) {
			project.create(null);
			}
			project.open(null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		dashboardPartFile = project.getFile("BidAgg.prt");
		if (this.dashboardPartFile != null && dashboardPartFile.exists()) {
			super.showDashboardPart(dashboardPartFile);
		}
		else {
			System.out.println("Keine Datei gefunden!");
		}
	}

}
