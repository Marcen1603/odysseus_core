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
package de.uniol.inf.is.odysseus.wrapper.fs20.physicaloperator.access;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

/**
 * Protocol handler for the FS20 and FHT communication protocol. The
 * implementation is based on http://fhz4linux.info/tiki-index.php
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 * @param <T>
 */
public class FS20ProtocolHandler<T extends IStreamObject<? extends IMetaAttribute>> extends AbstractProtocolHandler<T> {

	protected BufferedReader reader;
	protected BufferedWriter writer;

	public FS20ProtocolHandler() {
		super();
	}

	public FS20ProtocolHandler(ITransportDirection direction,
			IAccessPattern access, IStreamObjectDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if (getDirection().equals(ITransportDirection.IN)) {
			if ((this.getAccessPattern().equals(IAccessPattern.PULL))
					|| (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL))) {
				reader = new BufferedReader(new InputStreamReader(
						getTransportHandler().getInputStream()));
			}
		} else {
			writer = new BufferedWriter(new OutputStreamWriter(
					getTransportHandler().getOutputStream()));
		}
	}

	@Override
	public void close() throws IOException {
		if (getDirection().equals(ITransportDirection.IN)) {
			if (reader != null) {
				reader.close();
			}
		} else {
			writer.close();
		}
		getTransportHandler().close();
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		throw new RuntimeException("Not implemented yet");

		//FS20ProtocolHandler<T> instance = new FS20ProtocolHandler<T>(direction,
		//		access, dataHandler, options);
		// return instance;
	}

	@Override
	public String getName() {
		return "FS20";
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		}
		return ITransportExchangePattern.OutOnly;
	}

	@Override
	public boolean hasNext() throws IOException {
		return reader.ready();
	}

	@Override
	public T getNext() throws IOException {
		throw new RuntimeException("Not implemented yet");
		// if (reader.ready()) {
		// return getDataHandler().readData(reader.readLine());
		// } else {
		// return null;
		// }
	}

	@Override
	public void write(T object) throws IOException {
		writer.write(object.toString());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		if(!(other instanceof FS20ProtocolHandler)) {
			return false;
		}
		// the datahandler was already checked in the isSemanticallyEqual-Method of AbstracProtocolHandler
		return true;
	}
}
