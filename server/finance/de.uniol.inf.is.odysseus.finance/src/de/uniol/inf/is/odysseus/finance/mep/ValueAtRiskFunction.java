package de.uniol.inf.is.odysseus.finance.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.finance.risk.var.model.IVaRForecaster;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * MEP Function to calculate a the value at risk by a given VaR-Model, the
 * confidence level and the time horizon.
 * 
 * @author Christoph Schröer
 *
 */
public class ValueAtRiskFunction extends AbstractFunction<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8451631288538005330L;

	/**
	 * first: the Value at Risk Forecaster second: the confidence level third:
	 * the time horizon
	 */
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.INTEGER } };

	public ValueAtRiskFunction() {
		super("calculateValueAtRisk", 3, accTypes, SDFDatatype.DOUBLE, false, 3, 5);
	}

	@Override
	public Double getValue() {
		IVaRForecaster varForecaster = (IVaRForecaster) this.getInputValue(0);
		double confidenceLevel = this.getNumericalInputValue(1);
		int timeHorizon = (int) (long) this.getInputValue(2);

		Double var = varForecaster.getVaR(confidenceLevel, timeHorizon);

		return var;
	}

}
