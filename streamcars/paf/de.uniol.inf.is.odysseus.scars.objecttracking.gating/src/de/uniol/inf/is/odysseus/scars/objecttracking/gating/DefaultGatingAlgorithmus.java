package de.uniol.inf.is.odysseus.scars.objecttracking.gating;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

public class DefaultGatingAlgorithmus implements IGatingAlgorithmus {
	
	private double threshold;
	private String operator;
	private double[] leftVector;
	private double[] rightVector;
	private double[][] covarianceMatrix;
	private double distance;
	
	@Override
	public Object executeGatingAlgorithmus() {
		RealMatrix left = new RealMatrixImpl(leftVector);
		RealMatrix right = new RealMatrixImpl(rightVector);
		RealMatrix rightCovMatrix = new RealMatrixImpl(this.covarianceMatrix);
		RealMatrix rightCovInvMatrix = rightCovMatrix.inverse();
		
		// (left - right)^T rightCov (left - right)
		RealMatrix distanceMatrix = left.subtract(right).transpose().multiply(rightCovInvMatrix).multiply(left.subtract(right));
		
		distance = distanceMatrix.getEntry(0, 0);
		
		if(this.operator.equals("<")){
			return distance < this.threshold;
		}else if(this.operator.equals("<=")){
			return distance <= this.threshold;
		}else if(this.operator.equals("=")){
			return distance == this.threshold;
		}else if(this.operator.equals(">=")){
			return distance >= this.threshold;
		}else{
			return distance > this.threshold;
		}
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public double[] getLeftVector() {
		return leftVector;
	}

	public void setLeftVector(double[] leftVector) {
		this.leftVector = leftVector;
	}

	public double[] getRightVector() {
		return rightVector;
	}

	public void setRightVector(double[] rightVector) {
		this.rightVector = rightVector;
	}

	public double[][] getCovarianceMatrix() {
		return covarianceMatrix;
	}

	public void setCovarianceMatrix(double[][] covarianceMatrix) {
		this.covarianceMatrix = covarianceMatrix;
	}

}
