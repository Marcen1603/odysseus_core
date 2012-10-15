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
package de.uniol.inf.is.odysseus.broker.transaction;


/**
 * This class represents a mapping of an association
 * selection operator to a port of the broker. An
 * assocation selection operator always reads a broker.
 * 
 * @author Andre Bolles
 *
 */
public class BrokerAssociationMapping {

	private String associationName;
	private String brokerName;
	private int outPort;
	
	public BrokerAssociationMapping(){
	}
	
	public BrokerAssociationMapping(String associationName, String brokerName, int outPort){
		this.associationName = associationName;
		this.brokerName = brokerName;
		this.outPort = outPort;
	}

	public String getAssociationName() {
		return associationName;
	}

	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public int getOutPort() {
		return outPort;
	}

	public void setOutPort(int outPort) {
		this.outPort = outPort;
	}
	
}
