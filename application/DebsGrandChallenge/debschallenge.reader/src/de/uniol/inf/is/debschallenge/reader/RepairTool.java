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

package de.uniol.inf.is.debschallenge.reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Dennis Geesen Created at: 20.04.2012
 */
public class RepairTool {

	private BufferedReader in;
	private BufferedWriter out;
	private long counter = 0;
	NumberFormat nf = NumberFormat.getIntegerInstance(Locale.GERMAN);

	private List<String[]> repair(String[] raw) throws ParseException {
		String middle = raw[55];
		String last = middle.substring(0, 1);
		String rest = middle.substring(1);

		String[] first = new String[56];
		String[] second = new String[56];

		for (int i = 0; i < 55; i++) {
			first[i] = raw[i];
		}
		first[55] = last;
		for (int i = 1; i < 56; i++) {
			second[i] = raw[i + 55];
		}
		second[0] = rest;

		List<String[]> tuples = new ArrayList<String[]>();
		tuples.add(first);
		tuples.add(second);
		return tuples;
	}

	public List<String[]> next() {
		String line;
		try {
			line = in.readLine();
			if (line == null) {
				System.out.println("Process done: " + nf.format(counter) + " lines processed!");
				return null;
			}
			// timestamp
			String[] raw = line.split("\t");

			List<String[]> list = new ArrayList<String[]>();
			if (raw.length > 56) {
				System.out.println("------------------------");
				System.out.println("ZEILE " + counter + ": " + line);
				List<String[]> repaired = repair(raw);
				System.out.println("Repaired: ");
				for (String[] t : repaired) {
					System.out.println(printTuple(t));
				}
				list.addAll(repaired);
			} else {
				list.add(raw);
			}
			counter++;
			if (counter % 1000000 == 0) {
				
				System.out.println("Processed: " + nf.format(counter)+" still: "+nf.format(32390518-counter));
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void dump() throws IOException {
		List<String[]> tuples = next();
		while (tuples != null) {
			for (String[] tuple : tuples) {				
				if(tuple[12].equalsIgnoreCase("1") ||
						tuple[13].equalsIgnoreCase("1") ||
						tuple[14].equalsIgnoreCase("1") ||
						tuple[15].equalsIgnoreCase("1") ||
						tuple[16].equalsIgnoreCase("1") ||
						tuple[17].equalsIgnoreCase("1")
						){
					System.err.println(printTuple(tuple));
				}
				// System.out.println(printTuple(tuple));
				//out.write(printTuple(tuple)+System.getProperty("line.separator"));
			}
			tuples = next();
		}

	}

	private String printTuple(String[] tuple) {
		StringBuffer sb = new StringBuffer();
		String sep = "";
		for (String s : tuple) {
			sb.append(sep);
			sb.append(s);
			sep = "\t";
		}
		// System.getProperty("line.separator");
		return sb.toString();
	}

	public void init() {
		counter = 0;
		// open reader
		URL fileURL = Activator.getContext().getBundle().getEntry("/allData.txt");
		try {
			InputStream inputStream = fileURL.openConnection().getInputStream();
			in = new BufferedReader(new InputStreamReader(inputStream));
			// open writer			
			FileWriter fwriter = new FileWriter("gcrepaired.txt");
			out = new BufferedWriter(fwriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void go() {
		System.out.println("GO!");
		long starttime = System.currentTimeMillis();
		init();
		try {
			dump();
		} catch (IOException e) {		
			e.printStackTrace();
		}
		close();
		long millis = System.currentTimeMillis()-starttime;
		String needed = String.format("%d min, %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(millis),
			    TimeUnit.MILLISECONDS.toSeconds(millis) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
			);
		System.out.println("Needed: "+needed);

	}

}
