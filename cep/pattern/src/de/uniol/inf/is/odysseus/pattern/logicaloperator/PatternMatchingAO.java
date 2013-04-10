package de.uniol.inf.is.odysseus.pattern.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.sla.unit.TimeUnit;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Michael Falk
 */
@LogicalOperator(name="PATTERN", minInputPorts=1, maxInputPorts=Integer.MAX_VALUE)
public class PatternMatchingAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 938637032482611886L;
	
	// Pattern-Type
	private String type;
	private int time;
	private TimeUnit timeUnit;
	private int size;
	// relevante Event-Typen-Liste
	private List<String> eventTypes;
	// Port -> Name des Input-Types
	private Map<Integer, String> inputTypeNames;
	private Map<Integer, SDFSchema> inputSchemas;
	// Bedingung
	private SDFExpression expression;
	// Expressions fürs Ausgabeschema
	private List<SDFExpression> returnExpressions;
	// wiederholen nach erfolg
	// ausgabeverhalten
	private boolean simpleOutput;
	// Zeitfenster ??
	
	public PatternMatchingAO() {
        super();
        this.eventTypes = new ArrayList<String>();
        this.inputTypeNames = new HashMap<Integer, String>();
        this.inputSchemas = new HashMap<Integer, SDFSchema>();
    }
    
    public PatternMatchingAO(PatternMatchingAO patternAO){
        super(patternAO);
        this.type = patternAO.getType();
        this.eventTypes = patternAO.getEventTypes();
        this.inputTypeNames = patternAO.getInputTypeNames();
        this.inputSchemas = patternAO.getInputSchemas();
        this.simpleOutput = patternAO.simpleOutput;
        this.expression = patternAO.expression;
        this.returnExpressions = patternAO.returnExpressions;
    }
	
    @Parameter(type=SDFExpressionParameter.class, optional=true)
    public void setAssertion(NamedExpressionItem expr) {
        this.expression = expr.expression;
    }
    
    public SDFExpression getExpression() {
    	return expression;
    }
    
    @Parameter(type=StringParameter.class)
    public void setType(String type) {
    	this.type = type;
    }
    
    public String getType() {
    	return type;
    }
    
    @Parameter(type=IntegerParameter.class, optional = true)
    public void setTime(Integer time) {
    	this.time = time;
    }
    
    public int getTime() {
    	return time;
    }
    
    @Parameter(type=IntegerParameter.class, optional = true)
    public void setSize(Integer size) {
    	this.size = size;
    }
    
    public int getSize() {
    	return size;
    }
    
    @Parameter(type = EnumParameter.class, optional = true)
    public void setTimeUnit(TimeUnit unit) {
    	this.timeUnit = unit;
    }
    
    public TimeUnit getTimeUnit() {
    	return timeUnit;
    }
    
    @Parameter(type = StringParameter.class, isList = true)
    public void setEventTypes(List<String> eventTypes) {
    	this.eventTypes = eventTypes;
    }
    
    public List<String> getEventTypes() {
    	return eventTypes;
    }
    
    @Parameter(name = "return", type = SDFExpressionParameter.class, isList = true, optional = true)
    public void setReturnExpressions(List<NamedExpressionItem> namedReturnExpressions) {
		returnExpressions = new ArrayList<>();
		for (NamedExpressionItem e : namedReturnExpressions) {
			returnExpressions.add(e.expression);
		}
		setOutputSchema(null);
    }
    
    @Parameter(type=BooleanParameter.class, optional=true)
    public void setSimpleOutput(boolean output) {
        this.simpleOutput = output; 
    }
    
    public Map<Integer, String> getInputTypeNames() {
		return inputTypeNames;
	}
    
    public Map<Integer, SDFSchema> getInputSchemas() {
		return inputSchemas;
	}
    
	@Override
	public AbstractLogicalOperator clone() {
		return new PatternMatchingAO(this);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int port) {
        // Input-Typen und Schemas für Ports speichern
		for (LogicalSubscription s : this.getSubscribedToSource()) {
			SDFSchema schema = inputSchemas.get(s.getSinkInPort());
			if (schema == null) {
				schema = s.getSchema();
				inputSchemas.put(s.getSinkInPort(), schema);
			}
			String name = inputTypeNames.get(s.getSinkInPort());
			if (name == null) {
				name = schema.getURI();
				if (name == null){
					throw new IllegalArgumentException("Input stream must have a type.");
				}
				inputTypeNames.put(s.getSinkInPort(), name);
			}
		}
		// TODO: Default ist Port 0 // siehe MapAO
		//return this.getInputSchema(port);
		
		// einfache Variante (ohne Expressions)
		SDFAttribute type = new SDFAttribute("PATTERN", "type", SDFDatatype.STRING);
		SDFAttribute detected = new SDFAttribute("PATTERN", "detected", SDFDatatype.BOOLEAN);
		SDFSchema schema = new SDFSchema("PATTERN", type, detected);
		return schema;
	}
	
	@Override
	public boolean isValid() {
		// Elemente der relevanten Eventliste müssen auch als Quellen vorhanden sein
		/*boolean help = false;
		for (String eventType : eventTypes) {
			help = false;
			for (String inputType : inputTypeNames.values()) {
				if (eventType.equals(inputType)) {
					help = true;
					break;
				}				
			}
			if (!help) {
				addError(new IllegalArgumentException("eventTypes have to be defined also as a source."));
				break;
			}
		}
		return help;*/
		// TODO
		// weitere Validierungen
		return true;
	}

}
