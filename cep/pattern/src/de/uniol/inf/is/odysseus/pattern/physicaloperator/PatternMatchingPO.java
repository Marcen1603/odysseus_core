package de.uniol.inf.is.odysseus.pattern.physicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.AllPatternDataStructure;
import model.AttributeMap;
import model.EventBuffer;
import model.EventObject;
import de.uniol.inf.is.odysseus.cep.epa.exceptions.InvalidEventException;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * Operator, um Pattern-Matching durchzuführen.
 * @author Michael Falk
 * @param <T>
 */
public class PatternMatchingPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>>
	implements IHasPredicate, IProcessInternal<Tuple<T>> {
	
	private SDFExpression expression;
	/**
	 * Der Pattern-Typ.
	 */
	private String type;
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
	private AllPatternDataStructure allPattern;
	private List<EventBuffer<T>> objectLists;
	private AttributeMap[] attrMapping;
	
	
	public PatternMatchingPO(String type, List<String> eventTypes, SDFExpression expr,
			Map<Integer, String> inputTypeNames, Map<Integer, SDFSchema> inputSchemas, IInputStreamSyncArea<Tuple<T>> inputStreamSyncArea) {
        super();
        this.type = type;
        this.eventTypes = eventTypes;
        this.expression = expr;
        this.inputTypeNames = inputTypeNames;
        this.inputSchemas = inputSchemas;
        this.inputStreamSyncArea = inputStreamSyncArea;
        this.allPattern = new AllPatternDataStructure(this.eventTypes);
        this.objectLists = new ArrayList<EventBuffer<T>>();
        this.init();
    }
	
	// Copy-Konstruktor
    public PatternMatchingPO(PatternMatchingPO<T> patternPO) {
        this.type = patternPO.type;
        this.eventTypes = patternPO.eventTypes;
        this.expression = patternPO.expression;
        this.inputTypeNames = patternPO.inputTypeNames;
        this.inputSchemas = patternPO.inputSchemas;
        this.inputStreamSyncArea = patternPO.inputStreamSyncArea;
        this.allPattern = patternPO.allPattern;
        this.objectLists = patternPO.objectLists;
        this.init();
    }
	
    private void init() {
    	if (expression != null) {
    		List<SDFAttribute> neededAttr = expression.getAllAttributes();
    		attrMapping = new AttributeMap[neededAttr.size()];
    		int i = 0;
    		int schemaID = 0;
    		for (SDFAttribute attr : neededAttr) {
    			// passendes Schema raussuchen
    			while (schemaID < inputSchemas.size()) {
    				SDFSchema schema = inputSchemas.get(schemaID);
    				int attrPos = schema.indexOf(attr);
    				if (attrPos != -1) {
    					// Mapping speichern
    					attrMapping[i].setSchema(schema);
    					attrMapping[i].setAttrPos(attrPos);
    					break;
    				}
    				schemaID++;
    			}
    			i++;
    		}
    	}
    	
    }
    
	@Override
	public String toString(){
		return super.toString() + " type: " + type + " eventTypes: " + eventTypes.toString(); 
	}
	
	public SDFExpression getExpression() {
		return expression;
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
		// Prädikat ausführbar?
		List<SDFAttribute> attributes = inputSchemas.get(port).getAttributes();
		boolean detected = false;
		switch(type) {
			// Logische Pattern
			case "ANY":
				// Prädikat notwendig?
				detected = eventTypes.contains(eventType);
				if (detected) {
					// ANY-Pattern erkannt
					this.transfer(this.createComplexEvent(type));
					// TODO: Komplexes Event erzeugen
				}
				break;
			case "ALL":
				if (eventTypes.contains(eventType)) {
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
					this.transfer(this.createComplexEvent(type));
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

	@Override
	public IPredicate getPredicate() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
