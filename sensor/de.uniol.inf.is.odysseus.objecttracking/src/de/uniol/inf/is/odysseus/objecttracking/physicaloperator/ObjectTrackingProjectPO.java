package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingProjectAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
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
@SuppressWarnings("unchecked")
public class ObjectTrackingProjectPO<T extends IProbability & IPredictionFunctionKey> extends ObjectTrackingProjectBasePO<T> {

	
	public ObjectTrackingProjectPO(ObjectTrackingProjectAO ao){
		super(ao);
	}
	
//	public ObjectTrackingProjectPO(int[] restrictList, RealMatrix projectMatrix, RealMatrix projectVector, SDFAttributeList inputSchema, SDFAttributeList outputSchema) {
//		super(restrictList, projectMatrix, projectVector, inputSchema, outputSchema);
//	}
	
	public ObjectTrackingProjectPO(ObjectTrackingProjectPO<T> copy) {
		super(copy);
	}
	
	@Override
	public ObjectTrackingProjectPO<T> clone() {
		return new ObjectTrackingProjectPO<T>(this);
	}
	
	@Override
	final protected void process_next(MVRelationalTuple<T> object, int port) {

		try {
			// restrict the original tuple and set the new metadata
			MVRelationalTuple objectNew = (MVRelationalTuple)object.restrict(this.restrictList, this.projectMatrix, false);
			
			// updating the prediction function
			// is not necessary, since the
			// prediction function is not contained
			// in the tuple any more. Only the
			// key for the prediction function is
			// contained there. However, this key
			// will not be changed by the prediction po.
//			mData = updatePrdFct(mData);
//			objectNew.setMetadata(mData);
			
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
	 * 
	 * This cannot be done in advance, since every element
	 * in a data stream can have a different prediction function.
	 * 
	 * TODO: Da die Menge an Prädiktionsfunktionen endlich ist,
	 * wird man wohl auch das vorher berechnen können und dann
	 * einfach nur schon aus den bekannten Prädiktionsfunktionen
	 * auswählen, ob null gesetzt werden muss oder nicht.
	 * 
	 * @param mData The prediction function to be updated
	 * @return the updated prediction function.
	 * 
	 * @deprecated We do not have to update the prediction function 
	 * any more. This is because, no prediction function is contained
	 * in the tuple. Only the key for the prediction function is contained
	 * in the tuple. However, this key will not be changed by the project po.
	 */
//	@Deprecated
//	private T updatePrdFct(T mData){
//		int[][] variables = mData.getVariables();
//		SDFExpression[] expressions = mData.getExpressions();
//		
//		int[][] newVariables = new int[this.restrictList.length][];
//		SDFExpression[] newExpressions = new SDFExpression[this.restrictList.length];
//		
//		// only expressions are needed
//		// that affect attributes, that
//		// are also in the output
//		for(int i: this.restrictList){
//			newVariables[i] = variables[i];
//			newExpressions[i] = expressions[i];
//			newExpressions[i].initAttributePositions(this.outputSchema);
//		}
//		
//		for(int i =0; i<newVariables.length; i++){
//			vars:
//			for(int u = 0; u<newVariables[i].length; u++){
//				for(int v = 0; v<restrictList[v]; v++){
//					if(restrictList[v] == newVariables[i][u]){
//						continue vars;
//					}
//				}
//				
//				mData.setExpressions(null);
//				mData.setTimeInterval(null);
//				return mData;
//			}
//		}
//		
//		// if all needed prediction functions only use variables
//		// that are availabe after the projection
//		// then these prediction functions can be used further
//		mData.setExpressions(newExpressions);
//		mData.initVariables();
//		return mData;
//	}
}
