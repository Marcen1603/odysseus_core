package de.uniol.inf.is.odysseus.rcp.evaluation.plot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.eclipse.core.runtime.IProgressMonitor;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class MeasurementFileUtil {

	public static final String DELIMITER = ";";

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
			// first, write the header!
			String header = getHeader();
			bw.write(header);
			bw.write(System.lineSeparator());

			boolean end = false;
			int lineNumber = 1;
			outer:while(!end){				
				List<String> lines = new ArrayList<>();
				for(BufferedReader br : readers){
					String line = br.readLine();					
					if(line==null){
						end = true;
						break outer;
					}
					// add line number (was done by former FileHandlerAO) MBr
					line = String.valueOf(lineNumber++) + DELIMITER + line;
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

	private static String getHeader() {
		StringBuilder sb = new StringBuilder("COUNT");
		sb.append(DELIMITER);
		sb.append("SUM");
		sb.append(DELIMITER);
		sb.append("MIN");
		sb.append(DELIMITER);
		sb.append("MAX");
		sb.append(DELIMITER);
		sb.append("MEAN");
		sb.append(DELIMITER);		
		sb.append("STANDARD_DEVIATION");
		sb.append(DELIMITER);
		sb.append("VARIANCE");
		sb.append(DELIMITER);
		sb.append("10_PERCENTILE");
		sb.append(DELIMITER);
		sb.append("20_PERCENTILE");
		sb.append(DELIMITER);
		sb.append("30_PERCENTILE");
		sb.append(DELIMITER);
		sb.append("40_PERCENTILE");
		sb.append(DELIMITER);
		sb.append("MEDIAN");
		sb.append(DELIMITER);
		sb.append("60_PERCENTILE");
		sb.append(DELIMITER);
		sb.append("70_PERCENTILE");
		sb.append(DELIMITER);
		sb.append("80_PERCENTILE");
		sb.append(DELIMITER);
		sb.append("90_PERCENTILE");		
		return sb.toString();
	}

	private static String mergeLines(List<String> lines) {		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		// first column is count
		double sumCount = 0.0;		
		for(String line : lines){
			Pair<Integer, Double> val = parseLine(line);
			sumCount = sumCount + val.getE1();			
			stats.addValue(val.getE2());
		}		
		double avgCount = 0.0;		
		if(sumCount>0){
			avgCount = sumCount/lines.size();
		}
		
		StringBuilder sb = new StringBuilder(Integer.toString((int)avgCount));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getSum()));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getMin()));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getMax()));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getMean()));
		sb.append(DELIMITER);		
		sb.append(Double.toString(stats.getStandardDeviation()));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getVariance()));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getPercentile(10)));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getPercentile(20)));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getPercentile(30)));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getPercentile(40)));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getPercentile(50)));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getPercentile(60)));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getPercentile(70)));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getPercentile(80)));
		sb.append(DELIMITER);
		sb.append(Double.toString(stats.getPercentile(90)));		
		return sb.toString();
	}
	
    public static Pair<Integer, Double> parseLine(String line) {
        return parseLine(line, 1);

    }

    public static Pair<Integer, Double> parseLine(String line, int index) {
        String[] parts = line.split(DELIMITER);
        int c = Integer.parseInt(parts[0]);
        double v = Double.parseDouble(parts[index]);
        return new Pair<>(c, v);

    }

}
