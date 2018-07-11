package de.uniol.inf.is.odysseus.temporaltypes.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.expression.VarHelper;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.temporaltypes.merge.ValidTimesIntersectionMetadataMergeFunction;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.GenericTemporalType;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

/**
 * This class extends the relational expression so that expressions with
 * temporal attributes are supported. This type of expression shall only be used
 * if there is at least one temporal attribute in the expression. The result of
 * a temporal expression is always a temporal type, not a normal type.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class TemporalRelationalExpression<T extends IValidTimes> extends RelationalExpression<T> {

	private static final long serialVersionUID = 7516261668144789244L;

	private TimeUnit streamBaseTimeUnit;

	public TemporalRelationalExpression(SDFExpression expression, TimeUnit streamBaseTimeUnit) {
		super(expression);
		this.streamBaseTimeUnit = streamBaseTimeUnit;
	}

	public TemporalRelationalExpression(RelationalExpression<T> expression, TimeUnit streamBaseTimeUnit) {
		super(expression);
		this.streamBaseTimeUnit = streamBaseTimeUnit;
	}

	@Override
	public Object evaluate(Tuple<T> object, List<ISession> sessions, List<Tuple<T>> history) {

		/*
		 * The result is always a temporal type. As it is not possible to always
		 * generate a certain function or so, the result is generic by using a map.
		 */
		GenericTemporalType<Object> temporalType = new GenericTemporalType<>();

		/*
		 * The trust function for the resulting object is created by using the min trust
		 * of all involved temporal attributes
		 */
		GenericTemporalType<Double> temporalTrust = new GenericTemporalType<>();
		List<TemporalType<?>> temporalAttributes = getTemporalAttributes(object);

		/*
		 * Iterate over the whole valid time intervals and evaluate the expression for
		 * each point in time. This is done by calculating the values of the temporal
		 * types at the points in time, fill a Tuple with it and do the normal
		 * evaluation process for this filled tuple.
		 */
		T metadata = object.getMetadata();
		List<IValidTime> validTimes = metadata.getValidTimes();
		TimeUnit predictionTimeUnit = metadata.getPredictionTimeUnit();
		if (predictionTimeUnit == null) {
			// The prediction time unit does not differ from the stream time unit
			predictionTimeUnit = streamBaseTimeUnit;
		}

		for (IValidTime validTime : validTimes) {
			PointInTime validStart = validTime.getValidStart();
			PointInTime validEnd = validTime.getValidEnd();

			for (PointInTime i = validStart.clone(); i.before(validEnd); i = i.plus(1)) {

				/*
				 * Convert this point in time to the stream time, as all prediction functions
				 * use the stream time.
				 */
				long streamTime = streamBaseTimeUnit.convert(i.getMainPoint(), predictionTimeUnit);
				PointInTime inStreamTime = new PointInTime(streamTime);

				Tuple<T> nonTemporalObject = this.atTimeInstance(object, inStreamTime);
				Object result = super.evaluate(nonTemporalObject, sessions, history);

				/*
				 * Store the value in the stream time, not the prediction time, as we convert
				 * the times to the stream time before accessing them.
				 */
				temporalType.setValue(inStreamTime, result);
				temporalTrust.setValue(inStreamTime, getMinTrust(temporalAttributes, inStreamTime));
			}
		}

		temporalTrust.setTrustFunction(temporalTrust);
		return temporalType;
	}

	/**
	 * @return The minimal trust value of all given temporal attributes at this
	 *         point in time
	 */
	private double getMinTrust(List<TemporalType<?>> temporalAttributes, PointInTime time) {
		double minTrust = Double.MAX_VALUE;
		for (TemporalType<?> temporalAttribute : temporalAttributes) {
			double trust = temporalAttribute.getTrust(time);
			if (minTrust > trust) {
				minTrust = trust;
			}
		}
		return minTrust;
	}

	/**
	 * @return All the attributes involved in the expression that are temporal
	 */
	private List<TemporalType<?>> getTemporalAttributes(Tuple<T> object) {
		List<TemporalType<?>> temporalAttributes = new ArrayList<>();
		for (int i = 0; i < this.variables.length; i++) {
			VarHelper thisVar = this.variables[i];
			int pos = thisVar.getPos();
			Object attribute = object.getAttribute(pos);
			if (attribute instanceof TemporalType) {
				TemporalType<?> tempAttribute = (TemporalType<?>) attribute;
				temporalAttributes.add(tempAttribute);
			}
		}

		return temporalAttributes;
	}

	@Override
	public Object evaluate(Tuple<T> left, Tuple<T> right, List<ISession> sessions, List<Tuple<T>> history) {

		ValidTimesIntersectionMetadataMergeFunction mergeFunction = new ValidTimesIntersectionMetadataMergeFunction(
				false);
		IValidTimes resultValidTimes = (IValidTimes) left.getMetadata().clone();
		mergeFunction.mergeInto(resultValidTimes, (IValidTimes) left.getMetadata(), (IValidTimes) right.getMetadata());

		/*
		 * The result is always a temporal type. As it is not possible to always
		 * generate a certain function or so, the result is generic by using a map.
		 */
		GenericTemporalType<Object> temporalType = new GenericTemporalType<>();

		for (IValidTime validTime : resultValidTimes.getValidTimes()) {
			PointInTime validStart = validTime.getValidStart();
			PointInTime validEnd = validTime.getValidEnd();

			for (PointInTime i = validStart.clone(); i.before(validEnd); i = i.plus(1)) {
				/*
				 * Convert this point in time to the stream time, as all prediction functions
				 * use the stream time.
				 */
				long streamTime = streamBaseTimeUnit.convert(i.getMainPoint(),
						resultValidTimes.getPredictionTimeUnit());
				PointInTime inStreamTime = new PointInTime(streamTime);

				Tuple<T> leftNonTemporalObject = this.atTimeInstance(left, inStreamTime);
				Tuple<T> rightNonTemporalObject = this.atTimeInstance(right, inStreamTime);
				Object result = super.evaluate(leftNonTemporalObject, rightNonTemporalObject, sessions, history);

				/*
				 * Store the value in the stream time, not the prediction time, as we convert
				 * the times to the stream time before accessing them.
				 */
				temporalType.setValue(inStreamTime, result);
			}
		}

		return temporalType;
	}

	/**
	 * Copies the given tuple, searches for temporal attributes and fills the
	 * temporal attributes with the non-temporal counterparts for the given point in
	 * time.
	 * 
	 * @param object The tuple with the temporal attributes to fill
	 * @param time   The point in time to which the temporal functions shall be
	 *               evaluated
	 * @return A copy of the given tuple with the temporal attributes filled for the
	 *         given point in time
	 */
	private Tuple<T> atTimeInstance(Tuple<T> object, PointInTime time) {

		Tuple<T> nonTemporalTuple = object.clone();

		for (int i = 0; i < object.getAttributes().length; i++) {
			Object attribute = object.getAttribute(i);
			if (attribute instanceof TemporalType) {
				@SuppressWarnings("rawtypes")
				TemporalType temporalType = (TemporalType) attribute;
				Object value = temporalType.getValue(time);
				nonTemporalTuple.setAttribute(i, value);
			}
		}

		return nonTemporalTuple;
	}

	@Override
	public TemporalRelationalExpression<T> clone() {
		return new TemporalRelationalExpression<>(this, this.streamBaseTimeUnit);
	}

}
