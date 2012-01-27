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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

//import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTableScheme;

/**
 * Objekte dieser Klasse kapseln den für das CEP erforderlichen Automaten sowie
 * sämtliche Schemata für Symboltabelle und Event-Aggregation
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
@XmlRootElement
public class StateMachine<E> implements Serializable{
	
	private static final long serialVersionUID = 6012448345909786996L;

	long windowSize = -1;

	/**
	 * Liste aller Zustände des Automaten. Bei der Verarbeitung durch den EPA
	 * darf diese Liste nicht leer sein.
	 */
	private List<State> states;
	private Map<String, State> stateMap;
	/**
	 * Referenz auf den Startzustand des Automaten. Das referenzierte Objekt
	 * muss bei der Verarbeitung durch den EPA in states enthalten sein. Darf
	 * bei der Verarbeitung durch den EPA nicht null sein.
	 */
	private String initialStateId;
	/**
	 * Definiert das Schema der Symboltabelle, das für die Ausführung des
	 * Automaten durch den EPA erfroderlich ist. Darf zur Verarbeitungszeit null
	 * sein und muss ein für die Verarbeitung korrektes Schema enthalten.
	 */
	private List<CepVariable> symTabScheme;
	/**
	 * Definiert das Ausgabeschema der CEP-Anfrage. Darf nicht null sein.
	 */
	private OutputScheme outputScheme;

	private EEventSelectionStrategy eventSelectionStrategy;

	public EEventSelectionStrategy getEventSelectionStrategy() {
		return eventSelectionStrategy;
	}

	public void setEventSelectionStrategy(
			EEventSelectionStrategy eventSelectionStrategy) {
		this.eventSelectionStrategy = eventSelectionStrategy;
	}

	/**
	 * Erzeugt einen neuen Automaten aus seinen Bestandteilen. Die
	 * Matching-Startegie ist standardmäßig auf allMatches gesetzt.
	 * 
	 * @param states
	 *            Liste der Zustände
	 * @param initialState
	 *            Startzustand
	 * @param symTabScheme
	 *            Schema der Symboltabelle
	 * @param outputScheme
	 *            AusgabeSchema
	 */
	public StateMachine(List<State> states, String initialStateId, OutputScheme outputScheme, long windowSize) {
		this.states = states;
		this.stateMap = new HashMap<String, State>();
		for (State s:states){
			stateMap.put(s.getId(), s);
		}
		this.initialStateId = initialStateId;
		this.outputScheme = outputScheme;
	}

	/**
	 * leerer Standardkonstruktor
	 */
	public StateMachine() {
		this.states = new LinkedList<State>();
		this.stateMap = new HashMap<String, State>();
		this.outputScheme = new OutputScheme();
	}

	/**
	 * Liefert die Liste aller Zustände des Automaten.
	 * 
	 * @return Liste aller Zustände des Automaten.
	 */
	@XmlElementWrapper(name = "states")
	@XmlElement(name = "state")
	public List<State> getStates() {
		return states;
	}

	/**
	 * Setzt die Liste, die alle Zustände des Automaten enthält.
	 * 
	 * @param states
	 *            Eine nicht leere Liste von Zuständen. Nicht null.
	 */
	public void setStates(List<State> states) {
		this.states = states;
		this.stateMap = new HashMap<String, State>();
		for (State s:states){
			stateMap.put(s.getId(), s);
		}
	}

	/**
	 * Gibt den Startzustand des Automaten zurück.
	 * 
	 * @return Der Startzustand des Automaten.
	 */
	public State getInitialState() {
		return getState(initialStateId);
	}

	public State getState(String id) {
		return stateMap.get(id);
	}
	

	/**
	 * Setzt den Startzustand des Automaten.
	 * 
	 * @param initialState
	 *            Der neue Startzustand des Automaten, nicht null.
	 */
	public void setInitialState(String initialStateId) {
		this.initialStateId = initialStateId;
	}

	/**
	 * Liefert das Symboltabellen-Schema des Automaten.
	 * 
	 * @return Das Symboltabellen-Schema des Automaten.
	 */
	public List<CepVariable> getSymTabScheme(boolean recalc) {
		if (symTabScheme == null || recalc){
			initSymTabScheme();
		}
		return symTabScheme;
	}
	
	private void initSymTabScheme(){
		symTabScheme = new ArrayList<CepVariable>();
		//System.out.println("INIT SYM TAB SCHEME");
		for (State s: states){
			//System.out.println("State "+s.getId());
			for (Transition t: s.getTransitions()){
				//System.out.println("Transition "+t.getId());
				Set<CepVariable> varnames = t.getCondition().getVarNames();
				for (CepVariable var: varnames){
			//		System.out.println("Variable "+v);
//					if (var.getStateIdentifier() == null){
//						var.setStateIdentifier(s.getId());
//					}
//					if (var.getOperation() == null){
//						var.setOperation(SymbolTableOperationFactory.getOperation("Write"));
//					}
					
					if (var.getOperation() != null){
					//	System.out.println("add "+var);
						symTabScheme.add(var);
					}
				}
			}
		}
	}

	/**
	 * Liefert das Ausgabeschema für die zu erzeugenden komplexen Events.
	 * 
	 * @return Das Ausgabeschema für komplexe Events.
	 */
	public OutputScheme getOutputScheme() {
		return outputScheme;
	}

	/**
	 * Setzt das Ausgabeschema für die komplexen Events.
	 * 
	 * @param outputScheme
	 *            Das neue Ausgabeschema, nicht null.
	 */
	public void setOutputScheme(OutputScheme outputScheme) {
		this.outputScheme = outputScheme;
	}

