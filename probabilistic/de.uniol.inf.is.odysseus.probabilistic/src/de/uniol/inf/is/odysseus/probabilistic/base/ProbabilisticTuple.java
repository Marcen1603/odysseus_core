/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.base;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * A probabilistic tuple for probabilistic continuous data
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <T>
 */
public class ProbabilisticTuple<T extends IMetaAttribute> extends Tuple<T> {

	/**
     * 
     */
	private static final long serialVersionUID = -4389825802466821416L;
	/** The probabilistic distribution function (Gaussian mixture model */
	protected NormalDistributionMixture[] distributions;

	/**
	 * 
	 * @param attributeCount
	 * @param b
	 */
	public ProbabilisticTuple(final int attributeCount, final boolean b) {
		super(attributeCount, b);
		this.distributions = new NormalDistributionMixture[0];
	}

	/**
	 * 
	 * @param attributes
	 * @param b
	 */
	public ProbabilisticTuple(final Object[] attributes, final boolean b) {
		super(attributes, b);
		this.distributions = new NormalDistributionMixture[0];
	}

	/**
	 * 
	 * @param attributes
	 * @param distributions
	 * @param b
	 */
	public ProbabilisticTuple(final Object[] attributes,
			final NormalDistributionMixture[] distributions, final boolean b) {
		super(attributes, b);
		this.distributions = distributions;
	}

	/**
	 * 
	 * @param copy
	 * @param newAttrs
	 * @param b
	 */
	public ProbabilisticTuple(final ProbabilisticTuple<T> copy,
			final Object[] newAttrs, final boolean b) {
		super(copy);
		this.distributions = new NormalDistributionMixture[0];
	}

	/**
	 * 
	 * @param copy
	 * @param newAttrs
	 * @param newDistrs
	 * @param b
	 */
	public ProbabilisticTuple(final ProbabilisticTuple<T> copy,
			final Object[] newAttrs,
			final NormalDistributionMixture[] newDistrs, final boolean b) {
		super(copy, newAttrs, b);
		if (newDistrs != null) {
			this.distributions = new NormalDistributionMixture[newDistrs.length];
			for (int i = 0; i < newDistrs.length; i++) {
				this.distributions[i] = newDistrs[i].clone();
			}
		} else {
			if (copy.distributions != null) {
				for (int i = 0; i < copy.distributions.length; i++) {
					this.distributions[i] = copy.distributions[i].clone();
				}
			} else {
				this.distributions = new NormalDistributionMixture[0];
			}
		}
	}

	/**
	 * 
	 * @param copy
	 */
	public ProbabilisticTuple(final ProbabilisticTuple<T> copy) {
		super(copy);
		if (copy.distributions != null) {
			this.distributions = new NormalDistributionMixture[copy.distributions.length];
			for (int i = 0; i < copy.distributions.length; i++) {
				this.distributions[i] = copy.distributions[i].clone();
			}
		} else {
			this.distributions = new NormalDistributionMixture[0];
		}
	}

	/**
	 * 
	 * @param pos
	 * @return
	 */
	public final NormalDistributionMixture getDistribution(final int pos) {
		if ((pos < 0) || (pos > this.distributions.length)) {
			return null;
		}
		return this.distributions[pos];
	}

	/**
	 * 
	 * @param pos
	 * @param distribution
	 */
	public final void setDistribution(final int pos,
			final NormalDistributionMixture distribution) {
		this.distributions[pos] = distribution;
	}

	/**
	 * 
	 * @param distributions
	 */
	public void setDistributions(final NormalDistributionMixture[] distributions) {
		this.distributions = distributions;
	}

