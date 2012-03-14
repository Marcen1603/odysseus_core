/** Copyright [2011] [The Odysseus Team]
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class AccessAO extends AbstractLogicalOperator implements
		OutputSchemaSettable {

	private static final long serialVersionUID = -5423444612698319659L;
	/**
	 * Die Uri der von diesem AccessPO gekapselten Quelle
	 */
	private SDFSource source = null;
	private Map<Integer, SDFSchema> outputSchema = new HashMap<Integer, SDFSchema>();

	private int port;
	private String host;
	private String fileURL;
	private String login;
	private String password;
	private boolean autoreconnect = false;
	private Map<String, String> optionsMap;
	private String adapter;

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	/**
	 * This variable will be used to generate an ID for every new input tuple
	 */
	private static long ID = 1;

	/**
	 * this variable will be used, if a wildcard is necessary for an id
	 */
	private static Long wildcard = Long.valueOf(-1);

	public AccessAO(AbstractLogicalOperator po) {
		super(po);
	}

	public AccessAO() {
		super();
	}

	public AccessAO(AccessAO po) {
		super(po);
		this.source = po.source.clone();
		this.port = po.port;
		this.host = po.host;
		this.outputSchema = createCleanClone(po.outputSchema);
		this.login = po.login;
		this.password = po.password;
		this.autoreconnect = po.autoreconnect;
	}

	public AccessAO(SDFSource source) {
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public SDFSource getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(SDFSource source) {
		this.source = source;
	}

	@Override
	public SDFSchema getOutputSchema() {
		return getOutputSchema(0);
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema) {
		setOutputSchema(outputSchema, 0);
	}

	@Override
	public SDFSchema getOutputSchema(int port) {
		return outputSchema.get(port);
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema, int port) {
		this.outputSchema.put(port, outputSchema);
	}

	@Override
	public AccessAO clone() {
		return new AccessAO(this);
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	public String getSourceType() {
		return this.source.getSourceType();
	}

	private static long genID() {
		return ++ID;
	}

	public static List<Long> nextID() {
		ArrayList<Long> idList = new ArrayList<Long>();
		idList.add(Long.valueOf(genID()));
		return idList;
	}

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
				+ (this.getSource() != null ? this.getSource().getURI() : null)
				+ " | " + this.getSourceType() + ")";
	}

	@Override
	public boolean isAllPhysicalInputSet() {
		return true;
	}

	private static Map<Integer, SDFSchema> createCleanClone(
			Map<Integer, SDFSchema> old) {
		Map<Integer, SDFSchema> copy = new HashMap<Integer, SDFSchema>();
		for (Entry<Integer, SDFSchema> e : old.entrySet()) {
			copy.put(e.getKey(), e.getValue().clone());
		}
		return copy;
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

	public void setAdapter(String value) {
		this.adapter = value;
	}
	
	public String getAdapter() {
		return adapter;
	}
	
	
	
}
