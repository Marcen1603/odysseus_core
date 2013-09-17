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

import de.uniol.inf.is.odysseus.core.collection.Resource;

abstract public class AbstractAccessAO extends AbstractLogicalOperator  {

	private static final long serialVersionUID = -5423444612698319659L;
	
	private List<String> inputSchema = null;

	private Map<String, String> optionsMap;
	private String wrapper;
	
	private String dataHandler;
	
	private String inputDataHandler;
	
	private String protocolHandler;
	private String transportHandler;
	
	private String dateFormat;

	private Resource accessAOName;


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
		wrapper = po.wrapper;
		optionsMap = po.optionsMap != null? new HashMap<String, String>(po.optionsMap):null;
		inputSchema = po.inputSchema;		
		dataHandler = po.dataHandler;
		protocolHandler = po.protocolHandler;
		transportHandler = po.transportHandler;
		accessAOName = po.accessAOName;
	}

	public AbstractAccessAO(Resource name, String wrapper, String transportHandler, String protocolHandler, String dataHandler, Map<String, String> optionsMap) {		
		setAccessAOName(name);
		this.wrapper = wrapper;
		this.transportHandler = transportHandler;
		this.protocolHandler = protocolHandler;
		this.dataHandler = dataHandler;
		this.optionsMap = optionsMap;
	}

	
	public void setInputSchema(List<String> inputSchema) {
		this.inputSchema = inputSchema;
	}
	
	public List<String> getInputSchema() {
		return inputSchema;
	}

	public static Long getWildcard() {
		return wildcard;
	}


	@Override
	public String toString() {
		return getName() + " (" + this.wrapper + ")";
	}

	@Override
	public boolean isAllPhysicalInputSet() {
		return true;
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
	
	public void setWrapper(String wrapper) {
		this.wrapper = wrapper;
	}
		
	public String getDataHandler() {
		return dataHandler;
	}
	
	public void setDataHandler(String dataHandler) {
		this.dataHandler = dataHandler;
	}
	
	public String getInputDataHandler() {
		return inputDataHandler;
	}

	public void setInputDataHandler(String inputDataHandler) {
		this.inputDataHandler = inputDataHandler;
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

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat){
		this.dateFormat = dateFormat;
	}
	
	public void setAccessAOName(Resource name) {
		super.setName(name.getResourceName());
		this.accessAOName = name;
	}
	
	public Resource getAccessAOName() {
		return accessAOName;
	}
}

