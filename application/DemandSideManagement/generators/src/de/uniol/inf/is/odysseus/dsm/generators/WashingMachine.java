/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.dsm.generators;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

/**
 * 
 * @author Daniel Weinberg
 * Created at: 07.09.2011
 */
public class WashingMachine extends StreamClientHandler{
	
	/*
	 * Durchschnittswerte Waschmaschine
	 * Verbrauch : 1,15 kWh
	 * Laufzeit: 45 Minuten
	 * Volt: 230 V
	 * Ampere: 5 A
	 * Watt = V * A = 1150 W
	 */
	
	/*
	 * TODO: Soll das Gerät heute überhaupt starten?
	 */
	
	Calendar calendar = Calendar.getInstance();
	
	private long timestamp;
	private int randomRuntimes;
	private double ampere = 5.0;
	private double volt = 230.0;
	private double runtime = 0.75;
	private int runtimesMax = 3;
	private long [] randomStart = new long[runtimesMax];
	private int interval = 0;
	private int startMin = 12;
	private int startMax = 18;
	private long currentDay;

	@Override
	public void init() {
		calendar.set(2011, 0, 1, 11, 59, 0);
		timestamp = calendar.getTimeInMillis();
		currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		randomRuntimes = (int) (Math.random()*runtimesMax+1);
		randomStart[0] = getRandomStart(startMin, (((startMax - startMin)/randomRuntimes)+startMin));
		for(int i = 1; i < randomRuntimes; i++){
			calendar.setTimeInMillis((long) (randomStart[i-1]+(runtime*3600000)));
			int tmpStartMin = calendar.get(Calendar.HOUR_OF_DAY);
			randomStart[i] = getRandomStart(tmpStartMin, (((startMax - tmpStartMin)/(randomRuntimes-i))+tmpStartMin));
		}
	}
	
	@Override
	public void close() {
		
	}

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		newDay();
		
		if (timestamp >= randomStart[interval] && timestamp <= randomStart[interval]+(runtime*3600000)){
			tuple.addLong(timestamp);
			tuple.addString("Washing Machine");
			tuple.addInteger(1);
			tuple.addDouble(volt);
			tuple.addDouble(ampere);
			
			//DEBUG TUPEL
			tuple.addLong(randomStart[interval]);
			tuple.addInteger(randomRuntimes);
			calendar.setTimeInMillis(timestamp);
			tuple.addInteger(calendar.get(Calendar.HOUR_OF_DAY));
			timestamp += 1000 * 100; //Test
		} else {
			if (timestamp >= randomStart[interval]+(runtime*3600000) && interval < randomRuntimes - 1){
				interval++;
			}
			tuple.addLong(timestamp);
			tuple.addString("Washing Machine");
			tuple.addInteger(1);
			tuple.addDouble(0);
			tuple.addDouble(0);
			
			//DEBUG TUPEL
			tuple.addLong(randomStart[interval]);
			tuple.addInteger(randomRuntimes);
			calendar.setTimeInMillis(timestamp);
			tuple.addInteger(calendar.get(Calendar.HOUR_OF_DAY));
			timestamp += 1000 * 100; //Test
		}
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);
		return list;
	}
	
	private boolean newDay(){
		calendar.setTimeInMillis(timestamp);
		if (currentDay == calendar.get(Calendar.DAY_OF_MONTH)){
			return false;
		} else {
			currentDay = calendar.get(Calendar.DAY_OF_MONTH);
			randomRuntimes = (int) (Math.random()*runtimesMax+1);
			randomStart[0] = getRandomStart(startMin, (((startMax - startMin)/randomRuntimes)+startMin));
			for(int i = 1; i < randomRuntimes; i++){
				calendar.setTimeInMillis((long) (randomStart[i-1]+(runtime*3600000)));
				int tmpStartMin = calendar.get(Calendar.HOUR_OF_DAY);
				randomStart[i] = getRandomStart(tmpStartMin, (((startMax - tmpStartMin)/(randomRuntimes-i))+tmpStartMin));
			}
			interval = 0;
			return true;
		}
	}
	
	private Long getRandomStart(int min, int max){
		calendar.set(Calendar.HOUR_OF_DAY, min);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long startMin = calendar.getTimeInMillis();

		calendar.set(Calendar.HOUR_OF_DAY, max);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long startMax = calendar.getTimeInMillis();

		Random random = new Random();
		long start = (long)(random.nextDouble() * (startMax - startMin)) + startMin; 
		
		return start;
	}

}
