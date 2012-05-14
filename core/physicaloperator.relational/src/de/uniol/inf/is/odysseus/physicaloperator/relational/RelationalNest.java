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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AbstractListAggregation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ListPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

@SuppressWarnings({"unchecked","rawtypes"})
public class RelationalNest extends AbstractListAggregation<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9172869315418294224L;
	private int[] restrictList;

	public RelationalNest(int[] restrictList){
		super("NEST");
		this.restrictList = restrictList;
	}
	
	@Override
	public Tuple evaluate(IPartialAggregate<Tuple<? extends IMetaAttribute>> p) {
		
		
		
		
		
		List<Tuple<?>> elems = ((ListPartialAggregate<Tuple<? extends IMetaAttribute>>)p).getElems();
		Tuple ret = new Tuple<IMetaAttribute>(0);
		
		
		
		ret.setAttribute(0, elems);
		return ret;
	}

	@Override
	public IPartialAggregate<Tuple<? extends IMetaAttribute>> merge(IPartialAggregate<Tuple<? extends IMetaAttribute>> p, Tuple<? extends IMetaAttribute> toMerge,
			boolean createNew) {
		return ((ListPartialAggregate)p).addElem(toMerge.restrict(restrictList, true));
	}
	
}
