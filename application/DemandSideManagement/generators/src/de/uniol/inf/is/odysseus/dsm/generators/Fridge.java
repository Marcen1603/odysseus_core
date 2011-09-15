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
public class Fridge extends StreamClientHandler{

	/*
	 * Durchschnittswerte Kühlschrank
	 * Verbrauch : 0,86 kWh
	 * Laufzeit: 30 Minuten
	 * Watt = 100 W
	 * StartUp = true
	 */
	
	Calendar calendar = Calendar.getInstance();
	private long timestamp;
	private int randomRuntimes;
	private long currentDay;
	private int interval = 0;
	
	private double maxWatt;
	private int rMin = 10;
	private int rMax = 15;
	private long [][] randomStart = new long[2][rMax];
	private int startMin = 6; // in Stunden
	private int startMax = 24; // in Stunden
	private double startUpTime = 60000.0; // in ms
	private String name = "Fridge";
	private int roomId = 1;
	private int runtimeMin = 30; // in Minuten
	private int runtimeMax = 30; // in Minuten
	
	public Fridge(double watt){
		this.maxWatt = watt;
	}
	
	public Fridge(Fridge fridge) {
		this.maxWatt = fridge.maxWatt;
	}

	@Override
	public void init() {
		calendar.setTimeInMillis(SimulationClock.getInstance().getTime());
		currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		randomRuntimes = getRandomIntNumber(rMin,rMax);
		if (randomRuntimes > 0){
			randomStart[1][0] = (long) getRandomIntNumber(runtimeMin, runtimeMax);
			randomStart[0][0] = getRandomStart(startMin, (((startMax - startMin)/randomRuntimes)+startMin));
			for(int i = 1; i < randomRuntimes; i++){
				calendar.setTimeInMillis((long) (randomStart[0][i-1]+(randomStart[1][i-1]*60000)));
				int tmpStartMin = calendar.get(Calendar.HOUR_OF_DAY);
				randomStart[0][i] = getRandomStart(tmpStartMin, (((startMax - tmpStartMin)/(randomRuntimes-i))+tmpStartMin));
				randomStart[1][i] = (long) getRandomIntNumber(runtimeMin, runtimeMax);
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
			if (timestamp >= randomStart[0][interval] && timestamp <= randomStart[0][interval]+(randomStart[1][interval]*60000)){
				tuple.addLong(timestamp);
				tuple.addString(name);
				tuple.addInteger(roomId);
				tuple.addDouble(getMeteredValue(timestamp));
			} else {
				if (timestamp >= randomStart[0][interval]+(randomStart[1][interval]*60000) && interval < randomRuntimes - 1){
					interval++;
				}
				tuple.addLong(timestamp);
				tuple.addString(name);
				tuple.addInteger(roomId);
				tuple.addDouble(0);
			}
		} else {
			tuple.addLong(timestamp);
			tuple.addString(name);
			tuple.addInteger(roomId);
			tuple.addDouble(0);
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
			randomRuntimes = getRandomIntNumber(rMin, rMax);
			if (randomRuntimes > 0){
				randomStart[1][0] = (long) getRandomIntNumber(runtimeMin, runtimeMax);
				randomStart[0][0] = getRandomStart(startMin, (((startMax - startMin)/randomRuntimes)+startMin));
				for(int i = 1; i < randomRuntimes; i++){
					calendar.setTimeInMillis((long) (randomStart[0][i-1]+(randomStart[1][i-1]*60000)));
					int tmpStartMin = calendar.get(Calendar.HOUR_OF_DAY);
					randomStart[0][i] = getRandomStart(tmpStartMin, (((startMax - tmpStartMin)/(randomRuntimes-i))+tmpStartMin));
					randomStart[1][i] = (long) getRandomIntNumber(runtimeMin, runtimeMax);
				}
			}
			interval = 0;
			return true;
		}
	}
	
	private double getMeteredValue(long ts){
		double mTime = ts - randomStart[0][interval];
		double a;
		double g;
		
		if(mTime >= 0 && mTime < startUpTime){
			a = ((1 / startUpTime) * mTime);
			g = Math.sin( Math.PI/2 * a ); 
    		return g * maxWatt;
		} else {
			g = 0.1 * Math.sin( Math.PI/(2*10) * (mTime/1000) ) + 1;
			return g * maxWatt;
		}
	}
	
	public int getRandomIntNumber(int runtimeMin, int runtimeMax){
	    Random random = new Random();
	    return random.nextInt(runtimeMax - runtimeMin + 1) + runtimeMin;
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
	
	@Override
	public StreamClientHandler clone() {
		return new Fridge(this);
	}

}
