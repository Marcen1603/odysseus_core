/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class MinMax<R extends Comparable<R>, W> extends AbstractAggregateFunction<R, W>{
	
	boolean isMax = true;
	
	protected MinMax(boolean isMax) {
		super (isMax?"MAX":"MIN");
		this.isMax = isMax;
	}
		
	
	@Override
	public IPartialAggregate<R> init(R in) {
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

	@Override
	public W evaluate(IPartialAggregate<R> p) {
		@SuppressWarnings("unchecked")
		ElementPartialAggregate<W> pa = (ElementPartialAggregate<W>) p;
		return pa.getElem();
	}

}
