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

import java.util.Map;

/**
 * Generic sender operator to transfer processing results to arbitrary targets
 * using existing transport, protocol, and data handler.
 * Example: <code>
 * Sender({wrapper='GenericPush', transport='File',protocol='SimpleCSV',
 * dataHandler='Tuple',options=[['filename','outfile.txt']]}, result)
 * </code>
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
// @LogicalOperator(maxInputPorts = Integer.MAX_VALUE, minInputPorts = 1, name =
// "Sender")
public class SenderAO extends AbstractLogicalOperator {
    /**
     * 
     */
    private static final long   serialVersionUID = -6830784739913623456L;

    private String              dataHandler      = "Tuple";
    private String              protocolHandler;
    private String              transportHandler;
    private Map<String, String> optionsMap;
    private String              wrapper;

    /**
     * @param po
     *            The {@link AbstractLogicalOperator} operator
     */
    public SenderAO(AbstractLogicalOperator po) {
        super(po);
    }

    /**
     * Default constructor
     */
    public SenderAO() {
        super();
    }

    /**
     * Creates a new {@link SenderAO} with the given wrapper and options
     * 
     * @param wrapper
     *            The wrapper name
     * @param optionsMap
     *            The options
     */
    public SenderAO(String wrapper, Map<String, String> optionsMap) {
        this.wrapper = wrapper;
        this.optionsMap = optionsMap;
    }

    /**
     * Creates a new {@link SenderAO} with the given wrapper, data handler, and
     * options
     * 
     * @param wrapper
     *            The wrapper name
     * @param dataHandler
     *            The name of the data handler
     * @param optionsMap
     *            The options
     */
    public SenderAO(String wrapper, String dataHandler, Map<String, String> optionsMap) {
        this.wrapper = wrapper;
        this.dataHandler = dataHandler;
        this.optionsMap = optionsMap;
    }

    /**
     * Clone constructor
     * 
     * @param senderAO
     *            The {@link SenderAO} instance
     */
    public SenderAO(SenderAO senderAO) {
        this.wrapper = senderAO.wrapper;
        this.dataHandler = senderAO.dataHandler;
        this.optionsMap = senderAO.optionsMap;
        this.protocolHandler = senderAO.protocolHandler;
        this.transportHandler = senderAO.transportHandler;
    }

    /**
     * Set the options of the sender operator for transport and prrotocol
     * handler
     * 
     * @param value
     *            A {@link Map} of options
     */
    // @Parameter(name = "options", type = StringParameter.class, optional =
    // true, isMap = true)
    public void setOptions(Map<String, String> value) {
        this.optionsMap = value;
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
    // @Parameter(name = "wrapper", type = StringParameter.class, optional =
    // false)
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
    // @Parameter(name = "dataHandler", type = StringParameter.class, optional =
    // false)
    public void setDataHandler(String dataHandler) {
        this.dataHandler = dataHandler;
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
    // @Parameter(name = "protocol", type = StringParameter.class, optional =
    // false)
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
    // @Parameter(name = "transport", type = StringParameter.class, optional =
    // false)
    public void setTransportHandler(String transportHandler) {
        this.transportHandler = transportHandler;
    }

    /*
     * (non-Javadoc)
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * #clone()
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new SenderAO(this);
    }
}
