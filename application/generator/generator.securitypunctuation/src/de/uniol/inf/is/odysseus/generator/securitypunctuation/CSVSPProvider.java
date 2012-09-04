package de.uniol.inf.is.odysseus.generator.securitypunctuation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.SADataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class CSVSPProvider extends StreamClientHandler {
		
		Integer i = 0;
		private BufferedReader in;
		
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
			SADataTuple tuple;
			String line;
			try {
				line = in.readLine();
				if (line == null) {
					System.out.println("restarting stream...");
					//restart data
					in.close();
					initFileStream();
					line = in.readLine();
				}
				String[] rawTuple = line.split(";");

				if(rawTuple[0].equals("SP")) {
					tuple = new SADataTuple(true);
					
					tuple.addAttribute(rawTuple[1]); // DDP - Stream
					tuple.addAttribute(new Long(rawTuple[2])); // DDP - Starttupel
					tuple.addAttribute(new Long(rawTuple[3])); // DDP - Endtupel
					tuple.addAttribute(rawTuple[4]); // DDP - Attribute
					tuple.addAttribute(rawTuple[5]); // SRP - Rollen
					tuple.addAttribute(new Integer(rawTuple[6])); // Sign
					tuple.addAttribute(new Integer(rawTuple[7])); // Immutable
					tuple.addAttribute(new Long(rawTuple[8])); // ts
				} else {
					tuple = new SADataTuple(false);
					
					tuple.addAttribute(rawTuple[1]); // DDP - Stream
					tuple.addAttribute(new Long(rawTuple[2])); // DDP - Starttupel
					tuple.addAttribute(new Long(rawTuple[3])); // DDP - Endtupel
					tuple.addAttribute(rawTuple[4]); // DDP - Attribute
					tuple.addAttribute(rawTuple[5]); // SRP - Rollen
					tuple.addAttribute(new Integer(rawTuple[6])); // Sign
					tuple.addAttribute(new Integer(rawTuple[7])); // Immutable
					tuple.addAttribute(new Long(rawTuple[8])); // ts
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(line);
				List<DataTuple> list = new ArrayList<DataTuple>();
				list.add(tuple);
				return list;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public StreamClientHandler clone() {
			return new CSVSPProvider();
		}

		private void initFileStream(){
			URL fileURL = Activator.getContext().getBundle().getEntry("/data/input.csv");
			try {
				InputStream inputStream = fileURL.openConnection().getInputStream();
				in = new BufferedReader(new InputStreamReader(inputStream));
				in.readLine();  //skip the header
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
}