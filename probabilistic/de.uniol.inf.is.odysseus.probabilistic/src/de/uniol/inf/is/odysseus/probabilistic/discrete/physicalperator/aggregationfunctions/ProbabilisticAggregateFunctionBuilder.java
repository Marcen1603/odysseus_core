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
package de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticAggregateFunctionBuilder implements IAggregateFunctionBuilder {
	private final static String SUM = "SUM";
	private final static String COUNT = "COUNT";
	private final static String AVG = "AVG";
	private final static String MIN = "MIN";
	private final static String MAX = "MAX";
	private final static String STDDEV = "STDDEV";
	private static Collection<String> names = new LinkedList<String>();
	{
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.SUM);
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.COUNT);
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.AVG);
	};

	@Override
	public String getDatamodel() {
		return SchemaUtils.DATATYPE;
	}

	@Override
	public Collection<String> getFunctionNames() {
		return ProbabilisticAggregateFunctionBuilder.names;
	}

	@Override
	public IAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> createAggFunction(final AggregateFunction key, final int[] pos, final boolean partialAggregateInput, final String datatype) {
		IAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> aggFunc = null;
		if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.AVG)) {
			aggFunc = ProbabilisticAvg.getInstance(pos[0], partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.SUM)) {
			aggFunc = ProbabilisticSum.getInstance(pos[0], partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.COUNT)) {
			aggFunc = ProbabilisticCount.getInstance(pos[0], partialAggregateInput);
		} else if (key.getName().equalsIgnoreCase("MIN")) {
			aggFunc = ProbabilisticMin.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException("MIN Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase("MAX")) {
			aggFunc = ProbabilisticMax.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException("MAX Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase("STDDEV")) {
			aggFunc = ProbabilisticStdDev.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException("STDDEV Aggregatefunction not implemented");
		} else {
			throw new IllegalArgumentException("No such Aggregatefunction");
		}
		return aggFunc;
	}
}
