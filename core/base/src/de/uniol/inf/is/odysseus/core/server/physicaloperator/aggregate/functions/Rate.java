/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class Rate<R extends MetaAttributeContainer<?>, W extends MetaAttributeContainer<?>>
		extends AbstractAggregateFunction<R, W> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7073964618277613915L;

	protected Rate() {
		super("RATE");
	}

	@Override
	public IPartialAggregate<R> init(R in) {
		IPartialAggregate<R> pa = new RatePartialAggregate<R>(
				(ITimeInterval) in.getMetadata());
		return pa;
	}

	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge,
			boolean createNew) {
		RatePartialAggregate<R> pa = null;
		if (createNew) {
			pa = new RatePartialAggregate<R>(((RatePartialAggregate<R>) p));
		} else {
			pa = (RatePartialAggregate<R>) p;
		}
		pa.add();
		return pa;
	}
}
