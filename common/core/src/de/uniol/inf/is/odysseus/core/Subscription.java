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
package de.uniol.inf.is.odysseus.core;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


/**
 * This class represents a link to another element.
 * 
 * @author Marco Grawunder, Jonas Jacobi
 *
 * @param <K>
 */

public class Subscription<I,O> implements ISubscription<I,O>, Serializable{

	private static final long serialVersionUID = 5744808958349736195L;
	/**
	 * The the source
	 */
	private I source;
	
	/**
	 * The sink of the connection
	 */
	private O sink;
	
	/**
	 * If the target has more than one input, this is the input number
	 */
	private int sinkInPort;
	/**
	 * If the source has more that one output, this is the output number
	 */
	private int sourceOutPort;
	/**
	 * The schema of the data that is transported through this link
	 */
	private SDFSchema schema;

	/**
	 * Create a new Subscription
	 * @param target What is the link target (could be a source or a sink!)
	 * @param sinkInPort The input port of the sink that is affected
	 * @param sourceOutPort The output port of the source that is affected
	 * @param schema The data schema of the elements that should be processed
	 */
	public Subscription(I source, O sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		this.source = source;
		this.sink = sink;
		this.sinkInPort = sinkInPort;
		this.sourceOutPort = sourceOutPort;
		setSchema(schema);
	}

	@Override
	public O getSink() {
		return sink;
	}
	
	
	@Override
	public I getSource() {
		return source;
	}
		
	
	@Override
	public int getSinkInPort() {
		return sinkInPort;
	}
		
	@Override
	public int getSourceOutPort() {
		return sourceOutPort;
	}
	
	
	protected void setSinkInPort(int port) {
		this.sinkInPort = port;
	}
	
	/**
	 * Set the schema of the data that is transported by this link
	 * @param inputSchema
	 */
	public void setSchema(SDFSchema inputSchema) {
		this.schema = inputSchema;
	}
	
	@Override
	public SDFSchema getSchema() {
		return schema;
	}
	
	@Override
	public String toString() {
		return "source "+source+" inPort"+sinkInPort+" outPort "+sourceOutPort+" sink "+sink+" Schema "+schema;
	}

	// ACHTUNG: BEI DER GENERIERUNG VON HASHCODE UND EQUALS
	// DARF NIEMALS EIN EQUALS AUF DEN TARGETS ERFOLGEN, DA DIE WIEDER IHRE 
	// SUBSCRIPTIONS TESTEN W�RDEN --> REKURSION --> STACK OVERFLOW!!
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	final public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sinkInPort;
		result = prime * result + sourceOutPort;
		result = prime * result + ((sink == null) ? 0 : sink.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	@SuppressWarnings({"rawtypes"})
	final public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscription other = (Subscription) obj;
		if (sinkInPort != other.sinkInPort)
			return false;
		if (sourceOutPort != other.sourceOutPort)
			return false;
		if (source == null) {
			if (other.source != null) {
				return false;
			}
        // ACHTUNG. KEIN EQUALS AUF DER TARGET!!
		} else if (!(source == other.source)) {
			return false;
		}
			
		if (sink == null) {
			if (other.sink != null) {
				return false;
			}
        // ACHTUNG. KEIN EQUALS AUF DER TARGET!!
		} else if (!(sink == other.sink)) {
			return false;
		}

		return true;
	}
	
	
	
	
}
