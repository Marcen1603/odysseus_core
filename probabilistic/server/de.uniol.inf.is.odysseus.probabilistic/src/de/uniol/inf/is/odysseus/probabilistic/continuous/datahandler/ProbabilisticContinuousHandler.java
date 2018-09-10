/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.continuous.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousHandler extends AbstractDataHandler<ProbabilisticDouble> {

    /** The continuous probabilistic handler handles all distributions. */
    private static final List<String> TYPES = new ArrayList<String>();
    static {
        ProbabilisticContinuousHandler.TYPES.add(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI());
    }


    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.
     * lang.String)
     */
    @Override
    public final ProbabilisticDouble readData(final String string) {
        Objects.requireNonNull(string);
        Preconditions.checkArgument(!string.isEmpty());
        final int distributionIndex = Integer.parseInt(string.substring(1,string.length()-1));
        return new ProbabilisticDouble(distributionIndex);
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.
     * nio.ByteBuffer)
     */
    @Override
    public final ProbabilisticDouble readData(final ByteBuffer buffer) {
        Objects.requireNonNull(buffer);
        Preconditions.checkArgument(buffer.remaining() >= 4);
        final int distributionIndex = buffer.getInt();
        return new ProbabilisticDouble(distributionIndex);
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#writeData(java
     * .nio.ByteBuffer, java.lang.Object)
     */
    @Override
    public final void writeData(final ByteBuffer buffer, final Object data) {
        Objects.requireNonNull(buffer);
        Objects.requireNonNull(data);
        final ProbabilisticDouble value = (ProbabilisticDouble) data;
        buffer.putInt(value.getDistribution());
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#memSize(java.lang
     * .Object)
     */
    @Override
    public final int memSize(final Object attribute) {
        return Integer.SIZE / 8;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getInstance
     * (de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
     */
    @Override
    protected final IDataHandler<ProbabilisticDouble> getInstance(final SDFSchema schema) {
        return new ProbabilisticContinuousHandler();
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#
     * getSupportedDataTypes()
     */
    @Override
    public final List<String> getSupportedDataTypes() {
        return Collections.unmodifiableList(ProbabilisticContinuousHandler.TYPES);
    }

    @Override
    public final Class<?> createsType() {
        return ProbabilisticDouble.class;
    }

}
