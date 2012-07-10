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
package de.uniol.inf.is.odysseus.markov.operator.aggregate;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class ForwardAggregationFunction extends AbstractAggregateFunction<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9078982639010201605L;
	
	public ForwardAggregationFunction(HiddenMarkovModel hmm) {
		super("FORWARD");
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, Tuple<?> toMerge, boolean createNew) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
