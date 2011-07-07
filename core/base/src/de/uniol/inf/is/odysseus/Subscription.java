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
package de.uniol.inf.is.odysseus;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * 
 * @author Marco Grawunder, Jonas Jacobi
 *
 * @param <K>
 */

public class Subscription<K> implements ISubscription<K>, Serializable{

	private static final long serialVersionUID = 5744808958349736195L;
	private K target;
	private int sinkInPort;
	private int sourceOutPort;
	private SDFAttributeList schema;

	public Subscription(K target, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		this.target = target;
		this.sinkInPort = sinkInPort;
		this.sourceOutPort = sourceOutPort;
		this.schema=schema;
	}

	@Override
	public K getTarget() {
		return target;
	}
	
	
	@Override
	public int getSinkInPort() {
		return sinkInPort;
	}
		
	@Override
	public int getSourceOutPort() {
		return sourceOutPort;
	}
	
	public void setSchema(SDFAttributeList inputSchema) {
		this.schema = inputSchema;
	}
	
	@Override
	public SDFAttributeList getSchema() {
		return schema;
	}
	
	@Override
	public String toString() {
		return target+" "+sinkInPort+" "+sourceOutPort;
	}

	// ACHTUNG: BEI DER GENERIERUNG VON HASHCODE UND EQUALS
	// DARF NIEMALS EIN EQUALS AUF DEN TARGETS ERFOLGEN, DA DIE WIEDER IHRE 
	// SUBSCRIPTIONS TESTEN WÜRDEN --> REKURSION --> STACK OVERFLOW!!
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sinkInPort;
		result = prime * result + sourceOutPort;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	@SuppressWarnings({"unchecked","rawtypes"})
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Subscription other = (Subscription) obj;
		if (sinkInPort != other.sinkInPort)
			return false;
		if (sourceOutPort != other.sourceOutPort)
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!(target == other.target)) // ACHTUNG. KEIN EQUALS AUF DER TARGET!!
			return false;
		return true;
	}
	
	
	
	
}
