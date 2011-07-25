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
    private final static String MERGEGRID = "MERGEGRID";
    private final static String LAST = "LAST";
    private final static String FIRST = "FIRST";
    private final static String NTH = "NTH";
    private static Collection<String> names = new LinkedList<String>();
    {
        names.add(MERGEGRID);
        names.add(LAST);
        names.add(FIRST);
        names.add(NTH);
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
        if (key.getName().equalsIgnoreCase(MERGEGRID)) {
            aggFunc = new MergeGrid(pos);
        }
        else if (key.getName().equalsIgnoreCase(LAST)) {
            aggFunc = RelationalLast.getInstance();
        }
        else if (key.getName().equalsIgnoreCase(FIRST)) {
            aggFunc = RelationalFirst.getInstance();
        }
        else if (key.getName().equalsIgnoreCase(NTH)) {
            aggFunc = RelationalNth.getInstance(Integer.parseInt(key.getProperty("nth")));
        }
        else {
            throw new IllegalArgumentException("No such Aggregatefunction");
        }
        return aggFunc;
    }

}
