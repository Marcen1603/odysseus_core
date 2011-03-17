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
package de.uniol.inf.is.odysseus.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class AccessAO extends AbstractLogicalOperator implements
		OutputSchemaSettable {

	private static final long serialVersionUID = -5423444612698319659L;
	/**
	 * Die Uri der von diesem AccessPO gekapselten Quelle
	 */
	protected SDFSource source = null;
	private Map<Integer, SDFAttributeList> outputSchema = new HashMap<Integer, SDFAttributeList>();

	private int port;
	private String host;

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
		this.source = po.source;
		this.port = po.port;
		this.host = po.host;
		this.outputSchema = createCleanClone(po.outputSchema);
	}

	public AccessAO(SDFSource source) {
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public synchronized SDFSource getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public synchronized void setSource(SDFSource source) {
		this.source = source;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getOutputSchema(0);
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		setOutputSchema(outputSchema, 0);
	}

	@Override
	public SDFAttributeList getOutputSchema(int port) {
		return outputSchema.get(port);
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema, int port) {
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

	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = super.hashCode();
	// result = prime * result + ((host == null) ? 0 : host.hashCode());
	// result = prime * result + port;
	// result = prime * result + ((source == null) ? 0 : source.hashCode());
	// return result;
	// }

	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (!super.equals(obj))
	// return false;
	// if (getClass() != obj.getClass())
	// return false;
	// AccessAO other = (AccessAO) obj;
	// if (host == null) {
	// if (other.host != null)
	// return false;
	// } else if (!host.equals(other.host))
	// return false;
	// if (port != other.port)
	// return false;
	// if (source == null) {
	// if (other.source != null)
	// return false;
	// } else if (!source.equals(other.source))
	// return false;
	// return true;
	// }

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

	private Map<Integer, SDFAttributeList> createCleanClone(
			Map<Integer, SDFAttributeList> old) {
		Map<Integer, SDFAttributeList> copy = new HashMap<Integer, SDFAttributeList>();
		for (Entry<Integer, SDFAttributeList> e : old.entrySet()) {
			copy.put(e.getKey(), e.getValue().clone());
		}
		return copy;
	}

}
