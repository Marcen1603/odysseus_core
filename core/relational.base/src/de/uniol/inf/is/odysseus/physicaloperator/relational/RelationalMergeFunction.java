/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * Implements a merge function for RelationalTuples. A full outer join is
 * implemented (left or right can be null), so make sure you pass the right
 * parameters to merge, if you don't want outer joins to happen.
 * 
 * @author Jonas Jacobi
 */
public class RelationalMergeFunction<M extends IMetaAttribute> extends AbstractRelationalMergeFunction<Tuple<M>, M> implements
		IDataMergeFunction<Tuple<M>, M> {


	public RelationalMergeFunction(int resultSchemaSize) {
		super(resultSchemaSize);
	}
	
	protected RelationalMergeFunction(RelationalMergeFunction<M> original){
		super(original.schemaSize);
	}
	

	@Override
	public Tuple<M> merge(Tuple<M> left, Tuple<M> right,
			IMetadataMergeFunction<M> metamerge, Order order) {
		return (Tuple<M>) left.merge(left, right, metamerge, order);
	}
	
	@Override
	public void init(){
	}
	
	@Override
	public RelationalMergeFunction<M> clone(){
		return new RelationalMergeFunction<M>(this);
	}
}
