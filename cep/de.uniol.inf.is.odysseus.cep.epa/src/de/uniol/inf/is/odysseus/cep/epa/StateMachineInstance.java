package de.uniol.inf.is.odysseus.cep.epa;

import de.uniol.inf.is.odysseus.cep.epa.eventreading.IEventReader;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.UndefinedActionException;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTable;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTableEntry;

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
	public StateMachineInstance(StateMachine stateMachine) {
		this.currentState = stateMachine.getInitialState();
		this.matchingTrace = new MatchingTrace<R>(stateMachine.getStates());
		this.symTab = new SymbolTable(stateMachine.getSymTabScheme());
	}

	/**
	 * Vertseckter Konstruktor der nur für die clone()-Methode benötigt wird und
	 * daher ansonsten nicht benutzt werden sollte
	 * 
	 * @param currentState
	 *            Referenz auf den aktuellen Zustand
	 * @param matchingTrace
	 *            (Tiefe) Kopie des MatchingTrace
	 * @param symTab
	 *            (Tiefe) Kopie der Symboltabelle
	 */
	private StateMachineInstance(State currentState,
			MatchingTrace<R> matchingTrace, SymbolTable symTab) {
		this.currentState = currentState;
		this.matchingTrace = matchingTrace;
		this.symTab = symTab;
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
			for (SymbolTableEntry<?> entry : this.symTab.getEntries()) {
				if (entry.getScheme().getStateIdentifier().equals(
						this.currentState.getId())) {
					if (entry.getScheme().getIndex() == this.matchingTrace
							.getStateBuffer(this.currentState).getEvents()
							.size()-1) {
						entry.executeOperation(eventReader.getValue(entry.getScheme().getAttribute(), event));
					} else if (entry.getScheme().getIndex() < 0) {
						entry.executeOperation(eventReader.getValue(entry.getScheme().getAttribute(), event));
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
		return new StateMachineInstance<R>(this.currentState, this.matchingTrace
				.clone(), this.symTab.clone());
	}
	
	public String getStats() {
		String str = "State machine instance statistics:\n";
		str += "Current state: " + this.currentState.getId() + "\n";
		str += this.symTab.toString();
		return str;
	}
}
