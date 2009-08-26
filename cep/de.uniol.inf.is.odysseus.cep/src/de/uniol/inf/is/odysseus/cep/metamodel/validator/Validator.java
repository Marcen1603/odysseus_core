package de.uniol.inf.is.odysseus.cep.metamodel.validator;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

import de.uniol.inf.is.odysseus.cep.epa.Agent;
import de.uniol.inf.is.odysseus.cep.metamodel.OutputSchemeEntry;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.SymbolTableSchemeEntry;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.ConditionWithoutExpressionError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.InvalidCharactersInNameError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.InvalidVariableNameError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.MissingAcceptingStateError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.MissingAttributeIdentifierError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.MissingMatchinStrategyError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.MissingOutputSchemeError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.MissingStateIdentifierError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.MissingSymbolTableOperationError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.NoActionError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.NoConditionError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.NoExpressionLabelError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.NoInitialStateError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.NoStatesError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.NoSymbolTableSchemeError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.NoTransitionListError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.StateNotInStateSetError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.error.TransitionWithoutDestinationStateError;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.warning.UnreachableStateWarning;

/**
 * Objekte dieser Klasse können im Metamodell spezifizierte Automaten
 * überprüfen, indem alle Invarianten überprüft werden.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Validator {

	/**
	 * Überprüft einen im Metamodell spezifizierten Automaten auf Fehler.
	 * 
	 * @param stateMachine
	 *            Der Automat der überprüft werden soll.
	 * @return Das Validierungsergebnis.
	 */
	public ValidationResult validate(StateMachine stateMachine) {
		ValidationResult result = new ValidationResult();
		result.addValidationError(this.checkStateSet(stateMachine));
		result.addValidationError(this.checkInitialState(stateMachine));

		LinkedList<ValidationException> exceptions = new LinkedList<ValidationException>();
		Hashtable<State, Boolean> visited = new Hashtable<State, Boolean>();
		if (stateMachine.getStates() != null) {
			for (State state : stateMachine.getStates()) {
				visited.put(state, new Boolean(false));
			}
		}
		this
				.visitEachState(stateMachine.getInitialState(), exceptions,
						visited);
		for (ValidationException e : exceptions) {
			result.addValidationException(e);
		}

		LinkedList<ValidationWarning> warnings = this
				.checkStateReachability(visited);
		for (ValidationWarning w : warnings) {
			result.addValidationWarning(w);
		}

		exceptions = this.checkSymbolTableScheme(stateMachine);
		for (ValidationException valEx : exceptions) {
			result.addValidationException(valEx);
		}

		result.addValidationError(this.checkAcceptingState(visited));

		LinkedList<ValidationError> errors = this
				.checkOutputScheme(stateMachine);
		for (ValidationError valErr : errors) {
			result.addValidationError(valErr);
		}

		result.addValidationError(this.checkMatchingStrategy(stateMachine));
		return result;
	}

	/**
	 * Überprüft, ob ein Startzustand vorhanden ist und ob dieser in der
	 * Zustandsmenge des Automaten vorhanden ist (Invarianten 1 und 2).
	 * 
	 * @return Fehlerobjekt oder null, falls kein Fehler gefunden wurde.
	 */
	private ValidationError checkInitialState(StateMachine stateMachine) {
		if (stateMachine.getInitialState() == null) {
			return new NoInitialStateError();
		}
		boolean inStateSet = false;
		if (stateMachine.getStates() != null) {
			for (State state : stateMachine.getStates()) {
				if (state == stateMachine.getInitialState()) {
					inStateSet = true;
				}
			}
		}
		if (!inStateSet) {
			return new StateNotInStateSetError();
		}
		return null;
	}

	/**
	 * Überprüft, ob ein Automat Zustände hat (Invariante 3).
	 * 
	 * @param stateMachine
	 *            Der zu prüfende Automat
	 * @return Validierungsfehler oder null, falls kein Fehler gefunden wurde.
	 */
	private ValidationError checkStateSet(StateMachine stateMachine) {
		if (stateMachine.getStates() != null) {
			if (stateMachine.getStates().size() > 0) {
				return null;
			}
		}
		return new NoStatesError();
	}

	/**
	 * Besucht rekursiv jeden vom Startzustand aus erreichbaren Zustand und jede
	 * erreichbare Transition. Dabei werden die Invarianten 4, 5, 6, 8, 9, 10,
	 * 16, 17 überprüft und bei verstößen gegen diese entsprechende Fehler bzw.
	 * Warn-Objekte erzeugt. Diese Methode hat Seiteneffekte auf die übergebenen
	 * Objekte exceptions und visited!
	 * 
	 * @param currentState
	 *            der zu überprüfende Zustand
	 * @param exceptions
	 *            Liste der Ausnahmen, in die Fehler/Warnungen eingetragen
	 *            werden
	 * @param visited
	 *            Hashtable mit allen in der Zustandsliste des Automaten
	 *            aufgeführten Zuständen, die auf einen Wert vom Typ Boolean
	 *            verweisen. Der Wert ist true, wenn der Zustand schon einmal
	 *            besucht wurde, ansonsten false.
	 */
	private void visitEachState(State currentState,
			LinkedList<ValidationException> exceptions,
			Hashtable<State, Boolean> visited) {
		if (!visited.get(currentState).booleanValue()) {
			// Zustand wurde noch nicht besucht.
			visited.put(currentState, new Boolean(true));
			if (currentState.getTransitions() == null) {
				/*
				 * Invariante 5
				 */
				NoTransitionListError e = new NoTransitionListError();
				e.setRelated(currentState);
				exceptions.add(e);
			} else {
				for (Transition transition : currentState.getTransitions()) {
					if (transition.getNextState() == null) {
						/*
						 * Invariante 4
						 */
						TransitionWithoutDestinationStateError e = new TransitionWithoutDestinationStateError();
						e.setRelated(transition);
						exceptions.add(e);
					} else {
						if (!visited.containsKey(transition.getNextState())) {
							/*
							 * Invariante 6
							 */
							StateNotInStateSetError e = new StateNotInStateSetError();
							e.setRelated(transition.getNextState());
							exceptions.add(e);
						}
						// wenn nächster zustand vorhanden: rekursiver aufruf
						this.visitEachState(transition.getNextState(),
								exceptions, visited);
					}
					if (transition.getAction() == null) {
						/*
						 * Invariante 9
						 */
						NoActionError e = new NoActionError();
						e.setRelated(transition);
						exceptions.add(e);
					}
					if (transition.getCondition() == null) {
						/*
						 * Invariante 8
						 */
						NoConditionError e = new NoConditionError();
						e.setRelated(transition);
						exceptions.add(e);
					} else {
						if (transition.getCondition().getExpression() == null) {
							/*
							 * Invariante 10
							 */
							ConditionWithoutExpressionError e2 = new ConditionWithoutExpressionError();
							e2.setRelated(transition.getCondition());
							exceptions.add(e2);
						} else {
							/*
							 * Invariante 17
							 */
							for (String name : (Set<String>) transition
									.getCondition().getExpression()
									.getSymbolTable().keySet()) {
								if (!this.checkActualVarName(name)
										&& !this.checkHistoricalVarName(name)) {
									InvalidVariableNameError e2 = new InvalidVariableNameError();
									e2.setRelated(name);
									exceptions.add(e2);
								}
							}
						}
						if (transition.getCondition().getLabel() == null) {
							/*
							 * Invariante 16
							 */
							NoExpressionLabelError e2 = new NoExpressionLabelError();
							e2.setRelated(transition.getCondition());
							exceptions.add(e2);
						}
					}
				}
			}
		}
	}

	/**
	 * Überprüft ob Zustände im Automaten definiert sind, die nicht erreichbar
	 * sind (Invariante 15). Für jeden unerreichbaren Zustand wird eine Warnung
	 * erzeugt.
	 * 
	 * @param visited
	 *            Hashmap der von der Methode visitEachState besuchten Zustände.
	 * @return
	 */
	private LinkedList<ValidationWarning> checkStateReachability(
			Hashtable<State, Boolean> visited) {
		LinkedList<ValidationWarning> warnings = new LinkedList<ValidationWarning>();
		for (State state : visited.keySet()) {
			if (visited.get(state).booleanValue() == false) {
				UnreachableStateWarning w = new UnreachableStateWarning();
				w.setRelated(state);
				warnings.add(w);
			}
		}
		return warnings;
	}

	/**
	 * Überprüft, ob das Symboltabellenschema eines Automaten korrekt ist
	 * (Invarianten 11 und 12)
	 * 
	 * @param stateMachine
	 *            Der zu überprüfende Automat
	 * @return Liste der gefunden Fehler/Warnungen.
	 */
	private LinkedList<ValidationException> checkSymbolTableScheme(
			StateMachine stateMachine) {
		LinkedList<ValidationException> exceptions = new LinkedList<ValidationException>();
		if (stateMachine.getSymTabScheme() == null) {
			NoSymbolTableSchemeError e = new NoSymbolTableSchemeError();
			exceptions.add(e);
		} else {
			for (SymbolTableSchemeEntry entry : stateMachine.getSymTabScheme()
					.getEntries()) {
				if (entry.getStateIdentifier() == null
						|| entry.getStateIdentifier().isEmpty()) {
					MissingStateIdentifierError e = new MissingStateIdentifierError();
					e.setRelated(entry);
					exceptions.add(e);
				} else if (!this.checkName(entry.getStateIdentifier())
						&& entry.getStateIdentifier().isEmpty()) {
					InvalidCharactersInNameError e = new InvalidCharactersInNameError();
					e.setRelated(entry.getStateIdentifier());
					exceptions.add(e);
				}
				if (entry.getAttribute() == null) {
					MissingAttributeIdentifierError e = new MissingAttributeIdentifierError();
					e.setRelated(entry);
					exceptions.add(e);
				} else if (!this.checkName(entry.getAttribute())) {
					InvalidCharactersInNameError e = new InvalidCharactersInNameError();
					e.setRelated(entry.getAttribute());
					exceptions.add(e);
				}
				if (entry.getOperation() == null) {
					MissingSymbolTableOperationError e = new MissingSymbolTableOperationError();
					e.setRelated(entry);
					exceptions.add(e);
				}
			}
		}
		return exceptions;
	}

	/**
	 * Prüft, ob ein Name nur aus Buchstaben und Zahlen besteht und das erste
	 * Zeichen ein Buchstabe ist.
	 * 
	 * @param name
	 *            Der Name der geprüft werden soll.
	 * @return true, wenn der name den Vorgaben entspricht, ansonsten false
	 */
	private boolean checkName(String name) {
		if (name == null)
			return false;
		for (int i = 0; i < name.length(); i++) {
			if (i == 0) {
				if (!Character.isLetter(name.charAt(i)))
					return false;
			} else {
				if (!Character.isLetterOrDigit(name.charAt(i)))
					return false;
			}
		}
		return true;
	}

	/**
	 * Überprüft, ob ein Automat eine Matching Strategie hat (Invariante 13).
	 * 
	 * @param stateMachine
	 *            Der zu überprüfende Automat.
	 * @return {@link MissingMatchinStrategyError} falls keine
	 *         Matching-Strategie gesetzt ist, ansonsten null.
	 */
	private ValidationError checkMatchingStrategy(StateMachine stateMachine) {
		if (stateMachine.getConsumptionMode() == null) {
			return new MissingMatchinStrategyError();
		}
		return null;
	}

	/**
	 * Prüft, ob der Automat einen erreichbaren Endzustand hat.
	 * 
	 * @param visited
	 *            Bereits von der Methode visitEachState belegte visit-Tabelle.
	 * @return {@link MissingAcceptingStateError} wenn kein erreichbarer
	 *         Endzustand existiert, ansonsten null.
	 */
	private ValidationError checkAcceptingState(
			Hashtable<State, Boolean> visited) {
		for (State state : visited.keySet()) {
			if (state.isAccepting() && visited.get(state).booleanValue()) {
				return null;
			}
		}
		return new MissingAcceptingStateError();
	}

	/**
	 * Überprüft, ob ein Ausgabeschema vorhanden ist (Invariante 7), ob jeder
	 * Eintrag im Ausgabeschema gültige Variablennamen und ein Label hat
	 * (Invarianten 18 und 19).
	 * 
	 * @param stateMachine
	 *            Der zu prüfende Automat.
	 * @return {@link MissingOutputSchemeError} wenn kein Ausgabeschema
	 *         vorhanden ist, ansonsten null.
	 */
	private LinkedList<ValidationError> checkOutputScheme(
			StateMachine stateMachine) {
		LinkedList<ValidationError> errors = new LinkedList<ValidationError>();
		if (stateMachine.getOutputScheme() == null) {
			errors.add(new MissingOutputSchemeError());
		} else {
			for (OutputSchemeEntry entry : stateMachine.getOutputScheme()
					.getEntries()) {
				Set<String> varNames = entry.getExpression().getSymbolTable()
						.keySet();
				for (String name : varNames) {
					if (!this.checkActualVarName(name)
							&& !this.checkHistoricalVarName(name)) {
						InvalidVariableNameError e = new InvalidVariableNameError();
						e.setRelated(name);
						errors.add(e);
					}
				}
				if (entry.getLabel() == null) {
					NoExpressionLabelError e = new NoExpressionLabelError();
					e.setRelated(entry);
					errors.add(e);
				}
			}

		}
		return errors;
	}

	/**
	 * Prüft, ob ein Variablenname der Namenskonvention für Variablen, die sich
	 * auf historische (bereits konsumkierte) Events bezieht, entspricht.
	 * 
	 * @param varName
	 *            Der zu prüfende Variablenname.
	 * @return true, wenn der Variablenname der Konvention entspricht, ansonsten
	 *         false.
	 */
	private boolean checkHistoricalVarName(String varName) {
		if (varName == null) {
			return false;
		} else {
			String[] split = varName.split(Agent.SEPERATOR);
			if (split.length == 4) {
				if (!this.checkName(split[0]) || !this.checkName(split[1]))
					return false;
				if (!this.checkName(split[3]) && !split[3].isEmpty())
					return false;
				if (!split[2].isEmpty()) {
					try {
						Integer.parseInt(split[2]);
					} catch (NumberFormatException e) {
						return false;
					}
				}
			} else if (split.length == 2) {
				if (!this.checkName(split[0]) || !this.checkName(split[1]))
					return false;
				if (!varName.endsWith(Agent.SEPERATOR + Agent.SEPERATOR))
					return false;
				
			} else {
				return false;
			}
			// for (int i = 0; i < split[1].length(); i++) {
			// if (!Character.isDigit(split[i].charAt(i)))
			// return false;
			// }
		}
		return true;
	}

	/**
	 * Prüft, ob ein Variablenname der Namenskonvention für Variablen, die sich
	 * auf das aktuelle (gerade in der Verarbeitung befindliche) Event beziehen,
	 * entspricht.
	 * 
	 * @param varName
	 *            Der zu prüfende Variablenname.
	 * @return true, wenn der Variablenname der Konvention entspricht, ansonsten
	 *         false.
	 */
	private boolean checkActualVarName(String name) {
		if (name == null)
			return false;
		String[] split = name.split(Agent.SEPERATOR);
		for (int i = 0; i < 3; i++) {
			if (!split[i].isEmpty())
				return false;
		}
		if (!this.checkName(Agent.getAttributeName(name)))
			return false;
		return true;
	}
}
