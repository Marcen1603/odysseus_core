package de.uniol.inf.is.odysseus.cep.epa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.cep.metamodel.State;

/**
 * Der MatchingTrace ist eine Verwaltungsstruktur, in dem die konsumierten
 * Events einer Automateninstanz zwischengespeichert werden.
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
public class MatchingTrace<R> {

	private Map<String, List<MatchedEvent<R>>> stateBuffer = new HashMap<String, List<MatchedEvent<R>>>();
	/**
	 * Referenz auf das zuletzt konsumierte Event
	 */
	private MatchedEvent<R> lastEvent;

	/**
	 * Erzeugt einen neuen MatchingTrace.
	 * 
	 * @param states
	 *            Liste mit allen Zuständen des Automaten
	 */
	public MatchingTrace(List<State> states) {
		this.lastEvent = null;
		for (int i = 0; i < states.size(); i++) {
			this.stateBuffer.put(states.get(i).getId(), new ArrayList<MatchedEvent<R>>());
		}
	}

	public MatchingTrace(MatchingTrace<R> matchingTrace) {
		this.stateBuffer = new HashMap<String, List<MatchedEvent<R>>>();
		for (String s:matchingTrace.stateBuffer.keySet()){
			this.stateBuffer.put(s, new ArrayList<MatchedEvent<R>>(matchingTrace.stateBuffer.get(s)));
		}
		this.lastEvent = matchingTrace.lastEvent;
	}

	/**
	 * 
	 * @return Das zuletzt konsumierte Event.
	 */
	public MatchedEvent<R> getLastEvent() {
		return lastEvent;
	}

	/**
	 * Setzt das zuletzt konsumierte Event.
	 * 
	 * @param lastEvent
	 *            Das zuletzt konsumierte Event.
	 */
//	public void setLastEvent(MatchedEvent<R> lastEvent) {
//		this.lastEvent = lastEvent;
//	}

	/**
	 * 
	 * @return Liste mit allen StateBuffern.
	 */
//	public List<StateBuffer<R>> getStateBuffer() {
//		return stateBuffer;
//	}

	/**
	 * Liefert den zu einem Automatenzustand gehörenden Event-Speicher.
	 * 
	 * @param state
	 *            Zustand des Automaten, zu dem der Event-Speicher gesucht wird.
	 * @return Der gesuchte StateBuffer oder null, falls kein zum Zustand
	 *         gehöriger StateBuffer gefunden werden konnte.
	 */
//	public StateBuffer<R> getStateBuffer(State state) {
//		for (int i = 0; i < this.stateBuffer.size(); i++) {
//			if (state == this.stateBuffer.get(i).getState()) {
//				return this.stateBuffer.get(i);
//			}
//		}
//		return null;
//	}
//	
	/**
	 * Liefert den zu einem Zustandsnamen gehörenden Event-Speicher.
	 * 
	 * @param stateName
	 *            Zustandsname, zu dem der Event-Speicher gesucht wird.
	 * @return Der gesuchte StateBuffer oder null, falls kein zum Namen
	 *         gehöriger StateBuffer gefunden werden konnte.
	 */
//	public StateBuffer<R> getStateBuffer(String stateName) {
//		for (StateBuffer<R> buffer : this.stateBuffer) {
//			if (buffer.getState().getId().equals(stateName))
//				return buffer;
//		}
//		return null;
//	}

	/**
	 * F�gt ein konsumiertes Event in den MatchingTrace ein.
	 * 
	 * @param event
	 *            Das konsumierte Event. Nicht null.
	 * @param state
	 *            Der Zustand, in den der Automat beim konsumieren des Elements
	 *            übergegangen ist. Muss in der Liste der Zustände enthalten
	 *            sein. Nicht null.
	 */
	public void addEvent(R event, State state, StateMachineInstance<R> stmt) {
		
		List<MatchedEvent<R>> l = stateBuffer.get(state.getId()); 
		if (l == null){
			l = new ArrayList<MatchedEvent<R>>();
			stateBuffer.put(state.getId(), l);
		}
		MatchedEvent<R> matchedevent = new MatchedEvent<R>(lastEvent, event, stmt);
		l.add(matchedevent);
		this.lastEvent = matchedevent;
	}
	
	public MatchedEvent<R> getEvent(String stateId, int pos){
		List<MatchedEvent<R>> l = stateBuffer.get(stateId);
		if (l!=null && l.size()>0){
			if (pos > 0 ){ // && pos < l.size()
				return l.get(pos);
			}else{
				return l.get(l.size()-1);
			}
		}
		return null;
		
	}
	
	

	/**
	 * Gibte eine tiefe Kopie des Matchingtrace zurück
	 */
	@Override
	public MatchingTrace<R> clone() {
		return new MatchingTrace<R>(this);
	}

	public int getStateBufferSize(State currentState) {
		List<MatchedEvent<R>> l = stateBuffer.get(currentState.getId());
		return l.size();
	}
	
	@Override
	public String toString() {
		return stateBuffer.toString();
	}
	
}
