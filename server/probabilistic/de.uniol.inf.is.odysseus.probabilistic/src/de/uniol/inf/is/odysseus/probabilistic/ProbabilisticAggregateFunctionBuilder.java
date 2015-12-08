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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AbstractAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions.ProbabilisticAvg;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions.ProbabilisticCount;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions.ProbabilisticOneWorldAvg;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions.ProbabilisticStdDev;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions.ProbabilisticSum;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions.ProbabilisticTupleCompleteness;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticAggregateFunctionBuilder extends AbstractAggregateFunctionBuilder {
    /** The AVG aggregate. */
    private final static String AVG = "AVG";
    /** The MEDIAN aggregate. */
    private final static String MEDIAN = "MEDIAN";
    /** The SUM aggregate. */
    private final static String SUM = "SUM";
    /** The COUNT aggregate. */
    private final static String COUNT = "COUNT";
    /** The MIN aggregate. */
    private final static String MIN = "MIN";
    /** The MAX aggregate. */
    private final static String MAX = "MAX";
    /** The STDDEV aggregate. */
    private final static String STDDEV = "STDDEV";
    /** The COMPLETENESS aggregate. */
    private final static String COMPLETENESS = "COMPLETENESS";

    /** The available aggregate functions. */
    private static Collection<String> names = new LinkedList<String>();
    {
        ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.SUM);
        ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.COUNT);
        ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.AVG);
        ProbabilisticAggregateFunctionBuilder.names.add(ProbabilisticAggregateFunctionBuilder.MEDIAN);
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
    public final IAggregateFunction<ProbabilisticTuple<IProbabilistic>, ProbabilisticTuple<?>> createAggFunction(final AggregateFunction key, final SDFSchema schema, final int[] pos,
            final boolean partialAggregateInput, final String outputDatatype) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(key.getName());
        Objects.requireNonNull(pos);
        Objects.requireNonNull(schema);
        // Preconditions.checkElementIndex(0, pos.length);
        // Preconditions.checkElementIndex(pos[0], schema.size());

        // final SDFAttribute attribute = schema.get(pos[0]);
        // Preconditions.checkArgument(attribute.getDatatype() instanceof
        // SDFProbabilisticDatatype);

        IAggregateFunction<ProbabilisticTuple<IProbabilistic>, ProbabilisticTuple<?>> aggFunc = null;
        // SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype)
        // attribute.getDatatype();
        if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.AVG)) {
            if (outputDatatype.equalsIgnoreCase(SDFDatatype.DOUBLE.toString())) {
                aggFunc = ProbabilisticOneWorldAvg.getInstance(pos[0], partialAggregateInput, outputDatatype);
            }
            else {
                aggFunc = ProbabilisticAvg.getInstance(pos[0], partialAggregateInput, outputDatatype);
            }
        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.SUM)) {
            if (outputDatatype.equalsIgnoreCase(SDFDatatype.DOUBLE.toString())) {
                // aggFunc = ProbabilisticOneWorldSum.getInstance(pos[0],
                // partialAggregateInput, outputDatatype);
            }
            else {
                aggFunc = ProbabilisticSum.getInstance(pos[0], partialAggregateInput, outputDatatype);
            }
        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.COUNT)) {
            aggFunc = ProbabilisticCount.getInstance(partialAggregateInput, outputDatatype);
        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.MIN)) {
            // aggFunc =
            // ProbabilisticDiscreteOneWorldMin.getInstance(pos[0],
            // partialAggregateInput, datatype);
            throw new IllegalArgumentException("MIN Aggregatefunction not implemented");

        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.MAX)) {
            // aggFunc =
            // ProbabilisticDiscreteOneWorldMax.getInstance(pos[0],
            // partialAggregateInput, datatype);
            throw new IllegalArgumentException("MAX Aggregatefunction not implemented");
        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.STDDEV)) {
            aggFunc = ProbabilisticStdDev.getInstance(pos[0], partialAggregateInput, outputDatatype);
            throw new IllegalArgumentException("STDDEV Aggregatefunction not implemented");
        }
        else if (key.getName().equalsIgnoreCase(ProbabilisticAggregateFunctionBuilder.COMPLETENESS)) {
            if (pos.length == 0) {
                aggFunc = ProbabilisticTupleCompleteness.getInstance(partialAggregateInput, outputDatatype);
            }
            else {
                // aggFunc =
                // ProbabilisticAttributeCompleteness.getInstance(pos[0],
                // partialAggregateInput, outputDatatype);
            }
        }
        else {
            throw new IllegalArgumentException("No such Aggregatefunction");
        }
        return aggFunc;
    }
}
