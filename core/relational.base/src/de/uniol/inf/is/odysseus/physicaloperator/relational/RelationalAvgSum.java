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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AvgSum;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AvgSumPartialAggregate;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RelationalAvgSum extends AvgSum<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7768784425424062403L;

	private int pos;

	static public RelationalAvgSum getInstance(int pos, boolean isAvg,
			boolean partialAggregateInput) {
//		Map<Integer, RelationalAvgSum> in = instances.get(isAvg);
//		RelationalAvgSum ret;
//		if (in == null) {
//			in = new HashMap<Integer, RelationalAvgSum>();
//			instances.put(isAvg, in);
//			ret = new RelationalAvgSum(pos, isAvg, partialAggregateInput);
//			in.put(pos, ret);
//		} else {
//			ret = in.get(pos);
//			if (ret == null) {
//				ret = new RelationalAvgSum(pos, isAvg, partialAggregateInput);
//				in.put(pos, ret);
//			}
//		}
		return new RelationalAvgSum(pos, isAvg, partialAggregateInput);
	}

	private RelationalAvgSum(int pos, boolean isAvg,
			boolean partialAggregateInput) {
		super(isAvg, partialAggregateInput);
		this.pos = pos;
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple in) {
		if (isPartialAggregateInput()) {
			return init((AvgSumPartialAggregate<Tuple<?>>) in.getAttribute(pos));
		} else {
			return new AvgSumPartialAggregate<Tuple<?>>(
					((Number) in.getAttribute(pos)).doubleValue(), 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
	 * .AbstractAggregateFunction#init(de.uniol.inf.is.odysseus.core.server.
	 * physicaloperator.aggregate.basefunctions.IPartialAggregate)
	 */
	@Override
	public IPartialAggregate<Tuple<?>> init(IPartialAggregate<Tuple<?>> in) {
		return new AvgSumPartialAggregate<Tuple<?>>(
				(AvgSumPartialAggregate<Tuple<?>>) in);
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate p,
			Tuple toMerge, boolean createNew) {
		AvgSumPartialAggregate<Tuple> pa = null;
		if (createNew) {
			AvgSumPartialAggregate<Tuple> h = (AvgSumPartialAggregate<Tuple>) p;
			pa = new AvgSumPartialAggregate<Tuple>(h.getAggValue(),
					h.getCount());

		} else {
			pa = (AvgSumPartialAggregate<Tuple>) p;
		}
		return merge(pa, toMerge);
	}

	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate p, Tuple toMerge) {
		AvgSumPartialAggregate pa = (AvgSumPartialAggregate) p;
		if (isPartialAggregateInput()) {
			return merge(p, (IPartialAggregate) toMerge.getAttribute(pos),
					false);
		} else {
			Double newAggValue = pa.getAggValue().doubleValue()
					+ ((Number) toMerge.getAttribute(pos)).doubleValue();
			pa.setAggValue(newAggValue, pa.getCount() + 1);
			return pa;
		}
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p,
			IPartialAggregate<Tuple<?>> toMerge, boolean createNew) {
		AvgSumPartialAggregate<Tuple<?>> pa = null;
		if (createNew) {
			AvgSumPartialAggregate<Tuple<?>> h = (AvgSumPartialAggregate<Tuple<?>>) p;
			pa = new AvgSumPartialAggregate<Tuple<?>>(h.getAggValue(),
					h.getCount());
		} else {
			pa = (AvgSumPartialAggregate<Tuple<?>>) p;
		}
		return merge(pa, toMerge);
	}

	/**
	 * @param pa
	 * @param toMerge
	 * @return
	 */
	public IPartialAggregate<Tuple<?>> merge(
			AvgSumPartialAggregate<Tuple<?>> pa,
			IPartialAggregate<Tuple<?>> toMerge) {

		AvgSumPartialAggregate paToMerge = (AvgSumPartialAggregate) toMerge;
		Double newAggValue = pa.getAggValue().doubleValue()
				+ paToMerge.getAggValue().doubleValue();
		pa.setAggValue(newAggValue, pa.getCount() + paToMerge.getCount());
		return pa;
	}

	@Override
	public Tuple evaluate(IPartialAggregate p) {
		AvgSumPartialAggregate pa = (AvgSumPartialAggregate) p;
		Tuple r = new Tuple(1, false);
		if (isAvg()) {
			r.setAttribute(0,
					new Double(pa.getAggValue().doubleValue() / pa.getCount()));
		} else {
			r.setAttribute(0, new Double(pa.getAggValue().doubleValue()));
		}
		return r;
	}

}
