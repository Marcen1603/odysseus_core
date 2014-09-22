/*******************************************************************************
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.wrapper.opcda.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DoubleHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IntegerHandler;
import de.uniol.inf.is.odysseus.core.datahandler.LongHandler;
import de.uniol.inf.is.odysseus.core.datahandler.ShortDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.opcda.datatype.OPCValue;
import de.uniol.inf.is.odysseus.wrapper.opcda.sdf.schema.SDFOPCDADatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class OPCDADataHandler extends AbstractDataHandler<OPCValue<Double>> {
    static protected List<String> types = new ArrayList<>();
    static {
        OPCDADataHandler.types.add(SDFOPCDADatatype.OPCVALUE.getURI());
    }
    private final IDataHandler<Double> doubleHandler = new DoubleHandler();
    private final IDataHandler<Long> longHandler = new LongHandler();
    private final IDataHandler<Short> shortHandler = new ShortDataHandler();
    private final IDataHandler<Integer> intHandler = new IntegerHandler();

    /**
     * 
     * Class constructor.
     *
     */
    public OPCDADataHandler() {
        super();

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IDataHandler<OPCValue<Double>> getInstance(final SDFSchema schema) {
        return new OPCDADataHandler();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public OPCValue<Double> readData(final ObjectInputStream inputStream) throws IOException {
        long timestamp = this.longHandler.readData(inputStream).longValue();
        double value = this.doubleHandler.readData(inputStream).doubleValue();
        short quality = this.shortHandler.readData(inputStream).shortValue();
        int error = this.intHandler.readData(inputStream).intValue();

        return new OPCValue<Double>(timestamp, value, quality, error);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public OPCValue<Double> readData(final String string) {
        if (string != null && string.length() > 0) {
            long timestamp = this.longHandler.readData(string).longValue();
            double value = this.doubleHandler.readData(string).doubleValue();
            short quality = this.shortHandler.readData(string).shortValue();
            int error = this.intHandler.readData(string).intValue();
            return new OPCValue<Double>(timestamp, value, quality, error);
        }
        return null;

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public OPCValue<Double> readData(final ByteBuffer buffer) {
        long timestamp = this.longHandler.readData(buffer).longValue();
        double value = this.doubleHandler.readData(buffer).doubleValue();
        short quality = this.shortHandler.readData(buffer).shortValue();
        int error = this.intHandler.readData(buffer).intValue();
        return new OPCValue<Double>(timestamp, value, quality, error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OPCValue<Double> readData(List<String> input) {
        long timestamp = this.longHandler.readData(input.get(0)).longValue();
        double value = this.doubleHandler.readData(input.get(1)).doubleValue();
        short quality = this.shortHandler.readData(input.get(2)).shortValue();
        int error = this.intHandler.readData(input.get(3)).intValue();
        return new OPCValue<Double>(timestamp, value, quality, error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OPCValue<Double> readData(String[] input) {
        long timestamp = this.longHandler.readData(input[0]).longValue();
        double value = this.doubleHandler.readData(input[1]).doubleValue();
        short quality = this.shortHandler.readData(input[2]).shortValue();
        int error = this.intHandler.readData(input[3]).intValue();
        return new OPCValue<Double>(timestamp, value, quality, error);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        @SuppressWarnings("unchecked")
		final OPCValue<Double> value = (OPCValue<Double>) data;
        this.longHandler.writeData(buffer, new Long(value.getTimestamp()));
        this.doubleHandler.writeData(buffer, new Double(value.getValue()));
        this.shortHandler.writeData(buffer, new Short(value.getQuality()));
        this.intHandler.writeData(buffer, new Integer(value.getError()));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    final public List<String> getSupportedDataTypes() {
        return OPCDADataHandler.types;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int memSize(final Object data) {
        return (Long.SIZE + Double.SIZE + Short.SIZE + Integer.SIZE) / 8;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Class<?> createsType() {
        return OPCValue.class;
    }
}
