package de.uniol.inf.is.odysseus.action.eclipseconsole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import de.uniol.inf.is.odysseus.action.services.actuator.ActionMethod;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorFactory;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorManager;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterParserID;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

/**
 * Extension for the Equinox OSGI Console providing commands to access
 * action & actuator functions
 * @author Simon Flandergan
 *
 */
public class Console implements	org.eclipse.osgi.framework.console.CommandProvider {
	private IAdvancedExecutor executer;
	private IActuatorFactory actuatorFactory;

	public void bindActuatorFactory(IActuatorFactory factory){
		this.actuatorFactory = factory;
	}
	
	public void bindExecutor(IAdvancedExecutor executer){
		this.executer = executer;
	}
	
	
	public void _addactionquery(CommandInterpreter ci){
		String args[] = this.extractArgument(ci);
		if (args.length < 1){
			ci.println("Insufficient number of arguments. You must provide: <queryDescription>");
			return;
		}
		
		try {
			Collection<Integer> ids = this.executer.addQuery(args[0], "ECA", new ParameterParserID("ECA"));
			ci.println("Query installed successfully. QueryID is <"+ids.iterator().next()+">");
		} catch (PlanManagementException e) {
			ci.println(e.getMessage());
		}
	}
	
	public void _createactuator(CommandInterpreter ci){
		String args[] = this.extractArgument(ci);
		if (args.length < 3){
			ci.println("Insufficient number of arguments. You must provide: <managerName>, " +
					"<actuatorName> and <description>");
			return;
		}
		
		String managerName = args[0];
		String actuatorName = args[1];
		String description = args[2];
		try {
			this.actuatorFactory.createActuator(actuatorName, description, managerName);
			ci.println("<"+managerName+"> created <"+actuatorName+"> successfully");
		} catch (ActuatorException e) {
			ci.println(e.getMessage());
		}
	}
	
	public void _lsactuatormanager(CommandInterpreter ci) {
		ci.println("--Registered ActuatorManager--");
		for (String name : this.actuatorFactory.getActuatorManagers().keySet()){
			ci.println(" + "+name);
		}
	}
	
	public void _lsactuators(CommandInterpreter ci){
		String[] args = this.extractArgument(ci);
		if (args.length<1){
			ci.println("Insufficient number of arguments. You must provide: <managerName>");
			return;
		}
		
		String managerName = args[0];
		IActuatorManager manager = this.actuatorFactory.getActuatorManagers().get(managerName);
		if (manager != null){
			ci.println("Actuators registered at <"+managerName+">:");
			for (String actuator : manager.getRegisteredActuatorNames()){
				ci.println(" + "+actuator);
			}
		}else {
			ci.println("<"+managerName+"> does not exist.");
		}
	}
	
	public void _removeactuator(CommandInterpreter ci){
		String[] args = this.extractArgument(ci);
		if (args.length < 2){
			ci.println("Insufficient number of arguments. You must provide: <managerName>" +
			"and <actuatorName>");
			return;
		}
		
		String managerName = args[0];
		String actuatorName = args[1];
		try {
			this.actuatorFactory.removeActuator(actuatorName, managerName);
		} catch (ActuatorException e) {
			ci.println(e.getMessage());
		}
	}
	
	public void _showschema(CommandInterpreter ci) {
		String args[] = this.extractArgument(ci);
		if (args.length <2){
			ci.println("Insufficient number of arguments. You have to provide: <managerName>" +
					" and <actuatorName");
			return;
		}
		
		String managerName = args[0];
		String actuatorName = args[1];
		try {
			List<ActionMethod> schema = this.actuatorFactory.getSchema(actuatorName, managerName);
			ci.println("--Schema of <"+actuatorName+">--");
			for (ActionMethod method : schema){
				ci.print("+"+method.getName()+"(");
				int ctr = 0;
				for (Class<?>c : method.getParameterTypes()){
					if (ctr > 0){
						ci.print(", ");
					}
					ci.print(c.getName());
					ctr++;
				}
				ci.println(")");
			}
		}catch (ActuatorException e){
			ci.println(e.getMessage());
		}
	}
	
	
	private String[] extractArgument(CommandInterpreter ci) {
		ArrayList<String> args = new ArrayList<String>(); 
		String argPart = ci.nextArgument();
		while (argPart != null && argPart.length()>0){
			args.add(argPart);
			argPart = ci.nextArgument();
		}
		return args.toArray(new String[args.size()]);
	}

	@Override
	public String getHelp() {
		return "\n---Action-Functions---\n" +
				"	addactionquery '<queryDescription>' - adds new action query\n" +
				"	createactuator <managerName> <actuatorName> <actuatorDescription> " +
				"- triggers specified manager to create a new actuator\n" +
				"	lsactuatormanager - lists all registered actuator managers\n" +
				"	removeactuator <managerName> <actuatorName> - triggers specified " +
				"manager to remove declared actuator\n" +
				"	showschema <managerName> <actuatorName> - shows all methods provided by" +
				"the specified actuator\n";
	}

}
