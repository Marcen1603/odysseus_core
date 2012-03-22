package de.uniol.inf.is.odysseus.salsa.aggregation;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class AggregationFunctionBuilder implements IAggregateFunctionBuilder {
    private final static String PMERGE = "PMERGE";

    private static Collection<String> names = new LinkedList<String>();
    {
        names.add(PMERGE);
    }

    @Override
    public String getDatamodel() {
        return "relational";
    }

    @Override
    public Collection<String> getFunctionNames() {
        return names;
    }

    @Override
    public IAggregateFunction<?, ?> createAggFunction(AggregateFunction key, int[] pos) {
        IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
        if (key.getName().equalsIgnoreCase(PMERGE)) {
            aggFunc = new RelationalPolygonAggregation(pos);
        }
        else {
            throw new IllegalArgumentException(String.format("No such Aggregatefunction: %s",
                    key.getName()));
        }
        return aggFunc;
    }

}
