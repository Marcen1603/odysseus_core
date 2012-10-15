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
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

/**
 * Sla conformance for metric latency and scope rate
 * 
 * @author Thomas Vogelgesang
 * 
 * @param <T>
 */
public class LatencyRateConformance<T extends IStreamObject<?>> extends AbstractSLaConformance<T> {
	/**
	 * counts the number of violating the specified latency
	 */
	private int numberOfViolations;
	/**
	 * counts the number of all processed elements
	 */
	private int totalNumber;
	/**
	 * threshold of the latency that should not be exceeded
	 */
	private double latencyThreshold;

	/**
	 * creates a new sla conformance for metric latency and scope number
	 * 
	 * @param dist
	 *            event distributor to send generated events to event listener
	 * @param sla
	 *            the related sla
	 * @param query
	 *            the related query
	 * @param latencyThreshold
	 *            latency threshold that should not be exceeded
	 */
	public LatencyRateConformance(ISLAViolationEventDistributor dist, SLA sla,
			IPhysicalQuery query, double latencyThreshold) {
		super(dist, sla, query);
		this.numberOfViolations = 0;
		this.totalNumber = 0;
		this.latencyThreshold = latencyThreshold;
	}

	/**
	 * copy constructor, required for clone
	 * 
	 * @param conformance
	 *            object to clone
	 */
	private LatencyRateConformance(LatencyRateConformance<T> conformance) {
		super(conformance);
		this.numberOfViolations = conformance.numberOfViolations;
		this.latencyThreshold = conformance.latencyThreshold;
		this.totalNumber = conformance.totalNumber;
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

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// nothing to do
	}

	/**
	 * measures the latency and counts how often it exceeds the threshold
	 */
	@Override
	protected void process_next(T object, int port) {
		super.process_next(object, port);
		IMetaAttribute metadata = object.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			if (this.nanoToMilli(latency.getLatency()) > latencyThreshold) {
				this.numberOfViolations++;
			}
			this.totalNumber++;
		}
	}

	/**
	 * returns a copy of the object
	 */
	@Override
	public AbstractSink<T> clone() {
		return new LatencyRateConformance<T>(this);
	}

	@Override
	public double predictConformance() {
		double conformance = 0.0;
		double numberViolationsPredicted = this
				.getNumberOfViolationsPredictedLatency();
		double numberPredicted = this.getNumberOfPredictedLatency();
		conformance = (this.totalNumber + numberPredicted - this.numberOfViolations - numberViolationsPredicted)
				/ (numberPredicted + this.totalNumber);
		if (Double.isNaN(conformance))
			return 0.0;
        return conformance;
	}

}
