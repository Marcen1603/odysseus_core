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

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.fusion.metadata.IFusionProbability;
import de.uniol.inf.is.odysseus.fusion.util.matrix.Matrix;

public class SpatialFilterPO  extends AbstractPipe<Tuple<? extends IFusionProbability>, Tuple<? extends IFusionProbability>> {
    
    // temporary matrices
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
	protected void process_next(Tuple<? extends IFusionProbability> tuple, int port) {
		Point centroid = (Point)tuple.getAttribute(2);
		
		Matrix measure = new Matrix(2, 1); // measurement [x]
		measure.set(0, 0, centroid.getX());
		measure.set(1, 0, centroid.getY());
		
		correct(tuple.getMetadata(),measure);
		tuple.getMetadata().setError_cov_post(updateCovariance(tuple.getMetadata()));
		transfer(tuple);
		process_done();
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractPipe<Tuple<? extends IFusionProbability>, Tuple<? extends IFusionProbability>> clone() {
		return this.clone();
	}
	
    private Matrix correct(IFusionProbability metadata, Matrix measurment) {
        // (1) Compute the Kalman gain
        // temp1 = H*P'(k)
        temp1 =  metadata.getMeasurement_matrix().times(metadata.getError_cov_pre());     
        // temp2 = temp1*Ht + R 
        temp2 = temp1.gemm(metadata.getMeasurement_matrix().transpose(), metadata.getMeasurement_noise_cov(), 1, 1);
        // temp3 = inv(temp2)*temp1 = Kt(k) 
        temp3 = temp2.solve(temp1);
        // K(k) 
        metadata.setGain(temp3.transpose());
        // (2) Update estimate with measurement z(k)
        // temp4 = z(k) - H*x'(k) 
        temp4 = metadata.getMeasurement_matrix().gemm(metadata.getState_pre(), measurment, -1, 1);
        // x(k) = x'(k) + K(k)*temp5 
        metadata.setState_post(metadata.getGain().gemm(temp4, metadata.getState_pre(), 1, 1));
        return metadata.getState_post();
    }
    
    private Matrix updateCovariance(IFusionProbability metatdata){
    	// (3) Update the error covariance.
        // P(k) = P'(k) - K(k)*temp2 
        return metatdata.getGain().gemm(metatdata.getMeasurement_matrix().times(metatdata.getError_cov_pre()), metatdata.getError_cov_pre(), -1, 1);
    }

}
