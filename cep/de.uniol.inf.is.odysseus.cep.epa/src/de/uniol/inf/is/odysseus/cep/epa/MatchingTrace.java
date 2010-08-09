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
			this.stateBuffer.put(states.get(i).getVar(), new ArrayList<MatchedEvent<R>>());
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
		
		List<MatchedEvent<R>> l = stateBuffer.get(state.getVar()); 
		if (l == null){
			l = new ArrayList<MatchedEvent<R>>();
			stateBuffer.put(state.getVar(), l);
		}
		MatchedEvent<R> matchedevent = new MatchedEvent<R>(lastEvent, event, stmt);
		l.add(matchedevent);
		this.lastEvent = matchedevent;
	}
	
	public MatchedEvent<R> getEvent(State state, int pos){
		List<MatchedEvent<R>> l = stateBuffer.get(state.getVar());
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
		List<MatchedEvent<R>> l = stateBuffer.get(currentState.getVar());
		return l.size();
	}
	
	@Override
	public String toString() {
		return stateBuffer.toString();
	}
	
}
