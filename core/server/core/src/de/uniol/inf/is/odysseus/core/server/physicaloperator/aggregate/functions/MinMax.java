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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class MinMax<R extends Comparable<R>, W> extends AbstractAggregateFunction<R, W>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7793230815454083728L;
	protected final boolean isMax;
	protected final String datatype;
	
	protected MinMax(boolean isMax, boolean partialAggregateInput, String datatype) {
		super (isMax?"MAX":"MIN",partialAggregateInput);
		this.isMax = isMax;
		this.datatype = datatype;
	}
		
	
	@Override
	public IPartialAggregate<R> init(R in) {
		return new ElementPartialAggregate<R>(in, datatype);
	}

	@Override
	public IPartialAggregate<R> init(IPartialAggregate<R> in) {
		return new ElementPartialAggregate<R>(in);
	}
	
	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge, boolean createNew) {
		ElementPartialAggregate<R> pa = null;
		if (createNew){
			pa = new ElementPartialAggregate<R>(p);
		}else{
			pa = (ElementPartialAggregate<R>) p;	
		}		
		if (isMax){
			if (pa.getElem().compareTo(toMerge) < 0){
				pa.setElem(toMerge);
			}
		}else{
			if (pa.getElem().compareTo(toMerge) > 0){
				pa.setElem(toMerge);
			}			
		}
		return pa;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction#merge(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate, de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate, boolean)
	 */
	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p,
			IPartialAggregate<R> toMerge, boolean createNew) {
		return merge(p, ((ElementPartialAggregate<R>)toMerge).getElem(), createNew);
	}

	@Override
	public W evaluate(IPartialAggregate<R> p) {
		@SuppressWarnings("unchecked")
		ElementPartialAggregate<W> pa = (ElementPartialAggregate<W>) p;
		return pa.getElem();
	}

}
