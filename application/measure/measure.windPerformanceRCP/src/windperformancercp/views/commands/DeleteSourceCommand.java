package windperformancercp.views.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.model.sources.ISource;
import windperformancercp.views.IPresenter;
import windperformancercp.views.sources.ManageSourceView;

public class DeleteSourceCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windPerformanceRCP.DeleteSource";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		IPresenter presenter = ((ManageSourceView)page.findView("measure.windPerformanceRCP.sourceManagerView")).getPresenter();

		
		if(selection != null & selection instanceof IStructuredSelection){
			
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (Iterator<Object> iterator = (Iterator<Object>) strucSelection.iterator(); 
				iterator.hasNext();) {
				Object element = iterator.next();
				
				ISource source = (ISource) element;
				boolean answerOK = MessageDialog.openConfirm(parent,"Delete Confirmation" , 
						"Do you really want to delete source: "+source.getName()+"?\n" +
						"Warning: This could impact some of the power measurements also.");
				if(answerOK){
					presenter.fire(new InputDialogEvent(presenter,InputDialogEventType.DeleteSourceItem,source));
				}
			}
		}
		
		//TODO: evtl. anzeigen, welche PMs dann davon betroffen sind
		

		return null;
	}
}
