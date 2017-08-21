package de.uniol.inf.is.odysseus.pattern.logicaloperator;

import static de.uniol.inf.is.odysseus.pattern.util.PatternType.ALWAYS;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.DECREASING;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.FIRST_N;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.INCREASING;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.LAST_N;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.MIXED;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.NON_DECREASING;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.NON_INCREASING;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.NON_STABLE;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.RELATIVE_N_HIGHEST;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.RELATIVE_N_LOWEST;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.SOMETIMES;
import static de.uniol.inf.is.odysseus.pattern.util.PatternType.STABLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.pattern.util.PatternOutput;
import de.uniol.inf.is.odysseus.pattern.util.PatternType;

/**
 * @author Michael Falk
 */
@LogicalOperator(name="PATTERN", minInputPorts=1, maxInputPorts=Integer.MAX_VALUE, doc="This generic operator allows the definition of different kinds of pattern (e.g. all, any). For sequence based patterns see SASE operator ",category = {LogicalOperatorCategory.PATTERN})
public class PatternMatchingAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 938637032482611886L;
	
	// Pattern-Type
	private PatternType type;
	private Integer time;
	private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
	private Integer size;
	// Ausgabeverhalten
	private PatternOutput outputMode = PatternOutput.SIMPLE;
	private Integer inputPort = 0;
	// auszulesendes Attribut
	private String attribute;
	
	// relevante Event-Typen-Liste
	private List<String> eventTypes;
	// Port -> Name des Input-Types
	private Map<Integer, String> inputTypeNames;
	private Map<Integer, SDFSchema> inputSchemas;
	// Bedingung
	private List<SDFExpression> assertions;
	// Expressions f�rs Ausgabeschema
	private List<NamedExpression> returnExpressions;
	private Integer count;
	
	public PatternMatchingAO() {
        super();
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
        this.assertions = patternAO.assertions;
        this.returnExpressions = patternAO.returnExpressions;
        this.attribute = patternAO.attribute;
        this.count = patternAO.count;
        this.inputPort = patternAO.inputPort;
    }
	
    @Parameter(type=NamedExpressionParameter.class, optional=true, isList=true)
    public void setAssertions(List<NamedExpression> exprs) {
        this.assertions = new ArrayList<SDFExpression>();
        for (NamedExpression expr : exprs) {
        	assertions.add(expr.expression);
        }
    }
    
    @Parameter(name = "count", type=IntegerParameter.class, optional=true)
    public void setCountEvents(Integer count) {
        this.count = count;
    }
    
    public Integer getCountEvents() {
        return count;
    }
    
    public List<SDFExpression> getAssertions() {
    	return assertions;
    }
    
    @Parameter(type=EnumParameter.class)
    public void setType(PatternType type) {
    	this.type = type;
    }
    
    public PatternType getType() {
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
    
    @Parameter(type = StringParameter.class, optional = true)
    public void setAttribute(String attr) {
    	this.attribute = attr;
    }
    
    public String getAttribute() {
    	return attribute;
    }

    @Parameter(type = StringParameter.class, isList = true)
    public void setEventTypes(List<String> eventTypes) {
    	this.eventTypes = eventTypes;
    }
    
    public List<String> getEventTypes() {
    	return eventTypes;
    }
    
    @Parameter(name = "inputport", type=IntegerParameter.class, optional = true)
    public void setInputPort(Integer port) {
    	this.inputPort = port;
    }
    
    public Integer getInputPort() {
    	return inputPort;
    }
    
    @Parameter(name = "return", type = NamedExpressionParameter.class, isList = true, optional = true)
    public void setReturnExpressions(List<NamedExpression> namedReturnExpressions) {
		returnExpressions = namedReturnExpressions;
		setOutputSchema(null);
    }
    
    public List<SDFExpression> getReturnExpressions() {
    	if (returnExpressions == null) return null;
    	List<SDFExpression> expressions = new ArrayList<SDFExpression>();
    	for (NamedExpression expr : returnExpressions) {
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
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int port) {
		// Ausgabeschema bestimmen
		SDFSchema schema = null;
		if (outputMode == PatternOutput.INPUT) {
			if (inputPort == null || inputPort < 0 || inputPort > this.getNumberOfInputs()) {
				inputPort = 0;
			}
			LogicalSubscription s = this.getSubscribedToSource(inputPort);
			schema = inputSchemas.get(s.getSinkInPort());
			if (schema == null) {
				schema = s.getSchema();
			}
		} else if (outputMode == PatternOutput.TUPLE_CONTAINER) {
			// create tuple container attribute
			SDFAttribute tuple = new SDFAttribute("PATTERN", "tuple_container", SDFDatatype.TUPLE, null, null, null);
			
			schema = SDFSchemaFactory.createNewTupleSchema("PATTERN", tuple);
		} else if (outputMode == PatternOutput.EXPRESSIONS && returnExpressions != null) {
			// EXPRESSIONS: Ausgabe h�ngt vom return-Parameter ab
			List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
			for (NamedExpression expr : returnExpressions) {
				
				// TODO: Maybe here should an attribute resolver be used?
				
				SDFAttribute attr = null;
				IMepExpression<?> mepExpression = expr.expression
						.getMEPExpression();
				String exprString;
				boolean isOnlyAttribute = false;

				exprString = expr.expression.toString();
				// Variable could be source.name oder name, we are looking
				// for
				// name!
				String lastString = null;
				String toSplit;
				if (exprString.startsWith("__")) {
					toSplit = exprString.substring(exprString.indexOf(".") + 1);
					lastString = exprString.substring(0,
							exprString.indexOf(".") - 1);
				} else {
					toSplit = exprString;
				}
				String[] split = SDFElement.splitURI(toSplit);
				final SDFElement elem;
				if (split[1] != null && split[1].length() > 0) {
					elem = new SDFElement(split[0], split[1]);
				} else {
					elem = new SDFElement(null, split[0]);
				}

				// If expression is an attribute use this data type
				List<SDFAttribute> inAttribs = expr.expression
						.getAllAttributes();
				for (SDFAttribute attributeToCheck : inAttribs) {
					SDFAttribute attribute;
					String attributeURI = attributeToCheck.getURI();
					if (attributeURI.startsWith("__")) {
						String realAttributeName = attributeURI
								.substring(attributeURI.indexOf(".") + 1);
						split = SDFElement.splitURI(realAttributeName);
						if (split.length > 1) {
							attribute = new SDFAttribute(split[0], split[1],
									attributeToCheck);
						} else {
							attribute = new SDFAttribute(null, split[0],
									attributeToCheck);
						}
					} else {
						attribute = attributeToCheck;
					}
					if (attribute.equalsCQL(elem)) {
						if (lastString != null) {
							String attrName = elem.getURIWithoutQualName() != null ? elem
									.getURIWithoutQualName()
									+ "."
									+ elem.getQualName() : elem.getQualName();
							attr = new SDFAttribute(lastString, attrName,
									attribute.getDatatype(), attribute.getUnit(), attribute.getDtConstraints());
						} else {
							attr = new SDFAttribute(
									elem.getURIWithoutQualName(),
									elem.getQualName(), attribute.getDatatype(), attribute.getUnit(), attribute.getDtConstraints());
						}
						isOnlyAttribute = true;
					}
				}

				// Expression is an attribute and name is set --> keep Attribute
				// type
				if (isOnlyAttribute) {
					if (!"".equals(expr.name)) {
						if ((attr != null) && (!attr.getSourceName().startsWith("__"))) {
							attr = new SDFAttribute(attr.getSourceName(),
									expr.name, attr);
						} else {
							attr = new SDFAttribute(null, expr.name, attr);
						}
					}
				}

				// else use the expression data type
				if (attr == null) {
					attr = new SDFAttribute(null, !"".equals(expr.name) ? expr.name
							: exprString, mepExpression.getReturnType(), null, null, null);
				}
				attrs.add(attr);
			}
			// Was ist die URI hier??
			schema = SDFSchemaFactory.createNewWithAttributes(attrs, getInputSchema(0));
		} else {
			// SIMPLE: einfache Variante (ohne Expressions)
			SDFAttribute type = new SDFAttribute("PATTERN", "type", SDFDatatype.STRING, null, null, null);
			SDFAttribute timestamp = new SDFAttribute("PATTERN", "timestamp", SDFDatatype.LONG, null, null, null);
			SDFAttribute detected = new SDFAttribute("PATTERN", "detected", SDFDatatype.BOOLEAN, null, null, null);
			schema =  SDFSchemaFactory.createNewTupleSchema("PATTERN", type, timestamp, detected);
		}
		setOutputSchema(schema);
		return schema;
	}
	
	@Override
	public boolean isValid() {
		if (inputSchemas == null && inputTypeNames == null) {
			// Input-Typen und Schemas f�r Ports speichern
	        inputTypeNames = new HashMap<Integer, String>();
	        inputSchemas = new HashMap<Integer, SDFSchema>();
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
					if (name.startsWith("System.")) {
						name = name.substring(7);
					}
					inputTypeNames.put(s.getSinkInPort(), name);
				}
			}
		}
		boolean help = false;
		// check conversion to enum goes automatically (type, timeUnit and outputMode)
		// check value of eventTypes
		for (String eventType : eventTypes) {
			help = false;
			for (String inputType : inputTypeNames.values()) {
				if (eventType.equals(inputType)) {
					help = true;
					break;
				}				
			}
			if (!help) {
				addError("one or more elements from eventTypes don't exist as sources.");
				break;
			}
		}
		// check value of time
		if (time != null) {
			if (time <= 0) {
				help = false;
				addError("time has to be a positive numeric value.");
			}
		}
		// check value of size
		if (size != null) {
			if (size <= 0) {
				help = false;
				addError("size has to be a positive numeric value.");
			}
		}
		// check value of count
		if (count != null) {
			if (count <= 0) {
				help = false;
				addError("count has to be a positive numeric value.");
			}
		}
		// check value of inputPort
		if (inputPort < 0 || inputPort > inputTypeNames.size() - 1) {
			help = false;
			addError("inputPort has to be a positive numeric value and the input port must exit.");
		}
		// check value of timeUnit
		if (timeUnit == TimeUnit.NANOSECONDS || timeUnit == TimeUnit.MICROSECONDS) {
			help = false;
			addError("timeUnit doesn't accept the values MICROSECONDS and NANOSECONDS.");
		}
		// check value of attribute
		if (attribute != null) {
			// every schema must contain this attribute
			for (SDFSchema schema : inputSchemas.values()) {
				if (schema.findAttribute(attribute) == null) {
					// no attribute found
					help = false;
					addError("the schema " + schema.getURIWithoutQualName()
							+ " doesn't contain the attribute.");
				}				
			}
		}
		// check pattern constraints
		List<PatternType> patternList = Arrays.asList(RELATIVE_N_HIGHEST, RELATIVE_N_LOWEST);
		if (patternList.contains(type)) {
			if (attribute == null || count == null || (time == null && size == null)) {
				help = false;
				addError("some for this pattern required attributes are missing.");
			}
		}
		patternList = Arrays.asList(ALWAYS, SOMETIMES);
		if (patternList.contains(type)) {
			if (time == null && size == null) {
				help = false;
				addError("some for this pattern required attributes are missing.");
			}
		}
		patternList = Arrays.asList(FIRST_N, LAST_N);
		if (patternList.contains(type)) {
			if (count == null || (time == null && size == null)) {
				help = false;
				addError("some for this pattern required attributes are missing.");
			}
		}
		patternList = Arrays.asList(INCREASING, DECREASING, STABLE,
				NON_DECREASING, NON_INCREASING, NON_STABLE, MIXED);
		if (patternList.contains(type)) {
			if (attribute == null || (time == null && size == null)) {
				help = false;
				addError("some for this pattern required attributes are missing.");
			}
		}
		return help;
	}

}
