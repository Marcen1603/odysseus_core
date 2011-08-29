package de.uniol.inf.is.odysseus.costmodel.operator.relational.cfg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.OdysseusDefaults;

public class DataStreamRateSaver {

	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(DataStreamRateSaver.class);
		}
		return _logger;
	}

	private static DataStreamRateSaver instance = null;
	private static final String FILENAME = "ac_datarates.conf";
	
	private Map<String, Double> datarates = new HashMap<String, Double>();
	
	private DataStreamRateSaver() {}
	
	public static DataStreamRateSaver getInstance() {
		if( instance == null ) 
			instance = new DataStreamRateSaver();
		return instance;
	}
	
	public void load() {
		String filename = OdysseusDefaults.getHomeDir() + FILENAME;
		getLogger().debug("Loading datarates from " + filename);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			String line = br.readLine();
			while( line != null ) {
				
				String[] parts = line.split("\\=");
				
				Double d = new Double(parts[1]);
				datarates.put(parts[0], d);
				getLogger().debug("Datarate of " + parts[0] + ":" + d);
				
				line = br.readLine();
			}
		} catch( FileNotFoundException ex ) {
			File file = new File(filename);
			try {
				file.createNewFile();
				getLogger().debug("New cfg-File created: " + filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void set( String streamName, double datarate ) {
		datarates.put(streamName, new Double(datarate));
		getLogger().debug("Setting datarate of " + streamName + " to " + datarate);
	}
	
	public double get( String streamName ) {
		if( !datarates.containsKey(streamName))
			return 1.0;
		
		return datarates.get(streamName);
	}
	
	public void save() {
		String filename = OdysseusDefaults.getHomeDir() + FILENAME;
		getLogger().debug("Saving datarates in " + filename);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			
			for( String str : datarates.keySet()) {
				Double rate = datarates.get(str);
				bw.write(str + "=" + rate + "\n");
				bw.flush();
				
				getLogger().debug("Writing " + str + "=" + rate);
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
