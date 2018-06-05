package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class MinuteOfDayFunction extends AbstractUnaryDateFunction<Integer>{

	private static final long serialVersionUID = -6322377428748782517L;

	public MinuteOfDayFunction() {
		super("minuteOfDay", SDFDatatype.INTEGER);
	}
	
	@Override
	public Integer getValue() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getInputValue(0));
		return calendar.get(Calendar.MINUTE)+calendar.get(Calendar.HOUR)*60;
	}

}
