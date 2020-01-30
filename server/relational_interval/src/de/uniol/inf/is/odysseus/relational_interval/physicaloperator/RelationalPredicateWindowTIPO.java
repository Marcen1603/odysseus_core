package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalStateExpression;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.PredicateWindowTIPO;

public class RelationalPredicateWindowTIPO extends PredicateWindowTIPO<Tuple<ITimeInterval>> {

	private RelationalStateExpression<ITimeInterval> start;
	private RelationalStateExpression<ITimeInterval> end;
	private RelationalStateExpression<ITimeInterval> advance;
	

	@SuppressWarnings("unchecked")
	public RelationalPredicateWindowTIPO(AbstractWindowAO windowao) {
		super(windowao);
		if (windowao.getStartCondition() != null) {
			this.start = new RelationalStateExpression<>(
					((RelationalExpression<ITimeInterval>) windowao.getStartCondition()).getExpression().clone());
			this.start.initVars(windowao.getInputSchema());
		}
		if (windowao.getEndCondition() != null) {
			this.end = new RelationalStateExpression<>(
					((RelationalExpression<ITimeInterval>) windowao.getEndCondition()).getExpression().clone());
			this.end.initVars(windowao.getInputSchema());
		}
		if (windowao.getAdvanceCondition() != null) {
			this.advance = new RelationalStateExpression<>(
					((RelationalExpression<ITimeInterval>) windowao.getAdvanceCondition()).getExpression().clone());
			this.advance.initVars(windowao.getInputSchema());			
		}
	}

	@Override
	protected Boolean evaluateStartCondition(Tuple<ITimeInterval> object, List<Tuple<ITimeInterval>> buffer) {
		return (Boolean) start.evaluate(object, (List<ISession>) null, buffer);
	}

	@Override
	protected Boolean evaluateEndCondition(Tuple<ITimeInterval> object, List<Tuple<ITimeInterval>> buffer) {
		return (Boolean) end.evaluate(object, (List<ISession>) null, buffer);
	}
	
	@Override
	protected Boolean evaluateAdvanceCondition(Tuple<ITimeInterval> object, List<Tuple<ITimeInterval>> buffer) {
		return (Boolean) advance.evaluate(object, (List<ISession>) null, buffer);
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
