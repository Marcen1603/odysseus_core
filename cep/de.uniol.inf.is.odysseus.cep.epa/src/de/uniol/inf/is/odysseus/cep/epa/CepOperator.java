package de.uniol.inf.is.odysseus.cep.epa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.IComplexEventFactory;
import de.uniol.inf.is.odysseus.cep.epa.eventreading.IEventReader;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.ConditionEvaluationException;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidEventException;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.IOutputSchemeEntry;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
import de.uniol.inf.is.odysseus.cep.metamodel.exception.InvalidStateMachineException;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationResult;
import de.uniol.inf.is.odysseus.cep.metamodel.validator.Validator;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.base.PointInTime;

/**
 * Objekte dieser Klasse stellen die Grundkomponente fuer das Complex Event
 * Processing dar. Sie sind als Operator in den physischen Ablaufplan
 * integrierbar und steuern die gesamte Verarbeitung von komplexen Events
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class CepOperator<R, W> extends AbstractPipe<R, W> {

	Logger logger = LoggerFactory.getLogger(CepOperator.class);

	/**
	 * Factory zum erzeugen komplexer Events
	 */
	private IComplexEventFactory<R, W> complexEventFactory;
	/**
	 * Referenz auf eine eventReader Implementierung zum
	 * datenmodellunabhaengigen Auslesen von Events.
	 */
	private Map<Integer, IEventReader<R, ?>> eventReader = new HashMap<Integer, IEventReader<R, ?>>();
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
	private StateMachine<R> stateMachine;

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
	 *            Gibt an, ob der übergebene Automat auf Fehler überprüft
	 *            werden soll. True, wenn nach Fehlern gesucht werden soll,
	 *            ansonsten false.
	 * @throws InvalidStateMachineException
	 *             Falls der übergebene Automat nicht die erforderlichen
	 *             Invarianten einhält.
	 */
	public CepOperator(StateMachine<R> stateMachine,
			Map<Integer, IEventReader<R, ?>> eventReader,
			IComplexEventFactory<R, W> complexEventFactory, boolean validate)
			throws Exception {
		super();
		this.stateMachine = stateMachine;
		this.complexEventFactory = complexEventFactory;
		this.eventReader = eventReader;
		this.instances = new LinkedList<StateMachineInstance<R>>();
		this.branchingBuffer = new BranchingBuffer<R>();
		if (validate) {
			Validator<R> validator = new Validator<R>();
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
	protected void process_next(R event, int port) {
		if (logger.isDebugEnabled())
			logger.debug("Start processing event " + event);
		if (logger.isDebugEnabled())
			logger.debug(this.getStats());

		this.instances.add(new StateMachineInstance<R>(this.stateMachine,
				getEventReader().get(port).getTime(event)));
		if (event == null)
			throw new InvalidEventException(
					"The event to be processed is null.");

		LinkedList<StateMachineInstance<R>> outdatedInstances = new LinkedList<StateMachineInstance<R>>();
		LinkedList<StateMachineInstance<R>> branchedInstances = new LinkedList<StateMachineInstance<R>>();

		validateTransitions(event, outdatedInstances, branchedInstances, port);
		this.instances.addAll(branchedInstances);
		LinkedList<W> complexEvents = validateFinalStates(outdatedInstances,
				port);
		this.instances.removeAll(outdatedInstances);

		if (complexEvents.size() > 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("Create Events: " + complexEvents);
			}
			this.transfer(complexEvents);
		}

	}

	private void validateTransitions(R event,
			LinkedList<StateMachineInstance<R>> outdatedInstances,
			LinkedList<StateMachineInstance<R>> branchedInstances, int port) {

		for (StateMachineInstance<R> instance : this.instances) {
			if (logger.isDebugEnabled())
				logger.debug(instance.getStats());
			Stack<Transition> takeTransition = new Stack<Transition>();
			boolean outofWindow = false;

			for (Transition transition : instance.getCurrentState()
					.getTransitions()) {
				// Terminate if out of Window
				if (outofWindow)
					break;
				// First check Type
				if (!transition.getCondition().checkEventType(
						eventReader.get(port).getType()))
					continue;
				// and Time
				if (stateMachine.getWindowSize() > 0) {
					if (!transition.getCondition().checkTime(
							instance.getStartTimestamp(),
							eventReader.get(port).getTime(event),
							stateMachine.getWindowSize())) {
						outofWindow = true;
						continue;
					}
				}
				/**
				 * update variables /* Über Variablen iterieren und neu
				 * belegen. Achtung! Sobald eine Variabel nicht belegbar ist,
				 * braucht (darf) die Transition nicht ausgewertet werden (kann
				 * also nie true sein)
				 */
				if (updateVariables(event, instance, transition, port)) {
					try {
						if (transition.evaluate()) {
							takeTransition.push(transition);
							if (logger.isDebugEnabled())
								logger.debug("TRUE: "
										+ transition.getCondition().getLabel());

						}
					} catch (Exception e) {
						// System.out.println(transition.getCondition().getLabel());
						// System.out.println(transition.getCondition().getExpression().getErrorInfo());
						// e.printStackTrace();
						throw new ConditionEvaluationException(
								"Cannot evaluate condition "
										+ transition.getCondition(), e);
					}
				}
			}
			if (takeTransition.isEmpty()) {
				/*
				 * Kein Zustandswechsel möglich: Automateninstanz als
				 * entfernbar makieren. Die Instanz muss auch aus dem
				 * Verzweigungsspeicher entfernt werden um Speicherleichen zu
				 * verhindern.
				 */
				if (logger.isDebugEnabled()) {
					if (outofWindow) {
						logger.debug("Instance " + instance + " out of window");
					} else {
						logger
								.debug("No available transition. Remove instance "
										+ instance);
					}
				}
				outdatedInstances.add(instance);
				this.branchingBuffer.removeBranch(instance);
			} else if (takeTransition.size() == 1) {
				// one next state: Change state, execute action
				// if (logger.isDebugEnabled())
				// logger.debug("One transition found: "
				// + takeTransition.peek());
				this
						.takeTransition(instance, takeTransition.pop(), event,
								port);
			} else if (takeTransition.size() > 1) {
				// mehr als 1 Folgezustand: nichtdeterministische Verzweigung!
				if (logger.isDebugEnabled())
					logger
							.debug(""
									+ takeTransition.size()
									+ "  transitions found (nondeterministic branching).");
				while (takeTransition.size() > 1) {
					StateMachineInstance<R> newInstance = instance.clone();
					this.takeTransition(newInstance, takeTransition.pop(),
							event, port);
					this.branchingBuffer.addBranch(instance, newInstance);
					branchedInstances.add(newInstance);
				}
				// Warum dahinter? K�nnte das nicht auch als erstes gemacht
				// werden?
				// Dann kann man sich den Fall takeTransition.size()==1 sparen
				// ...
				this
						.takeTransition(instance, takeTransition.pop(), event,
								port);
			}
		}
	}

	private boolean updateVariables(R object, StateMachineInstance<R> instance,
			Transition transition, int port) {
		for (String varName : transition.getCondition().getVarNames()) {
			String name = CepVariable.getAttributeName(varName);

			Object newValue = null;
			if (CepVariable.isActEventName(varName)) {
				newValue = this.eventReader.get(port).getValue(name, object);
				if (newValue == null) {
					return false;
				}
			} else { // historic
				newValue = getValue(port, instance, varName);
			}
			transition.getCondition().setValue(varName, newValue);
			if (logger.isDebugEnabled()) {
				logger.debug(varName + " = " + newValue + " from (" + name
						+ ") " + object);
			}
		}
		return true;
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
			 * Instanzen kann eventuell zu fehlerhaftem Verhalten führen. Um
			 * das zu verhindern, müssen diese Instanzen bei der Verarbeitung
			 * übersprungen werden.
			 */
			if (outdatedInstances.contains(instance))
				continue;

			if (instance.getCurrentState().isAccepting()) {
				if (logger.isDebugEnabled()) {
					logger.debug("Reached final state in " + instance);
				}
				// Werte in den Symboltabellen der JEP-Ausdrücke im
				// Ausgabeschema setzen:
				for (IOutputSchemeEntry entry : this.stateMachine
						.getOutputScheme().getEntries()) {

					for (String varName : entry.getVarNames()) {
						Object value = getValue(-1, instance, varName);
						entry.setValue(varName, value);
					}
				}
				complexEvents.add(this.complexEventFactory.createComplexEvent(
						this.stateMachine.getOutputScheme(), 
						instance.getMatchingTrace(), 
						instance.getSymTab(), 
						new PointInTime(getEventReader().get(port).getTime(instance.getMatchingTrace().getLastEvent().getEvent()),0)));
				/*
				 * An dieser Stelle muss die Instanz, die zum Complex Event
				 * geführt hat, als veraltet markiert werden. Je nach
				 * Consumption-Mode müssen auch die verwandten Instanzen als
				 * veraltet markiert werden.
				 */
				// Hmmm Dont know
				// outdatedInstances.addAll(this
				// .getRemovableInstancesByConsumptionMode(instance));
				// better this?
				outdatedInstances.add(instance);
			}
		}
		return complexEvents;
	}

	private Object getValue(int port, StateMachineInstance<R> instance,
			String varName) {
		/*
		 * Two Cases: Var is in symbol table Var is from consumed event
		 */

		Object value = instance.getSymTab().getValue(varName);
		if (value == null) {
			String[] split = varName.split(CepVariable.getSeperator());
			int index = split[2].isEmpty() ? -1 : Integer.parseInt(split[2]);
			MatchedEvent<R> event = instance.getMatchingTrace().getEvent(
					split[1], index);
			if (event != null) {
				IEventReader<R, ?> eventR = this.eventReader.get(port);
				if (port > 0) {
					eventR = this.eventReader.get(port);
				} else {
					// For final Results ... find Event-Reader
					String type = stateMachine.getState(split[1]).getType();
					for (IEventReader<R, ?> r : eventReader.values()) {
						if (r.getType().equals(type)) {
							eventR = r;
							break;
						}
					}
				}
				value = eventR.getValue(split[3], event.getEvent());
			}
		}
		return value;
	}

	/**
	 * Liefert das Factory-Objekt für komplexe Events.
	 * 
	 * @return Das Factory-Objekt für komplexe Events.
	 */
	public IComplexEventFactory<R, W> getComplexEventFactory() {
		return complexEventFactory;
	}

	/**
	 * Setzt die Factory für die komplexen Events und legt damit gleichzeitig
	 * den Datentyp des Ausgabestroms fest.
	 * 
	 * @param complexEventFactory
	 *            Das neue Factory-Objekt für komplexe Events, nicht null.
	 */
	public void setComplexEventFactory(
			IComplexEventFactory<R, W> complexEventFactory) {
		this.complexEventFactory = complexEventFactory;
	}

	/**
	 * Liefert das Event-Reader-Objekt.
	 * 
	 * @return Das Event-Reader-Objekt.
	 */
	public Map<Integer, IEventReader<R, ?>> getEventReader() {
		return eventReader;
	}

	/**
	 * Setzt das Event-Reader-Objekt und definiert damit gleichzeitig den
	 * Datentyp des Eingabestroms.
	 * 
	 * @param eventReader
	 *            Das neue Event-Reader-Objekt, nicht null.
	 */
	public void setEventReader(IEventReader<R, ?> eventReader, int port) {
		this.eventReader.put(port, eventReader);
	}

	/**
	 * Liefert den Automaten, der das zu suchende Event-Muster definiert.
	 * 
	 * @return Den Automaten, der das zu suchende Event-Muster definiert.
	 */
	protected StateMachine<R> getStateMachine() {
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
	 * ????
	 * 
	 * Entfernt die übergebene Automateninstanz aus dem {@link BranchingBuffer}
	 * . Ist der Consumption-Mode onlyOneMatch, so werden zusätzlich alle
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
	 *         Abhängigkeit vom Consumption Mode enthält. Die übergebene
	 *         Instanz instance ist immer in der Liste enthalten.
	 */
	// private LinkedList<StateMachineInstance<R>>
	// getRemovableInstancesByConsumptionMode(
	// StateMachineInstance<R> instance) {
	// LinkedList<StateMachineInstance<R>> outdated = null;
	// if (this.stateMachine.getConsumptionMode() ==
	// EConsumptionMode.onlyOneMatch) {
	// outdated = this.branchingBuffer
	// .getAllNestedStateMachineInstances(instance);
	// this.branchingBuffer.removeAllNestedBranches(instance);
	// } else if (this.stateMachine.getConsumptionMode() ==
	// EConsumptionMode.allMatches) {
	// outdated = new LinkedList<StateMachineInstance<R>>();
	// outdated.add(instance);
	// this.branchingBuffer.removeBranch(instance);
	// } else {
	// throw new UndefinedConsumptionModeException(
	// "Undefined consumption mode: "
	// + this.stateMachine.getConsumptionMode());
	// }
	// return outdated;
	// }

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
		if (logger.isDebugEnabled()) {
			logger.debug(instance.toString() + " Fire: " + transition.getId()
					+ " " + "Execute action: " + transition.getAction());
		}

		instance.executeAction(transition.getAction(), event, this.eventReader
				.get(port));

		// TODO: getauscht ... Auswirkungen (s.u.)??
		instance.setCurrentState(transition.getNextState());

		if (logger.isDebugEnabled()) {
			logger.debug("--> " + instance.getStats());
		}
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
	 * Liefert Statistiken zum aktuellen Zustand des EPA.
	 * 
	 * @return
	 */
	public String getStats() {
		String str = "";
		str = str + "#instances:" + this.instances.size() + " ";
		str = str + "#branch trees:"
				+ this.branchingBuffer.getBranches().size();
		return str;
	}

	@Override
	public CepOperator<R, W> clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
