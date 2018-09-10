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
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AbstractListAggregation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ListPartialAggregate;

@SuppressWarnings({"rawtypes"})
public class RelationalNest extends AbstractListAggregation<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	private static final long serialVersionUID = -9172869315418294224L;
	private int[] restrictList;

	public RelationalNest(int[] restrictList, boolean partialAggregateInput){
		super("NEST", partialAggregateInput);
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
		List<Tuple<?>> elems = ((ListPartialAggregate<Tuple<? extends IMetaAttribute>>)p).getElems();
		Tuple ret = new Tuple<IMetaAttribute>(1, false);
		if (restrictList.length == 1){
			// unpack tuples
			List<Object> elemsUnpacked = new ArrayList<Object>();
			for (Iterator<Tuple<? extends IMetaAttribute>> iterator = elems.iterator(); iterator.hasNext();) {
				elemsUnpacked.add(iterator.next().getAttribute(0));
			}
			ret.setAttribute(0,elemsUnpacked);
		}else{
			ret.setAttribute(0, elems);
		}
		return ret;
	}

	@Override
	public IPartialAggregate<Tuple<? extends IMetaAttribute>> merge(IPartialAggregate<Tuple<? extends IMetaAttribute>> p, Tuple<? extends IMetaAttribute> toMerge,
			boolean createNew) {
		final ListPartialAggregate<Tuple<? extends IMetaAttribute>> ret;
		if (createNew){
			ret = new ListPartialAggregate<Tuple<? extends IMetaAttribute>>((ListPartialAggregate<Tuple<? extends IMetaAttribute>>)p);
		}else{
			ret = (ListPartialAggregate<Tuple<? extends IMetaAttribute>>)p;
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
			// FIXME: Metadata schema of subtype is missing.
			return SDFDatatype.createTypeWithSubSchema(SDFDatatype.LIST, SDFDatatype.TUPLE, SDFSchemaFactory.createNewSchema("", Tuple.class, inputTypes));
		}
	}
	
}
