package de.uniol.inf.is.odysseus.core.server.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

public class WeekdayFunction extends AbstractDateFunction {

	private static final long serialVersionUID = -3067241277049264670L;

	@Override
	public String getSymbol() {
		return "weekday";
	}

	@Override
	public Integer getValue() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getInputValue(0));
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

}
