package de.uniol.inf.is.odysseus.visualquerylanguage.controler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.widgets.Control;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamConstruct;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamSetter;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.query.DefaultQuery;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.tabs.DefaultGraphArea;

public class DefaultQueryController implements IQueryController<DefaultQuery> {

	private static final Logger log = LoggerFactory
			.getLogger(DefaultQueryController.class);

	private Collection<DefaultQuery> queries;

	public DefaultQueryController() {
		this.queries = new ArrayList<DefaultQuery>();
	}

	@Override
	public Collection<DefaultQuery> getAllQueries() {
		return this.queries;
	}

	@Override
	public void addQuery(DefaultQuery query) {
		this.queries.add(query);
	}

	@Override
	public DefaultQuery getQueryById(int id) {
		for (DefaultQuery query : this.queries) {
			if (query.getId() == id) {
				return query;
			}
		}
		return null;
	}

	@Override
	public void removeQuery(DefaultQuery query) {
		for (DefaultQuery dQuery : this.queries) {
			if (dQuery == query) {
				this.queries.remove(query);
				break;
			} else {
				log.info("Query not in list.");
			}
		}
	}

	@Override
	public void launchQuery(Control control, IExecutor executor) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
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
		if (sinkCounter == 1) {
			root = createOperators(area, executor);
		}

		try {
			if (root != null) {
				executor.addQuery((ILogicalOperator) root,
						new ParameterDefaultRoot(null));
			} else {
				log.error("Root of the tree is null.");
			}
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private ILogicalOperator createOperators(DefaultGraphArea area,
			IExecutor executor) throws ClassNotFoundException,
			SecurityException, NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		ArrayList<ILogicalOperator> opList = new ArrayList<ILogicalOperator>();
		ArrayList<INodeModel<INodeContent>> modelList = new ArrayList<INodeModel<INodeContent>>();
		Class clazz = null;
		Class[] constructParameters = null;
		Constructor con = null;
		ILogicalOperator logOp = null;
		WindowAO windowOp = null;

		ArrayList<Object> parameterValues = null;
		Object paramObj = new Object();
		INodeContent content = null;
		ILogicalOperator root = null;
		
		for (INodeModel<INodeContent> nodeModel : area.getController()
				.getModel().getNodes()) {

			clazz = Class.forName(nodeModel.getContent().getType());

			content = nodeModel.getContent();
			modelList.add(nodeModel);

			if (!content.getConstructParameterList().isEmpty()) {
				constructParameters = new Class[content
						.getConstructParameterList().size()];
				parameterValues = new ArrayList<Object>();
				for (IParamConstruct<?> param : content
						.getConstructParameterList()) {
					Class paramClazz = null;
					paramClazz = Class.forName(param.getType());
					paramObj = param.getValue();
					parameterValues.add(paramObj);
					constructParameters[param.getPosition()] = paramClazz;
				}
				con = clazz.getConstructor(constructParameters);
				if (content.isOnlySource() && parameterValues.size() == 1
						&& parameterValues.get(0) instanceof String) {
					SDFSource source = DataDictionary.getInstance().getSource(
							(String) parameterValues.get(0));
					logOp = (ILogicalOperator) con.newInstance(source);
				} else {
					if(clazz.newInstance() instanceof WindowAO) {
						windowOp = (WindowAO) con.newInstance(parameterValues);
					}else {
						logOp = (ILogicalOperator) con.newInstance(parameterValues);
					}
					
				}
			} else {
				if(clazz.newInstance() instanceof WindowAO) {
					windowOp = (WindowAO) clazz.newInstance();
				}else {
					logOp = (ILogicalOperator) clazz.newInstance();
				}
			}
			for (IParamSetter<?> param : content.getSetterParameterList()) {
				if(param.getValue() != null) {
					Method method = logOp.getClass().getMethod(param.getSetter(), new Class[] {param.getValue().getClass()});
					method.invoke(logOp, new Object[]{param.getValue()});
				}
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
					if(startNodes.size() != 1) {
						return false;
					}
						operatorList.get(i).setInputAO(0, startNodes.get(0));
						operatorList.get(i).setInputSchema(0, startNodes.get(0).getOutputSchema());
				}else if(operatorList.get(i) instanceof BinaryLogicalOp) {
					if(startNodes.size() != 2) {
						return false;
					}
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
