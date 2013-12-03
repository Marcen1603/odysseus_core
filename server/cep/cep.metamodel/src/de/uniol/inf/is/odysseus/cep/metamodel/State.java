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
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlID;

/**
 * Objekte dieser Klasse repräsentieren einen Zustand des Automaten.
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
public class State implements Serializable{


	private static final long serialVersionUID = 590839149140344317L;
	/**
	 * ID des Zustands. Darf nicht null sein. Muss innerhalb eines Automaten
	 * eindeutig sein. Darf nur Buchstaben und Ziffern beinhalten, wobei das
	 * erste Zeichen ein Buchstabe sein muss. Darf kein leerer String sein.
	 */
	final private String id;
	final private String var;
	final private String type;
	/**
	 * Gibt an, ob ein Zustand ein Endzustand ist oder nicht.
	 */
	final private boolean accepting;

	/**
	 * If a state is negated (e.g. event should not occur), a special
	 * handling is necessary
	 */
	final private boolean negated;

	/**
	 * Liste aller von einem Zustand ausgehenden Transitionen. Sollte nicht null
	 * sein.
	 */
	final private List<Transition> transitions;

	/**
	 * Erzeugt einen neuen Automatenzustand
	 * 
	 * @param id
	 *            Die ID des Zustands (eindeutig innerhalb des Automaten)
	 * @param var Eine Variable, die diesen Zustand betrifft
	 * @param type der Eventtyp der verarbeitet wird
	 * @param accepting
	 *            true, wenn der Zustand ein Endzustand ist, ansonsten false
	 * @param outgoingTransitions
	 *            Liste der vom Zustand ausgehenden Transitionen. Nicht null.
	 */
	public State(String id, String var, String type, boolean accepting) {
		this.id = id;
		this.accepting = accepting;
		this.transitions = new LinkedList<Transition>();
		this.var = var;
		this.negated = false;
		this.type = null;
	}

	public State(String id, String var, String type, boolean accepting, boolean negated) {
		this.id = id;
		this.accepting = accepting;
		this.var = var;
		this.type = type;
		this.transitions = new LinkedList<Transition>();
		this.negated = negated;
	}

	public State(State otherState, boolean copyTransition){
		this.id = otherState.id;
		this.accepting = otherState.accepting;
		this.var = otherState.var;
		this.type = otherState.type;
		if (copyTransition){
			this.transitions = new LinkedList<Transition>(otherState.transitions);
		}else{
			this.transitions = new LinkedList<Transition>();
		}
		this.negated = otherState.negated;
	}
		

	/**
	 * Gibt die automatenweit eindeutige ID des Zustands zurück.
	 * 
	 * @return Die automatenweit eindeutige ID des Zustands.
	 */
	@XmlID
	public String getId() {
		return id;
	}

	/**
	 * Gibt an, ob es sich bei einem Zustand um einen Endzustand handelt.
	 * 
	 * @return True, wenn der Zustand ein Endzustand ist, sonst False.
	 */
	public boolean isAccepting() {
		return accepting;
	}

	/**
	 * Liefert die Liste mit allen Transitionen, die von dem Zustand ausgehen.
	 * 
	 * @return Liste der von einem Zustand ausgehenden Transitionen.
	 */
	public List<Transition> getTransitions() {
		return transitions;
	}
	
	public Transition getTransition(String id){
		for (Transition t: transitions){
			if (t.getId().equals(id)){
				return t;
			}
		}
		return null;
	}
	
	public void addTransition(Transition t){
		transitions.add(t);
	}
	
	@Override
	public String toString() {
		return id+(accepting?"<F>":"")+" "+(transitions==null?"null":transitions+" ");
	}

	public String prettyPrint() {
		String ret = "State: " +id+(accepting?" <F>":"")+":"+getVar()+"\n";
		for (Transition t: transitions){
			ret +="\t"+t.toString()+"\n";
		}
		return ret;
	}
	
	public boolean hasTarget(State state) {
		for (Transition t: transitions){
			if (t.getNextState().getId() == state.getId()){
				return true;
			}
		}
		return false;
	}

	public String getVar() {
		return var;
	}

	public String getType() {
		return type;
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
		State other = (State) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean isNegated() {
		return negated;
	}
	
}
