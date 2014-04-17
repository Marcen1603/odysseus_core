package de.uniol.inf.is.odysseus.rcp.evaluation.plot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class MeasurementFileUtil {

	private static final String DELIMITER = ";";

	public static MeasurementResult mergeFiles(MeasurementResult result, String destinationPath, IProgressMonitor monitor) {

		try {			
			File destination = new File(destinationPath);
			if (!destination.getParentFile().exists()) {
				destination.getParentFile().mkdirs();
			}
			if (!destination.exists()) {
				destination.createNewFile();
			}
			monitor.setTaskName("Reading files...");
			// create destination file
			BufferedWriter bw = new BufferedWriter(new FileWriter(destination));
			// open and prepare to read
			List<BufferedReader> readers = new ArrayList<>();
			for (File f : result.getFiles()) {
				BufferedReader br = new BufferedReader(new FileReader(f));				
				readers.add(br);
			}

			boolean end = false;
			outer:while(!end){				
				List<String> lines = new ArrayList<>();
				for(BufferedReader br : readers){
					String line = br.readLine();
					if(line==null){
						end = true;
						break outer;
					}
					lines.add(line);
				}
				String mergedLine = mergeLines(lines);
				bw.write(mergedLine);
				bw.write(System.lineSeparator());
			}
			
			for(BufferedReader br : readers){
				br.close();
			}					
			bw.close();

			MeasurementResult merged = new MeasurementResult(result.getType());
			merged.setName(result.getName());
			merged.setVariable(result.getVariable());
			merged.addFile(destination);
			return merged;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static String mergeLines(List<String> lines) {		
		// first column is count
		double sumCount = 0.0;
		// second column is time/amount
		double sumValue = 0.0;
		for(String line : lines){
			Pair<Integer, Double> val = parseLine(line);
			sumCount = sumCount + val.getE1();
			sumValue = sumValue + val.getE2();
		}
		double avgCount = 0.0;
		double avgValue = 0.0;
		if(sumCount>0){
			avgCount = sumCount/lines.size();
		}
		if(sumValue>0){
			avgValue = sumValue/lines.size();
		}
		return Integer.toString((int)avgCount)+DELIMITER+Double.toString(avgValue);		
	}
	
	public static Pair<Integer, Double> parseLine(String line){
		String[] parts = line.split(DELIMITER);
		int c = Integer.parseInt(parts[0]);
		double v = Double.parseDouble(parts[1]);
		return new Pair<Integer, Double>(c, v);
		
	}

}
