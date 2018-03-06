package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

public class AddDayFunction extends AbstractAddDateFunction {

	private static final long serialVersionUID = -1990168507355080275L;

	public AddDayFunction(){
		super("addDay");
	}
	
	@Override
	public Date getValue() {
		return add(Calendar.DAY_OF_MONTH);
	}

}
