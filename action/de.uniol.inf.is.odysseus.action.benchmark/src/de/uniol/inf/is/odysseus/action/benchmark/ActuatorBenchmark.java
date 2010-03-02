package de.uniol.inf.is.odysseus.action.benchmark;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ActuatorBenchmark implements IActuatorBenchmark{
	private Map<String, Integer> tupleNo;
	private List<BenchmarkData> bmData;
	
	private static PrintWriter file;
	private static String fileLoc = System.getProperty("user.home");
	
	public ActuatorBenchmark(){
		this.tupleNo = new HashMap<String, Integer>();
		this.bmData = new ArrayList<BenchmarkData>();
	}
	
	@Override
	public void addBenchmarkData(BenchmarkData data) {
		this.bmData.add(data);
	}

	public void writeResultsToFile(){
		try {
			file = new PrintWriter(new BufferedWriter(new FileWriter(fileLoc+"bm"+System.currentTimeMillis()+".csv")));
			//write header
			file.println("Identifier; OutputID; CreationTime;OutputTime;TakenTime");
			for (BenchmarkData data : bmData){
				for (Entry<String, Long> entry : data.getOutputTimes().entrySet()){
					long cTime = data.getCreationTime();
					long oTime = entry.getValue();
					long tTime = oTime-cTime;
					file.println(
							data.getIdentifier()+";"+
							entry.getKey()+";"+
							cTime+";"+
							oTime+";"+
							tTime);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getNextID(String identifier) {
		Integer no = this.tupleNo.get(identifier);
		if (no == null){
			no = -1;
		}
		no++;
		this.tupleNo.put(identifier, no);
		return identifier+no;
	}




	
}
