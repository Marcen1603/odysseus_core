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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractCSVHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResourceParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;

/**
 * Generic sender operator to transfer processing results to arbitrary targets
 * using existing transport, protocol, and data handler. Example: <code>
 * Sender({wrapper='GenericPush', transport='File',protocol='SimpleCSV',
 * dataHandler='Tuple',options=[['filename','outfile.txt']]}, result)
 * </code>
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
abstract public class AbstractSenderAO extends AbstractLogicalOperator {
	/**
     * 
     */
	private static final long serialVersionUID = -6830784739913623456L;
	private Resource sink = null;
	private String dataHandler = new TupleDataHandler().getSupportedDataTypes()
			.get(0);
	private boolean writeMetaData;
	private String protocolHandler;
	private String transportHandler;
	final private Map<String, String> optionsMap = new HashMap<>();
	private String wrapper;

	/**
	 * Default constructor
	 */
	public AbstractSenderAO() {
		super();
	}

	/**
	 * Creates a new {@link SenderAO} with the given wrapper and options
	 * 
	 * @param sink
	 *            The sink name
	 * @param wrapper
	 *            The wrapper name
	 * @param optionsMap
	 *            The options
	 */
	public AbstractSenderAO(Resource sink, String wrapper,
			Map<String, String> optionsMap) {
		this.sink = sink;
		this.wrapper = wrapper;
		this.optionsMap.putAll(optionsMap);
	}

	/**
	 * Creates a new {@link SenderAO} with the given wrapper, data handler, and
	 * options
	 * 
	 * @param sink
	 *            The sink name
	 * @param wrapper
	 *            The wrapper name
	 * @param dataHandler
	 *            The name of the data handler
	 * @param optionsMap
	 *            The options
	 */
	public AbstractSenderAO(Resource sink, String wrapper, String dataHandler,
			Map<String, String> optionsMap) {
		this.sink = sink;
		this.wrapper = wrapper;
		this.dataHandler = dataHandler;
		this.optionsMap.putAll(optionsMap);
	}

	/**
	 * Clone constructor
	 * 
	 * @param senderAO
	 *            The {@link SenderAO} instance
	 */
	public AbstractSenderAO(AbstractSenderAO senderAO) {
		super(senderAO);
		this.sink = senderAO.sink;
		this.wrapper = senderAO.wrapper;
		this.dataHandler = senderAO.dataHandler;
		this.optionsMap.putAll(senderAO.optionsMap);
		this.protocolHandler = senderAO.protocolHandler;
		this.transportHandler = senderAO.transportHandler;
		this.writeMetaData = senderAO.writeMetaData;
	}

	/**
	 * @return The sink name
	 */
	public Resource getSinkname() {
		return sink;
	}

	/**
	 * Set the sink name
	 * 
	 * @param sink
	 *            The sink name
	 */
	@Parameter(name = "SINK", type = ResourceParameter.class, optional = false, doc = "The name of the sink.")
	public void setSink(Resource sink) {
		this.sink = sink;
	}
	
	public Resource getSink() {
		return sink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
	 * #getName()
	 */
	@Override
	public String getName() {
		String name = getSinkname().getResourceName();
		if (Strings.isNullOrEmpty(name)) {
			return super.getName();
		}
		return name;
	}

	/**
	 * Set the options of the sender operator for transport and prrotocol
	 * handler
	 * 
	 * @param value
	 *            A {@link Map} of options
	 */
	public void setOptions(List<Option> value) {
		this.optionsMap.clear();
		for (Option option : value) {
			optionsMap.put(option.getName().toLowerCase(), option.getValue());
		}
	}
	
	public void setOptionMap(Map<String, String> options) {
		this.optionsMap.clear();
		this.optionsMap.putAll(options);
	}
	
	protected void addOption(String key, String value) {
		optionsMap.put(key.toLowerCase(), value);
	}

	protected String getOption( String key ) {
		return optionsMap.get(key);
	}
	
	/**
	 * @return The options
	 */
	public Map<String, String> getOptionsMap() {
		return optionsMap;
	}

	/**
	 * Set the wrapper type
	 * 
	 * @param wrapper
	 *            The wrapper type
	 */
	public void setWrapper(String wrapper) {
		this.wrapper = wrapper;
	}

	/**
	 * @return The wrapper type
	 */
	public String getWrapper() {
		return wrapper;
	}

	/**
	 * @return The name of the data handler
	 */
	public String getDataHandler() {
		return dataHandler;
	}

	/**
	 * Set the name of the data handler ie.: Tuple
	 * 
	 * @param dataHandler
	 *            The name of the data handler
	 */
	public void setDataHandler(String dataHandler) {
		this.dataHandler = dataHandler;
	}

	@Parameter(type = BooleanParameter.class, name = AbstractCSVHandler.CSV_WRITE_METADATA, optional = true, doc = "Write metadata.")
	public void setWriteMetaData(boolean writeMetaData){
		this.writeMetaData = writeMetaData;
	}
	
	public boolean isWriteMetaData(){
		return writeMetaData;
	}
	
	/**
	 * @return The name of the protocol handler
	 */
	public String getProtocolHandler() {
		return protocolHandler;
	}

	/**
	 * Set the name of the protocol handler ie.: CSV
	 * 
	 * @param protocolHandler
	 *            The name of the protocol handler
	 */
	public void setProtocolHandler(String protocolHandler) {
		this.protocolHandler = protocolHandler;
	}

	/**
	 * @return The name of the transport handler
	 */
	public String getTransportHandler() {
		return transportHandler;
	}

	/**
	 * Set the name of the transport handler ie.: TCPClient
	 * 
	 * @param transportHandler
	 *            The name of the transport handler
	 */
	public void setTransportHandler(String transportHandler) {
		this.transportHandler = transportHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
	 * #toString()
	 */
	@Override
	public String toString() {
		return getName() + " (" + this.wrapper + ")";
	}

	@Override
	public boolean isValid() {
		if (!WrapperRegistry.containsWrapper(this.wrapper)) {
			this.addError("Wrapper "
					+ this.wrapper + " unknown");
			return false;
		}

		return super.isValid();
	}

	@Override
	public boolean isSourceOperator() {
		return false;
	}
}
