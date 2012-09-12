package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;

public class NewDashboardWizard extends Wizard implements INewWizard {

	private static final Logger LOG = LoggerFactory.getLogger(NewDashboardWizard.class);
	private static final String WINDOW_TITLE = "New Dashboard";
	private static final IDashboardHandler DASHBOARD_HANDLER = new XMLDashboardHandler();

	private ContainerSelectionPage containerSelectionPage;

	public NewDashboardWizard() {
		super();

		setWindowTitle(WINDOW_TITLE);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		containerSelectionPage = new ContainerSelectionPage("Select file name", selection, "dashboard." + DashboardPlugIn.DASHBOARD_EXTENSION);
	}

	@Override
	public void addPages() {
		addPage(containerSelectionPage);
	}

	@Override
	public boolean performFinish() {
		try {
			String dashboardFileName = getDashboardPartFileName(containerSelectionPage);

			IPath path = containerSelectionPage.getContainerFullPath().append(dashboardFileName);
			IFile dashboardFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			dashboardFile.create(null, IResource.NONE, null);

			Dashboard dashboard = new Dashboard();
			FileUtil.write(DASHBOARD_HANDLER.save(dashboard), dashboardFile);

			return true;
		} catch (CancelException ex) {
			return false;
		} catch (Exception ex) {
			LOG.error("Could not finish wizard", ex);
			return false;
		}
	}

	private static String getDashboardPartFileName(ContainerSelectionPage containerPage) throws CancelException {
		String queryFileName = containerPage.getFileName();

		Optional<String> optionalExtension = getFileExtension(queryFileName);
		if (!optionalExtension.isPresent()) {
			queryFileName = queryFileName + "." + DashboardPlugIn.DASHBOARD_EXTENSION;
		} else {
			String extension = optionalExtension.get();
			if (!extension.equals(DashboardPlugIn.DASHBOARD_EXTENSION)) {

				if (!isOtherExtensionOk(extension, DashboardPlugIn.DASHBOARD_EXTENSION)) {
					queryFileName = queryFileName.replace(extension, DashboardPlugIn.DASHBOARD_EXTENSION);
				}
			}
		}
		return queryFileName;
	}

	private static Optional<String> getFileExtension(String fileName) {
		int lastPoint = fileName.lastIndexOf(".");
		if (lastPoint != -1) {
			return Optional.of(fileName.substring(lastPoint + 1));
		}
		return Optional.absent();
	}

	private static boolean isOtherExtensionOk(String desiredExtension, String standardExtension) throws CancelException {
		MessageDialog dlg = new MessageDialog(Display.getCurrent().getActiveShell(), "Custom file extension", null, "Should the file extension '" + desiredExtension + "' be replaced by '"
				+ standardExtension + "'?", MessageDialog.QUESTION, new String[] { "Replace", "Keep", "Cancel" }, 0);
		int ret = dlg.open();
		if (ret == 1) {
			return true;
		} else if (ret == 0) {
			return false;
		}
		throw new CancelException();
	}
}
