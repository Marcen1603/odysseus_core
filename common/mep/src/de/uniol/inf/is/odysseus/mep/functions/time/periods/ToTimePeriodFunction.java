package de.uniol.inf.is.odysseus.mep.functions.time.periods;

import java.time.Period;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Creates a period based on years, months and days.
 * 
 * @author Tobias Brandt
 *
 */
public class ToTimePeriodFunction extends AbstractFunction<Period> {

	private static final long serialVersionUID = -2860630970754621210L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.INTEGER, SDFDatatype.LONG },
			{ SDFDatatype.INTEGER, SDFDatatype.LONG }, { SDFDatatype.INTEGER, SDFDatatype.LONG } };

	public ToTimePeriodFunction() {
		super("toTimePeriod", 3, accTypes, SDFDatatype.PERIOD);
	}

	@Override
	public Period getValue() {
		int years = ((Number) this.getInputValue(0)).intValue();
		int months = ((Number) this.getInputValue(1)).intValue();
		int days = ((Number) this.getInputValue(2)).intValue();
		return Period.of(years, months, days);
	}

}
