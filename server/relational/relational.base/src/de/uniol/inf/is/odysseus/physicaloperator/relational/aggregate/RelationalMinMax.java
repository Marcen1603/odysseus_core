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

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;

public class RelationalMinMax extends AbstractAggregateFunction<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 571119114462961967L;

	final private int attrList[] = new int[1];
	final private boolean isMax;
	final private String datatype;

	// static Map<Boolean, Map<Integer, RelationalMinMax>> instances = new
	// HashMap<Boolean, Map<Integer, RelationalMinMax>>();

	static public RelationalMinMax getInstance(int pos, boolean isMax, boolean partialAggregateInput, String datatype) {
		// Map<Integer, RelationalMinMax> in = instances.get(isMax);
		// RelationalMinMax ret;
		// if (in == null) {
		// in = new HashMap<Integer, RelationalMinMax>();
		// instances.put(isMax, in);
		// ret = new RelationalMinMax(pos, isMax, partialAggregateInput);
		// in.put(pos, ret);
		// } else {
		// ret = in.get(pos);
		// if (ret == null) {
		// ret = new RelationalMinMax(pos, isMax, partialAggregateInput);
		// in.put(pos, ret);
		// }
		// }
		// return ret;
		return new RelationalMinMax(pos, isMax, partialAggregateInput, datatype);
	}

	private RelationalMinMax(int pos, boolean isMax, boolean partialAggregateInput, String datatype) {
		super(isMax ? "MAX" : "MIN", partialAggregateInput);
		this.attrList[0] = pos;
		this.isMax = isMax;
		this.datatype = datatype;
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		if (isPartialAggregateInput()) {
			return in.getAttribute(attrList[0]);
		} else {
			return new RelationalElementPartialAggregate(in.restrict(attrList, true), datatype);
		}
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, Tuple<?> merge, boolean createNew) {
		Tuple<?> toMerge = merge.restrict(attrList, true);
		ElementPartialAggregate<Tuple<?>> pa = null;
		if (createNew) {
			pa = new RelationalElementPartialAggregate(p);
		} else {
			pa = (RelationalElementPartialAggregate) p;
		}
		if (isMax) {
			if (pa.getElem().compareTo(toMerge) < 0) {
				pa.setElem(toMerge);
			}
		} else {
			if (pa.getElem().compareTo(toMerge) > 0) {
				pa.setElem(toMerge);
			}
		}
		return pa;
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, IPartialAggregate<Tuple<?>> toMerge,
			boolean createNew) {
		return merge(p, ((RelationalElementPartialAggregate) toMerge).getElem(), createNew);
	}

	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		return ((RelationalElementPartialAggregate) p).getElem();
	}

	@Override
	public SDFDatatype getReturnType(List<SDFAttribute> inputTypes) {
		if (inputTypes != null && inputTypes.size() == 1) {
			return inputTypes.get(0).getDatatype();
		} else {
			return null;
		}
	}

	@Override
	public SDFDatatype getPartialAggregateType() {
		return SDFDatatype.RELATIONAL_ELEMENT_PARTIAL_AGGREGATE;
	}

}
