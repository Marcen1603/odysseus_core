package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class WeekdayFunction extends AbstractUnaryDateFunction<Integer> {

	private static final long serialVersionUID = -3067241277049264670L;

	public WeekdayFunction() {
		super("weekday", SDFDatatype.INTEGER);
	}	

	@Override
	public Integer getValue() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getInputValue(0));
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

}
