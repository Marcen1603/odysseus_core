package de.uniol.inf.is.odysseus.action.benchmark.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.action.benchmark.BenchmarkData;
import de.uniol.inf.is.odysseus.action.benchmark.IActuatorBenchmark;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorFactory;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

public class ActuatorBenchmark implements IActuatorBenchmark, CommandProvider{
	public static ActuatorBenchmark getInstance() {
		return instance;
	}
	
	private List<BenchmarkData> bmData;
	
	private Map<String, String> queries;

	private PrintWriter file;

	private IAdvancedExecutor executor;

	private boolean running;

	private IActuatorFactory actuatorFactory;

	private int no;
	
	private static String fileLoc = System.getProperty("user.home");
	
	private static ActuatorBenchmark instance;
	
	public ActuatorBenchmark(){
		this.bmData = new ArrayList<BenchmarkData>();
		instance = this;
		
		this.queries = new HashMap<String, String>();
		
		this.running = false;
	}
	
	public void _addquerytobenchmark(CommandInterpreter ci){
		String[] args = this.extractArgument(ci);
		if (args.length > 1){
			this.addQuery(args[0], args[1]);
		}else {
			ci.println("Insufficient parameters. <Query> and <Lang> expected");
		}
	}
	
	public void _runbenchmark(CommandInterpreter ci){
		this.run();
	}
	
	
	public void _testmm(CommandInterpreter ci){
		this.mmsdb();
		
		try {
			this.addQuery("select * from machineMaintenance:usage", "CQL");
			this.addQuery("select * from machineMaintenance:factory", "CQL");
			this.addQuery("select * from machineMaintenance:machine", "CQL");
			
			this.actuatorFactory.createActuator("a1", "de.uniol.inf.is.odysseus.action.actuator." +
					"impl.TestActuator(name:String)", "ActuatorAdapterManager");
			
			this.addQuery("ON(select * from machineMaintenance:install) " +
					"DO ActuatorAdapterManager.a1.getName()", "ECA");
			
			this.run();
			
		} catch (ActuatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void addBenchmarkData(BenchmarkData data) {
		if (this.running){
			synchronized (this.bmData) {
				this.bmData.add(data);
				if (this.bmData.size() > 100){
					this.writeResultsToFile();
					this.bmData.clear();
				}
			}
		}
	}
	
	@Override
	public void addQuery(String query, String lang) {
		this.queries.put(query, lang);
	}

	public void bindActuatorFactory (IActuatorFactory factory){
		this.actuatorFactory = factory;
	}

	public void bindExecutor(IAdvancedExecutor executor){
		this.executor = executor;
	}

	public void closeWriter(){
		file.close();
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
	
	private void mmsdb(){
		List<String> queries = new ArrayList<String>();
		queries.add("CREATE STREAM machineMaintenance:factory (" +
				"timestamp Long, id Integer, name String)" +
				"CHANNEL localhost : 55556");
		queries.add("CREATE STREAM machineMaintenance:machine (" +
				"timestamp Long, id Integer, factoryId Integer, name String)" +
				"CHANNEL localhost : 55557");
		queries.add("CREATE STREAM machineMaintenance:install (" +
				"timestamp Long, id Integer, machineId Integer, limit1 Double, limit2 Double, pastUsageTime Double)" +
				"CHANNEL localhost : 55558");
		queries.add("CREATE STREAM machineMaintenance:usage (" +
				"timestamp Long, machineId Integer, rate Double)" +
				"CHANNEL localhost : 55559");
		for (String query : queries){
			try {
				this.executor.addQuery(query, "CQL");
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		try {
			file = new PrintWriter(new BufferedWriter(new FileWriter(fileLoc+File.separator+
					"bm"+System.currentTimeMillis()+".csv")));
			file.println("No.;OutputID;CreationTime;OutputTime;TakenTime");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//translate queries
		for (Entry<String, String> entry : queries.entrySet()){
			try {
				this.executor.addQuery(entry.getKey(), entry.getValue());
				
			} catch (PlanManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		try {
			this.executor.startExecution();
		} catch (PlanManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.running = true;
	}

	@Override
	public void stop() {
		this.running = false;
		this.writeResultsToFile();
		this.file.close();
	}
	

	private void writeResultsToFile(){
		for (BenchmarkData data : bmData){
			this.no++;
			for (Entry<String, Long> entry : data.getOutputTimes().entrySet()){
				long cTime = data.getCreationTime();
				long oTime = entry.getValue();
				long tTime = oTime-cTime;
				file.println(
						no+";"+
						entry.getKey()+";"+
						cTime+";"+
						oTime+";"+
						tTime);
			}
		}
	}


	
}
