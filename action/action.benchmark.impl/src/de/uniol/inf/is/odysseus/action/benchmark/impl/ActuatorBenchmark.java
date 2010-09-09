package de.uniol.inf.is.odysseus.action.benchmark.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.action.benchmark.BenchmarkData;
import de.uniol.inf.is.odysseus.action.benchmark.IActuatorBenchmark;

public class ActuatorBenchmark implements IActuatorBenchmark, CommandProvider{
	public static ActuatorBenchmark getInstance() {
		return instance;
	}
	
	private List<BenchmarkData> bmData;
	
	private PrintWriter file;

	private boolean running;

	private int no;
	
	private static String fileLoc = System.getProperty("user.home");
	
	private static ActuatorBenchmark instance;
	
	public ActuatorBenchmark(){
		this.bmData = new ArrayList<BenchmarkData>();
		instance = this;		
		this.running = false;
	}
	
	public void _runbenchmark(CommandInterpreter ci){
		this.run();
	}
	
	

	
	public void _stopbenchmark(CommandInterpreter ci){
		this.stop();
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
	
	public void closeWriter(){
		file.close();
	}
	
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
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
