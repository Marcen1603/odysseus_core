package de.uniol.inf.is.odysseus.generator.securitypunctuation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.SADataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class CSVSPProvider extends StreamClientHandler {
	
//    private static Logger LOG = LoggerFactory.getLogger(CSVSPProvider.class);
		
		Integer i = 0;
		private BufferedReader in;
		private String fileName;
		private boolean benchmark;
		private Integer counter = 0;
		private Integer delay;
		private String name;
		
		public CSVSPProvider(String fileName, Integer delay, String name, boolean benchmark) {
			super();
			this.fileName = fileName;
			this.delay = delay;
			this.benchmark = benchmark;
			this.name = name;
		}
		@Override
		public void init() {
			initFileStream();
			super.setIsSA(true);
		}

		@Override
		public void close() {
		}

		@Override
		public List<DataTuple> next() {
			if(!benchmark || counter++ < 50000) {
				SADataTuple tuple;
				String line;
				try {
					line = in.readLine();
//					if (line == null) {
//						System.out.println("restarting stream...");
	//					restart data
//						in.close();
//						initFileStream();
//						line = in.readLine();
//					}
					String[] rawTuple = line.split(";");
	
					if(rawTuple[0].equals("SP")) {
						tuple = new SADataTuple(true);
						
						tuple.addAttribute(rawTuple[2]); // DDP - Stream
						tuple.addAttribute(new Long(rawTuple[3])); // DDP - Starttupel
						tuple.addAttribute(new Long(rawTuple[4])); // DDP - Endtupel
						tuple.addAttribute(rawTuple[5]); // DDP - Attribute
						tuple.addAttribute(rawTuple[6]); // SRP - Rollen
						tuple.addAttribute(new Integer(rawTuple[7])); // Sign
						tuple.addAttribute(new Integer(rawTuple[8])); // Immutable
						tuple.addAttribute(new Long(rawTuple[1])); // ts
					} else {
						tuple = new SADataTuple(false);

						tuple.addAttribute(new Long(rawTuple[1])); // startTS
						tuple.addAttribute(new Long(rawTuple[2])); // endTS
						tuple.addAttribute(rawTuple[3]); // DDP - Attribute
						tuple.addAttribute(new Integer(rawTuple[4])); // Immutable
					}
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					System.out.println(line);
					System.out.println("next Tuple from " + name + ": " + line);
					List<DataTuple> list = new ArrayList<DataTuple>();
					list.add(tuple);
					return list;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			return null;
		}

		@Override
		public StreamClientHandler clone() {
			return new CSVSPProvider(fileName, delay, name, benchmark);
		}

		private void initFileStream(){
			URL fileURL = Activator.getContext().getBundle().getEntry("/data/" + fileName);
			try {
				InputStream inputStream = fileURL.openConnection().getInputStream();
				in = new BufferedReader(new InputStreamReader(inputStream));
				in.readLine();  //skip the header
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
}