package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.AttributeMap;
import model.EventBuffer;
import model.EventObject;

import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidEventException;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.sla.unit.TimeUnit;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * Operator, um Pattern-Matching durchzuführen.
 * @author Michael Falk
 * @param <T>
 */
public class PatternMatchingPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>>
	implements IProcessInternal<Tuple<T>> {
	
	private SDFExpression expression;
	/**
	 * Der Pattern-Typ.
	 */
	private String type;
	private int time;
	private int size;
	private TimeUnit timeUnit;
	/**
	 * Die relevante Event-Typen-Liste.
	 */
	private List<String> eventTypes;
	private Map<Integer, String> inputTypeNames;
	private Map<Integer, SDFSchema> inputSchemas;
	
	protected IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea;
	protected ITransferArea<Tuple<T>, Tuple<T>> outputTransferArea;
	
	// ALL-Pattern
	//private DefaultTISweepArea<IStreamObject<? extends ITimeInterval>>
	/**
	 * Port-Map, die für jeden Port eine Liste mit Event-Objekten enthält.
	 */
	private Map<Integer, EventBuffer<T>> objectLists;
	private AttributeMap[] attrMapping;
	
	public PatternMatchingPO(String type, int time, int size, TimeUnit timeUnit, List<String> eventTypes, SDFExpression expr,
			Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas, IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea) {
        super();
        this.type = type;
        this.time = time;
        this.size = size;
        this.timeUnit = timeUnit;
        this.eventTypes = eventTypes;
        this.expression = expr;
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
        this.init();
    }
	
	// Copy-Konstruktor
    public PatternMatchingPO(PatternMatchingPO<T> patternPO) {
        this.type = patternPO.type;
        this.time = patternPO.time;
        this.size = patternPO.size;
        this.timeUnit = patternPO.timeUnit;
        this.eventTypes = patternPO.eventTypes;
        this.expression = patternPO.expression;
        this.inputTypeNames = patternPO.inputTypeNames;
        this.inputSchemas = patternPO.inputSchemas;
        this.inputStreamSyncArea = patternPO.inputStreamSyncArea;
        this.objectLists = patternPO.objectLists;
        this.init();
    }
	
    private void init() {
    	if (expression != null) {
    		List<SDFAttribute> neededAttr = expression.getAllAttributes();
    		attrMapping = new AttributeMap[neededAttr.size()];
    		int i = 0;
    		int port = 0;
    		for (SDFAttribute attr : neededAttr) {
    			// passendes Schema raussuchen
    			while (port < inputSchemas.size()) {
    				SDFSchema schema = inputSchemas.get(port);
    				int attrPos = schema.indexOf(attr);
    				if (attrPos != -1) {
    					// Mapping speichern
    					attrMapping[i].setPort(port);
    					attrMapping[i].setAttrPos(attrPos);
    					break;
    				}
    				port++;
    			}
    			i++;
    		}
    	}
    	
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
	public AbstractPipe<Tuple<? extends ITimeInterval>,Tuple<? extends ITimeInterval>> clone() {
		return new PatternMatchingPO<T>(this);
	}
	
	@Override
    public void process_open() throws OpenFailedException {
        super.process_open();
        inputStreamSyncArea.init(this);
//        expression.init();
    }
     
	
	@Override
	protected void process_next(Tuple<? extends ITimeInterval> event, int port) {
		inputStreamSyncArea.newElement(event, port);
	}

	@Override
	public void process_internal(Tuple<T> event, int port) {
		String eventType = inputTypeNames.get(port);
		SDFSchema schema = inputSchemas.get(port);
		if (eventType == null) {
			throw new InvalidEventException("Der Datentyp des Events ist null!");
		}
		boolean detected = false;
		switch(type) {
			// Logische Pattern
			case "ANY":
				// Prädikat notwendig?
				if (eventTypes.contains(eventType)) {
					// Expression ausführbar?
//					List<SDFAttribute> attributes = inputSchemas.get(port).getAttributes();
//					for (AttributeMap attr : attrMapping) {
//						// TODO: Expressions beim ANY-Pattern
//					}
				}
				if (detected) {
					// ANY-Pattern erkannt
					this.transfer(this.createComplexEvent(type));
				}
				break;
			case "ALL":
				/*if (eventTypes.contains(eventType)) {
					EventObject<T> eventObj = new EventObject<T>(event, eventType, schema, port);
					// Event in vorhandene Listen einfügen,
					// wenn noch nicht vorhanden
					for (EventBuffer<T> objects : objectLists) {
						if (!objects.contains(eventType)) {
							objects.add(eventObj);
							// Pattern erfüllt?
							if (objects.matchAllPattern(eventTypes, expression, attrMapping, event)) {
								// evt. alle bis dahin gesammelten Daten löschen
								detected = true;
								break;
							}
						}
					}
					if (!detected) {
						// neue Liste erzeugen
						EventBuffer<T> objects = new EventBuffer<T>();
						objects.add(eventObj);
						objectLists.add(objects);
						if (objects.matchAllPattern(eventTypes, expression, attrMapping, event))
							detected = true;
					}
				}
				if (detected)
					this.transfer(this.createComplexEvent(type));*/
				if (eventTypes.contains(eventType)) {
					EventObject<T> eventObj = new EventObject<T>(event, eventType, schema, port);
					// Event einsortieren und alte Events entfernen
					objectLists.get(port).add(eventObj);
					dropOldEvents();
					// Kombinationen suchen und Bedingungen überprüfen
					List<List<EventObject<T>>> output = checkAssertion(eventObj);
					for (int i = 0; i < output.size(); i++) {
						this.transfer(this.createComplexEvent(type));
					}
				}
			case "ABSENCE":
			// Threshold Pattern
			case "COUNT":
			case "VALUE MAX":
			case "VALUE MIN:":
			case "FUNCTOR":
			// Subset Selection Pattern
			case "RELATIVE N HIGHEST":
			case "RELATIVE N LOWEST":
			// Modale Pattern
			case "ALWAYS":
			case "SOMETIMES":
			// Temporal Order Pattern
			case "SEQUENCE":
			case "FIRST N PATTERN":
			case "LAST N PATTERN":
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
	 * Erzeugt ein komplexes Event.
	 */
	private Tuple<? extends ITimeInterval> createComplexEvent(String type) {
		Object[] attributes = new Object[2];
		attributes[0] = type;
		attributes[1] = true;
		Tuple<? extends ITimeInterval> returnEvent = new Tuple<ITimeInterval>(attributes, false);
		return returnEvent;
	}
	
	private void dropOldEvents() {
		
	}
	
	private List<List<EventObject<T>>> checkAssertion(EventObject<T> object) {
		List<List<EventObject<T>>> output = new ArrayList<List<EventObject<T>>>();
		
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
		Set<List<EventObject<T>>> crossProduct = Sets.cartesianProduct(relevantSets);
		
		if (crossProduct.size() != 0) {
			// Expressions überprüfen
			for (List<EventObject<T>> crossElems : crossProduct) {
				boolean attrMappingFailure = false;
				Object[] values = new Object[attrMapping.length];
				for (int i = 0; i < attrMapping.length; i++) {
					EventObject<T> obj = null;
					Iterator<EventObject<T>> iterator = crossElems.iterator();
					while (iterator.hasNext()) {
						obj = iterator.next();
						if (obj.getPort() == attrMapping[i].getPort()) {
							break;
						}
					}
					if (obj != null) {
						int attrPos = attrMapping[i].getAttrPos();
						values[i] = obj.getEvent().getAttribute(attrPos);
					} else {
						// es gibt kein Objekt mit dem geforderten Attribut
						attrMappingFailure = true;
						break;
					}
				}
				if (attrMappingFailure == false) {
					expression.bindMetaAttribute(object.getEvent().getMetadata());
					expression.bindAdditionalContent(object.getEvent().getAdditionalContent());
					expression.bindVariables(values);
					boolean predicate = expression.getValue();
					if (predicate) {
						// MatchingSet in Ausgabemenge aufnehmen
						output.add(crossElems);
					}
				}
			}
		}
		return output;
	}
	
}
