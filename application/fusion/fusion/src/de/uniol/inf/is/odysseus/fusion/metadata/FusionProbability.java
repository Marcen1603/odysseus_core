package de.uniol.inf.is.odysseus.fusion.metadata;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.fusion.util.matrix.Matrix;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

public class FusionProbability extends TimeInterval implements IFusionProbability, Cloneable, Serializable{
	
	private static final long serialVersionUID = -3479265910143541469L;

    /** number of measurement vector dimensions */
    int mp = 2;
    /** number of state vector dimensions */
    int dp = 4;
    /** number of control vector dimensions */
    int cp = 0;
	
    // predicted state (x'(k)): x(k)=A*x(k-1)+B*u(k) 
    private  Matrix state_pre;   
    
    // state transition matrix (A) */
    private Matrix transition_matrix;
    
    // corrected state (x(k)): x(k)=x'(k)+K(k)*(z(k)-H*x'(k)) 
    private Matrix state_post;
    
    // control matrix (B) (it is not used if there is no control)
    private Matrix control_matrix;
    
    // priori error estimate covariance matrix (P'(k)): P'(k)=A*P(k-1)*At + Q) 
 	private Matrix error_cov_pre = null;
 	 
 	// posteriori error estimate covariance matrix (P(k)): P(k)=(I-K(k)*H)*P'(k) 
 	private Matrix error_cov_post = null;

 	private Matrix process_noise_cov = null;
	
    // measurement noise covariance matrix (R) 
 	private Matrix measurement_noise_cov;
	
 	// measurement matrix (H) */    
 	private  Matrix measurement_matrix;

 	// Kalman gain matrix (K(k)): K(k)=P'(k)*Ht*inv(H*P'(k)*Ht+R)
 	private Matrix gain;
	
	public FusionProbability() {
		error_cov_pre = new Matrix(dp, dp);
		error_cov_post = Matrix.identity(dp, dp); // 1 (0 in OpenCV)
		state_pre = new Matrix(dp, 1); // init by transition _matrix*state_post
		state_post = new Matrix(dp, 1);
		
        double[][] tr = { 	{1, 0, 1, 0}, 
                			{0, 1, 0, 1}, 
                			{0, 0, 1, 0}, 
                			{0, 0, 0, 1} };
        transition_matrix = new Matrix(tr);

		process_noise_cov = Matrix.identity(dp, dp, 1e-3);
		
		measurement_matrix = Matrix.identity(mp, dp);
        measurement_noise_cov = Matrix.identity(mp, mp, 1e-1); // 1e-1 (1 in OpenCV)
        
        error_cov_pre = new Matrix(dp, dp); // initialized in Predict
        error_cov_post = Matrix.identity(dp, dp); // 1 (0 in OpenCV)
		
        gain = new Matrix(dp, mp);
	}
	
	public FusionProbability(ITimeInterval timeInterval) {
		this();
		this.setStart(timeInterval.getStart());
		this.setEnd(timeInterval.getEnd());
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
		return process_noise_cov;
	}

	@Override
	public void setProcess_noise_cov(Matrix process_noise_cov) {
		this.process_noise_cov = process_noise_cov;
	}
	
	public Matrix getTransition_matrix() {
	    return transition_matrix;
    }

	public void setTransition_matrix(Matrix transition_matrix) {
	    this.transition_matrix = transition_matrix;
    }

	public Matrix getState_pre() {
	    return state_pre;
    }

	public void setState_pre(Matrix state_pre) {
	    this.state_pre = state_pre;
    }

	public Matrix getState_post() {
	    return state_post;
    }

	public void setState_post(Matrix state_post) {
	    this.state_post = state_post;
    }

	public Matrix getControl_matrix() {
	    return control_matrix;
    }

	public void setControl_matrix(Matrix control_matrix) {
	    this.control_matrix = control_matrix;
    }

	public int getCp() {
	    return cp;
    }

	public void setCp(int cp) {
	    this.cp = cp;
    }

	public Matrix getGain() {
	    return gain;
    }

	public void setGain(Matrix gain) {
	    this.gain = gain;
    }

	public Matrix getMeasurement_matrix() {
	    return measurement_matrix;
    }

	public void setMeasurement_matrix(Matrix measurement_matrix) {
	    this.measurement_matrix = measurement_matrix;
    }

	public Matrix getMeasurement_noise_cov() {
	    return measurement_noise_cov;
    }

	public void setMeasurement_noise_cov(Matrix measurement_noise_cov) {
	    this.measurement_noise_cov = measurement_noise_cov;
    }

}
