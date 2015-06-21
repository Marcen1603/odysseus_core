package de.uniol.inf.is.odysseus.query.transformation.viewer.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.query.transformation.viewer.gui.ExportQueryDialog;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.commands.AbstractQueryCommand;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;

public class ExportQueryCommand extends AbstractQueryCommand {

	private static final Logger LOG = LoggerFactory.getLogger(ExportQueryCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Object> selections = SelectionProvider.getSelection(event);
	//	List<String> texts = new ArrayList<>();				
		for (Object selection : selections) {
			IExecutor executor = OdysseusRCPPlugIn.getExecutor();
			if (executor != null) {
				int queryId = (Integer)selection;
		
				ExportQueryDialog transformationDialog = new ExportQueryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), queryId);
				transformationDialog.open();
				
				/*
				
				ILogicalQuery query = executor.getLogicalQueryById(id, OdysseusRCPPlugIn.getActiveSession());
				
				ILogicalOperator logicalPlan = query.getLogicalPlan();
				String queryText = query.getQueryText();
				
				walkThroughLogicalPlan(logicalPlan);
		
				logicalPlan.getSubscribedToSource();
			
		
			
				for(LogicalSubscription  operator : logicalPlan.getSubscribedToSource()){
					operator.getTarget();
					
				}
			

				//LogicalPlan an eigenes Bundel übergeben 
			
				texts.add(queryText);	
				*/			
			} else {
				LOG.error("Executor is not set");
			}						
		}
		return null;
	}
	
	private void walkThroughLogicalPlan(ILogicalOperator topAO){
		System.out.println("Operator-Name: "+topAO.getName());

		for(LogicalSubscription  operator : topAO.getSubscribedToSource()){
			System.out.println("Operator-Name: "+operator.getTarget().getName());
				
			for(LogicalSubscription  operator2 : operator.getTarget().getSubscribedToSource()){
				walkThroughLogicalPlan(operator2.getTarget());
				
			}
			
		
		}
		
	}

}
