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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "SOCKETSINK", minInputPorts = 1, maxInputPorts = 1)
public class SocketSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 4250341797170265988L;

	private Integer sinkPort;
	private String sinkType;
	private String dataHandler;

	private Boolean loginNeeded = false;
	private String sinkName;
	
	private String host = "localhost";	

	private boolean connectToServer = false;
	
	public SocketSinkAO(int sinkPort, String sinkType, boolean loginNeeded, String name){
		this.sinkPort = sinkPort;
		this.sinkType = sinkType;
		this.loginNeeded = loginNeeded;
		this.sinkName = name;		
	}
	
	public SocketSinkAO(){
	}
	
	public SocketSinkAO(SocketSinkAO socketSinkAO) {
		super(socketSinkAO);
		this.sinkPort = socketSinkAO.sinkPort;
		this.sinkType = socketSinkAO.sinkType;
		this.loginNeeded = socketSinkAO.loginNeeded;
		this.sinkName = socketSinkAO.sinkName;
		this.host = socketSinkAO.host;
		this.connectToServer = socketSinkAO.connectToServer;
	}

	@Parameter(name="SINKPORT",type = IntegerParameter.class, optional = false)
	public void setSinkPort(int sinkPort){
		this.sinkPort = sinkPort;
	}
	
	public int getSinkPort() {
		return sinkPort;
	}
	
	public String getSinkType(){
		return sinkType;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SocketSinkAO(this);
	}

	public boolean isLoginNeeded() {
		return loginNeeded;
	}
	
	public String getSinkName() {
		return sinkName;
	}
	
	@Parameter(name = "SINKNAME", type = StringParameter.class, optional = true)
	public void setSinkName(String sinkName){
		this.sinkName = sinkName;
	}

	@Parameter(name = "SINKTYPE", type = StringParameter.class, optional = false)
	public void setSinkType(String sinkType) {
		this.sinkType = sinkType;
	}
	
	@Parameter(name="LOGINNEEDED", type = BooleanParameter.class, optional = true)
	public void setLoginNeeded(boolean loginNeeded) {
		this.loginNeeded = loginNeeded;
	}
	
	public String getDataHandler() {
		return dataHandler;
	}
	
	public String getHost(){
		return this.host;
	}
	
	public boolean getConnectToServer(){
		return this.connectToServer;
	}
	
	
	@Parameter(name="HOST", type = StringParameter.class, optional = true)
	public void setHost(String host) {
		this.host = host;
	}
	
	@Parameter(name="CONNECTTOSERVER", type = BooleanParameter.class, optional = true)
	public void setConnectToServer(boolean connectToServer){
		this.connectToServer = connectToServer;
	}
		
	
	@Parameter(type = StringParameter.class, optional=true)
	public void setDataHandler(String dataHandler) {
		this.dataHandler = dataHandler;
	}
}
