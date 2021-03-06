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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class AbstractListAggregation<R, W> extends
		AbstractAggregateFunction<R, W> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8085619521442098537L;

	protected AbstractListAggregation(String name, boolean partialAggregateInput) {
		super(name, partialAggregateInput);
	}

	@Override
	public IPartialAggregate<R> init(R in) {
		return new ListPartialAggregate<R>(in);
	}

	@Override
	public IPartialAggregate<R> init(IPartialAggregate<R> in) {
		return new ListPartialAggregate<R>((ListPartialAggregate<R>) in);
	}

	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge,
			boolean createNew) {
		ListPartialAggregate<R> list = (ListPartialAggregate<R>) p;
		if (createNew) {
			list = new ListPartialAggregate<R>((ListPartialAggregate<R>) p);
		}
		list.addElem(toMerge);
		return list;
	}

	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p,
			IPartialAggregate<R> toMerge, boolean createNew) {
		ListPartialAggregate<R> list = (ListPartialAggregate<R>) p;
		if (createNew) {
			list = new ListPartialAggregate<R>((ListPartialAggregate<R>) p);
		}
		list.addAll((ListPartialAggregate<R>) toMerge);
		return list;
	}

}
