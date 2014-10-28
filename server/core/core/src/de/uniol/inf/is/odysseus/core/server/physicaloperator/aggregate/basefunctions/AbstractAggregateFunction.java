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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

abstract public class AbstractAggregateFunction<R, W> implements
		IAggregateFunction<R, W> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1420819068822674498L;
	final private String name;
	final private boolean partialAggregateInput;

	@Override
	public String getName() {
		return name;
	}

	protected AbstractAggregateFunction(String name,
			boolean partialAggregateInput) {
		this.name = name;
		this.partialAggregateInput = partialAggregateInput;
	}

	public boolean isPartialAggregateInput() {
		return partialAggregateInput;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
	 * .IInitializer#init(de.uniol.inf.is.odysseus.core.server.physicaloperator.
	 * aggregate.basefunctions.IPartialAggregate)
	 */
	@Override
	public IPartialAggregate<R> init(IPartialAggregate<R> in) {
		throw new RuntimeException(
				"Sorry. Cannot process partial aggregates as input");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
	 * .
	 * IMerger#merge(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate
	 * .basefunctions.IPartialAggregate,
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator
	 * .aggregate.basefunctions.IPartialAggregate, boolean)
	 */
	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p,
			IPartialAggregate<R> toMerge, boolean createNew) {
		throw new RuntimeException(
				"Sorry. Cannot process partial aggregates as input");
	}

	@Override
	public SDFDatatype getPartialAggregateType() {
		return null;
	}

	@Override
	public SDFDatatype getReturnType(List<SDFDatatype> inputTypes) {
		return inputTypes!=null&& inputTypes.size() > 0?inputTypes.get(0):SDFDatatype.DOUBLE;
	}
}
