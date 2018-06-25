package de.uniol.inf.is.odysseus.temporaltypes.physicalopertor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
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
public class TemporalSelectPO<T extends Tuple<IValidTimes>> extends SelectPO<T> {
	
	private TimeUnit streamBaseTimeUnit;

	public TemporalSelectPO(RelationalExpression<IValidTimes> expression, TimeUnit streamBaseTimeUnit) {
		super(expression);
		this.streamBaseTimeUnit = streamBaseTimeUnit;
	}

	public TemporalSelectPO(boolean predicateIsUpdateable, RelationalExpression<IValidTimes> expression, TimeUnit streamBaseTimeUnit) {
		super(predicateIsUpdateable, expression);
		this.streamBaseTimeUnit = streamBaseTimeUnit;
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
		TimeUnit predictionTimeUnit = object.getMetadata().getPredictionTimeUnit();
		List<IValidTime> validTimeIntervals = constructValidTimeIntervals(temporalType, predictionTimeUnit);
		T newObject = createOutputTuple(object, validTimeIntervals);
		if (validTimeIntervals.size() > 0) {
			transfer(newObject);
		}
	}

	/**
	 * Combines the valid times into one metadata object for valid times, puts this
	 * into a new (copied) stream object) and returns this new object.
	 * 
	 * @param originalStreamObject
	 * @param validTimeIntervals
	 * @return
	 */
	private T createOutputTuple(T originalStreamObject, List<IValidTime> validTimeIntervals) {
		@SuppressWarnings("unchecked")
		T newObject = (T) originalStreamObject.clone();
		IValidTimes validTimes = (IValidTimes) newObject.getMetadata();
		validTimes.clear();
		for (IValidTime validTime : validTimeIntervals) {
			validTimes.addValidTime(validTime);
		}
		return newObject;
	}

	private List<IValidTime> constructValidTimeIntervals(GenericTemporalType<Boolean> temporalType, TimeUnit predictionTimeUnit) {
		List<IValidTime> validTimeIntervals = new ArrayList<>();
		IValidTime currentInterval = null;
		PointInTime lastTime = null;

		/*
		 * Loop over all single results and build the time intervals for the ValidTime.
		 * If there is no valid time interval, nothing will be transferred.
		 */
		// streamTime -> stream base time is used here, e.g., milliseconds
		for (PointInTime inStreamTime : temporalType.getValues().keySet()) {
			
			// predictionTime -> prediction base time is used here, e.g., seconds
			long predictionTime = predictionTimeUnit.convert(inStreamTime.getMainPoint(), streamBaseTimeUnit);
			PointInTime inPredictionTime = new PointInTime(predictionTime);
			
			Object timeValue = temporalType.getValues().get(inStreamTime);

			/*
			 * The values are not necessarily always a boolean. E.g., an atMin function
			 * returns a reduced set of values which can be other types as well. In that
			 * case, use all the available values. In case that we get boolean, only use the
			 * ones which are true.
			 */
			boolean useValue = true;
			if (timeValue instanceof Boolean) {
				useValue = temporalType.getValues().get(inStreamTime);
			}

			if (useValue) {
				if (currentInterval == null) {
					currentInterval = new ValidTime(inPredictionTime);
				}
			} else {
				if (currentInterval != null) {
					currentInterval.setValidEnd(inPredictionTime);
					validTimeIntervals.add(currentInterval);
					currentInterval = null;
				}
			}
			lastTime = inPredictionTime;
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

		return validTimeIntervals;
	}

	/**
	 * As we know that our predicate here is a TemporalRelationalExpression, we can
	 * use the predicate from the super class as the expression in this class.
	 * 
	 * @return The predicate at a TemporalRelationalExpression
	 */
	@SuppressWarnings("unchecked")
	private RelationalExpression<IValidTimes> getExpression() {
		if (this.getPredicate() instanceof RelationalExpression<?>) {
			return (RelationalExpression<IValidTimes>) (this.getPredicate());
		}
		return null;
	}
}
