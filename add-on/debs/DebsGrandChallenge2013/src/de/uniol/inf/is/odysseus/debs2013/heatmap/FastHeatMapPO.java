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
package de.uniol.inf.is.odysseus.debs2013.heatmap;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;

/**
 * Operator for the DEBS Grand Challenge 2013. 
 * 
* @author Jan Sören Schwarz
*/
//public class HeatMapPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
public class FastHeatMapPO<K extends ITimeInterval, T extends IStreamObject<K>> extends AbstractPipe<T, T> {

	private long lastSend;
	private int x;
	private int y;
	private int xLength;
	private int yLength;
	private String xAttribute;
	private String yAttribute;
	private String valueAttribute;
	private SDFSchema inputSchema;
	private ArrayList<FastCoord> events;
	private double[][] map;
	private int a = 0;
	private int rowAtt = -1;
	private int colAtt = -1;
	private int valAtt = -1;
	private int tsAtt = -1;
	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();
	
	public FastHeatMapPO(int x, int y, int xLength, int yLength,
						String xAttribute, String yAttribute,
						String valueAttribute, SDFSchema inputSchema){
		this.x = x;
		this.y = y;
		this.xLength = xLength;
		this.yLength = yLength;
		this.xAttribute = xAttribute;
		this.yAttribute = yAttribute;
		this.valueAttribute = valueAttribute;
		this.inputSchema = inputSchema;
		this.events = new ArrayList<FastCoord>();
		this.map = new double[this.x][this.y];	
		for(int i=0; i < x; i++) {
			for(int j=0; j < y; j++) {
				this.map[i][j] = 0.0;
			}
		}
		initSchema();
	}
	
	public FastHeatMapPO(FastHeatMapPO<K, T> po){
		this.x = po.x;
		this.y = po.y;
		this.xLength = po.xLength;
		this.yLength = po.yLength;
		this.inputSchema = po.inputSchema;
		this.xAttribute = po.xAttribute;
		this.yAttribute = po.yAttribute;
		this.valueAttribute = po.valueAttribute;
		this.events = po.events;
		this.map = po.map;
		this.heartbeatGenerationStrategy = po.heartbeatGenerationStrategy.clone();
		initSchema();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void process_next(T object, int port) {
		Tuple tuple = (Tuple) object;
		long endTS = object.getMetadata().getEnd().getMainPoint();
		long startTS = object.getMetadata().getStart().getMainPoint();
		int xCoord = -1;
		int yCoord = -1;
		
		// aussortieren was älter ist als ts
		if(endTS >= 0) {
			int remove = 0;
			for(FastCoord event: events) {
				if(event.getEndTS() < startTS) {
					map[event.getX()][event.getY()] = map[event.getX()][event.getY()] - event.getValue();
					remove++;
				} else {
					break;
				}
			}
			for(int i = 0; i < remove; i++) {
				events.remove(0);
			}
		}
		// get data out of tuple
		if(rowAtt >= 0 && colAtt >= 0 && valAtt >= 0 && tsAtt >= 0) {
			Integer xValue = (Integer) tuple.getAttribute(rowAtt);
			xCoord = (int) Math.floor(xValue / (52477 / x));
			Integer yValue = (Integer) tuple.getAttribute(colAtt);
			yCoord = (int) Math.floor(yValue / (67925 / y));
			Double value = (Double) tuple.getAttribute(valAtt);
			//if the maxValue is reached:
			if(xCoord == x) {
				xCoord--;
			}
			if(yCoord == y) {
				yCoord--;
			}
			if (xCoord < x && xCoord >= 0 && yCoord < y && yCoord >= 0) {
				map[xCoord][yCoord] = map[xCoord][yCoord] + value;
				events.add(new FastCoord(xCoord, yCoord, value, endTS));
			} 
		}

		//senden, falls Sekunde vorbei ist:
		if((((Long) tuple.getAttribute(tsAtt)) - lastSend) >= 1000000000000L) {
			lastSend = (Long) tuple.getAttribute(tsAtt);
			Object[] attributes = new Object[this.x * this.y * 5 + 2];
			
			double totalTime = 0;
			for(int i = 0; i < this.x; i++) {
				for(int j = 0; j < this.y; j++) {
					totalTime += map[i][j];
				}
			}
			
			if(totalTime > 0) {
				attributes[0] = tuple.getAttribute(tsAtt);
				attributes[1] = tuple.getAttribute(1);
				for(int i = 0; i < this.x; i++) {
					for(int j = 0; j < this.y; j++) {
						attributes[2 + i*5*this.y + j*5] = ((xLength / x) * i);
	//					added -33960 for correct visualization
						attributes[3 + i*5*this.y + j*5] = ((yLength / y) * (j + 1))-33960;
						attributes[4 + i*5*this.y + j*5] = ((xLength / x) * (i + 1));
	//					added -33960 for correct visualization
						attributes[5 + i*5*this.y + j*5] = ((yLength / y) * j)-33960;
						attributes[6 + i*5*this.y + j*5] = map[i][j] / totalTime;
					}
				}
				Tuple outputTuple = new Tuple<>(attributes, true);
				outputTuple.setMetadata(tuple.getMetadata());
				transfer((T) outputTuple);
			}
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	@Override
	public void process_open() throws OpenFailedException{
		
	}	

	public IHeartbeatGenerationStrategy<T> getHeartbeatGenerationStrategy() {
		return heartbeatGenerationStrategy;
	}

	public void setHeartbeatGenerationStrategy(
			IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy) {
		this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
	}
	
	public void initSchema() {
		a = 0;
		for(SDFAttribute attribute: inputSchema.getAttributes()) {
			if(attribute.getAttributeName().equals(this.xAttribute)) {
				rowAtt = a;
			} else if(attribute.getAttributeName().equals(this.yAttribute)) {
				colAtt = a;
			} else if(attribute.getAttributeName().equals(this.valueAttribute)) {
				valAtt = a;
			} else if(attribute.getAttributeName().equals("ts")) {
				tsAtt = a;
			}
			a++;
		}
	}
}

class FastCoord {
	private int x;
	private int y;
	private double value;
	private long endTS;
	
	public FastCoord(int x, int y, double value, long endTS) {
		this.x = x;
		this.y = y;
		this.value = value;
		this.endTS = endTS;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

	public long getEndTS() {
		return endTS;
	}
	public void setEndTS(long endTS) {
		this.endTS = endTS;
	}
}

