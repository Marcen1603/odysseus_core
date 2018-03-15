package de.uniol.inf.is.odysseus.temporaltypes.physicalopertor;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.temporaltypes.expressions.TemporalRelationalExpression;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.ValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.types.GenericTemporalType;

public class TemporalSelectPO<T extends Tuple<IValidTime>> extends SelectPO<T> {

	TemporalRelationalExpression<IValidTime> expression;

	public TemporalSelectPO(TemporalRelationalExpression<IValidTime> expression) {
		super(expression);
		this.expression = expression;
	}

	public TemporalSelectPO(boolean predicateIsUpdateable, TemporalRelationalExpression<IValidTime> expression) {
		super(predicateIsUpdateable, expression);
		this.expression = expression;
	}

	@Override
	protected void process_next(T object, int port) {

		Object expressionResult = expression.evaluate(object, this.getSessions(), null);
		if (!(expressionResult instanceof GenericTemporalType)) {
			return;
		}

		GenericTemporalType<Boolean> temporalType = (GenericTemporalType<Boolean>) expressionResult;
		List<IValidTime> validTimeIntervals = new ArrayList<>();
		IValidTime currentInterval = null;

		PointInTime lastTime = null;
		for (PointInTime time : temporalType.getValues().keySet()) {
			boolean value = temporalType.getValues().get(time);
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
		
		for (IValidTime validTime : validTimeIntervals) {
			T newObject = (T) object.clone();
			newObject.getMetadata().setValidStartAndEnd(validTime.getValidStart(), validTime.getValidEnd());
			transfer(newObject);
		}
	}
}
