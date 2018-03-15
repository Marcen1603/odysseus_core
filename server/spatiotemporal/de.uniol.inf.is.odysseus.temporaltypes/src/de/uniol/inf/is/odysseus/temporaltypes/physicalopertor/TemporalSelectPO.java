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

/**
 * This class extends the select operator so that it can work with expressions
 * that have temporal attributes (temporal expressions / predicates).
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class TemporalSelectPO<T extends Tuple<IValidTime>> extends SelectPO<T> {

	public TemporalSelectPO(TemporalRelationalExpression<IValidTime> expression) {
		super(expression);
	}

	public TemporalSelectPO(boolean predicateIsUpdateable, TemporalRelationalExpression<IValidTime> expression) {
		super(predicateIsUpdateable, expression);
	}

	@Override
	protected void process_next(T object, int port) {

		Object expressionResult = this.getExpression().evaluate(object, this.getSessions(), null);
		if (!(expressionResult instanceof GenericTemporalType)) {
			/*
			 * A temporal expression should always return a temporal type. If this is not
			 * the case, we cannot work with the result.
			 */
			return;
		}

		@SuppressWarnings("unchecked")
		GenericTemporalType<Boolean> temporalType = (GenericTemporalType<Boolean>) expressionResult;
		List<IValidTime> validTimeIntervals = new ArrayList<>();
		IValidTime currentInterval = null;

		PointInTime lastTime = null;

		/*
		 * Loop over all single results and build the time intervals for the ValidTime.
		 * If there is no valid time interval, nothing will be transferred.
		 */
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

		/*
		 * We need to close the last interval if not already done so that it is not
		 * valid until infinity.
		 */
		if (lastTime != null && currentInterval != null) {
			currentInterval.setValidEnd(lastTime.plus(1));
			validTimeIntervals.add(currentInterval);
			currentInterval = null;
		}

		/*
		 * For each valid time interval, create a new tuple with the same stream time,
		 * the same content but different valid time intervals. It would be possible to
		 * create a single tuple with a list of valid time intervals. As for now,
		 * multiple tuples are created if there are multiple time intervals.
		 */
		for (IValidTime validTime : validTimeIntervals) {
			@SuppressWarnings("unchecked")
			T newObject = (T) object.clone();
			newObject.getMetadata().setValidStartAndEnd(validTime.getValidStart(), validTime.getValidEnd());
			transfer(newObject);
		}
	}

	/**
	 * As we know that our predicate here is a TemporalRelationalExpression, we can
	 * use the predicate from the super class as the expression in this class.
	 * 
	 * @return The predicate at a TemporalRelationalExpression
	 */
	@SuppressWarnings("unchecked")
	private TemporalRelationalExpression<IValidTime> getExpression() {
		if (this.getPredicate() instanceof TemporalRelationalExpression<?>) {
			return (TemporalRelationalExpression<IValidTime>) (this.getPredicate());
		}
		return null;
	}
}
