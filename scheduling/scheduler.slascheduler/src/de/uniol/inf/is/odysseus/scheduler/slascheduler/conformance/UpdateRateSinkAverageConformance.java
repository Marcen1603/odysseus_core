/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AvgSumPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.core.server.sla.unit.TimeUnit;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

/**
 * sla conformance for metric update rate and scope average
 * 
 * @author Lena Eylert
 * 
 * @param <T>
 */
public class UpdateRateSinkAverageConformance<R extends IStreamObject<?>, W extends IStreamObject<?>> extends AbstractSLAPipeConformance<R, W> {
	/**
	 * partial aggregate for calculating the average
	 */
	private AvgSumPartialAggregate<R> aggregate;
	
//	private T prevObj;
	private long prevTime = -1;
	private long lastTupleSend;
	private R lastObjectSend;
	private int lastPortSend;
	
	private boolean isSelectivityNull = false;
	
	/**
	 * creates a new sla conformance for metric update rate and scope average
	 * 
	 * @param dist
	 *            distributor to send events to event listeners
	 * @param sla
	 *            the related sla
	 * @param query
	 *            the related query
	 */
	public UpdateRateSinkAverageConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPhysicalQuery query) {
		super(dist, sla, query);
		this.aggregate = new AvgSumPartialAggregate<R>(0.0, 0);
	}
	
	/**
	 * copy constructor, required for clone method
	 * @param conformance object to copy
	 */
	private UpdateRateSinkAverageConformance(UpdateRateSinkAverageConformance<R, W> conformance) {
		super(conformance);
		this.aggregate = conformance.aggregate.clone();
	}

	/**
	 * returns the average update rate of the elements processed by the related
	 * query
	 */
	@Override
	public double getConformance() {
		double avg = this.aggregate.getAggValue().doubleValue()
				/ this.aggregate.getCount();
		if (Double.isNaN(avg))
			return 0.0;
        return avg;
	}
	
	/**
	 * resets the aggregated average
	 */
	@Override
	public void reset() {
		super.reset();
		this.aggregate.setAggValue(0.0, 0);
	}
	
	/**
	 * measures the average update rate
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(R object, int port) {
//		super.process_next(object, port);
		
		long diff = 0;
		if (this.prevTime != -1) {
			long currTime = System.currentTimeMillis();
			diff = currTime - prevTime;
			this.aggregate.addAggValue((double)diff);
		}
		this.prevTime = System.currentTimeMillis();
		
		this.lastTupleSend = System.currentTimeMillis();
		this.lastObjectSend = object;
		this.lastPortSend = port;
		
		Tuple<?> tuple = new Tuple<>(3, false);
		tuple.addAttributeValue(0, this.getOwner().get(0).getID());
		tuple.addAttributeValue(1, diff);
		tuple.addAttributeValue(2, getConformance() >= this.getSLA().getMetric().getValue());
		
		super.process_next((R) tuple, port);
//		super.process_next(object, port);
		
//		if (this.prevObj != null) {
//			IMetaAttribute currMetadata = object.getMetadata();
//			IMetaAttribute prevMetadata = this.prevObj.getMetadata();
//			
//			if (currMetadata instanceof ILatency && prevMetadata instanceof ILatency) {
//				ILatency currLatency = (ILatency) currMetadata;
//				ILatency prevLatency = (ILatency) prevMetadata;
//				long diff = currLatency.getLatencyStart() - prevLatency.getLatencyStart();
//				this.aggregate.addAggValue(nanoToMilli(diff));
//			} else {
//				throw new RuntimeException("Latency missing");
//			}
//		}
//		this.prevObj = object;
	}

	@Override
	public double predictConformance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// heartbeat received
		// TODO: Selectivity Listener fuer vorangehende Operatoren
		// wenn Selektivitaet 0 und und seit x Zeiteineheiten (Updaterate) 
		// nichts gesendet wurde dann z. B. das letzte nochmal senden
		
		long buffer = 1000;
		TimeUnit unit = null;
		if (this.getSLA().getMetric().getUnit() instanceof TimeUnit)
			unit = (TimeUnit) this.getSLA().getMetric().getUnit();

		if (unit != null) {
			switch (unit) {
			case ms:
				buffer = 1000;
				break;
			case s:
				buffer = 1000;
				break;
			case m:
				buffer = 1000;
				break;
			case h:
				buffer = 1000;
				break;
			case d:
				buffer = 1000;
				break;
			case months:
				buffer = 1000;
				break;
			}
		}
		
		// check update rate
		if ((System.currentTimeMillis() - this.lastTupleSend) > (this.getSLA().getMetric().getValue() - buffer)) {
			// check if selectivity of at least one operator is 0
			parseQuery(this);
			if (isSelectivityNull) {
				// unindebted sla violation -> send last object again
				process_next(lastObjectSend, lastPortSend);
			}
		}
		
		
//		for (PhysicalSubscription<? extends ISource<?>> s : ((ISink<?>) this)
//				.getSubscribedToSource()) {
//			ISource<?> source = s.getTarget();
////			source.getMonitoringData(MonitoringDataTypes.SELECTIVITY.name);
//			if (getSelectivityMetadata(source) == 0.0 && (System.currentTimeMillis() - this.lastTupleSend) > this.getSLA().getMetric().getValue()) {
//				// unindebted sla violation -> send last object again
//				process_next(lastObjectSend, lastPortSend);
//			}
//		}
	}
	
	private void parseQuery(IPhysicalOperator operator) {
		if (operator.isSink()) {
			@SuppressWarnings("unchecked")
			ISink<R> sink = (ISink<R>) operator;
			Collection<PhysicalSubscription<ISource<? extends R>>> sources = sink
					.getSubscribedToSource();
			for (PhysicalSubscription<ISource<? extends R>> sub : sources) {
				if (sub.getTarget().isSink()) {
					if (getSelectivityMetadata(sub.getTarget()) == 0.0) {
						isSelectivityNull = true;
						break;
					} else { 
						parseQuery(sub.getTarget());
					}
				} //else {
				//	parse(sub.getTarget(), sub);
				//}
			}
		} else {
			if (getSelectivityMetadata(operator) == 0.0)
				isSelectivityNull = true;
		}
	}
	
	private static double getSelectivityMetadata(IPhysicalOperator operator) {
		try {
			if (operator.isOpen()) {
				IMonitoringData<Double> selectivityMonitoringData = operator.getMonitoringData(MonitoringDataTypes.SELECTIVITY.name);
				if (selectivityMonitoringData != null) {
					Double selectivity = selectivityMonitoringData.getValue();
					if (selectivity == null || Double.isNaN(selectivity)) {
						return -1.0;
					}
					
					return selectivity;
				}
			}
			return -1.0;
		} catch (NullPointerException ex) {
			return -1.0;
		}
	}

	/**
	 * copy object
	 */
	@Override
	public AbstractPipe<R, W> clone() {
		return new UpdateRateSinkAverageConformance<R, W>(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

}
