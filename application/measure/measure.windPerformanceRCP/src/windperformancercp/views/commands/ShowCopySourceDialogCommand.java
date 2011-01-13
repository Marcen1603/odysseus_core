package windperformancercp.views.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import windperformancercp.model.sources.ISource;
import windperformancercp.views.AbstractUIDialog;
import windperformancercp.views.IPresenter;
import windperformancercp.views.SourceDialog;
import windperformancercp.views.SourceDialogPresenter;

public class ShowCopySourceDialogCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windPerformanceRCP.ShowCopySourceDialog";
		 
	    @Override
	    public Object execute(ExecutionEvent event) throws ExecutionException {
	    	
	    	ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		
	    	if(selection != null & selection instanceof IStructuredSelection){
				IStructuredSelection strucSelection = (IStructuredSelection) selection;
							
				for (Iterator<Object> iterator = (Iterator<Object>) strucSelection.iterator(); iterator.hasNext();) {
					Object element = iterator.next();
					ISource source = (ISource) element;
					
					Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
					final Shell dialogShell = new Shell(parent);
					AbstractUIDialog dialog = new SourceDialog(dialogShell);
					IPresenter presenter = dialog.getPresenter();
					dialog.create();					
					((SourceDialogPresenter) presenter).feedDialog(source);
					dialog.open();
								
					}
				return null;
	        }
	        if(selection == null){
	        	System.out.println("ShowCopySourceCommand: Selection was null!");
	        } else{
	        	System.out.println("ShowCopySourceCommand: Selection was not null but did not match!");
	        }

	        return null;
	    }

	/*@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//TODO: werte des values uebergeben, am besten als command parameter (source)
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

		final Shell dialogShell = new Shell(parent);
		SourceDialog dialog = new SourceDialog(dialogShell);
		//TODO: generate
		if(dialog.open()== Window.OK){
			System.out.println("Copy Source Handler says: Dialog says Ok!");
		}

		return null;
	}*/

}
