package de.uniol.inf.is.odysseus.cep.epa;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.cep.metamodel.State;

/**
 * Der StateBuffer speichert alle Events, die in einem bestimmten Zustand
 * konsumiert wurden zwischen.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class StateBuffer {

	/**
	 * Zustand des Automaten, auf den sich der StateBuffer bezieht.
	 */
	private State state;
	/**
	 * Liste mit allen in diesem Buffer gematchten Events
	 */
	private LinkedList<MatchedEvent> events;

	/**
	 * Erzeugt einen neuen StateBuffer aus einem Automatenzustand.
	 * 
	 * @param state
	 *            der Zustand des Automaten, auf den sich der StateBuffer
	 *            beziehen soll
	 */
	public StateBuffer(State state) {
		this.state = state;
		this.events = new LinkedList<MatchedEvent>();
	}

	/**
	 * Konstruktor, der nur für die clone()-Methode benötigt wird und daher
	 * nicht weiter benutzt werde sollte.
	 * 
	 * @param state
	 *            Referen auf den Zustand, auf den sich der StateBuffer beziehen
	 *            soll.
	 * @param events Liste von Events, die bisher konsumiert wurden.
	 */
	private StateBuffer(State state, LinkedList<MatchedEvent> events) {
		this.state = state;
		this.events = events;
	}

	/**
	 * Liefert eine Referenz auf den Automatenzustand, auf den sich der
	 * StateBuffer bezieht.
	 * 
	 * @return Der zum StateBuffer zugehörige Automatenzustand.
	 */
	public State getState() {
		return state;
	}

	/**
	 * Liefert die Liste der Events, die im StateBuffer zwischengespeichert
	 * wurden.
	 * 
	 * @return Liste aller in diesem StateBuffer gespeicherten Elemente.
	 */
	public LinkedList<MatchedEvent> getEvents() {
		return events;
	}

	/**
	 * gibt eine tiefe Kopie des StateBuffers zurück.
	 */
	@Override
	public StateBuffer clone() {
		LinkedList<MatchedEvent> newEventList = new LinkedList<MatchedEvent>();
		for (MatchedEvent me : this.events) {
			newEventList.add(me.clone());
		}
		return new StateBuffer(this.state, newEventList);
	}

}
