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

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public interface ICepCondition extends ICepExpression<Boolean>,Serializable{

	/**
	 * Setzt die Transitionsbedingung.
	 * 
	 * @param label
	 *            Die neue Transitionsbedingung als String. Darf nur (nach dem
	 *            internen Namensschema) gültige Variablennamen beinhalten.
	 *         	  Wird null
	 *            übergeben, so wird der Ausdruck auf 1 (true) gesetzt. Die
	 *            Transition wird somit zu einem Epsilon-Übergang.
	 */

	void setLabel(String label);
	String getLabel();
	void updateCondition(List<SDFSchema> schema);	
	String toString(String indent);
	boolean evaluate(String eventType);
	boolean evaluate(int eventTypePort);
	void appendAND(String fullExpression);
	void appendOR(String fullExpression);
	void addAssignment(String attribute, String fullexpression);
	void negate();
	boolean isNegate();

	boolean doEventTypeChecking();
	void setEventTypeChecking(boolean eventTypeChecking);
	/**
	 * Set Type of Events that can be processed by this Transition (only one)
	 * @param type
	 * @return
	 */
	void setEventType(String type);
	String getEventType();
	
	/**
	 * Set InputPort of Events that can be processed by this Transition 
	 * @param port
	 * @return
	 */
	void setEventPort(int eventPort);
	
	boolean checkEventType(String eventType);
	boolean checkEventTypeWithPort(int port);

}
