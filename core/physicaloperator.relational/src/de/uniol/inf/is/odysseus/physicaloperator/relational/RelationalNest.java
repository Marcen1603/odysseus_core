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

import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ListPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.Nest;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

@SuppressWarnings({"unchecked","rawtypes"})
public class RelationalNest extends Nest<RelationalTuple<? extends IMetaAttribute>, RelationalTuple<? extends IMetaAttribute>> {

	private int[] restrictList;

	public RelationalNest(int[] restrictList){
		this.restrictList = restrictList;
	}
	
	@Override
	public RelationalTuple evaluate(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p) {
		List<RelationalTuple<?>> elems = ((ListPartialAggregate<RelationalTuple<? extends IMetaAttribute>>)p).getElems();
		RelationalTuple ret = new RelationalTuple<IMetaAttribute>(0);
		ret.setAttribute(0, elems);
		return ret;
	}

	@Override
	public IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> merge(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p, RelationalTuple<? extends IMetaAttribute> toMerge,
			boolean createNew) {
		return ((ListPartialAggregate)p).addElem(toMerge.restrict(restrictList, true));
	}
	
}
