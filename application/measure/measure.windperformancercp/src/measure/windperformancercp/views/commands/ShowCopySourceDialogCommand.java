package measure.windperformancercp.views.commands;

import java.util.Iterator;

import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.model.sources.MetMast;
import measure.windperformancercp.model.sources.WindTurbine;
import measure.windperformancercp.views.AbstractUIDialog;
import measure.windperformancercp.views.IPresenter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import measure.windperformancercp.views.sources.SourceDialog;
import measure.windperformancercp.views.sources.SourceDialogPresenter;

public class ShowCopySourceDialogCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windperformancercp.ShowCopySourceDialog";
		 
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
					
					if(source instanceof MetMast){					
						MetMast mm = new MetMast((MetMast)source);
						((SourceDialogPresenter) presenter).feedDialog(mm);
					}
					else{
						if(source instanceof WindTurbine){
							WindTurbine wt = new WindTurbine((WindTurbine)source);
							((SourceDialogPresenter) presenter).feedDialog(wt);
						}
						else return null;
					}

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

}
