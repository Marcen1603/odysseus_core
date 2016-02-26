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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IntegerHandler;
import de.uniol.inf.is.odysseus.core.datahandler.LongHandler;
import de.uniol.inf.is.odysseus.core.datahandler.ShortDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.opcda.datatype.OPCValue;
import de.uniol.inf.is.odysseus.wrapper.opcda.sdf.schema.SDFOPCDADatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @author Marco Grawunder
 *
 */
public class OPCDADataHandler<T> extends AbstractDataHandler<OPCValue<T>> {
    static protected List<String> types = new ArrayList<>();
    static {
        OPCDADataHandler.types.add(SDFOPCDADatatype.OPCVALUE.getURI());
    }
    private final IDataHandler<Long> longHandler = new LongHandler();
    private final IDataHandler<Short> shortHandler = new ShortDataHandler();
    private final IDataHandler<Integer> intHandler = new IntegerHandler();
	private SDFSchema subType;
	private IDataHandler<?> valueHandler;

    /**
     * 
     * Class constructor.
     *
     */
    public OPCDADataHandler() {
        super(null);

    }

	public OPCDADataHandler(SDFSchema subType){
		this.subType = subType;
		this.valueHandler = DataHandlerRegistry.getDataHandler(this.subType.getAttribute(0).getAttributeName(), this.subType);

		//Is needed for handling of KeyValueObject
		if(this.valueHandler == null && subType.getAttribute(0).getDatatype().getSubType() != null) {
			this.valueHandler = DataHandlerRegistry.getDataHandler(this.subType.getAttribute(0).getDatatype().getSubType().toString(), this.subType);
		}
	}
    
    /**
     *
     * {@inheritDoc}
     */
    @Override
    public IDataHandler<OPCValue<T>> getInstance(final SDFSchema schema) {
        return new OPCDADataHandler<T>(schema);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public OPCValue<T> readData(final String string) {
        if (string != null && string.length() > 0) {
            long timestamp = this.longHandler.readData(string).longValue();
            short quality = this.shortHandler.readData(string).shortValue();
            int error = this.intHandler.readData(string).intValue();

            @SuppressWarnings("unchecked")
			T value = (T) this.valueHandler.readData(string);

            return new OPCValue<T>(timestamp, value, quality, error);
        }
        return null;

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public OPCValue<T> readData(final ByteBuffer buffer) {
        long timestamp = this.longHandler.readData(buffer).longValue();
        short quality = this.shortHandler.readData(buffer).shortValue();
        int error = this.intHandler.readData(buffer).intValue();

        @SuppressWarnings("unchecked")
		T value = (T) this.valueHandler.readData(buffer);

        
        return new OPCValue<T>(timestamp, value, quality, error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OPCValue<T> readData(Iterator<String> input) {
        long timestamp = this.longHandler.readData(input.next()).longValue();
        short quality = this.shortHandler.readData(input.next()).shortValue();
        int error = this.intHandler.readData(input.next()).intValue();

        @SuppressWarnings("unchecked")
		T value = (T) this.valueHandler.readData(input.next());

        return new OPCValue<T>(timestamp, value, quality, error);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        @SuppressWarnings("unchecked")
		final OPCValue<T> value = (OPCValue<T>) data;
        this.longHandler.writeData(buffer, new Long(value.getTimestamp()));        
        this.shortHandler.writeData(buffer, new Short(value.getQuality()));
        this.intHandler.writeData(buffer, new Integer(value.getError()));
        this.valueHandler.writeData(buffer, value.getValue());
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
