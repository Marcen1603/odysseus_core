package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalStateExpression;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.PredicateWindowTIPO;

public class RelationalPredicateWindowTIPO extends PredicateWindowTIPO<Tuple<ITimeInterval>> {

	private RelationalStateExpression<ITimeInterval> start;
	private RelationalStateExpression<ITimeInterval> end;
	private RelationalStateExpression<ITimeInterval> advance;
	private RelationalStateExpression<ITimeInterval> clear;
	

	public RelationalPredicateWindowTIPO(AbstractWindowAO windowao) {
		super(windowao);
		if (windowao.getStartCondition() != null) {
			this.start = initCondition(windowao.getStartCondition(), windowao.getInputSchema());
		}
		if (windowao.getEndCondition() != null) {
			this.end = initCondition(windowao.getEndCondition(), windowao.getInputSchema());
		}
		if (windowao.getAdvanceCondition() != null) {
			this.advance = initCondition(windowao.getAdvanceCondition(), windowao.getInputSchema());
		}
		if (windowao.getClearCondition() != null) {
			this.clear = initCondition(windowao.getClearCondition(), windowao.getInputSchema());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private RelationalStateExpression<ITimeInterval> initCondition(IPredicate<?> condition, SDFSchema schema ) {
		RelationalStateExpression<ITimeInterval> relCondition  = new RelationalStateExpression<>(
				((RelationalExpression<ITimeInterval>) condition).getExpression().clone());
		relCondition.initVars(schema);
		return relCondition;
	}

	@Override
	protected Boolean evaluateStartCondition(Tuple<ITimeInterval> object, List<Tuple<ITimeInterval>> buffer) {
		return evaluateCondition(start, object, buffer);
	}

	@Override
	protected Boolean evaluateEndCondition(Tuple<ITimeInterval> object, List<Tuple<ITimeInterval>> buffer) {
		return evaluateCondition(end, object, buffer);
	}
	
	@Override
	protected Boolean evaluateAdvanceCondition(Tuple<ITimeInterval> object, List<Tuple<ITimeInterval>> buffer) {
		return evaluateCondition(advance, object, buffer);
	}
	
	@Override
	protected Boolean evaluateClearCondition(Tuple<ITimeInterval> object, List<Tuple<ITimeInterval>> buffer) {
		return evaluateCondition(clear, object, buffer);
	}
	
	private Boolean evaluateCondition(RelationalStateExpression<ITimeInterval> condition,Tuple<ITimeInterval> object, List<Tuple<ITimeInterval>> buffer) {
		try {
			return (Boolean) condition.evaluate(object, (List<ISession>) null, buffer);
		}catch(NullPointerException npe) {
			return false;
		}
	}

	@Override
	protected void transferNested(List<Tuple<ITimeInterval>> nestedElements, boolean keepTimeOrder) {
		Tuple<ITimeInterval> out = new Tuple<ITimeInterval>(1, true);
		// get the meta data from the last element, that is part of the collection
		out.setMetadata((ITimeInterval) nestedElements.get(nestedElements.size()-1).getMetadata().clone());
		// get the start timestamp from the first element
		out.getMetadata().setStart(nestedElements.get(0).getMetadata().getStart());
		out.setAttribute(0, nestedElements);
		if (keepTimeOrder) {
			transferArea.transfer(out);			
		}else {
			transfer(out);
		}
	}
	
}
