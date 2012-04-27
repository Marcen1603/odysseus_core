/** Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.context.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ISweepArea.Order;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * 
 * @author Dennis Geesen Created at: 27.04.2012
 */
public class EnrichPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, T> {

		
	private IContextStore<T> store;
	protected IDataMergeFunction<T> dataMerge;
	protected IMetadataMergeFunction<M> metadataMerge;
	protected ITransferArea<T, T> transferFunction;


	public EnrichPO(IContextStore<T> store, IDataMergeFunction<T> dataMerge, CombinedMergeFunction<M> metadataMerge, ITransferArea<T, T> transferFunction){
		super();
		this.store = store;
		this.metadataMerge = metadataMerge;
		this.dataMerge = dataMerge;
		this.transferFunction = transferFunction;
	}
	

	public EnrichPO(EnrichPO<T, M> enrichPO) {
		super();
		this.store = enrichPO.store;
		this.metadataMerge = enrichPO.metadataMerge.clone();
		this.dataMerge = enrichPO.dataMerge.clone();
		this.transferFunction = enrichPO.transferFunction.clone();
	}



	@Override
	protected void process_next(T object, int port) {
		ITimeInterval ti = object.getMetadata();
		List<T> values = this.store.getValues(ti);
		for(T value : values){
			T newElement = merge(object, value, Order.LeftRight);
			transferFunction.transfer(newElement);
		}
	}
	
	
	
	protected T merge(T left, T right, Order order) {
		// if (logger.isTraceEnabled()) {
		// logger.trace("JoinTIPO (" + hashCode() + ") start merging: " + left
		// + " AND " + right);
		// }
		T mergedData;
		M mergedMetadata;
		if (order == Order.LeftRight) {
			mergedData = dataMerge.merge(left, right);
			mergedMetadata = metadataMerge.mergeMetadata(left.getMetadata(),
					right.getMetadata());
		} else {
			mergedData = dataMerge.merge(right, left);
			mergedMetadata = metadataMerge.mergeMetadata(right.getMetadata(),
					left.getMetadata());
		}
		mergedData.setMetadata(mergedMetadata);
		return mergedData;
	}
	

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// if getValues is invoked, the start timestamp is used for purging of
		// the store
		// so, we "simulate" this for a punctuation
		TimeInterval ti = new TimeInterval(timestamp);
		this.store.getValues(ti);
	}

	@Override
	public EnrichPO<T, M> clone() {
		return new EnrichPO<T, M>(this);
	}
	
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
