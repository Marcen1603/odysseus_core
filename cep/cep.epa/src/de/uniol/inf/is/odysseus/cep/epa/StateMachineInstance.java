package de.uniol.inf.is.odysseus.cep.epa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.cep.epa.eventreading.IEventReader;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.UndefinedActionException;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTable;

/**
 * Die StateMachineInstance repräsentiert eine Instanz des an den EPA
 * übergebenen Automaten, sodass das eigentliche Automaten-Objekt vom Typ
 * {@link StateMachine} zur Laufzeit nur einmal vorgehalten werden muss.
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
public class StateMachineInstance<R> {

	Logger logger = LoggerFactory.getLogger(StateMachineInstance.class);

	static int _instanceCounter = 0;
	private int instance = 0;
	private int derivedFrom = -1;

	/**
	 * Referenz auf den aktuellen Zustand der Automateninstanz
	 */
	private State currentState;
	/**
	 * Referenz auf die Symboltabelle der Automateninstanz
	 */
	final private SymbolTable<R> symTab;
	/**
	 * Referenz auf den Puffer für bereits konsumierte Events
	 */
	final private MatchingTrace<R> matchingTrace;
	final private long startTimestamp;
	
	final private StateMachine<R> stateMachine;

	/**
	 * erzeugt eine neue Automateninstanz
	 * 
	 * @param stateMachine
	 *            Referenz auf den Automaten, von dem eine neue Instanz erstellt
	 *            werden soll
	 */
	public StateMachineInstance(StateMachine<R> stateMachine,
			long startTimestamp) {
		this.currentState = stateMachine.getInitialState();
		this.matchingTrace = new MatchingTrace<R>(stateMachine.getStates());
		this.symTab = new SymbolTable<R>(stateMachine.getSymTabScheme(true));
		this.startTimestamp = startTimestamp;
		instance = _instanceCounter++;
		this.stateMachine = stateMachine;
		// logger.debug("Created new initial StateMachineInstance "+instance);
	}

	public StateMachineInstance(StateMachineInstance<R> stateMachineInstance) {
		this.currentState = stateMachineInstance.currentState; // Hier reicht
																// Referenz
		this.symTab = stateMachineInstance.symTab.clone();
		this.matchingTrace = stateMachineInstance.matchingTrace.clone();
		this.startTimestamp = stateMachineInstance.startTimestamp;
		instance = _instanceCounter++;
		derivedFrom = stateMachineInstance.instance;
		this.stateMachine = stateMachineInstance.stateMachine;
		// logger.debug("Created new StateMachineInstance " + toString());
	}

	/**
	 * Liefert den aktuellen Zustand einer Automateninstanz.
	 * 
	 * @return Der aktuelle Automatenzustand.
	 */
	public State getCurrentState() {
		return currentState;
	}

	/**
	 * Setzt den aktuellen Zustand der Automateninstanz.
	 * 
	 * @param currentState
	 *            Der neue aktuelle Zustand der Automateninstanz, nicht null.
	 */
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	/**
	 * Liefert die Symboltabelle der Automateninstanz.
	 * 
	 * @return Die Symboltabelle der Automateninstanz.
	 */
	public SymbolTable<R> getSymTab() {
		return symTab;
	}

	/**
	 * Liefert den Puffer mit allen von der Automateninstanz konsumierten Events
	 * zurück.
	 * 
	 * @return Puffer mit allen von der AUtomateninstanz konsumierten Events
	 */
	public MatchingTrace<R> getMatchingTrace() {
		return matchingTrace;
	}

	/**
	 * Wechselt den Zustand einer Automateninstanz
	 * 
	 * @param instance
	 *            Automateninstanz, die den Zustand wechseln soll.
	 * @param transition
	 *            Die Transition, die genommen werden soll.
	 * @param event
	 *            Referenz auf das sich aktuell in der Verarbeitung befindliche
	 *            Event.
	 * @param port
	 */
	public void takeTransition(Transition transition, R event,
			IEventReader<R, R> eventReader) {
//		if (logger.isDebugEnabled()) {
//			logger.debug(instance + " Fire: " + transition.getId() + " "
//					+ "Execute action: " + transition.getAction());
//		}

		// TODO : Reihenfolge getauscht ... siehe unten ...
		executeAction(transition.getAction(), event, eventReader);

		setCurrentState(transition.getNextState());

//		if (logger.isDebugEnabled()) {
//			logger.debug("Updated Instance "+instance);
//		}
		
		// if (logger.isDebugEnabled()) {
		// logger.debug(instance + " --> " + instance.getStats());
		// }
		/*
		 * Die Reihenfolge der Methodenaufrufe legt fest, in welchem StateBuffer
		 * das Event gespeichert wird. Wird der Zustand zuerst gewechselt, wird
		 * das Event dem Zielzustand der Transition zugeordnet, ansonsten dem
		 * Ausgangszustand.
		 * 
		 * Die CEP-Komponente erfordert zur Zeit, dass erst der Zustand
		 * gewechselt wird und anschließend die Aktion ausgeführt wird. Die
		 * umgekehrte Reihenfolge würde zu Problemen mit der internen
		 * Namenskonvention für Events führen. --> MG: Ist das so? Warum?
		 */
	}

	/**
	 * Führt eine Aktion des Automaten aus und aktualisiert die Symboltabelle.
	 * 
	 * @param action
	 *            Die Action die ausgeführt werden soll.
	 * @param event
	 *            Das Event, das gerade verarbeitet wird.
	 * @throws UndefinedActionException
	 *             Wenn die auszuführende Aktion nicht definiert ist.
	 */
	public void executeAction(EAction action, R event,
			IEventReader<R, R> eventReader) throws UndefinedActionException {
		if (action == EAction.consumeBufferWrite) {
			// logger.debug(this+" add Event to matching trace " + event +
			// " "
			// + currentState);
			this.matchingTrace.addEvent(event, this.currentState, this);
			// Symboltabelle aktualisieren
			for (CepVariable entry : this.symTab.getKeys()) {

				// logger.debug(this+" Trying to update Symboltable entry "
				// + entry + " with "
				// + event + " " + currentState);

				if (entry.getStateIdentifier().equals(
						this.currentState.getVar())) {
					// if (logger.isDebugEnabled()){
					// logger.debug(this+" Update "+entry+" with "+eventReader.getValue(entry.getAttribute(),
					// event));
					// }
					symTab.updateValue(entry,
							eventReader.getValue(entry.getAttribute(), event));
				}
			}
		} else if (action == EAction.discard) {
			// in diesem Fall ist nichts zu tun
			// logger.debug(this+" Discard "+event);
		} else {
			throw new UndefinedActionException("The action " + action
					+ " is undefined.");
		}
	}

	@Override
	public int hashCode() {
		return instance;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateMachineInstance other = (StateMachineInstance) obj;
		if (instance != other.instance)
			return false;
		return true;
	}

	/**
	 * Gibt eine tiefe Kopie der Automateninstanz zurück.
	 */
	@Override
	public StateMachineInstance<R> clone() {
		return new StateMachineInstance<R>(this);
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public String getStats() {
		String str = toString() + " ";
		str += this.symTab.toString();
		return str;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + instance
				+ (derivedFrom >= 0 ? "[" + derivedFrom + "]" : "") + " ("
				+ startTimestamp + ") Current State: " + currentState.getId()
				+ " " + symTab.toString();
	}

	public StateMachine<R> getStateMachine() {
		return stateMachine;
	}
	
}
