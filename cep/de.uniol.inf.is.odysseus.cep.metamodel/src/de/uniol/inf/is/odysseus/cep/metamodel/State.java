package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;

/**
 * Objekte dieser Klasse repräsentieren einen Zustand des Automaten.
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
public class State {

	/**
	 * ID des Zustands. Darf nicht null sein. Muss innerhalb eines Automaten
	 * eindeutig sein. Darf nur Buchstaben und Ziffern beinhalten, wobei das
	 * erste Zeichen ein Buchstabe sein muss. Darf kein leerer String sein.
	 */
	private String id;
	private String var;
	private String type;
	/**
	 * Gibt an, ob ein Zustand ein Endzustand ist oder nicht.
	 */
	private boolean accepting;
	/**
	 * Liste aller von einem Zustand ausgehenden Transitionen. Sollte nicht null
	 * sein.
	 */
	private List<Transition> transitions = new LinkedList<Transition>();

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
	public State(String id, String var, String type, boolean accepting,
			List<Transition> outgoingTransitions) {
		setId(id);
		setAccepting(accepting);
		setTransitions(outgoingTransitions);
		setVar(var);
	}

	public State(String id, String var, String type, boolean accepting) {
		setId(id);
		setAccepting(accepting);
		setVar(var);
		setType(type);
	}

	public State() {
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
	 * Setzt die ID des Zustands. Die Zustands-ID muss im gesamten Automaten
	 * eindeutig sein.
	 * 
	 * @param id
	 *            Die neue automatenweit eindeutige Zustands-ID.
	 */
	public void setId(String id) {
		this.id = id;
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
	 * Setzt, ob der Zustand ein Endzustand ist.
	 * 
	 * @param accepting
	 *            True, wenn der Zustand als Endzustand markiert werden soll,
	 *            sonst False.
	 */
	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
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

	/**
	 * Setzt die Liste mit den ausgehenden Transitionen des Zustands.
	 * 
	 * @param outgoingTransitions
	 *            Liste, die alle vom Zustand ausgehenden Transitionen enthält.
	 *            Nicht null.
	 */
	@XmlElementWrapper(name = "transitions") 
	@XmlElement(name = "transition")
	public void setTransitions(List<Transition> outgoingTransitions) {
		this.transitions = outgoingTransitions;
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

	public void setVar(String var) {
		this.var = var;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}


}
