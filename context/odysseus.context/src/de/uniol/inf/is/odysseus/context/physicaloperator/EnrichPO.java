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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ISweepArea.Order;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * 
 * @author Dennis Geesen Created at: 27.04.2012
 */
public class EnrichPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private IContextStore<Tuple<M>> store;
	protected IDataMergeFunction<Tuple<M>> dataMerge;
	protected IMetadataMergeFunction<M> metadataMerge;
	private List<String> attribtues;
	private int[] restrictList;

	public EnrichPO(IContextStore<Tuple<M>> store, IDataMergeFunction<Tuple<M>> dataMerge, CombinedMergeFunction<M> metadataMerge, List<String> attributes) {
		super();
		this.store = store;
		this.metadataMerge = metadataMerge;
		this.dataMerge = dataMerge;
		this.attribtues = attributes;
	}

	public EnrichPO(EnrichPO<M> enrichPO) {
		super();
		this.store = enrichPO.store;
		this.metadataMerge = enrichPO.metadataMerge.clone();
		this.dataMerge = enrichPO.dataMerge.clone();
		this.attribtues = new ArrayList<String>(enrichPO.attribtues);
	}

	@Override
	protected void process_next(Tuple<M> object, int port) {
		ITimeInterval ti = object.getMetadata();
		List<Tuple<M>> values = this.store.getValues(ti);
		for (Tuple<M> value : values) {
			System.out.println("      with: "+value);
			Tuple<M> res = value.restrict(this.restrictList, true);
			Tuple<M> newElement = merge(object, res, Order.LeftRight);		
			transfer(newElement);
		}
	}

	private void calcRestrictList() {		
		SDFSchema storeSchema = this.store.getSchema();
		int[] ret = new int[this.attribtues.size()];
		DirectAttributeResolver dar = new DirectAttributeResolver(storeSchema);
		for(String attName : this.attribtues){
			int i=0;
			SDFAttribute a = dar.getAttribute(attName);
			int j=0;
			for(SDFAttribute b : storeSchema){
				if(b.equals(a)){
					ret[i] = j;
				}
				j++;
			}
			i++;
		}			
		this.restrictList= ret;
	}

	protected Tuple<M> merge(Tuple<M> left, Tuple<M> right, Order order) {
		// if (logger.isTraceEnabled()) {
		// logger.trace("JoinTIPO (" + hashCode() + ") start merging: " + left
		// + " AND " + right);
		// }
		Tuple<M> mergedData;
		M mergedMetadata;
		if (order == Order.LeftRight) {
			mergedData = dataMerge.merge(left, right);
			 mergedMetadata = metadataMerge.mergeMetadata(left.getMetadata(),
			 right.getMetadata());
			//mergedMetadata = left.getMetadata();
		} else {
			mergedData = dataMerge.merge(right, left);
			mergedMetadata = metadataMerge.mergeMetadata(right.getMetadata(), left.getMetadata());
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
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.calcRestrictList();
		this.metadataMerge.init();
		this.dataMerge.init();
	}

	@Override
	protected void process_done() {
		super.process_done();
	}

	@Override
	public EnrichPO<M> clone() {
		return new EnrichPO<M>(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
