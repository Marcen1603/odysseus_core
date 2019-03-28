package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Sets the second of a given date.
 * 
 * @author Tobias Brandt
 *
 */
public class SetSecondFunction extends AbstractFunction<Date> {

	private static final long serialVersionUID = -1186564029238822595L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.DATE },
			{ SDFDatatype.INTEGER, SDFDatatype.LONG } };

	public SetSecondFunction() {
		super("setSecond", 2, accTypes, SDFDatatype.DATE);
	}

	@Override
	public Date getValue() {
		Date date = this.getInputValue(0);
		int second = ((Number) this.getInputValue(1)).intValue();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND, second);
		Date newDate = cal.getTime();
				
		return newDate;
	}

}
