package de.uniol.inf.is.odysseus.fusion.metadata;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.fusion.util.matrix.Matrix;



public interface IFusionProbability extends ITimeInterval,IMetaAttribute{		    
		 
	public Matrix getError_cov_post();
	public Matrix getError_cov_pre();
	public Matrix getMeasurement_noise_cov();
	public Matrix getProcess_noise_cov();
	 
	public void setError_cov_post(Matrix error_cov_post);
	public void setError_cov_pre(Matrix error_cov_pre);
	public void setMeasurement_noise_cov(Matrix measurement_noise_cov);
	public void setProcess_noise_cov(Matrix process_noise_cov);
	
}
