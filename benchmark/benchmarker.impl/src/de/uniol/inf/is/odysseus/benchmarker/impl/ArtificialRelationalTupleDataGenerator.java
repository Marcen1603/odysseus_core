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

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Andre Bolles
 */
public class ArtificialRelationalTupleDataGenerator {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out
					.println("Usage: ArtificialRelationalTupleData <datacount> <outputfile>");
			return;
		}
		int dataCount = Integer.parseInt(args[0]);
		String outputFilename = args[1];
		ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(
				outputFilename));
//		int lineCount = 0;
//		String line = null;
		try {
			
			int[] epochs = new int[20];
			int[] temperatures = {16, 17, 18, 19, 20, 21, 22, 23, 24};
			int[] humidities = {20, 23, 26, 29, 30, 32, 36, 37, 39, 41, 42};
			int[] lights = {91, 90, 85, 87, 75, 45, 34, 33, 30, 29};
			int[] voltages = {10, 11, 9, 12, 4, 3, 2, 1};
			for(int i = 0; i<epochs.length; i++){
				epochs[i] = 0;
			}
			
			long timestamp = 0;
			for(int i = 0; i<dataCount; timestamp = 2 * (++i)){
				int moteid = (i % 20) + 1;
				epochs[i%20]++;
				Random random = new Random(3457876549L);
				int temperature = temperatures[random.nextInt(9)];
				int humidity = humidities[random.nextInt(11)];
				int light = lights[random.nextInt(10)];
				int voltage = voltages[random.nextInt(8)];
				
				Object[] values = new Object[7];
				values[0] = timestamp;
				values[1] = epochs[i%20];
				values[2] = moteid;
				values[3] = temperature;
				values[4] = humidity;
				values[5] = light;
				values[6] = voltage;
				Tuple<ITimeInterval> tuple = new Tuple<ITimeInterval>(
						values, false);
				// Sortierung nach Zeitstempel nicht notwendig, da ohnehin aufsteigend erzeugt
				o.writeObject(tuple);
			}
//			System.out.println("#elements: " + l.size());
//			Collections.sort(l, new Comparator<Tuple<?>>() {
//
//				@Override
//				public int compare(Tuple<?> o1, Tuple<?> o2) {
//					if (o1.getAttribute(0).equals(o2.getAttribute(0))) {
//						return 0;
//					}
//					return (Long) o1.getAttribute(0) < (Long) o2
//							.getAttribute(0) ? -1 : 1;
//				}
//			});
//			for (Object obj : l) {
//				o.writeObject(obj);
//			}
//			o.writeObject(null);
		} catch (Exception e) {
			throw e;
		} finally {
			o.close();
		}
		System.out.println("Done");
	}

}
