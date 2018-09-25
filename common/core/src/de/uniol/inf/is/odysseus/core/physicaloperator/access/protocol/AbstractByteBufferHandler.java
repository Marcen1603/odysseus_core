/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

abstract public class AbstractByteBufferHandler<T extends IStreamObject<? extends IMetaAttribute>> extends AbstractProtocolHandler<T> {

    public static final String WRITE_METADATA = "writemetadata";
	protected ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

    public AbstractByteBufferHandler() {
        super();
    }

    public AbstractByteBufferHandler(ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<T> datahandler, OptionMap optionsMap) {
        super(direction, access, datahandler, optionsMap);
        init_internal();
    }

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
	}

	@Override
	public void close() throws IOException {
		getTransportHandler().close();
	}
    
	protected static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}

    private void init_internal() {
    	String byteOrderTxt = optionsMap.get("byteorder");
        if ("LITTLE_ENDIAN".equalsIgnoreCase(byteOrderTxt)) {
            byteOrder = ByteOrder.LITTLE_ENDIAN;
        }
        else {
            byteOrder = ByteOrder.BIG_ENDIAN;
        }
    }

	public ByteOrder getByteOrder() {
		return byteOrder;
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {

		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		}
		return ITransportExchangePattern.OutOnly;
	}
	

}
