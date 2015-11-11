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
package de.uniol.inf.is.odysseus.prototyping.aggregation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AbstractAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class AggregationFunctionBuilder extends AbstractAggregateFunctionBuilder {
    private final static String SCRIPT = "SCRIPT";
    private final static String BEAN = "BEAN";
    private static Collection<String> names = new LinkedList<>();
    {
        AggregationFunctionBuilder.names.add(AggregationFunctionBuilder.SCRIPT);
        AggregationFunctionBuilder.names.add(AggregationFunctionBuilder.BEAN);
    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends IStreamObject> getDatamodel() {
        return Tuple.class;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getFunctionNames() {
        return AggregationFunctionBuilder.names;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IAggregateFunction<?, ?> createAggFunction(final AggregateFunction key, final SDFSchema schema, final int[] pos, final boolean partialAggregateInput, final String datatype) {
        IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
        Objects.requireNonNull(key);
        Objects.requireNonNull(key.getName());
        Objects.requireNonNull(key.getProperty("resource"));

        if (key.getName().equalsIgnoreCase(AggregationFunctionBuilder.SCRIPT)) {
            aggFunc = new JSR223Aggregation(pos, key.getProperty("resource"), partialAggregateInput, datatype);
        }
        else if (key.getName().equalsIgnoreCase(AggregationFunctionBuilder.BEAN)) {
            aggFunc = new BeanAggregation(pos, key.getProperty("resource"), partialAggregateInput, datatype);
        }
        else {
            throw new IllegalArgumentException(String.format("No such Aggregatefunction: %s", key.getName()));
        }
        return aggFunc;
    }

}
