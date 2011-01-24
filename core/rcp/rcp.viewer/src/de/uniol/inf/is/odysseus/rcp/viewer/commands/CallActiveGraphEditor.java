package de.uniol.inf.is.odysseus.rcp.viewer.commands;

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
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.impl.PhysicalGraphEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.OdysseusModelProviderMultipleSink;

public class CallActiveGraphEditor extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
	
		
		IExecutor executor = OdysseusRCPViewerPlugIn.getExecutor();
		if( executor == null ) 
			return null;
		
	
		try {
			ArrayList<ISink<?>> sinkRoots = new ArrayList<ISink<?>>();
			for(IPhysicalOperator po : executor.getPlan().getRoots() ) 
				sinkRoots.add((ISink<?>)po);

			IModelProvider<IPhysicalOperator> provider = new OdysseusModelProviderMultipleSink(sinkRoots);
			
			PhysicalGraphEditorInput input = new PhysicalGraphEditorInput(provider, "CurrentPlan");
			page.openEditor(input, OdysseusRCPViewerPlugIn.GRAPH_EDITOR_ID);

		} catch (Exception ex) {
			ex.getStackTrace();
		}

		return null;
	}

}
