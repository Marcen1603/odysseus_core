package de.uniol.inf.is.odysseus.rcp.editor.navigator.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

public class DeleteCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object[] selections = Helper.getSelection(event);

		if( selections == null || selections.length == 0 || !(selections[0] instanceof IResource))
			return null;
		
		IResource resource = (IResource)selections[0];
		MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING |SWT.YES | SWT.NO);
	    box.setMessage("Do you really want to delete " + resource.getName() );
    	box.setText("Delete resource");
	          
		if( box.open() == SWT.YES ) {
			try {
				resource.delete(true, null);
			} catch (CoreException e) {}
		}
		
		return null;
	}

}
