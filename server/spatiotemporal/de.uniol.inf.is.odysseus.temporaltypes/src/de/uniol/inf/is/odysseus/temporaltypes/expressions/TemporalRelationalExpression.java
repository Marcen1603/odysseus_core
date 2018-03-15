package de.uniol.inf.is.odysseus.temporaltypes.expressions;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
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
public class TemporalRelationalExpression<T extends IValidTime> extends RelationalExpression<T> {

	private static final long serialVersionUID = 7516261668144789244L;

	public TemporalRelationalExpression(SDFExpression expression) {
		super(expression);
	}

	public TemporalRelationalExpression(RelationalExpression<T> expression) {
		super(expression);
	}

	@Override
	public Object evaluate(Tuple<T> object, List<ISession> sessions, List<Tuple<T>> history) {

		/*
		 * The result is always a temporal type. As it is not possible to always
		 * generate a certain function or so, the result is generic by using a map.
		 */
		GenericTemporalType<Object> temporalType = new GenericTemporalType<>();

		PointInTime validStart = object.getMetadata().getValidStart();
		PointInTime validEnd = object.getMetadata().getValidEnd();

		/*
		 * Iterate over the whole valid time interval and evaluate the expression for
		 * each point in time. This is done by calculating the values of the temporal
		 * types at the points in time, fill a Tuple with it and do the normal
		 * evaluation process for this filled tuple.
		 */
		for (PointInTime i = validStart.clone(); i.before(validEnd); i = i.plus(1)) {
			Tuple<T> nonTemporalObject = evaluateTemporalAttributes(object, i);
			Object result = super.evaluate(nonTemporalObject, sessions, history);
			temporalType.setValue(i, result);
		}

		return temporalType;
	}

	/**
	 * Copies the given tuple, searches for temporal attributes and fills the
	 * temporal attributes with the non-temporal counterparts for the given point in
	 * time.
	 * 
	 * @param object
	 *            The tuple with the temporal attributes to fill
	 * @param time
	 *            The point in time to which the temporal functions shall be
	 *            evaluated
	 * @return A copy of the given tuple with the temporal attributes filled for the
	 *         given point in time
	 */
	private Tuple<T> evaluateTemporalAttributes(Tuple<T> object, PointInTime time) {

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
		return new TemporalRelationalExpression<>(this);
	}

}
