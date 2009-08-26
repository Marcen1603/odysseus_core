package de.uniol.inf.is.odysseus.probability.predicate;

import java.util.List;

import matlab.MatLab;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTProbabilityPredicate.OperandTypes;
import de.uniol.inf.is.odysseus.probability.MVRelationalTuple;
import de.uniol.inf.is.odysseus.probability.metadata.IProbability;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class ProbabilityPredicate<T extends IProbability> extends
		AbstractPredicate<MVRelationalTuple<T>> implements IRelationalPredicate {

	private static final long serialVersionUID = 6467624302125491583L;

	// stores which attributes are needed at which position for
	// variable bindings
	private int[] attributePositions;

	// fromRightChannel[i] stores if the getAttribute(attributePositions[i])
	// should be called on the left or on the right input tuple
	private boolean[] fromRightChannel;

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

	SDFAttributeList leftSchema;
	SDFAttributeList rightSchema;

	OperandTypes calculationType;

	MatLab matlab;

	String compareOperator;

	/**
	 * If true, only marginal distributions will be calculated and no
	 * multivariate distributions
	 */
	boolean calcMarginalDist;

	public ProbabilityPredicate(String leftSource, double[][] leftMatrix,
			double[] leftVector, double[] xLow, double[] xUp, double prob,
			String compareOperator) {

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

	public ProbabilityPredicate(String leftSource, double[][] leftMatrix,
			double[] leftVector, String rightSource, double[][] rightMatrix,
			double[] rightVector, double[] xLow, double[] xUp, double prob,
			OperandTypes type, String compareOperator) {
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

		this.calculationType = type;
		this.compareOperator = compareOperator;
	}

	public SDFAttributeList getLeftSchema() {
		return leftSchema;
	}

	public void setLeftSchema(SDFAttributeList leftSchema) {
		this.leftSchema = leftSchema;
	}

	public SDFAttributeList getRightSchema() {
		return rightSchema;
	}

	public void setRightSchema(SDFAttributeList rightSchema) {
		this.rightSchema = rightSchema;
	}

	public void init() {
		try {
			this.matlab = new MatLab();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Only the left values are set.
	 */
	@Override
	public boolean evaluate(MVRelationalTuple<T> input) {

		double[] leftMVarray = new double[input.getMeasurementValuePositions().length];
		for (int i = 0; i < input.getMeasurementValuePositions().length; i++) {
			leftMVarray[i] = input.getAttribute(input
					.getMeasurementValuePositions()[i]);
		}

		RealMatrix leftMV = new RealMatrixImpl(leftMVarray);
		RealMatrix leftCov = new RealMatrixImpl(input.getMetadata()
				.getCovariance());

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

			RealMatrix projectedLeftCov = null;
			if (this.leftMatrix != null) {
				projectedLeft = this.leftMatrix.multiply(leftCov).multiply(
						this.leftMatrix.transpose());
			} else {
				projectedLeftCov = leftCov;
			}

			double[] mu = projectedLeft.getColumn(0);
			double[][] sigma = projectedLeftCov.getData();

			Object[] matlabRes = null;
			double resProb = 0;
			try {
				long start = 0;
				long end = 0;
				// Only calc multivariate, if dim > 1
				if (xLow.length > 1) {
					// start = System.nanoTime();
					matlabRes = this.matlab.calcMVN(1, this.xLow, this.xUp, mu,
							sigma);
					// end = System.nanoTime();
				}
				// calc normal if dim = 1
				else {
					double[] x = new double[2];
					x[0] = xLow[0];
					x[1] = xUp[0];
					// start = System.nanoTime();
					matlabRes = this.matlab.calcNormCDF(1, x, mu, sigma);
					// end = System.nanoTime();
				}
				// System.out.println("Mathe: " + (end - start));
			} catch (MWException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resProb = ((MWNumericArray) matlabRes[0]).getDouble();

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

			throw new IllegalArgumentException("Operator "
					+ this.compareOperator + " cannot be evaluated.");
		} else {
			double[] resProb = new double[this.xLow.length];
			for (int i = 0; i < this.xLow.length; i++) {
				RealMatrix projectedLeft = null;
				RealMatrix row = null;
				RealMatrix projectedLeftCov = null;
				if (this.leftMatrix != null) {
					row = this.leftMatrix.getSubMatrix(i, i, 0,
							(xLow.length - 1));
					projectedLeft = row.multiply(leftMV);
					projectedLeftCov = row.multiply(leftCov).multiply(
							row.transpose());
				} else {
					projectedLeft = new RealMatrixImpl(leftMV.getColumn(i));
					// select entry i,i from the matrix and generate a new
					// double array from it.
					double[] cov = { leftCov.getData()[i][i] };
					projectedLeftCov = new RealMatrixImpl(cov);
				}

				if (this.leftVector != null) {
					projectedLeft = projectedLeft.add(leftVector);
				}

				double[] mu = projectedLeft.getColumn(0);
				double[][] sigma = projectedLeftCov.getData();

				Object[] matlabRes = null;
				try {
					long start = 0;
					long end = 0;

					double[] x = new double[2];
					x[0] = xLow[0];
					x[1] = xUp[0];
					// start = System.nanoTime();
					matlabRes = this.matlab.calcNormCDF(1, x, mu, sigma);
					// end = System.nanoTime();
					// System.out.println("Mathe: " + (end - start));
				} catch (MWException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resProb[i] = ((MWNumericArray) matlabRes[0]).getDouble();
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
	}

	@Override
	public boolean evaluate(MVRelationalTuple<T> left,
			MVRelationalTuple<T> right) {

		double[] leftMVarray = new double[left.getMeasurementValuePositions().length];
		for (int i = 0; i < left.getMeasurementValuePositions().length; i++) {
			leftMVarray[i] = (Double) left.getAttribute(left
					.getMeasurementValuePositions()[i]);
		}

		RealMatrix leftMV = new RealMatrixImpl(leftMVarray);
		RealMatrix leftCov = new RealMatrixImpl(left.getMetadata()
				.getCovariance());

		double[] rightMVarray = new double[right.getMeasurementValuePositions().length];
		for (int i = 0; i < right.getMeasurementValuePositions().length; i++) {
			rightMVarray[i] = (Double) right.getAttribute(right
					.getMeasurementValuePositions()[i]);
		}

		RealMatrix rightMV = new RealMatrixImpl(rightMVarray);
		RealMatrix rightCov = new RealMatrixImpl(right.getMetadata()
				.getCovariance());

		RealMatrix projectedLeft = null;
		RealMatrix projectedLeftCov = null;

		RealMatrix projectedRight = null;
		RealMatrix projectedRightCov = null;

		if (((CQLAttribute) this.leftSchema.get(0)).getSourceName().equals(
				this.leftSource)) {
			projectedLeft = this.leftMatrix.multiply(leftMV);
			if (this.leftVector != null) {
				projectedLeft = projectedLeft.add(leftVector);
			}
			projectedLeftCov = this.leftMatrix.multiply(leftCov).multiply(
					this.leftMatrix.transpose());

			projectedRight = this.rightMatrix.multiply(rightMV);
			if (this.rightVector != null) {
				projectedRight = projectedRight.add(rightVector);
			}
			projectedRightCov = this.rightMatrix.multiply(rightCov).multiply(
					this.rightMatrix.transpose());
		} else {
			projectedLeft = this.rightMatrix.multiply(leftMV);
			if (this.rightVector != null) {
				projectedLeft = projectedLeft.add(rightVector);
			}
			projectedLeftCov = this.rightMatrix.multiply(leftCov).multiply(
					this.rightMatrix.transpose());

			projectedRight = this.leftMatrix.multiply(rightMV);
			if (this.rightVector != null) {
				projectedRight = projectedRight.add(leftVector);
			}
			projectedRightCov = this.leftMatrix.multiply(rightCov).multiply(
					this.leftMatrix.transpose());
		}

		// subtracting or adding the vectors
		double[] resultVector = null;
		if (calculationType == OperandTypes.MINUS) {
			resultVector = projectedLeft.subtract(projectedRight).getColumn(0);
		} else {
			resultVector = projectedLeft.add(projectedRight).getColumn(0);
		}

		// adding up the covariances
		double[][] resultCov = projectedLeftCov.add(projectedRightCov)
				.getData();

		Object[] matlabRes = null;
		try {
			long start = 0;
			long end = 0;
			// Only calc multivariate, if dim > 1
			if (xLow.length > 1) {
				start = System.nanoTime();
				matlabRes = this.matlab.calcMVN(1, this.xLow, this.xUp,
						resultVector, resultCov);
				end = System.nanoTime();
			}
			// calc normal if dim = 1
			else {
				double[] x = new double[2];
				x[0] = xLow[0];
				x[1] = xUp[0];
				start = System.nanoTime();
				matlabRes = this.matlab.calcNormCDF(1, x, resultVector,
						resultCov);
				end = System.nanoTime();
			}

			// matlabRes = this.matlab.calcMVN(1, this.xLow, this.xUp,
			// resultVector, resultCov);
		} catch (MWException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double resProb = ((MWNumericArray) matlabRes[0]).getDouble();

		// TODO kann man bestimmt sch�ner machen
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

		throw new IllegalArgumentException("Operator " + this.compareOperator
				+ " cannot be evaluated.");
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
		return SDFAttributeList.union(leftSchema, rightSchema);
	}

}
