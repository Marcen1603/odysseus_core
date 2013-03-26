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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;

/**
* @author Andreas Harre, Philipp Rudolph, Jan Sören Schwarz
*/
public class HeatMapPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private long lastSend;
	private int x;
	private int y;
	private int xLength;
	private int yLength;
	private String xAttribute;
	private String yAttribute;
	private String valueAttribute;
	private SDFSchema inputSchema;
	private double[][] map;
	private int a = 0;
	private int rowAtt = -1;
	private int colAtt = -1;
	private int valAtt = -1;
	private int tsAtt = -1;
	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();
	
	public HeatMapPO(int x, int y, int xLength, int yLength,
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
		this.map = new double[this.x][this.y];	
		for(int i=0; i < x; i++) {
			for(int j=0; j < y; j++) {
				this.map[i][j] = 0.0;
			}
		}
		initSchema();
	}
	
	public HeatMapPO(HeatMapPO<T> po){
		this.x = po.x;
		this.y = po.y;
		this.xLength = po.xLength;
		this.yLength = po.yLength;
		this.inputSchema = po.inputSchema;
		this.xAttribute = po.xAttribute;
		this.yAttribute = po.yAttribute;
		this.valueAttribute = po.valueAttribute;
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
		int xCoord = -1;
		int yCoord = -1;
		if(rowAtt >= 0 && colAtt >= 0 && valAtt >= 0 && tsAtt >= 0) {
			Integer xValue = (Integer) tuple.getAttribute(rowAtt);
			xCoord = (int) Math.floor(xValue / (52477 / x));
			Integer yValue = (Integer) tuple.getAttribute(colAtt);
			yCoord = (int) Math.floor(yValue / (67925 / y));
			Double value = (Double) tuple.getAttribute(valAtt);
			if (xCoord < x && xCoord >= 0 && yCoord < y && yCoord >= 0) {
				map[xCoord][yCoord] = map[xCoord][yCoord] + value;
				if((((Long) tuple.getAttribute(tsAtt)) - lastSend) > 1000000000000L) {
					lastSend = (Long) tuple.getAttribute(tsAtt);
					Double test = 0.0;
					for(int i = 0; i < this.x; i++) {
						for(int j = 0; j < this.y; j++) {
							Object[] attributes = new Object[7];
							attributes[0] = tuple.getAttribute(tsAtt);
							attributes[1] = tuple.getAttribute(1);
							attributes[2] = (xLength / x) * i;
							attributes[3] = (yLength / y) * (j + 1);
							attributes[4] = (xLength / x) * (i + 1);
							attributes[5] = (yLength / y) * j;
							attributes[6] = map[i][j];
							test = test + map[i][j];
							Tuple outputTuple = new Tuple<>(attributes, true);
							outputTuple.setMetadata(tuple.getMetadata());
							transfer((T) outputTuple);
						}
					}
//					System.out.println("Gesamt-Map-Zeit: " + test);
//					System.out.println("Zeit: " + ((Long) tuple.getAttribute(tsAtt) - 10748401988186756L));
				}
			} else {
//				System.out.println("FEHLER!!!!!!");
			}
		}
	}
	
	@Override
	public void process_open() throws OpenFailedException{
		
	}
	
	@Override
	public HeatMapPO<T> clone() {
		return new HeatMapPO<T>(this);
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
	
//	@Override
//	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
//		if(!(ipo instanceof HeatMapPO<?>)) {
//			return false;
//		}
//		@SuppressWarnings("unchecked")
//		HeatMapPO<T> spo = (HeatMapPO<T>) ipo;
//		// Different sources
//		if(!this.hasSameSources(spo)) return false;
//		// Predicates match
//		if(this.predicate.equals(spo.getPredicate())
//				|| (this.predicate.isContainedIn(spo.getPredicate()) && spo.getPredicate().isContainedIn(this.predicate))) {
//			return true;
//		}
//
//		return false;
//	}
	
//	@Override
//	@SuppressWarnings({"rawtypes"})
//	public boolean isContainedIn(IPipe<T,T> ip) {
//		if(!(ip instanceof SelectPO) || !this.hasSameSources(ip)) {
//			return false;
//		}
//		// Sonderfall, dass das Prädikat des anderen SelectPOs ein OrPredicate ist und das Prädikat von diesem SelectPO nicht.
//		if((ComplexPredicateHelper.isOrPredicate(((SelectPO)ip).getPredicate()) && !ComplexPredicateHelper.isOrPredicate(this.predicate))) {
//			return ComplexPredicateHelper.contains(((SelectPO)ip).getPredicate(), this.predicate);
//		}
//		if(this.predicate.isContainedIn(((SelectPO<T>)ip).predicate)) {
//			return true;
//		}
//		return false;
//	}
}

