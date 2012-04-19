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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class DataProvider extends StreamClientHandler {

	private BufferedReader in;
	private long counter = 0;
	// 2012-02-22T16:46:28.9670320+00:00

	private long parseTime(String time) throws ParseException {
		
		time = time.substring(0, time.length()-6);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

		long ts = format.parse(time).getTime();
		return ts;
	}

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		String line;
		try {
			line = in.readLine();
			if (line == null) {
				System.out.println("Process done: "+counter+" lines processed!");
				return null;
			}
			// timestamp
			String[] raw = line.split("\t");
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
			//bm05 - bm10
			tuple.addBoolean(Boolean.parseBoolean(raw[12]));
			tuple.addBoolean(Boolean.parseBoolean(raw[13]));
			tuple.addBoolean(Boolean.parseBoolean(raw[14]));
			tuple.addBoolean(Boolean.parseBoolean(raw[15]));
			tuple.addBoolean(Boolean.parseBoolean(raw[16]));
			tuple.addBoolean(Boolean.parseBoolean(raw[17]));
			// optional stuff
//			for (int i = 18; i < 54; i++) {
//				tuple.addBoolean(Boolean.parseBoolean(raw[i]));
//			}
//			for (int i = 54; i < 66; i++) {
//				tuple.addBoolean(Boolean.parseBoolean(raw[i]));
//			}
			

			//System.out.println(tuple.toString());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<DataTuple> list = new ArrayList<DataTuple>();
			list.add(tuple);
			counter++;
			if(counter%100000 == 0){
				System.out.println("Processed: "+counter);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void init() {
		counter = 0;
		URL fileURL = Activator.getContext().getBundle().getEntry("/allData.txt");
		try {
			InputStream inputStream = fileURL.openConnection().getInputStream();
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
		return new DataProvider();
	}

}
