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
package de.uniol.inf.is.odysseus.wrapper.fnirs.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class Test<R, W> extends AbstractAggregateFunction<R, W> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8658668731513975755L;

	protected Test(boolean partialAggregateInput) {
		super("TEST", partialAggregateInput);
	}

	@Override
	public IPartialAggregate<R> init(R in) {
		IPartialAggregate<R> pa = new TestPartialAggregate<R>(1);
		return pa;
	}

	@Override
	public IPartialAggregate<R> init(IPartialAggregate<R> in) {
		IPartialAggregate<R> pa = new TestPartialAggregate<R>(
				(TestPartialAggregate<R>) in);
		return pa;
	}

	@Override
	public synchronized IPartialAggregate<R> merge(IPartialAggregate<R> p,
			R toMerge, boolean createNew) {
		TestPartialAggregate<R> pa = null;
		if (createNew) {
			pa = new TestPartialAggregate<R>(
					((TestPartialAggregate<R>) p).getCount());
		} else {
			pa = (TestPartialAggregate<R>) p;
		}
		pa.add();
		return pa;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
	 * .AbstractAggregateFunction#merge(de.uniol.inf.is.odysseus.core.server.
	 * physicaloperator.aggregate.basefunctions.IPartialAggregate,
	 * de.uniol.inf.is
	 * .odysseus.core.server.physicaloperator.aggregate.basefunctions
	 * .IPartialAggregate, boolean)
	 */
	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p,
			IPartialAggregate<R> toMerge, boolean createNew) {
		TestPartialAggregate<R> pa = null;
		if (createNew) {
			pa = new TestPartialAggregate<R>(
					((TestPartialAggregate<R>) p).getCount());
		} else {
			pa = (TestPartialAggregate<R>) p;
		}
		pa.add(((TestPartialAggregate<R>) toMerge).getCount());
		return pa;
	}

}
