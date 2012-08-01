package de.uniol.inf.is.odysseus.fusion.metadata;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.fusion.util.matrix.Matrix;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

public class FusionProbability extends TimeInterval implements IFusionProbability, Cloneable, Serializable{
	
	private static final long serialVersionUID = -3479265910143541469L;

	private int dp = 4;
	
	// priori error estimate covariance matrix (P'(k)): P'(k)=A*P(k-1)*At + Q) 
	private Matrix error_cov_pre = null;
	 
	// posteriori error estimate covariance matrix (P(k)): P(k)=(I-K(k)*H)*P'(k) 
	private Matrix error_cov_post = null;

	private Matrix process_noise_cov = null;
	
	public FusionProbability() {
		error_cov_pre = new Matrix(dp, dp);
		error_cov_post = Matrix.identity(dp, dp); // 1 (0 in OpenCV)
		process_noise_cov  = Matrix.identity(dp, dp, 1e-3);
	}
	
	public FusionProbability(ITimeInterval timeInterval) {
		this.setStart(timeInterval.getStart());
		this.setEnd(timeInterval.getEnd());
		
		error_cov_pre = new Matrix(dp, dp);
		error_cov_post = Matrix.identity(dp, dp); // 1 (0 in OpenCV)
		process_noise_cov = Matrix.identity(dp, dp, 1e-3);
	}
	
	@Override
	public Matrix getError_cov_post() {
		return this.error_cov_post;
	}

	@Override
	public Matrix getError_cov_pre() {
		return this.error_cov_pre;
	}

	@Override
	public void setError_cov_post(Matrix error_cov_post) {
		this.error_cov_post = error_cov_post;
	}

	@Override
	public void setError_cov_pre(Matrix error_cov_pre) {
		this.error_cov_pre = error_cov_pre;
	}

	@Override
	public TimeInterval clone() {
		return new FusionProbability(this);
	}

	@Override
	public Matrix getProcess_noise_cov() {
		// TODO Auto-generated method stub
		return null;
	}

}
