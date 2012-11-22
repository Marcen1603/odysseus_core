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
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;

/**
 * 
 * @author Dennis Geesen Created at: 27.04.2012
 */
public class EnrichPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> implements IHasMetadataMergeFunction<M> {

	private IContextStore<Tuple<M>> store;
	protected IDataMergeFunction<Tuple<M>, M> dataMerge;
	protected IMetadataMergeFunction<M> metadataMerge;
	private List<String> attribtues;
	private int[] restrictList;
	private boolean outer;

	public EnrichPO(IContextStore<Tuple<M>> store, boolean outer, IDataMergeFunction<Tuple<M>, M> dataMerge, CombinedMergeFunction<M> metadataMerge, List<String> attributes) {
		super();
		this.store = store;
		this.metadataMerge = metadataMerge;
		this.dataMerge = dataMerge;
		this.attribtues = attributes;
		this.outer = outer;
	}

	public EnrichPO(EnrichPO<M> enrichPO) {
		super();
		this.store = enrichPO.store;
		this.metadataMerge = enrichPO.metadataMerge.clone();
		this.dataMerge = enrichPO.dataMerge.clone();
		this.attribtues = new ArrayList<String>(enrichPO.attribtues);
		this.outer = enrichPO.outer;
	}

	@Override
	protected void process_next(Tuple<M> object, int port) {
		ITimeInterval ti = object.getMetadata();
		List<Tuple<M>> values = this.store.getValues(ti);
		for (Tuple<M> value : values) {			
			Tuple<M> res = value.restrict(this.restrictList, true);
			Tuple<M> newElement = dataMerge.merge(object, res, metadataMerge, Order.LeftRight);		
			transfer(newElement);
		}
		if(values.size()==0 && outer){
			Tuple<M> nullTuple = new Tuple<M>(this.restrictList.length, false);
			nullTuple.setMetadata((M) object.getMetadata());
			Tuple<M> newElement = dataMerge.merge(object, nullTuple, metadataMerge, Order.LeftRight);		
			transfer(newElement);
		}
	}

	private void calcRestrictList() {		
		SDFSchema storeSchema = this.store.getSchema();
		int[] ret = new int[this.attribtues.size()];
		DirectAttributeResolver dar = new DirectAttributeResolver(storeSchema);
		int i=0;
		for(String attName : this.attribtues){
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

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// if getValues is invoked, the start timestamp is used for purging of
		// the store
		// so, we "simulate" this for a punctuation
		TimeInterval ti = new TimeInterval(punctuation.getTime());
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
	
	@Override
	public IMetadataMergeFunction<M> getMetadataMerge() {
		return metadataMerge;
	}

}
