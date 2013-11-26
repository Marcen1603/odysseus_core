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
package de.uniol.inf.is.odysseus.probabilistic.continuous.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * Distribution handler to read and write continuous probabilistic distributions as Gaussian mixtures.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticDistributionHandler extends AbstractDataHandler<NormalDistributionMixture> {
	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticDistributionHandler.class);
	/** The supported data types. */
	private static final List<String> TYPES = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java. nio.ByteBuffer)
	 */
	@Override
	public final NormalDistributionMixture readData(final ByteBuffer buffer) {
		Objects.requireNonNull(buffer);
		Preconditions.checkArgument(buffer.remaining()>=4);
		NormalDistributionMixture distributionMixture = null;
		final int size = buffer.getInt();
		if (size > 0) {
			final List<Pair<Double, MultivariateNormalDistribution>> mixtures = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
			final int dimension = buffer.getInt();
			for (int m = 0; m < size; m++) {
				final double weight = buffer.getDouble();
				final double[] mean = new double[dimension];
				for (int i = 0; i < mean.length; i++) {
					mean[i] = buffer.getDouble();
				}
				final double[] entries = new double[CovarianceMatrixUtils.getCovarianceTriangleSizeFromDimension(dimension)];
				for (int i = 0; i < entries.length; i++) {
					entries[i] = buffer.getDouble();
				}
				try {
					final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(mean, CovarianceMatrixUtils.toMatrix(entries).getData());
					mixtures.add(new Pair<Double, MultivariateNormalDistribution>(weight, distribution));
				} catch (final Exception e) {
					ProbabilisticDistributionHandler.LOG.warn(e.getMessage(), e);
				}

			}
			final double scale = buffer.getDouble();
			final Interval[] support = new Interval[dimension];
			for (int i = 0; i < support.length; i++) {
				support[i] = new Interval(buffer.getDouble(), buffer.getDouble());
			}

			distributionMixture = new NormalDistributionMixture(mixtures);
			distributionMixture.setScale(scale);
			distributionMixture.setSupport(support);
		}
		return distributionMixture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java. io.ObjectInputStream)
	 */
	@Override
	public final NormalDistributionMixture readData(final ObjectInputStream inputStream) throws IOException {
		Objects.requireNonNull(inputStream);
		Preconditions.checkArgument(inputStream.available()>=4);
		final int size = inputStream.readInt();
		final List<Pair<Double, MultivariateNormalDistribution>> mixtures = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
		final int dimension = inputStream.readInt();
		for (int m = 0; m < size; m++) {
			final double weight = inputStream.readDouble();
			final double[] mean = new double[dimension];
			for (int i = 0; i < mean.length; i++) {
				mean[i] = inputStream.readDouble();
			}
			final double[] entries = new double[dimension];
			for (int i = 0; i < entries.length; i++) {
				entries[i] = inputStream.readDouble();
			}

			final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(mean, CovarianceMatrixUtils.toMatrix(entries).getData());
			mixtures.add(new Pair<Double, MultivariateNormalDistribution>(weight, distribution));
		}
		final double scale = inputStream.readDouble();
		final Interval[] support = new Interval[dimension];
		for (int i = 0; i < support.length; i++) {
			support[i] = new Interval(inputStream.readDouble(), inputStream.readDouble());
		}

		final NormalDistributionMixture distributionMixture = new NormalDistributionMixture(mixtures);
		distributionMixture.setScale(scale);
		distributionMixture.setSupport(support);

		return distributionMixture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java. lang.String)
	 */
	@Override
	public final NormalDistributionMixture readData(final String string) {
		Objects.requireNonNull(string);
		Preconditions.checkArgument(!string.isEmpty());
		final String[] covarianceMatrix = string.split(":");
		final double[] entries = new double[covarianceMatrix.length];
		for (int i = 0; i < covarianceMatrix.length; i++) {
			entries[i] = Double.parseDouble(covarianceMatrix[i]);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#writeData(java .nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	public final void writeData(final ByteBuffer buffer, final Object data) {
		Objects.requireNonNull(buffer);
		Objects.requireNonNull(data);
		final NormalDistributionMixture value = (NormalDistributionMixture) data;
		buffer.putInt(value.getMixtures().getComponents().size());
		buffer.putInt(value.getDimension());

		for (final Pair<Double, MultivariateNormalDistribution> entry : value.getMixtures().getComponents()) {
			buffer.putDouble(entry.getKey());
			final double[] mean = entry.getValue().getMeans();
			for (final double element : mean) {
				buffer.putDouble(element);
			}
			final double[] entries = CovarianceMatrixUtils.fromMatrix(entry.getValue().getCovariances());
			for (final double entrie : entries) {
				buffer.putDouble(entrie);
			}
		}
		buffer.putDouble(value.getScale());

		final Interval[] support = value.getSupport();
		for (final Interval element : support) {
			buffer.putDouble(element.inf());
			buffer.putDouble(element.sup());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#memSize(java.lang .Object)
	 */
	@Override
	public final int memSize(final Object attribute) {
		Objects.requireNonNull(attribute);
		final NormalDistributionMixture value = (NormalDistributionMixture) attribute;
		final int numberOfMixtures = value.getMixtures().getComponents().size();
		final int dimension = value.getDimension();
		final int covarianceMatrixSize = (int) (-0.5 + Math.sqrt(0.25 + (dimension * 2)));
		// Number of mixtures: 1
		// Dimension: 1
		// Probability for each mixtue: 1 * numberOfMixtures
		// Mean of distribution: dimension * numberOfMixtures
		// Covariance matrix: numberOfMixtures * covarianceMatrixSize
		// Scale: 1
		// Support for each dimension: dimension * 2
		return ((2 * Integer.SIZE) + (Double.SIZE * (numberOfMixtures + (numberOfMixtures * dimension) + (numberOfMixtures * covarianceMatrixSize) + 1 + (2 * dimension)))) / 8;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getInstance (de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
	 */
	@Override
	protected final IDataHandler<NormalDistributionMixture> getInstance(final SDFSchema schema) {
		return new ProbabilisticDistributionHandler();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler# getSupportedDataTypes()
	 */
	@Override
	public final List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(ProbabilisticDistributionHandler.TYPES);
	}

	@Override
	public final Class<?> createsType() {
		return NormalDistributionMixture.class;
	}
}
