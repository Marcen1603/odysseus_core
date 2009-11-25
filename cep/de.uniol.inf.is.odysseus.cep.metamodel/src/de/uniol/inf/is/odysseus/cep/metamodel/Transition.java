package de.uniol.inf.is.odysseus.cep.metamodel;

import javax.xml.bind.annotation.XmlIDREF;

/**
 * Zustandsübergang eines Automaten. Die Zustandsübergänge werden grundsätzlich
 * in dem Zustand referenziert, von dem der Zustandsübergang ausgeht. Der
 * Zielzustand wird immer in der Transition als Attribut gespeichert.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Transition {

	/**
	 * Die im gesamten Automaten eindeutige ID der Transition.
	 */
	private int id;
	/**
	 * Referenz auf den Folgezustand. Darf während der Verarbeitung durch den
	 * EPA nicht null sein. Muss zudem ein Zustandsobjekt aus der Menge der
	 * Zustandsmenge des Automaten sein.
	 */
	private State nextState;
	/**
	 * Die Transitionsbedingung
	 */
	private Condition condition;
	/**
	 * Die Automatenausgabe / Aktion für die Transition
	 */
	private Action action;

	/**
	 * Default-Konstruktor
	 */
	public Transition() {
	}

	/**
	 * Erstellt ein neues Transitionsobjekt
	 * 
	 * @param id
	 *            Die automatenweit eindeutige id der Transition
	 * @param nextState
	 *            Referenz auf den Folgezustand
	 * @param condition
	 *            Die Transitionsbedingung
	 * @param action
	 *            Die Aktion, die für eine erfüllte Transitionsbedingung
	 *            ausgeführt wird.
	 */
	public Transition(int id, State nextState, Condition condition,
			Action action) {
		this.id = id;
		this.nextState = nextState;
		this.condition = condition;
		this.action = action;
	}

	/**
	 * Liefert die ID der Transition, welche im gesamten Automaten eindeutig
	 * ist.
	 * 
	 * @return Die ID der Transition.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setzt die ID der Transition.
	 * 
	 * @param id
	 *            Die neue ID der Transition, die im gesamten Automaten
	 *            eindeutig sein muss.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Liefert den Folgezustand der Transition.
	 * 
	 * @return Folgezustand der Transition, nicht null. Der gelieferte Zustand
	 *         ist ein Objekt aus der Menge der Zustände des Automaten.
	 */
	@XmlIDREF
	public State getNextState() {
		return nextState;
	}

	/**
	 * Setzt den Folgezustand der Transition.
	 * 
	 * @param nextState
	 *            Der neue Folgezustand der Transition. Darf nicht null sein.
	 *            Das übergebene Objekt muss auch in der Liste der Zustände im
	 *            Automaten vorhanden sein.
	 */
	public void setNextState(State nextState) {
		this.nextState = nextState;
	}

	/**
	 * Liefert die Transitionsbedingung.
	 * 
	 * @return Die Transitionsbedingung.
	 */
	public Condition getCondition() {
		return condition;
	}

	/**
	 * Setzt eine neue Transitionsbedingung.
	 * 
	 * @param condition
	 *            Die neue Transitionsbedingung, nicht null.
	 */
	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	/**
	 * Liefert die Action (Mealy-Ausgabe) der Transition
	 * 
	 * @return Die Action der Transition.
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Setzt eine neue Action (Mealy-Ausgabe) für die Transition.
	 * 
	 * @param action
	 *            Die neue Action der Transition, nicht null.
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	public String toString(String indent) {
		String str = indent + "Transition: " + this.hashCode();
		indent += "  ";
		str += indent + "id: " + this.id;
		str += indent + "nextState: " + this.nextState.getId() + " ("
				+ this.nextState.hashCode() + ")";
		str += this.condition.toString(indent);
		str += indent + "Action: " + this.action;
		return str;
	}

}
