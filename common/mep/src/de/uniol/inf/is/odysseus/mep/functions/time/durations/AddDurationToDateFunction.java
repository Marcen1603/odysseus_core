package de.uniol.inf.is.odysseus.mep.functions.time.durations;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * Adds a duration (seconds and nanoseconds) to a date. See the Java Duration
 * class for more details.
 * 
 * @author Tobias Brandt
 *
 */
public class AddDurationToDateFunction extends AbstractBinaryOperator<Date> {

	private static final long serialVersionUID = -8204832470283432772L;
	private static SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.DATE }, { SDFDatatype.DURATION } };

	public AddDurationToDateFunction() {
		super("+", accTypes, SDFDatatype.DATE);
	}

	@Override
	public Date getValue() {
		// Work with the old date format pre Java 8
		Date date = this.getInputValue(0);
		Instant instant = date.toInstant();

		// Add a period with the new duration class
		Duration duration = this.getInputValue(1);
		Temporal newTemporal = duration.addTo(instant);

		// It should always return an instant if we put an instant in
		if (newTemporal instanceof Instant) {
			Instant newInstant = (Instant) newTemporal;
			return Date.from(newInstant);
		}

		return null;
	}

	@Override
	public boolean isCommutative() {
		return false;
	}

	@Override
	public boolean isAssociative() {
		return false;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Date> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Date> operator) {
		return false;
	}

	@Override
	public int getPrecedence() {
		return 0;
	}

	@Override
	public ASSOCIATIVITY getAssociativity() {
		return null;
	}
}
