package windscadaanwendung.views.dashboard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.rcp.dashboard.views.DashboardPartView;

/**
 * This class extends the DashboardPartView and provides special functions and
 * variables for the DashboardPartViews out of the bundle windSCADA they should
 * extend this class
 * 
 * @author MarkMilster
 * 
 */
public class WindDashboardPartView extends DashboardPartView {

	public IFile dashboardPartFile;
	public IProject project;
	protected String valueType;
	private final String fileEnding = ".prt";

	/**
	 * The fileending of the XML-Files which defines the Dashboardparts
	 * 
	 * @return the fileEnding
	 */
	public String getFileEnding() {
		return fileEnding;
	}

	/**
	 * The valuetype of a Dashboardpart (e.g. windspeed)
	 * 
	 * @return the valueType
	 */
	public String getValueType() {
		return valueType;
	}

	/**
	 * The valuetype of a Dashboardpart (e.g. windspeed)
	 * 
	 * @param valueType
	 *            the valueType to set
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	/**
	 * True if this DashboardpartView shows measurements over a whole windfarm.
	 * False if this DashboardpartView shows measurements over only one WKA.
	 * 
	 * @return the farmPart
	 */
	protected boolean isFarmPart() {
		return farmPart;
	}

	/**
	 * True if this DashboardpartView shows measurements over a whole windfarm.
	 * False if this DashboardpartView shows measurements over only one WKA.
	 * 
	 * @param farmPart
	 *            the farmPart to set
	 */
	public void setFarmPart(boolean farmPart) {
		this.farmPart = farmPart;
	}

	public boolean farmPart;

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		// initiate the Project GUI in which the XML-Files of the Dashboardparts
		// should be found
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		project = workspaceRoot.getProject("GUI");
		try {
			if (!project.exists()) {
				project.create(null);
			}
			project.open(null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads a dashboardPartFile out of the project GUI in the
	 * Odysseus-Workspace and shows the DashboardPart which is defined in this
	 * file.
	 * 
	 * @param path
	 *            The path of the file in the Odysseus-Project GUI
	 */
	public void loadDashboardPartFile(String path) {
		dashboardPartFile = project.getFile(path);

		if (this.dashboardPartFile != null && dashboardPartFile.exists()) {
			super.showDashboardPart(dashboardPartFile);
		} else {
			System.out.println("No project-file " + path);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		DashboardPartViewObserver.removeDashboardPartView(this);
	}

}
