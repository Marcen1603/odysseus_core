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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.Count;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.CountPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

@SuppressWarnings({"unchecked","rawtypes"})
public class RelationalCount extends Count<RelationalTuple<?>, RelationalTuple<?>> {

	private static RelationalCount instance;

	private RelationalCount(){
		super();
	}
	
	public static RelationalCount getInstance(){
		if (instance == null){
			instance = new RelationalCount();
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public RelationalTuple<?> evaluate(IPartialAggregate<RelationalTuple<?>> p) {
		CountPartialAggregate<RelationalTuple<?>> pa = (CountPartialAggregate<RelationalTuple<?>>) p;
		RelationalTuple<?> r = new RelationalTuple(1);
		r.setAttribute(0, new Integer(pa.getCount()));
		return r;
	}


}
