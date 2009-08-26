package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;

/**
 * Objekte dieser Klasse repräsentieren einen Zustand des Automaten.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class State {

	/**
	 * ID des Zustands. Darf nicht null sein. Muss innerhalb eines Automaten
	 * eindeutig sein. Darf nur Buchstaben und Ziffern beinhalten, wobei das
	 * erste Zeichen ein Buchstabe sein muss. Darf kein leerer String sein.
	 */
	private String id;
	/**
	 * Gibt an, ob ein Zustand ein Endzustand ist oder nicht.
	 */
	private boolean accepting;
	/**
	 * Liste aller von einem Zustand ausgehenden Transitionen. Sollte nicht null
	 * sein.
	 */
	private LinkedList<Transition> transitions;

	/**
	 * Erzeugt einen neuen Automatenzustand
	 * 
	 * @param id
	 *            Die ID des Zustands (eindeutig innerhalb des Automaten)
	 * @param accepting
	 *            true, wenn der Zustand ein Endzustand ist, ansonsten false
	 * @param outgoingTransitions
	 *            Liste der vom Zustand ausgehenden Transitionen. Nicht null.
	 */
	public State(String id, boolean accepting,
			LinkedList<Transition> outgoingTransitions) {
		this.id = id;
		this.accepting = accepting;
		this.transitions = outgoingTransitions;
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
	public LinkedList<Transition> getTransitions() {
		return transitions;
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
	public void setTransitions(LinkedList<Transition> outgoingTransitions) {
		this.transitions = outgoingTransitions;
	}

	public String toString(String indent) {
		String str = indent + "State: " + this.hashCode();
		indent += "  ";
		str += indent + "id: " + this.id;
		str += indent + "accepting: " + this.accepting;
		for (Transition transition : this.transitions) {
			str += transition.toString(indent);
		}
		return str;
	}
}
