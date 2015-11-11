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
package cc.kuka.odysseus.signal;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import cc.kuka.odysseus.signal.physicaloperator.relational.RelationalDynamicTimeWraping;
import cc.kuka.odysseus.signal.physicaloperator.relational.RelationalFFTransformation;
import cc.kuka.odysseus.signal.physicaloperator.relational.RelationalSpectralCentroid;
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
public class SignalAggregateFunctionBuilder extends AbstractAggregateFunctionBuilder {

    /** The FFT aggregate. */
    private static final String FFT = "FFT";
    /** The DTW aggregate. */
    private static final String DTW = "DTW";
    /** The Spectral Centroid aggregate. */
    private static final String SPECTRAL_CENTROID = "SpectralCentroid";
    /** The available aggregate functions. */
    private static Collection<String> names = new LinkedList<>();
    {
        SignalAggregateFunctionBuilder.names.add(SignalAggregateFunctionBuilder.FFT);
        SignalAggregateFunctionBuilder.names.add(SignalAggregateFunctionBuilder.DTW);
        SignalAggregateFunctionBuilder.names.add(SignalAggregateFunctionBuilder.SPECTRAL_CENTROID);
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
        return SignalAggregateFunctionBuilder.names;
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
        if (key.getName().equalsIgnoreCase(SignalAggregateFunctionBuilder.FFT)) {
            aggFunc = RelationalFFTransformation.getInstance(pos[0], partialAggregateInput);
        }
        else if (key.getName().equalsIgnoreCase(SignalAggregateFunctionBuilder.DTW)) {
            aggFunc = RelationalDynamicTimeWraping.getInstance(pos[0], pos[1], partialAggregateInput);
        }
        else if (key.getName().equalsIgnoreCase(SignalAggregateFunctionBuilder.SPECTRAL_CENTROID)) {
            aggFunc = RelationalSpectralCentroid.getInstance(pos[0], pos[1], partialAggregateInput);
        }
        else {
            throw new IllegalArgumentException("No such Aggregatefunction");
        }
        return aggFunc;
    }

}
