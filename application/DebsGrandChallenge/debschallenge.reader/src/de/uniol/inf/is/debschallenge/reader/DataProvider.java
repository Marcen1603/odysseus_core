/** Copyright [2011] [The Odysseus Team]
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.eclipse.equinox.app.IApplicationContext;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class DataProvider extends StreamClientHandler {

	private BufferedReader in;
	private long counter = 0;
	private NumberFormat nf = NumberFormat.getIntegerInstance(Locale.GERMAN);
	private long LastTime = System.currentTimeMillis();
	private String file = null;
	private IApplicationContext context;

	// 2012-02-22T16:46:28.9670320+00:00

	public DataProvider() {

	}

	public DataProvider(String file, IApplicationContext context) {
		this.file = file;
		this.context = context;
	}

	public DataProvider(DataProvider dataProvider) {
		this.file = dataProvider.file;
		this.context = dataProvider.context;
	}

	private static long parseTime(String time) throws ParseException {

		String onlytime = time.substring(0, time.length() - 6);
		String timeAndMillis = onlytime.substring(0, onlytime.length() - 4);
		String nano = time.substring(time.length() - 1);
		String micros = onlytime.substring(onlytime.length() - 4, onlytime.length() - 1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
		Date parsed = format.parse(timeAndMillis + " +0100");
		long millis = parsed.getTime();

		long nanoLong = Long.parseLong(nano);
		long microsLong = Long.parseLong(micros);
		long ts = (((TimeUnit.MILLISECONDS.toMicros(millis) + microsLong) * 10) + nanoLong)*100;
		return ts;
	}

	// public static void main(String[] args) {
	// try {
	// System.out.println(parseTime("2012-02-22T16:46:28.9670320+00:00"));
	// System.out.println(parseTime("2012-03-20T06:53:51.4750599+00:00"));
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// }

	@Override
	public List<DataTuple> next() {
		String line;

		try {
			line = in.readLine();
			if (line == null) {
				System.out.println("Process done: " + nf.format(counter) + " lines processed!");
				return null;
			}
			// timestamp
			String[] raw = line.split("\t");
			DataTuple tuple = toTuple(raw);

			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// if(counter>35998 && counter<36010){
			// System.out.println("ZEILE "+counter+": "+tuple.toString());
			// }
			List<DataTuple> list = new ArrayList<DataTuple>();

			// if(raw.length>56){
			// System.out.println("------------------------");
			// System.out.println("ZEILE "+counter+": "+line);
			// List<DataTuple> repaired = repair(raw);
			// System.out.println("Repaired: ");
			// for(DataTuple t : repaired){
			// System.out.println(t);
			// }
			// list.addAll(repaired);
			// }else{
			list.add(tuple);
			// }
			counter++;
			if (counter % 100000 == 0) {
				long now = System.currentTimeMillis();
				long neededTime = now - LastTime;
				System.out.println("Processed: " + nf.format(counter) + " still: " + nf.format(32390518 - counter) + " time: " + nf.format(neededTime));
				LastTime = now;
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private DataTuple toTuple(String[] raw) throws ParseException {
		DataTuple tuple = new DataTuple();
		long timestamp = parseTime(raw[0]);
		tuple.addLong(timestamp);
		// index
		tuple.addLong(Long.parseLong(raw[1]));
		// mf
		tuple.addInteger(Integer.parseInt(raw[2]));
		tuple.addInteger(Integer.parseInt(raw[3]));
		tuple.addInteger(Integer.parseInt(raw[4]));
		// pc
		tuple.addInteger(Integer.parseInt(raw[5]));
		tuple.addInteger(Integer.parseInt(raw[6]));
		tuple.addInteger(Integer.parseInt(raw[7]));
		tuple.addLong(Long.parseLong(raw[8]));
		tuple.addLong(Long.parseLong(raw[9]));
		tuple.addLong(Long.parseLong(raw[10]));
		// res
		tuple.addLong(Long.parseLong(raw[11]));
		// bm05 - bm10
		tuple.addBoolean(getBoolean(raw[12]));
		tuple.addBoolean(getBoolean(raw[13]));
		tuple.addBoolean(getBoolean(raw[14]));
		tuple.addBoolean(getBoolean(raw[15]));
		tuple.addBoolean(getBoolean(raw[16]));
		tuple.addBoolean(getBoolean(raw[17]));
		// optional stuff
		for (int i = 18; i < 56; i++) {
			tuple.addBoolean(getBoolean(raw[i]));
		}
		// for (int i = 54; i < 66; i++) {
		// tuple.addBoolean(Boolean.parseBoolean(raw[i]));
		// }
		return tuple;
	}

	public List<DataTuple> repair(String[] raw) throws ParseException {
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

		List<DataTuple> tuples = new ArrayList<DataTuple>();
		tuples.add(toTuple(first));
		tuples.add(toTuple(second));
		return tuples;

	}

	private boolean getBoolean(String raw) {
		if (raw.equalsIgnoreCase("1") || raw.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void init() {
		counter = 0;
		LastTime = System.currentTimeMillis();
		InputStream inputStream;
		System.out.println("using file: "+file);
		try {
			if (file == null) {
				
				URL fileURL = context.getBrandingBundle().getEntry("/allData.txt");
				inputStream = fileURL.openConnection().getInputStream();
			} else {
				inputStream = new FileInputStream(file);
			}
			in = new BufferedReader(new InputStreamReader(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
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
	public StreamClientHandler clone() {
		return new DataProvider(this);
	}

	public void go() {
		System.out.println("GO!");
		init();
		while (next() != null) {
			// ...
		}
		close();

	}

}
