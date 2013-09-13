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
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

/**
 * sla conformance for metric update rate and scope single
 * 
 * @author Lena Eylert
 * 
 * @param <T>
 */
public class UpdateRateSourceSingleConformance<T extends IStreamObject<?>> extends AbstractSLaConformance<T> {
	/**
	 * the highest measured update rate
	 */
	private double maxUpdateRate;
	
	private T prevObj;
	
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
	public UpdateRateSourceSingleConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPhysicalQuery query) {
		super(dist, sla, query);
		this.maxUpdateRate = 0.0;
	}
	
	/**
	 * copy constructor, required for clone method
	 * @param conformance object to copy
	 */
	private UpdateRateSourceSingleConformance(UpdateRateSourceSingleConformance<T> conformance) {
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
	@Override
	public void process_next(T object, int port) {
		super.process_next(object, port);
		if (this.prevObj != null) {
			IMetaAttribute currMetadata = object.getMetadata();
			IMetaAttribute prevMetadata = this.prevObj.getMetadata();
			
			if (currMetadata instanceof ILatency && prevMetadata instanceof ILatency) {
				ILatency currLatency = (ILatency) currMetadata;
				ILatency prevLatency = (ILatency) prevMetadata;
				long diff = currLatency.getLatencyStart() - prevLatency.getLatencyStart();
				if (diff > this.maxUpdateRate) {
					this.maxUpdateRate = nanoToMilli(diff);
				}
			} else {
				throw new RuntimeException("Latency missing");
			}
		}
		this.prevObj = object;
	}

	@Override
	public double predictConformance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		// TODO Auto-generated method stub
	}

	@Override
	public AbstractSink<T> clone() {
		return new UpdateRateSourceSingleConformance<T>(this);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// nothing to do
	}
}
