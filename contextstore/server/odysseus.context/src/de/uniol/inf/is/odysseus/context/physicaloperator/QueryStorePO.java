/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.RelatedTuplesPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;

/**
 * 
 * @author Mazen Salous
 */
public class QueryStorePO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

    private IContextStore<Tuple<M>> store;
    protected IDataMergeFunction<Tuple<M>, M> dataMerge;
    protected IMetadataMergeFunction<M> metadataMerge;
    private List<Integer> keyIndices;
    private boolean outer;
    private IPredicate<? super Tuple<M>> queryPredicate;
    private List<String> attribtues;
    private int[] restrictList;

    public QueryStorePO(IContextStore<Tuple<M>> store, boolean outer, IDataMergeFunction<Tuple<M>, M> dataMerge, IMetadataMergeFunction<M> metadataMerge, List<Integer> keyIndices, List<String> attributes) {
        super();
        this.store = store;
        this.metadataMerge = metadataMerge;
        this.dataMerge = dataMerge;
        this.keyIndices = keyIndices;
        this.attribtues = attributes;
        this.outer = outer;
        this.queryPredicate = new RelatedTuplesPredicate<>(this.keyIndices);
        try {
			this.store.setQueryPredicate(this.queryPredicate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public QueryStorePO(QueryStorePO<M> another) {
        super();
        this.store = another.store;
        this.metadataMerge = another.metadataMerge.clone();
        this.dataMerge = another.dataMerge.clone();
        this.keyIndices = new ArrayList<Integer>(another.keyIndices);
        this.attribtues = new ArrayList<String>(another.attribtues);
        this.outer = another.outer;
        this.queryPredicate = new RelatedTuplesPredicate<>(this.keyIndices);
        try {
			this.store.setQueryPredicate(this.queryPredicate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    protected void process_next(Tuple<M> object, int port) {
		try {
			Iterator<Tuple<M>> iterator = this.store.query(object, Order.LeftRight);
	    	if (!iterator.hasNext() && this.outer) {
	    		Object[] nullArray = new Object[this.restrictList.length];
	            Arrays.fill(nullArray, null);
	            Tuple<M> newElement = object.appendList(Arrays.asList(nullArray), true);
	            transfer(newElement);
	    	}
	    	while (iterator.hasNext()) {
        	Tuple<M> value = iterator.next();
        	Tuple<M> res = value.restrict(this.restrictList, true);
            Tuple<M> newElement = dataMerge.merge(object, res, metadataMerge, Order.LeftRight);
            transfer(newElement);
            }
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void processPunctuation(IPunctuation punctuation, int port) {
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
    public OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

    /**
     * @param metadataMerge
     */
    public void setMetadataMerge(IMetadataMergeFunction<M> metadataMerge) {
        this.metadataMerge = metadataMerge;
    }

	/**
	 * @return the queryPredicate
	 */
	public IPredicate<?> getQueryPredicate() {
		return queryPredicate;
	}

	/**
	 * @param queryPredicate the queryPredicate to set
	 */
	public void setQueryPredicate(IPredicate<? super Tuple<M>> queryPredicate) {
		this.queryPredicate = queryPredicate;
	}
	
	private void calcRestrictList() {
        SDFSchema storeSchema = this.store.getSchema();
        int[] ret = new int[this.attribtues.size()];
        DirectAttributeResolver dar = new DirectAttributeResolver(storeSchema);
        int i = 0;
        for (String attName : this.attribtues) {
            SDFAttribute a = dar.getAttribute(attName);
            int j = 0;
            for (SDFAttribute b : storeSchema) {
                if (b.equals(a)) {
                    ret[i] = j;
                }
                j++;
            }
            i++;
        }
        this.restrictList = ret;
    }

}
