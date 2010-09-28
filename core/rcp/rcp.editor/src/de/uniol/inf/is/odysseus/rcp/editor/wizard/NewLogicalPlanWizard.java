package de.uniol.inf.is.odysseus.rcp.editor.wizard;

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

import de.uniol.inf.is.odysseus.rcp.editor.ILogicalPlanEditorConstants;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;

public class NewLogicalPlanWizard extends Wizard implements INewWizard {

	private NewLogicalPlanWizardPage page;
	
	public NewLogicalPlanWizard() {
		super();
		setWindowTitle("New Query Text");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page = new NewLogicalPlanWizardPage( getFolder((IResource)selection.getFirstElement()));
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
					ILogicalPlanEditorConstants.LOGICAL_PLAN_EDITOR_ID, 
					true);
			
			return true;
		} catch (Exception ex ) {
			new ExceptionWindow(ex);
			return false;
		}
	}
	
	protected InputStream getQueryTemplate() {
		return new ByteArrayInputStream("".getBytes());
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
