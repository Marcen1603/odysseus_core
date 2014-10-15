package de.uniol.inf.is.odysseus.generator.process.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public class CSVLogParser {
	static private BufferedReader br = null;
	static private String line = "";
	@SuppressWarnings("unused")
	static private String delimiter = ",";
	static private String csvFile = "/home/phil/running-example-non-conforming.csv";
	static private List<String> csvLines = Lists.newArrayList();
	
	/**
	 * For parsing an example eventlog 
	 */
	public CSVLogParser(){
		
	}
		
	public static List<String> getCSVLines(){
		try{
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] splitline = line.split("\n");
				
				for(String lstr : Arrays.asList(splitline)){
				csvLines.add(lstr);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return csvLines;
 	}
	
	public static String[] getLineItems(String delimiter, String line){
		return line.split(delimiter);
	}
	
	

}