//	/**
//	 * Liefert die Matching-Strategie, die verfolgt werden soll.
//	 * 
//	 * @return Die Matching-Strategie des Automaten.
//	 */
//	public EConsumptionMode getConsumptionMode() {
//		return this.consumptionMode;
//	}
//
//	/**
//	 * Setzt die Matching-Strategie des Automaten.
//	 * 
//	 * @param strategy
//	 *            Die neue Matching-Strategie, nicht null.
//	 */
//	public void setConsumptionMode(EConsumptionMode strategy) {
//		this.consumptionMode = strategy;
//	}


	public void setWindowSize(long windowSize) {
		this.windowSize = windowSize;
	}
	
	public long getWindowSize() {
		return windowSize;
	}
	
	@Override
	public String toString() {
		String str = "StateMachine: " + this.hashCode() + " ";
		str += states;
		str += " Initial State: "+initialStateId;
		str += this.symTabScheme;

		str += this.outputScheme;
//		str += "Matching Strategy: " + this.consumptionMode;
		return str;
	}

	public String prettyPrint() {
		String str = "StateMachine:(" + this.hashCode() + ")\n";
		for (State s : states) {
			if (s.getId().equals(initialStateId)) {
				str += "Initial-";
			}
			str += s.prettyPrint();
		}
		str += "\n";
		str += this.symTabScheme + "\n";
		str += this.outputScheme + "\n";
//		str += "Matching Strategy: " + this.consumptionMode + "\n";
		return str;
	}

	public State getLastState() {
		return states.get(states.size());
	}

	public List<State> getFinalStates() {
		List<State> fStates = new ArrayList<State>();
		for (State s : states) {
			if (s.isAccepting()) {
				fStates.add(s);
			}
		}
		return fStates;
	}

	
	/**
	 * Returns list of other (!) States that have outgoing transitions to state
	 * 
	 * @param state
	 * @return
	 */
	public List<State> getSourceStates(State state) {
		List<State> sStates = new ArrayList<State>();
		for (State so : states) {
			if (so.hasTarget(state) && so.getId() != state.getId()) {
				sStates.add(so);
			}
		}
		return sStates;
	}

	public List<State> getAllPathesToStart(State finalState) {
		List<State> states = new ArrayList<State>();
		states.add(finalState);
		getAllStatesBefore(finalState, states);
		return states;
	}

	private void getAllStatesBefore(State state, List<State> states) {
		if (!initialStateId.equals(state.getId())) {
			List<State> s = getSourceStates(state);
			// Endlosschleife vermeiden (alle Elemente aus s entfernen, die
			// bereits verarbeitet wurden
			s.removeAll(states);
			states.addAll(s);
			for (State st : s) {
				getAllStatesBefore(st, states);

			}
		}
	}
	
	@Override
	public StateMachine<E> clone()  {
		// Eine StateMachine zu clonen macht keinen Sinn! Probleme sind die Transitionen die
		// wieder States haben und dann nicht zu den hier geclonten passen w�rden.
		throw new RuntimeException("Clone Not implemented yet"); 
	}

}
