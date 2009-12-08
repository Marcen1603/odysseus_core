package de.uniol.inf.is.odysseus.cep.epa;

import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.cep.epa.eventreading.IEventReader;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.UndefinedActionException;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTable;

/**
 * Die StateMachineInstance repräsentiert eine Instanz des an den EPA
 * übergebenen Automaten, sodass das eigentliche Automaten-Objekt vom Typ {@link
 * StateMachine} zur Laufzeit nur einmal vorgehalten werden muss.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class StateMachineInstance<R> {

	/**
	 * Referenz auf den aktuellen Zustand der Automateninstanz
	 */
	private State currentState;
	/**
	 * Referenz auf die Symboltabelle der Automateninstanz
	 */
	private SymbolTable symTab;
	/**
	 * Referenz auf den Puffer für bereits konsumierte Events
	 */
	private MatchingTrace<R> matchingTrace;

	/**
	 * erzeugt eine neue Automateninstanz
	 * 
	 * @param stateMachine
	 *            Referenz auf den Automaten, von dem eine neue Instanz erstellt
	 *            werden soll
	 */
	public StateMachineInstance(StateMachine<R> stateMachine) {
		this.currentState = stateMachine.getInitialState();
		this.matchingTrace = new MatchingTrace<R>(stateMachine.getStates());
		this.symTab = new SymbolTable(stateMachine.getSymTabScheme(true));
	}

	public StateMachineInstance(StateMachineInstance<R> stateMachineInstance) {
		this.currentState = stateMachineInstance.currentState; // Hier reicht Referenz
		this.symTab = stateMachineInstance.symTab.clone();
		this.matchingTrace =  stateMachineInstance.matchingTrace.clone();
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
	public SymbolTable getSymTab() {
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
			IEventReader<R,?> eventReader) throws UndefinedActionException {
		if (action == EAction.consume) {
			this.matchingTrace.addEvent(event, this.currentState);	
			// Symboltabelle aktualisieren
			for (CepVariable entry : this.symTab.getKeys()) {
				
				if (entry.getStateIdentifier().equals(
						this.currentState.getId())) {
					if (entry.getIndex() == this.matchingTrace
							.getStateBufferSize(this.currentState)-1) {
						symTab.executeOperation(entry, eventReader.getValue(entry.getAttribute(), event));
					} else if (entry.getIndex() < 0) {
						symTab.executeOperation(entry, eventReader.getValue(entry.getAttribute(), event));
					}
				}
			}
		} else if (action == EAction.discard) {
			// in diesem Fall ist nichts zu tun
		} else {
			throw new UndefinedActionException("The action "
					+ action + " is undefined.");
		}
	}

	/**
	 * Gibt eine tiefe Kopie der Automateninstanz zurück.
	 */
	@Override
	public StateMachineInstance<R> clone() {
		return new StateMachineInstance<R>(this);
	}
	
	public String getStats() {
		String str = "State machine instance statistics:\n";
		str += "Current state: " + this.currentState.getId() + "\n";
		str += this.symTab.toString();
		return str;
	}
}
