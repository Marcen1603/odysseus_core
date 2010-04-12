package de.uniol.inf.is.odysseus.action.eclipseconsole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import de.uniol.inf.is.odysseus.action.benchmark.IActuatorBenchmark;
import de.uniol.inf.is.odysseus.action.services.actuator.ActionMethod;
import de.uniol.inf.is.odysseus.action.services.actuator.ActionParameter;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorFactory;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorManager;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;
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
	private IActuatorBenchmark benchmark;

	public void _addactionquery(CommandInterpreter ci){
		String args[] = this.extractArgument(ci);
		if (args.length < 1){
			ci.println("Insufficient number of arguments. You must provide: <queryDescription>");
			return;
		}
		
		try {
			Collection<Integer> ids = this.executer.addQuery(args[0], "ECA");
			ci.println("Query installed successfully. QueryID is <"+ids.iterator().next()+">");
		} catch (Exception e) {
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
	
	public void _ideaalsources(CommandInterpreter ci){
		List<String> queries = new ArrayList<String>();
		queries.add("CREATE STREAM ideaal:bett4in1 (" +
				"timestamp Long, id Integer, weight0 Double, weight1 Double, weight2 Double, weight3 Double)" +
				"CHANNEL localhost : 55560");
		queries.add("CREATE STREAM ideaal:bett1 (" +
				"timestamp Long, id Integer, weight0 Double)" +
				"CHANNEL localhost : 55561");
		queries.add("CREATE STREAM ideaal:bett2 (" +
				"timestamp Long, id Integer, weight0 Double)" +
				"CHANNEL localhost : 55562");
		queries.add("CREATE STREAM ideaal:bett3 (" +
				"timestamp Long, id Integer, weight0 Double)" +
				"CHANNEL localhost : 55563");
		queries.add("CREATE STREAM ideaal:bett4 (" +
				"timestamp Long, id Integer, weight0 Double)" +
				"CHANNEL localhost : 55564");
		for (String query : queries){
			try {
				this.executer.addQuery(query, "CQL");
			} catch (PlanManagementException e) {
				ci.println(e.getMessage());
			}
		}
	}
	
	
	public void _lsactuatormanager(CommandInterpreter ci) {
		ci.println("--Registered ActuatorManager--");
		for (String name : this.actuatorFactory.getActuatorManagers().keySet()){
			ci.println(" + "+name);
		}
	}
	
	public void _lsactuator(CommandInterpreter ci){
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
	
	public void _mmsdb(CommandInterpreter ci){
		List<String> queries = new ArrayList<String>();
		queries.add("CREATE STREAM machineMaintenance:factory (" +
				"timestamp Long, id Integer, name String)" +
				"CHANNEL localhost : 55556");
		queries.add("CREATE STREAM machineMaintenance:machine (" +
				"timestamp Long, id Integer, factoryId Integer, name String)" +
				"CHANNEL localhost : 55557");
		queries.add("CREATE STREAM machineMaintenance:install (" +
				"timestamp Long, id Integer, machineId Integer, limit1 Double, limit2 Double, pastUsageRate Double)" +
				"CHANNEL localhost : 55558");
		queries.add("CREATE STREAM machineMaintenance:usage (" +
				"timestamp Long, machineId Integer, rate Double)" +
				"CHANNEL localhost : 55559");
		for (String query : queries){
			try {
				this.executer.addQuery(query, "CQL");
			} catch (PlanManagementException e) {
				ci.println(e.getMessage());
			}
		}
	}
	
	public void _mmsp(CommandInterpreter ci){
		List<String> queries = new ArrayList<String>();
		queries.add("CREATE STREAM machineMaintenance:factory (" +
				"timestamp Long, id Integer, name String)" +
				"CHANNEL localhost : 55556");
		queries.add("CREATE STREAM machineMaintenance:machine (" +
				"timestamp Long, id Integer, factoryId Integer, name String)" +
				"CHANNEL localhost : 55557");
		queries.add("CREATE STREAM machineMaintenance:install (" +
				"timestamp Long, id Integer, machineId Integer, limit1 Double, limit2 Double)" +
				"CHANNEL localhost : 55558");
		queries.add("CREATE STREAM machineMaintenance:usage (" +
				"timestamp Long, machineId Integer, rate Double)" +
				"CHANNEL localhost : 55559");
		for (String query : queries){
			try {
				this.executer.addQuery(query, "CQL");
			} catch (PlanManagementException e) {
				ci.println(e.getMessage());
			}
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
			List<ActionMethod> schema = this.actuatorFactory.getReducedSchema(actuatorName, managerName);
			if (schema.isEmpty()){
				schema = this.actuatorFactory.getFullSchema(actuatorName, managerName);
			}
			ci.println("--Schema of <"+actuatorName+">--");
			for (ActionMethod method : schema){
				ci.print("+"+method.getName()+"(");
				int ctr = 0;
				for (ActionParameter param : method.getParameters()){
					if (ctr > 0){
						ci.print(", ");
					}
					ci.print(param.getName()+":"+param.getType());
					ctr++;
				}
				ci.println(")");
			}
		}catch (ActuatorException e){
			ci.println(e.getMessage());
		}
	}
	
	public void _testmm(CommandInterpreter ci){
		this._mmsdb(ci);
		
		try {
			executer.addQuery("select * from machineMaintenance:usage", "CQL");
			executer.addQuery("select * from machineMaintenance:factory", "CQL");
			executer.addQuery("select * from machineMaintenance:machine", "CQL");
			
			actuatorFactory.createActuator("a1", "de.uniol.inf.is.odysseus.action.actuator." +
					"impl.TestActuator(name:String)", "ActuatorAdapterManager");
			
			executer.addQuery("ON(select * from machineMaintenance:install) " +
					"DO ActuatorAdapterManager.a1.getName()", "ECA");
			
			this.benchmark.run();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void bindActuatorFactory(IActuatorFactory factory){
		this.actuatorFactory = factory;
	}
	
	public void bindExecutor(IAdvancedExecutor executer){
		this.executer = executer;
	}
	
	public void bindBenchmark(IActuatorBenchmark bm){
		this.benchmark = bm;
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
				" 	lsactuator <managerName> - lists all actuators registered to " +
				"specified manager\n"+
				"	lsactuatormanager - lists all registered actuator managers\n" +
				"	removeactuator <managerName> <actuatorName> - triggers specified " +
				"manager to remove declared actuator\n" +
				"	showschema <managerName> <actuatorName> - shows all methods provided by" +
				"the specified actuator\n" +
				"\n---Datagenerator-Functions---\n" +
				"	mmsdb - installs sources for MachineMaintenance scenario _with_ tupleUsage information." +
				" Make sure that the generatorConfig flag is set to <simulateDB>true</simulateDB>\n" +
				"	mmsp - installs sources for MachineMaintenance scenario _without_ tupleUsage information." +
				" Make sure that the generatorConfig flag is set to <simulateDB>false</simulateDB>\n";
	}

}
