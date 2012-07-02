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

public class NewDashboardPartWizard extends Wizard implements INewWizard {

	private static final Logger LOG = LoggerFactory.getLogger(NewDashboardPartWizard.class);
	
	private NewDashboardPartWizardPage1 page1;
	private NewDashboardPartWizardPage2 page2;
	
	public NewDashboardPartWizard() {
		super();
		setWindowTitle("New Odysseus Script");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page1 = new NewDashboardPartWizardPage1("Select file name", selection);
		page2 = new NewDashboardPartWizardPage2("Select type of Dashboard Part");
	}
	
	@Override
	public void addPages() {
		addPage(page1);
		addPage(page2);
	}

	@Override
	public boolean performFinish() {
		try {
			String queryFileName = getQueryFileName();

			IPath path = page1.getContainerFullPath().append(queryFileName);
			IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			newFile.create(null, IResource.NONE, null);

			// open editor
//			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(newFile), OdysseusRCPEditorTextPlugIn.ODYSSEUS_SCRIPT_EDITOR_ID, true);

			return true;
		} catch (CancelException ex) {
			return false;
		} catch (Exception ex) {
			LOG.error("Could not finish wizard", ex);
			return false;
		}
	}

	private String getQueryFileName() throws CancelException {
		String queryFileName = page1.getFileName();

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
