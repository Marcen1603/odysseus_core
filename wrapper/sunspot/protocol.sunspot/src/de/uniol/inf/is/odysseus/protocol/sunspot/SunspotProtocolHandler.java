/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.protocol.sunspot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

/**
 * @author Dennis Geesen
 * @param <T>
 *
 */
public class SunspotProtocolHandler<T extends IStreamObject<IMetaAttribute>> extends AbstractProtocolHandler<T> {

	protected BufferedReader reader;

	public SunspotProtocolHandler() {
		super();
	}

	public SunspotProtocolHandler(ITransportDirection direction,
			IAccessPattern access, IStreamObjectDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.
	 * IProtocolHandler#open()
	 */
	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		reader = new BufferedReader(new InputStreamReader(getTransportHandler()
				.getInputStream()));
	}

	@Override
	public T getNext() throws IOException {
		String line = reader.readLine();
		System.out.println("LINE: " + line);
		return getDataHandler().readData(line);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.
	 * IProtocolHandler#close()
	 */
	@Override
	public void close() throws IOException {
		reader.close();
		getTransportHandler().close();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.
	 * AbstractProtocolHandler#write(java.lang.Object)
	 */
	@Override
	public void write(T object) throws IOException {
		throw new IllegalArgumentException("Currently not implemented");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.
	 * IProtocolHandler
	 * #createInstance(de.uniol.inf.is.odysseus.core.physicaloperator
	 * .access.transport.ITransportDirection,
	 * de.uniol.inf.is.odysseus.core.physicaloperator
	 * .access.transport.IAccessPattern, java.util.Map,
	 * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler,
	 * de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler)
	 */
	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		SunspotProtocolHandler<T> instance = new SunspotProtocolHandler<T>(
				direction, access, dataHandler, options);
		return instance;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.
	 * IProtocolHandler#getName()
	 */
	@Override
	public String getName() {
		return "Sunspot";
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if(!(o instanceof SunspotProtocolHandler)) {
			return false;
		} else {
			// Checking of the DataHandler has already been done in the isSemanticallyEqual-method of AbstractProtocolHandler
			return true;
		}
	}
}
