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
package de.uniol.inf.is.odysseus.cep.metamodel;

/**
 * Diese Klasse kapselt die Transitionsbedingung.
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
abstract public class AbstractCondition implements ICepCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1739660261038759580L;
	/**
	 * Textuelle Repraesentation der Transitionsbedingung.
	 */
	private String label;
	private String eventType;
	private int eventPort;
	private boolean eventTypeChecking = true;

	
	public AbstractCondition() {
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Liefert die textuelle Repraesentation der Transitionsbedingung.
	 * 
	 * @return Transitionsbedingung als String.
	 */
	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getEventType() {
		return eventType;
	}

	@Override
	public void setEventType(String type) {
		eventType = type;
	}

	public int getEventPort() {
		return eventPort;
	}

	@Override
	public void setEventPort(int eventPort) {
		this.eventPort = eventPort;
	}

	@Override
	public boolean checkEventType(String eventType) {
		if (doEventTypeChecking()){
			return eventType.equals(this.eventType);
		}
		
		return true;
	}
	
	@Override
	public void setEventTypeChecking(boolean eventTypeChecking) {
		this.eventTypeChecking = eventTypeChecking;
	}

	@Override
	public boolean doEventTypeChecking() {
		return eventTypeChecking;
	}

	@Override
	public boolean checkEventTypeWithPort(int port) {
		if (doEventTypeChecking()){
			return port == eventPort;
		}
		
		return true;
	}


	@Override
	abstract public boolean isNegate();

	@Override
	public String toString() {
		return this.getLabel();
	}

}
