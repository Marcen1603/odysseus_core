package de.uniol.inf.is.odysseus.mep.functions.time.periods;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * 
 * Adds a period of years, months and days to a date. See the Java Periods class
 * for more details.
 * 
 * @author Tobias Brandt
 *
 */
public class AddPeriodToDateFunction extends AbstractBinaryOperator<Date> {

	private static final long serialVersionUID = -3351027784224773414L;
	private static SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.DATE }, { SDFDatatype.PERIOD } };

	public AddPeriodToDateFunction() {
		super("+", accTypes, SDFDatatype.DATE);
	}

	@Override
	public Date getValue() {

		// Work with the old date format pre Java 8
		Date date = this.getInputValue(0);
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Add a period with the new period class
		Period period = this.getInputValue(1);
		Temporal newTemporal = period.addTo(localDate);

		// Create a new date
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(newTemporal.get(ChronoField.YEAR), newTemporal.get(ChronoField.MONTH_OF_YEAR),
				newTemporal.get(ChronoField.DAY_OF_MONTH));
		Date newDate = cal.getTime();

		return newDate;
	}

	@Override
	public boolean isCommutative() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAssociative() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Date> operator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Date> operator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getPrecedence() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ASSOCIATIVITY getAssociativity() {
		// TODO Auto-generated method stub
		return null;
	}

}
