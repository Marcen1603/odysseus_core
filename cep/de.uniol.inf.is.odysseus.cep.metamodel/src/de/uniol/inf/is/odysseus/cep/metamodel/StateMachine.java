package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import javax.xml.bind.annotation.*;

import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTableScheme;

/**
 * Objekte dieser Klasse kapseln den für das CEP erforderlichen Automaten sowie
 * sämtliche Schemata für Symboltabelle und Event-Aggregation
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
@XmlRootElement
public class StateMachine {

	/**
	 * Liste aller Zustände des Automaten. Bei der Verarbeitung durch den EPA
	 * darf diese Liste nicht leer sein.
	 */
	private List<State> states;
	/**
	 * Referenz auf den Startzustand des Automaten. Das referenzierte Objekt
	 * muss bei der Verarbeitung durch den EPA in states enthalten sein. Darf
	 * bei der Verarbeitung durch den EPA nicht null sein.
	 */
	private State initialState;
	/**
	 * Definiert das Schema der Symboltabelle, das für die Ausführung des
	 * Automaten durch den EPA erfroderlich ist. Darf zur Verarbeitungszeit null
	 * sein und muss ein für die Verarbeitung korrektes Schema enthalten.
	 */
	private SymbolTableScheme<Object> symTabScheme;
	/**
	 * Definiert das Ausgabeschema der CEP-Anfrage. Darf nicht null sein.
	 */
	private OutputScheme outputScheme;
	/**
	 * Strategie, die angibt, ob bei nichtdeterministischen Verzweigungen nur
	 * die abgeschlossene Automateninstanz oder alle damit verbundenen
	 * Automateninstanzen aus dem Instanzspeicher entfernt werden.
	 */
	private EConsumptionMode consumptionMode;

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
	public StateMachine(List<State> states, State initialState,
			SymbolTableScheme<Object> symTabScheme, OutputScheme outputScheme) {
		this.states = states;
		this.initialState = initialState;
		this.symTabScheme = symTabScheme;
		this.outputScheme = outputScheme;
		this.consumptionMode = EConsumptionMode.allMatches;
	}

	/**
	 * leerer Standardkonstruktor
	 */
	public StateMachine() {
		this.states = new LinkedList<State>();
		this.symTabScheme = new SymbolTableScheme<Object>();
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
	}

	/**
	 * Gibt den Startzustand des Automaten zurück.
	 * 
	 * @return Der Startzustand des Automaten.
	 */
	@XmlIDREF
	public State getInitialState() {
		return initialState;
	}

	public State getState(String id) {
		for (State s : states) {
			if (s.getId().equals(id))
				return s;
		}
		return null;
	}

	/**
	 * Setzt den Startzustand des Automaten.
	 * 
	 * @param initialState
	 *            Der neue Startzustand des Automaten, nicht null.
	 */
	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	/**
	 * Liefert das Symboltabellen-Schema des Automaten.
	 * 
	 * @return Das Symboltabellen-Schema des Automaten.
	 */
	public SymbolTableScheme<Object> getSymTabScheme() {
		return symTabScheme;
	}

	/**
	 * Setzt eine neues Symboltabellen-Schema für den Automaten.
	 * 
	 * @param symTabScheme
	 *            Das neue Symboltabellen-Schema des Automaten, nicht null.
	 */
	public void setSymTabScheme(SymbolTableScheme<Object> symTabScheme) {
		this.symTabScheme = symTabScheme;
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

	/**
	 * Liefert die Matching-Strategie, die verfolgt werden soll.
	 * 
	 * @return Die Matching-Strategie des Automaten.
	 */
	public EConsumptionMode getConsumptionMode() {
		return this.consumptionMode;
	}

	/**
	 * Setzt die Matching-Strategie des Automaten.
	 * 
	 * @param strategy
	 *            Die neue Matching-Strategie, nicht null.
	 */
	public void setConsumptionMode(EConsumptionMode strategy) {
		this.consumptionMode = strategy;
	}

	public String toString() {
		String str = "StateMachine: " + this.hashCode() + " ";
		str += states;
		str += " Initial State: ";
		if (initialState != null) {
			str += this.initialState.getId() + "("
					+ this.initialState.hashCode() + ")";
		}
		str += this.symTabScheme;

		str += this.outputScheme;
		str += "Matching Strategy: " + this.consumptionMode;
		return str;
	}

	public String prettyPrint() {
		String str = "StateMachine:(" + this.hashCode() + ")\n";
		for (State s : states) {
			if (initialState != null && s.getId() == initialState.getId()) {
				str += "Initial-";
			}
			str += s.prettyPrint();
		}
		str += "\n";
		str += this.symTabScheme + "\n";
		str += this.outputScheme + "\n";
		str += "Matching Strategy: " + this.consumptionMode + "\n";
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
		if (initialState.getId() != state.getId()) {
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

}
