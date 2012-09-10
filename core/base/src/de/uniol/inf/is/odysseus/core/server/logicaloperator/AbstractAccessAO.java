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

abstract public class AbstractAccessAO extends AbstractLogicalOperator  {

	private static final long serialVersionUID = -5423444612698319659L;

	private String source = null;
	private List<String> inputSchema = null;

	private int port = -1;
	private String host;
	private String login;
	private String password;
	private boolean autoreconnect = false;
	private Map<String, String> optionsMap;
	private String wrapper;
	
	private String input;
	private String dataHandler = "Tuple";
	private String transformer;
	
	private String objectHandler;
	private String inputDataHandler;
	private String accessConnectionHandler = "nio";
	
	private String protocolHandler;
	private String transportHandler;

//	/**
//	 * This variable will be used to generate an ID for every new input tuple
//	 */
//	private static long ID = 1;

	/**
	 * this variable will be used, if a wildcard is necessary for an id
	 */
	private static Long wildcard = Long.valueOf(-1);

	public AbstractAccessAO(AbstractLogicalOperator po) {
		super(po);
	}

	public AbstractAccessAO() {
		super();
	}

	public AbstractAccessAO(AbstractAccessAO po) {
		super(po);
		source = po.source;
		port = po.port;
		host = po.host;
		login = po.login;
		password = po.password;
		autoreconnect = po.autoreconnect;
		wrapper = po.wrapper;
		optionsMap = po.optionsMap != null? new HashMap<String, String>(po.optionsMap):null;
		inputSchema = po.inputSchema;		
		input = po.input;
		dataHandler = po.dataHandler;
		transformer = po.transformer;
		protocolHandler = po.protocolHandler;
		transportHandler = po.transportHandler;
		
	}

	public AbstractAccessAO(String source, String wrapper, Map<String, String> optionsMap) {
		this.source = source;
		this.wrapper = wrapper;
		this.optionsMap = optionsMap;
	}

	public AbstractAccessAO(String source, String wrapper, String input, String transformer, String dataHandler, Map<String, String> optionsMap){
		this.source = source;
		this.wrapper = wrapper;
		this.input = input;
		this.transformer = transformer;
		this.dataHandler = dataHandler;
		this.optionsMap = optionsMap;
	}
	
	/**
	 * @return the source
	 */
	public String getSourcename() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	public void setInputSchema(List<String> inputSchema) {
		this.inputSchema = inputSchema;
	}
	
	public List<String> getInputSchema() {
		return inputSchema;
	}

	@Override
	public String getName() {
		return getSourcename();
	}

//	private static long genID() {
//		return ++ID;
//	}
//
//	public static List<Long> nextID() {
//		ArrayList<Long> idList = new ArrayList<Long>();
//		idList.add(Long.valueOf(genID()));
//		return idList;
//	}

	public static Long getWildcard() {
		return wildcard;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	@Override
	public String toString() {
		return getName() + " ("
				+ this.getSourcename() 
				+ " | " + this.wrapper + ")";
	}

	@Override
	public boolean isAllPhysicalInputSet() {
		return true;
	}

	public void setLoginInfo(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}
	
	public String getPassword() {
		return password;
	}

	public boolean isAutoReconnectEnabled() {
		return autoreconnect;
	}
	
	public void setAutoReconnectEnabled(boolean enable){
		this.autoreconnect = enable;		
	}

	public void setOptions(Map<String, String> value) {
		this.optionsMap = value;
	}
	
	public Map<String, String> getOptionsMap() {
		return optionsMap;
	}
	
	public String getWrapper() {
		return wrapper;
	}
	
	public String getInput() {
		return input;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public String getTransformer() {
		return transformer;
	}
	
	public void setTransformer(String transformer) {
		this.transformer = transformer;
	}
	
	public String getDataHandler() {
		return dataHandler;
	}
	
	public void setDataHandler(String dataHandler) {
		this.dataHandler = dataHandler;
	}
	
	public String getObjectHandler() {
		return objectHandler;
	}

	public void setObjectHandler(String objectHandler) {
		this.objectHandler = objectHandler;
	}

	public String getInputDataHandler() {
		return inputDataHandler;
	}

	public void setInputDataHandler(String inputDataHandler) {
		this.inputDataHandler = inputDataHandler;
	}

	public String getAccessConnectionHandler() {
		return accessConnectionHandler;
	}

	public void setAccessConnectionHandler(String accessConnectionHandler) {
		this.accessConnectionHandler = accessConnectionHandler;
	}

	public String getProtocolHandler() {
		return protocolHandler;
	}

	public void setProtocolHandler(String protocolHandler) {
		this.protocolHandler = protocolHandler;
	}

	public String getTransportHandler() {
		return transportHandler;
	}

	public void setTransportHandler(String transportHandler) {
		this.transportHandler = transportHandler;
	}


}

