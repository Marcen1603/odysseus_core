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
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAHelper;

/**
 * sla conformance for metric update rate and scope number
 * 
 * @author Lena Eylert
 * 
 * @param <T>
 */
public class UpdateRateSourceNumberConformance<R extends IStreamObject<?>, W extends IStreamObject<?>> extends AbstractSLAPipeConformance<R, W> {
	/**
	 * counts the number of violating the specified update rate
	 */
	private int numberOfViolations;
	/**
	 * threshold of the update rate that should not be exceeded
	 */
	private double updateRateThreshold;
	
	private long prevTime = -1;
	private long currentWindowStart = 0;
	
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
	public UpdateRateSourceNumberConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPhysicalQuery query, double threshold) {
		super(dist, sla, query);
		this.numberOfViolations = 0;
		this.updateRateThreshold = threshold;
	}
	
	/**
	 * copy constructor, required for clone method
	 * @param conformance object to copy
	 */
	private UpdateRateSourceNumberConformance(UpdateRateSourceNumberConformance<R, W> conformance) {
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
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(R object, int port) {
		long diff = 0;
		if (this.prevTime != -1) {
			long currTime = System.nanoTime();
			diff = currTime - prevTime;
			if (diff > this.updateRateThreshold) {
					this.numberOfViolations++;
			}
		}
		this.prevTime = System.nanoTime();
		
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
		((Tuple<?>)object).append(nanoToMilli(diff), false);
		int[] attrList = new int[2];
		int index = 0;
		for (int i = oldAttributeCount; i < oldAttributeCount+2; i++) {
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
		// nothing to do
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
}
