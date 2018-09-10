package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicates;

@LogicalOperator(name = "COALESCE", minInputPorts = 1, maxInputPorts = 1, doc = "This Operator can be used to combine sequent elements, e.g. by a set of grouping attributes or with a predicates. In the attributes case, the elements are merged with also given aggregations functions, as long as the grouping attributes (e.g. a sensorid) are the same. When a new group is opened (e.g. a measurement from a new sensor) the old aggregates values and the grouping attributes are created as a result. In the predicate case, the elements are merged as long as the predicates evaluates to false, i.e. a new tuple is created when the predicates evaluates to true.", category = { LogicalOperatorCategory.ADVANCED })
public class CoalesceAO extends AggregateAO implements IHasPredicates {

	private static final long serialVersionUID = 6314887685476173038L;
	private int maxElementsPerGroup = -1;
	private boolean createOnHeartbeat = false;
	private long heartbeatrate = -1;
	private IPredicate<?> predicate;
	private IPredicate<?> startPredicate;
	private IPredicate<?> endPredicate;

	public CoalesceAO() {
	}

	public CoalesceAO(CoalesceAO coalesceAO) {
		super(coalesceAO);
		maxElementsPerGroup = coalesceAO.maxElementsPerGroup;
		createOnHeartbeat = coalesceAO.createOnHeartbeat;
		heartbeatrate = coalesceAO.heartbeatrate;
		if (coalesceAO.predicate != null){
			this.predicate = coalesceAO.predicate.clone();
		}
		if (coalesceAO.startPredicate != null){
			this.startPredicate = coalesceAO.startPredicate.clone();
		}
		if (coalesceAO.endPredicate != null){
			this.endPredicate = coalesceAO.endPredicate.clone();
		}
	}	

	@Override
	@Parameter(name = "ATTR", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		for (SDFAttribute a : attributes) {
			addGroupingAttribute(a);
		}
	}

	@SuppressWarnings("rawtypes")
	@Parameter(name="Predicate", type = PredicateParameter.class, optional = true, deprecated = true, doc="Do not use. Use StartPredicate and EndPredicate instead.")
	public void setPredicate(IPredicate predicate) {
		this.predicate = predicate;
	}

	@SuppressWarnings("rawtypes")
	@Parameter(name="StartPredicate", type = PredicateParameter.class, optional = true)
	public void setStartPredicate(IPredicate predicate) {
		this.startPredicate = predicate;
	}

	@SuppressWarnings("rawtypes")
	@Parameter(name="EndPredicate", type = PredicateParameter.class, optional = true)
	public void setEndPredicate(IPredicate predicate) {
		this.endPredicate = predicate;
	}

	public IPredicate<?> getPredicate() {
		return predicate;
	}
	
	@Override
	public List<IPredicate<?>> getPredicates() {
		List<IPredicate<?>> predicates = new LinkedList<IPredicate<?>>();
		if (predicate != null){
			predicates.add(predicate);
		}
		if (startPredicate != null){
			predicates.add(startPredicate);
		}
		if (endPredicate != null){
			predicates.add(endPredicate);
		}
		return predicates;
	}
	
	public IPredicate<?> getStartPredicate() {
		return startPredicate;
	}

	public IPredicate<?> getEndPredicate() {
		return endPredicate;
	}

	@Override
	public boolean isValid() {
		boolean valid = super.isValid();

		if (getGroupingAttributes().size() == 0 && getPredicate() == null && getStartPredicate() == null){
			addError("One of ATTR, predicate, start/stoppredicate must be set");
			valid = false;
		}
		
		if (getGroupingAttributes().size() > 0) {
			if (getPredicate() != null) {
				addError("can't use attributes and predicates at the same time!");
				valid = false;
			}
		}

		if (getPredicate() != null) {

			if (isCreateOnHeartbeat()) {
				addError("can't use predicate with createOnHeartbeat!");
				valid = false;
			}

			if (getStartPredicate() != null) {
				addError("can't use predicate with startPredicate!");
				valid = false;
			}

			if (getEndPredicate() != null) {
				addError("can't use predicate with endPredicate!");
				valid = false;
			}
		}

		if ((getStartPredicate() != null && getEndPredicate() == null)
				|| (getStartPredicate() == null && getEndPredicate() != null)) {
			addError("Need both: Start and end predicate");
			valid = false;
		}

		return valid;
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
		return maxElementsPerGroup;
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

	@Parameter(name = "heartbeatrate", optional = true, type = LongParameter.class)
	public void setHeartbeatrate(long heartbeatrate) {
		this.heartbeatrate = heartbeatrate;
	}

	@Override
	public void setPredicates(List<IPredicate<?>> predicates) {
		this.setPredicate(predicates.get(0));
		this.setStartPredicate(predicates.get(1));
		this.setEndPredicate(predicates.get(2));
	}

	@Override
	public OperatorStateType getStateType() {
		return IOperatorState.OperatorStateType.ARBITRARY_STATE;
	}
}
