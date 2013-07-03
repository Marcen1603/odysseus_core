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

import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * A probabilistic tuple for probabilistic continuous data.
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
	/** The probabilistic distribution function (Gaussian mixture model). */
	private NormalDistributionMixture[] distributions;

	/**
	 * Creates a new probabilistic tuple with the given number of attributes.
	 * 
	 * @param count
	 *            The number of attributes
	 * @param requiresDeepClone
	 *            Flag indicating deep clone during transfer between operators
	 */
	public ProbabilisticTuple(final int count, final boolean requiresDeepClone) {
		super(count, requiresDeepClone);
		this.distributions = new NormalDistributionMixture[0];
	}

	/**
	 * Creates a new probabilistic tuple with the given attributes.
	 * 
	 * @param attributes
	 *            The attributes
	 * @param requiresDeepClone
	 *            Flag indicating deep clone during transfer between operators
	 */
	public ProbabilisticTuple(final Object[] attributes, final boolean requiresDeepClone) {
		super(attributes, requiresDeepClone);
		this.distributions = new NormalDistributionMixture[0];
	}

	/**
	 * Creates a new probabilistic tuple with the given attributes and distributions.
	 * 
	 * @param attributes
	 *            The attributes
	 * @param distributions
	 *            The distributions
	 * @param requiresDeepClone
	 *            Flag indicating deep clone during transfer between operators
	 */
	public ProbabilisticTuple(final Object[] attributes, final NormalDistributionMixture[] distributions, final boolean requiresDeepClone) {
		super(attributes, requiresDeepClone);
		this.distributions = distributions;
	}

	/**
	 * Creates a new probabilistic tuple with the given attributes based on the copy of another tuple.
	 * 
	 * @param copy
	 *            The copy
	 * @param newAttrs
	 *            The attributes
	 * @param requiresDeepClone
	 *            Flag indicating deep clone during transfer between operators
	 */
	public ProbabilisticTuple(final ProbabilisticTuple<T> copy, final Object[] newAttrs, final boolean requiresDeepClone) {
		super(copy, newAttrs, requiresDeepClone);
		if (copy.distributions != null) {
			for (int i = 0; i < copy.distributions.length; i++) {
				this.distributions[i] = copy.distributions[i].clone();
			}
		} else {
			this.distributions = new NormalDistributionMixture[0];
		}
	}

	/**
	 * Creates a new probabilistic tuple with the given attributes and distributions based on the copy of another tuple.
	 * 
	 * @param copy
	 *            The copy
	 * @param newAttrs
	 *            The attributes
	 * @param newDistrs
	 *            The distributions
	 * @param requiresDeepClone
	 *            Flag indicating deep clone during transfer between operators
	 */
	public ProbabilisticTuple(final ProbabilisticTuple<T> copy, final Object[] newAttrs, final NormalDistributionMixture[] newDistrs, final boolean requiresDeepClone) {
		super(copy, newAttrs, requiresDeepClone);
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
	 * Clone constructor.
	 * 
	 * @param copy
	 *            The copy
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
	 * Gets the distributions at the given index.
	 * 
	 * @param index
	 *            The index
	 * @return The distribution at the given position or <code>null</code> if no distribution exists at the given position
	 */
	public final NormalDistributionMixture getDistribution(final int index) {
		if ((index < 0) || (index >= this.distributions.length)) {
			return null;
		}
		return this.distributions[index];
	}

	/**
	 * Sets the distribution at the given index.
	 * 
	 * @param index
	 *            The index
	 * @param distribution
	 *            The distribution
	 */
	public final void setDistribution(final int index, final NormalDistributionMixture distribution) {
		this.distributions[index] = distribution;
	}

	/**
	 * Sets the distributions.
	 * 
	 * @param distributions
	 *            The distributions
	 */
	public final void setDistributions(final NormalDistributionMixture[] distributions) {
		this.distributions = distributions;
	}

	/**
	 * Gets the distributions.
	 * 
	 * @return All distributions in this tuple
	 */
	public final NormalDistributionMixture[] getDistributions() {
		return this.distributions;
	}

	/**
	 * Creates a new instance from the current tuple if the createNew param is true or uses the current instance and appends the given attribute object and distribution
	 * 
	 * @param object
	 *            the object to append
	 * @param distribution
	 *            the distribution to append
	 * @param createNew
	 *            indicates if create a copy
	 * @return the extended tuple
	 */
	public ProbabilisticTuple<T> append(Object object, final NormalDistributionMixture distribution, boolean createNew) {
		Object[] newAttrs = Arrays.copyOf(this.attributes, this.attributes.length + 1);
		newAttrs[this.attributes.length] = object;
		NormalDistributionMixture[] newDistrs = Arrays.copyOf(this.distributions, this.distributions.length + 1);
		newDistrs[this.distributions.length] = distribution;
		if (createNew) {
			ProbabilisticTuple<T> newTuple = new ProbabilisticTuple<T>(this, newAttrs, newDistrs, this.requiresDeepClone());
			return newTuple;
		}
		this.attributes = newAttrs;
		this.distributions = newDistrs;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.collection.Tuple#restrict(int, boolean)
	 */
	@Override
	public final ProbabilisticTuple<T> restrict(final int attr, final boolean createNew) {
		RealMatrix restrictMatrix;
		final Object newAttr = this.attributes[attr];
		if (newAttr.getClass() == ProbabilisticContinuousDouble.class) {
			restrictMatrix = MatrixUtils.createRealMatrix(new double[][] { { 1.0 } });
		} else {
			restrictMatrix = null;
		}
		return this.restrict(new int[] { attr }, new RealMatrix[] { restrictMatrix }, createNew);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.collection.Tuple#restrict(int[], boolean)
	 */
	@Override
	public final ProbabilisticTuple<T> restrict(final int[] attrList, final boolean createNew) {
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
				restrictMatrixes[j] = MatrixUtils.createRealMatrix(newDistrDimensions[i], this.distributions[i].getDimension());
				for (int row = 0, column = 0; (row < restrictMatrixes[j].getRowDimension()) && (column < restrictMatrixes[j].getColumnDimension()); row++, column++) {
					restrictMatrixes[j].setEntry(row, column, 1.0);
				}
				j++;
			}
		}
		return this.restrict(attrList, restrictMatrixes, createNew);
	}

	/**
	 * Restricts the tuple to the given attribute position.
	 * 
	 * @param attr
	 *            The attribute position
	 * @param restrictMatrix
	 *            The restriction matrix for probabilistic continuous attributes
	 * @param createNew
	 *            Flag indicating whether a new tuple should be created
	 * @return The restricted tuple
	 */
	public final ProbabilisticTuple<T> restrict(final int attr, final RealMatrix restrictMatrix, final boolean createNew) {
		return this.restrict(new int[] { attr }, new RealMatrix[] { restrictMatrix }, createNew);
	}

	/**
	 * Restricts the tuple to the given list of attribute positions.
	 * 
	 * @param attrList
	 *            The list of attribute positions
	 * @param restrictMatrix
	 *            The list of restriction matrixes for probabilistic continuous attributes
	 * @param createNew
	 *            flag for the creation of a new tuple or reusig the existing tuple
	 * @return The restricted tuple
	 */
	public final ProbabilisticTuple<T> restrict(final int[] attrList, final RealMatrix[] restrictMatrix, final boolean createNew) {
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
				final int newDimension = restrictMatrix[newDistrIndex].getRowDimension();

				newDistrs[newDistrIndex] = this.distributions[i].clone();

				final NormalDistributionMixture distr = newDistrs[newDistrIndex];
				for (final NormalDistribution mixture : distr.getMixtures().keySet()) {
					final RealMatrix covarianceMatrix = CovarianceMatrixUtils.toMatrix(mixture.getCovarianceMatrix());

					mixture.setCovarianceMatrix(CovarianceMatrixUtils.fromMatrix(restrictMatrix[newDistrIndex].multiply(covarianceMatrix).multiply(restrictMatrix[newDistrIndex].transpose())));

					mixture.setMean(restrictMatrix[newDistrIndex].multiply(MatrixUtils.createRealDiagonalMatrix(mixture.getMean())).multiply(restrictMatrix[newDistrIndex].transpose()).getColumn(0));
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
							((ProbabilisticContinuousDouble) newAttrs[k]).setDistribution(newDistrIndex);

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
	 * @see de.uniol.inf.is.odysseus.core.collection.Tuple#process_merge(de.uniol .inf.is.odysseus.core.metadata.IStreamObject, de.uniol.inf.is.odysseus.core.metadata.IStreamObject, de.uniol.inf.is.odysseus.core.Order)
	 */
	@Override
	protected final IStreamObject<T> process_merge(final IStreamObject<T> left, final IStreamObject<T> right, final Order order) {
		if (order == Order.LeftRight) {
			return processMergeInternal((ProbabilisticTuple<T>) left, (ProbabilisticTuple<T>) right);
		} else {
			return processMergeInternal((ProbabilisticTuple<T>) right, (ProbabilisticTuple<T>) left);
		}
	}

	/**
	 * Merges the two given tuple into one tuple.
	 * 
	 * @param left
	 *            The tuple from the left
	 * @param right
	 *            The tuple from the right
	 * @return The merged tuple
	 */
	private IStreamObject<T> processMergeInternal(final ProbabilisticTuple<T> left, final ProbabilisticTuple<T> right) {
		Object[] leftAttributes = null;
		NormalDistributionMixture[] leftDistributions = null;
		Object[] rightAttributes = null;
		NormalDistributionMixture[] rightDistributions = null;
		int distributionsLength = 0;
		int attributesLength = 0;
		boolean requiresDeepClone = false;
		if (left != null) {
			leftDistributions = left.getDistributions();
			leftAttributes = left.getAttributes();
			distributionsLength = leftDistributions.length;
			attributesLength = leftAttributes.length;
			requiresDeepClone |= left.requiresDeepClone();
		}
		if (right != null) {
			rightDistributions = right.getDistributions();
			rightAttributes = right.getAttributes();
			requiresDeepClone |= right.requiresDeepClone();
		}
		NormalDistributionMixture[] newDistributions = mergeDistributions(leftDistributions, rightDistributions, attributesLength);
		Object[] newAttributes = mergeAttributes(leftAttributes, rightAttributes, distributionsLength);
		ProbabilisticTuple<T> r = new ProbabilisticTuple<T>(newAttributes, newDistributions, requiresDeepClone);
		return r;
	}

	/**
	 * Merges the two given attributes into one attributes.
	 * 
	 * @param leftAttributes
	 *            The attributes from the first tuple
	 * @param rightAttributes
	 *            The attributes from the second tuple
	 * @param offset
	 *            The offset for distribution reference
	 * @return The merged attributes
	 */
	private Object[] mergeAttributes(final Object[] leftAttributes, final Object[] rightAttributes, final int offset) {
		int length = 0;
		int start = 0;
		if (leftAttributes != null) {
			length += leftAttributes.length;
			start = leftAttributes.length;
		}
		if (rightAttributes != null) {
			length += rightAttributes.length;
		}
		Object[] newAttributes = new Object[length];
		if (leftAttributes != null) {
			System.arraycopy(leftAttributes, 0, newAttributes, 0, leftAttributes.length);
		}
		if (rightAttributes != null) {
			System.arraycopy(rightAttributes, 0, newAttributes, leftAttributes.length, rightAttributes.length);
		}
		for (int i = start; i < length; i++) {
			if (newAttributes[i].getClass() == ProbabilisticContinuousDouble.class) {
				ProbabilisticContinuousDouble value = ((ProbabilisticContinuousDouble) newAttributes[i]);
				newAttributes[i] = new ProbabilisticContinuousDouble(value.getDistribution() + offset);
			}
		}
		return newAttributes;
	}

	/**
	 * Merges the two given distributions list into one distribution list.
	 * 
	 * @param leftDistributions
	 *            The distributions from the first tuple
	 * @param rightDistributions
	 *            The distributions from the second tuple
	 * @param offset
	 *            The offset for attribute reference
	 * @return The merged distributions list
	 */
	private NormalDistributionMixture[] mergeDistributions(final NormalDistributionMixture[] leftDistributions, final NormalDistributionMixture[] rightDistributions, final int offset) {
		int length = 0;
		int start = 0;
		if (leftDistributions != null) {
			length += leftDistributions.length;
			start = leftDistributions.length;
		}
		if (rightDistributions != null) {
			length += rightDistributions.length;
		}
		NormalDistributionMixture[] newDistributions = new NormalDistributionMixture[length];
		if (leftDistributions != null) {
			for (int i = 0; i < leftDistributions.length; i++) {
				newDistributions[i] = leftDistributions[i].clone();
			}
			// System.arraycopy(leftDistributions, 0, newDistributions, 0, leftDistributions.length);
		}
		if (rightDistributions != null) {
			for (int i = 0; i < rightDistributions.length; i++) {
				newDistributions[leftDistributions.length + i] = rightDistributions[i].clone();
			}
			// System.arraycopy(rightDistributions, 0, newDistributions, leftDistributions.length, rightDistributions.length);
		}
		for (int i = start; i < length; i++) {
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
	 * Creates a new probabilistic tuple with the given attributes and distributions.
	 * 
	 * @param createNew
	 *            if <code>true</code> creates a new tuple, if <code>false</code> reuse the existing tuple
	 * @param newAttrs
	 *            The new attribute values
	 * @param newDistrs
	 *            The new distribbutions
	 * @return A new tuple or the existing tuple
	 */
	private ProbabilisticTuple<T> restrictCreation(final boolean createNew, final Object[] newAttrs, final NormalDistributionMixture[] newDistrs) {
		if (createNew) {
			final ProbabilisticTuple<T> newTuple = new ProbabilisticTuple<T>(this, newAttrs, newDistrs, false);
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
	public final ProbabilisticTuple<T> clone() {
		return new ProbabilisticTuple<T>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.collection.Tuple#toString()
	 */
	@Override
	public final String toString() {
		StringBuffer retBuff = new StringBuffer();
		retBuff.append(super.toString());
		if ((getDistributions() != null) && (getDistributions().length > 0)) {
			retBuff.append("|DIS|");
			for (int i = 0; i < getDistributions().length; i++) {
				retBuff.append(getDistribution(i));
			}
		}
		return retBuff.toString();
	}
	
	
}
