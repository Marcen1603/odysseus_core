package de.uniol.inf.is.odysseus.parser.eca.test;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.action.services.actuator.IActuator;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorFactory;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterParserID;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.console.ExecutorConsole;

public class ECAParserTest implements CommandProvider {
	private IAdvancedExecutor executor;
	private ExecutorConsole executerConsole;
	private IActuatorFactory actuatorFactory;
	
	
	public void bindActuatorFactory (IActuatorFactory factory){
		this.actuatorFactory = factory;
	}
	
	public void bindExecutor(IAdvancedExecutor exec){
		this.executor = exec;
		
	}
	
	public void bindConsole(CommandProvider cp){
		if(cp.getClass() == ExecutorConsole.class){
			this.executerConsole = (ExecutorConsole)cp;
		}
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
			
			//install query
			this.executor.addQuery(
					"ON(Select * from nexmark:person2)" +
					"DO ActuatorAdapterManager.ecatest1.getName()", "ECA", new ParameterParserID("ECA"));

		}catch (Exception e){
			System.err.print("Test failed: ");
			System.err.println(e.getMessage());
		}
		
	}

	@Override
	public String getHelp() {
		return "\n---ECA Parser test--- \n" +
				"	testeca - runs eca test cases\n";
	}


}
