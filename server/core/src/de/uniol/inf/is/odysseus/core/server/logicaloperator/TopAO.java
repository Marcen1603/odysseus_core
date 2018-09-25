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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

/**
 * Ist nur eine Hilfsklasse um den obersten Knoten eines Plans eindeutig
 * bestimmen zu koennen.
 * 
 * @author Marco Grawunder
 * 
 */
public class TopAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 6533111765567598018L;

	public TopAO(TopAO po) {
		super(po);
	}

	public TopAO() {
		super();
	}

	public @Override
	TopAO clone() {
		return new TopAO(this);
	}

	@Override
	public boolean isSourceOperator() {
		return false;
	}
	
	@Override
	public InputOrderRequirement getInputOrderRequirement(int inputPort) {
		return InputOrderRequirement.NONE;
	}
	
	/**
	 * Creates a new {@link TopAO} on top of the sinks.
	 *
	 * @param sinks
	 *            The {@link ILogicalOperator}s which shall be subscribed to new
	 *            {@link TopAO}.
	 * @return The new {@link TopAO}.
	 */
	public static TopAO generateTopAO(final Collection<ILogicalOperator> sinks) {

		final TopAO topAO = new TopAO();
		int inputPort = 0;
		for (ILogicalOperator sink : sinks)
			topAO.subscribeToSource(sink, inputPort++, 0,
					sink.getOutputSchema());

		return topAO;

	}

	/**
	 * Removes all {@link TopAO} logical operators from a list of
	 * {@link ILogicalOperator}s representing an {@link ILogicalQuery}.
	 *
	 * @param operators
	 *            The list of {@link ILogicalOperator}s representing an
	 *            {@link ILogicalQuery}.
	 */
	public static void removeTopAOs(List<ILogicalOperator> operators) {

		final List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();

		for (final ILogicalOperator operator : operators) {

			if (operator instanceof TopAO) {

				operator.unsubscribeFromAllSources();
				operatorsToRemove.add(operator);

			}

		}

		for (final ILogicalOperator operatorToRemove : operatorsToRemove)
			operators.remove(operatorToRemove);

	}
}
