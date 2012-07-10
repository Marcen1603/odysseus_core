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
package de.uniol.inf.is.odysseus.fusion.physicaloperator.filter;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.fusion.metadata.IFusionProbability;
import de.uniol.inf.is.odysseus.fusion.util.matrix.Matrix;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class SpatialFilterPO  extends AbstractPipe<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

    /** measurement noise covariance matrix (R) */
    Matrix measurement_noise_cov;
	
    /** measurement matrix (H) */    
    Matrix measurement_matrix;

    /** Kalman gain matrix (K(k)): K(k)=P'(k)*Ht*inv(H*P'(k)*Ht+R) */
    Matrix gain;
    
    /** temporary matrices */
    Matrix temp1;
    Matrix temp2;
    Matrix temp3;
    Matrix temp4;
	
	public SpatialFilterPO(SDFSchema outputSchema) { 
		// TODO Auto-generated constructor stub
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(Tuple<? extends IMetaAttribute> tuple, int port) {
		
		Matrix state = (Matrix)tuple.getAttribute(0);
		Matrix measurment =  (Matrix)tuple.getAttribute(1);
		Matrix error_cov_pre = ((IFusionProbability)tuple.getMetadata()).getError_cov_pre();
		
		correct(state, measurment, error_cov_pre);
		((IFusionProbability)tuple.getMetadata()).setError_cov_post(updateCovariance(error_cov_pre));
		
		transfer(tuple);
		process_done();
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractPipe<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> clone() {
		return this.clone();
	}
	
    private Matrix correct(final Matrix state, final Matrix measurement, Matrix error_cov_pre) {
    	Matrix state_post;
    	
        // (1) Compute the Kalman gain
        // temp1 = H*P'(k)
        temp1 = measurement_matrix.times(error_cov_pre);        

        // temp2 = temp1*Ht + R 
        temp2 = temp1.gemm(measurement_matrix.transpose(), measurement_noise_cov, 1, 1);

        // temp3 = inv(temp2)*temp1 = Kt(k) 
        temp3 = temp2.solve(temp1);

        // K(k) 
        gain = temp3.transpose();
        
        // (2) Update estimate with measurement z(k)
        // temp4 = z(k) - H*x'(k) 
        temp4 = measurement_matrix.gemm(state, measurement, -1, 1);

        // x(k) = x'(k) + K(k)*temp5 
        state_post = gain.gemm(temp4, state, 1, 1);


        return state_post;
    }
    
    private Matrix updateCovariance(Matrix error_cov_pre){
    	
    	// (3) Update the error covariance.
        // P(k) = P'(k) - K(k)*temp2 
        return gain.gemm(measurement_matrix.times(error_cov_pre), error_cov_pre, -1, 1);
    }

}
