package de.uniol.inf.is.odysseus.fusion.metadata;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.fusion.util.matrix.Matrix;



public interface IFusionProbability extends ITimeInterval,IMetaAttribute{		    
		 
	public Matrix getError_cov_post();
	public Matrix getError_cov_pre();
	 
	public void setError_cov_post(Matrix error_cov_post);
	public void setError_cov_pre(Matrix error_cov_pre);

	public Matrix getTransition_matrix();
	public void setTransition_matrix(Matrix transition_matrix);

	public Matrix getState_pre();
	public void setState_pre(Matrix state_pre);

	public Matrix getState_post();
	public void setState_post(Matrix state_post) ;

	public Matrix getControl_matrix();
	public void setControl_matrix(Matrix control_matrix);
	
	public Matrix getProcess_noise_cov();
	public void setProcess_noise_cov(Matrix noise_cov);
	
	public Matrix getGain();
	public void setGain(Matrix gain);

	public Matrix getMeasurement_matrix();
	public void setMeasurement_matrix(Matrix measurement_matrix);
	
	public Matrix getMeasurement_noise_cov();
	public void setMeasurement_noise_cov(Matrix measurement_noise_cov);
	
	public int getCp();
	public void setCp(int cp);
}
