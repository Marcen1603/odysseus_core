package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator;

import java.util.Map;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

import com.google.common.primitives.Ints;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class SamplePO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	/** The attribute positions. */
	private final int[] attributes;
	/** The number of samples. */
	private int samples;

	/**
	 * Creates a new Sample operator.
	 * 
	 * @param attributes
	 *            The attribute positions
	 */
	public SamplePO(final int[] attributes, int samples) {
		this.attributes = attributes;
		this.samples = samples;
	}

	/**
	 * Clone constructor.
	 * 
	 * @param samplePO
	 *            The copy
	 */
	public SamplePO(final SamplePO<T> samplePO) {
		super(samplePO);
		this.attributes = samplePO.attributes.clone();
		this.samples = samplePO.samples;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#getOutputMode()
	 */
	@Override
	public final OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	protected final void process_next(final ProbabilisticTuple<T> object, final int port) {
		final NormalDistributionMixture[] distributions = object.getDistributions();
		for (int i = 0; i < this.samples; i++) {
			final ProbabilisticTuple<T> outputVal = object.clone();

			for (final int attributePos : this.attributes) {
				final NormalDistributionMixture distribution = distributions[((ProbabilisticContinuousDouble) object.getAttribute(attributePos)).getDistribution()];
				final int dimension = Ints.asList(distribution.getAttributes()).indexOf(attributePos);
				final double sample = this.sample(distribution, dimension);
				if (distribution.getSupport(dimension).contains(sample)) {
					outputVal.setAttribute(attributePos, this.sample(distribution, dimension));
				} else {
					outputVal.setAttribute(attributePos, 0.0);
				}
			}
			// KTHXBYE
			this.transfer(outputVal);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone()
	 */
	@Override
	public final AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
		return new SamplePO<T>(this);
	}

	private Double sample(final NormalDistributionMixture mixture, final int dimension) {
		double sample = 1.0;
		for (final Map.Entry<MultivariateNormalDistribution, Double> entry : mixture.getMixtures().entrySet()) {
			sample += (entry.getKey().sample()[dimension] * entry.getValue());
		}
		return sample;

	}
}
