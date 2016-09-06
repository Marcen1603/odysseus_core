package de.uniol.inf.is.odysseus.iql.odl.ui.wizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;

public class ODLWizard extends Wizard implements INewWizard {

	private static final String EDITOR_ID = "de.uniol.inf.is.odysseus.iql.odl.ODL";
	private static final String EXTENSION = "odl";

	private ODLWizardPage page;
	private SelectODLTemplateWizardPage selectTemplatePage;

	public ODLWizard() {
		super();
		setWindowTitle("New ODL File");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page = new ODLWizardPage("New ODL File", selection);
		selectTemplatePage = new SelectODLTemplateWizardPage("Select odl template");
	}

	@Override
	public void addPages() {
		addPage(page);
		addPage(selectTemplatePage);
	}
	
	@Override
	public IWizardPage getStartingPage() {
		return page;
	}

	@Override
	public boolean performFinish() {

		try {
			String queryFileName = getQueryFileName();

			IPath path = page.getContainerFullPath().append(queryFileName);
			IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			newFile.create(getTemplate(), IResource.NONE, null);

			// open editor
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(newFile), EDITOR_ID, true);

			return true;
		} catch (CancelException ex) {
			return false;
		} catch (Exception ex) {
			new ExceptionWindow(ex);
			return false;
		}
	}

	protected InputStream getTemplate() {
		String template = selectTemplatePage.getSelectedTemplate().getText();	
		return new ByteArrayInputStream(template.getBytes());
	}

	private String getQueryFileName() throws CancelException {
		String queryFileName = page.getFileName();

		Optional<String> optionalExtension = getFileExtension(queryFileName);
		if (!optionalExtension.isPresent()) {
			queryFileName = queryFileName + "." + EXTENSION;
		} else {
			String extension = optionalExtension.get();
			if (!extension.equals(EXTENSION)) {

				if (!isOtherExtensionOk(extension, EXTENSION)) {
					queryFileName = queryFileName.replace(extension, EXTENSION);
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
