package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

public class AddMonthFunction extends AbstractAddDateFunction {

	private static final long serialVersionUID = -1990168507355080275L;

	public AddMonthFunction(){
		super("addMonth");
	}
	
	@Override
	public Date getValue() {
		return add(Calendar.MONTH);
	}

}
