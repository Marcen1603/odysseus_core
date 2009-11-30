package de.uniol.inf.is.odysseus.cep.statemachinefactory;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTableScheme;

/**
 * Diese Fabrikklasse erstellt gültige Automaten im Metamodel. Die erstellten
 * Automaten werden zum Testen benötigt.
 * 
 * @author Thomas Vogelgesang
 * 
 */
@SuppressWarnings("unchecked")
public abstract class StateMachineFactory {

	protected LinkedList<State> states;
	protected State initialState;
	protected SymbolTableScheme symbolTableScheme;
	protected OutputScheme outputScheme;

	public StateMachineFactory() {
		this.states = new LinkedList<State>();
	}

	public StateMachine create(Object[] args) {
		this.initComponents(args);
		StateMachine stm = new StateMachine(this.states, this.initialState,
				this.symbolTableScheme, this.outputScheme);
		return stm;
	}

	/**
	 * initialisiert die einzelnen Komponenten des Automaten. Über den Parameter
	 * args können beliebige Objekte an die konkrete Implemntierung übergeben
	 * werden.
	 * 
	 * @param args
	 *            Array von Objekten. Welchen Typ die Objekte haben, wie viele
	 *            es sind sowie ob und an welcher Stelle null erlaubt ist, wird
	 *            von der jeweiligen konkreten Implementierung bestimmt und in
	 *            deren Doku festgehalten werden.
	 */
	protected abstract void initComponents(Object[] args);

}
