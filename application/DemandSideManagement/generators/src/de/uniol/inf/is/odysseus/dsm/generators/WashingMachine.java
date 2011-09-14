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
	 * Watt = 1150 W
	 * StartUp = true
	 */
	
	Calendar calendar = Calendar.getInstance();
	private long timestamp;
	private int randomRuntimes;
	private long currentDay;
	private int interval = 0;
	
	private double maxWatt = 1150.0;
	private double runtime = 0.75;
	private int rMin = 5;
	private int rMax = 5;
	private long [] randomStart = new long[rMax];
	private int startMin = 12;
	private int startMax = 18;
	private double startUpTime = 60000.0;
	private String name = "WashingMachine";
	private int roomId = 1;
	
	public WashingMachine(){
		
	}

	public WashingMachine(WashingMachine washingMachine) {
		
	}

	@Override
	public void init() {
		calendar.setTimeInMillis(SimulationClock.getInstance().getTime());
		currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		randomRuntimes = getRandomNumber(rMin,rMax);
		if (randomRuntimes > 0){
			randomStart[0] = getRandomStart(startMin, (((startMax - startMin)/randomRuntimes)+startMin));
			for(int i = 1; i < randomRuntimes; i++){
				calendar.setTimeInMillis((long) (randomStart[i-1]+(runtime*3600000)));
				int tmpStartMin = calendar.get(Calendar.HOUR_OF_DAY);
				randomStart[i] = getRandomStart(tmpStartMin, (((startMax - tmpStartMin)/(randomRuntimes-i))+tmpStartMin));
			}
		}
	}
	
	@Override
	public void close() {
		
	}

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		timestamp = SimulationClock.getInstance().getTime();
		newDay(timestamp);
		
		if (randomRuntimes > 0){
			if (timestamp >= randomStart[interval] && timestamp <= randomStart[interval]+(runtime*3600000)){
				tuple.addLong(timestamp);
				tuple.addString(name);
				tuple.addInteger(roomId);
				tuple.addDouble(getMeteredValue(timestamp));
				
				tuple.addInteger(randomRuntimes);
				tuple.addLong(randomStart[interval]);
				tuple.addInteger(interval);
			} else {
				if (timestamp >= randomStart[interval]+(runtime*3600000) && interval < randomRuntimes - 1){
					interval++;
				}
				tuple.addLong(timestamp);
				tuple.addString(name);
				tuple.addInteger(roomId);
				tuple.addDouble(0);
				
				tuple.addInteger(randomRuntimes);
				tuple.addLong(randomStart[interval]);
				tuple.addInteger(interval);
			}
		} else {
			tuple.addLong(timestamp);
			tuple.addString(name);
			tuple.addInteger(roomId);
			tuple.addDouble(0);
			
			tuple.addInteger(randomRuntimes);
			tuple.addLong(0);
			tuple.addInteger(interval);
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
	
	private boolean newDay(long ts){
		calendar.setTimeInMillis(ts);
		if (currentDay == calendar.get(Calendar.DAY_OF_MONTH)){
			return false;
		} else {
			currentDay = calendar.get(Calendar.DAY_OF_MONTH);
			randomRuntimes = getRandomNumber(rMin, rMax);
			if (randomRuntimes > 0){
				randomStart[0] = getRandomStart(startMin, (((startMax - startMin)/randomRuntimes)+startMin));
				for(int i = 1; i < randomRuntimes; i++){
					calendar.setTimeInMillis((long) (randomStart[i-1]+(runtime*3600000)));
					int tmpStartMin = calendar.get(Calendar.HOUR_OF_DAY);
					randomStart[i] = getRandomStart(tmpStartMin, (((startMax - tmpStartMin)/(randomRuntimes-i))+tmpStartMin));
				}
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
	
	private double getMeteredValue(long ts){
		double mTime = ts - randomStart[interval];
		double a;
		double g;
		
		if(mTime >= 0 && mTime < startUpTime){
			a = ((1 / startUpTime) * mTime);
			g = Math.sin( Math.PI/2 * a ); 
    		return g * maxWatt;
		} else {
			return maxWatt;  //TODO: Schwankungen
		}
	}
	
	public int getRandomNumber(int min, int max){
	    Random random = new Random();
	    return random.nextInt(max - min + 1) + min;
	}

	@Override
	public StreamClientHandler clone() {
		return new WashingMachine(this);
	}

}
