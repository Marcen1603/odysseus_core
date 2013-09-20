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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

/**
 * sla conformance for metric update rate and scope rate
 * 
 * @author Lena Eylert
 * 
 * @param <T>
 */
public class UpdateRateSinkRateConformance<T extends IStreamObject<?>> extends
		AbstractSLaConformance<T> {
	/**
	 * counts the number of violating the specified update rate
	 */
	private int numberOfViolations;
	/**
	 * counts the number of all processed elements
	 */
	private int totalNumber;
	/**
	 * threshold of the update rate that should not be exceeded
	 */
	private double updateRateThreshold;

	private long prevTime = -1;
	private long lastTupleSend;
	private T lastObjectSend;
	private int lastPortSend;

	private boolean isSelectivityNull = false;

	/**
	 * creates a new sla conformance for metric update rate and scope rate
	 * 
	 * @param dist
	 *            distributor to send events to event listeners
	 * @param sla
	 *            the related sla
	 * @param query
	 *            the related query
	 * @param threshold
	 *            update rate threshold that should not be exceeded
	 */
	public UpdateRateSinkRateConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPhysicalQuery query, double threshold) {
		super(dist, sla, query);
		this.numberOfViolations = 0;
		this.totalNumber = 0;
		this.updateRateThreshold = threshold;
	}

	/**
	 * copy constructor, required for clone method
	 * 
	 * @param conformance
	 *            object to copy
	 */
	private UpdateRateSinkRateConformance(
			UpdateRateSinkRateConformance<T> conformance) {
		super(conformance);
		this.numberOfViolations = conformance.numberOfViolations;
		this.totalNumber = conformance.totalNumber;
		this.updateRateThreshold = conformance.updateRateThreshold;
	}

	/**
	 * returns the conformance as the number of violations
	 */
	@Override
	public double getConformance() {
		if (this.totalNumber != 0) {
			return (double) (this.totalNumber - this.numberOfViolations)
					/ (double) this.totalNumber;
		}
		return 1.0;
	}

	/**
	 * resets the counter for the number of violations
	 */
	@Override
	public void reset() {
		super.reset();
		this.numberOfViolations = 0;
		this.totalNumber = 0;
	}

	/**
	 * measures the update rate and counts how often it exceeds the threshold
	 */
	@Override
	protected void process_next(T object, int port) {
		super.process_next(object, port);

		long diff = 0;
		if (this.prevTime != -1) {
			long currTime = System.currentTimeMillis();
			diff = currTime - prevTime;
			if (diff > this.updateRateThreshold) {
				this.numberOfViolations++;
			}
			this.totalNumber++;
		}
		this.prevTime = System.currentTimeMillis();
		
		this.lastTupleSend = System.currentTimeMillis();
		this.lastObjectSend = object;
		this.lastPortSend = port;
		
		Tuple<?> tuple = new Tuple<>(3, false);
		tuple.addAttributeValue(0, this.getOwner().get(0).getID());
		tuple.addAttributeValue(1, diff);
		tuple.addAttributeValue(2, getConformance() >= this.getSLA().getMetric().getValue());
		
//		super.process_next((T) tuple, port);
	}

	@Override
	public double predictConformance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// heartbeat received
		// TODO: Selectivity Listener fuer vorangehenden Operator
		// wenn Selektivitaet 0 und und seit x Zeiteineheiten (Updaterate)
		// nichts gesendet wurde dann z. B. das letzte nochmal senden

		long buffer = 1000;

		// check update rate
		if ((System.currentTimeMillis() - this.lastTupleSend) > (this.getSLA()
				.getMetric().getValue() - buffer)) {
			// check if selectivity of at least one operator is 0
			parseQuery(this);
			if (isSelectivityNull) {
				// unindebted sla violation -> send last object again
				process_next(lastObjectSend, lastPortSend);
			}
		}
	}

	private void parseQuery(IPhysicalOperator operator) {
		if (operator.isSink()) {
			@SuppressWarnings("unchecked")
			ISink<T> sink = (ISink<T>) operator;
			Collection<PhysicalSubscription<ISource<? extends T>>> sources = sink
					.getSubscribedToSource();
			for (PhysicalSubscription<ISource<? extends T>> sub : sources) {
				if (sub.getTarget().isSink()) {
					if (getSelectivityMetadata(sub.getTarget()) == 0.0) {
						isSelectivityNull = true;
						break;
					} else {
						parseQuery(sub.getTarget());
					}
				}
			}
		} else {
			if (getSelectivityMetadata(operator) == 0.0)
				isSelectivityNull = true;
		}
	}

	private static double getSelectivityMetadata(IPhysicalOperator operator) {
		try {
			if (operator.isOpen()) {
				IMonitoringData<Double> selectivityMonitoringData = operator
						.getMonitoringData(MonitoringDataTypes.SELECTIVITY.name);
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

	@Override
	public AbstractSink<T> clone() {
		return new UpdateRateSinkRateConformance<T>(this);
	}

}
