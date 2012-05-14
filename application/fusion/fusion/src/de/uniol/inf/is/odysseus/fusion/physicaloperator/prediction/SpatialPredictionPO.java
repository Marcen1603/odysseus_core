package de.uniol.inf.is.odysseus.fusion.physicaloperator.prediction;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.fusion.metadata.IFusionProbability;
import de.uniol.inf.is.odysseus.fusion.util.matrix.Matrix;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class SpatialPredictionPO  extends AbstractPipe<Tuple<? extends IFusionProbability>, Tuple<? extends IFusionProbability>> {

    /** number of control vector dimensions */
    int cp = 0;
	
    /** predicted state (x'(k)): x(k)=A*x(k-1)+B*u(k) */
    Matrix state_pre;   
    
    /** state transition matrix (A) */
    Matrix transition_matrix;
    
    /** corrected state (x(k)): x(k)=x'(k)+K(k)*(z(k)-H*x'(k)) */
    Matrix state_post;
    
    /** control matrix (B) (it is not used if there is no control)*/
    Matrix control_matrix;
	
    /** temporary matrices */
    Matrix temp;
    
	public SpatialPredictionPO(SDFSchema outputSchema) { 
		// TODO Auto-generated constructor stub
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(Tuple<? extends IFusionProbability> tuple, int port) {
		
		//Find the right attribute - AttributeResolver
		state_pre = predict((Matrix)tuple.getAttribute(0),null);
        
        Matrix error_cov = updateCovariance(((IFusionProbability)tuple.getMetadata()).getError_cov_pre(),((IFusionProbability)tuple.getMetadata()).getProcess_noise_cov());
        ((IFusionProbability)tuple.getMetadata()).setError_cov_pre(error_cov);
       
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

    private Matrix predict(Matrix state, Matrix control) {
        // (1) Project the state ahead
        // update the state: x'(k) = A*x(k)
        state_pre = transition_matrix.times(state);
        if( control != null && cp > 0 ) {
             // x'(k) = x'(k) + B*u(k)
             state_pre = control_matrix.gemm(control, state_pre, 1, 1);
        }
        // (2) Project the error covariance ahead
        // update error covariance matrices: temp1 = A*P(k)
        return state_pre;
    }
    
    private Matrix updateCovariance(Matrix error_cov_post, Matrix process_noise_cov){
    	 temp = transition_matrix.times(error_cov_post);
         // P'(k) = temp1*At + Q
         return temp.gemm(transition_matrix.transpose(), process_noise_cov, 1, 1);
    }
    
    
}
