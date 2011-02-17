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

abstract public class Count<T> extends AbstractAggregateFunction<T> {
	
	protected Count() {
		super("COUNT");
	}
	
	@Override
	public IPartialAggregate<T> init(T in) {
		IPartialAggregate<T> pa = new CountPartialAggregate<T>(1); 
		return pa;
	}

	@Override
	public synchronized IPartialAggregate<T> merge(IPartialAggregate<T> p, T toMerge, boolean createNew) {
		CountPartialAggregate<T> pa = null;
		if (createNew){
			pa = new CountPartialAggregate<T>(((CountPartialAggregate<T>) p).getCount());
		}else{
			pa = (CountPartialAggregate<T>) p;
		}		
		pa.add();
		return pa;
	}
	
	

}
