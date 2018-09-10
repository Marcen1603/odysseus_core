/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AbstractAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class AggregateFunctionBuilder extends AbstractAggregateFunctionBuilder {

    private final static String AMEDIAN2 = "AMEDIAN2";

    private static Collection<String> names = new LinkedList<String>();
    {

        names.add(AMEDIAN2);

    }

    @Override
    public IAggregateFunction<Tuple<?>, Tuple<?>> createAggFunction(AggregateFunction key, SDFSchema schema, int[] pos, boolean partialAggregateInput, String datatype) {
        IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
        if (key.getName().equalsIgnoreCase(AMEDIAN2)) {
            // aggFunc = RelationalMedian.getInstance(pos[0],
            // partialAggregateInput);
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
