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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
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
public class UpdateRateSourceRateConformance<R extends IStreamObject<?>, W extends IStreamObject<?>> extends AbstractSLAPipeConformance<R, W> {
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
	
	private R prevObj;
	
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
	public UpdateRateSourceRateConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPhysicalQuery query, double threshold) {
		super(dist, sla, query);
		this.numberOfViolations = 0;
		this.totalNumber = 0;
		this.updateRateThreshold = threshold;
	}
	
	/**
	 * copy constructor, required for clone method
	 * @param conformance object to copy
	 */
	private UpdateRateSourceRateConformance(UpdateRateSourceRateConformance<R, W> conformance) {
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
	protected void process_next(R object, int port) {
		super.process_next(object, port);
		
		long diff = 0;
		if (this.prevObj != null) {
			IMetaAttribute currMetadata = object.getMetadata();
			IMetaAttribute prevMetadata = this.prevObj.getMetadata();
			
			if (currMetadata instanceof ILatency && prevMetadata instanceof ILatency) {
				ILatency currLatency = (ILatency) currMetadata;
				ILatency prevLatency = (ILatency) prevMetadata;
				diff = currLatency.getLatencyStart() - prevLatency.getLatencyStart();
				if (diff > this.updateRateThreshold) {
					this.numberOfViolations++;
				}
				this.totalNumber++;
			} else {
				throw new RuntimeException("TimeIntervall missing");
			}
		}
		this.prevObj = object;
		
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
	public AbstractPipe<R, W> clone() {
		return new UpdateRateSourceRateConformance<R, W>(this);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// nothing to do
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
}
