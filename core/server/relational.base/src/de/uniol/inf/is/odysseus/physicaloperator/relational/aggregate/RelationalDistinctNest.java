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
package de.uniol.inf.is.odysseus.physicaloperator.relational.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AbstractSetAggregation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.SetPartialAggregate;

@SuppressWarnings({"rawtypes"})
public class RelationalDistinctNest extends AbstractSetAggregation<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	private static final long serialVersionUID = -9172869315418294224L;
	private int[] restrictList;

	public RelationalDistinctNest(int[] restrictList, boolean partialAggregateInput){
		super("DISTINCTNEST", partialAggregateInput);
		this.restrictList = restrictList;
	}
	
	@Override
	public IPartialAggregate<Tuple<? extends IMetaAttribute>> init(
			Tuple<? extends IMetaAttribute> in) {
		Tuple<? extends IMetaAttribute> t = in.restrict(restrictList, true);
		t.setMetadata(null);
		return super.init(t);
	}
	
	@Override
	public Tuple evaluate(IPartialAggregate<Tuple<? extends IMetaAttribute>> p) {		
		Set<Tuple<?>> elems = ((SetPartialAggregate<Tuple<? extends IMetaAttribute>>)p).getElems();
		Tuple ret = new Tuple<IMetaAttribute>(1, false);
		ret.setAttribute(0, new ArrayList<>(elems));
		return ret;
	}

	@Override
	public IPartialAggregate<Tuple<? extends IMetaAttribute>> merge(IPartialAggregate<Tuple<? extends IMetaAttribute>> p, Tuple<? extends IMetaAttribute> toMerge,
			boolean createNew) {
		final SetPartialAggregate<Tuple<? extends IMetaAttribute>> ret;
		if (createNew){
			ret = new SetPartialAggregate<Tuple<? extends IMetaAttribute>>((SetPartialAggregate<Tuple<? extends IMetaAttribute>>)p);
		}else{
			ret = (SetPartialAggregate<Tuple<? extends IMetaAttribute>>)p;
		}
		Tuple<? extends IMetaAttribute> t = toMerge.restrict(restrictList, true);
		t.setMetadata(null);
		return ret.addElem(t);
	}
	
	@Override
	public SDFDatatype getReturnType(List<SDFAttribute> inputTypes) {
		if (inputTypes.size() == 1){
			SDFDatatype t = inputTypes.get(0).getDatatype();
			return new SDFDatatype("LIST_"+t.getURI(), KindOfDatatype.LIST, t);
		}else{
			return new SDFDatatype("LIST_TUPLE", KindOfDatatype.LIST, inputTypes);
		}
	}
	
}
