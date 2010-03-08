package de.uniol.inf.is.odysseus.action.benchmark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class ActuatorBenchmark implements IActuatorBenchmark{
	private List<BenchmarkData> bmData;
	
	private PrintWriter file;
	private static String fileLoc = System.getProperty("user.home");
	
	private static ActuatorBenchmark instance;
	
	public ActuatorBenchmark(){
		this.bmData = new ArrayList<BenchmarkData>();
		instance = this;
		try {
			file = new PrintWriter(new BufferedWriter(new FileWriter(fileLoc+File.separator+
					"bm"+System.currentTimeMillis()+".csv")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void addBenchmarkData(BenchmarkData data) {
		this.bmData.add(data);
		if (this.bmData.size() > 100){
			this.writeResultsToFile();
			this.bmData.clear();
		}
	}
	
	public void closeWriter(){
		file.close();
	}

	public void writeResultsToFile(){
		//write header
		file.println("Identifier;No.;OutputID;CreationTime;OutputTime;TakenTime");
		int no=0;
		for (BenchmarkData data : bmData){
			no++;
			for (Entry<String, Long> entry : data.getOutputTimes().entrySet()){
				long cTime = data.getCreationTime();
				long oTime = entry.getValue();
				long tTime = oTime-cTime;
				file.println(
						data.getIdentifier()+";"+
						no+";"+
						entry.getKey()+";"+
						cTime+";"+
						oTime+";"+
						tTime);
			}
		}
	}
	
	public static ActuatorBenchmark getInstance() {
		return instance;
	}


	
}
