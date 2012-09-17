/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Jonas Jacobi
 */
public class LabdataToRelationalTuple {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out
					.println("Usage: LabdataToTuple <labdatafile> <outputfile>");
			return;
		}
		String inputFilename = args[0];
		String outputFilename = args[1];
		BufferedReader reader = new BufferedReader(
				new FileReader(inputFilename));
		ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(
				outputFilename));
		@SuppressWarnings("unused")
		int lineCount = 0;
		String line = null;
		LinkedList<Tuple<ITimeInterval>> l = new LinkedList<Tuple<ITimeInterval>>();
		try {
			while (reader.ready()) {
				try {
					++lineCount;
					line = reader.readLine();
					// 2004-03-31 03:38:15.757551 2 1 122.153 -3.91901 11.04
					// 2.03397
					String[] fields = line.split(" ");
					GregorianCalendar cal = new GregorianCalendar();
					cal.set(Integer.parseInt(fields[0].substring(0, 4)),
							Integer.parseInt(fields[0].substring(5, 7)),
							Integer.parseInt(fields[0].substring(8, 10)),
							Integer.parseInt(fields[1].substring(0, 2)),
							Integer.parseInt(fields[1].substring(3, 5)),
							Integer.parseInt(fields[1].substring(6, 8)));
					int fieldPos;
					double value;
					if (fields.length == 9) {
						fieldPos = 3;
						value = Double.parseDouble("0." + fields[1].substring(9));
					} else {
						fieldPos = 2;
						value = Double.parseDouble("0." + fields[2]);
					}
					cal.set(Calendar.MILLISECOND, (int) (1000 * value));
					
					long time = cal.getTimeInMillis();
					
					
					Object[] values = new Object[7];
					values[0] = time;
					values[1] = Integer.parseInt(fields[fieldPos++]);
					values[2] = Integer.parseInt(fields[fieldPos++]);
					values[3] = Double.parseDouble(fields[fieldPos++]);
					values[4] = Double.parseDouble(fields[fieldPos++]);
					values[5] = Double.parseDouble(fields[fieldPos++]);
					values[6] = Double.parseDouble(fields[fieldPos]);
					Tuple<ITimeInterval> tuple = new Tuple<ITimeInterval>(
							values, false);
					l.add(tuple);
				} catch (Exception e) {
//					System.err.println("error in line: " + lineCount);
//					System.err.println("line: " + line);
//					e.printStackTrace();
				}
			}
			System.out.println("#elements: " + l.size());
			Collections.sort(l, new Comparator<Tuple<?>>() {

				@Override
				public int compare(Tuple<?> o1, Tuple<?> o2) {
					if (o1.getAttribute(0).equals(o2.getAttribute(0))) {
						return 0;
					}
					return (Long) o1.getAttribute(0) < (Long) o2
							.getAttribute(0) ? -1 : 1;
				}
			});
			for (Object obj : l) {
				o.writeObject(obj);
			}
//			o.writeObject(null);
		} catch (Exception e) {
			throw e;
		} finally {
			reader.close();
			o.close();
		}
		System.out.println("Done");
	}

}
