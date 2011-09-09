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
    private final static String MERGEDOUBLEGRID = "MERGEDOUBLEGRID";
    private final static String MERGEFLOATGRID = "MERGEFLOATGRID";
    private final static String MERGEBYTEGRID = "MERGEBYTEGRID";
    private final static String MERGEBOOLEANGRID = "MERGEBOOLEANGRID";
    private final static String LAST = "LAST";
    private final static String FIRST = "FIRST";
    private final static String NTH = "NTH";
    private final static String PMERGE = "PMERGE";
    private static Collection<String> names = new LinkedList<String>();
    {
        names.add(MERGEDOUBLEGRID);
        names.add(MERGEFLOATGRID);
        names.add(MERGEBYTEGRID);
        names.add(MERGEBOOLEANGRID);
        names.add(LAST);
        names.add(FIRST);
        names.add(NTH);
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
        IAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> aggFunc = null;
        if (key.getName().equalsIgnoreCase(MERGEDOUBLEGRID)) {
            aggFunc = new MergeDoubleGrid(pos);
        }
        else if (key.getName().equalsIgnoreCase(MERGEFLOATGRID)) {
            aggFunc = new MergeFloatGrid(pos);
        }
        else if (key.getName().equalsIgnoreCase(MERGEBYTEGRID)) {
            aggFunc = new MergeByteGrid(pos);
        }
        else if (key.getName().equalsIgnoreCase(MERGEBOOLEANGRID)) {
            aggFunc = new MergeBooleanGrid(pos);
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
        else if (key.getName().equalsIgnoreCase(PMERGE)) {
            aggFunc = new RelationalPolygonAggregation(pos);
        }
        else {
            throw new IllegalArgumentException(String.format("No such Aggregatefunction: %s",
                    key.getName()));
        }
        return aggFunc;
    }

}
