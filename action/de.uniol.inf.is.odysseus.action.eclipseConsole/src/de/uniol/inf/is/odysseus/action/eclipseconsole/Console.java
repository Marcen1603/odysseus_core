package de.uniol.inf.is.odysseus.action.eclipseconsole;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorFactory;
import de.uniol.inf.is.odysseus.action.services.actuator.ActionMethod;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

public class Console implements	org.eclipse.osgi.framework.console.CommandProvider {
	private IAdvancedExecutor executer;

	public void bindExecuter(IAdvancedExecutor executer){
		this.executer = executer;
	}
	
	public void _addActionQuery(CommandInterpreter ci){
		String args[] = this.extractArgument(ci);
		try {
			//FIXME korrekte parameter setzen
			this.executer.addQuery(args[0], "ECA", null);
		} catch (PlanManagementException e) {
			ci.println(e.getMessage());
		}
	}
	
	public void _createactuator(CommandInterpreter ci){
		String args[] = this.extractArgument(ci);
		if (args.length < 3){
			ci.println("Insufficient number of args. You must provide: <managerName>, " +
					"<actuatorName> and <description>");
		}
		String managerName = args[0];
		String actuatorName = args[1];
		String description = args[2];
		try {
			ActuatorFactory.getInstance().createActuator(actuatorName, description, managerName);
			ci.println("<"+managerName+"> created <"+actuatorName+"> successfully");
		} catch (ActuatorException e) {
			ci.println(e.getMessage());
		}
	}
	
	public void _lsactuatormanager(CommandInterpreter ci) {
		ci.println("--Registered ActuatorManager--");
		for (String name : ActuatorFactory.getInstance().getActuatorManager().keySet()){
			ci.println(" + "+name);
		}
	}
	
	public void _showschema(CommandInterpreter ci) {
		String args[] = this.extractArgument(ci);
		String managerName = args[0];
		String actuatorName = args[1];
		try {
			List<ActionMethod> schema = ActuatorFactory.getInstance().getSchema(actuatorName, managerName);
			ci.println("--Schema of <"+actuatorName+">--");
			for (ActionMethod method : schema){
				ci.println("+"+method.getName());
				for (Class<?>c : method.getParameterTypes()){
					ci.println(" *"+c.getName());
				}
				ci.println();
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
		// TODO Auto-generated method stub
		return null;
	}

}
