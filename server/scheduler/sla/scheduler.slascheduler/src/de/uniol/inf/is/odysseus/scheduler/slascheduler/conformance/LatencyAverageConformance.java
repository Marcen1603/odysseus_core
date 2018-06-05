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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AvgSumPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

/**
 * Sla conformance for metric latency and scope average
 * 
 * @author Thomas Vogelgesang
 * 
 * @param <T>
 */
public class LatencyAverageConformance<T extends IStreamObject<?>> extends AbstractSLaConformance<T> {
	/**
	 * partial aggregate for calculating teh average
	 */
	private AvgSumPartialAggregate<T> aggregate;

	/**
	 * creates a new sla conformance obejct for metric latency and scope average
	 * 
	 * @param dist
	 *            reference to the event ditributor to pass generated events
	 * @param sla
	 *            teh related sla
	 * @param query
	 *            the related query
	 */
	public LatencyAverageConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPhysicalQuery query) {
		super(dist, sla, query);
		this.aggregate = new AvgSumPartialAggregate<T>(0.0, 0);
	}

	/**
	 * copy constructor for clone method
	 * 
	 * @param conformance
	 *            the object to clone
	 */
	private LatencyAverageConformance(LatencyAverageConformance<T> conformance) {
		super(conformance);
		this.aggregate = conformance.aggregate.clone();
	}

	/**
	 * returns the average latency of the elements processed by the related
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

	@Override
	public double predictConformance() {
		double sumPredictedLatency = this.getSumPredictedLatency();
		double numberOfPredictedLatency = this.getNumberOfPredictedLatency();
		double conformance = (this.aggregate.getAggValue() + sumPredictedLatency)
				/ (this.aggregate.getCount() + numberOfPredictedLatency);
		if (Double.isNaN(conformance))
			return 0.0;
        return conformance;
	}

	/**
	 * resets the aggregated average
	 */
	@Override
	public void reset() {
		super.reset();
		this.aggregate.setAggValue(0.0, 0);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// nothing to do
	}

	/**
	 * measures the average tuple latency
	 */
	@Override
	protected void process_next(T object, int port) {
		super.process_next(object, port);
		IMetaAttribute metadata = object.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			this.aggregate.addAggValue(this.nanoToMilli(latency
					.getLatency()));
		} else {
			throw new RuntimeException("Latency missing");
		}
	}

	/**
	 * copy object
	 */
	@Override
	public AbstractSink<T> clone() {
		return new LatencyAverageConformance<T>(this);
	}
}
