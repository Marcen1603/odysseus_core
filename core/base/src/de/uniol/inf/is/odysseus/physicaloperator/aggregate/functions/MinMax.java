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

abstract public class MinMax<T extends Comparable<T>> extends AbstractAggregateFunction<T>{
	
	boolean isMax = true;
	
	protected MinMax(boolean isMax) {
		super (isMax?"MAX":"MIN");
		this.isMax = isMax;
	}
		
	
	@Override
	public IPartialAggregate<T> init(T in) {
		return new ElementPartialAggregate<T>(in);
	}

	@Override
	public IPartialAggregate<T> merge(IPartialAggregate<T> p, T toMerge, boolean createNew) {
		ElementPartialAggregate<T> pa = null;
		if (createNew){
			pa = new ElementPartialAggregate<T>(p);
		}else{
			pa = (ElementPartialAggregate<T>) p;	
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
	public T evaluate(IPartialAggregate<T> p) {
		ElementPartialAggregate<T> pa = (ElementPartialAggregate<T>) p;
		return pa.getElem();
	}

}
