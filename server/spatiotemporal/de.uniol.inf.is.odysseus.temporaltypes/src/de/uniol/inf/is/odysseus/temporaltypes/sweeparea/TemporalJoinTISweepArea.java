package de.uniol.inf.is.odysseus.temporaltypes.sweeparea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.temporaltypes.expressions.TemporalRelationalExpression;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.ValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.types.GenericTemporalType;

public class TemporalJoinTISweepArea<T extends Tuple<? extends ITimeInterval>> extends JoinTISweepArea<T> {

	private static final long serialVersionUID = 4566244293129914500L;

	private TimeUnit streamBaseTimeUnit;

	public TemporalJoinTISweepArea(TimeUnit streamBaseTimeUnit) {
		this.streamBaseTimeUnit = streamBaseTimeUnit;
	}

	@Override
	public Iterator<T> queryCopy(T element, Order order, boolean extract) {
		LinkedList<T> result = new LinkedList<T>();
		Iterator<T> iter;
		synchronized (this.getElements()) {
			switch (order) {
			case LeftRight:
				iter = this.getElements().iterator();
				while (iter.hasNext()) {
					T next = iter.next();
					if (TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())) {
						continue;
					}
					if (TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())) {
						break;
					}

					doTemporalEvaluation(element, next, result, extract, iter);
				}
				break;
			case RightLeft:
				iter = this.getElements().iterator();
				while (iter.hasNext()) {
					T next = iter.next();
					if (TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())) {
						continue;
					}
					if (TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())) {
						break;
					}

					doTemporalEvaluation(element, next, result, extract, iter);
				}
				break;
			}
		}
		return result.iterator();
	}

	private void doTemporalEvaluation(T element, T next, LinkedList<T> result, boolean extract, Iterator<T> iter) {
		GenericTemporalType resultObject = null;
		if (getQueryPredicate() instanceof TemporalRelationalExpression) {
			TemporalRelationalExpression queryPredicate2 = (TemporalRelationalExpression) getQueryPredicate();
			Object evaluationResult = queryPredicate2.evaluate(element, next, null, null);
			if (evaluationResult instanceof GenericTemporalType) {
				resultObject = (GenericTemporalType) evaluationResult;
				IValidTimes validTimesMeta = (IValidTimes) element.getMetadata();
				List<IValidTime> validTimes = constructValidTimeIntervals(resultObject,
						validTimesMeta.getPredictionTimeUnit());
				T newObject = createOutputTuple(next, validTimes);
				if (validTimes.size() > 0) {
					result.add(newObject);
					if (extract) {
						iter.remove();
					}
				}
			}
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

	private List<IValidTime> constructValidTimeIntervals(GenericTemporalType<Boolean> temporalType,
			TimeUnit predictionTimeUnit) {
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

}
