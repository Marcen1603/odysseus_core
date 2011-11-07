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
public class Appliance extends StreamClientHandler{
	
	Calendar calendar = Calendar.getInstance();
	private long timestamp;
	private int randomRuntimes;
	private long currentDay;
	private int interval = 0;
	private long [][] randomTimes;
	
	
	private double watt;
	private int rMin; //min Starts pro Tag
	private int rMax; //max Starts pro Tag
	private int startMin; // in Stunden
	private int startMax; // in Stunden
	private double startUpTime; // in ms
	private String name;
	private int roomId;
	private int runtimeMin; // in Minuten
	private int runtimeMax; // in Minuten
	private double iRange;
	private double iLength;
	private int speed;
	private double measuredWatt = 0;
	private long measureTimestamp;
	
	private int id;
	private int evuId;
	
	public Appliance(int id, int evuId, String name, double watt, int runtimeMin, int runtimeMax, double iRange, double iLength, int speed, int rMin, int rMax, int startMin, int startMax, double startUpTime, int roomId){
		this.id = id;
		this.evuId = evuId;
		this.name = name;
		this.watt = watt;
		this.runtimeMin = runtimeMin;
		this.runtimeMax = runtimeMax;
		this.iRange = iRange;
		this.iLength = iLength * 10;
		this.speed = speed;
		this.rMin = rMin;
		this.rMax = rMax;
		this.startMin = startMin;
		this.startMax = startMax;
		this.startUpTime = startUpTime;
		this.roomId = roomId;
	}
	
	public Appliance(Appliance appliance) {
		this.id = appliance.id;
		this.evuId = appliance.evuId;
		this.name = appliance.name;
		this.watt = appliance.watt;
		this.runtimeMin = appliance.runtimeMin;
		this.runtimeMax = appliance.runtimeMax;
		this.iRange = appliance.iRange;
		this.iLength = appliance.iLength;
		this.speed = appliance.speed;
		this.rMin = appliance.rMin;
		this.rMax = appliance.rMax;
		this.startMin = appliance.startMin;
		this.startMax = appliance.startMax;
		this.startUpTime = appliance.startUpTime;
		this.roomId = appliance.roomId;
	}

	@Override
	public void init() {
		randomTimes = new long[2][rMax];
		calendar.setTimeInMillis(SimulationClock.getInstance().getTime());
		currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		randomRuntimes = getRandomNumber(rMin,rMax);
		if (randomRuntimes > 0){
			randomTimes[1][0] = (long) getRandomNumber(runtimeMin, runtimeMax);
			randomTimes[0][0] = getRandomStart(startMin, (((startMax - startMin)/randomRuntimes)+startMin));
			for(int i = 1; i < randomRuntimes; i++){
				calendar.setTimeInMillis((long) (randomTimes[0][i-1]+(randomTimes[1][i-1]*60000)));
				int tmpStartMin = calendar.get(Calendar.HOUR_OF_DAY);
				randomTimes[0][i] = getRandomStart(tmpStartMin, (((startMax - tmpStartMin)/(randomRuntimes-i))+tmpStartMin));
				randomTimes[1][i] = (long) getRandomNumber(runtimeMin, runtimeMax);
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
			if (timestamp >= randomTimes[0][interval] && timestamp <= randomTimes[0][interval]+(randomTimes[1][interval]*60000)){
				tuple.addLong(timestamp);
				tuple.addString(name);
//				tuple.addInteger(id);
//				tuple.addInteger(evuId);
//				tuple.addInteger(roomId);
				tuple.addDouble(getMeteredValue(timestamp));
//				tuple.addDouble(getMeteredConsumption(timestamp));
			} else {
				if (timestamp >= randomTimes[0][interval]+(randomTimes[1][interval]*60000) && interval < randomRuntimes - 1){
					interval++;
				}
				tuple.addLong(timestamp);
				tuple.addString(name);
//				tuple.addInteger(id);
//				tuple.addInteger(evuId);
//				tuple.addInteger(roomId);
				tuple.addDouble(0);
//				tuple.addDouble(0);
			}
		} else {
			tuple.addLong(timestamp);
			tuple.addString(name);
//			tuple.addInteger(id);
//			tuple.addInteger(evuId);
//			tuple.addInteger(roomId);
			tuple.addDouble(0);
//			tuple.addDouble(0);
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
				randomTimes[1][0] = (long) getRandomNumber(runtimeMin, runtimeMax);
				randomTimes[0][0] = getRandomStart(startMin, (((startMax - startMin)/randomRuntimes)+startMin));
				for(int i = 1; i < randomRuntimes; i++){
					calendar.setTimeInMillis((long) (randomTimes[0][i-1]+(randomTimes[1][i-1]*60000)));
					int tmpStartMin = calendar.get(Calendar.HOUR_OF_DAY);
					randomTimes[0][i] = getRandomStart(tmpStartMin, (((startMax - tmpStartMin)/(randomRuntimes-i))+tmpStartMin));
					randomTimes[1][i] = (long) getRandomNumber(runtimeMin, runtimeMax);
				}
			}
			interval = 0;
			return true;
		}
	}
	
	private double getMeteredValue(long ts){
		double mTime = ts - randomTimes[0][interval];
		double a;
		double g;
		
		if(mTime >= 0 && mTime < startUpTime){
			a = ((1 / startUpTime) * mTime);
			g = Math.sin( Math.PI/2 * a ); 
    		return g * watt;
		} else {
			g = iRange * Math.sin( Math.PI/(iLength) * (mTime/1000) ) + 1;
			return g * watt;
		}
	}
	
	private double getMeteredConsumption(long ts){
		double mTime = ts - randomTimes[0][interval];
		double a;
		double g;
		double returnValue;
		long mIntervall;
			if(measureTimestamp > 0){
				mIntervall = ts - measureTimestamp;
			} else {
				mIntervall = 0;
			}
		measureTimestamp = ts;
		
		if(mTime >= 0 && mTime < startUpTime){
			a = ((1 / startUpTime) * mTime);
			g = Math.sin( Math.PI/2 * a );
			returnValue = measuredWatt * mIntervall;
			measuredWatt = g * watt;
    		return returnValue;
		} else {
			g = iRange * Math.sin( Math.PI/(iLength) * (mTime/1000) ) + 1;
			returnValue = measuredWatt * mIntervall;
			measuredWatt = g * watt;
    		return returnValue;
		}
	}
	
	public int getRandomNumber(int min, int max){
	    Random random = new Random();
	    return random.nextInt(max - min + 1) + min;
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
		return new Appliance(this);
	}

}
