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
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousAvg;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousCompleteness;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousCount;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousMax;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousMin;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions.ProbabilisticContinuousSum;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.aggregationfunctions.ProbabilisticDiscreteCompleteness;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.aggregationfunctions.ProbabilisticDiscreteCount;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.aggregationfunctions.ProbabilisticDiscreteMultiWorldAvg;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.aggregationfunctions.ProbabilisticDiscreteMultiWorldMax;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.aggregationfunctions.ProbabilisticDiscreteMultiWorldMin;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.aggregationfunctions.ProbabilisticDiscreteMultiWorldSum;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.aggregationfunctions.ProbabilisticDiscreteOneWorldAvg;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.aggregationfunctions.ProbabilisticDiscreteOneWorldSum;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.aggregationfunctions.ProbabilisticDiscreteStdDev;

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
    /** The STDDEV aggregate. */
    private static final String COMPLETENESS = "COMPLETENESS";
    /** The available aggregate functions. */
    private static Collection<String> names = new LinkedList<String>();
    {
        ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.SUM);
        ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.COUNT);
        ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.AVG);
        ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.MIN);
        ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.MAX);
        ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.STDDEV);
        ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.COMPLETENESS);

    };

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.
     * IAggregateFunctionBuilder#getDatamodel()
     */
    @SuppressWarnings("rawtypes")
    @Override
    public final Class<? extends IStreamObject> getDatamodel() {
        return ProbabilisticTuple.class;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.
     * IAggregateFunctionBuilder#getFunctionNames()
     */
    @Override
    public final Collection<String> getFunctionNames() {
        return ProbabilisticAggregateFunctionBuilder.names;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.
     * IAggregateFunctionBuilder
     * #createAggFunction(de.uniol.inf.is.odysseus.core.
     * server.physicaloperator.aggregate.AggregateFunction, int[], boolean,
     * java.lang.String)
     */
    @Override
    public final IAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> createAggFunction(final AggregateFunction key, SDFSchema schema, final int[] pos,
            final boolean partialAggregateInput, final String outputDatatype) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(key.getName());
        Objects.requireNonNull(pos);
        Objects.requireNonNull(schema);
        Preconditions.checkElementIndex(0, pos.length);
        Preconditions.checkElementIndex(pos[0], schema.size());

        SDFAttribute attribute = schema.get(pos[0]);
        Preconditions.checkArgument(attribute.getDatatype() instanceof SDFProbabilisticDatatype);

        IAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> aggFunc = null;
        SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute.getDatatype();
        if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.AVG)) {
            if (datatype.isDiscrete()) {
                if (outputDatatype.equalsIgnoreCase(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.toString())) {
                    aggFunc = ProbabilisticDiscreteMultiWorldAvg.getInstance(pos[0], partialAggregateInput, outputDatatype);
                }
                else {
                    aggFunc = ProbabilisticDiscreteOneWorldAvg.getInstance(pos[0], partialAggregateInput, outputDatatype);
                }
            }
            else {
                aggFunc = ProbabilisticContinuousAvg.getInstance(pos[0], partialAggregateInput, outputDatatype);
            }
        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.SUM)) {
            if (datatype.isDiscrete()) {
                if (outputDatatype.equalsIgnoreCase(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.toString())) {
                    aggFunc = ProbabilisticDiscreteMultiWorldSum.getInstance(pos[0], partialAggregateInput, outputDatatype);
                }
                else {
                    aggFunc = ProbabilisticDiscreteOneWorldSum.getInstance(pos[0], partialAggregateInput, outputDatatype);
                }
            }
            else {
                aggFunc = ProbabilisticContinuousSum.getInstance(pos[0], partialAggregateInput, outputDatatype);
            }
        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.COUNT)) {
            if (datatype.isDiscrete()) {
                aggFunc = ProbabilisticDiscreteCount.getInstance(pos[0], partialAggregateInput, outputDatatype);
            }
            else {
                aggFunc = ProbabilisticContinuousCount.getInstance(pos[0], partialAggregateInput, outputDatatype);
            }
        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.MIN)) {
            if (datatype.isDiscrete()) {
                if (outputDatatype.equalsIgnoreCase(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.toString())) {
                    aggFunc = ProbabilisticDiscreteMultiWorldMin.getInstance(pos[0], partialAggregateInput, outputDatatype);
                }
                else {
                    // aggFunc =
                    // ProbabilisticDiscreteOneWorldMin.getInstance(pos[0],
                    // partialAggregateInput, datatype);
                    throw new IllegalArgumentException("MIN Aggregatefunction not implemented");
                }
            }
            else {
                aggFunc = ProbabilisticContinuousMin.getInstance(pos[0], partialAggregateInput, outputDatatype);
                throw new IllegalArgumentException("MIN Aggregatefunction not implemented");
            }
        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.MAX)) {
            if (datatype.isDiscrete()) {
                if (outputDatatype.equalsIgnoreCase(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.toString())) {
                    aggFunc = ProbabilisticDiscreteMultiWorldMax.getInstance(pos[0], partialAggregateInput, outputDatatype);
                }
                else {
                    // aggFunc =
                    // ProbabilisticDiscreteOneWorldMax.getInstance(pos[0],
                    // partialAggregateInput, datatype);
                    throw new IllegalArgumentException("MAX Aggregatefunction not implemented");
                }
            }
            else {
                aggFunc = ProbabilisticContinuousMax.getInstance(pos[0], partialAggregateInput, outputDatatype);
                throw new IllegalArgumentException("MAX Aggregatefunction not implemented");
            }
        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.STDDEV)) {
            aggFunc = ProbabilisticDiscreteStdDev.getInstance(pos[0], partialAggregateInput, outputDatatype);
            throw new IllegalArgumentException("STDDEV Aggregatefunction not implemented");
        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.COMPLETENESS)) {
            if (datatype.isDiscrete()) {
                aggFunc = ProbabilisticDiscreteCompleteness.getInstance(pos[0], partialAggregateInput, outputDatatype);
            }
            else {
                aggFunc = ProbabilisticContinuousCompleteness.getInstance(pos[0], partialAggregateInput, outputDatatype);
            }
        }
        else {
            throw new IllegalArgumentException("No such Aggregatefunction");
        }
        return aggFunc;
    }
}
