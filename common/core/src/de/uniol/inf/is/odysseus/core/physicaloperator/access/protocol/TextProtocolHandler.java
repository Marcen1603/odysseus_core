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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Scanner;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

public class TextProtocolHandler<T extends IStreamObject<IMetaAttribute>> extends AbstractProtocolHandler<T> {
	private String objectDelimiter;
	private Scanner scanner;
	private boolean keepDelimiter;
	private BufferedWriter writer;

	public TextProtocolHandler() {
		super();
	}

	public TextProtocolHandler(ITransportDirection direction,
			IAccessPattern access, IStreamObjectDataHandler<T> dataHandler,
			OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		init_internal();
	}

	public void init_internal() {
		if (optionsMap.containsKey("delimiter")) {
			objectDelimiter = optionsMap.get("delimiter");
		} else {
			objectDelimiter = ",";
		}
		if (optionsMap.containsKey("keepdelimiter")) {
			keepDelimiter = Boolean.parseBoolean(optionsMap
					.get("keepdelimiter"));
		} else {
			keepDelimiter = false;
		}
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if (getDirection().equals(ITransportDirection.IN)) {
			if ((this.getAccessPattern().equals(IAccessPattern.PULL))
					|| (this.getAccessPattern()
							.equals(IAccessPattern.ROBUST_PULL))) {
				this.scanner = new Scanner(getTransportHandler()
						.getInputStream(), getCharset().name());
				scanner.useDelimiter(objectDelimiter);
			}
		} else {
			if ((this.getAccessPattern().equals(IAccessPattern.PUSH))
					|| (this.getAccessPattern()
							.equals(IAccessPattern.ROBUST_PUSH))) {
				writer = new BufferedWriter(new OutputStreamWriter(
						getTransportHandler().getOutputStream()));
			}
		}

	}

	@Override
	public void close() throws IOException {
		if (getDirection().equals(ITransportDirection.IN)) {
			if (scanner != null) {
				scanner.close();
			}
		} else {
			if (writer != null) {
				writer.close();
			}
		}
		getTransportHandler().close();
	}

	@Override
	public boolean hasNext() throws IOException {
		return scanner.hasNext();
	}

	@Override
	public T getNext() throws IOException {
		if (keepDelimiter) {
			return getDataHandler().readData(scanner.next() + objectDelimiter);
		} else {
			return getDataHandler().readData(scanner.next());
		}
	}

	@Override
	public void write(T object) throws IOException {
		writer.write(object.toString());
		writer.write(objectDelimiter);
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {

		TextProtocolHandler<T> instance = new TextProtocolHandler<T>(direction,
				access, dataHandler, options);
		return instance;
	}

	// @Override
	// public IProtocolHandler<T> createInstance(Map<String, String> options,
	// ITransportHandler transportHandler,
	// IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
	// TextProtocolHandler<T> instance = new TextProtocolHandler<T>();
	// instance.setDataHandler(dataHandler);
	// instance.setTransportHandler(transportHandler);
	// instance.setTransfer(transfer);
	//
	// instance.charset = options.get("charset");
	// instance.objectDelimiter = options.get("delimiter");
	// instance.keepDelimiter =
	// Boolean.parseBoolean(options.get("keepdelimiter"));
	// return instance;
	// }

	@Override
	public String getName() {
		return "Text";
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		// TODO: check if callerId is relevant
		Scanner scanner = new Scanner(
				new ByteArrayInputStream(message.array()), getCharset().name());
		scanner.useDelimiter(objectDelimiter);
		while (scanner.hasNext()) {
			if (keepDelimiter) {
				getTransfer().transfer(
						getDataHandler().readData(
								scanner.next() + objectDelimiter));
			} else {
				getTransfer().transfer(
						getDataHandler().readData(scanner.next()));
			}
		}
		scanner.close();
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if (!(o instanceof TextProtocolHandler)) {
			return false;
		}
		TextProtocolHandler<?> other = (TextProtocolHandler<?>) o;
		if (!this.objectDelimiter.equals(other.getObjectDelimiter())
				|| this.keepDelimiter != other.isKeepDelimiter()) {
			return false;
		}
		return true;
	}

	public String getObjectDelimiter() {
		return objectDelimiter;
	}

	public boolean isKeepDelimiter() {
		return keepDelimiter;
	}

}
