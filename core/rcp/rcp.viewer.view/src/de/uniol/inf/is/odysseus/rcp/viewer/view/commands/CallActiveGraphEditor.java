package de.uniol.inf.is.odysseus.rcp.viewer.view.commands;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.OdysseusModelProviderMultipleSink;
import de.uniol.inf.is.odysseus.rcp.viewer.view.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl.GraphViewEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl.PhysicalGraphEditorInput;

public class CallActiveGraphEditor extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
	
		
		IExecutor executor = ExecutorHandler.getExecutor();
		if( executor == null ) 
			return null;
		
	
		try {
			ArrayList<ISink<?>> sinkRoots = new ArrayList<ISink<?>>();
			for(IPhysicalOperator po : executor.getSealedPlan().getRoots() ) 
				sinkRoots.add((ISink<?>)po);

			IModelProvider<IPhysicalOperator> provider = new OdysseusModelProviderMultipleSink(sinkRoots);
			
			PhysicalGraphEditorInput input = new PhysicalGraphEditorInput(provider, "CurrentPlan");
			page.openEditor(input, GraphViewEditor.EDITOR_ID);

		} catch (Exception ex) {
			System.out.println(ex.getStackTrace());
		}

		return null;
	}

}
