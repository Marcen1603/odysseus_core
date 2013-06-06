/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.spatial.aggregation;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class AggregationFunctionBuilder implements IAggregateFunctionBuilder {
    private final static String       UNION_GEOMETRY = "UNIONGEOMETRY";

    private static Collection<String> names          = new LinkedList<String>();
    {
        AggregationFunctionBuilder.names.add(AggregationFunctionBuilder.UNION_GEOMETRY);
    }

    @Override
    public String getDatamodel() {
        return "relational";
    }

    @Override
    public Collection<String> getFunctionNames() {
        return AggregationFunctionBuilder.names;
    }

    @Override
    public IAggregateFunction<?, ?> createAggFunction(final AggregateFunction key, final int[] pos, boolean partialAggregateInput, String datatype) {
        IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
        if (key.getName().equalsIgnoreCase(AggregationFunctionBuilder.UNION_GEOMETRY)) {
            aggFunc = new UnionGeometry(pos, partialAggregateInput);
        }
        else {
            throw new IllegalArgumentException(String.format("No such Aggregatefunction: %s", key.getName()));
        }
        return aggFunc;
    }

}
