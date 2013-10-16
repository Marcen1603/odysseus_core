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
package de.uniol.inf.is.odysseus.probabilistic;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticDiscreteAvg;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticDiscreteCount;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticDiscreteSum;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticMax;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticMin;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticStdDev;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticAggregateFunctionBuilder implements IAggregateFunctionBuilder {
	/** The SUM aggregate. */
	private static final String SUM = "SUM";
	/** The COUNT aggregate. */
	private static final String COUNT = "COUNT";
	/** The AVG aggregate. */
	private static final String AVG = "AVG";
	/** The MIN aggregate. */
	private static final String MIN = "MIN";
	/** The MAX aggregate. */
	private static final String MAX = "MAX";
	/** The STDDEV aggregate. */
	private static final String STDDEV = "STDDEV";
	/** The available aggregate functions. */
	private static Collection<String> names = new LinkedList<String>();
	{
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.SUM);
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.COUNT);
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.AVG);
	};

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder#getDatamodel()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public final Class<? extends IStreamObject> getDatamodel() {
		return ProbabilisticTuple.class;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder#getFunctionNames()
	 */
	@Override
	public final Collection<String> getFunctionNames() {
		return ProbabilisticAggregateFunctionBuilder.names;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder#createAggFunction(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction, int[], boolean, java.lang.String)
	 */
	@Override
	public final IAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> createAggFunction(final AggregateFunction key, final int[] pos, final boolean partialAggregateInput, final String datatype) {
		IAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> aggFunc = null;
		if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.AVG)) {
			aggFunc = ProbabilisticDiscreteAvg.getInstance(pos[0], partialAggregateInput, datatype);
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.SUM)) {
			aggFunc = ProbabilisticDiscreteSum.getInstance(pos[0], partialAggregateInput, datatype);
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.COUNT)) {
			aggFunc = ProbabilisticDiscreteCount.getInstance(pos[0], partialAggregateInput, datatype);
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.MIN)) {
			aggFunc = ProbabilisticMin.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException("MIN Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.MAX)) {
			aggFunc = ProbabilisticMax.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException("MAX Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.STDDEV)) {
			aggFunc = ProbabilisticStdDev.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException("STDDEV Aggregatefunction not implemented");
		} else {
			throw new IllegalArgumentException("No such Aggregatefunction");
		}
		return aggFunc;
	}
}
