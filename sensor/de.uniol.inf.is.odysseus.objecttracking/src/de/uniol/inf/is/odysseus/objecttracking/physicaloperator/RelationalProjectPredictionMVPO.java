package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * This operator works the same way as relationalProjectMVPO. It additionally deletes
 * the prediction function, if after the projection some attributes necessary for
 * the prediction function.
 * 
 * @author Andre Bolles
 *
 * @param <T>
 */
public class RelationalProjectPredictionMVPO<T extends IProbability & IPredictionFunction> extends RelationalProjectMVPO<T> {

	
	public RelationalProjectPredictionMVPO(int[] restrictList, RealMatrix projectMatrix, RealMatrix projectVector, SDFAttributeList inputSchema) {
		super(restrictList, projectMatrix, projectVector, inputSchema);
	}
	
	public RelationalProjectPredictionMVPO(RelationalProjectPredictionMVPO<T> copy) {
		super(copy);
	}
	
	@Override
	public RelationalProjectPredictionMVPO<T> clone() {
		return new RelationalProjectPredictionMVPO<T>(this);
	}
	
	@Override
	final protected void process_next(MVRelationalTuple<T> object, int port) {

		try {
			// first project the metadata:
			T mData = object.getMetadata();
			RealMatrixImpl c = new RealMatrixImpl(mData.getCovariance());
			double[][] covProjected = this.projectMatrix.multiply(c).multiply(this.projectMatrix.transpose()).getData();
			mData.setCovariance(covProjected);
			
			// restrict the original tuple and set the new metadata
			object.findMeasurementValuePositions(this.inputSchema);
			MVRelationalTuple objectNew = object.restrict(this.restrictList, this.projectMatrix, this.projectVector, null, this.inputSchema);
			
			// update the prediction function
			mData = updatePrdFct(mData);
			objectNew.setMetadata(mData);
			
			// transfer the new tuple
			transfer(objectNew);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method deletes the prediction function from 
	 * the metadata, if not all necessary attributes are
	 * available after projection. Also the schema of
	 * the prediction function will be updated and only
	 * expressions affecting any of the output attributes
	 * will be in the prediction function.
	 * @param mData The prediction function to be updated
	 * @return the updated prediction function.
	 */
	private T updatePrdFct(T mData){
		int[][] variables = mData.getVariables(this.inputSchema);
		SDFExpression[] expressions = mData.getPredictionFunction();
		
		int[][] newVariables = new int[this.restrictList.length][];
		SDFExpression[] newExpressions = new SDFExpression[this.restrictList.length];
		
		// only expressions are needed
		// that affect attributes, that
		// are also in the output
		for(int i: this.restrictList){
			newVariables[i] = variables[i];
			newExpressions[i] = expressions[i];
		}
		
		for(int i =0; i<newVariables.length; i++){
			vars:
			for(int u = 0; u<newVariables[i].length; u++){
				for(int v = 0; v<restrictList[v]; v++){
					if(restrictList[v] == newVariables[i][u]){
						continue vars;
					}
				}
				
				mData.setPredictionFunction(null);
				mData.setTimeInterval(null);
				return mData;
			}
		}
		
		return mData;
	}
}
