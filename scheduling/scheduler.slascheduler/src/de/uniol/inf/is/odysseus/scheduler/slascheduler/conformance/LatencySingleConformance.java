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
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

/**
 * sla Conformance for metric latency and scope single
 * 
 * @author Thomas Vogelgesang
 * 
 * @param <T>
 */
public class LatencySingleConformance<T> extends AbstractSLaConformance<T> {
	/**
	 * the highest measured latency
	 */
	private double maxLatency;

	/**
	 * creates a new sla conformance for metric latency and scope single
	 * 
	 * @param dist
	 *            distributor to send events to event listeners
	 * @param sla
	 *            the related sla
	 * @param query
	 *            the related query
	 */
	public LatencySingleConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPhysicalQuery query) {
		super(dist, sla, query);
		this.maxLatency = 0;
	}

	/**
	 * copy constructor, required for clone method
	 * @param conformance object to copy
	 */
	private LatencySingleConformance(LatencySingleConformance<T> conformance) {
		super(conformance);
		this.maxLatency = conformance.maxLatency;
	}

	/**
	 * returns the conformancy as the highest measured latency
	 */
	@Override
	public double getConformance() {
		return this.maxLatency;
	}

	/**
	 * resets the measured maximum value for latency
	 */
	@Override
	public void reset() {
		super.reset();
		this.maxLatency = 0;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// nothing to do
	}

	/**
	 * measures the latency and saves it, if it exceeds the current maximum
	 */
	@Override
	protected void process_next(T object, int port) {
		super.process_next(object, port);
		MetaAttributeContainer<?> metaAttributeContainer = (MetaAttributeContainer<?>) object;
		IMetaAttribute metadata = metaAttributeContainer.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			if (latency.getLatency() > this.maxLatency) {
				this.maxLatency = nanoToMilli(latency.getLatency());
			}
		}
	}

	/**
	 * copies the object
	 */
	@Override
	public AbstractSink<T> clone() {
		return new LatencySingleConformance<T>(this);
	}

	@Override
	public double predictConformance() {
		double maxPredictedLatency = this.getMaxPredictedLatency();
		return Math.max(maxPredictedLatency, this.maxLatency);
	}

}