	/**
	 * 
	 * @return
	 */
	public NormalDistributionMixture[] getDistributions() {
		return this.distributions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.collection.Tuple#restrict(int,
	 * boolean)
	 */
	@Override
	public ProbabilisticTuple<T> restrict(final int attr,
			final boolean createNew) {
		RealMatrix restrictMatrix;
		final Object newAttr = this.attributes[attr];
		if (newAttr.getClass() == ProbabilisticContinuousDouble.class) {
			restrictMatrix = MatrixUtils
					.createRealMatrix(new double[][] { { 1.0 } });
		} else {
			restrictMatrix = null;
		}
		return this.restrict(new int[] { attr },
				new RealMatrix[] { restrictMatrix }, createNew);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.collection.Tuple#restrict(int[],
	 * boolean)
	 */
	@Override
	public ProbabilisticTuple<T> restrict(final int[] attrList,
			final boolean createNew) {
		final int[] newDistrDimensions = new int[this.distributions.length];

		int matrixes = 0;
		for (final int element : attrList) {
			final Object newAttr = this.attributes[element];
			if (newAttr.getClass() == ProbabilisticContinuousDouble.class) {
				final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) newAttr;
				final int distrIndex = value.getDistribution();
				newDistrDimensions[distrIndex]++;
				matrixes++;
			}
		}
		final RealMatrix[] restrictMatrixes = new RealMatrix[matrixes];
		for (int i = 0, j = 0; i < this.distributions.length; i++) {
			if (newDistrDimensions[i] > 0) {
				restrictMatrixes[j] = MatrixUtils.createRealMatrix(
						newDistrDimensions[i],
						this.distributions[i].getDimension());
				for (int row = 0, column = 0; (row < restrictMatrixes[j]
						.getRowDimension())
						&& (column < restrictMatrixes[j].getColumnDimension()); row++, column++) {
					restrictMatrixes[j].setEntry(row, column, 1.0);
				}
				j++;
			}
		}
		return this.restrict(attrList, restrictMatrixes, createNew);
	}

	/**
	 * 
	 * @param attr
	 * @param restrictMatrix
	 * @param createNew
	 * @return
	 */
	public ProbabilisticTuple<T> restrict(final int attr,
			final RealMatrix restrictMatrix, final boolean createNew) {
		return this.restrict(new int[] { attr },
				new RealMatrix[] { restrictMatrix }, createNew);
	}

	/**
	 * Restricts the tuple to the given list of attributes
	 * 
	 * @param attrList
	 *            The list of attribute positions
	 * @param restrictMatrix
	 *            The list of restriction matrixes for probabilistic continuous
	 *            attributes
	 * @param createNew
	 *            flag for the creation of a new tuple or reusig the existing
	 *            tuple
	 * @return The restricted tuple
	 */
	public ProbabilisticTuple<T> restrict(final int[] attrList,
			final RealMatrix[] restrictMatrix, final boolean createNew) {
		final int[] newDistrDimensions = new int[this.distributions.length];

		final Object[] newAttrs = new Object[attrList.length];
		final NormalDistributionMixture[] newDistrs = new NormalDistributionMixture[restrictMatrix.length];

		for (int i = 0; i < attrList.length; i++) {
			newAttrs[i] = this.attributes[attrList[i]];
			if (newAttrs[i].getClass() == ProbabilisticContinuousDouble.class) {
				final ProbabilisticContinuousDouble value = (ProbabilisticContinuousDouble) newAttrs[i];
				final int distrIndex = value.getDistribution();
				newDistrDimensions[distrIndex]++;
			}
		}

		int newDistrIndex = 0;
		for (int i = 0; i < this.distributions.length; i++) {
			if (newDistrDimensions[i] > 0) {
				final int oldDimension = this.distributions[i].getDimension();
				final int newDimension = restrictMatrix[newDistrIndex]
						.getRowDimension();

				newDistrs[newDistrIndex] = this.distributions[i].clone();

				final NormalDistributionMixture distr = newDistrs[newDistrIndex];
				for (final NormalDistribution mixture : distr.getMixtures()
						.keySet()) {
					final RealMatrix covarianceMatrix = CovarianceMatrixUtils
							.toMatrix(mixture.getCovarianceMatrix());

					System.out.println(restrictMatrix[newDistrIndex] + " * "
							+ covarianceMatrix + " * "
							+ restrictMatrix[newDistrIndex].transpose());
					mixture.setCovarianceMatrix(CovarianceMatrixUtils
							.fromMatrix(restrictMatrix[newDistrIndex].multiply(
									covarianceMatrix).multiply(
									restrictMatrix[newDistrIndex].transpose())));

					mixture.setMean(restrictMatrix[newDistrIndex]
							.multiply(
									MatrixUtils
											.createRealDiagonalMatrix(mixture
													.getMean()))
							.multiply(restrictMatrix[newDistrIndex].transpose())
							.getColumn(0));
				}

				final int[] oldDistrAttrList = distr.getAttributes();
				final int[] newDistrAttrList = new int[newDimension];
				final Interval[] newDistrSupportList = new Interval[newDimension];

				int dimension = 0;
				for (int j = 0; j < oldDimension; j++) {
					final int oldAttrPos = oldDistrAttrList[j];
					for (int k = 0; k < attrList.length; k++) {
						if (oldAttrPos == attrList[k]) {
							newDistrAttrList[dimension] = k;
							newDistrSupportList[dimension] = distr.getSupport()[j];
							((ProbabilisticContinuousDouble) newAttrs[k])
									.setDistribution(newDistrIndex);

							dimension++;
							break;
						}
					}
				}
				distr.setAttributes(newDistrAttrList);
				distr.setSupport(newDistrSupportList);
				newDistrIndex++;
			}
		}
		return this.restrictCreation(createNew, newAttrs, newDistrs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.collection.Tuple#process_merge(de.uniol
	 * .inf.is.odysseus.core.metadata.IStreamObject,
	 * de.uniol.inf.is.odysseus.core.metadata.IStreamObject,
	 * de.uniol.inf.is.odysseus.core.Order)
	 */
	protected IStreamObject<T> process_merge(IStreamObject<T> left,
			IStreamObject<T> right, Order order) {
		if (order == Order.LeftRight) {
			return processMergeInternal((ProbabilisticTuple<T>) left,
					(ProbabilisticTuple<T>) right);
		} else {
			return processMergeInternal((ProbabilisticTuple<T>) right,
					(ProbabilisticTuple<T>) left);
		}
	}

	/**
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	private IStreamObject<T> processMergeInternal(ProbabilisticTuple<T> left,
			ProbabilisticTuple<T> right) {

		NormalDistributionMixture[] newDistributions = mergeDistributions(
				left != null ? left.getDistributions() : null,
				right != null ? right.getDistributions() : null,
				left.getAttributes().length);

		Object[] newAttributes = mergeAttributes(
				left != null ? left.getAttributes() : null,
				right != null ? right.getAttributes() : null,
				left.getDistributions().length);

		ProbabilisticTuple<T> r = new ProbabilisticTuple<T>(newAttributes,
				newDistributions, left.requiresDeepClone()
						|| right.requiresDeepClone());
		return r;
	}

	/**
	 * 
	 * @param leftAttributes
	 * @param rightAttributes
	 * @return
	 */
	private Object[] mergeAttributes(Object[] leftAttributes,
			Object[] rightAttributes, int offset) {
		Object[] newAttributes = new Object[leftAttributes.length
				+ rightAttributes.length];
		if (leftAttributes != null) {
			System.arraycopy(leftAttributes, 0, newAttributes, 0,
					leftAttributes.length);
		}
		if (rightAttributes != null) {
			System.arraycopy(rightAttributes, 0, newAttributes,
					leftAttributes.length, rightAttributes.length);
		}
		for (int i = leftAttributes.length; i < (leftAttributes.length + rightAttributes.length); i++) {
			if (newAttributes[i].getClass() == ProbabilisticContinuousDouble.class) {
				ProbabilisticContinuousDouble value = ((ProbabilisticContinuousDouble) newAttributes[i]);
				value.setDistribution(value.getDistribution() + offset);
			}
		}
		return newAttributes;
	}

	/**
	 * 
	 * @param leftDistributions
	 * @param rightDistributions
	 * @return
	 */
	private NormalDistributionMixture[] mergeDistributions(
			NormalDistributionMixture[] leftDistributions,
			NormalDistributionMixture[] rightDistributions, int offset) {
		NormalDistributionMixture[] newDistributions = new NormalDistributionMixture[leftDistributions.length
				+ rightDistributions.length];
		if (leftDistributions != null) {
			System.arraycopy(leftDistributions, 0, newDistributions, 0,
					leftDistributions.length);
		}
		if (rightDistributions != null) {
			System.arraycopy(rightDistributions, 0, newDistributions,
					leftDistributions.length, rightDistributions.length);
		}
		for (int i = leftDistributions.length; i < (leftDistributions.length + rightDistributions.length); i++) {
			int dimension = newDistributions[i].getDimension();
			int[] newAttrsPos = new int[dimension];
			for (int j = 0; j < dimension; j++) {
				newAttrsPos[j] = newDistributions[i].getAttribute(j) + offset;
			}
			newDistributions[i].setAttributes(newAttrsPos);
		}
		return newDistributions;
	}

	/**
	 * Creates a new probabilistic tuple with the given attributes and
	 * distributions
	 * 
	 * @param createNew
	 *            if <code>true</code> creates a new tuple, if
	 *            <code>false</code> reuse the existing tuple
	 * @param newAttrs
	 *            The new attribute values
	 * @param newDistrs
	 *            The new distribbutions
	 * @return A new tuple or the existing tuple
	 */
	private ProbabilisticTuple<T> restrictCreation(final boolean createNew,
			final Object[] newAttrs, final NormalDistributionMixture[] newDistrs) {
		if (createNew) {
			final ProbabilisticTuple<T> newTuple = new ProbabilisticTuple<T>(
					this, newAttrs, newDistrs, false);
			return newTuple;
		}
		this.attributes = newAttrs;
		this.distributions = newDistrs;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.collection.Tuple#clone()
	 */
	@Override
	public ProbabilisticTuple<T> clone() {
		return new ProbabilisticTuple<T>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.collection.Tuple#toString()
	 */
	@Override
	public String toString() {
		StringBuffer retBuff = new StringBuffer();
		retBuff.append(super.toString());
		if (getDistributions().length > 0) {
			retBuff.append("|DIS|");
			for (int i = 0; i < getDistributions().length; i++) {
				retBuff.append(getDistribution(i));
			}
		}
		return retBuff.toString();
	}

	/**********************************************************************************
	 * Test Main
	 **********************************************************************************/

	/**
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		final NormalDistributionMixture mixture1 = new NormalDistributionMixture(
				new double[] { 1.0, 2.0 }, new CovarianceMatrix(new double[] {
						1.0, 0.0, 1.0 }));
		mixture1.setAttributes(new int[] { 0, 4 });
		final NormalDistributionMixture mixture2 = new NormalDistributionMixture(
				new double[] { 3.0 },
				new CovarianceMatrix(new double[] { 2.0 }));
		mixture2.setAttributes(new int[] { 2 });

		final ProbabilisticContinuousDouble probAttr11 = new ProbabilisticContinuousDouble(
				0);
		final ProbabilisticContinuousDouble probAttr12 = new ProbabilisticContinuousDouble(
				0);
		final ProbabilisticContinuousDouble probAttr21 = new ProbabilisticContinuousDouble(
				1);

		final Object[] attrs = new Object[] { probAttr21, 1.0, probAttr11,
				"String", probAttr12 };

		final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(
				attrs, new NormalDistributionMixture[] { mixture1, mixture2 },
				true);
		tuple.setDistributions(new NormalDistributionMixture[] { mixture1,
				mixture2 });
		System.out.println(tuple);
		for (NormalDistributionMixture dist : tuple.distributions) {
			System.out.println(dist);
		}
		final ProbabilisticTuple<IMetaAttribute> newTuple = tuple.restrict(
				new int[] { 0, 1, 2 }, true);
		System.out.println(tuple);
		for (NormalDistributionMixture dist : tuple.distributions) {
			System.out.println(dist);
		}
		System.out.println(newTuple);

		for (NormalDistributionMixture dist : newTuple.distributions) {
			System.out.println(dist);
		}

		ProbabilisticTuple<IMetaAttribute> mergeTuple = (ProbabilisticTuple<IMetaAttribute>) tuple
				.process_merge(tuple, newTuple, Order.LeftRight);

		System.out.println(mergeTuple);
		for (NormalDistributionMixture dist : mergeTuple.distributions) {
			System.out.println(dist);
		}
	}
}
