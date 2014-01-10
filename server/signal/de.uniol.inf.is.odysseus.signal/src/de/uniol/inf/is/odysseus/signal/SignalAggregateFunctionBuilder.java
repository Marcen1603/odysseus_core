/**
 * 
 */
package de.uniol.inf.is.odysseus.signal;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.signal.physicaloperator.aggregationfunctions.FFTransformation;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SignalAggregateFunctionBuilder implements IAggregateFunctionBuilder {

    /** The FFT aggregate. */
    private static final String FFT = "FFT";
    /** The available aggregate functions. */
    private static Collection<String> names = new LinkedList<String>();
    {
        SignalAggregateFunctionBuilder.names.add(SignalAggregateFunctionBuilder.FFT);
    };

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.
     * IAggregateFunctionBuilder#getDatamodel()
     */
    @SuppressWarnings("rawtypes")
    @Override
    public final Class<? extends IStreamObject> getDatamodel() {
        return Tuple.class;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.
     * IAggregateFunctionBuilder#getFunctionNames()
     */
    @Override
    public final Collection<String> getFunctionNames() {
        return SignalAggregateFunctionBuilder.names;
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
    public final IAggregateFunction<Tuple<?>, Tuple<?>> createAggFunction(final AggregateFunction key, SDFSchema schema, final int[] pos, final boolean partialAggregateInput,
            final String outputDatatype) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(key.getName());
        Objects.requireNonNull(pos);
        Objects.requireNonNull(schema);
        Preconditions.checkElementIndex(0, pos.length);
        Preconditions.checkElementIndex(pos[0], schema.size());

        SDFAttribute attribute = schema.get(pos[0]);
        Preconditions.checkArgument(attribute.getDatatype() instanceof SDFDatatype);

        IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
        if (key.getName().equalsIgnoreCase(SignalAggregateFunctionBuilder.FFT)) {
            aggFunc = FFTransformation.getInstance(pos[0], partialAggregateInput);
        }
        else {
            throw new IllegalArgumentException("No such Aggregatefunction");
        }
        return aggFunc;
    }

}
