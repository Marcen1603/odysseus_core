/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.statistic;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import cc.kuka.odysseus.statistic.physicaloperator.relational.RelationalDAgostinoPerasonOmnibusTest;
import cc.kuka.odysseus.statistic.physicaloperator.relational.RelationalJarqueBeraTest;
import cc.kuka.odysseus.statistic.physicaloperator.relational.RelationalPopulationKurtosis;
import cc.kuka.odysseus.statistic.physicaloperator.relational.RelationalPopulationSkewness;
import cc.kuka.odysseus.statistic.physicaloperator.relational.RelationalPopulationStandardDeviation;
import cc.kuka.odysseus.statistic.physicaloperator.relational.RelationalSampleKurtosis;
import cc.kuka.odysseus.statistic.physicaloperator.relational.RelationalSampleSkewness;
import cc.kuka.odysseus.statistic.physicaloperator.relational.RelationalSampleStandardDeviation;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AbstractAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class StatisticAggregateFunctionBuilder extends AbstractAggregateFunctionBuilder {
    /** The PSKEW aggregate. */
    private final static String POPULATIONSKEWNESS = "PSKEW";
    /** The SSKEW aggregate. */
    private final static String SAMPLESKEWNESS = "SSKEW";
    private final static String SAMPLESKEWNESS_ALIAS = "SKEW";

    /** The PKURT aggregate. */
    private final static String POPULATIONKURTOSIS = "PKURT";
    /** The SKURT aggregate. */
    private final static String SAMPLEKURTOSIS = "SKURT";
    private final static String SAMPLEKURTOSIS_ALIAS = "KURT";

    /** The PSTDDEV aggregate. */
    private final static String POPULATIONSTANDARDDEVIATION = "PSTDDEV";
    /** The SSTDDEV aggregate. */
    private final static String SAMPLESTANDARDDEVIATION = "SSTDDEV";

    private final static String JARQUEBETATEST = "JARQUE";
    private final static String DAGOSTINOPEARSONOMNIBUSTEST = "DPO";

    /** The available aggregate functions. */
    private static Collection<String> names = new LinkedList<>();
    {
        StatisticAggregateFunctionBuilder.names.add(StatisticAggregateFunctionBuilder.POPULATIONSKEWNESS);
        StatisticAggregateFunctionBuilder.names.add(StatisticAggregateFunctionBuilder.SAMPLESKEWNESS);
        StatisticAggregateFunctionBuilder.names.add(StatisticAggregateFunctionBuilder.SAMPLESKEWNESS_ALIAS);
        StatisticAggregateFunctionBuilder.names.add(StatisticAggregateFunctionBuilder.POPULATIONKURTOSIS);
        StatisticAggregateFunctionBuilder.names.add(StatisticAggregateFunctionBuilder.SAMPLEKURTOSIS);
        StatisticAggregateFunctionBuilder.names.add(StatisticAggregateFunctionBuilder.SAMPLEKURTOSIS_ALIAS);
        StatisticAggregateFunctionBuilder.names.add(StatisticAggregateFunctionBuilder.POPULATIONSTANDARDDEVIATION);
        StatisticAggregateFunctionBuilder.names.add(StatisticAggregateFunctionBuilder.SAMPLESTANDARDDEVIATION);
        StatisticAggregateFunctionBuilder.names.add(StatisticAggregateFunctionBuilder.JARQUEBETATEST);
        StatisticAggregateFunctionBuilder.names.add(StatisticAggregateFunctionBuilder.DAGOSTINOPEARSONOMNIBUSTEST);

    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public final Class<? extends IStreamObject> getDatamodel() {
        return Tuple.class;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final Collection<String> getFunctionNames() {
        return StatisticAggregateFunctionBuilder.names;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final IAggregateFunction<Tuple<?>, Tuple<?>> createAggFunction(final AggregateFunction key, final SDFSchema schema, final int[] pos, final boolean partialAggregateInput,
            final String outputDatatype) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(key.getName());
        Objects.requireNonNull(pos);
        Objects.requireNonNull(schema);

        IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
        if (key.getName().equalsIgnoreCase(StatisticAggregateFunctionBuilder.POPULATIONSKEWNESS)) {
            aggFunc = RelationalPopulationSkewness.getInstance(pos[0], partialAggregateInput);
        }
        else if ((key.getName().equalsIgnoreCase(StatisticAggregateFunctionBuilder.SAMPLESKEWNESS)) || (key.getName().equalsIgnoreCase(StatisticAggregateFunctionBuilder.SAMPLESKEWNESS_ALIAS))) {
            aggFunc = RelationalSampleSkewness.getInstance(pos[0], partialAggregateInput);
        }
        else if (key.getName().equalsIgnoreCase(StatisticAggregateFunctionBuilder.POPULATIONKURTOSIS)) {
            aggFunc = RelationalPopulationKurtosis.getInstance(pos[0], partialAggregateInput);
        }
        else if ((key.getName().equalsIgnoreCase(StatisticAggregateFunctionBuilder.SAMPLEKURTOSIS)) || (key.getName().equalsIgnoreCase(StatisticAggregateFunctionBuilder.SAMPLEKURTOSIS_ALIAS))) {
            aggFunc = RelationalSampleKurtosis.getInstance(pos[0], partialAggregateInput);
        }
        else if (key.getName().equalsIgnoreCase(StatisticAggregateFunctionBuilder.POPULATIONSTANDARDDEVIATION)) {
            aggFunc = RelationalPopulationStandardDeviation.getInstance(pos[0], partialAggregateInput);
        }
        else if (key.getName().equalsIgnoreCase(StatisticAggregateFunctionBuilder.SAMPLESTANDARDDEVIATION)) {
            aggFunc = RelationalSampleStandardDeviation.getInstance(pos[0], partialAggregateInput);
        }
        else if (key.getName().equalsIgnoreCase(StatisticAggregateFunctionBuilder.JARQUEBETATEST)) {
            aggFunc = RelationalJarqueBeraTest.getInstance(pos[0], partialAggregateInput);
        }
        else if (key.getName().equalsIgnoreCase(StatisticAggregateFunctionBuilder.DAGOSTINOPEARSONOMNIBUSTEST)) {
            aggFunc = RelationalDAgostinoPerasonOmnibusTest.getInstance(pos[0], partialAggregateInput);
        }
        else {
            throw new IllegalArgumentException("No such Aggregatefunction");
        }
        return aggFunc;
    }
}
