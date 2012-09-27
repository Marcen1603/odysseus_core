/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.editor.text.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;

public class OdysseusScriptWizard extends Wizard implements INewWizard {

	private OdysseusScriptWizardPage page;

	public OdysseusScriptWizard() {
		super();
		setWindowTitle("New Odysseus Script");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page = new OdysseusScriptWizardPage("New Odysseus Script", selection);
	}

	@Override
	public void addPages() {
		addPage(page);
	}

	@Override
	public boolean performFinish() {

		try {
			String queryFileName = getQueryFileName();

			IPath path = page.getContainerFullPath().append(queryFileName);
			IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			newFile.create(getQueryTemplate(), IResource.NONE, null);

			// open editor
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(newFile), OdysseusRCPEditorTextPlugIn.ODYSSEUS_SCRIPT_EDITOR_ID, true);

			return true;
		} catch (CancelException ex) {
			return false;
		} catch (Exception ex) {
			new ExceptionWindow(ex);
			return false;
		}
	}

	protected InputStream getQueryTemplate() {
		String template = 
				"///OdysseusScript\n" +
				"#PARSER CQL\n" +
				"#TRANSCFG Standard\n" +
				"#RUNQUERY\n" +
				"///Your first query here";		
		return new ByteArrayInputStream(template.getBytes());
	}

	private String getQueryFileName() throws CancelException {
		String queryFileName = page.getFileName();

		Optional<String> optionalExtension = getFileExtension(queryFileName);
		if (!optionalExtension.isPresent()) {
			queryFileName = queryFileName + "." + OdysseusRCPEditorTextPlugIn.QUERY_TEXT_EXTENSION;
		} else {
			String extension = optionalExtension.get();
			if (!extension.equals(OdysseusRCPEditorTextPlugIn.QUERY_TEXT_EXTENSION)) {

				if (!isOtherExtensionOk(extension, OdysseusRCPEditorTextPlugIn.QUERY_TEXT_EXTENSION)) {
					queryFileName = queryFileName.replace(extension, OdysseusRCPEditorTextPlugIn.QUERY_TEXT_EXTENSION);
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
