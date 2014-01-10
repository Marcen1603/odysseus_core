package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

public class MinuteOfDayFunction extends AbstractDateFunction{

	private static final long serialVersionUID = -6322377428748782517L;


	@Override
	public String getSymbol() {
		return "minuteOfDay";
	}

	@Override
	public Integer getValue() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getInputValue(0));
		return calendar.get(Calendar.MINUTE)+calendar.get(Calendar.HOUR)*60;
	}


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean optimizeConstantParameter() {
        return true;
    }

}
