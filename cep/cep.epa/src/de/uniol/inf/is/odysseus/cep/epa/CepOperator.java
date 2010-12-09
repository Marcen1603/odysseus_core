package de.uniol.inf.is.odysseus.cep.epa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTable;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.physicaloperator.IProcessInternal;
import de.uniol.inf.is.odysseus.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;

/**
 * Objekte dieser Klasse stellen die Grundkomponente fuer das Complex Event
 * Processing dar. Sie sind als Operator in den physischen Ablaufplan
 * integrierbar und steuern die gesamte Verarbeitung von komplexen Events
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
public class CepOperator<R extends IMetaAttributeContainer<? extends ITimeInterval>, W extends IMetaAttributeContainer<?>>
		extends AbstractPipe<R, W> implements IProcessInternal<R> {

	Logger logger = LoggerFactory.getLogger(CepOperator.class);

	/**
	 * Factory zum erzeugen komplexer Events
	 */
	private IComplexEventFactory<R, W> complexEventFactory;
	/**
	 * Referenz auf eine eventReader Implementierung zum
	 * datenmodellunabhaengigen Auslesen von Events.
	 */
	private Map<Integer, IEventReader<R, R>> eventReader = new HashMap<Integer, IEventReader<R, R>>();

	/**
	 * Transferfunktion for reading and writing Elements
	 */
	protected IInputStreamSyncArea<R> inputStreamSyncArea;
	protected ITransferArea<R, W> outputTransferFunction;

	// removed for performance improvements
	// /**
	// * Referenz auf den Verzweigungsspeicher
	// */
	// private BranchingBuffer<R> branchingBuffer;
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
	 * Referenz auf die globale, automaten�bergreifende Symboltabelle
	 */
	//private SymbolTable<R> symTab;

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
			Map<Integer, IEventReader<R, R>> eventReader,
			IComplexEventFactory<R, W> complexEventFactory, boolean validate,
			IInputStreamSyncArea<R> inputStreamSyncArea,
			ITransferArea<R, W> outputTransferFunction) throws Exception {
		super();
		this.stateMachine = stateMachine;
		this.complexEventFactory = complexEventFactory;
		this.eventReader = eventReader;
		this.instances = new LinkedList<StateMachineInstance<R>>();
		// this.branchingBuffer = new BranchingBuffer<R>();
		this.inputStreamSyncArea = inputStreamSyncArea;
		this.outputTransferFunction = outputTransferFunction;
	}

	public CepOperator(CepOperator<R, W> cepOperator) {
		throw new RuntimeException(this.getClass()
				+ " Copy Constructor not yet implemented");
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		inputStreamSyncArea.init(this);
		outputTransferFunction.init(this);
	}

	/**
	 * Verarbeitet ein übergebenes Event.
	 */
	@Override
	protected void process_next(R event, int port) {
		// if (logger.isDebugEnabled())
		// logger.debug("read "+ event + " "+port);
		// insertIntoInputBuffer(event, port);
		inputStreamSyncArea.newElement(event, port);
		outputTransferFunction.newElement(event, port);
	}

	@Override
	public void process_internal(R event, int port) {
		synchronized (instances) {
			if (logger.isDebugEnabled())
				logger.debug("-------------------> NEXT EVENT from "
						+ eventReader.get(port).getType() + ": " + event + " "
						+ port);
			// if (logger.isDebugEnabled())
			// logger.debug(this.getStats());

			// Bevor ueberhaupt eine Instanz angelegt wird, testen, ob
			// mindestens
			// die Typbedingung erfuellt ist
			boolean createNewInstance = false;
			for (Transition transition : this.stateMachine.getInitialState()
					.getTransitions()) {

				if (transition.getCondition().checkEventTypeWithPort(port)) {
					createNewInstance = true;
					break;
				}
			}
			LinkedList<W> complexEvents = null;
			if (createNewInstance) {
				logger.debug("Created New Initial Instance");
				this.instances.add(new StateMachineInstance<R>(
						this.stateMachine, getEventReader().get(port).getTime(
								event)));
			}
			if (event == null)
				throw new InvalidEventException(
						"The event to be processed is null.");

			LinkedList<StateMachineInstance<R>> outdatedInstances = new LinkedList<StateMachineInstance<R>>();
			LinkedList<StateMachineInstance<R>> branchedInstances = new LinkedList<StateMachineInstance<R>>();
			validateTransitions(event, outdatedInstances, branchedInstances,
					port);
			this.instances.addAll(branchedInstances);
			complexEvents = validateFinalStates(outdatedInstances, port);
			this.instances.removeAll(outdatedInstances);

			if (complexEvents.size() > 0) {
				for (W e : complexEvents) {
					if (logger.isDebugEnabled()) {
						logger.debug("Created Event: " + e);
					}
					outputTransferFunction.transfer(e);
				}

			}
		}
	}

	private void validateTransitions(R event,
			LinkedList<StateMachineInstance<R>> outdatedInstances,
			LinkedList<StateMachineInstance<R>> branchedInstances, int port) {

		for (StateMachineInstance<R> instance : this.instances) {
			if (logger.isDebugEnabled())
				logger.debug("Validating " + instance);
			List<Transition> transitionsToTake = new ArrayList<Transition>();
			boolean outofWindow = false;

			for (Transition transition : instance.getCurrentState()
					.getTransitions()) {
				// logger.debug("Evaluating: " + transition + " on event " +
				// event);
				// Terminate if out of Window
				if (outofWindow) {
					break;
				}

				// Check Time
				if (stateMachine.getWindowSize() > 0) {
					if (!transition.getCondition().checkTime(
							instance.getStartTimestamp(),
							eventReader.get(port).getTime(event),
							stateMachine.getWindowSize())) {
						outofWindow = true;
						// logger.debug(instance + " Out of Window ...");
						continue;
					}
				}
				/**
				 * update variables
				 */
				try {
					boolean allVarSet = updateVariables(event, instance,
							transition, port);
					// take the transition if all variables are set and the
					// condition evaluates to true
					// OR: if not all variables are set (== condition cannot be
					// evaluated --> false) and
					// the condition is negated (e.g. in _ignore transitions)
					if ((!allVarSet && transition.getCondition().isNegate())
							|| (allVarSet && transition.evaluate(port))) {
						transitionsToTake.add(transition);
						// if (logger.isDebugEnabled())
						// logger.debug(instance + " Transition true: "
						// + transition.getCondition().getLabel());
						//
						// }
						// else {
						// logger.debug(instance + " Transition false: "
						// + transition.getCondition().getLabel());
					}

				} catch (Exception e) {
					// System.out.println(transition.getCondition().getLabel());
					e.printStackTrace();
					throw new ConditionEvaluationException(
							"Cannot evaluate condition "
									+ transition.getCondition(), e);
				}
			} // for (Transition transition...)
			if (transitionsToTake.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.debug("No transition on " + instance);

				// no transition on this instance, mark for removal
				outdatedInstances.add(instance);
				// this.branchingBuffer.removeBranch(instance);
			} else {
				if (transitionsToTake.size() == 1) {
					// execute transition
					instance.takeTransition(transitionsToTake.remove(0), event,
							eventReader.get(port));
				} else {
					// execute possible further transitions on new instances
					// because it must be cloned from current instance,
					// make takeTransition on current instance last
					while (transitionsToTake.size() > 1) {
						StateMachineInstance<R> newInstance = instance.clone();
						Transition toTake = transitionsToTake.remove(0);
						newInstance.takeTransition(toTake, event,
								eventReader.get(port));
						// this.branchingBuffer.addBranch(instance,
						// newInstance);
						branchedInstances.add(newInstance);
					}
					// Now its save to update current Transition
					instance.takeTransition(transitionsToTake.remove(0), event,
							eventReader.get(port));
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("After Taking Transitions " + instance);
				for (StateMachineInstance s : branchedInstances) {
					logger.debug("Branched Instances " + s);
				}
			}
		}
	}

	// update Variables in transition. If no values is found for at least one
	// variable return false
	// if all variables set return true
	private boolean updateVariables(R object, StateMachineInstance<R> instance,
			Transition transition, int port) {
		// logger.debug("Update Variables in "+transition.getCondition()+" --> "+transition.getCondition().getVarNames());
		for (CepVariable varName : transition.getCondition().getVarNames()) {

			//logger.debug("Setting Value for "+varName);

			Object newValue = null;
			if (varName.isActEventName()) {
				newValue = this.eventReader.get(port).getValue(
						varName.getVariableName(), object);

//				 logger.debug("Setze " + varName + " auf " + newValue +
//				 " from " + object);

				if (newValue == null) {
					return false;
				}
			} else { // historic
				newValue = getValue(port, instance, varName);
			}
			// Set Value in Expression to evaluate
			transition.getCondition().setValue(varName, newValue);
			// if (logger.isDebugEnabled()) {
			// logger.debug(varName + " = " + newValue + " from " + object);
			// }
		}
		return true;
	}

	private LinkedList<W> validateFinalStates(
			LinkedList<StateMachineInstance<R>> outdatedInstances, int port) {
		LinkedList<W> complexEvents = new LinkedList<W>();
		for (StateMachineInstance<R> instance : instances) {
			// if (logger.isDebugEnabled()) {
			// logger.debug("Testing for final state in " + instance);
			// }

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

					for (CepVariable varName : entry.getVarNames()) {
						Object value = getValue(-1, instance, varName);
						if (value != null){
							entry.setValue(varName, value);
						}else{
							logger.warn("Variable "+varName+" has no value!");
						}
					}
				}
				complexEvents.add(this.complexEventFactory.createComplexEvent(
						this.stateMachine.getOutputScheme(),
						instance.getMatchingTrace(),
						instance.getSymTab(),
						new PointInTime(getEventReader().get(port).getTime(
								instance.getMatchingTrace().getLastEvent()
										.getEvent()))));
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
			CepVariable varName) {
		/*
		 * Two Cases: Var is in symbol table Var is from consumed event
		 */

		Object value = instance.getSymTab().getValue(varName);
		if (value == null) {
			// String[] split = varName.split(CepVariable.getSeperator());
			// int index = split[2].isEmpty() ? -1 : Integer.parseInt(split[2]);
			MatchedEvent<R> event = instance.getMatchingTrace().getEvent(
					varName.getStateIdentifier() , varName.getIndex());
			if (event != null) {
				IEventReader<R, ?> eventR = this.eventReader.get(port);
				if (port > 0) {
					eventR = this.eventReader.get(port);
				} else {
					// For final Results ... find Event-Reader
					String type = stateMachine.getState(
							varName.getStateIdentifier()).getType();
					for (IEventReader<R, ?> r : eventReader.values()) {
						if (r.getType().equals(type)) {
							eventR = r;
							break;
						}
					}
				}
				value = eventR.getValue(varName.getAttribute(),
						event.getEvent());
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
	public Map<Integer, IEventReader<R, R>> getEventReader() {
		return eventReader;
	}

	/**
	 * Setzt das Event-Reader-Objekt und definiert damit gleichzeitig den
	 * Datentyp des Eingabestroms.
	 * 
	 * @param eventReader
	 *            Das neue Event-Reader-Objekt, nicht null.
	 */
	public void setEventReader(IEventReader<R, R> eventReader, int port) {
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

	// /**
	// * Liefert den Verzweigungsspeicher des EPA zurück.
	// *
	// * @return Der aktuelle Verzweigungsspeicher.
	// */
	// public BranchingBuffer<R> getBranchingBuffer() {
	// return branchingBuffer;
	// }

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
	 * Liefert Statistiken zum aktuellen Zustand des EPA.
	 * 
	 * @return
	 */
	public String getStats() {
		String str = "";
		str = str + "#instances:" + this.instances.size() + " ";
		// str = str + "#branch trees:"
		// + this.branchingBuffer.getBranches().size();
		return str;
	}

	@Override
	public CepOperator<R, W> clone() {
		return new CepOperator(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}

}
