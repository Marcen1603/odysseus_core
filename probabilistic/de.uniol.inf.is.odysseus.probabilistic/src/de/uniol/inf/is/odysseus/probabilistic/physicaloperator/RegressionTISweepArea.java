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
import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * Probabilistic view as described in Tran, T. T. L., Peng, L., Diao, Y.,
 * McGregor, A., & Liu, A. (2011). CLARO: modeling and processing uncertain data
 * streams. The VLDB Journal. doi:10.1007/s00778-011-0261-7
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class RegressionTISweepArea extends JoinTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> {

    @SuppressWarnings("unused")
    private static Logger LOG = LoggerFactory.getLogger(RegressionTISweepArea.class);
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
    public RegressionTISweepArea(int[] dependentAttributePos, int[] explanatoryAttributePos) {
        this.dependentAttributePos = dependentAttributePos;
        this.explanatoryAttributePos = explanatoryAttributePos;
    }

    /**
     * Clone constructor.
     * 
     * @param regressionTISweepArea
     *            The sweep area
     */
    public RegressionTISweepArea(RegressionTISweepArea regressionTISweepArea) {
        this.dependentAttributePos = regressionTISweepArea.dependentAttributePos.clone();
        this.explanatoryAttributePos = regressionTISweepArea.explanatoryAttributePos.clone();
        this.residual = regressionTISweepArea.residual.copy();
        this.regressionCoefficients = regressionTISweepArea.regressionCoefficients.copy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea#insert(de.uniol
     * .inf.is.odysseus.core.metadata.IStreamObject)
     */
    @Override
    public void insert(ProbabilisticTuple<? extends ITimeInterval> s) {
        super.insert(s);
        updateRegression(dependentAttributePos, explanatoryAttributePos);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.AbstractSweepArea
     * #insertAll(java.util.List)
     */
    @Override
    public void insertAll(List<ProbabilisticTuple<? extends ITimeInterval>> toBeInserted) {
        super.insertAll(toBeInserted);
        updateRegression(dependentAttributePos, explanatoryAttributePos);
    }

    /**
     * Gets the residual vector.
     * 
     * @return The residual vector
     */
    public RealMatrix getResidual() {
        return this.residual;
    }

    /**
     * Sets the residual vector.
     * 
     * @param residual
     *            The residual vector
     */
    private void setResidual(RealMatrix residual) {
        this.residual = residual;
    }

    /**
     * Gets the regression coefficients.
     * 
     * @return The regression coefficients
     */
    public RealMatrix getRegressionCoefficients() {
        return regressionCoefficients;
    }

    /**
     * Sets the regression coefficients.
     * 
     * @param regressionCoefficients
     *            The regression coefficients
     */
    private void setRegressionCoefficients(RealMatrix regressionCoefficients) {
        this.regressionCoefficients = regressionCoefficients;
    }

    /**
     * Gets the explanatory attribute positions array.
     * 
     * @return The attribute positions
     */
    public int[] getExplanatoryAttributePos() {
        return explanatoryAttributePos;
    }

    /**
     * Gets the dependent attribute positions array.
     * 
     * @return The attribute positions
     */
    public int[] getDependentAttributePos() {
        return dependentAttributePos;
    }

    /**
     * Returns <code>true</code> there is at least one more element in the area
     * than the number of attributes. More formaly this function returns
     * <code>true</code> iff the number of data is greater than the number of
     * explanatory and dependent attributes.
     * 
     * @return <code>true</code> if the linear regression is estimable.
     */
    public boolean isEstimable() {
        return this.size() > (explanatoryAttributePos.length + dependentAttributePos.length);
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
     * @param dependentAttributePos
     *            Position array of all dependent attributes
     * @param explanatoryAttributePos
     *            Position array of all explanatory attributes
     */
    private synchronized void updateRegression(int[] dependentAttributePos, int[] explanatoryAttributePos) {
        if (isEstimable()) {
            Iterator<ProbabilisticTuple<? extends ITimeInterval>> iter = this.iterator();

            int attributes = dependentAttributePos.length + explanatoryAttributePos.length;
            ProbabilisticTuple<? extends ITimeInterval> element = null;
            double[][] dependentAttributesData = new double[dependentAttributePos.length][this.size()];
            double[][] explanatoryAttributesData = new double[explanatoryAttributePos.length][this.size()];

            int dimension = 0;
            while (iter.hasNext()) {
                element = iter.next();
                for (int i = 0; i < element.getAttributes().length; i++) {
                    for (int j = 0; j < dependentAttributePos.length; j++) {
                        dependentAttributesData[j][dimension] = element.getAttribute(dependentAttributePos[j]);
                    }
                    for (int j = 0; j < explanatoryAttributePos.length; j++) {
                        explanatoryAttributesData[j][dimension] = element.getAttribute(explanatoryAttributePos[j]);
                    }
                }
                dimension++;
            }
            RealMatrix dependentAttributes = MatrixUtils.createRealMatrix(dependentAttributesData).transpose();
            RealMatrix explanatoryAttributes = MatrixUtils.createRealMatrix(explanatoryAttributesData).transpose();
            RealMatrix dependentAttributesTranspose = dependentAttributes.transpose();
            RealMatrix explanatoryAttributesTranspose = explanatoryAttributes.transpose();
            RealMatrix dependentAttributesInverse = null;
            RealMatrix dependentAttributesInverseTmp = dependentAttributesTranspose.multiply(dependentAttributes);
            DecompositionSolver solver;
            try {
                solver = new CholeskyDecomposition(dependentAttributesInverseTmp).getSolver();
            } catch (NonSymmetricMatrixException | NonPositiveDefiniteMatrixException e) {
                solver = new LUDecomposition(dependentAttributesInverseTmp).getSolver();
            }
            dependentAttributesInverse = solver.getInverse();
            RealMatrix identity = MatrixUtils.createRealIdentityMatrix(dimension);
            setRegressionCoefficients(dependentAttributesInverse.multiply(dependentAttributesTranspose).multiply(explanatoryAttributes));
            setResidual((explanatoryAttributesTranspose.multiply(identity.subtract(dependentAttributes.multiply(dependentAttributesInverse).multiply(dependentAttributesTranspose))).multiply(explanatoryAttributes)).scalarMultiply(1 / (dimension - attributes)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea#clone()
     */
    @Override
    public RegressionTISweepArea clone() {
        return new RegressionTISweepArea(this);
    }
}
