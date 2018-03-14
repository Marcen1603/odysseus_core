package de.uniol.inf.is.odysseus.temporaltypes.physicalopertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.temporaltypes.expressions.TemporalRelationalExpression;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.ValidTime;

public class TemporalSelectPO<S extends IValidTime, T extends Tuple<S>> extends SelectPO<T> {

	TemporalRelationalExpression<S> expression;

	public TemporalSelectPO(TemporalRelationalExpression expression) {
		super(expression);
		this.expression = expression;
	}

	public TemporalSelectPO(boolean predicateIsUpdateable, TemporalRelationalExpression expression) {
		super(predicateIsUpdateable, expression);
		this.expression = expression;
	}

	@Override
	protected void process_next(T object, int port) {

		Object expressionResult = expression.evaluate(object, this.getSessions(), null);
		if (!(expressionResult instanceof Map)) {
			return;
		}
	
		Map<PointInTime, Boolean> temporalType = (Map) expressionResult;
		List<IValidTime> validTimeIntervals = new ArrayList<>();
		IValidTime currentInterval = null;
		
		PointInTime lastTime = null;
		for (PointInTime time : temporalType.keySet()) {
			boolean value = temporalType.get(time);
			if (value) {
				if (currentInterval == null) {
					currentInterval = new ValidTime(time);
				}
			} else {
				if (currentInterval != null) {
					currentInterval.setValidEnd(time);
					validTimeIntervals.add(currentInterval);
					currentInterval = null;
				}
			}
			lastTime = time;
		}
		
		if (lastTime != null && currentInterval != null) {
			currentInterval.setValidEnd(lastTime.plus(1));
			validTimeIntervals.add(currentInterval);
			currentInterval = null;
		}
	}

}
