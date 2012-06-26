/** Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.generator.mining.arff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

/**
 * 
 * @author Dennis Geesen Created at: 31.05.2012
 */
public class ArffReader extends StreamClientHandler {

	private BufferedReader in;
	private String file;
	private long counter = 0;
	private long number = 0;
	private NumberFormat nf = NumberFormat.getIntegerInstance(Locale.GERMAN);

	public ArffReader(ArffReader arffReader) {
		this.file = arffReader.file;
		int number = 0;
	}

	public ArffReader(String file) {
		this.file = file;
		int number = 0;
	}

	@Override
	public void init() {
		URL fileURL = Activator.getContext().getBundle().getEntry(file);
		try {
			counter = 0;
			InputStream inputStream = fileURL.openConnection().getInputStream();
			in = new BufferedReader(new InputStreamReader(inputStream));
			readHeader();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readHeader() throws IOException {
		System.out.println("Reading header...");
		String line = in.readLine();
		List<String> attributes = new ArrayList<String>();
		while (line != null) {
			line = in.readLine();
			if (line.startsWith("%") || line.isEmpty()) {
				continue;
			}
			System.out.println("--> "+line);
			int splitPoint = line.indexOf(" ");
			if(splitPoint == -1){
				splitPoint = line.length();
			}
			String identifier = line.substring(0, splitPoint);
			String value = "";
			if (line.length() > splitPoint) {
				value = line.substring(splitPoint, line.length());
			}
			if (identifier.equalsIgnoreCase("@DATA")) {				
				return;
			}
			if (identifier.equalsIgnoreCase("@RELATION")) {
				System.out.println("Reading relation: " + value);
			}
			if (identifier.equalsIgnoreCase("@ATTRIBUTE")) {
				System.out.println("Attribute found: " + value);
			}
		}
	}

	@Override
	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		String line;
		try {			
			line = in.readLine();
			if (line == null) {
				System.out.println("Process done: " + nf.format(counter) + " tuples processed!");	
				System.out.println("Restarting....");
				close();
				init();
				line = in.readLine();
			}
			while(line.trim().startsWith("%") || line.isEmpty()){
				line = in.readLine();
			}
			
			String[] rawTuple = line.split(",");
			tuple.addLong(number);
			tuple.addString(rawTuple[0]);
			tuple.addBoolean(rawTuple[1]);
			tuple.addBoolean(rawTuple[2]);
			tuple.addBoolean(rawTuple[3]);
			tuple.addBoolean(rawTuple[4]);
			tuple.addBoolean(rawTuple[5]);
			tuple.addBoolean(rawTuple[6]);
			tuple.addBoolean(rawTuple[7]);
			tuple.addBoolean(rawTuple[8]);
			tuple.addBoolean(rawTuple[9]);
			tuple.addBoolean(rawTuple[10]);
			tuple.addBoolean(rawTuple[11]);
			tuple.addBoolean(rawTuple[12]);
			tuple.addInteger(rawTuple[13]);
			tuple.addBoolean(rawTuple[14]);
			tuple.addBoolean(rawTuple[15]);
			tuple.addBoolean(rawTuple[16]);
			tuple.addInteger(rawTuple[17]);
			counter++;
			number++;
			try {
				Thread.sleep(1);
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
		return new ArffReader(this);
	}

}
