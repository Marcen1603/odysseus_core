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
package de.uniol.inf.is.odysseus.wrapper.fnirs.physicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.wrapper.fnirs.aggregate.functions.Test;
import de.uniol.inf.is.odysseus.wrapper.fnirs.aggregate.functions.TestPartialAggregate;
import de.uniol.inf.is.odysseus.wrapper.fnirs.datahandler.TestPartialAggregateDataHandler;

@SuppressWarnings({ "rawtypes" })
public class RelationalTest extends Test<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -788185013111220731L;
	private final int pos;

	private RelationalTest(int pos, boolean partialAggregateInput) {
		super(partialAggregateInput);
		this.pos = pos;
	}

	public static RelationalTest getInstance(int pos,
			boolean partialAggregateInput) {
		return new RelationalTest(pos, partialAggregateInput);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		if (isPartialAggregateInput()) {
			return super.init((IPartialAggregate) in.getAttribute(pos));
		} else {
			return super.init(in);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized IPartialAggregate<Tuple<?>> merge(
			IPartialAggregate<Tuple<?>> p, Tuple<?> toMerge, boolean createNew) {
		if (isPartialAggregateInput()) {
			return super.init((IPartialAggregate) toMerge.getAttribute(pos));
		} else {
			return super.merge(p, toMerge, createNew);
		}
	}

	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		TestPartialAggregate<Tuple<?>> pa = (TestPartialAggregate<Tuple<?>>) p;
		Tuple<?> r = new Tuple(1, false);
		r.setAttribute(0, new Integer(pa.getCount()));
		return r;
	}

	@Override
	public SDFDatatype getPartialAggregateType() {
		return TestPartialAggregateDataHandler.TEST_PARTIAL_AGGREGATE;
	}
}
