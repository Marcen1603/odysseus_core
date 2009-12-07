package de.uniol.inf.is.odysseus.cep.epa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.IComplexEventFactory;
import de.uniol.inf.is.odysseus.cep.epa.eventreading.IEventReader;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.ConditionEvaluationException;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidEventException;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.UndefinedConsumptionModeException;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.UndeterminableVariableValueException;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.EConsumptionMode;
import de.uniol.inf.is.odysseus.cep.metamodel.IOutputSchemeEntry;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
import de.uniol.inf.is.odysseus.cep.metamodel.exception.InvalidStateMachineException;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationResult;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.Validator;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

/**
 * Objekte dieser Klasse stellen die Grundkomponente für das Complex Event
 * Processing dar. Sie sind als Operator in den physischen Ablaufplan
 * integrierbar und steuern die gesamte Verarbeitung von komplexen Events
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class CepOperator<R,W> extends AbstractPipe<R, W> {

	Logger logger = LoggerFactory.getLogger(CepOperator.class); 
	
	/**
	 * Factory zum erzeugen komplexer Events
	 */
	private IComplexEventFactory<R,W> complexEventFactory;
	/**
	 * Referenz auf eine eventReader Implementierung zum datenmodellunabhängigen
	 * Auslesen von Events.
	 */
	private Map<Integer, IEventReader<R,?>> eventReader = new HashMap<Integer, IEventReader<R,?>>(); 
	/**
	 * Referenz auf den Verzweigungsspeicher
	 */
	private BranchingBuffer<R> branchingBuffer;
	/**
	 * Liste aller Automaten-Instanzen, die gerade vom EPA verarbeitet werden
	 */
	private LinkedList<StateMachineInstance<R>> instances;
	/**
	 * Der Automat, der das zu suchende Event-Muster sowie die Event-Aggregation
	 * und die Struktur des Zwischenspeichers enthält
	 */
	private StateMachine stateMachine;

	/**
	 * leerer Standardkonstruktor
	 */
	public CepOperator() {
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
	public CepOperator(StateMachine stateMachine, Map<Integer, IEventReader<R,?>> eventReader,
			IComplexEventFactory<R,W> complexEventFactory, boolean validate)
			throws Exception {
		super();
		this.stateMachine = stateMachine;
		this.complexEventFactory = complexEventFactory;
		this.eventReader = eventReader;
		this.instances = new LinkedList<StateMachineInstance<R>>();
		this.branchingBuffer = new BranchingBuffer<R>();
		if (validate) {
			Validator validator = new Validator();
			ValidationResult result = validator.validate(stateMachine);
			if (!result.isValid()) {
				throw new InvalidStateMachineException(result);
			}
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	/**
	 * Verarbeitet ein übergebenes Event.
	 */
	@Override
	protected void process_next(R object, int port) {
		if (logger.isDebugEnabled()) logger.debug("Beginne Verarbeitung von Event " + object);
		if (logger.isDebugEnabled()) logger.debug(this.getStats());

		this.instances.add(new StateMachineInstance<R>(this.stateMachine));
		if (object == null)
			throw new InvalidEventException(
					"The event to be processed is null.");

		LinkedList<StateMachineInstance<R>> outdatedInstances = new LinkedList<StateMachineInstance<R>>();
		LinkedList<StateMachineInstance<R>> branchedInstances = new LinkedList<StateMachineInstance<R>>();

		// Auswerten der Transition:
		validateTransitions(object, outdatedInstances, branchedInstances, port);

		this.instances.addAll(branchedInstances);

		// Überprüfen auf Endzustände und erzeugen komplexer Events
		LinkedList<W> complexEvents = validateFinalStates(outdatedInstances, port);

		// Aufräumen: Alle als veraltet markierten Automateninstanzen entfernen.
		this.instances.removeAll(outdatedInstances);

		if (logger.isDebugEnabled()) logger.debug("Erzeugte Events: " + complexEvents);

		this.transfer(complexEvents);

		if (logger.isDebugEnabled()) logger.debug("Verarbeitung abgeschlossen\n");
	}

	private void validateTransitions(R object,
			LinkedList<StateMachineInstance<R>> outdatedInstances,
			LinkedList<StateMachineInstance<R>> branchedInstances, int port) {
	
		for (StateMachineInstance<R> instance: this.instances) {
			if (logger.isDebugEnabled()) logger.debug(instance.getStats());
			Stack<Transition> takeTransition = new Stack<Transition>();

			for (Transition transition: instance.getCurrentState().getTransitions()) {
				/*
				 * Über Variablen iterieren und neu belegen.
				 */
				updateVariables(object, instance, transition, port);
				try {
					if (transition.evaluate()){
						takeTransition.push(transition);
						if (logger.isDebugEnabled()) logger.debug("Transitionsbedingung ist true: "
								+ transition.getCondition().getLabel()
								+ " (Wert: "
								+ transition.getCondition().getValue()
								+ ", "
								+ transition.getCondition().getErrorInfo() + ")");

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
				if (logger.isDebugEnabled()) logger.debug("Keine Transition kann genommen werden. Verwerfe Instanz "
								+ instance);
				outdatedInstances.add(instance);
				this.branchingBuffer.removeBranch(instance);
			} else if (takeTransition.size() == 1) {
				// genau 1 Folgezustand: Zustand wechseln und Aktion ausführen.
				if (logger.isDebugEnabled()) logger.debug("Eine gehbare Transition gefunden: "
						+ takeTransition.peek());
				this.takeTransition(instance, takeTransition.pop(), object, port);
			} else if (takeTransition.size() > 1) {
				// mehr als 1 Folgezustand: nichtdeterministische Verzweigung!
				if (logger.isDebugEnabled()) logger
						.debug(""
								+ takeTransition.size()
								+ " Transitionen können genommen werden (Nichtdeterministische Verzweigung).");
				while (takeTransition.size() > 1) {
					StateMachineInstance<R> newInstance = instance.clone();
					this.takeTransition(newInstance, takeTransition.pop(),
							object, port);
					this.branchingBuffer.addBranch(instance, newInstance);
					branchedInstances.add(newInstance);
				}
				this.takeTransition(instance, takeTransition.pop(), object, port);
			}
		}
	}

	private void updateVariables(R object, 
								 StateMachineInstance<R> instance,
								 Transition transition, int port) {
		for (String varName: transition.getCondition().getVarNames()){				
			if (logger.isDebugEnabled()) logger.debug("Setze Variable " + varName);
			
			Object newValue = null;
			if (CepVariable.isActEventName(varName)) {
				// Variable bezieht sich auf aktuelles Event
				newValue = this.eventReader.get(port).getValue(CepVariable.getAttributeName(varName), object);	
			} else {
				// Variable bezieht sich auf historisches (bereits
				// konsumiertes Event)
				newValue = instance.getSymTab().getValue(varName);
			}
			transition.getCondition().setValue(varName, newValue);
			if (logger.isDebugEnabled()){
				logger.debug("Neuer Wert: "+newValue);
			}
		}
	}

	private LinkedList<W> validateFinalStates(
			LinkedList<StateMachineInstance<R>> outdatedInstances, int port) {
		LinkedList<W> complexEvents = new LinkedList<W>();
		for (Iterator<StateMachineInstance<R>> i = this.instances.iterator(); i
				.hasNext();) {
			StateMachineInstance<R> instance = i.next();

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
				for (IOutputSchemeEntry entry : this.stateMachine
						.getOutputScheme().getEntries()) {
	
					
					for (String varName:entry.getVarNames()){

						//Variable var = expr.getVar(varIt.next());
						/*
						 * 2 mögliche Fälle: 1. variablenwert steht in der
						 * symboltabelle 2. variablenwert steht in einem
						 * konsumierten Event
						 */
						Object value = instance.getSymTab().getValue(varName);
						if (value == null) {
							// variablenwert steht nicht in der symboltabelle
							// -> wert im MatchingTrace suchen (teuer?!)
							String[] split = varName.split(CepVariable.getSeperator());
							StateBuffer<R> buffer = instance.getMatchingTrace()
									.getStateBuffer(split[1]);
							if (buffer != null) {
								try {
									R event = null;
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
									value = this.eventReader.get(port).getValue(split[3],
											event);
								} catch (Exception e) {
									throw new UndeterminableVariableValueException(
											"'"
													+ varName
													+ "' not found in symbol table. Cannot find related event in state buffer."
													+ "");
								}
							} else {
								throw new UndeterminableVariableValueException(
										"'"
												+ varName
												+ "' not found in symbol table. Cannot find related state buffer '"
												+ split[0]
												+ "' in matching trace.");
							}
						}
						entry.setValue(varName,value);
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
		return complexEvents;
	}
	
	/**
	 * Liefert das Factory-Objekt für komplexe Events.
	 * 
	 * @return Das Factory-Objekt für komplexe Events.
	 */
	public IComplexEventFactory<R,W> getComplexEventFactory() {
		return complexEventFactory;
	}

	/**
	 * Setzt die Factory für die komplexen Events und legt damit gleichzeitig
	 * den Datentyp des Ausgabestroms fest.
	 * 
	 * @param complexEventFactory
	 *            Das neue Factory-Objekt für komplexe Events, nicht null.
	 */
	public void setComplexEventFactory(IComplexEventFactory<R,W> complexEventFactory) {
		this.complexEventFactory = complexEventFactory;
	}

	/**
	 * Liefert das Event-Reader-Objekt.
	 * 
	 * @return Das Event-Reader-Objekt.
	 */
	public Map<Integer, IEventReader<R,?>> getEventReader() {
		return eventReader;
	}

	/**
	 * Setzt das Event-Reader-Objekt und definiert damit gleichzeitig den
	 * Datentyp des Eingabestroms.
	 * 
	 * @param eventReader
	 *            Das neue Event-Reader-Objekt, nicht null.
	 */
	public void setEventReader(IEventReader<R,?> eventReader, int port) {
		this.eventReader.put(port, eventReader);
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
	public BranchingBuffer<R> getBranchingBuffer() {
		return branchingBuffer;
	}

	/**
	 * Liefert eine Liste, mit allen zur Zeit aktiven Automateninstanzen.
	 * 
	 * @return Liste der Automateninstanzen.
	 */
	public LinkedList<StateMachineInstance<R>> getInstances() {
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
	private LinkedList<StateMachineInstance<R>> getRemovableInstancesByConsumptionMode(
			StateMachineInstance<R> instance) {
		LinkedList<StateMachineInstance<R>> outdated = null;
		if (this.stateMachine.getConsumptionMode() == EConsumptionMode.onlyOneMatch) {
			outdated = this.branchingBuffer
					.getAllNestedStateMachineInstances(instance);
			this.branchingBuffer.removeAllNestedBranches(instance);
		} else if (this.stateMachine.getConsumptionMode() == EConsumptionMode.allMatches) {
			outdated = new LinkedList<StateMachineInstance<R>>();
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
	 * @param port 
	 */
	private void takeTransition(StateMachineInstance<R> instance,
			Transition transition, R event, int port) {
		if (logger.isDebugEnabled()) logger.debug("Zustandswechsel: " + instance.getCurrentState().getId()
				+ "-->" + transition.getNextState().getId());
		instance.setCurrentState(transition.getNextState());
		if (logger.isDebugEnabled()) logger.debug("Führe Aktion aus: " + transition.getAction());
		instance.executeAction(transition.getAction(), event, this.eventReader.get(port));
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





}
