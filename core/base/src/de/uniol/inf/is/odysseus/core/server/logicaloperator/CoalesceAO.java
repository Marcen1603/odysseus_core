package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(name = "COALESCE", minInputPorts = 1, maxInputPorts = 1)
public class CoalesceAO extends AggregateAO {

	private static final long serialVersionUID = 6314887685476173038L;
	private int maxElementsPerGroup = -1;
	private boolean createOnHeartbeat = false;

	public CoalesceAO() {
	}
	
	public CoalesceAO(CoalesceAO coalesceAO) {
		super(coalesceAO);
	}

	@Parameter(name = "ATTR", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		for (SDFAttribute a : attributes) {
			addGroupingAttribute(a);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@Parameter(type=PredicateParameter.class, optional = true)
	public void setPredicate(IPredicate predicate) {
			super.setPredicate(predicate);
	}
	
	@Override
	public boolean isValid() {
		if (getGroupingAttributes().size() > 0 && getPredicate() != null){
			addError(new IllegalParameterException(
					"can't use attributes and predicate at the same time!"));
			return false;
		}
		
		if (getPredicate() != null && isCreateOnHeartbeat()){
			addError(new IllegalParameterException(
					"can't use predicate with createOnHeartbeat!"));
			return false;			
		}
			
		return super.isValid();
	}
	
	@Override
	public CoalesceAO clone() {
		return new CoalesceAO(this);
	}

	@Parameter(name = "MaxElementsPerGroup", optional = true, type = IntegerParameter.class)
	public void setMaxElementsPerGroup(int maxElementsPerGroup) {
		this.maxElementsPerGroup = maxElementsPerGroup;
	}
	
	public int getMaxElementsPerGroup() {
		return maxElementsPerGroup ;
	}
	
	@Parameter(name = "CreateOnHeartbeat", optional = true, type = BooleanParameter.class)
	public void setCreateOnHeartbeat(boolean createOnHeartbeat) {
		this.createOnHeartbeat = createOnHeartbeat;
	}
	
	public boolean isCreateOnHeartbeat() {
		return createOnHeartbeat;
	}
}
