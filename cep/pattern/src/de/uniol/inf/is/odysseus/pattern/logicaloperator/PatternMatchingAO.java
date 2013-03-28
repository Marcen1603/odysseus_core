package de.uniol.inf.is.odysseus.pattern.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Michael Falk
 */
@LogicalOperator(name="PATTERN", minInputPorts=1, maxInputPorts=2) // TODO
public class PatternMatchingAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 938637032482611886L;
	
	// Pattern-Type
	private String type;
	// relevante Event-Typen-Liste
	private List<String> eventTypes;
	// wiederholen nach erfolg
	// ausgabeverhalten
	// predikate, bedingungen
	
	public PatternMatchingAO() {
        super();
    }
    
    public PatternMatchingAO(PatternMatchingAO routeAO){
        super(routeAO);
    }
	
    @Override
    @Parameter(type=PredicateParameter.class)
    public void setPredicate(IPredicate<?> predicate) {
        super.setPredicate(predicate);
    }
    
    @Parameter(type=StringParameter.class)
    public void setType(String type) {
    	this.type = type;
    }
    
    public String getType() {
    	return type;
    }
    
    @Parameter(type=StringParameter.class, isList=true)
    public void setEventTypes(List<String> eventTypes) {
    	this.eventTypes = eventTypes;
    }
    
    public List<String> getEventTypes() {
    	return eventTypes;
    }
    
	@Override
	public AbstractLogicalOperator clone() {
		return new PatternMatchingAO(this);
	}

}
