package de.uniol.inf.is.odysseus.timeseries.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Calculates a start timestamp by a given time horizon and metadata.
 * 
 * @author Christoph Schröer
 *
 */
public class ForecastedStartFunction extends AbstractFunction<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8451631288538005330L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.INTEGER },
			{ SDFDatatype.TIMESTAMP }, { SDFDatatype.TIMESTAMP } };

	public ForecastedStartFunction() {
		super("getForecastedStart", 3, accTypes, SDFDatatype.TIMESTAMP, false, 1, 1);
	}

	@Override
	public Long getValue() {
		Double  timeHorizon = this.getNumericalInputValue(0);
		long start = this.getInputValue(1);
		long end = this.getInputValue(2);

		long forecastedStart = (long) (start + ((end - start) * timeHorizon));

		return forecastedStart;

	}

}
