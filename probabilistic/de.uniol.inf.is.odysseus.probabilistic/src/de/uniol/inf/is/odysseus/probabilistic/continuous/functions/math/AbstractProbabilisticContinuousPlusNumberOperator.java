package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math;

import java.util.Map;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractProbabilisticContinuousPlusNumberOperator extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4535153232522764588L;

	@Override
	public int getPrecedence() {
		return 6;
	}

	@Override
	public String getSymbol() {
		return "+";
	}

	protected NormalDistributionMixture getValueInternal(final NormalDistributionMixture a, final Double b) {
		NormalDistributionMixture result = a.clone();
		result.getMixtures().clear();
		for (Map.Entry<MultivariateNormalDistribution, Double> entry : a.getMixtures().entrySet()) {
			double[] means = entry.getKey().getMeans();
			for (int i = 0; i < means.length; i++) {
				means[i] += b;
			}
			result.getMixtures().put(new MultivariateNormalDistribution(means, entry.getKey().getCovariances().getData().clone()), entry.getValue());
		}
		Interval[] support = new Interval[result.getSupport().length];
		for (int i = 0; i < result.getSupport().length; i++) {
			support[i] = result.getSupport(i).add(b);
		}
		result.setSupport(support);
		return result;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

	@Override
	public boolean isLeftDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return false;
	}

}
