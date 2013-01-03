package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <mixture>
 * @param <mixtureEntry>
 */
public class NormalDistributionMixture implements Serializable, Cloneable {
	/**
     * 
     */
	private static final long serialVersionUID = 8771213908605100590L;
	private int[] attributes;
	private double scale;
	private Interval[] support;
	private final Map<NormalDistribution, Double> mixtures = new HashMap<NormalDistribution, Double>();

	public NormalDistributionMixture(final double mean,
			final CovarianceMatrix covarianceMatrix) {
		this(new double[] { mean }, covarianceMatrix);
	}

	public NormalDistributionMixture(final double[] mean,
			final CovarianceMatrix covarianceMatrix) {
		int dimension = mean.length;
		this.attributes = new int[dimension];
		this.mixtures.put(new NormalDistribution(mean, covarianceMatrix), 1.0);
		this.scale = 1.0;
		this.support = new Interval[dimension];
		for (int i = 0; i < support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY);
		}
	}

	public NormalDistributionMixture(Map<NormalDistribution, Double> mixtures) {
		int dimension = 0;
		for (Entry<NormalDistribution, Double> mixture : mixtures.entrySet()) {
			dimension = mixture.getKey().getMean().length;
			this.mixtures.put(mixture.getKey(), mixture.getValue());
		}
		this.attributes = new int[dimension];
		this.scale = 1.0;
		this.support = new Interval[dimension];
		for (int i = 0; i < support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY);
		}
	}

	public NormalDistributionMixture(
			final NormalDistributionMixture normalDistributionMixture) {
		this.attributes = normalDistributionMixture.attributes.clone();
		this.scale = normalDistributionMixture.scale;
		for (NormalDistribution distribution : normalDistributionMixture.mixtures
				.keySet()) {
			this.mixtures.put(distribution.clone(),
					normalDistributionMixture.mixtures.get(distribution));
		}
		this.support = new Interval[this.attributes.length];
		for (int i = 0; i < support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY);
		}
	}

	public int getDimension() {
		return this.attributes.length;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public Interval[] getSupport() {
		return support;
	}

	public void setSupport(int dimension, Interval support) {
		this.support[dimension] = this.support[dimension].intersection(support);
	}

	public void setSupport(Interval[] support) {
		this.support = support;
	}

	public Map<NormalDistribution, Double> getMixtures() {
		return this.mixtures;
	}

	public int[] getAttributes() {
		return attributes;
	}

	public void setAttributes(int[] attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (Entry<NormalDistribution, Double> mixture : mixtures.entrySet()) {
			if (sb.length() > 1) {
				sb.append(";");
			}
			sb.append(mixture.getKey().toString()).append(":")
					.append(mixture.getValue());
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public NormalDistributionMixture clone() {
		return new NormalDistributionMixture(this);
	}

}
