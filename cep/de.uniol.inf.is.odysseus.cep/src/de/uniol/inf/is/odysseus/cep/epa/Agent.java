package de.uniol.inf.is.odysseus.cep.epa;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.nfunk.jep.JEP;
import org.nfunk.jep.Variable;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.ComplexEventFactory;
import de.uniol.inf.is.odysseus.cep.epa.eventreading.AbstractEventReader;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.ConditionEvaluationException;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidEventException;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidStateMachineException;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.UndefinedConsumptionModeException;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.UndeterminableVariableValueException;
import de.uniol.inf.is.odysseus.cep.metamodel.ConsumptionMode;
import de.uniol.inf.is.odysseus.cep.metamodel.OutputSchemeEntry;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationResult;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.Validator;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

/**
 * Objekte dieser Klasse stellen die Grundkomponente für das Complex Event
 * Processing dar. Sie sind als Operator in den physikalischen Ablaufplan
 * integrierbar und steuern die gesamte Verarbeitung von komplexen Events
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Agent extends AbstractPipe<Object, Object> {

	/**
	 * Trennzeichen für die einzelnen Bestandteile der Variablen.
	 */
	public static final String SEPERATOR = "_";
	/**
	 * Präfix einer Variable, die sich auf das aktuelle Event bezieht. Besteht
	 * aus 2 aufeinander folgenden Unterstrichen, da sowohl der
	 * Event/state-Identifier, als auch der Index leer sind.
	 */
	@Deprecated
	public static final String ACT_EVENT_PREFIX = "__";
	/**
	 * Factory zum erzeugen komplexer Events
	 */
	private ComplexEventFactory complexEventFactory;
	/**
	 * Referenz auf eine eventReader Implementierung zum datenmodellunabhängigen
	 * Auslesen von Events.
	 */
	private AbstractEventReader eventReader;
	/**
	 * Referenz auf den Verzweigungsspeicher
	 */
	private BranchingBuffer branchingBuffer;
	/**
	 * Liste aller Automaten-Instanzen, die gerade vom EPA verarbeitet werden
	 */
	private LinkedList<StateMachineInstance> instances;
	/**
	 * Der Automat, der das zu suchende Event-Muster sowie die Event-Aggregation
	 * und die Struktur des Zwischenspeichers enthält
	 */
	private StateMachine stateMachine;
	/**
	 * true, wenn die CEP-Komponente im Debug Modus benutz wird, false sonst
	 */
	private boolean debug;
	/**
	 * LogManager fürs debuggen
	 */
	private LogManager logMan;
	/**
	 * Standard Logger fürs debuggen
	 */
	private Logger defLog;

	/**
	 * leerer Standardkonstruktor
	 */
	public Agent() {
		super();
	}

	/**
	 * Erzeugt einen neuen EPA.
	 * 
	 * @param stateMachine
	 *            Der Automat, der das Suchmuster beschreibt
	 * @param eventReader
	 *            Eine konkrete EventReader-Instanz, die das Datenmodell des
	 *            Eingabestroms lesen und auswerten kann.
	 * @param complexEventFactory
	 *            Eine Fabrikklasse, die dem Datenmodell des Ausgabestroms
	 *            entspricht
	 * @param validate
	 *            Gibt an, ob der übergebene Automat auf Fehler überprüft werden
	 *            soll. True, wenn nach Fehlern gesucht werden soll, ansonsten
	 *            false.
	 * @throws InvalidStateMachineException
	 *             Falls der übergebene Automat nicht die erforderlichen
	 *             Invarianten einhält.
	 */
	public Agent(StateMachine stateMachine, AbstractEventReader eventReader,
			ComplexEventFactory complexEventFactory, boolean validate)
			throws Exception {
		super();
		this.stateMachine = stateMachine;
		this.complexEventFactory = complexEventFactory;
		this.eventReader = eventReader;
		this.instances = new LinkedList<StateMachineInstance>();
		this.branchingBuffer = new BranchingBuffer();
		if (validate) {
			Validator validator = new Validator();
			ValidationResult result = validator.validate(stateMachine);
			if (!result.isValid()) {
				throw new InvalidStateMachineException(result);
			}
		}
		this.debug = false;

		// preparing logger:
		this.logMan = LogManager.getLogManager();
		this.logMan.addLogger(Logger.getLogger("default"));
		this.defLog = this.logMan.getLogger("default");
		this.defLog.setLevel(Level.ALL);
		try {
			this.defLog.addHandler(new FileHandler("%h/cep.log"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.defLog.getHandlers()[0].setFormatter(new SimpleFormatter());
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	/**
	 * Verarbeitet ein übergebenes Event.
	 */
	@Override
	protected void process_next(Object object, int port) {
		this.debug("Beginne Verarbeitung von Event " + object);
		this.debug(this.getStats());

		this.instances.add(new StateMachineInstance(this.stateMachine));
		if (object == null)
			throw new InvalidEventException(
					"The event to be processed is null.");

		LinkedList<StateMachineInstance> outdatedInstances = new LinkedList<StateMachineInstance>();
		LinkedList<StateMachineInstance> branchedInstances = new LinkedList<StateMachineInstance>();

		// Auswerten der Transition:
		for (Iterator<StateMachineInstance> i = this.instances.iterator(); i
				.hasNext();) {

			StateMachineInstance instance = i.next();

			this.debug(instance.getStats());

			Stack<Transition> takeTransition = new Stack<Transition>();

			for (int k = 0; k < instance.getCurrentState().getTransitions()
					.size(); k++) {
				Transition transition = instance.getCurrentState()
						.getTransitions().get(k);
				/*
				 * Über Variablen iterieren und neu belegen.
				 */
				Set<String> varNames = transition.getCondition()
						.getExpression().getSymbolTable().keySet();
				Iterator<String> varIt = varNames.iterator();
				while (varIt.hasNext()) {
					Variable var = transition.getCondition().getExpression()
							.getVar(varIt.next());
					this.debug("Setze Variable " + var.getName());
					if (Agent.isActEventName(var.getName())) {
						// Variable bezieht sich auf aktuelles Event
						var.setValue(this.eventReader.getValue(Agent
								.getAttributeName(var.getName()), object));
						this.debug("Neuer Wert: "
								+ this.eventReader.getValue(Agent
										.getAttributeName(var.getName()),
										object));
					} else {
						// Variable bezieht sich auf historisches (bereits
						// konsumiertes Event)
						var.setValue(instance.getSymTab().getValue(
								var.getName()));
						this.debug("Neuer Wert: "
								+ instance.getSymTab().getValue(var.getName()));
					}
				}
				try {
					/*
					 * C-Semantik: Alles ungleich 0 oder null ist true! JEP tut
					 * komische Dinge: - Vergleichsoperatoren liefern
					 * Boolean-Objekte und NaN getValue() - alle anderen
					 * Operatoren liefern Double-Objekte (auch für boolesche
					 * Operatoren, immer 0.0 oder 1.0)
					 */
					double conditionValue = transition.getCondition()
							.getExpression().getValue();
					if (Double.isNaN(conditionValue)) {
						Boolean boolVal = (Boolean) transition.getCondition()
								.getExpression().getValueAsObject();
						conditionValue = boolVal.booleanValue() ? 1.0 : 0.0;
					}
					if (conditionValue != 0.0) {
						takeTransition.push(transition);
						this.debug("Transitionsbedingung ist true: "
								+ transition.getCondition().getLabel()
								+ " (Wert: "
								+ transition.getCondition().getExpression()
										.getValue()
								+ ", "
								+ transition.getCondition().getExpression()
										.getErrorInfo() + ")");
					}
				} catch (Exception e) {
					// System.out.println(transition.getCondition().getLabel());
					// System.out.println(transition.getCondition().getExpression().getErrorInfo());
					// e.printStackTrace();
					throw new ConditionEvaluationException(
							"Cannot evaluate condition "
									+ transition.getCondition());
				}
			}
			if (takeTransition.isEmpty()) {
				/*
				 * Kein Zustandswechsel möglich: Automateninstanz als entfernbar
				 * makieren. Die Instanz muss auch aus dem Verzweigungsspeicher
				 * entfernt werden um Speicherleichen zu verhindern.
				 */
				this
						.debug("Keine Transition kann genommen werden. Verwerfe Instanz "
								+ instance);
				outdatedInstances.add(instance);
				this.branchingBuffer.removeBranch(instance);
			} else if (takeTransition.size() == 1) {
				// genau 1 Folgezustand: Zustand wechseln und Aktion ausführen.
				this.debug("Eine gehbare Transition gefunden: "
						+ takeTransition.peek());
				this.takeTransition(instance, takeTransition.pop(), object);
			} else if (takeTransition.size() > 1) {
				// mehr als 1 Folgezustand: nichtdeterministische Verzweigung!
				this
						.debug(""
								+ takeTransition.size()
								+ " Transitionen können genommen werden (Nichtdeterministische Verzweigung).");
				while (takeTransition.size() > 1) {
					StateMachineInstance newInstance = instance.clone();
					this.takeTransition(newInstance, takeTransition.pop(),
							object);
					this.branchingBuffer.addBranch(instance, newInstance);
					branchedInstances.add(newInstance);
				}
				this.takeTransition(instance, takeTransition.pop(), object);
			}
		}

		this.instances.addAll(branchedInstances);

		// Überprüfen auf Endzustände und erzeugen komplexer Events
		LinkedList<Object> complexEvents = new LinkedList<Object>();
		for (Iterator<StateMachineInstance> i = this.instances.iterator(); i
				.hasNext();) {
			StateMachineInstance instance = i.next();

			/*
			 * Durch das Markieren der veralteten Automateninstanzen und das
			 * nachträgliche entfernen, kann instance bereits veraltet sein.
			 * Diese muss damit als gelöscht gelten, obwohl sie noch in der
			 * Instanzen-Liste enthalten ist. Eine Verarbeitung solcher
			 * Instanzen kann eventuell zu fehlerhaftem Verhalten führen. Um das
			 * zu verhindern, müssen diese Instanzen bei der Verarbeitung
			 * übersprungen werden.
			 */
			if (outdatedInstances.contains(instance))
				continue;

			if (instance.getCurrentState().isAccepting()) {
				// Werte in den Symboltabellen der JEP-Ausdrücke im
				// Ausgabeschema setzen:
				for (OutputSchemeEntry entry : this.stateMachine
						.getOutputScheme().getEntries()) {
					JEP expr = entry.getExpression();
					Set<String> varNames = expr.getSymbolTable().keySet();
					Iterator<String> varIt = varNames.iterator();
					while (varIt.hasNext()) {
						Variable var = expr.getVar(varIt.next());
						/*
						 * 2 mögliche Fälle: 1. variablenwert steht in der
						 * symboltabelle 2. variablenwert steht in einem
						 * konsumierten Event
						 */
						Object value = instance.getSymTab().getValue(
								var.getName());
						if (value == null) {
							// variablenwert steht nicht in der symboltabelle
							// -> wert im MatchingTrace suchen (teuer?!)
							String[] split = var.getName().split(
									Agent.SEPERATOR);
							StateBuffer buffer = instance.getMatchingTrace()
									.getStateBuffer(split[1]);
							if (buffer != null) {
								try {
									Object event = null;
									if (split[2].isEmpty()) {
										/*
										 * leerer String an Index-Position heißt
										 * oberstes Buffer Element (Das Element,
										 * welches zuletzt vom Buffer konsumiert
										 * wurde).
										 */
										event = buffer.getEvents().getLast()
												.getEvent();
									} else {
										int index = Integer.parseInt(split[2]);
										event = buffer.getEvents().get(index)
												.getEvent();
									}
									value = this.eventReader.getValue(split[3],
											event);
								} catch (Exception e) {
									throw new UndeterminableVariableValueException(
											"'"
													+ var.getName()
													+ "' not found in symbol table. Cannot find related event in state buffer."
													+ "");
								}
							} else {
								throw new UndeterminableVariableValueException(
										"'"
												+ var.getName()
												+ "' not found in symbol table. Cannot find related state buffer '"
												+ split[0]
												+ "' in matching trace.");
							}
						}
						var.setValue(value);
					}
				}
				complexEvents.add(this.complexEventFactory.createComplexEvent(
						this.stateMachine.getOutputScheme(), instance
								.getMatchingTrace(), instance.getSymTab()));
				/*
				 * An dieser Stelle muss die Instanz, die zum Complex Event
				 * geführt hat, als veraltet markiert werden. Je nach
				 * Consumption-Mode müssen auch die verwandten Instanzen als
				 * veraltet markiert werden.
				 */
				outdatedInstances.addAll(this
						.getRemovableInstancesByConsumptionMode(instance));
			}
		}

		// Aufräumen: Alle als veraltet markierten Automateninstanzen entfernen.
		this.instances.removeAll(outdatedInstances);

		this.debug("Erzeugte Events: " + complexEvents);

		this.transfer(complexEvents);

		this.debug("Verarbeitung abgeschlossen\n");
	}

	/**
	 * Liefert das Factory-Objekt für komplexe Events.
	 * 
	 * @return Das Factory-Objekt für komplexe Events.
	 */
	public ComplexEventFactory getComplexEventFactory() {
		return complexEventFactory;
	}

	/**
	 * Setzt die Factory für die komplexen Events und legt damit gleichzeitig
	 * den Datentyp des Ausgabestroms fest.
	 * 
	 * @param complexEventFactory
	 *            Das neue Factory-Objekt für komplexe Events, nicht null.
	 */
	public void setComplexEventFactory(ComplexEventFactory complexEventFactory) {
		this.complexEventFactory = complexEventFactory;
	}

	/**
	 * Liefert das Event-Reader-Objekt.
	 * 
	 * @return Das Event-Reader-Objekt.
	 */
	public AbstractEventReader getEventReader() {
		return eventReader;
	}

	/**
	 * Setzt das Event-Reader-Objekt und definiert damit gleichzeitig den
	 * Datentyp des Eingabestroms.
	 * 
	 * @param eventReader
	 *            Das neue Event-Reader-Objekt, nicht null.
	 */
	public void setEventReader(AbstractEventReader eventReader) {
		this.eventReader = eventReader;
	}

	/**
	 * Liefert den Automaten, der das zu suchende Event-Muster definiert.
	 * 
	 * @return Den Automaten, der das zu suchende Event-Muster definiert.
	 */
	protected StateMachine getStateMachine() {
		return stateMachine;
	}

	/**
	 * Liefert den Verzweigungsspeicher des EPA zurück.
	 * 
	 * @return Der aktuelle Verzweigungsspeicher.
	 */
	public BranchingBuffer getBranchingBuffer() {
		return branchingBuffer;
	}

	/**
	 * Liefert eine Liste, mit allen zur Zeit aktiven Automateninstanzen.
	 * 
	 * @return Liste der Automateninstanzen.
	 */
	public LinkedList<StateMachineInstance> getInstances() {
		return instances;
	}

	/**
	 * ACHTUNG: Diese Methode ist fehlerhaft und wird deshalb bald entfernt!
	 * Entfernt die übergebene Automateninstanz aus dem {@link BranchingBuffer}.
	 * Ist der Consumption-Mode onlyOneMatch, so werden zusätzlich alle
	 * verwandten Automateninstanzen aus dem {@link BranchingBuffer} entfernt.
	 * In einer Liste werden alle aus dem {@link BranchingBuffer} entfernten
	 * Instanzen zurückgegeben, welche nach der Iteration über die Liste der
	 * Atomateninstanzen aus dieser entfernt werden müssen. Die übergebene
	 * Instanz ist in jedem Fall in der zurückgegebenen Liste enthalten. Auch
	 * sie muss wie alle anderen gelieferten Automateninstanzen außerhalb der
	 * Methode entfernt werden.
	 * 
	 * @param instance
	 *            Die zu entfernende Automateninstanz
	 * @return Eine Liste, die alle zu entfernenden Automateninstanzen in
	 *         Abhängigkeit vom Consumption Mode enthält. Die übergebene Instanz
	 *         instance ist immer in der Liste enthalten.
	 */
	private LinkedList<StateMachineInstance> getRemovableInstancesByConsumptionMode(
			StateMachineInstance instance) {
		LinkedList<StateMachineInstance> outdated = null;
		if (this.stateMachine.getConsumptionMode() == ConsumptionMode.onlyOneMatch) {
			outdated = this.branchingBuffer
					.getAllNestedStateMachineInstances(instance);
			this.branchingBuffer.removeAllNestedBranches(instance);
		} else if (this.stateMachine.getConsumptionMode() == ConsumptionMode.allMatches) {
			outdated = new LinkedList<StateMachineInstance>();
			outdated.add(instance);
			this.branchingBuffer.removeBranch(instance);
		} else {
			throw new UndefinedConsumptionModeException(
					"Undefined consumption mode: "
							+ this.stateMachine.getConsumptionMode());
		}
		return outdated;
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
	 */
	private void takeTransition(StateMachineInstance instance,
			Transition transition, Object event) {
		this.debug("Zustandswechsel: " + instance.getCurrentState().getId()
				+ "-->" + transition.getNextState().getId());
		instance.setCurrentState(transition.getNextState());
		this.debug("Führe Aktion aus: " + transition.getAction());
		instance.executeAction(transition.getAction(), event, this.eventReader);
		/*
		 * Die Reihenfolge der Methodenaufrufe legt fest, in welchem StateBuffer
		 * das Event gespeichert wird. Wird der Zustand zuerst gewechselt, wird
		 * das Event dem Zielzustand der Transition zugeordnet, ansonsten dem
		 * Ausgangszustand.
		 * 
		 * Die CEP-Komponente erfordert zur Zeit, dass erst der Zustand
		 * gewechselt wird und anschließend die Aktion ausgeführt wird. Die
		 * umgekehrte Reihenfolge würde zu Problemen mit der internen
		 * Namenskonvention für Events führen.
		 */
	}

	/**
	 * liefert true, wenn sich der Agent im Debug-Modus befindet, ansonsten
	 * false. Standardwert ist false.
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * setzt den Debug-Modus
	 * 
	 * @param debug
	 *            true, wenn der debugging aktiviert werden soll, false sonst
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Schreibt eine Debug-Nachricht in das Log-File. Das Log-File wird im
	 * Home-Verzeichnis als "cep.log" gespeichert.
	 * 
	 * @param message
	 *            Die Nachricht, die in das Log-File geschrieben werden soll
	 */
	private void debug(String message) {
		if (this.debug)
			this.defLog.log(Level.ALL, message);
	}

	/**
	 * Liefert Statistiken zum aktuellen Zustand des EPA.
	 * 
	 * @return
	 */
	public String getStats() {
		String str = "Current EPA state:\n";
		str = str + "Number of state machine instances:"
				+ this.instances.size() + "\n";
		str = str + "Number of branch trees:"
				+ this.branchingBuffer.getBranches().size();
		return str;
	}

	/*
	 * **************************************************************************
	 * Hilfsmethoden fürs Namensschema der Variablen *
	 * ***************************
	 * ***********************************************
	 */

	/*
	 * Die internen Variablennamen sind nach folgendem Muster aufgebaut:
	 * <OperationName>_<StateID>_<Index>_<AttributName>
	 * 
	 * OperationName: Name der Symboltabellenoperation. Bezieht sich der
	 * Variablenname auf das aktuelle Event, so ist OperationName leer.
	 * 
	 * StateID: Name des Zustands, in dem das Event konsumiert wurde. Bezieht
	 * sich der Variablenname auf das aktuelle Event, so ist StateID leer
	 * 
	 * Index: Der Index des Events im StateBuffer. Bezieht sich der
	 * Variablenname auf das aktuelle Event, so ist Index leer
	 * 
	 * AttributName: Der Name des Attributs, auf das sich die Variable bezieht.
	 * Ist leer, wenn sich die Variable nicht auf ein Attribut, sondern direkt
	 * auf ein Event bezieht (nur für Operation Count)
	 */

	/**
	 * Überprüft, ob sich der übergebene Variablenname auf ein aktuelles Event
	 * bezieht.
	 * 
	 * @param name
	 *            Der zu prüfende Variablenname
	 * @return true, wenn sich der Variablenname auf ein aktuelles Event
	 *         bezieht, ansonsten false.
	 */
	public static boolean isActEventName(String name) {
		String[] split = name.split(Agent.SEPERATOR);
		if (split[0].isEmpty() && split[1].isEmpty() && split[2].isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static String getAttributeName(String varName) {
		String[] split = varName.split(Agent.SEPERATOR);
		return split[3];
	}

}
