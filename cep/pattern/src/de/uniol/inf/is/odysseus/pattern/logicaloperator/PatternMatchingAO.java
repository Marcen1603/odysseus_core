package de.uniol.inf.is.odysseus.pattern.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import model.PatternOutput;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Michael Falk
 */
@LogicalOperator(name="PATTERN", minInputPorts=1, maxInputPorts=Integer.MAX_VALUE)
public class PatternMatchingAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 938637032482611886L;
	
	// Pattern-Type
	private String type;
	private Integer time;
	private TimeUnit timeUnit;
	private Integer size;
	// Ausgabeverhalten
	private PatternOutput outputMode;
	
	// relevante Event-Typen-Liste
	private List<String> eventTypes;
	// Port -> Name des Input-Types
	private Map<Integer, String> inputTypeNames;
	private Map<Integer, SDFSchema> inputSchemas;
	// Bedingung
	private SDFExpression expression;
	// Expressions fürs Ausgabeschema
	private List<NamedExpressionItem> returnExpressions;
	// wiederholen nach erfolg
	// Zeitfenster ??
	
	public PatternMatchingAO() {
        super();
        this.eventTypes = new ArrayList<String>();
        this.inputTypeNames = new HashMap<Integer, String>();
        this.inputSchemas = new HashMap<Integer, SDFSchema>();
    }
    
    public PatternMatchingAO(PatternMatchingAO patternAO){
        super(patternAO);
        this.type = patternAO.type;
        this.time = patternAO.time;
    	this.timeUnit = patternAO.timeUnit;
    	this.size = patternAO.size;
    	this.outputMode = patternAO.outputMode;
        this.eventTypes = patternAO.getEventTypes();
        this.inputTypeNames = patternAO.getInputTypeNames();
        this.inputSchemas = patternAO.getInputSchemas();
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
    
    public Integer getTime() {
    	return time;
    }
    
    @Parameter(name = "size", type=IntegerParameter.class, optional = true)
    public void setBufferSize(Integer size) {
    	this.size = size;
    }
    
    public Integer getBufferSize() {
    	return size;
    }
    
    @Parameter(type = EnumParameter.class, optional = true)
    public void setTimeUnit(TimeUnit unit) {
    	this.timeUnit = unit;
    }
    
    public TimeUnit getTimeUnit() {
    	return timeUnit;
    }
    
    @Parameter(name = "outputmode", type = EnumParameter.class, optional = true)
    public void setOutput(PatternOutput outputMode) {
    	this.outputMode = outputMode;
    }
    
    public PatternOutput getOutputMode() {
    	return outputMode;
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
		returnExpressions = namedReturnExpressions;
		setOutputSchema(null);
    }
    
    public List<SDFExpression> getReturnExpressions() {
    	List<SDFExpression> expressions = new ArrayList<SDFExpression>();
    	for (NamedExpressionItem expr : returnExpressions) {
    		expressions.add(expr.expression);
    	}
    	return expressions;
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
	
	private SDFSchema calcOutputSchema() {
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
		
		SDFSchema schema = null;
		if (outputMode == PatternOutput.EXPRESSIONS && returnExpressions != null) {
			List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
			
			for (NamedExpressionItem expr : returnExpressions) {
				SDFAttribute attr = null;
				IExpression<?> mepExpression = expr.expression.getMEPExpression();
				String exprString;
				boolean isOnlyAttribute = false;
				// Determine attribute name
				if ("".equals(expr.name)) {
					exprString = expr.expression.toString();
					// Variable could be source.name oder name, we are looking for name!
					String[] split = SDFElement.splitURI(exprString);
					final SDFElement elem;
					if (split[1] != null && split[1].length() > 0) {
						elem = new SDFElement(split[0], split[1]);
					} else {
						elem = new SDFElement(null, split[0]);
					}

					// If expression is an attribute use this data type
					List<SDFAttribute> inAttribs = expr.expression.getAllAttributes();
					for (SDFAttribute attribute : inAttribs) {
						if (attribute.equalsCQL(elem)) {
							attr = new SDFAttribute(elem.getURIWithoutQualName(), elem.getQualName(), attribute.getDatatype());
							isOnlyAttribute = true;
						}
					}
				} else {
					exprString = expr.name;
				}
				// Expression is an attribute and name is set --> keep Attribute type
				if (isOnlyAttribute && !"".equals(expr.name)) {
					attr = new SDFAttribute(attr.getSourceName(), expr.name, attr);

				}
				// else use the expression data type
				if (attr == null) {
					attr = new SDFAttribute(null, exprString, mepExpression.getReturnType());
				}
				attrs.add(attr);
			}
		} else {
			// SIMPLE: einfache Variante (ohne Expressions)
			SDFAttribute type = new SDFAttribute("PATTERN", "type", SDFDatatype.STRING);
			SDFAttribute detected = new SDFAttribute("PATTERN", "detected", SDFDatatype.BOOLEAN);
			schema = new SDFSchema("PATTERN", type, detected);
		}
		setOutputSchema(schema);
		return schema;
	}
	
//	@Override
//	public void initialize() {
////		calcOutputSchema();
//	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int port) {
//		calcOutputSchema();
//        return getOutputSchema();
		return calcOutputSchema();
	}
	
	@Override
	public boolean isValid() {
		// Elemente der relevanten Eventliste müssen auch als Quellen vorhanden sein
//		boolean help = false;
//		for (String eventType : eventTypes) {
//			help = false;
//			for (String inputType : inputTypeNames.values()) {
//				if (eventType.equals(inputType)) {
//					help = true;
//					break;
//				}				
//			}
//			if (!help) {
//				addError(new IllegalArgumentException("eventTypes have to be defined also as a source."));
//				break;
//			}
//		}
//		return help;
		// TODO
		// weitere Validierungen
		return true;
	}

}
