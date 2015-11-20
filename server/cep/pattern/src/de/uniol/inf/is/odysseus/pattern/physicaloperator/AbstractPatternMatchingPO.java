package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidEventException;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;
import de.uniol.inf.is.odysseus.pattern.util.AttributeMap;
import de.uniol.inf.is.odysseus.pattern.util.EventBuffer;
import de.uniol.inf.is.odysseus.pattern.util.EventObject;
import de.uniol.inf.is.odysseus.pattern.util.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.util.PatternType;

/**
 * Abstrakter physischer Operator, der gemeinsam genutzte Methoden und Daten
 * f�rs Pattern-Matching kapselt.
 * 
 * @author Michael Falk
 * @param <T>
 */
public abstract class AbstractPatternMatchingPO<T extends ITimeInterval> extends
		AbstractPipe<Tuple<T>, Tuple<T>> implements IProcessInternal<Tuple<T>> {

	protected static Logger logger = LoggerFactory
			.getLogger(AbstractPatternMatchingPO.class);

	protected List<SDFExpression> assertions;
	protected List<SDFExpression> returnExpressions;

	protected PatternType type;
	protected Integer time;
	protected Integer size;
	protected TimeUnit timeUnit;
	protected PatternOutput outputMode;
	protected Integer inputPort;

	/**
	 * Die relevante Event-Typen-Liste.
	 */
	protected List<String> eventTypes;
	protected Map<Integer, String> inputTypeNames;
	protected Map<Integer, SDFSchema> inputSchemas;

	private boolean started;
	protected PointInTime startTime;
	private boolean timeElapsed;
	protected int countEvents;
	private boolean sizeMatched;

	protected IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea;
	protected ITransferArea<Tuple<T>, Tuple<T>> outputTransferArea;

	protected Map<SDFExpression, AttributeMap[]> attrMappings;
	protected Map<SDFExpression, AttributeMap[]> returnAttrMappings;

	public AbstractPatternMatchingPO(PatternType type, Integer time, Integer size,
			TimeUnit timeUnit, PatternOutput outputMode,
			List<String> eventTypes, List<SDFExpression> assertions,
			List<SDFExpression> returnExpressions,
			Map<Integer, String> inputTypeNames,
			Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea, Integer inputPort) {
		super();
		this.type = type;
		this.time = time;
		this.size = size;
		this.timeUnit = timeUnit;
		this.outputMode = outputMode;
		this.eventTypes = eventTypes;
		this.assertions = assertions;
		this.returnExpressions = returnExpressions;
		this.inputTypeNames = inputTypeNames;
		this.inputSchemas = inputSchemas;
		this.inputStreamSyncArea = inputStreamSyncArea;
		this.outputTransferArea = new TITransferArea<>();
		this.inputPort = inputPort;
		this.init();
	}

	// Copy-Konstruktor
	public AbstractPatternMatchingPO(AbstractPatternMatchingPO<T> patternPO) {
		this.type = patternPO.type;
		this.time = patternPO.time;
		this.size = patternPO.size;
		this.timeUnit = patternPO.timeUnit;
		this.outputMode = patternPO.outputMode;
		this.eventTypes = new ArrayList<String>();
		for (String eventType : patternPO.eventTypes)
			this.eventTypes.add(eventType);
		this.assertions = new ArrayList<SDFExpression>();
		for (SDFExpression expr : patternPO.assertions)
			this.assertions.add(expr);
		this.returnExpressions = new ArrayList<SDFExpression>();
		for (SDFExpression expr : patternPO.returnExpressions)
			this.returnExpressions.add(expr);
		this.inputTypeNames = new HashMap<Integer, String>();
		this.inputTypeNames.putAll(patternPO.inputTypeNames);
		this.inputSchemas = new HashMap<Integer, SDFSchema>();
		this.inputSchemas.putAll(patternPO.inputSchemas);
		this.inputStreamSyncArea = patternPO.inputStreamSyncArea.clone();
		this.outputTransferArea = patternPO.outputTransferArea.clone();
		this.attrMappings = new HashMap<SDFExpression, AttributeMap[]>();
		this.attrMappings.putAll(patternPO.attrMappings);
		this.returnAttrMappings = new HashMap<SDFExpression, AttributeMap[]>();
		this.returnAttrMappings.putAll(patternPO.returnAttrMappings);
		this.timeElapsed = patternPO.timeElapsed;
		this.countEvents = patternPO.countEvents;
		this.sizeMatched = patternPO.sizeMatched;
		this.inputPort = patternPO.inputPort;
		this.init();
	}

	private void init() {
		logger.info("Operator built.");
		// Defaultwerte setzen
		if (timeUnit == null) {
			timeUnit = TimeUnit.MILLISECONDS;
		}
		if (outputMode == null
				|| (outputMode == PatternOutput.EXPRESSIONS && returnExpressions == null)) {
			outputMode = PatternOutput.SIMPLE;
		}

		// Assertions intitialisieren
		if (assertions != null)
			attrMappings = initExpressions(assertions);
		if (outputMode == PatternOutput.EXPRESSIONS
				&& returnExpressions != null)
			returnAttrMappings = initExpressions(returnExpressions);

		// Zeit in richtiges Zeitformat konvertieren
		if (time != null) {
			time = (int) TimeUnit.MILLISECONDS.convert(time, timeUnit);
		}
	}

	/**
	 * Initialisiert Expressions.
	 * 
	 * @param expressions
	 * @return liefert eine Map, die einer Expression eine AttributeMap[]
	 *         zuordnet.
	 */
	private Map<SDFExpression, AttributeMap[]> initExpressions(
			List<SDFExpression> expressions) {
		Map<SDFExpression, AttributeMap[]> attrMappings = new HashMap<SDFExpression, AttributeMap[]>();
		if (expressions == null)
			return attrMappings;
		for (SDFExpression expression : expressions) {
			if (expression != null) {
				List<SDFAttribute> neededAttr = expression.getAllAttributes();
				AttributeMap[] attrMapping = new AttributeMap[neededAttr.size()];
				int i = 0;
				for (SDFAttribute attr : neededAttr) {
					Iterator<Integer> ports = inputSchemas.keySet().iterator();
					// passendes Schema raussuchen
					while (ports.hasNext()) {
						int port = ports.next();
						SDFSchema schema = inputSchemas.get(port);
						int attrPos = schema.indexOf(attr);
						if (attrPos != -1) {
							// Mapping speichern
							attrMapping[i] = new AttributeMap(port, attrPos);
							break;
						}
					}
					i++;
				}
				attrMappings.put(expression, attrMapping);
			}
		}
		return attrMappings;
	}

	@Override
	public String toString() {
		return super.toString() + " type: " + type + " eventTypes: "
				+ eventTypes.toString();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public void process_open() throws OpenFailedException {
		super.process_open();
		inputStreamSyncArea.init(this, getSubscribedToSource().size());
		outputTransferArea.init(this, getSubscribedToSource().size());
	}

	@Override
	protected void process_next(Tuple<T> event, int port) {
		inputStreamSyncArea.transfer(event, port);
		outputTransferArea.newElement(event, port);
	}

	@Override
	public void process_internal(Tuple<T> event, int port) {
		// Allgemeine �berpr�fungen
		if (inputTypeNames.get(port) == null) {
			throw new InvalidEventException("Der Datentyp des Events ist null!");
		}
		if (event == null) {
			throw new InvalidEventException("Das Event ist null!");
		}
		if (!started) {
			startTime = event.getMetadata().getStart();
			started = true;
		}
		if (time != null) {
			// Annahme: Zeiteinheit von PointInTime ist Millisekunden
			PointInTime currentTime = event.getMetadata().getStart();
			if (currentTime.minus(startTime).getMainPoint() >= time) {
				timeElapsed = true;
				startTime = currentTime;
			}
		}
		if (size != null) {
			if (eventTypes.contains(inputTypeNames.get(port))) {
				countEvents++;
				if (countEvents >= size) {
					sizeMatched = true;
					countEvents = 0;
				}
			}
		}
	}

	/**
	 * Check whether the time interval is over because of the time.
	 * 
	 * @return true, when timeElapsed
	 */
	protected boolean checkTimeElapsed() {
		if (timeElapsed) {
			// Zur�cksetzen
			timeElapsed = false;
			return true;
		}
		return false;
	}

	/**
	 * Check whether the interval is over because of the size.
	 * 
	 * @return true, when sizeMatched
	 */
	protected boolean checkSizeMatched() {
		if (sizeMatched) {
			// Zur�cksetzen
			sizeMatched = false;
			return true;
		}
		return false;
	}

	@Override
	public void process_punctuation_intern(IPunctuation punctuation, int port) {
		//inputStreamSyncArea.newElement(punctuation, port);
		outputTransferArea.sendPunctuation(punctuation, port);
	}

	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		// startTime speichern
		if (!started) {
			startTime = pointInTime.getTime();
			started = true;
		}
		if (time != null) {
			// Annahme: Zeiteinheit von PointInTime ist Millisekunden
			if (pointInTime.getTime().minus(startTime).getMainPoint() >= time) {
				timeElapsed = true;
				startTime = pointInTime.getTime();
			}
		}
	}

	/**
	 * Creates a complex event. The output depends on the output mode.
	 * 
	 * @param objectChoices Contains objects which are used to form the output with expressions.
	 * @param currentObj The current object.
	 * @param start time, the pattern was detected
	 * @return Ein komplexes Event oder null
	 */
	@SuppressWarnings("unchecked")
	protected Tuple<T> createComplexEvent(List<EventObject<T>> objectChoices,
			EventObject<T> currentObj, PointInTime start) {
		if (currentObj == null && objectChoices == null) {
			// e.g. absence pattern
			outputMode = PatternOutput.SIMPLE;
		}
		if (outputMode == PatternOutput.SIMPLE) {
			Object[] attributes = new Object[3];
			attributes[0] = type;
			if (currentObj != null) {
				attributes[1] = currentObj.getEvent().getMetadata().getStart()
						.getMainPoint();
			} else {
				attributes[1] = start.getMainPoint();
			}
			attributes[2] = true;
			Tuple<T> returnEvent = new Tuple<T>(attributes, false);
			if (currentObj != null) {
				returnEvent.setMetadata((T) currentObj.getEvent().getMetadata()
						.clone());
			} else {
				returnEvent.setMetadata((T) new TimeInterval(start));
			}
			return returnEvent;
		}
		if ((outputMode == PatternOutput.EXPRESSIONS) && (currentObj != null)) {
			Tuple<T> outputVal = new Tuple<T>(returnExpressions.size(), false);
			outputVal.setMetadata((T) currentObj.getEvent().getMetadata()
					.clone());
			for (int i = 0; i < returnExpressions.size(); i++) {
				SDFExpression expr = returnExpressions.get(i);
				Object[] values = findExpressionValues(objectChoices,
						returnAttrMappings.get(expr));
				if (values != null) {
//					expr.bindMetaAttribute(currentObj.getEvent().getMetadata());
//					expr.bindAdditionalContent(currentObj.getEvent()
//							.getAdditionalContent());
					expr.bindVariables(values);
					Object exprValue = expr.getValue();
					outputVal.setAttribute(i, exprValue);
				}
			}
			return outputVal;
		}
		if ((outputMode == PatternOutput.TUPLE_CONTAINER) && (currentObj != null)) {
			Tuple<T> complexEvent = new Tuple<T>(1, false);
			if (objectChoices != null && objectChoices.size() > 0) {
				// create tuple with matching set
				complexEvent.setMetadata((T) objectChoices.get(objectChoices.size()-1).getEvent().getMetadata().clone());
				List<Tuple<T>> tupleList = new ArrayList<>();
				for (EventObject<T> obj : objectChoices) {
					tupleList.add(obj.getEvent());
				}
				complexEvent.setAttribute(0, tupleList);
			} else {
				complexEvent.setMetadata((T) currentObj.getEvent().getMetadata().clone());
				complexEvent.setAttribute(0, currentObj.getEvent());
			}
			return complexEvent;
		}
		if (outputMode == PatternOutput.INPUT) {
			if (currentObj == null) {
				// return first object from inputport
				for (EventObject<T> obj : objectChoices) {
					if (obj.getPort() == inputPort) {
						return obj.getEvent();
					}
				}
			}
			if (currentObj != null) {
				return currentObj.getEvent();
			}
		}
		return null;
	}

	/**
	 * Creates a complex event. The complex event uses the given object for output.
	 * @param currentObj
	 * @return
	 */
	protected Tuple<T> createComplexEvent(EventObject<T> currentObj) {
		List<EventObject<T>> eventObjects = new ArrayList<>();
		eventObjects.add(currentObj);
		return createComplexEvent(eventObjects, currentObj, null);
	}
	
	/**
	 * Creates a complex event. The complex event uses start for the metadata.
	 * @param start time, the pattern was detected
	 * @return
	 */
	protected Tuple<T> createComplexEvent(PointInTime start) {
		return createComplexEvent(null, null, start);
	}

	protected boolean checkAssertion(EventObject<T> object,
			List<EventObject<T>> eventObjectSet,
			Entry<SDFExpression, AttributeMap[]> entry) {
		Object[] values = findExpressionValues(eventObjectSet, entry.getValue());
		SDFExpression expression = entry.getKey();
		if (values != null) {
//			expression.bindMetaAttribute(object.getEvent().getMetadata());
//			expression.bindAdditionalContent(object.getEvent()
//					.getAdditionalContent());
			expression.bindVariables(values);
		}

		Object result = expression.getValue();
		if (result != null) {
			boolean predicate = (boolean) result;
			if (!predicate) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks all assertions on one event object.
	 * @param event
	 * @return true, if all assertions are satisfied
	 */
	protected boolean checkAssertions(EventObject<T> event) {
		if (attrMappings == null) {
			// no assertions -> true
			return true;
		}
		Iterator<Entry<SDFExpression, AttributeMap[]>> iterator = attrMappings
				.entrySet().iterator();
		boolean satisfied = true;
		while (iterator.hasNext() && satisfied) {
			Entry<SDFExpression, AttributeMap[]> entry = iterator.next();
			satisfied = checkAssertion(event, Arrays.asList(event), entry);
		}
		return satisfied;
	}
	
	protected boolean checkAssertion(EventObject<T> object,
			Entry<SDFExpression, AttributeMap[]> entry) {
		List<EventObject<T>> eventObjectSet = new ArrayList<>();
		eventObjectSet.add(object);
		return checkAssertion(object, eventObjectSet, entry);
	}

	/**
	 * Pr�ft f�r eine Liste von Events, ob die Bedingungen erf�llt sind. Als
	 * Ausgabe liefert diese Methode eine Liste der Events zur�ck, die die
	 * Bedingungen erf�llen.
	 * 
	 * @param eventBuffer
	 * @return
	 */
	protected EventBuffer<T> calcSatisfiedEvents(EventBuffer<T> eventBuffer) {
		if (attrMappings == null)
			return eventBuffer;
		EventBuffer<T> output = new EventBuffer<T>();
		// Expressions f�r jedes Objekt �berpr�fen
		for (EventObject<T> event : eventBuffer) {
			if (checkAssertions(event)) {
				// MatchingSet in Ausgabemenge aufnehmen
				output.add(event);
			}
		}
		return output;
	}
	
	/**
	 * Sucht nach den Werten f�r die Attribute aus der Expression. Bei Erfolg
	 * werden die Werte zur�ckgeliefert, bei misserfolg null.
	 * 
	 * @param eventObjects
	 *            Liste mit Event-Objekten.
	 * @return values, null bei Misserfolg
	 */
	protected Object[] findExpressionValues(List<EventObject<T>> eventObjects,
			AttributeMap[] attrMapping) {
		Object[] values = new Object[attrMapping.length];
		for (int i = 0; i < attrMapping.length; i++) {
			EventObject<T> obj = null;
			Iterator<EventObject<T>> iterator = eventObjects.iterator();
			boolean detected = false;
			while (iterator.hasNext()) {
				obj = iterator.next();
				if (obj.getPort() == attrMapping[i].getPort()) {
					detected = true;
					break;
				}
			}
			if (obj != null && detected) {
				int attrPos = attrMapping[i].getAttrPos();
				values[i] = obj.getEvent().getAttribute(attrPos);
			} else {
				// es gibt kein Objekt mit dem geforderten Attribut
				return null;
			}
		}
		return values;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		process_punctuation_intern(punctuation, port);
	}


}
