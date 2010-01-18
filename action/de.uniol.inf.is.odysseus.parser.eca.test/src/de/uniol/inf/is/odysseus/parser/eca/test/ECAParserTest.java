package de.uniol.inf.is.odysseus.parser.eca.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.action.operator.EventDetectionAO;
import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.IActionParameter;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorFactory;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
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
			System.out.println("--Testsuite1, Query: ON(Select * from nexmark:person2)" +
			"DO ActuatorAdapterManager.ecatest1.getName()--");
			this.runTestSuite("ON(Select * from nexmark:person2)" +
					"DO ActuatorAdapterManager.ecatest1.getName()", new ArrayList<IActionParameter>());			

		}catch (Exception e){
			System.err.print("Test failed: ");
			System.err.println(e.getMessage());
		}
		
	}

	private void runTestSuite(String query, List<IActionParameter> parameters) throws Exception {	
		List<ILogicalOperator> logicalPlan = this.compiler.translateQuery(query , "ECA");
		
		//check logical operator
		System.out.println("	*Testcase1: Check if top operator is eAO");
		ILogicalOperator eAO = logicalPlan.get(0);
		if (! (eAO.getClass() == EventDetectionAO.class)) {
			throw new Exception("EventDetectionAO is not top operator");
		}
		System.out.println("		++success, top operatortype is <"+EventDetectionAO.class+">");
		
		if (eAO.getSubscriptions().size() != 0 || eAO.getSubscribedToSource().size() != 1){
			throw new Exception("EventDetectionAO is not subscribed correctly");
		}
		System.out.println("		++success, eAO has no subscriptions and is subscribed to one source");
		
		//check actions
		System.out.println("	*Testcase2: Check if action bound to eAO is correct");
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
				if (
						param1.getType() != param2.getType() ||
						param1.getParamClass() != param2.getParamClass() ||
						param1.getValue() != param2.getValue()){
					throw new Exception("One or more parameter is incompatible");
				}
			}
		}else {
			throw new Exception("Incompatible number of parameters set");
		}
		
		System.out.println("		++success, number of actions & parameters is correct");
		
		action.executeMethod(null);
		System.out.println("		++success, action can be executed properly");
		
		//check physical operators
		this.executor.addQuery(logicalPlan.get(0), new ParameterParserID("ECA"));
	}

	@Override
	public String getHelp() {
		return "\n---ECA Parser test--- \n" +
				"	testeca - runs eca test cases\n";
	}


}
