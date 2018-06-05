package de.uniol.inf.is.odysseus.timeseries.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * Calculates an end timestamp by a given time horizon and metadata.
 * 
 * 
 * This is useful, when the original timestamp of the estimation date should be
 * reset.
 * 
 * @author Christoph Schröer
 *
 */
public class EstimatedEndFunction extends AbstractFunction<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8451631288538005330L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.INTEGER },
			{ SDFDatatype.TIMESTAMP }, { SDFDatatype.TIMESTAMP } };

	public EstimatedEndFunction() {
		super("getEstimatedEnd", 3, accTypes, SDFDatatype.TIMESTAMP, false, 1, 1);
	}

	@Override
	public Long getValue() {
		Double timeHorizon = this.getNumericalInputValue(0);
		long start = this.getInputValue(1);
		long end = this.getInputValue(2);

		long estimatedEnd = (long) (end - ((end - start) * timeHorizon));

		return estimatedEnd;

	}

}
