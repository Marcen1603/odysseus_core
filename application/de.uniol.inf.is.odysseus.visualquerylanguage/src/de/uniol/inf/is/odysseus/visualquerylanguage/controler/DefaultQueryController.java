package de.uniol.inf.is.odysseus.visualquerylanguage.controler;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Control;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.visualquerylanguage.ReflectionException;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.tabs.DefaultGraphArea;

public class DefaultQueryController implements IQueryController{

	private static final Logger log = LoggerFactory
			.getLogger(DefaultQueryController.class);

	public DefaultQueryController() {
		
	}

	@Override
	public void launchQuery(Control control, IAdvancedExecutor executor) throws ReflectionException {
		
		DefaultGraphArea area = null;
		ILogicalOperator root = null;
		
		if (control instanceof DefaultGraphArea) {
			area = (DefaultGraphArea) control;
		}

		int sinkCounter = 0;

		for (INodeModel<INodeContent> nodeModel : area.getController()
				.getModel().getNodes()) {
			if (nodeModel.getConnectionsAsStartNode().isEmpty()) {
				sinkCounter++;
			}
		}
		try {
			if (sinkCounter == 1) {
				root = createOperators(area, executor);
			}
			
			if (root != null) {
				area.setQueryID(executor.addQuery(root));
				
			} else {
				log.error("Root of the tree is null.");
			}
		} catch (Exception e) {
			throw new ReflectionException();
		}
	}

	private ILogicalOperator createOperators(DefaultGraphArea area,
			IExecutor executor){
		ArrayList<ILogicalOperator> opList = new ArrayList<ILogicalOperator>();
		ArrayList<INodeModel<INodeContent>> modelList = new ArrayList<INodeModel<INodeContent>>();
		ILogicalOperator logOp = null;
		WindowAO windowOp = null;
		
		ILogicalOperator root = null;
		
		for (INodeModel<INodeContent> nodeModel : area.getController()
				.getModel().getNodes()) {
			
			logOp = nodeModel.getContent().getOperator();
			modelList.add(nodeModel);
			
			if(logOp instanceof WindowAO) {
				windowOp = (WindowAO) logOp;
			}
			
			if(windowOp != null) {
				opList.add(windowOp);
			}else {
				opList.add(logOp);
			}
			if(nodeModel.getConnectionsAsStartNode().isEmpty()) {
				if(windowOp != null) {
					root = windowOp;
				}else {
					root = logOp;
				}
			}
		}
		
		if(!buildTree(opList, modelList)) {
			return null;
		}
		
		return root;
	}

	@SuppressWarnings("unchecked")
	private boolean buildTree(ArrayList<ILogicalOperator> operatorList, ArrayList<INodeModel<INodeContent>> modelList) {
		ArrayList<ILogicalOperator> startNodes = null;
		
		for (int i = 0; i<operatorList.size(); i++) {
			startNodes = new ArrayList<ILogicalOperator>();
				for (Object conn : modelList.get(i).getConnectionsAsEndNode()) {
					for (int j = 0; j<modelList.size(); j++) {
						if(modelList.get(j)==((IConnectionModel<INodeContent>)conn).getStartNode()) {
							startNodes.add(operatorList.get(j));
						}
					}
				}
				if(operatorList.get(i) instanceof UnaryLogicalOp) {
						operatorList.get(i).subscribeTo(startNodes.get(0),0,0);
						operatorList.get(i).setInputSchema(0, startNodes.get(0).getOutputSchema());
				}else if(operatorList.get(i) instanceof BinaryLogicalOp) {
					if(startNodes.get(0) instanceof AbstractLogicalOperator && startNodes.get(1) instanceof AbstractLogicalOperator) {
						((BinaryLogicalOp)operatorList.get(i)).setLeftInput((AbstractLogicalOperator)startNodes.get(0));
						((BinaryLogicalOp)operatorList.get(i)).setLeftInputSchema(((AbstractLogicalOperator)startNodes.get(0)).getOutputSchema());
						((BinaryLogicalOp)operatorList.get(i)).setRightInput((AbstractLogicalOperator)startNodes.get(1));
						((BinaryLogicalOp)operatorList.get(i)).setLeftInputSchema(((AbstractLogicalOperator)startNodes.get(1)).getOutputSchema());
					}
				}
		}
		return true;
	}
}
