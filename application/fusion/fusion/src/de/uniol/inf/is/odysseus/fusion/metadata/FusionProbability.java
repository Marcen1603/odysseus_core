/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.fusion.metadata;

import de.uniol.inf.is.odysseus.fusion.util.matrix.Matrix;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

public class FusionProbability extends TimeInterval implements IFusionProbability {
	
	private static final long serialVersionUID = -3479265910143541469L;

	// priori error estimate covariance matrix (P'(k)): P'(k)=A*P(k-1)*At + Q) 
	private Matrix error_cov_pre = null;
	 
	// posteriori error estimate covariance matrix (P(k)): P(k)=(I-K(k)*H)*P'(k) 
	private Matrix error_cov_post = null;
	
	// process noise covariance matrix (Q)
	private Matrix process_noise_cov = null;
		
	// measurement noise covariance matrix (R) 
	private Matrix measurement_noise_cov = null;
	
	@Override
	public Matrix getError_cov_post() {
		return this.error_cov_post;
	}

	@Override
	public Matrix getError_cov_pre() {
		return this.error_cov_pre;
	}

	@Override
	public Matrix getMeasurement_noise_cov() {
		return this.measurement_noise_cov;
	}

	@Override
	public Matrix getProcess_noise_cov() {
		return this.process_noise_cov;
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
	public void setMeasurement_noise_cov(Matrix measurement_noise_cov) {
		this.measurement_noise_cov = measurement_noise_cov;
	}

	@Override
	public void setProcess_noise_cov(Matrix process_noise_cov) {
		this.process_noise_cov = process_noise_cov;
	}



	

	
	
}
