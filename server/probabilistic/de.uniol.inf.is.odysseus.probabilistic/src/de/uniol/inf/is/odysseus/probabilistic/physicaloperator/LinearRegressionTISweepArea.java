/*
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

package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.NonSymmetricMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTISweepArea;

/**
 * Probabilistic view as described in Tran, T. T. L., Peng, L., Diao, Y.,
 * McGregor, A., & Liu, A. (2011). CLARO: modeling and processing uncertain data
 * streams. The VLDB Journal. doi:10.1007/s00778-011-0261-7
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class LinearRegressionTISweepArea extends JoinTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> implements Cloneable {
  
	private static final long serialVersionUID = 7930617361084149456L;
	
	/** Logger. */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(LinearRegressionTISweepArea.class);
    /** The dependent attribute positions. */
    private final int[] dependentAttributePos;
    /** The explanatory attributes positions. */
    private final int[] explanatoryAttributePos;
    /** The residual. */
    private RealMatrix residual;
    /** The regression coefficients. */
    private RealMatrix regressionCoefficients;

    /**
     * Default constructor to create a sweep area that performs a linear
     * regression on the given attributes.
     * 
     * @param dependentAttributePos
     *            Positions array of the dependent attributes
     * @param explanatoryAttributePos
     *            Positions array of the explanatory attributes
     */
    public LinearRegressionTISweepArea(final int[] dependentAttributePos, final int[] explanatoryAttributePos) {
        this.dependentAttributePos = dependentAttributePos;
        this.explanatoryAttributePos = explanatoryAttributePos;
    }

    /**
     * Clone constructor.
     * 
     * @param regressionTISweepArea
     *            The sweep area
     */
    public LinearRegressionTISweepArea(final LinearRegressionTISweepArea regressionTISweepArea) {
        this.dependentAttributePos = regressionTISweepArea.dependentAttributePos.clone();
        this.explanatoryAttributePos = regressionTISweepArea.explanatoryAttributePos.clone();
        this.residual = regressionTISweepArea.residual.copy();
        this.regressionCoefficients = regressionTISweepArea.regressionCoefficients.copy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.server.intervalapproach.JoinTISweepArea#insert
     * (de.uniol .inf.is.odysseus.core.metadata.IStreamObject)
     */
    @Override
    public final void insert(final ProbabilisticTuple<? extends ITimeInterval> s) {
        super.insert(s);
        this.updateRegression(this.dependentAttributePos, this.explanatoryAttributePos);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.sweeparea.AbstractSweepArea
     * #insertAll(java.util.List)
     */
    @Override
    public final void insertAll(final List<ProbabilisticTuple<? extends ITimeInterval>> toBeInserted) {
        super.insertAll(toBeInserted);
        this.updateRegression(this.dependentAttributePos, this.explanatoryAttributePos);
    }

    /**
     * Gets the residual vector.
     * 
     * @return The residual vector
     */
    public final RealMatrix getResidual() {
        return this.residual;
    }

    /**
     * Sets the residual vector.
     * 
     * @param residual
     *            The residual vector
     */
    private void setResidual(final RealMatrix residual) {
        this.residual = residual;
    }

    /**
     * Gets the regression coefficients.
     * 
     * @return The regression coefficients
     */
    public final RealMatrix getRegressionCoefficients() {
        return this.regressionCoefficients;
    }

    /**
     * Sets the regression coefficients.
     * 
     * @param regressionCoefficients
     *            The regression coefficients
     */
    private void setRegressionCoefficients(final RealMatrix regressionCoefficients) {
        this.regressionCoefficients = regressionCoefficients;
    }

    /**
     * Gets the explanatory attribute positions array.
     * 
     * @return The attribute positions
     */
    public final int[] getExplanatoryAttributePos() {
        return this.explanatoryAttributePos;
    }

    /**
     * Gets the dependent attribute positions array.
     * 
     * @return The attribute positions
     */
    public final int[] getDependentAttributePos() {
        return this.dependentAttributePos;
    }

    /**
     * Returns <code>true</code> there is at least one more element in the area
     * than the number of attributes. More formaly this function returns
     * <code>true</code> iff the number of data is greater than the number of
     * explanatory and dependent attributes.
     * 
     * @return <code>true</code> if the linear regression is estimable.
     */
    public final boolean isEstimable() {
        return this.size() > (this.explanatoryAttributePos.length + this.dependentAttributePos.length);
    }

    /**
     * Perform least square estimation of residual (r) and regression
     * coefficient (c) for the given explanatory attribute. More formaly perform
     * the following equation:
     * 
     * \f$c = (A^{T} A)^{-1} A^{T} B\f$
     * 
     * \f$r = B^{T} (I - A(A^{T} A)^{-1} A^{T}) B/(n - k)\f$
     * 
     * @param dependentAttributePositions
     *            Position array of all dependent attributes
     * @param explanatoryAttributePositions
     *            Position array of all explanatory attributes
     */
    private synchronized void updateRegression(final int[] dependentAttributePositions, final int[] explanatoryAttributePositions) {
        if (this.isEstimable()) {
            final Iterator<ProbabilisticTuple<? extends ITimeInterval>> iter = this.iterator();

            final int attributes = dependentAttributePositions.length + explanatoryAttributePositions.length;
            ProbabilisticTuple<? extends ITimeInterval> element = null;
            final double[][] dependentAttributesData = new double[dependentAttributePositions.length][this.size()];
            final double[][] explanatoryAttributesData = new double[explanatoryAttributePositions.length][this.size()];

            int dimension = 0;
            while (iter.hasNext()) {
                element = iter.next();
                for (int i = 0; i < element.getAttributes().length; i++) {
                    for (int j = 0; j < dependentAttributePositions.length; j++) {
                        dependentAttributesData[j][dimension] = element.getAttribute(dependentAttributePositions[j]);
                    }
                    for (int j = 0; j < explanatoryAttributePositions.length; j++) {
                        explanatoryAttributesData[j][dimension] = element.getAttribute(explanatoryAttributePositions[j]);
                    }
                }
                dimension++;
            }
            final RealMatrix dependentAttributes = MatrixUtils.createRealMatrix(dependentAttributesData).transpose();
            final RealMatrix explanatoryAttributes = MatrixUtils.createRealMatrix(explanatoryAttributesData).transpose();
            final RealMatrix dependentAttributesTranspose = dependentAttributes.transpose();
            final RealMatrix explanatoryAttributesTranspose = explanatoryAttributes.transpose();
            RealMatrix dependentAttributesInverse = null;
            final RealMatrix dependentAttributesInverseTmp = dependentAttributesTranspose.multiply(dependentAttributes);
            DecompositionSolver solver;
            try {
                solver = new CholeskyDecomposition(dependentAttributesInverseTmp).getSolver();
            }
            catch (NonSymmetricMatrixException | NonPositiveDefiniteMatrixException e) {
                solver = new LUDecomposition(dependentAttributesInverseTmp).getSolver();
            }
            dependentAttributesInverse = solver.getInverse();
            final RealMatrix identity = MatrixUtils.createRealIdentityMatrix(dimension);
            this.setRegressionCoefficients(dependentAttributesInverse.multiply(dependentAttributesTranspose).multiply(explanatoryAttributes));
            this.setResidual((explanatoryAttributesTranspose.multiply(identity.subtract(dependentAttributes.multiply(dependentAttributesInverse).multiply(dependentAttributesTranspose)))
                    .multiply(explanatoryAttributes)).scalarMultiply(1 / (dimension - attributes)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.server.intervalapproach.DefaultTISweepArea#clone
     * ()
     */
    @Override
    public final LinearRegressionTISweepArea clone() {
        return new LinearRegressionTISweepArea(this);
    }
}
