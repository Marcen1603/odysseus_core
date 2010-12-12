package de.uniol.inf.is.odysseus.cep.cepviewer.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.epa.CepOperator;

public class AddCommand extends AbstractHandler implements IHandler {

	@SuppressWarnings("unchecked")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = window.getActivePage().getSelection();
		
		if (selection == null) {
			System.out.println("null");
			return null;
		}
		
		CepOperator operator= null;
		CEPListView view = null; 
		
		if (selection instanceof IStructuredSelection) {
			
			IStructuredSelection structSelection = (IStructuredSelection) selection;
			Object selectedObject = structSelection.getFirstElement();
			
			try {
				PlatformUI.getWorkbench().showPerspective("de.uniol.inf.is.odysseus.cep.cepviewer.cepviewer", window);
				for(IViewReference a : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences()) {
					if(a.getId().equals("de.uniol.inf.is.odysseus.cep.cepviewer.listview")) {
						view = (CEPListView) a.getView(false);
					}
				}
				
			} catch (WorkbenchException e) {
				e.printStackTrace();
			}
			
			if( selectedObject instanceof CepOperator ) {
				operator = (CepOperator)selectedObject;
				view.addStateMaschine(operator);	
			} else {
				System.out.println("No CepOperator found");
			}

		} 

		return null;
	}

}
