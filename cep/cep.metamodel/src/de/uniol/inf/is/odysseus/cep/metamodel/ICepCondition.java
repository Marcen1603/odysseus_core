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
package de.uniol.inf.is.odysseus.cep.metamodel;


public interface ICepCondition extends ICepExpression<Boolean>{

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

	public void setLabel(String label);
	public String getLabel();
	public String toString(String indent);
	public boolean evaluate(String eventType);
	public boolean evaluate(int eventTypePort);
	public void appendAND(String fullExpression);
	public void appendOR(String fullExpression);
	public void addAssignment(String attribute, String fullexpression);
	public void negate();
	public boolean isNegate();

	public boolean doEventTypeChecking();
	public void setEventTypeChecking(boolean eventTypeChecking);
	/**
	 * Set Type of Events that can be processed by this Transition (only one)
	 * @param type
	 * @return
	 */
	public void setEventType(String type);
	public String getEventType();
	
	/**
	 * Set InputPort of Events that can be processed by this Transition 
	 * @param port
	 * @return
	 */
	void setEventPort(int eventPort);
	
	/**
	 * 
	 * @param start
	 * @param current
	 * @param windowsize
	 * @return start + windowsize < current;
	 */
	public boolean checkTime(long start, long current, long windowsize);
	boolean checkEventType(String eventType);
	boolean checkEventTypeWithPort(int port);	

}
