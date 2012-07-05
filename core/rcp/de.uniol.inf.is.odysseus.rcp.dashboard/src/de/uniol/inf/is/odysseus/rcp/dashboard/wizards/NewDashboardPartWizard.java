package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import java.util.Map;

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

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;

public class NewDashboardPartWizard extends Wizard implements INewWizard {

	private static final Logger LOG = LoggerFactory.getLogger(NewDashboardPartWizard.class);
	
	private ContainerSelectionPage containerPage;
	private DashboardPartTypeSelectionPage partTypePage;
	private QueryFileSelectionPage queryFilePage;
	
	public NewDashboardPartWizard() {
		super();
		setWindowTitle("New Odysseus Script");
		
		
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		containerPage = new ContainerSelectionPage("Select file name", selection);
		partTypePage = new DashboardPartTypeSelectionPage("Select type of Dashboard Part");
		queryFilePage = new QueryFileSelectionPage("Select query", containerPage);
	}
	
	@Override
	public void addPages() {
		addPage(containerPage);
		addPage(partTypePage);
		addPage(queryFilePage);
	}
	
	@Override
	public boolean performFinish() {
		try {
			String dashboardPartFileName = getDashboardPartFileName();

			IPath path = containerPage.getContainerFullPath().append(dashboardPartFileName);
			IFile dashboardPartFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			dashboardPartFile.create(null, IResource.NONE, null);
			
			IDashboardPart part = DashboardPartRegistry.createDashboardPart(partTypePage.getSelectedDashboardPartName());
			Configuration defaultConfiguration = part.getConfiguration();
			Map<String, String> settings = partTypePage.getSelectedSettings();
			for( String key : settings.keySet() ) {
				defaultConfiguration.setAsString(key, settings.get(key));
			}			
			part.setQueryFile(queryFilePage.getQueryFile());
			
			IDashboardPartHandler handler = new XMLDashboardPartHandler();
			handler.save(part, dashboardPartFile);
			
			return true;
		} catch (CancelException ex) {
			return false;
		} catch (Exception ex) {
			LOG.error("Could not finish wizard", ex);
			return false;
		}
	}

	private String getDashboardPartFileName() throws CancelException {
		String queryFileName = containerPage.getFileName();

		Optional<String> optionalExtension = getFileExtension(queryFileName);
		if (!optionalExtension.isPresent()) {
			queryFileName = queryFileName + "." + DashboardPlugIn.DASHBOARD_PART_EXTENSION;
		} else {
			String extension = optionalExtension.get();
			if (!extension.equals(DashboardPlugIn.DASHBOARD_PART_EXTENSION)) {

				if (!isOtherExtensionOk(extension, DashboardPlugIn.DASHBOARD_PART_EXTENSION)) {
					queryFileName = queryFileName.replace(extension, DashboardPlugIn.DASHBOARD_PART_EXTENSION);
				}
			}
		}
		return queryFileName;
	}

	private static boolean isOtherExtensionOk(String desiredExtension, String standardExtension) throws CancelException {
		MessageDialog dlg = new MessageDialog(Display.getCurrent().getActiveShell(), "Custom file extension", null, "Should the file extension '" + desiredExtension + "' be replaced by '" + standardExtension + "'?", MessageDialog.QUESTION, new String[] { "Replace", "Keep", "Cancel" }, 0);
		int ret = dlg.open();
		if (ret == 1) {
			return true;
		} else if (ret == 0) {
			return false;
		}
		throw new CancelException();
	}

	private static Optional<String> getFileExtension(String fileName) {
		int lastPoint = fileName.lastIndexOf(".");
		if (lastPoint != -1) {
			return Optional.of(fileName.substring(lastPoint + 1));
		}
		return Optional.absent();
	}
}

@SuppressWarnings("serial")
class CancelException extends Exception {
};
