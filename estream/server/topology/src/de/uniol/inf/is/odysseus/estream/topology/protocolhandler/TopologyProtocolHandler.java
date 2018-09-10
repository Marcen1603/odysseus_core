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
package de.uniol.inf.is.odysseus.estream.topology.protocolhandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.estream.topology.parser.AbstractTopologyParser;
import de.uniol.inf.is.odysseus.estream.topology.parser.TopologyParser;

/**
 * Data Handler for XML documents The schema attribute names define the XPath
 * for the attribute values
 *
 * @author Jens Plümer, Marcel Hamacher
 *
 */
public class TopologyProtocolHandler extends AbstractProtocolHandler<IStreamObject<? extends IMetaAttribute>> {
	private static String name = "TOPOLOGY";
	private static final String TYPE = "type";

	private static final Logger log = LoggerFactory.getLogger(TopologyProtocolHandler.class.getSimpleName());
	private AbstractTopologyParser<IStreamObject<? extends IMetaAttribute>> parser;
	private boolean isDone;

	public TopologyProtocolHandler() {
		super();
	}

	@Override
	public IProtocolHandler<IStreamObject<? extends IMetaAttribute>> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<IStreamObject<? extends IMetaAttribute>> dataHandler) {
		return new TopologyProtocolHandler(direction, access, options, dataHandler);
	}

	public TopologyProtocolHandler(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<IStreamObject<? extends IMetaAttribute>> dataHandler) {
		super(direction, access, dataHandler, options);
		log.info("Initialized " + TopologyProtocolHandler.class.getSimpleName());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

	/**
	 * Is called when a new connection with the transport handler is established
	 * 
	 * @param caller
	 */
	public void onConnect(ITransportHandler caller) {
		super.onConnect(caller);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if (getDirection().equals(ITransportDirection.IN)) {
			try {
				InputStream initialStream = getTransportHandler().getInputStream();
				initParser(initialStream, optionsMap.get(TYPE));
				if (this.parser != null) {
					parser.parse();
				}

				log.info("Parser initialized for " + optionsMap.get(TYPE));
			} catch (IllegalArgumentException e) {
				log.info("Given transport handler has no input stream");
			}
			isDone = false;
		}
	}

	@Override
	public void close() throws IOException {
		terminateParser();
		super.close();
		log.info("connection closed");
	}

	/**
	 * Is called when an existing connection to the transport handler is interrupted
	 * 
	 * @param caller
	 */
	public void onDisonnect(ITransportHandler caller) {
		terminateParser();
		if (getTransportHandler() != null) {
			try {
				if (getDirection().equals(ITransportDirection.OUT)) {
					getTransportHandler().processOutClose();
				} else if (getDirection().equals(ITransportDirection.IN)) {
					// getTransportHandler().processInClose();
				} else {
					getTransportHandler().processInClose();
					getTransportHandler().processOutClose();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onDisonnect(caller);
	}

	@Override
	public boolean hasNext() throws IOException {
		if (this.parser != null) {
			return parser.isDone() ? false : true;
		}
		return false;
	}

	/**
	 * pull-based processing
	 */
	@Override
	public IStreamObject<? extends IMetaAttribute> getNext() throws IOException {
		return hasNext() ? getDataHandler().readData(parser.parse()) : null;
	}

	/**
	 * Implement this method to process the message
	 * 
	 * @param message
	 *            as String Array
	 */
	public void process(String[] message) {
		try {
			if (optionsMap.containsKey(TYPE)) {
				initParser(String.join("", message).getBytes(), optionsMap.get(TYPE));

				while (!parser.isDone()) {
					Tuple<?> tuple = (Tuple<?>) parser.parse();

					if (tuple != null) {
						// log.info("tuple=" + tuple.toString());
						getTransfer().transfer(getDataHandler().readData(tuple));
					}
				}
			} else {
				throw new IllegalArgumentException("Could not find type to parse!");
			}
			terminateParser();
		} catch (IllegalArgumentException | NullPointerException e) {
			log.error("exception occurred: " + e);
			if (!isDone) {
				try {
					close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			throw e;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initParser(byte[] message, String type) {
		InputStream reader = new ByteArrayInputStream(message);
		parser = new TopologyParser(reader, getSchema(), type);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initParser(InputStream message, String type) {
		parser = new TopologyParser(message, getSchema(), type);
	}

	private void terminateParser() {
		if (parser != null) {
			parser.close();
			parser = null;
		}
	}
}
