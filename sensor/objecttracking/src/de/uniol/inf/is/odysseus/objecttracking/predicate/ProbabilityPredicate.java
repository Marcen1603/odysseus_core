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
package de.uniol.inf.is.odysseus.objecttracking.predicate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public class ProbabilityPredicate<T extends IProbability> extends AbstractPredicate<MVTuple<T>> {

    private static final long serialVersionUID = 6467624302125491583L;

    // stores which attributes are needed at which position for
    // variable bindings
    // private int[] attributePositions;

    // fromRightChannel[i] stores if the getAttribute(attributePositions[i])
    // should be called on the left or on the right input tuple
    // private boolean[] fromRightChannel;

    /**
     * Contains the values xLow for calculuating P(xLow[0] <= x <= xUp[0],...)
     * <op> prob
     */
    double[] xLow;

    /**
     * Contains the values xUp for calculuating P(xLow[0] <= x <= xUp[0],...)
     * <op> prob
     */
    double[] xUp;

    /**
     * Contains the value prob for calculuating P(xLow[0] <= x <= xUp[0],...)
     * <op> prob
     */
    double prob;

    /**
     * for calculating Hx + c
     */
    RealMatrix leftMatrix;
    RealMatrix rightMatrix;

    /**
     * for calculating Hx + c
     */
    RealMatrix leftVector;
    RealMatrix rightVector;

    String leftSource;
    String rightSource;

    SDFSchema leftSchema;
    SDFSchema rightSchema;

    String calculationType;

    // MatLab matlab;

    String compareOperator;

    /**
     * If true, only marginal distributions will be calculated and no
     * multivariate distributions
     */
    boolean calcMarginalDist;

    /* I think, this will not be used. */
    private Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();

    public ProbabilityPredicate(String leftSource, double[][] leftMatrix, double[] leftVector, double[] xLow, double[] xUp, double prob, String compareOperator) {

        this.leftSource = leftSource;
        if (leftMatrix != null) {
            this.leftMatrix = new RealMatrixImpl(leftMatrix);
        }
        if (leftVector != null) {
            this.leftVector = new RealMatrixImpl(leftVector);
        }
        this.xLow = xLow;
        this.xUp = xUp;
        this.prob = prob;
        this.compareOperator = compareOperator;
    }

    public ProbabilityPredicate(String leftSource, double[][] leftMatrix, double[] leftVector, String rightSource, double[][] rightMatrix, double[] rightVector, double[] xLow, double[] xUp,
            double prob, String operandType, String compareOperator) {
        this.leftSource = leftSource;
        this.leftMatrix = new RealMatrixImpl(leftMatrix);
        if (leftVector != null) {
            this.leftVector = new RealMatrixImpl(leftVector);
        }

        this.rightSource = rightSource;
        this.rightMatrix = new RealMatrixImpl(rightMatrix);
        if (rightVector != null) {
            this.rightVector = new RealMatrixImpl(rightVector);
        }

        this.xLow = xLow;
        this.xUp = xUp;
        this.prob = prob;

        this.calculationType = operandType;
        this.compareOperator = compareOperator;
    }

    public ProbabilityPredicate(ProbabilityPredicate<T> probabilityPredicate) {
        // TODO Implement Copy Constructor
        throw new RuntimeException("No copy constructor for ProbabiltyPredicate defined.");
    }

    public SDFSchema getLeftSchema() {
        return leftSchema;
    }

    public void setLeftSchema(SDFSchema leftSchema) {
        this.leftSchema = leftSchema;
    }

    public SDFSchema getRightSchema() {
        return rightSchema;
    }

    public void setRightSchema(SDFSchema rightSchema) {
        this.rightSchema = rightSchema;
    }

    @Override
    public void init() {
        // if(this.matlab == null){
        // try {
        // this.matlab = new MatLab();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
    }

    /**
     * Only the left values are set.
     */
    @Override
    public boolean evaluate(MVTuple<T> input) {

        double[] leftMVarray = new double[input.getMeasurementValuePositions().length];
        for (int i = 0; i < input.getMeasurementValuePositions().length; i++) {
            leftMVarray[i] = input.getAttribute(input.getMeasurementValuePositions()[i]);
        }

        RealMatrix leftMV = new RealMatrixImpl(leftMVarray);
        RealMatrix leftCov = new RealMatrixImpl(input.getMetadata().getCovariance());

        if (!this.calcMarginalDist) {
            RealMatrix projectedLeft = null;
            if (this.leftMatrix != null) {
                projectedLeft = this.leftMatrix.multiply(leftMV);
            } else {
                projectedLeft = leftMV;
            }

            if (this.leftVector != null) {
                projectedLeft = projectedLeft.add(leftVector);
            }

            if (this.leftMatrix != null) {
                projectedLeft = this.leftMatrix.multiply(leftCov).multiply(this.leftMatrix.transpose());
            } 

            double resProb = 0;
            double[] x = new double[2];
            x[0] = xLow[0];
            x[1] = xUp[0];
            resProb = 0.0;

            if (this.compareOperator.equals("<")) {
                return resProb < this.prob;
            } else if (this.compareOperator.equals("<=")) {
                return resProb <= this.prob;
            } else if (this.compareOperator.equals("=")) {
                return resProb == this.prob;
            } else if (this.compareOperator.equals(">=")) {
                return resProb >= this.prob;
            } else if (this.compareOperator.equals(">")) {
                return resProb > this.prob;
            }

            throw new IllegalArgumentException("Operator " + this.compareOperator + " cannot be evaluated.");
        }
        double[] resProb = new double[this.xLow.length];
        for (int i = 0; i < this.xLow.length; i++) {
            RealMatrix projectedLeft = null;
            RealMatrix row = null;
            if (this.leftMatrix != null) {
                row = this.leftMatrix.getSubMatrix(i, i, 0, (xLow.length - 1));
                projectedLeft = row.multiply(leftMV);
            } else {
                projectedLeft = new RealMatrixImpl(leftMV.getColumn(i));
            }

            if (this.leftVector != null) {
                projectedLeft = projectedLeft.add(leftVector);
            }

            double[] x = new double[2];
            x[0] = xLow[0];
            x[1] = xUp[0];
            resProb[i] = 0.0; 
        }

        for (int i = 0; i < resProb.length; i++) {
            if (this.compareOperator.equals("<")) {
                if (resProb[i] >= this.prob) {
                    return false;
                }
            } else if (this.compareOperator.equals("<=")) {
                if (resProb[i] > this.prob) {
                    return false;
                }
            } else if (this.compareOperator.equals("=")) {
                if (resProb[i] != this.prob) {
                    return false;
                }
            } else if (this.compareOperator.equals(">=")) {
                if (resProb[i] < this.prob) {
                    return false;
                }
            } else if (this.compareOperator.equals(">")) {
                if (resProb[i] <= this.prob) {
                    return false;
                }
            }

            // throw new IllegalArgumentException("Operator " +
            // this.compareOperator + " cannot be evaluated.");
        }
        return true;

    }

    @Override
    public boolean evaluate(MVTuple<T> left, MVTuple<T> right) {

        double[] leftMVarray = new double[left.getMeasurementValuePositions().length];
        for (int i = 0; i < left.getMeasurementValuePositions().length; i++) {
            leftMVarray[i] = (Double) left.getAttribute(left.getMeasurementValuePositions()[i]);
        }

        RealMatrix leftMV = new RealMatrixImpl(leftMVarray);

        double[] rightMVarray = new double[right.getMeasurementValuePositions().length];
        for (int i = 0; i < right.getMeasurementValuePositions().length; i++) {
            rightMVarray[i] = (Double) right.getAttribute(right.getMeasurementValuePositions()[i]);
        }

        RealMatrix rightMV = new RealMatrixImpl(rightMVarray);

        RealMatrix projectedLeft = null;
        RealMatrix projectedRight = null;

        if (this.leftSchema.get(0).getSourceName().equals(this.leftSource)) {
            projectedLeft = this.leftMatrix.multiply(leftMV);
            if (this.leftVector != null) {
                projectedLeft = projectedLeft.add(leftVector);
            }

            projectedRight = this.rightMatrix.multiply(rightMV);
            if (this.rightVector != null) {
                projectedRight = projectedRight.add(rightVector);
            }
        } else {
            projectedLeft = this.rightMatrix.multiply(leftMV);
            if (this.rightVector != null) {
                projectedLeft = projectedLeft.add(rightVector);
            }

            projectedRight = this.leftMatrix.multiply(rightMV);
            if (this.rightVector != null) {
                projectedRight = projectedRight.add(leftVector);
            }
        }

        double resProb = 0.0; 

        if (this.compareOperator.equals("<")) {
            return resProb < this.prob;
        } else if (this.compareOperator.equals("<=")) {
            return resProb <= this.prob;
        } else if (this.compareOperator.equals("=")) {
            return resProb == this.prob;
        } else if (this.compareOperator.equals(">=")) {
            return resProb >= this.prob;
        } else if (this.compareOperator.equals(">")) {
            return resProb > this.prob;
        }

        throw new IllegalArgumentException("Operator " + this.compareOperator + " cannot be evaluated.");
    }

    public String getLeftSource() {
        return this.leftSource;
    }

    public String getRightSource() {
        return this.rightSource;
    }

    public void setCalcMarginalDist(boolean b) {
        this.calcMarginalDist = b;
    }

    @Override
    public List<SDFAttribute> getAttributes() {
        return SDFSchema.union(leftSchema, rightSchema).getAttributes();
    }

    // @Override
    public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
        this.replacementMap.put(curAttr, newAttr);

    }

    @Override
    public ProbabilityPredicate<T> clone() {
        return new ProbabilityPredicate<T>(this);
    }

}
