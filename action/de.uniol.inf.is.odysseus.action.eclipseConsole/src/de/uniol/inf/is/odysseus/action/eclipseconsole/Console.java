package de.uniol.inf.is.odysseus.action.eclipseconsole;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorFactory;
import de.uniol.inf.is.odysseus.action.services.actuator.ActionMethod;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterParserID;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

public class Console implements	org.eclipse.osgi.framework.console.CommandProvider {
	private IAdvancedExecutor executer;

	public void bindExecuter(IAdvancedExecutor executer){
		this.executer = executer;
	}
	
	public void _addactionquery(CommandInterpreter ci){
		String args[] = this.extractArgument(ci);
		if (args.length < 1){
			ci.println("Insufficient number of arguments. You must provide: <queryDescription>");
			return;
		}
		
		try {
			this.executer.addQuery(args[0], "ECA", new ParameterParserID("ECA"));
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
		if (args.length <2){
			ci.println("Insufficient number of arguments. You have to provide: <managerName>" +
					" and <actuatorName");
			return;
		}
		
		String managerName = args[0];
		String actuatorName = args[1];
		try {
			List<ActionMethod> schema = ActuatorFactory.getInstance().getSchema(actuatorName, managerName);
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
				"	showschema <managerName> <actuatorName> - shows all methods provided by" +
				"the specified actuator\n";
	}

}
