package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

public class MahalanobisDistanceEvaluationPO<M extends IProbability & IConnectionContainer> extends
    AbstractHypothesisEvaluationPO<M> {

  private static final String THRESHOLD_ID = "threshold";
  private static final String OPERATOR_ID = "operator";

  private static final String LESS = "LESS";
  private static final String LESS_EQUAL = "LESSEQUAL";
  private static final String GREATER = "GREATER";
  private static final String GREATER_EQUAL = "GREATEREQUAL";
  private static final String EQUAL = "EQUAL";

  private double threshold = 5;
  private String operator = LESS_EQUAL;

  public MahalanobisDistanceEvaluationPO() {
  }

  public MahalanobisDistanceEvaluationPO(MahalanobisDistanceEvaluationPO<M> clone) {
    super(clone);
    this.threshold = clone.threshold;
    this.operator = clone.operator;
  }

  public double evaluate(double[][] scannedObjCovariance, double[] scannedObjMesurementValues,
      double[][] predictedObjCovariance, double[] prdictedObjMesurementValues, double currentRatingValue) {

    RealMatrix leftV = new RealMatrixImpl(scannedObjMesurementValues);
    RealMatrix rightV = new RealMatrixImpl(prdictedObjMesurementValues);

    RealMatrix rightCovMatrix = new RealMatrixImpl(scannedObjCovariance);
    RealMatrix leftCovMatrix = new RealMatrixImpl(predictedObjCovariance);

    RealMatrix covInvMatrix = rightCovMatrix.add(leftCovMatrix);
    try {
      covInvMatrix = covInvMatrix.inverse();
    } catch (Exception e) {
      return 0;
    }

    RealMatrix distanceMatrix = leftV.subtract(rightV).transpose().multiply(covInvMatrix).multiply(
        leftV.subtract(rightV));
    double distance = Math.abs(distanceMatrix.getEntry(0, 0));

    if (this.operator.toUpperCase().equals(LESS)) {
      if (distance < this.threshold) {
        return 100;
      } else {
        return 0;
      }
    } else if (this.operator.toUpperCase().equals(LESS_EQUAL)) {
      if (distance <= this.threshold) {
        return 100;
      } else {
        return 0;
      }
    } else if (this.operator.toUpperCase().equals(EQUAL)) {
      if (distance == this.threshold) {
        return 100;
      } else {
        return 0;
      }
    } else if (this.operator.toUpperCase().equals(GREATER_EQUAL)) {
      if (distance >= this.threshold) {
        return 100;
      } else {
        return 0;
      }
    } else if (this.operator.toUpperCase().equals(GREATER)) {
      if (distance > this.threshold) {
        return 100;
      } else {
        return 0;
      }
    }
    return -1;
  }

  public void initAlgorithmParameter() {
    if (this.getAlgorithmParameter().containsKey(THRESHOLD_ID)) {
      this.threshold = Double.valueOf(this.getAlgorithmParameter().get(THRESHOLD_ID));
    }
    if (this.getAlgorithmParameter().containsKey(OPERATOR_ID)) {
      this.operator = this.getAlgorithmParameter().get(OPERATOR_ID);
    }
  }

  @Override
  public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
    return new MahalanobisDistanceEvaluationPO<M>(this);
  }
}