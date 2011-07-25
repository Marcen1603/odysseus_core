package de.uniol.inf.is.odysseus.salsa.aggregation;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class AggregationFunctionBuilder implements IAggregateFunctionBuilder {
    private final static String NAME = "MERGEGRID";
    private static Collection<String> names = new LinkedList<String>();
    {
        names.add(NAME);
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
        IAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> aggFunc = null;
        if (key.getName().equalsIgnoreCase(NAME)) {
            aggFunc = new MergeGrid(pos);
        }
        else {
            throw new IllegalArgumentException("No such Aggregatefunction");
        }
        return aggFunc;
    }

}
