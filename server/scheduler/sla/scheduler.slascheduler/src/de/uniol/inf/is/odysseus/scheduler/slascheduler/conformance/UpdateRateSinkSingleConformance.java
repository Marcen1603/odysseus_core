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
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.core.server.sla.ServiceLevel;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAHelper;

/**
 * sla conformance for metric update rate and scope single
 * 
 * @author Lena Eylert
 * 
 * @param <T>
 */
public class UpdateRateSinkSingleConformance<R extends IStreamObject<?>, W extends IStreamObject<?>>
		extends AbstractSLAPipeConformance<R, W> {
	/**
	 * the highest measured update rate
	 */
	private double maxUpdateRate;

	private long prevTime = -1;
	private long lastTupleSend;
	private R lastObjectSend;
	private int lastPortSend;
	
	private boolean isSelectivityNull = false;
	private long currentWindowStart = 0;

	/**
	 * creates a new sla conformance for metric update rate and scope single
	 * 
	 * @param dist
	 *            distributor to send events to event listeners
	 * @param sla
	 *            the related sla
	 * @param query
	 *            the related query
	 */
	public UpdateRateSinkSingleConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPhysicalQuery query) {
		super(dist, sla, query);
		this.maxUpdateRate = 0.0;
	}

	/**
	 * copy constructor, required for clone method
	 * 
	 * @param conformance
	 *            object to copy
	 */
	private UpdateRateSinkSingleConformance(
			UpdateRateSinkSingleConformance<R, W> conformance) {
		super(conformance);
		this.maxUpdateRate = conformance.maxUpdateRate;
	}

	/**
	 * returns the conformance as the highest measured update rate
	 */
	@Override
	public double getConformance() {
		return this.maxUpdateRate;
	}

	/**
	 * resets the measured maximum value for update rate
	 */
	@Override
	public void reset() {
		super.reset();
		this.maxUpdateRate = 0;
	}

	/**
	 * measures the update rate and saves it, if it exceeds the current maximum
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(R object, int port) {
		long diff = 0;
		if (this.prevTime != -1) {
			long currTime = System.currentTimeMillis();
			diff = currTime - prevTime;
			if (diff > this.maxUpdateRate) {
				this.maxUpdateRate = diff;
			}
		}
		this.prevTime = System.currentTimeMillis();
		
		this.lastTupleSend = System.currentTimeMillis();
		this.lastObjectSend = object;
		this.lastPortSend = port;
		
		long latencyValue = -1;
		IMetaAttribute metadata = object.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			latency.setLatencyEnd(System.nanoTime());
			latencyValue = (long) this.nanoToMilli(latency.getLatency());
		} else {
			throw new RuntimeException("Latency missing");
		}
		
		if (SLAHelper.isTestWorkaroundEnabled()) {
			if (currentWindowStart == 0)
				currentWindowStart = System.currentTimeMillis();
			
			long passedTime = System.currentTimeMillis() - currentWindowStart;
			if (passedTime >= this.getSLA().getWindow().lengthToMilliseconds()) {
				currentWindowStart = System.currentTimeMillis();
				checkViolation();
			}
		}
		
		int oldAttributeCount = ((Tuple<?>)object).getAttributes().length;
		((Tuple<?>)object).append(this.getOwner().get(0).getID(), false);
		((Tuple<?>)object).append(diff, false);
		((Tuple<?>)object).append(latencyValue, false);
		int[] attrList = new int[3];
		int index = 0;
		for (int i = oldAttributeCount; i < oldAttributeCount+3; i++) {
			attrList[index] = i;
			index++;
		}
		Tuple<?> tuple = ((Tuple<?>)object).restrict(attrList, true);
		
		transfer((W) tuple);
		super.process_next((R) tuple, port);
	}

	@Override
	public double predictConformance() {
		// TODO Auto-generated method stub
		return this.getConformance();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// wenn Selektivitaet 0 und und seit x Zeiteinheiten (Updaterate) nichts gesendet wurde dann z. B. das letzte nochmal senden
		
		long buffer = SLAHelper.getHeartbeatInterval();

		boolean violated = false;
		List<ServiceLevel> serviceLevels = this.getSLA().getServiceLevel();
		double currentThreshold = serviceLevels.get(0).getThreshold();
		for (int i = serviceLevels.size() - 1; i >= 0 && !violated; i--) {
			if (serviceLevels.get(i).getThreshold() < (System.currentTimeMillis() - this.lastTupleSend)) {
				violated = true;
				currentThreshold = serviceLevels.get(i).getThreshold();
			}
		}

		// check update rate
		if ((System.currentTimeMillis() - this.lastTupleSend) > (currentThreshold - buffer)) {
			// check if selectivity of at least one operator is 0
			parseQuery(this);
			if (isSelectivityNull) {
				// unindebted sla violation -> send last object again
				if (lastObjectSend != null) {
					process_next(lastObjectSend, lastPortSend);
//					System.out.println("Sending alternative tuple (sla)");
				}
			}
		}
	}

	private void parseQuery(IPhysicalOperator operator) {
		if (operator.isSink()) {
			@SuppressWarnings("unchecked")
			ISink<R> sink = (ISink<R>) operator;
			Collection<AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?>> sources = sink
					.getSubscribedToSource();
			for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?> sub : sources) {
				if (sub.getSource().isSink()) {
					if (getSelectivityMetadata(sub.getSource()) == 0.0) {
						isSelectivityNull = true;
						break;
					} else { 
						parseQuery(sub.getSource());
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

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

}
