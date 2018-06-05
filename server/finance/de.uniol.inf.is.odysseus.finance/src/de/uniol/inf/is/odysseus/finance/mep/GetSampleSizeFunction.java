package de.uniol.inf.is.odysseus.finance.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.finance.risk.var.model.NumericalVaRForecaster;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * MEP Function to get the current sample size of a
 * {@link NumericalVaRForecaster}
 * 
 * 
 * @author Christoph Schröer
 *
 */
public class GetSampleSizeFunction extends AbstractFunction<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8451631288538005330L;

	/**
	 * first: the Value at Risk Forecaster
	 */
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT } };

	public GetSampleSizeFunction() {
		super("getSampleSize", 1, accTypes, SDFDatatype.INTEGER, false, 1, 2);
	}

	@Override
	public Integer getValue() {
		NumericalVaRForecaster varForecaster = (NumericalVaRForecaster) this.getInputValue(0);
		Integer sampleSize = varForecaster.getSample().size();
		return sampleSize;
	}

}
