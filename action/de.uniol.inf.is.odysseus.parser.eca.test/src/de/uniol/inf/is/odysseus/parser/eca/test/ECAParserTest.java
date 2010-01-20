package de.uniol.inf.is.odysseus.parser.eca.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.action.operator.EventDetectionAO;
import de.uniol.inf.is.odysseus.action.operator.EventDetectionPO;
import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.IActionParameter;
import de.uniol.inf.is.odysseus.action.output.StaticParameter;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorFactory;
import de.uniol.inf.is.odysseus.action.services.actuator.PrimitivTypeComparator;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterParserID;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.console.ExecutorConsole;

/**
 * Testclass for ECAParser and its integration into the executer
 * @author Simon Flandergan
 *
 */
public class ECAParserTest implements CommandProvider {
	private IAdvancedExecutor executor;
	private ExecutorConsole executerConsole;
	private IActuatorFactory actuatorFactory;
	private ICompiler compiler;
	
	//set to false if u want to prevent removal of testActuators!
	private static boolean autoRemoveActuator = true;
	
	public void bindActuatorFactory (IActuatorFactory factory){
		this.actuatorFactory = factory;
	}
	
	public void bindConsole(CommandProvider cp){
		if(cp.getClass() == ExecutorConsole.class){
			this.executerConsole = (ExecutorConsole)cp;
		}
	}
	
	public void bindCompiler(ICompiler compiler){
		this.compiler = compiler;
	}
	
	public void bindExecutor(IAdvancedExecutor exec){
		this.executor = exec;
		
	}
	
	
	
	public void _testeca(CommandInterpreter ci){
		try {
			System.err.println("Preparing sources and actuators");
			//create nexmark sources
			executerConsole._nmsn(ci);
			
			//remove existing actuator
			try {
				this.actuatorFactory.removeActuator("ecatest1", "ActuatorAdapterManager");
			}catch(Exception e){}
			
			//create actuator
			this.actuatorFactory.createActuator("ecatest1", 
					"de.uniol.inf.is.odysseus.action.services.actuator.impl.TestActuator(name)", 
					"ActuatorAdapterManager");
			
			//1st testsuite
			System.err.println("--Testsuite1, Query: ON(Select * from nexmark:person2 RANGE 10000)" +
			"DO ActuatorAdapterManager.ecatest1.getName()--");
			this.runSingleActionTestSuite("ON(Select * from nexmark:person2)" +
					"DO ActuatorAdapterManager.ecatest1.getName()", new ArrayList<IActionParameter>());			
	
			//2nd testsuite
			ArrayList<IActionParameter> parameters = new ArrayList<IActionParameter>();
			parameters.add(new StaticParameter((byte)1));
			parameters.add(new StaticParameter(2.0d));
			parameters.add(new StaticParameter(3.0d));
			parameters.add(new StaticParameter(4));
			System.err.println("--Testsuite2, Query: ON(Select * from nexmark:person2 RANGE 10000)" +
			"DO ActuatorAdapterManager.ecatest1.doSomething" +
			"(1:byte, 2.0:double, 3.0:double, 4:int)");
			this.runSingleActionTestSuite("ON(Select * from nexmark:person2)" +
					"DO ActuatorAdapterManager.ecatest1.doSomething" +
					"(1:byte, 2.0:double, 3.0:double, 4:int)"
					, parameters);
			
		}catch (Exception e){
			System.err.print("Test failed: ");
			System.err.println(e.getMessage());
		}
		
	}

	@SuppressWarnings("unchecked")
	private void runSingleActionTestSuite(String query, List<IActionParameter> parameters) throws Exception {	
		List<ILogicalOperator> logicalPlan = this.compiler.translateQuery(query , "ECA");
		
		//check logical operator
		System.err.println("	*Testcase1: Check if top operator is eAO");
		ILogicalOperator eAO = logicalPlan.get(0);
		if (! (eAO.getClass() == EventDetectionAO.class)) {
			throw new Exception("EventDetectionAO is not top operator");
		}
		System.err.println("		++success, top operatortype is <"+EventDetectionAO.class+">");
		
		if (eAO.getSubscriptions().size() != 0 || eAO.getSubscribedToSource().size() != 1){
			throw new Exception("EventDetectionAO is not subscribed correctly");
		}
		System.err.println("		++success, eAO has no subscriptions and is subscribed to one source");
		
		//check actions
		System.err.println("	*Testcase2: Check if action bound to eAO is correct");
		Map<Action, List<IActionParameter>> actions = ((EventDetectionAO)eAO).getActions();
		if (actions.size()!= 1){
			throw new Exception("Incompatible number of actions bound");
		}
		Action action = actions.keySet().iterator().next();
		List<IActionParameter> parametersSet = actions.get(action);
		
		Iterator<IActionParameter> iterator1 = parameters.iterator();
		Iterator<IActionParameter> iterator2 = parametersSet.iterator();
		if (parameters.size() == parametersSet.size()){
			while (iterator1.hasNext()){
				IActionParameter param1 = iterator1.next();
				IActionParameter param2 = iterator2.next();
				if (param1.getType() != param2.getType()){
					throw new Exception("Type <"+param1.getType()+"> expected but type <"+param2.getType()+"> set.");
				}
				if (!PrimitivTypeComparator.sameType(param1.getParamClass(), param2.getParamClass())) {
					throw new Exception("Class <"+param1.getParamClass()+"> expected but class <"+param2.getParamClass()+"> set.");
				}
				if (!param1.getValue().equals(param2.getValue())){
					throw new Exception("Value <"+param1.getValue()+"> expected but value <"+param2.getValue()+"> set.");
				}
			}
		}else {
			throw new Exception("Incompatible number of parameters set");
		}
		
		System.err.println("		++success, number of actions & parameters is correct");
		
		//check physical operators
		System.err.println("	*Testcase3: Check if physical plan is correct");
		int queryID = this.executor.addQuery(logicalPlan.get(0), new ParameterParserID("ECA"));
		IPlan plan = this.executor.getSealedPlan();
		IQuery installedQuery = plan.getQuery(queryID);
		IPhysicalOperator physicalOp = installedQuery.getSealedRoot();
		if (! (physicalOp.getClass() == EventDetectionPO.class)){
			throw new Exception("Physical operator root is wrong class: <"+physicalOp.getClass()+">");
		}
		System.err.println("		++success, physical operator root class is correct");
		
		if (!((EventDetectionPO)physicalOp).getActions().equals(actions)){
			throw new Exception("Actions from physical and logical operator differ");
		}
		System.err.println("		++success, actions bound by physical operator are correct");
		
	}

	@Override
	public String getHelp() {
		return "\n---ECA Parser test--- \n" +
				"	testeca - runs eca test cases\n";
	}


}
