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
package de.uniol.inf.is.odysseus.sparql.parser.helper;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;

/**
 * This class contains information of a datastream clause.
 * It contains the stream name if it is a named stream clause.
 * It contains the access operator that itself contains the schema
 * |sX.subject|sX.predicate|sX.object|.
 * It contains the window operator that is defined in the from
 * part of a sparql query.
 * 
 * WindowAO and AccessAO must not be connected to a plan, because
 * windows can also be specified in a group graph pattern and in this
 * case the window from the group graph pattern has to be used and not
 * the window in the FROM part.
 * 
 * @author André Bolles
 *
 */
public class SourceInfo {

	/**
	 * The name of a stream. Even a default
	 * stream has a name. To distinguish between
	 * default stream and named stream the field
	 * defaultStream is used.
	 */
	private String streamName;
	
	/**
	 * Since both default streams and named streams
	 * have a name, this field is used to distinguish
	 * between both cases.
	 */
	private boolean defaultStream;
	
	/**
	 * The access operator connecting to the stream.
	 */
	private AccessAO sourceOp;
	
	/**
	 * This window operator specified in the FROM part
	 * of a query.
	 */
	private WindowAO windowOp;
	
	public SourceInfo(){
		this.streamName = null;
		this.sourceOp = null;
		this.windowOp = null;
	}
	
	public SourceInfo(String streamName, AccessAO sourceOp, WindowAO windowOp){
		this.streamName = streamName;
		this.sourceOp = sourceOp;
		this.windowOp = windowOp;
	}
	
	public boolean isNamedStream(){
		return !this.defaultStream;
	}
	
	public boolean isDefaultStream(){
		return this.defaultStream;
	}
	
	/**
	 * Sets defaultStream to <defaultStream>
	 * and namedStream to !<defaultStream>
	 * @param defaultStream
	 */
	public void setDefaultStream(boolean defaultStream){
		this.defaultStream = defaultStream;
	}
	
	/**
	 * Sets namedStream to <namedStream>
	 * and defaultStream to !<namedStream>
	 * @param namedStream
	 */
	public void setNamedStream(boolean namedStream){
		this.defaultStream = !namedStream;
	}
	
// ================================= GETTERS AND SETTERS ==============================
	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public AccessAO getSourceOp() {
		return sourceOp;
	}

	public void setSourceOp(AccessAO sourceOp) {
		this.sourceOp = sourceOp;
	}

	public WindowAO getWindowOp() {
		return windowOp;
	}

	public void setWindowOp(WindowAO windowOp) {
		this.windowOp = windowOp;
	}
}
