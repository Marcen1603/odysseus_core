/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class EMTISweepArea extends JoinTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> implements Cloneable {

	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(EMTISweepArea.class);
	/** The number of Gaussian mixtures. */
	private final int mixtures;
	/** The attribute positions. */
	private final int[] attributes;
	/** The convergence threshold for fitting. */
	private final double threshold;
	/** The maximum number of iterations allowed per fitting process. */
	private final int iterations;
	/** Incremental model fitting. */
	private boolean incremental = false;
	/** The predicate for model fitting. */
	private IPredicate<IStreamObject<?>> predicate;
	/** The fitted model. */
	private MixtureMultivariateNormalDistribution model;
	/** Using adaptive mode and model has to be normalized. */
	private boolean normalizeModel = false;

	/**
	 * Creates a new sweep area for Expectation Maximization (EM) classification.
	 * 
	 * @param attributes
	 *            The attribute indexes to perform the classification on
	 * @param mixtures
	 *            The number of mixtures
	 * @param iterations
	 *            The maximum number of iterations allowed per fitting process
	 * @param threshold
	 *            The convergence threshold for fitting
	 * @param incremental
	 *            Flag indicating incremental fitting
	 * @param predicate
	 *            The predicate for model fitting
	 */
	public EMTISweepArea(final int[] attributes, final int mixtures, final int iterations, final double threshold, final boolean incremental, final IPredicate<IStreamObject<?>> predicate) {
		this.attributes = attributes;
		this.mixtures = mixtures;
		this.iterations = iterations;
		this.threshold = threshold;
		this.incremental = incremental;
		if (predicate != null) {
			this.predicate = predicate.clone();
			this.predicate.init();
		}
	}

	/**
	 * Class constructor.
	 * 
	 * @param emModelTISweepArea
	 *            The clone
	 * @throws IllegalAccessException Illegal access exception
	 * @throws InstantiationException Unable to instantiate the area
	 */
	public EMTISweepArea(final EMTISweepArea emModelTISweepArea) throws InstantiationException, IllegalAccessException {
		super(emModelTISweepArea);
		this.attributes = emModelTISweepArea.attributes;
		this.mixtures = emModelTISweepArea.mixtures;
		this.iterations = emModelTISweepArea.iterations;
		this.threshold = emModelTISweepArea.threshold;
		this.incremental = emModelTISweepArea.incremental;
		if (emModelTISweepArea.predicate != null) {
			this.predicate = emModelTISweepArea.predicate.clone();
			this.predicate.init();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea#insert(de.uniol .inf.is.odysseus.core.metadata.IStreamObject)
	 */
	@Override
	public final void insert(final ProbabilisticTuple<? extends ITimeInterval> s) {
		ProbabilisticTuple<? extends ITimeInterval> restricted = s.restrict(this.attributes, true);
		if ((getPredicate() == null) || (predicate.evaluate(s))) {
			super.insert(restricted);

			try {
				if ((!isIncremental()) || (getModel() == null)) {
					MixtureMultivariateNormalDistribution newModel = MultivariateNormalMixtureExpectationMaximization.estimate(getData(), mixtures);
					if (newModel != null) {
						setModel(newModel);
					}
				}
				if (getModel() != null) {
					MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(getData());
					try {
						em.fit(getModel(), getIterations(), getThreshold());
					} catch (MathIllegalArgumentException e) {
						em.fit(getModel(), getIterations(), getThreshold());
					}
					MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
					if (fittedModel != null) {
						setModel(fittedModel);
					}
				}
			} catch (MaxCountExceededException | ConvergenceException | MathIllegalArgumentException e) {
				// FIXME What to do now?
				// setModel(null);
				// throw e;
				LOG.trace(e.getMessage(), e);

			}
			if (getPredicate() != null) {
				normalizeModel = true;
			}
		} else {
			if ((getPredicate() != null) && (normalizeModel)) {
				double[] avgAttributes = getAvg();
				final List<Pair<Double, MultivariateNormalDistribution>> components = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
				for (final Pair<Double, MultivariateNormalDistribution> entry : getModel().getComponents()) {
					final MultivariateNormalDistribution normalDistribution = entry.getValue();
					final Double weight = entry.getKey();
					RealVector means = MatrixUtils.createRealVector(normalDistribution.getMeans());
					means.subtract(MatrixUtils.createRealVector(avgAttributes));
					MultivariateNormalDistribution component = new MultivariateNormalDistribution(means.toArray(), normalDistribution.getCovariances().getData());
					components.add(new Pair<Double, MultivariateNormalDistribution>(weight, component));
				}
				setModel(new MixtureMultivariateNormalDistribution(components));
				normalizeModel = false;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.AbstractSweepArea #insertAll(java.util.List)
	 */
	@Override
	public final void insertAll(final List<ProbabilisticTuple<? extends ITimeInterval>> toBeInserted) {
		super.insertAll(toBeInserted);
	}

	/**
	 * Gets the fitted model.
	 * 
	 * @return The model
	 */
	public final MixtureMultivariateNormalDistribution getModel() {
		return this.model;
	}

	/**
	 * Sets the fitted model.
	 * 
	 * @param model
	 *            the model to set
	 */
	private void setModel(final MixtureMultivariateNormalDistribution model) {
		this.model = model;
	}

	/**
	 * Gets the maximum number of iterations allowed per fitting process.
	 * 
	 * @return the iterations
	 */
	public final int getIterations() {
		return this.iterations;
	}

	/**
	 * Gets the convergence threshold for fitting.
	 * 
	 * @return the threshold
	 */
	public final double getThreshold() {
		return this.threshold;
	}

	/**
	 * Gets the dimension of the Gaussian Mixture Model.
	 * 
	 * @return The dimension
	 */
	public final int getDimension() {
		return this.attributes.length;
	}

	/**
	 * Gets the number of mixtures of the Gaussian Mixture Model.
	 * 
	 * @return The number of mixtures
	 */
	public final int getMixtures() {
		return this.mixtures;
	}

	/**
	 * Gets the model fitting predicate.
	 * 
	 * @return the predicate
	 */
	public final IPredicate<?> getPredicate() {
		return this.predicate;
	}

	/**
	 * @return the incremental
	 */
	public final boolean isIncremental() {
		return this.incremental;
	}

	/**
	 * Gets the weights array for the mixtures of the Gaussian Mixture Model. The sum of mixture (m) weights (w) is 1:
	 * 
	 * \f$\sum_{i=1}^{|m|} w_{i} = 1\f$
	 * 
	 * @return The weights.
	 */
	public final double[] getWeights() {
		double[] weights = new double[getDimension()];
		for (int c = 0; c < this.model.getComponents().size(); c++) {
			Pair<Double, MultivariateNormalDistribution> component = this.model.getComponents().get(c);
			weights[c] = component.getFirst();
		}

		return weights;
	}

	/**
	 * Gets the weight for the given mixture. If the mixture index is lower than zero or greater than the number of mixtures the result is 0.
	 * 
	 * @param component
	 *            The index of the mixture
	 * @return The weight
	 */
	public final double getWeight(final int component) {
		if ((component < 0) || (component > this.getMixtures())) {
			return 0;
		}
		return this.model.getComponents().get(component).getFirst();
	}

	/**
	 * Gets the means vectors of all mixtures.
	 * 
	 * @return The means vectors
	 */
	public final RealMatrix[] getMeans() {
		RealMatrix[] means = new RealMatrix[getDimension()];
		for (int c = 0; c < this.model.getComponents().size(); c++) {
			Pair<Double, MultivariateNormalDistribution> component = this.model.getComponents().get(c);
			means[c] = MatrixUtils.createColumnRealMatrix(component.getValue().getMeans());
		}
		return means;
	}

	/**
	 * 
	 * Gets the mean vector for the given mixture or <code>null</code> if the given mixture does not exist.
	 * 
	 * @param component
	 *            The index of the mixture
	 * @return The means vector
	 */
	public final RealMatrix getMean(final int component) {
		if ((component < 0) || (component > this.getMixtures())) {
			return null;
		}
		return MatrixUtils.createColumnRealMatrix(this.model.getComponents().get(component).getValue().getMeans());
	}

	/**
	 * Gets the covariance matrixes of all mixtures.
	 * 
	 * @return The covariance matrixes
	 */
	public final RealMatrix[] getCovarianceMatrices() {
		RealMatrix[] covariances = new RealMatrix[getDimension()];
		for (int c = 0; c < this.model.getComponents().size(); c++) {
			Pair<Double, MultivariateNormalDistribution> component = this.model.getComponents().get(c);
			covariances[c] = component.getValue().getCovariances();
		}
		return covariances;
	}

	/**
	 * Gets the covariance matrix for the given mixture or <code>null</code> if the given mixture does not exist.
	 * 
	 * @param component
	 *            The index of the mixture
	 * @return The covariance matrix
	 */
	public final RealMatrix getCovarianceMatrix(final int component) {
		if ((component < 0) || (component > this.getMixtures())) {
			return null;
		}
		return this.model.getComponents().get(component).getValue().getCovariances();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final EMTISweepArea clone() {
		try {
			return new EMTISweepArea(this);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the data in the current sweep area.
	 * 
	 * @return The data.
	 */
	private double[][] getData() {
		final double[][] data = new double[this.getElements().size()][this.getDimension()];
		for (int i = 0; i < this.getElements().size(); i++) {
			for (int d = 0; d < this.getDimension(); d++) {
				data[i][d] = ((Number) this.getElements().get(i).getAttribute(d)).doubleValue();
			}
		}
		return data;
	}

	/**
	 * Gets the average of each dimension.
	 * 
	 * @return The average of each dimension
	 */
	private double[] getAvg() {
		double[] avg = new double[this.getDimension()];
		for (int i = 0; i < this.getElements().size(); i++) {
			for (int d = 0; d < this.getDimension(); d++) {
				avg[d] += ((Number) this.getElements().get(i).getAttribute(d)).doubleValue();
			}
		}
		for (int d = 0; d < this.getDimension(); d++) {
			avg[d] /= this.getElements().size();
		}
		return avg;
	}

}
