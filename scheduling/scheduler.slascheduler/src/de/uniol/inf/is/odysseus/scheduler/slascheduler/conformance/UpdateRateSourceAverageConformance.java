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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AvgSumPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

/**
 * sla conformance for metric update rate and scope average
 * 
 * @author Lena Eylert
 * 
 * @param <T>
 */
public class UpdateRateSourceAverageConformance<R extends IStreamObject<?>, W extends IStreamObject<?>> extends AbstractSLAPipeConformance<R, W> {
	/**
	 * partial aggregate for calculating the average
	 */
	private AvgSumPartialAggregate<R> aggregate;
	
	private R prevObj;
	
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
	public UpdateRateSourceAverageConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPhysicalQuery query) {
		super(dist, sla, query);
		this.aggregate = new AvgSumPartialAggregate<R>(0.0, 0);
	}
	
	/**
	 * copy constructor, required for clone method
	 * @param conformance object to copy
	 */
	private UpdateRateSourceAverageConformance(UpdateRateSourceAverageConformance<R, W> conformance) {
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
		long diff = -1;
		if (this.prevObj != null) {
			IMetaAttribute currMetadata = object.getMetadata();
			IMetaAttribute prevMetadata = this.prevObj.getMetadata();
			
			if (currMetadata instanceof ILatency && prevMetadata instanceof ILatency) {
				ILatency currLatency = (ILatency) currMetadata;
				ILatency prevLatency = (ILatency) prevMetadata;
				diff = currLatency.getLatencyStart() - prevLatency.getLatencyStart();
				this.aggregate.addAggValue(nanoToMilli(diff));
			} else {
				throw new RuntimeException("Latency missing");
			}
		}
		this.prevObj = object;
		
		int attributeCount = ((Tuple<?>)object).getAttributes().length;
		((Tuple<?>)object).append(this.getOwner().get(0).getID(), false);
		((Tuple<?>)object).append(nanoToMilli(diff), false);
		((Tuple<?>)object).append(getConformance() >= this.getSLA().getMetric().getValue(), false);
		int[] attrList = new int[3];
		int index = 0;
		for (int i = attributeCount; i < attributeCount+3; i++) {
			attrList[index] = i;
			index++;
		}
		Tuple<?> tuple = ((Tuple<?>)object).restrict(attrList, true);
		
		transfer((W) tuple);
		super.process_next((R) tuple, port);
//		super.process_next(object, port);
	}

	@Override
	public double predictConformance() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * copy object
	 */
	@Override
	public AbstractPipe<R, W> clone() {
		return new UpdateRateSourceAverageConformance<R, W>(this);
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
