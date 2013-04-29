package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import model.AttributeMap;
import model.EventBuffer;
import model.EventObject;
import model.PatternOutput;
import model.PatternType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidEventException;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;

/**
 * Operator, um Pattern-Matching durchzuführen.
 * @author Michael Falk
 * @param <T>
 */
public class PatternMatchingPO<T extends ITimeInterval> extends AbstractPipe<Tuple<T>, Tuple<T>>
	implements IProcessInternal<Tuple<T>> {
	
	private static Logger logger = LoggerFactory.getLogger(PatternMatchingPO.class);
	
	private List<SDFExpression> assertions;
	private List<SDFExpression> returnExpressions;

	private PatternType type;
	private Integer time;
	private Integer size;
	private TimeUnit timeUnit;
	private PatternOutput outputMode;
	
	/**
	 * Die relevante Event-Typen-Liste.
	 */
	private List<String> eventTypes;
	private Map<Integer, String> inputTypeNames;
	private Map<Integer, SDFSchema> inputSchemas;
	
	protected IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea;
	protected ITransferArea<Tuple<T>, Tuple<T>> outputTransferArea;
	
	/**
	 * Port-Map, die für jeden Port eine Liste mit Event-Objekten enthält.
	 */
	private Map<Integer, EventBuffer<T>> objectLists;
	private Map<SDFExpression, AttributeMap[]> attrMappings;
	private Map<SDFExpression, AttributeMap[]> returnAttrMappings;
	
	public PatternMatchingPO(PatternType type, Integer time, Integer size, TimeUnit timeUnit, PatternOutput outputMode, List<String> eventTypes,
			List<SDFExpression> assertions, List<SDFExpression> returnExpressions, Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas,
			IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea) {
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
        this.objectLists = new HashMap<Integer, EventBuffer<T>>();
        // create lists for every port that is in the eventTypes list
        for (Integer port : this.inputTypeNames.keySet()) {
        	if (this.eventTypes.contains(this.inputTypeNames.get(port))) {
        		EventBuffer<T> buffer = new EventBuffer<T>();
            	this.objectLists.put(port, buffer);
        	}
        }
        this.outputTransferArea = new TITransferArea<>();
        logger.info(eventTypes.get(0) + " " + eventTypes.get(1));
        this.init();
    }
	
	// Copy-Konstruktor
    public PatternMatchingPO(PatternMatchingPO<T> patternPO) {
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
        this.objectLists = new HashMap<Integer, EventBuffer<T>>();
        this.objectLists.putAll(patternPO.objectLists);
        this.attrMappings = new HashMap<SDFExpression, AttributeMap[]>();
        this.attrMappings.putAll(patternPO.attrMappings);
        this.returnAttrMappings = new HashMap<SDFExpression, AttributeMap[]>();
        this.returnAttrMappings.putAll(patternPO.returnAttrMappings);
        this.init();
    }
	
    private void init() {
    	// Defaultwerte setzen
    	if (timeUnit == null) {
    		timeUnit = TimeUnit.MILLISECONDS;
    	}
    	if (outputMode == null || (outputMode == PatternOutput.EXPRESSIONS && returnExpressions == null)) {
    		outputMode = PatternOutput.SIMPLE;
    	}
    	
    	// Assertions intitialisieren
    	if (assertions != null)
    		attrMappings = initExpressions(assertions);
    	if (outputMode == PatternOutput.EXPRESSIONS && returnExpressions != null)
    		returnAttrMappings = initExpressions(returnExpressions);
    	
    	// Zeit in richtiges Zeitformat konvertieren
    	if (time != null) {
    		time = (int) TimeUnit.MILLISECONDS.convert(time, timeUnit);
    	}
    }
    
    /**
     * Initialisiert Expressions.
     * @param expressions
     * @return liefert eine Map, die einer Expression eine AttributeMap[] zuordnet.
     */
    private Map<SDFExpression, AttributeMap[]> initExpressions(List<SDFExpression> expressions) {
    	Map<SDFExpression, AttributeMap[]> attrMappings = new HashMap<SDFExpression, AttributeMap[]>();
    	if (expressions == null) return attrMappings;
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
	public String toString(){
		return super.toString() + " type: " + type + " eventTypes: " + eventTypes.toString(); 
	}
    
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public PatternMatchingPO<T> clone() {
		return new PatternMatchingPO<T>(this);
	}
	
	@Override
    public void process_open() throws OpenFailedException {
        super.process_open();
        inputStreamSyncArea.init(this);
        outputTransferArea.init(this);
    }
     
	
	@Override
	protected void process_next(Tuple<T> event, int port) {
		inputStreamSyncArea.newElement(event, port);
		outputTransferArea.newElement(event, port);
	}

	@Override
	public void process_internal(Tuple<T> event, int port) {
		String eventType = inputTypeNames.get(port);
		SDFSchema schema = inputSchemas.get(port);
		EventObject<T> eventObj = new EventObject<T>(event, eventType, schema, port);
		if (eventType == null) {
			throw new InvalidEventException("Der Datentyp des Events ist null!");
		}
		switch(type) {
			// Logische Pattern
			case ANY:
				if (eventTypes.contains(eventType)) {
					// Assertions überprüfen
					int index = eventTypes.indexOf(eventType);
					SDFExpression assertion = assertions.get(index);
					Entry<SDFExpression, AttributeMap[]> entry = new SimpleEntry<>(assertion, attrMappings.get(assertion));
					
					boolean satisfied = checkAssertion(eventObj, entry);
					if (satisfied) {
						Tuple<T> complexEvent = this.createComplexEvent(eventObj, type);
						outputTransferArea.transfer(complexEvent);
					}
				}
				// ANY-Pattern erkannt
				this.transfer(this.createComplexEvent(null, null, type));
				break;
			case ALL:
				if (eventTypes.contains(eventType)) {
					// Event einsortieren und alte Events entfernen
					objectLists.get(port).add(eventObj);
					dropOldEvents(eventObj);
					// Kombinationen suchen und Bedingungen überprüfen
					List<List<EventObject<T>>> output = checkAssertions(eventObj, computeCrossProduct(eventObj), type);
					for (List<EventObject<T>> outputObject : output) {
						Tuple<T> complexEvent = this.createComplexEvent(outputObject, eventObj, type);
						outputTransferArea.transfer(complexEvent);
					}
				}
			case ABSENCE:
			// Threshold Pattern
			case COUNT:
			case VALUE_MAX:
			case VALUE_MIN:
			case FUNCTOR:
			// Subset Selection Pattern
			case RELATIVE_N_HIGHEST:
			case RELATIVE_N_LOWEST:
			// Modale Pattern
			case ALWAYS:
			case SOMETIMES:
			// Temporal Order Pattern
			case FIRST_N_PATTERN:
			case LAST_N_PATTERN:
		}	

	}

	@Override
	public void process_punctuation_intern(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);	
	}

	@Override
	public void process_newHeartbeat(Heartbeat pointInTime) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Erzeugt ein komplexes Event abhängig vom PatternOutput.
	 * @param outputObjects
	 * @param currentObj
	 * @param type
	 * @return Ein komplexes Event oder null
	 */
	@SuppressWarnings("unchecked")
	private Tuple<T> createComplexEvent(List<EventObject<T>> outputObjects, EventObject<T> currentObj, PatternType type) {
		if (outputMode == PatternOutput.SIMPLE) {
			Object[] attributes = new Object[2];
			attributes[0] = type;
			attributes[1] = true;
			Tuple<T> returnEvent = new Tuple<T>(attributes, false);
			return returnEvent;
		}
		if (outputMode == PatternOutput.EXPRESSIONS) {
			Tuple<T> outputVal = new Tuple<T>(returnExpressions.size(), false);
			outputVal.setMetadata((T) currentObj.getEvent().getMetadata().clone());
			for (int i = 0; i < returnExpressions.size(); i++) {
				SDFExpression expr = returnExpressions.get(i);
				Object[] values = findExpressionValues(outputObjects, returnAttrMappings.get(expr));
				if (values != null) {
					expr.bindMetaAttribute(currentObj.getEvent().getMetadata());
					expr.bindAdditionalContent(currentObj.getEvent().getAdditionalContent());
					expr.bindVariables(values);
					Object exprValue = expr.getValue();
					outputVal.setAttribute(i, exprValue);
				}
			}
			return outputVal;
		}
		return null;
	}
	
	private Tuple<T> createComplexEvent(EventObject<T> currentObj, PatternType type) {
		List<EventObject<T>> eventObjects = new ArrayList<>();
		return createComplexEvent(eventObjects, currentObj, type);
	}
	
	private void dropOldEvents(EventObject<T> event) {
		EventBuffer<T> currentEvents = objectLists.get(event.getPort());
		// Anzahl Elemente berücksichtigen
		if (size != null) {
			while (currentEvents.getSize() > size) {
				currentEvents.removeFirst();
			}
		}
		// Zeit berücksichtigen
		if (time != null) {
			PointInTime newStartTime = event.getEvent().getMetadata().getStart();
			for (EventBuffer<T> eventBuffer : objectLists.values()) {
				// alte Elemente löschen
				Iterator<EventObject<T>> iterator = eventBuffer.getEventObjects().iterator();
				while (iterator.hasNext()) {
					EventObject<T> currEvent = iterator.next();
					PointInTime currStartTime = currEvent.getEvent().getMetadata().getStart();
					// Annahme: Zeiteinheit von PointInTime ist Millisekunden 
					if (newStartTime.minus(currStartTime).getMainPoint() > time) {
						iterator.remove();
					}
				}
			}
		}
	}
	
	/**
	 * Berechnet das Kreuzprodukt von dem Event, das reingekommen ist und den anderen gespeicherten
	 * Events, die einen anderen Eventtyp haben.
	 * @param object
	 * @return
	 */
	private Set<List<EventObject<T>>> computeCrossProduct(EventObject<T> object) {
		// Alle EventBuffer außer den des empfangenen Events auswählen
		List<EventBuffer<T>> relevantEventBuffer = new ArrayList<EventBuffer<T>>();
		for (Entry<Integer, EventBuffer<T>> eventBuffer : objectLists.entrySet()) {
			if (!eventBuffer.getKey().equals(object.getPort())) {
				relevantEventBuffer.add(eventBuffer.getValue());
			}
		}
		
		// Kreuzprodukt berechnen
		List<Set<EventObject<T>>> relevantSets = new ArrayList<Set<EventObject<T>>>();
		for (EventBuffer<T> objects : relevantEventBuffer) {
			relevantSets.add(objects.toSet());
		}
		// Neues Event auch berücksichtigen
		Set<EventObject<T>> objSet = new HashSet<EventObject<T>>();
		objSet.add(object);
		relevantSets.add(objSet);
		return Sets.cartesianProduct(relevantSets);
	}
	
	/**
	 * Prüft für jede gültige Kombination (-> ALL-Pattern), ob alle Bedingungen erfüllt sind.
	 * Als Ausgabe liefert diese Methode eine Liste der Kombinationen zurück,
	 * für die die Bedingungen zutreffen.
	 * @param object
	 * @param eventObjectSets
	 * @param type
	 * @return
	 */
	private List<List<EventObject<T>>> checkAssertions(EventObject<T> object, Set<List<EventObject<T>>> eventObjectSets, PatternType type) {
		List<List<EventObject<T>>> output = new ArrayList<List<EventObject<T>>>();
		
		// Expressions überprüfen
		for (List<EventObject<T>> eventObjectSet : eventObjectSets) {
			Iterator<Entry<SDFExpression, AttributeMap[]>> iterator = attrMappings.entrySet().iterator();
			boolean satisfied = true;
			while (iterator.hasNext() && satisfied) {
				Entry<SDFExpression, AttributeMap[]> entry = iterator.next();
				satisfied = checkAssertion(object, eventObjectSet, entry);
			}
			if (satisfied) {
				// MatchingSet in Ausgabemenge aufnehmen
				output.add(eventObjectSet);
			}
		}
		return output;
	}
	
	private boolean checkAssertion(EventObject<T> object, List<EventObject<T>> eventObjectSet, Entry<SDFExpression, AttributeMap[]> entry) {
		Object[] values = findExpressionValues(eventObjectSet, entry.getValue());
		SDFExpression expression = entry.getKey();
		if (values != null) {
			expression.bindMetaAttribute(object.getEvent().getMetadata());
			expression.bindAdditionalContent(object.getEvent().getAdditionalContent());
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
	
	private boolean checkAssertion(EventObject<T> object, Entry<SDFExpression, AttributeMap[]> entry) {
		List<EventObject<T>> eventObjectSet = new ArrayList<>();
		eventObjectSet.add(object);
		return checkAssertion(object, eventObjectSet, entry);
	}
	
	/**
	 * Sucht nach den Werten für die Attribute aus der Expression.
	 * Bei Erfolg werden die Werte zurückgeliefert, bei misserfolg null.
	 * @param eventObjects Liste mit Event-Objekten.
	 * @return values, null bei Misserfolg
	 */
	private Object[] findExpressionValues(List<EventObject<T>> eventObjects, AttributeMap[] attrMapping) {
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
	
}
