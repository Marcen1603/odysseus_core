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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Dennis Geesen Created at: 20.04.2012
 */
public class RepairTool {

	private BufferedReader in;
	private BufferedWriter out;
	private long counter = 0;
	private static NumberFormat nf = NumberFormat.getIntegerInstance(Locale.GERMAN);
	private static Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

	public static void main(String[] args) throws Exception {
		long nanos = 1330086739644209700L;

		long millis = nanos / 1000000;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		System.out.println(cal.getTime());
		FileReader reader = new FileReader("E:" + File.separator + "Workspace" + File.separator + "Odysseus" + File.separator + "trunk" + File.separator + "application" + File.separator
				+ "DebsGrandChallenge" + File.separator + "gcrepaired.txt");
		BufferedReader br = new BufferedReader(reader);
		String line = br.readLine();
		int count = 1;
		long millisLast = 0;
		String lastLine = "";
		while (line != null) {
			try{
			if (!line.isEmpty()) {
				line = line.trim();
				String parts[] = line.split("\\.");
				long millisCurrent = parseTime(parts[0]);

				if ((millisCurrent - millisLast) > 1000) {
					System.out.println("found lines at " + count);
					System.out.println(lastLine);
					System.out.println(line);
					System.out.println("--------------------------------------------------------");
				}
				millisLast = millisCurrent;
				if (count % 100000 == 0) {
					System.out.println("Processed: " + nf.format(count) + " still: " + nf.format(32390518 - count));
				}
			}
			count++;
			lastLine = line;
			line = br.readLine();
			}catch (Exception e) {				
				System.err.println("Error at line "+count);
				System.err.println("Line: "+line);
				System.err.println("Last: "+lastLine);
				e.printStackTrace();
				break;
			}
		}
		System.out.println("Done");
	}

	private static long parseTime(String timeString) {
		String splits[] = timeString.split("T");
		String date[] = splits[0].split("-");
		String time[] = splits[1].split(":");
		
		int year = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[2]);
		int hour = Integer.parseInt(time[0]);
		int minute = Integer.parseInt(time[1]);
		int second = Integer.parseInt(time[2]);
		cal.set(year, month, day, hour, minute, second);		
		return cal.getTimeInMillis();
	}

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

				System.out.println("Processed: " + nf.format(counter) + " still: " + nf.format(32390518 - counter));
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
				if (tuple[21].equalsIgnoreCase("1") || tuple[22].equalsIgnoreCase("1") || tuple[23].equalsIgnoreCase("1")) {
					System.out.println("Counter: "+counter);
					System.err.println(printTuple(tuple));
				}
				// System.out.println(printTuple(tuple));
				//out.write(printTuple(tuple) + System.getProperty("line.separator"));
			}
			tuples = next();
		}
		// and an empty line at the end
		out.write(System.getProperty("line.separator"));

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
		return sb.toString().trim();
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
		long millis = System.currentTimeMillis() - starttime;
		String needed = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		System.out.println("Needed: " + needed);

	}

}
