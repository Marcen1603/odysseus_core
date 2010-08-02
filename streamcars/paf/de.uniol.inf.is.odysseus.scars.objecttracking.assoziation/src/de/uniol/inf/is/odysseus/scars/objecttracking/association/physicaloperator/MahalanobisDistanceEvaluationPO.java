package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;

public class MahalanobisDistanceEvaluationPO<M extends IProbability & IConnectionContainer> extends AbstractHypothesisEvaluationPO<M> {

	private double threshold = 5;
	private String operator = "<=";

	public double evaluate(MVRelationalTuple<M> tupleNew, MVRelationalTuple<M> tupleOld, ArrayList<int[]> mesurementValuePathsNew, ArrayList<int[]> mesurementValuePathsOld) {
		// new = left; old = right

		double[] leftMVVector = OrAttributeResolver.getMeasurementValues(mesurementValuePathsNew, tupleNew);
		RealMatrix leftV = new RealMatrixImpl(leftMVVector);

		double[] rightMVVector = OrAttributeResolver.getMeasurementValues(mesurementValuePathsOld, tupleOld);
		RealMatrix rightV = new RealMatrixImpl(rightMVVector);

		double[][] rightCov = tupleOld.getMetadata().getCovariance();
		RealMatrix rightCovMatrix = new RealMatrixImpl(rightCov);

		double[][] leftCov = tupleNew.getMetadata().getCovariance();
		RealMatrix leftCovMatrix = new RealMatrixImpl(leftCov);
		RealMatrix covInvMatrix = rightCovMatrix.add(leftCovMatrix);
		try {
			covInvMatrix = covInvMatrix.inverse();
		} catch (Exception e) {
			return 0;
		}

		RealMatrix distanceMatrix = leftV.subtract(rightV).transpose().multiply(covInvMatrix).multiply(leftV.subtract(rightV));
		double distance = Math.abs(distanceMatrix.getEntry(0, 0));

		if(this.operator.equals("<")){
			if(distance < this.threshold) {
				return 100;
			} else {
				return 0;
			}
		}

		else if(this.operator.equals("<=")){
			if(distance <= this.threshold) {
				return 100;
			} else {
				return 0;
			}
		}

		else if(this.operator.equals("=")){
			if(distance == this.threshold) {
				return 100;
			} else {
				return 0;
			}
		}

		else if(this.operator.equals(">=")){
			if(distance >= this.threshold) {
				return 100;
			} else {
				return 0;
			}
		}

		else if(distance > this.threshold) {
			return 100;
		} else {
			return 0;
		}
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public void initAlgorithmParameter() {
		this.threshold = Double.valueOf(this.getAlgorithmParameter().get("treshold"));
		this.operator = this.getAlgorithmParameter().get("operator");
	}

	public double getThreshold() {
		return threshold;
	}

	public String getOperator() {
		return operator;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new MahalanobisDistanceEvaluationPO<M>(this);
	}

	public MahalanobisDistanceEvaluationPO() {

	}

	public MahalanobisDistanceEvaluationPO(MahalanobisDistanceEvaluationPO<M> clone) {
		super(clone);
		this.threshold = clone.getThreshold();
		this.operator = clone.getOperator();
	}

}