package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

@LogicalOperator(name = "COALESCE", minInputPorts = 1, maxInputPorts = 1, doc="This Operator can be used to combine sequent elements, e.g. by a set of grouping attributes or with a predicates. In the attributes case, the elements are merged with also given aggregations functions, as long as the grouping attributes (e.g. a sensorid) are the same. When a new group is opened (e.g. a measurement from a new sensor) the old aggregates values and the grouping attributes are created as a result. In the predicate case, the elements are merged as long as the predicates evaluates to false, i.e. a new tuple is created when the predicates evaluates to true.", category={LogicalOperatorCategory.ADVANCED})
public class CoalesceAO extends AggregateAO implements IHasPredicate{

	private static final long serialVersionUID = 6314887685476173038L;
	private int maxElementsPerGroup = -1;
	private boolean createOnHeartbeat = false;
	private long heartbeatrate = -1;
	private IPredicate<?> predicate;

	public CoalesceAO() {
	}
	
	public CoalesceAO(CoalesceAO coalesceAO) {
		super(coalesceAO);
		maxElementsPerGroup = coalesceAO.maxElementsPerGroup;
		createOnHeartbeat = coalesceAO.createOnHeartbeat;
		heartbeatrate = coalesceAO.heartbeatrate;
	}

	@Override
	@Parameter(name = "ATTR", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		for (SDFAttribute a : attributes) {
			addGroupingAttribute(a);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Parameter(type=PredicateParameter.class, optional = true)
	public void setPredicate(IPredicate predicate) {
			this.predicate = predicate;
	}
	
	@Override
	public IPredicate<?> getPredicate() {
		return predicate;
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

	public long getHeartbeatrate() {
		return heartbeatrate;
	}

	@Parameter(name="heartbeatrate", optional = true, type = LongParameter.class)
	public void setHeartbeatrate(long heartbeatrate) {
		this.heartbeatrate = heartbeatrate;
	}
}
