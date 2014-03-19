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
package de.uniol.inf.is.odysseus.probabilistic.common.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.ExtendedMixtureMultivariateRealDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.ProbabilisticContinuousDouble;

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
    private ExtendedMixtureMultivariateRealDistribution[] distributions;

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
        Preconditions.checkArgument(count >= 0);
        this.distributions = new ExtendedMixtureMultivariateRealDistribution[0];
    }

    /**
     * Creates a new probabilistic tuple with the given number of attributes.
     * 
     * @param count
     *            The number of attributes
     * @param distributions
     *            The number of distributions
     * @param requiresDeepClone
     *            Flag indicating deep clone during transfer between operators
     */
    public ProbabilisticTuple(final int count, final int distributions, final boolean requiresDeepClone) {
        super(count, requiresDeepClone);
        Preconditions.checkArgument(count >= 0);
        Preconditions.checkArgument(distributions >= 0);
        this.distributions = new ExtendedMixtureMultivariateRealDistribution[distributions];
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
        Preconditions.checkNotNull(attributes);
        this.distributions = new ExtendedMixtureMultivariateRealDistribution[0];
    }

    /**
     * Creates a new probabilistic tuple with the given attributes and
     * distributions.
     * 
     * @param attributes
     *            The attributes
     * @param distributions
     *            The distributions
     * @param requiresDeepClone
     *            Flag indicating deep clone during transfer between operators
     */
    public ProbabilisticTuple(final Object[] attributes, final ExtendedMixtureMultivariateRealDistribution[] distributions, final boolean requiresDeepClone) {
        super(attributes, requiresDeepClone);
        Preconditions.checkNotNull(attributes);
        Preconditions.checkNotNull(distributions);
        this.distributions = distributions;
    }

    /**
     * Creates a new probabilistic tuple with the given attributes based on the
     * copy of another tuple.
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
        Preconditions.checkNotNull(copy);
        if (copy.distributions != null) {
            this.distributions = new ExtendedMixtureMultivariateRealDistribution[copy.distributions.length];
            for (int i = 0; i < copy.distributions.length; i++) {
                this.distributions[i] = (ExtendedMixtureMultivariateRealDistribution) copy.distributions[i].clone();
            }
        }
        else {
            this.distributions = new ExtendedMixtureMultivariateRealDistribution[0];
        }
    }

    /**
     * Creates a new probabilistic tuple with the given attributes and
     * distributions based on the copy of another tuple.
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
    public ProbabilisticTuple(final ProbabilisticTuple<T> copy, final Object[] newAttrs, final ExtendedMixtureMultivariateRealDistribution[] newDistrs, final boolean requiresDeepClone) {
        super(copy, newAttrs, requiresDeepClone);
        Preconditions.checkNotNull(copy);
        Preconditions.checkNotNull(copy.distributions);
        if (newDistrs != null) {
            this.distributions = new ExtendedMixtureMultivariateRealDistribution[newDistrs.length];
            for (int i = 0; i < newDistrs.length; i++) {
                this.distributions[i] = (ExtendedMixtureMultivariateRealDistribution) newDistrs[i].clone();
            }
        }
        else {
            if (copy.distributions != null) {
                this.distributions = new ExtendedMixtureMultivariateRealDistribution[copy.distributions.length];
                for (int i = 0; i < copy.distributions.length; i++) {
                    this.distributions[i] = (ExtendedMixtureMultivariateRealDistribution) copy.distributions[i].clone();
                }
            }
            else {
                this.distributions = new ExtendedMixtureMultivariateRealDistribution[0];
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
        Preconditions.checkNotNull(copy);
        Preconditions.checkNotNull(copy.distributions);
        if (copy.distributions != null) {
            this.distributions = new ExtendedMixtureMultivariateRealDistribution[copy.distributions.length];
            for (int i = 0; i < copy.distributions.length; i++) {
                this.distributions[i] = (ExtendedMixtureMultivariateRealDistribution) copy.distributions[i].clone();
            }
        }
        else {
            this.distributions = new ExtendedMixtureMultivariateRealDistribution[0];
        }
    }

    /**
     * @param object
     */
    public ProbabilisticTuple(Tuple<T> copy) {
        super(copy);
        Preconditions.checkNotNull(copy);
        this.distributions = new ExtendedMixtureMultivariateRealDistribution[0];
    }

    /**
     * Gets the distributions at the given index.
     * 
     * @param index
     *            The index
     * @return The distribution at the given position or <code>null</code> if no
     *         distribution exists at the given position
     */
    public final ExtendedMixtureMultivariateRealDistribution getDistribution(final int index) {
        Preconditions.checkArgument((index >= 0) && (index < this.distributions.length));
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
    public final void setDistribution(final int index, final ExtendedMixtureMultivariateRealDistribution distribution) {
        Preconditions.checkArgument((index >= 0) && (index < this.distributions.length));
        Preconditions.checkNotNull(distribution);
        this.distributions[index] = distribution;
    }

    /**
     * Sets the distributions.
     * 
     * @param distributions
     *            The distributions
     */
    public final void setDistributions(final ExtendedMixtureMultivariateRealDistribution[] distributions) {
        Preconditions.checkNotNull(distributions);
        this.distributions = distributions;
    }

    /**
     * Gets the distributions.
     * 
     * @return All distributions in this tuple
     */
    public final ExtendedMixtureMultivariateRealDistribution[] getDistributions() {
        return this.distributions;
    }

    /**
     * Creates a new instance from the current tuple if the createNew param is
     * true or uses the current instance and appends the given attribute object
     * and distribution.
     * 
     * @param object
     *            the object to append
     * @param distribution
     *            the distribution to append
     * @param createNew
     *            indicates if create a copy
     * @return the extended tuple
     */
    public final ProbabilisticTuple<T> append(final Object object, final ExtendedMixtureMultivariateRealDistribution distribution, final boolean createNew) {
        final Object[] newAttrs = Arrays.copyOf(this.attributes, this.attributes.length + 1);
        newAttrs[this.attributes.length] = object;
        final ExtendedMixtureMultivariateRealDistribution[] newDistrs = Arrays.copyOf(this.distributions, this.distributions.length + 1);
        newDistrs[this.distributions.length] = distribution;
        if (createNew) {
            final ProbabilisticTuple<T> newTuple = new ProbabilisticTuple<T>(this, newAttrs, newDistrs, this.requiresDeepClone());
            return newTuple;
        }
        this.attributes = newAttrs;
        this.distributions = newDistrs;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.core.collection.Tuple#restrict(int,
     * boolean)
     */
    @Override
    public final ProbabilisticTuple<T> restrict(final int attr, final boolean createNew) {
        RealMatrix restrictMatrix;
        final Object newAttr = this.attributes[attr];
        if (newAttr.getClass() == ProbabilisticContinuousDouble.class) {
            restrictMatrix = MatrixUtils.createRealMatrix(new double[][] { { 1.0 } });
        }
        else {
            restrictMatrix = null;
        }
        return this.restrict(new int[] { attr }, new RealMatrix[] { restrictMatrix }, createNew);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.core.collection.Tuple#restrict(int[],
     * boolean)
     */
    @Override
    public final ProbabilisticTuple<T> restrict(final int[] attrList, final boolean createNew) {
        // The new dimension of each distribution
        final int[] newDistributionDimensions = new int[this.distributions.length];

        // Determine the new dimensions of each distribution after the
        // restriction
        final List<Integer> restrictAttributePos = new ArrayList<Integer>(attrList.length);
        for (final int attributePos : attrList) {
            final Object attribute = this.attributes[attributePos];
            if (attribute.getClass() == ProbabilisticContinuousDouble.class) {
                restrictAttributePos.add(new Integer(attributePos));
                final ProbabilisticContinuousDouble continuousAttribute = (ProbabilisticContinuousDouble) attribute;
                final int distributionPos = continuousAttribute.getDistribution();
                newDistributionDimensions[distributionPos]++;
            }
        }

        // The restriction matrixes for each existing distribution
        final RealMatrix[] restrictMatrixes = new RealMatrix[this.distributions.length];
        // For each distribution that will exist after the restriction construct
        // a restriction matrix
        for (int i = 0; i < this.distributions.length; i++) {
            if (newDistributionDimensions[i] > 0) {
                final ExtendedMixtureMultivariateRealDistribution distribution = this.distributions[i];
                restrictMatrixes[i] = MatrixUtils.createRealMatrix(newDistributionDimensions[i], distribution.getDimension());
                final List<Integer> distributionAttributes = new ArrayList<Integer>(distribution.getAttributes().length);
                for (int pos = 0, newPos = 0; pos < distribution.getAttributes().length; pos++) {
                    final int attributePos = distribution.getAttribute(pos);
                    distributionAttributes.add(new Integer(attributePos));
                    if (restrictAttributePos.contains(new Integer(attributePos))) {
                        restrictMatrixes[i].setEntry(newPos, pos, 1.0);
                        newPos++;
                    }
                }
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
     *            The list of restriction matrixes for probabilistic continuous
     *            attributes
     * @param createNew
     *            flag for the creation of a new tuple or reusig the existing
     *            tuple
     * @return The restricted tuple
     */
    public final ProbabilisticTuple<T> restrict(final int[] attrList, final RealMatrix[] restrictMatrix, final boolean createNew) {
        final Object[] newAttributes = new Object[attrList.length];

        // Link to the attribute data, no copy needed
        for (int pos = 0; pos < attrList.length; pos++) {
            newAttributes[pos] = this.attributes[attrList[pos]];
        }
        // Get the required size of the distribution layer
        int distributionsLayerSize = 0;
        for (int i = 0; i < this.distributions.length; i++) {
            if (restrictMatrix[i] != null) {
                distributionsLayerSize++;
            }
        }
        final ExtendedMixtureMultivariateRealDistribution[] newDistributions = new ExtendedMixtureMultivariateRealDistribution[distributionsLayerSize];
        for (int oldLayerIndex = 0, newLayerIndex = 0; oldLayerIndex < this.distributions.length; oldLayerIndex++) {
            if (restrictMatrix[oldLayerIndex] != null) {
                final int oldDimension = this.distributions[oldLayerIndex].getDimension();
                final int newDimension = restrictMatrix[oldLayerIndex].getRowDimension();

                // Create the new distributions using the old distribution and
                // the restriction matrix
                newDistributions[newLayerIndex] = (ExtendedMixtureMultivariateRealDistribution) this.distributions[oldLayerIndex].clone();
                // First, update the covariance matrix and the mean in all
                // mixtures

                this.distributions[oldLayerIndex].restrict(restrictMatrix[oldLayerIndex]);
                newDistributions[newLayerIndex] = this.distributions[oldLayerIndex];
                final ExtendedMixtureMultivariateRealDistribution distribution = newDistributions[newLayerIndex];
                // Second, update the support vector and the attribute link from
                // the payload
                final int[] oldDistributionAttributes = distribution.getAttributes();
                final int[] newDistributionAttributes = new int[newDimension];
                final Interval[] support = new Interval[newDimension];

                int dimension = 0;
                for (int d = 0; d < oldDimension; d++) {
                    final int oldAttributePos = oldDistributionAttributes[d];
                    for (int attributeIndex = 0; attributeIndex < attrList.length; attributeIndex++) {
                        if (oldAttributePos == attrList[attributeIndex]) {
                            // Set the new index of the attribute in the payload
                            newDistributionAttributes[dimension] = attributeIndex;
                            // Change the index of the support vector to the new
                            // dimension
                            support[dimension] = distribution.getSupport()[d];
                            // Update the link from the payload to the right
                            // index in the distribution layer
                            ((ProbabilisticContinuousDouble) newAttributes[attributeIndex]).setDistribution(newLayerIndex);
                            dimension++;
                            break;
                        }
                    }
                }
                // Update the attribute links to the payload
                distribution.setAttributes(newDistributionAttributes);
                // Update the support for the distribution
                distribution.setSupport(support);
                newLayerIndex++;
            }
        }
        return this.restrictCreation(createNew, newAttributes, newDistributions);
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
    @Override
    protected final IStreamObject<T> process_merge(final IStreamObject<T> left, final IStreamObject<T> right, final Order order) {
        if (order == Order.LeftRight) {
            return this.processMergeInternal((ProbabilisticTuple<T>) left, (ProbabilisticTuple<T>) right);
        }
        else {
            return this.processMergeInternal((ProbabilisticTuple<T>) right, (ProbabilisticTuple<T>) left);
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
        ExtendedMixtureMultivariateRealDistribution[] leftDistributions = null;
        Object[] rightAttributes = null;
        ExtendedMixtureMultivariateRealDistribution[] rightDistributions = null;
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
        final ExtendedMixtureMultivariateRealDistribution[] newDistributions = this.mergeDistributions(leftDistributions, rightDistributions, attributesLength);
        final Object[] newAttributes = this.mergeAttributes(leftAttributes, rightAttributes, distributionsLength);
        final ProbabilisticTuple<T> r = new ProbabilisticTuple<T>(newAttributes, newDistributions, requiresDeepClone);
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
        final Object[] newAttributes = new Object[length];
        if (leftAttributes != null) {
            System.arraycopy(leftAttributes, 0, newAttributes, 0, leftAttributes.length);
        }
        if (rightAttributes != null) {
            System.arraycopy(rightAttributes, 0, newAttributes, leftAttributes.length, rightAttributes.length);
        }
        for (int i = start; i < length; i++) {
            if (newAttributes[i].getClass() == ProbabilisticContinuousDouble.class) {
                final ProbabilisticContinuousDouble value = ((ProbabilisticContinuousDouble) newAttributes[i]);
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
    private ExtendedMixtureMultivariateRealDistribution[] mergeDistributions(final ExtendedMixtureMultivariateRealDistribution[] leftDistributions,
            final ExtendedMixtureMultivariateRealDistribution[] rightDistributions, final int offset) {
        int length = 0;
        int start = 0;
        if (leftDistributions != null) {
            length += leftDistributions.length;
            start = leftDistributions.length;
        }
        if (rightDistributions != null) {
            length += rightDistributions.length;
        }
        final ExtendedMixtureMultivariateRealDistribution[] newDistributions = new ExtendedMixtureMultivariateRealDistribution[length];
        if (leftDistributions != null) {
            for (int i = 0; i < leftDistributions.length; i++) {
                newDistributions[i] = leftDistributions[i].clone();
            }
            // System.arraycopy(leftDistributions, 0, newDistributions, 0,
            // leftDistributions.length);
        }
        if (rightDistributions != null) {
            for (int i = 0; i < rightDistributions.length; i++) {
                newDistributions[leftDistributions.length + i] = rightDistributions[i].clone();
            }
            // System.arraycopy(rightDistributions, 0, newDistributions,
            // leftDistributions.length, rightDistributions.length);
        }
        for (int i = start; i < length; i++) {
            final int dimension = newDistributions[i].getDimension();

            final int[] newAttrsPos = new int[dimension];
            for (int j = 0; j < dimension; j++) {
                newAttrsPos[j] = newDistributions[i].getAttribute(j) + offset;
            }
            newDistributions[i].setAttributes(newAttrsPos);
        }
        return newDistributions;
    }

    /**
     * Creates a new probabilistic tuple with the given attributes and
     * distributions.
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
    private ProbabilisticTuple<T> restrictCreation(final boolean createNew, final Object[] newAttrs, final ExtendedMixtureMultivariateRealDistribution[] newDistrs) {
        if (createNew) {
            final ProbabilisticTuple<T> newTuple = new ProbabilisticTuple<T>(this, newAttrs, newDistrs, this.requiresDeepClone());
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
        final StringBuffer retBuff = new StringBuffer();
        retBuff.append(super.toString());
        retBuff.append("|DIS|");
        if ((this.getDistributions() != null) && (this.getDistributions().length > 0)) {
            for (int i = 0; i < this.getDistributions().length; i++) {
                if (i > 0) {
                    retBuff.append("|");
                }
                retBuff.append(this.getDistribution(i));
            }
        }
        else {
            retBuff.append("-");
        }
        return retBuff.toString();
    }

}
