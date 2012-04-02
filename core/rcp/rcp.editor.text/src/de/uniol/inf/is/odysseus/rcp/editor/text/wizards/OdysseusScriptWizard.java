/** Copyright [2011] [The Odysseus Team]
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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

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
		page = new OdysseusScriptWizardPage( getFolder((IResource)selection.getFirstElement()));
	}
	
	@Override
	public void addPages() {
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		String queryName = page.getFullQueryName();
		IContainer container = page.getFileContainer();
		
		try {
			IPath path = container.getFullPath().append(queryName);
			IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			newFile.create(getQueryTemplate(), IResource.NONE, null);

			// open editor
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(
					new FileEditorInput(newFile), 
					OdysseusRCPEditorTextPlugIn.ODYSSEUS_SCRIPT_EDITOR_ID, 
					true);
			
			return true;
		} catch (Exception ex ) {
			new ExceptionWindow(ex);
			return false;
		}
	}
	
	protected InputStream getQueryTemplate() {
		return new ByteArrayInputStream("///OdysseusScript\n#PARSER CQL\n#TRANSCFG Standard\n#QUERY\n///Your first query here".getBytes());
	}

	private static IContainer getFolder( IResource resource ) {
		if( resource instanceof IContainer ) 
			return (IContainer)resource;
		if( resource instanceof IFile ) {
			IFile file = (IFile)resource;
			return file.getParent();
		}
		throw new IllegalArgumentException("unknown resource-type:" + resource.getClass().getName());
	}
}
