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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

/**
 * sla conformance for metric update rate and scope number
 * 
 * @author Lena Eylert
 * 
 * @param <T>
 */
public class UpdateRateSinkNumberConformance<T extends IStreamObject<?>> extends AbstractSLaConformance<T> {
	/**
	 * counts the number of violating the specified update rate
	 */
	private int numberOfViolations;
	/**
	 * threshold of the update rate that should not be exceeded
	 */
	private double updateRateThreshold;
	
//	private T prevObj;
	private long prevTime = -1;
	
	/**
	 * creates a new sla conformance for metric update rate and scope number
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
	public UpdateRateSinkNumberConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPhysicalQuery query, double threshold) {
		super(dist, sla, query);
		this.numberOfViolations = 0;
		this.updateRateThreshold = threshold;
	}
	
	/**
	 * copy constructor, required for clone method
	 * @param conformance object to copy
	 */
	private UpdateRateSinkNumberConformance(UpdateRateSinkNumberConformance<T> conformance) {
		super(conformance);
		this.numberOfViolations = conformance.numberOfViolations;
	}

	/**
	 * returns the conformance as the number of violations
	 */
	@Override
	public double getConformance() {
		return this.numberOfViolations;
	}
	
	/**
	 * resets the number of violations
	 */
	@Override
	public void reset() {
		super.reset();
		this.numberOfViolations = 0;
	}
	
	/**
	 * measures the update rate and counts how often it exceeds the threshold
	 */
	@Override
	protected void process_next(T object, int port) {
		super.process_next(object, port);
		
		if (this.prevTime != -1) {
			long currTime = System.currentTimeMillis();
			long diff = currTime - prevTime;
			if (diff > this.updateRateThreshold) {
				this.numberOfViolations++;
			}
		}
		this.prevTime = System.currentTimeMillis();
		
//		if (this.prevObj != null) {
//			IMetaAttribute currMetadata = object.getMetadata();
//			IMetaAttribute prevMetadata = this.prevObj.getMetadata();
//			
//			if (currMetadata instanceof ILatency && prevMetadata instanceof ILatency) {
//				ILatency currLatency = (ILatency) currMetadata;
//				ILatency prevLatency = (ILatency) prevMetadata;
//				long diff = currLatency.getLatencyStart() - prevLatency.getLatencyStart();
//				if (diff > this.updateRateThreshold) {
//					this.numberOfViolations++;
//				}
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
		// nothing to do
	}

	@Override
	public AbstractSink<T> clone() {
		return new UpdateRateSinkNumberConformance<T>(this);
	}

}
