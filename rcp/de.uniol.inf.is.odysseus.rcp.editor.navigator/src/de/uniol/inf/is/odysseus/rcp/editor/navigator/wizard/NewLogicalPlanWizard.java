package de.uniol.inf.is.odysseus.rcp.editor.navigator.wizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewLogicalPlanWizard extends Wizard implements INewWizard {

	private NewLogicalPlanWizardPage page;
	private IContainer root;
	ArrayList<String> usedNames = new ArrayList<String>();
	
	
	public NewLogicalPlanWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		root = null;
		Object selected = selection.getFirstElement();
		if( selected instanceof IContainer ) 
			root = (IContainer)selected;
		else if( selected instanceof IFile ) 
			root = ((IFile)selected).getParent();
		
		try {
			for( IResource file : root.members()) {
				usedNames.add(file.getName());
			}
		} catch (CoreException e) {}
		
	}
	
	@Override
	public void addPages() {
		page = new NewLogicalPlanWizardPage(usedNames);
		
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		String name = page.getName();
		
		String fileName = name + ".pln";
		
		Path path = new Path(fileName);
		IFile newFile = root.getFile(path);
		try {
			byte[] bytes = "".getBytes();
		    InputStream source = new ByteArrayInputStream(bytes);
			newFile.create(source, IResource.NONE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
