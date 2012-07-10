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

package de.uniol.inf.is.odysseus.spatial.grid.aggregation;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MergeGrid extends
		AbstractAggregateFunction<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 443315119696013306L;
	private final int attribPos;

	public MergeGrid(int[] pos) {
		super("MergeBeliefeGrid");
		this.attribPos = pos[0];
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(final Tuple<?> in) {
		final IPartialAggregate<Tuple<?>> grid = new GridPartialAggregate<Tuple<?>>(
				(Grid) in.getAttribute(attribPos));
		return grid;
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(
			final IPartialAggregate<Tuple<?>> p, final Tuple<?> toMerge,
			final boolean createNew) {
		GridPartialAggregate<Tuple<?>> grid = null;
		if (createNew) {
			grid = new GridPartialAggregate<Tuple<?>>(
					((GridPartialAggregate<Tuple<?>>) p).getGrid());
		} else {
			grid = (GridPartialAggregate<Tuple<?>>) p;
		}
		grid.merge((Grid) toMerge.getAttribute(attribPos));
		return grid;
	}

	@Override
	public Tuple<?> evaluate(final IPartialAggregate<Tuple<?>> p) {
		final GridPartialAggregate<Tuple<?>> grid = (GridPartialAggregate<Tuple<?>>) p;
		grid.evaluate();
		@SuppressWarnings("rawtypes")
		final Tuple<?> tuple = new Tuple(1, false);
		tuple.setAttribute(0, grid.getGrid());
		return tuple;
	}
}
