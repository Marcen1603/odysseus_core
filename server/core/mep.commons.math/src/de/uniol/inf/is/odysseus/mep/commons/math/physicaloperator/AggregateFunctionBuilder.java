/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.relational.RelationalGreenwaldKhannaMedian;
import de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.relational.RelationalMedian;
import de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.relational.RelationalMedian2;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class AggregateFunctionBuilder implements IAggregateFunctionBuilder {

    private final static String MEDIAN = "MEDIAN";
    private final static String AMEDIAN = "AMEDIAN";
    private final static String MEDIAN2 = "MEDIAN2";
    private static Collection<String> names = new LinkedList<String>();
    {
        names.add(MEDIAN);
        names.add(AMEDIAN);
        names.add(MEDIAN2);
    }

    @Override
    public IAggregateFunction<Tuple<?>, Tuple<?>> createAggFunction(AggregateFunction key, SDFSchema schema, int[] pos, boolean partialAggregateInput, String datatype) {
        IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
        if (key.getName().equalsIgnoreCase(MEDIAN)) {
            aggFunc = RelationalMedian.getInstance(pos[0], partialAggregateInput);
        }
        else if (key.getName().equalsIgnoreCase(AMEDIAN)) {
            aggFunc = RelationalGreenwaldKhannaMedian.getInstance(pos[0], partialAggregateInput);
        }
        else if (key.getName().equalsIgnoreCase(MEDIAN2)) {
            aggFunc = RelationalMedian2.getInstance(pos[0], partialAggregateInput);
        }
        else {
            throw new IllegalArgumentException("No such Aggregatefunction");
        }
        return aggFunc;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends IStreamObject> getDatamodel() {
        return Tuple.class;
    }

    @Override
    public Collection<String> getFunctionNames() {
        return names;
    }
}
