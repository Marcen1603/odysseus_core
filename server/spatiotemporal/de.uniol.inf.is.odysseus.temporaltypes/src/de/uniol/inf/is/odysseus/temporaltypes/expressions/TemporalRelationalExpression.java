package de.uniol.inf.is.odysseus.temporaltypes.expressions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

public class TemporalRelationalExpression<T extends IValidTime> extends RelationalExpression<T> {

	private static final long serialVersionUID = 7516261668144789244L;

	public TemporalRelationalExpression(SDFExpression expression) {
		super(expression);
	}

	@Override
	public Object evaluate(Tuple<T> object, List<ISession> sessions, List<Tuple<T>> history) {
		
		Map<PointInTime, Object> results = new HashMap<>();
		
		PointInTime validStart = object.getMetadata().getValidStart();
		PointInTime validEnd = object.getMetadata().getValidEnd();
		
		for (PointInTime i = validStart.clone(); i.before(validEnd); i.plus(1)) {
			Tuple<T> nonTemporalObject = evaluateTemporalAttributes(object, i);
			Object result = super.evaluate(nonTemporalObject, sessions, history);
			results.put(i.clone(), result);
		}
		
		return results;
	}
	
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

}
