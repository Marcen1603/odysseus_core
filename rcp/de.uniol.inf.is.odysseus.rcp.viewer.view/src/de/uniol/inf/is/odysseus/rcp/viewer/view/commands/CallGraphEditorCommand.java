package de.uniol.inf.is.odysseus.rcp.viewer.view.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.OdysseusModelProviderSinkOneWay;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl.GraphViewEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl.PhysicalGraphEditorInput;

public class CallGraphEditorCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		
		ISelection selection = window.getSelectionService().getSelection();
		if( selection instanceof IStructuredSelection ) {
			IStructuredSelection treeSelection = (IStructuredSelection)selection;
			Object obj = treeSelection.getFirstElement();
						
			// Auswahl holen
			List<IPhysicalOperator> graph = null;
			if( obj instanceof IQuery ) {
				IQuery query = (IQuery)obj;
				graph = query.getRoots();
			
				ISink<?> sink = (ISink<?>)graph.get(0); 
				IModelProvider<IPhysicalOperator> provider = new OdysseusModelProviderSinkOneWay(sink, query);
				PhysicalGraphEditorInput input = new PhysicalGraphEditorInput(provider, "Query " + query.getID());
				
				try {
					page.openEditor(input, GraphViewEditor.EDITOR_ID);
					
				} catch( PartInitException ex ) {
					System.out.println(ex.getStackTrace());
				}
			}
		}
		
		return null;
	}

}
