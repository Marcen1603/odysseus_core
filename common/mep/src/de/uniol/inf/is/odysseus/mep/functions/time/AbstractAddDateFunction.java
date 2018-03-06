package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

public abstract class AbstractAddDateFunction extends AbstractDateLongFunction {

	private static final long serialVersionUID = -1990168507355080275L;

	AbstractAddDateFunction(String symbol){
		super(symbol);
	}
	
	protected Date add(int position) {
		Calendar in = Calendar.getInstance();
		int amount = ((Number)getInputValue(1)).intValue();
		in.setTime(getInputValue(0));
		in.add(position, amount);
		return in.getTime();
	}

}
