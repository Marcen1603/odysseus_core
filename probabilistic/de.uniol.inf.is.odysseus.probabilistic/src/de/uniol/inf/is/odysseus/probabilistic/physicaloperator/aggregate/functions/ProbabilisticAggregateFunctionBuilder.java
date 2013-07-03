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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticAvg;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticCount;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticMax;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticMin;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticStdDev;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticSum;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticAggregateFunctionBuilder implements
		IAggregateFunctionBuilder {
	private static Collection<String> names = new LinkedList<String>();
	{
	};

	@Override
	public String getDatamodel() {
		return "relational";
	}

	@Override
	public Collection<String> getFunctionNames() {
		return ProbabilisticAggregateFunctionBuilder.names;
	}

	@Override
	public IAggregateFunction<?, ?> createAggFunction(
			final AggregateFunction key, final int[] pos, boolean partialAggregateInput, String datatype) {
		IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
		if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.PROBABILISTIC_NAMESPACE + "AVG")) {
			aggFunc = ProbabilisticAvg.getInstance(pos[0], partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.PROBABILISTIC_NAMESPACE + "SUM")) {
			aggFunc = ProbabilisticSum.getInstance(pos[0], partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.PROBABILISTIC_NAMESPACE + "COUNT")) {
			aggFunc = ProbabilisticCount.getInstance(pos[0], partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.PROBABILISTIC_NAMESPACE + "MIN")) {
			aggFunc = ProbabilisticMin.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException(
					"MIN Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.PROBABILISTIC_NAMESPACE + "MAX")) {
			aggFunc = ProbabilisticMax.getInstance(pos[0],partialAggregateInput, datatype);
			throw new IllegalArgumentException(
					"MAX Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.PROBABILISTIC_NAMESPACE + "STDDEV")) {
			aggFunc = ProbabilisticStdDev.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException(
					"STDDEV Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.CONTINUOUS_PROBABILISTIC_NAMESPACE
						+ "AVG")) {
			throw new IllegalArgumentException(
					"AVG Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.CONTINUOUS_PROBABILISTIC_NAMESPACE
						+ "SUM")) {
			throw new IllegalArgumentException(
					"SUM Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.CONTINUOUS_PROBABILISTIC_NAMESPACE
						+ "COUNT")) {
			throw new IllegalArgumentException(
					"COUNT Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.CONTINUOUS_PROBABILISTIC_NAMESPACE
						+ "MIN")) {
			throw new IllegalArgumentException(
					"MIN Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.CONTINUOUS_PROBABILISTIC_NAMESPACE
						+ "MAX")) {
			throw new IllegalArgumentException(
					"MAX Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.CONTINUOUS_PROBABILISTIC_NAMESPACE
						+ "STDDEV")) {
			throw new IllegalArgumentException(
					"STDDEV Aggregatefunction not implemented");
		} else {
			throw new IllegalArgumentException("No such Aggregatefunction");
		}
		return aggFunc;
	}
}
