package de.uniol.inf.is.odysseus.cep.epa;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.cep.metamodel.State;

/**
 * Der MatchingTrace ist eine Verwaltungsstruktur, in dem die konsumierten
 * Events einer Automateninstanz zwischengespeichert werden.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class MatchingTrace {

	/**
	 * Liste mit allen StateBuffern einer Instanz
	 */
	private LinkedList<StateBuffer> stateBuffer;
	/**
	 * Referenz auf das zuletzt konsumierte Event
	 */
	private MatchedEvent lastEvent;

	/**
	 * Erzeugt einen neuen MatchingTrace.
	 * 
	 * @param states
	 *            Liste mit allen Zuständen des Automaten
	 */
	public MatchingTrace(LinkedList<State> states) {
		this.lastEvent = null;
		this.stateBuffer = new LinkedList<StateBuffer>();
		for (int i = 0; i < states.size(); i++) {
			this.stateBuffer.add(new StateBuffer(states.get(i)));
		}
	}

	/**
	 * Konstruktor, der für die clone()-Methode benötigt wird, aber ansonsten
	 * nicht verwendet werden sollte
	 * 
	 * @param stateBuffer
	 * @param lastEvent
	 */
	private MatchingTrace(LinkedList<StateBuffer> stateBuffer,
			MatchedEvent lastEvent) {
		this.stateBuffer = stateBuffer;
		this.lastEvent = lastEvent;
	}

	/**
	 * 
	 * @return Das zuletzt konsumierte Event.
	 */
	public MatchedEvent getLastEvent() {
		return lastEvent;
	}

	/**
	 * Setzt das zuletzt konsumierte Event.
	 * 
	 * @param lastEvent
	 *            Das zuletzt konsumierte Event.
	 */
	public void setLastEvent(MatchedEvent lastEvent) {
		this.lastEvent = lastEvent;
	}

	/**
	 * 
	 * @return Liste mit allen StateBuffern.
	 */
	public LinkedList<StateBuffer> getStateBuffer() {
		return stateBuffer;
	}

	/**
	 * Liefert den zu einem Automatenzustand gehörenden Event-Speicher.
	 * 
	 * @param state
	 *            Zustand des Automaten, zu dem der Event-Speicher gesucht wird.
	 * @return Der gesuchte StateBuffer oder null, falls kein zum Zustand
	 *         gehöriger StateBuffer gefunden werden konnte.
	 */
	public StateBuffer getStateBuffer(State state) {
		for (int i = 0; i < this.stateBuffer.size(); i++) {
			if (state == this.stateBuffer.get(i).getState()) {
				return this.stateBuffer.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Liefert den zu einem Zustandsnamen gehörenden Event-Speicher.
	 * 
	 * @param stateName
	 *            Zustandsname, zu dem der Event-Speicher gesucht wird.
	 * @return Der gesuchte StateBuffer oder null, falls kein zum Namen
	 *         gehöriger StateBuffer gefunden werden konnte.
	 */
	public StateBuffer getStateBuffer(String stateName) {
		for (StateBuffer buffer : this.stateBuffer) {
			if (buffer.getState().getId().equals(stateName))
				return buffer;
		}
		return null;
	}

	/**
	 * Fügt ein konsumiertes Event in den MatchingTrace ein.
	 * 
	 * @param event
	 *            Das konsumierte Event. Nicht null.
	 * @param state
	 *            Der Zustand, in den der Automat beim konsumieren des Elements
	 *            übergegangen ist. Muss in der Liste der Zustände enthalten
	 *            sein. Nicht null.
	 */
	public void addEvent(Object event, State state) {
		MatchedEvent matchedEvent = new MatchedEvent(this.lastEvent, event);
		this.lastEvent = matchedEvent;
		StateBuffer stateBuffer = this.getStateBuffer(state); 
		if (stateBuffer != null) 
			stateBuffer.getEvents().add(matchedEvent);
	}

	/**
	 * Gibte eine tiefe Kopie des Matchingtrace zurück
	 */
	@Override
	public MatchingTrace clone() {
		LinkedList<StateBuffer> stateBuffer = new LinkedList<StateBuffer>();
		for (StateBuffer tmpBuffer : this.stateBuffer) {
			stateBuffer.add(tmpBuffer.clone());
		}
		return new MatchingTrace(stateBuffer, (this.lastEvent==null) ? null : this.lastEvent.clone());
	}
	
}
