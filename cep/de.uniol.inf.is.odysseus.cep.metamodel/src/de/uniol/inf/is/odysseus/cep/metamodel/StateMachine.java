package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.LinkedList;

import javax.xml.bind.annotation.*;

/**
 * Objekte dieser Klasse kapseln den für das CEP erforderlichen Automaten sowie
 * sämtliche Schemata für Symboltabelle und Event-Aggregation
 * 
 * @author Thomas Vogelgesang
 * 
 */
@XmlRootElement
public class StateMachine {

	/**
	 * Liste aller Zustände des Automaten. Bei der Verarbeitung durch den EPA
	 * darf diese Liste nicht leer sein.
	 */
	private LinkedList<State> states;
	/**
	 * Referenz auf den Startzustand des Automaten. Das referenzierte Objekt
	 * muss bei der Verarbeitung durch den EPA in states enthalten sein.
	 * Darf bei der Verarbeitung durch den EPA nicht null sein.
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
	private ConsumptionMode consumptionMode;

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
	public StateMachine(LinkedList<State> states, State initialState,
			SymbolTableScheme<Object> symTabScheme, OutputScheme outputScheme) {
		this.states = states;
		this.initialState = initialState;
		this.symTabScheme = symTabScheme;
		this.outputScheme = outputScheme;
		this.consumptionMode = ConsumptionMode.allMatches;
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
	public LinkedList<State> getStates() {
		return states;
	}

	/**
	 * Setzt die Liste, die alle Zustände des Automaten enthält.
	 * 
	 * @param states
	 *            Eine nicht leere Liste von Zuständen. Nicht null.
	 */
	public void setStates(LinkedList<State> states) {
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
	public ConsumptionMode getConsumptionMode() {
		return this.consumptionMode;
	}

	/**
	 * Setzt die Matching-Strategie des Automaten.
	 * 
	 * @param strategy
	 *            Die neue Matching-Strategie, nicht null.
	 */
	public void setConsumptionMode(ConsumptionMode strategy) {
		this.consumptionMode = strategy;
	}
	
	public String toString(String indent) {
		String str = "StateMachine: " + this.hashCode();
		indent += "  ";
		for (State state : this.states) {
			str += state.toString(indent);
		}
		str += indent + "Initial State: ";
		str += this.initialState.getId() + "(" + this.initialState.hashCode() + ")";
		str += this.symTabScheme.toString(indent);
		str += this.outputScheme.toString(indent);
		str += indent + "Matching Strategy: " + this.consumptionMode;
		return str;
	}
}
