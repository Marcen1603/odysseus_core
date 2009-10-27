package de.uniol.inf.is.odysseus.visualquerylanguage.controler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

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
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParam;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamSetter;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.tabs.DefaultGraphArea;

public class DefaultQueryController implements IQueryController{

	private static final Logger log = LoggerFactory
			.getLogger(DefaultQueryController.class);

	public DefaultQueryController() {
		
	}

	@SuppressWarnings("unchecked")
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
			for (IParamSetter<?> param : nodeModel.getContent().getSetterParameterList()) {
				try {
					Object value = getParamValue(getParamClasses(param), param.getValue(), param.getTypeList().size()-1);
					Class paramClass = Class.forName(param.getType());
					Class opClass = nodeModel.getContent().getOperator().getClass();
					Class[] pClass = new Class[1];
					pClass[0] = paramClass;
					Method m = opClass.getMethod(param.getSetter(), pClass);
					m.invoke(value);
				} catch (Exception e) {
					log.error("While trying to create Parameters.");
					e.printStackTrace();
				}
			}
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
		
		ILogicalOperator root = null;
		
		for (INodeModel<INodeContent> nodeModel : area.getController()
				.getModel().getNodes()) {
			
			logOp = nodeModel.getContent().getOperator();
			modelList.add(nodeModel);
			
			opList.add(logOp);

			if(nodeModel.getConnectionsAsStartNode().isEmpty()) {
					root = logOp;
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
	

	@SuppressWarnings("unchecked")
	private Class[] getParamClasses(IParam<?> param) {
		Class paramTypes[] = new Class[param.getTypeList().size()];
		Object paramArray[] = param.getTypeList().toArray();
		for (int i = paramArray.length - 1; i >= 0; i--) {
			try {
				paramTypes[i] = Class.forName((String)paramArray[i]);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return paramTypes;
	}


	@SuppressWarnings("unchecked")
	private Object getParamValue(Class[] classes, Object value, int index)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor con = null;
		con = classes[index-1].getConstructor(classes[index]);
		Object[] argList =  new Object[1];
		argList[0] = value;
		Object object = con.newInstance(argList);
		if(index-1 != 0) {
			object = getParamValue(classes, object, --index);
		}
		return object;
	}
}
