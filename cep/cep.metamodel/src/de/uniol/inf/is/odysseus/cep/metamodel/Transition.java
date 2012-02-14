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

import java.io.Serializable;

/**
 * Zustandsübergang eines Automaten. Die Zustandsübergänge werden grundsätzlich
 * in dem Zustand referenziert, von dem der Zustandsübergang ausgeht. Der
 * Zielzustand wird immer in der Transition als Attribut gespeichert.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Transition implements Serializable{

	private static final long serialVersionUID = 4496816133940791011L;
	/**
	 * Die im gesamten Automaten eindeutige ID der Transition.
	 */
	final private String id;
	/**
	 * Referenz auf den Folgezustand. Darf während der Verarbeitung durch den
	 * EPA nicht null sein. Muss zudem ein Zustandsobjekt aus der Menge der
	 * Zustandsmenge des Automaten sein.
	 */
	final private State nextState;
	/**
	 * Die Transitionsbedingung
	 */
	final private ICepCondition condition;
	/**
	 * Die Automatenausgabe / Aktion für die Transition
	 */
	final private EAction action;

	/**
	 * Erstellt ein neues Transitionsobjekt
	 * 
	 * @param id
	 *            Die automatenweit eindeutige id der Transition
	 * @param nextState
	 *            Referenz auf den Folgezustand
	 * @param condition
	 *            Die Transitionsbedingung
	 * @param action
	 *            Die Aktion, die für eine erfüllte Transitionsbedingung
	 *            ausgeführt wird.
	 */
	public Transition(String id, State nextState, ICepCondition condition,
			EAction action) {
		this.id = id;
		this.nextState = nextState;
		this.condition = condition;
		this.action = action;
	}

	
	/**
	 * Liefert die ID der Transition, welche im gesamten Automaten eindeutig
	 * ist.
	 * 
	 * @return Die ID der Transition.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Liefert den Folgezustand der Transition.
	 * 
	 * @return Folgezustand der Transition, nicht null. Der gelieferte Zustand
	 *         ist ein Objekt aus der Menge der Zustände des Automaten.
	 */
	public State getNextState() {
		return nextState;
	}

	/**
	 * Liefert die Transitionsbedingung.
	 * 
	 * @return Die Transitionsbedingung.
	 */
	public ICepCondition getCondition() {
		return condition;
	}

	/**
	 * Liefert die Action (Mealy-Ausgabe) der Transition
	 * 
	 * @return Die Action der Transition.
	 */
	public EAction getAction() {
		return action;
	}

	@Override
	public String toString() {
		String str =   "T: " + this.id;
		str += ":="+condition;
		str +=  "-->" + this.nextState.getId();
		str += " [" + this.action+"]";
		return str;
	}
	
	public boolean evaluate(String eventType) {		
		return getCondition().evaluate(eventType);
	}

	public boolean evaluate(int eventTypePort) {		
		return getCondition().evaluate(eventTypePort);
	}

	public void addAssigment(String attributeName, String fullExpression) {
		getCondition().addAssignment(attributeName, fullExpression);		
	}
	
	public void appendAND(String fullExpression) {
		getCondition().appendAND(fullExpression);		
	}
	
	public void appendOR(String fullExpression){
		getCondition().appendOR(fullExpression);		
	}
	
	public void negateExpression() {
		getCondition().negate();
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transition other = (Transition) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	

}
