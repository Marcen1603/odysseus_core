package de.uniol.inf.is.odysseus.cep.statemachinefactory;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
import de.uniol.inf.is.odysseus.cep.metamodel.jep.JEPCondition;
import de.uniol.inf.is.odysseus.cep.metamodel.jep.JEPOutputSchemeEntry;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTableScheme;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Write;

@SuppressWarnings("unchecked")
public class SMF2 extends StateMachineFactory {
	
	public SMF2() {
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
		LinkedList<Transition> transistions1 = new LinkedList<Transition>();
		
		transistions1.add(new Transition("0", null, new JEPCondition("1"),
				EAction.discard));
		transistions1.add(new Transition("1", null, new JEPCondition("1"),
				EAction.discard));
		State state1 = new State("state1", false, transistions1);
		transistions1 = new LinkedList<Transition>();
		State state2 = new State("state2", true, transistions1);
		state1.getTransitions().get(0).setNextState(state1);
		state1.getTransitions().get(1).setNextState(state2);
		this.states = new LinkedList<State>();
		this.states.add(state1);
		this.states.add(state2);
		this.initialState = state1;

		this.outputScheme = new OutputScheme();
		this.outputScheme.getEntries().add(new JEPOutputSchemeEntry("1"));
		this.outputScheme.getEntries().add(
				new JEPOutputSchemeEntry("state1__attribute1"));

		this.symbolTableScheme = new SymbolTableScheme();
		this.symbolTableScheme.getEntries().add(
				new CepVariable(0, "abcd", 1, "xyz", new Write()));
	}

}
