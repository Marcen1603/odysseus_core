package windscadaanwendung.views.dashboard;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.rcp.dashboard.views.DashboardPartView;

public class WindDashboardPartView extends DashboardPartView {

	public IFile dashboardPartFile;
	public IProject project;
	protected String valueType;
	private final String fileEnding = ".prt";
	/**
	 * @return the fileEnding
	 */
	public String getFileEnding() {
		return fileEnding;
	}

	/**
	 * @return the valueType
	 */
	public String getValueType() {
		return valueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return the farmPart
	 */
	protected boolean isFarmPart() {
		return farmPart;
	}

	/**
	 * @param farmPart the farmPart to set
	 */
	public void setFarmPart(boolean farmPart) {
		this.farmPart = farmPart;
	}

	public boolean farmPart;
	
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
			super.showDashboardPart(dashboardPartFile);
		}
		else {
			System.out.println("No project-file " + path);
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		DashboardPartViewObserver.removeDashboardPartView(this);
	}

}
