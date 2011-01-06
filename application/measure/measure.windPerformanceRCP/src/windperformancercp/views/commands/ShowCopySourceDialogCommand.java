package windperformancercp.views.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class ShowCopySourceDialogCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windPerformanceRCP.ShowCopySourceDialog";
		 
	    @Override
	    public Object execute(ExecutionEvent event) throws ExecutionException {
	        ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
	                .getActivePage().getSelection();
	        
	        if (selection != null & selection instanceof IStructuredSelection) {
	            IStructuredSelection strucSelection = (IStructuredSelection) selection;
	            System.out.println("Structuredselection: "+strucSelection.getFirstElement().toString());
	        /*    MyObject o = (MyObject ) strucSelection.getFirstElement();
	        
	            ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);

	            try {
	            	Map<String, Object> parameters= new HashMap<String, Object>();
	            	parameters.put("key.des.parameter", deinParameterObject);
	            	 
	            	Command command = commandService.getCommand("measure.windPerformanceRCP.ShowNewSourceDialog");
	            	ExecutionEvent newEvent = new ExecutionEvent(command, parameters, null, null);
	            	command.executeWithChecks(newEvent);

	            } catch (Exception ex) {
	                throw new RuntimeException("add.command");
	            }*/
	            
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
