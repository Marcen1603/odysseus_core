package de.uniol.inf.is.odysseus.costmodel.operator.relational;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModelCfg;

public class CPURateSaver {

	private static CPURateSaver instance = null;
	private static final String FILENAME = "ac_cpurates.conf";
	
	private Map<String, Double> cpuRates = new HashMap<String, Double>();
	
	private CPURateSaver() {}
	
	public static CPURateSaver getInstance() {
		if( instance == null ) 
			instance = new CPURateSaver();
		return instance;
	}
	
	public void load() {
		String filename = OdysseusDefaults.getHomeDir() + FILENAME;
//		System.out.println("Loading CPURates from " + filename);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			String line = br.readLine();
			while( line != null ) {
				
				String[] parts = line.split("\\=");
				
				Double d = new Double(parts[1]);
				cpuRates.put(parts[0], d);
//				System.out.println("CPURate of " + parts[0] + ":" + d);
				
				line = br.readLine();
			}
		} catch( FileNotFoundException ex ) {
			File file = new File(filename);
			try {
				file.createNewFile();
//				System.out.println("New cfg-File created: " + filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void set( String streamName, double cpurate ) {
		cpuRates.put(streamName, new Double(cpurate));
				
//		System.out.println("Setting cpurate of " + streamName + " to " + cpurate);
	}
	
	public double get( String streamName ) {
		if( !cpuRates.containsKey(streamName))
			return OperatorCostModelCfg.getInstance().getStandardCpuCost();
		
		return cpuRates.get(streamName);
	}
	
	public void save() {
		String filename = OdysseusDefaults.getHomeDir() + FILENAME;
//		System.out.println("Saving cpurates in " + filename);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			
			for( String str : cpuRates.keySet()) {
				Double rate = cpuRates.get(str);
				bw.write(str + "=" + rate + "\n");
				bw.flush();
				
//				System.out.println("Writing " + str + "=" + rate);
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
