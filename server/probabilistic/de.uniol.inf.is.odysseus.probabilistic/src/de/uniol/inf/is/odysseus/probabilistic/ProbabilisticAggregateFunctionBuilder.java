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
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousAvg;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousCount;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousMax;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousMin;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousStdDev;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousSum;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticDiscreteCount;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticDiscreteMultiWorldAvg;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticDiscreteMultiWorldMax;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticDiscreteMultiWorldMin;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticDiscreteMultiWorldSum;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticDiscreteOneWorldAvg;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticDiscreteOneWorldSum;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions.ProbabilisticDiscreteStdDev;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticAggregateFunctionBuilder implements IAggregateFunctionBuilder {
	/** The SUM aggregate. */
	private static final String DISCRETE_SUM = "DISCRETE_SUM";
	/** The SUM aggregate. */
	private static final String CONTINUOUS_SUM = "CONTINUOUS_SUM";
	/** The COUNT aggregate. */
	private static final String DISCRETE_COUNT = "DISCRETE_COUNT";
	/** The COUNT aggregate. */
	private static final String CONTINUOUS_COUNT = "CONTINUOUS_OUNT";
	/** The AVG aggregate. */
	private static final String DISCRETE_AVG = "DISCRETE_AVG";
	/** The AVG aggregate. */
	private static final String CONTINUOUS_AVG = "CONTINUOUS_AVG";
	/** The MIN aggregate. */
	private static final String DISCRETE_MIN = "DISCRETE_MIN";
	/** The MIN aggregate. */
	private static final String CONTINUOUS_MIN = "CONTINUOUS_MIN";
	/** The MAX aggregate. */
	private static final String DISCRETE_MAX = "DISCRETE_MAX";
	/** The MAX aggregate. */
	private static final String CONTINUOUS_MAX = "CONTINUOUS_MAX";
	/** The STDDEV aggregate. */
	private static final String DISCRETE_STDDEV = "DISCRETE_STDDEV";
	/** The STDDEV aggregate. */
	private static final String CONTINUOUS_STDDEV = "CONTINUOUS_STDDEV";
	/** The available aggregate functions. */
	private static Collection<String> names = new LinkedList<String>();
	{
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.DISCRETE_SUM);
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.CONTINUOUS_SUM);
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.DISCRETE_COUNT);
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.CONTINUOUS_COUNT);
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.DISCRETE_AVG);
		ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.CONTINUOUS_AVG);
	};

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate. IAggregateFunctionBuilder#getDatamodel()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public final Class<? extends IStreamObject> getDatamodel() {
		return ProbabilisticTuple.class;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate. IAggregateFunctionBuilder#getFunctionNames()
	 */
	@Override
	public final Collection<String> getFunctionNames() {
		return ProbabilisticAggregateFunctionBuilder.names;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate. IAggregateFunctionBuilder #createAggFunction(de.uniol.inf.is.odysseus.core. server.physicaloperator.aggregate.AggregateFunction, int[], boolean, java.lang.String)
	 */
	@Override
	public final IAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> createAggFunction(final AggregateFunction key, final int[] pos, final boolean partialAggregateInput, final String datatype) {
		IAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> aggFunc = null;
		if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.DISCRETE_AVG)) {
			if (datatype.equalsIgnoreCase(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.toString())) {
				aggFunc = ProbabilisticDiscreteMultiWorldAvg.getInstance(pos[0], partialAggregateInput, datatype);
			} else {
				aggFunc = ProbabilisticDiscreteOneWorldAvg.getInstance(pos[0], partialAggregateInput, datatype);
			}
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.CONTINUOUS_AVG)) {
			aggFunc = ProbabilisticContinuousAvg.getInstance(pos[0], partialAggregateInput, datatype);
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.DISCRETE_SUM)) {
			if (datatype.equalsIgnoreCase(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.toString())) {
				aggFunc = ProbabilisticDiscreteMultiWorldSum.getInstance(pos[0], partialAggregateInput, datatype);
			} else {
				aggFunc = ProbabilisticDiscreteOneWorldSum.getInstance(pos[0], partialAggregateInput, datatype);
			}
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.CONTINUOUS_SUM)) {
			aggFunc = ProbabilisticContinuousSum.getInstance(pos[0], partialAggregateInput, datatype);
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.DISCRETE_COUNT)) {
			aggFunc = ProbabilisticDiscreteCount.getInstance(pos[0], partialAggregateInput, datatype);
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.CONTINUOUS_COUNT)) {
			aggFunc = ProbabilisticContinuousCount.getInstance(pos[0], partialAggregateInput, datatype);
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.DISCRETE_MIN)) {
			if (datatype.equalsIgnoreCase(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.toString())) {
				aggFunc = ProbabilisticDiscreteMultiWorldMin.getInstance(pos[0], partialAggregateInput, datatype);
			} else {
				// aggFunc =
				// ProbabilisticDiscreteOneWorldMin.getInstance(pos[0],
				// partialAggregateInput, datatype);
				throw new IllegalArgumentException("MIN Aggregatefunction not implemented");
			}
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.CONTINUOUS_MIN)) {
			aggFunc = ProbabilisticContinuousMin.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException("MIN Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.DISCRETE_MAX)) {
			if (datatype.equalsIgnoreCase(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.toString())) {
				aggFunc = ProbabilisticDiscreteMultiWorldMax.getInstance(pos[0], partialAggregateInput, datatype);
			} else {
				// aggFunc =
				// ProbabilisticDiscreteOneWorldMax.getInstance(pos[0],
				// partialAggregateInput, datatype);
				throw new IllegalArgumentException("MAX Aggregatefunction not implemented");
			}
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.CONTINUOUS_MAX)) {
			aggFunc = ProbabilisticContinuousMax.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException("MAX Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.DISCRETE_STDDEV)) {
			aggFunc = ProbabilisticDiscreteStdDev.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException("STDDEV Aggregatefunction not implemented");
		} else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.CONTINUOUS_STDDEV)) {
			aggFunc = ProbabilisticContinuousStdDev.getInstance(pos[0], partialAggregateInput, datatype);
			throw new IllegalArgumentException("STDDEV Aggregatefunction not implemented");
		} else {
			throw new IllegalArgumentException("No such Aggregatefunction");
		}
		return aggFunc;
	}
}
