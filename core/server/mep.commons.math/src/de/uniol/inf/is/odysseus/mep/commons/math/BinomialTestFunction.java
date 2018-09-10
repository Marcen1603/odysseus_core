package de.uniol.inf.is.odysseus.mep.commons.math;

import org.apache.commons.math3.stat.inference.AlternativeHypothesis;
import org.apache.commons.math3.stat.inference.BinomialTest;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 *
 * @author Christoph Schröer
 *
 */
public class BinomialTestFunction extends AbstractFunction<Boolean> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8451631288538005330L;

	/**
	 * first: the Value at Risk Forecaster second: the confidence level third:
	 * the time horizon
	 */
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.INTEGER },
			{ SDFDatatype.INTEGER }, { SDFDatatype.DOUBLE }, { SDFDatatype.STRING }, { SDFDatatype.DOUBLE } };

	public BinomialTestFunction() {
		super("binomialTest", 5, accTypes, SDFDatatype.TIMESTAMP, false, 1, 1);
	}

	@Override
	public Boolean getValue() {
		int numberOfTrials = (int) (long) this.getInputValue(0);
		Double numberOfSuccesses = this.getInputValue(1);
		int numberOfSuccessesInt = numberOfSuccesses.intValue();
		double probability = this.getInputValue(2);
		String alternativeHypothesisStr = this.getInputValue(3);
		AlternativeHypothesis alternativeHypothesis = AlternativeHypothesis.valueOf(alternativeHypothesisStr);
		double alpha = this.getInputValue(4);

		BinomialTest test = new BinomialTest();
		boolean rejection = test.binomialTest(numberOfTrials, numberOfSuccessesInt, probability, alternativeHypothesis,
				alpha);

		return rejection;

	}

}
