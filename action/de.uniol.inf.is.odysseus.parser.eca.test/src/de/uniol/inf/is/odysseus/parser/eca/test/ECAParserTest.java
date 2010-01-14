package de.uniol.inf.is.odysseus.parser.eca.test;

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
			
			//create actuator
			this.actuatorFactory.createActuator("ecatest1", 
					"de.uniol.inf.is.odysseus.action.services.actuator.impl.TestActuator(name)", 
					"ActuatorAdapterManager");
			
			this.runTestSuite1();			

		}catch (Exception e){
			System.err.print("Test failed: ");
			System.err.println(e.getMessage());
		}
		
	}

	private void runTestSuite1() throws Exception {
		System.out.println("--Testsuite1, Query: ON(Select * from nexmark:person2)" +
		"DO ActuatorAdapterManager.ecatest1.getName()--");
		List<ILogicalOperator> logicalPlan = this.compiler.translateQuery(
				"ON(Select * from nexmark:person2)" +
				"DO ActuatorAdapterManager.ecatest1.getName()", "ECA");
		
		System.out.println("	*Testcase1: Check if top operator is eAO");
		ILogicalOperator eAO = logicalPlan.get(0);
		if (! (eAO.getClass() == EventDetectionAO.class)) {
			throw new Exception("EventDetecionAO is not top operator");
		}
		System.out.println("		++success, top operatortype is <"+EventDetectionAO.class+">++");
		
		System.out.println("	*Testcase2: Check if action bound to eAO is correct");
		Map<Action, List<IActionParameter>> actions = ((EventDetectionAO)eAO).getActions();
		if (actions.size()!= 1){
			throw new Exception("Incompatible number of actions bound");
		}
		Action action = actions.keySet().iterator().next();
		List<IActionParameter> parameters = actions.get(action);
		if (parameters.size() != 0){
			throw new Exception("Incompatible number of parameters");
		}
		System.out.println("		++success, number of actions & parameters is correct");
		
		action.executeMethod(null);
		System.out.println("		++success, action can be executed properly");
		
		System.out.println(eAO);
	}

	@Override
	public String getHelp() {
		return "\n---ECA Parser test--- \n" +
				"	testeca - runs eca test cases\n";
	}


}
