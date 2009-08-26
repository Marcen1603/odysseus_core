package de.uniol.inf.is.odysseus.cep.statemachinefactory;

import java.util.LinkedList;


import de.uniol.inf.is.odysseus.cep.metamodel.Action;
import de.uniol.inf.is.odysseus.cep.metamodel.Condition;
import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.SymbolTableScheme;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

public class SMF1 extends StateMachineFactory {

	public SMF1() {
		super();
	}

	/**
	 * erzeugt einen einfachen Test-Automaten mit einer bestimmten Anzahl
	 * sequentieller Zustände. Als erstes Elemnt von arg kann ein Integerobjekt
	 * mit einer Zahl größer 0 übergeben werden, die angibt wie viele Zustände
	 * der Automat haben soll. Wird kein Wert übergeben, so werden 5 Zustände
	 * erzeugt. Alle Transitionsbedingungen sind "true", alle Aktionen vom Typ
	 * Discard.
	 */
	@Override
	protected void initComponents(Object[] args) {
		int numberOfStates = 5;
		if (args != null) {
			if (args.length > 0) {
				if (args[0] != null) {
					if (args[0] instanceof Integer) {
						int argNumber = ((Integer) args[0]).intValue();
						if (argNumber > 0) {
							numberOfStates = argNumber;
						}
					}
				}
			}
		}

		State dest = null;
		LinkedList<Transition> outTransitions = null;
		for (int i = 0; i < numberOfStates; i++) {
			boolean acc = (i == 0) ? true : false;
			outTransitions = new LinkedList<Transition>();
			if (dest != null) {
				Condition cond = new Condition("x + 1");
				outTransitions.add(new Transition(i, dest, cond, Action.discard));
			}
			State s = new State("state" + i, acc, outTransitions);
			dest = s;
			this.states.add(s);
			if (i == numberOfStates - 1)
				this.initialState = s;
		}

		this.symbolTableScheme = new SymbolTableScheme();

		this.outputScheme = new OutputScheme();
	}

}
