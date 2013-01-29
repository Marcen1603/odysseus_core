package de.uniol.inf.is.odysseus.probabilistic.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * Distribution handler to read and write continuous probabilistic distributions
 * as Gaussian mixtures
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticDistributionHandler extends
		AbstractDataHandler<NormalDistributionMixture> {
	static protected List<String> types = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.
	 * nio.ByteBuffer)
	 */
	@Override
	public NormalDistributionMixture readData(ByteBuffer buffer) {
		final int size = buffer.getInt();
		final Map<NormalDistribution, Double> mixtures = new HashMap<NormalDistribution, Double>(
				size);
		final int dimension = buffer.getInt();
		for (int m = 0; m < size; m++) {
			final double weight = buffer.getDouble();
			final double[] mean = new double[dimension];
			for (int i = 0; i < mean.length; i++) {
				mean[i] = buffer.getDouble();
			}
			final double[] entries = new double[CovarianceMatrixUtils
					.getCovarianceTiangleSizeFromDimension(dimension)];
			for (int i = 0; i < entries.length; i++) {
				entries[i] = buffer.getDouble();
			}

			CovarianceMatrix covarianceMatrix = new CovarianceMatrix(entries);
			NormalDistribution distribution = new NormalDistribution(mean,
					covarianceMatrix);
			mixtures.put(distribution, weight);
		}
		double scale = buffer.getDouble();
		Interval[] support = new Interval[dimension];
		for (int i = 0; i < support.length; i++) {
			support[i] = new Interval(buffer.getDouble(), buffer.getDouble());
		}

		NormalDistributionMixture distributionMixture = new NormalDistributionMixture(
				mixtures);
		distributionMixture.setScale(scale);
		distributionMixture.setSupport(support);

		return distributionMixture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.
	 * io.ObjectInputStream)
	 */
	@Override
	public NormalDistributionMixture readData(ObjectInputStream inputStream)
			throws IOException {
		final int size = inputStream.readInt();
		final Map<NormalDistribution, Double> mixtures = new HashMap<NormalDistribution, Double>(
				size);
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

			CovarianceMatrix covarianceMatrix = new CovarianceMatrix(entries);
			NormalDistribution distribution = new NormalDistribution(mean,
					covarianceMatrix);
			mixtures.put(distribution, weight);
		}
		double scale = inputStream.readDouble();
		Interval[] support = new Interval[dimension];
		for (int i = 0; i < support.length; i++) {
			support[i] = new Interval(inputStream.readDouble(),
					inputStream.readDouble());
		}

		NormalDistributionMixture distributionMixture = new NormalDistributionMixture(
				mixtures);
		distributionMixture.setScale(scale);
		distributionMixture.setSupport(support);

		return distributionMixture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.
	 * lang.String)
	 */
	@Override
	public NormalDistributionMixture readData(String string) {
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
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#writeData(java
	 * .nio.ByteBuffer, java.lang.Object)
	 */
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		final NormalDistributionMixture value = (NormalDistributionMixture) data;
		buffer.putInt(value.getMixtures().size());
		buffer.putInt(value.getDimension());

		for (Entry<NormalDistribution, Double> mixture : value.getMixtures()
				.entrySet()) {
			buffer.putDouble(mixture.getValue());
			double[] mean = mixture.getKey().getMean();
			for (int i = 0; i < mean.length; i++) {
				buffer.putDouble(mean[i]);
			}
			final double[] entries = mixture.getKey().getCovarianceMatrix()
					.getEntries();
			for (int i = 0; i < entries.length; i++) {
				buffer.putDouble(entries[i]);
			}
		}
		buffer.putDouble(value.getScale());

		Interval[] support = value.getSupport();
		for (int i = 0; i < support.length; i++) {
			buffer.putDouble(support[i].inf());
			buffer.putDouble(support[i].sup());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#memSize(java.lang
	 * .Object)
	 */
	@Override
	public int memSize(Object attribute) {
		final NormalDistributionMixture value = (NormalDistributionMixture) attribute;
		int numberOfMixtures = value.getMixtures().size();
		int dimension = value.getDimension();
		int covarianceMatrixSize = (int) (-0.5 + Math
				.sqrt(0.25 + (dimension * 2)));
		// Number of mixtures: 1
		// Dimension: 1
		// Probability for each mixtue: 1 * numberOfMixtures
		// Mean of distribution: dimension * numberOfMixtures
		// Covariance matrix: numberOfMixtures * covarianceMatrixSize
		// Scale: 1
		// Support for each dimension: dimension * 2
		return (2 * Integer.SIZE + Double.SIZE
				* (numberOfMixtures + numberOfMixtures * dimension
						+ numberOfMixtures * covarianceMatrixSize + 1 + 2 * dimension)) / 8;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getInstance
	 * (de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
	 */
	@Override
	protected IDataHandler<NormalDistributionMixture> getInstance(
			SDFSchema schema) {
		return new ProbabilisticDistributionHandler();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#
	 * getSupportedDataTypes()
	 */
	@Override
	public List<String> getSupportedDataTypes() {
		return Collections
				.unmodifiableList(ProbabilisticDistributionHandler.types);
	}

}
