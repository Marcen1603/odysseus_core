/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import org.apache.commons.math.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.objecttracking.IProbabilityPredictionFunctionKeyLatency;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingProjectAO;

/**
 * This operator works the same way as relationalProjectMVPO. It additionally deletes
 * the prediction function, if after the projection some attributes necessary for
 * the prediction function.
 * 
 * @author Andre Bolles
 *
 * @param <T>
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ObjectTrackingProjectPO<T extends IProbabilityPredictionFunctionKeyLatency> extends AbstractPipe<MVRelationalTuple<T>, MVRelationalTuple<T>>{

//	private final String LOGGER_NAME = "ObjectTrackingProjectPO";
	
	int[] restrictList;
	RealMatrix projectMatrix;
	SDFSchema inputSchema;
	SDFSchema outputSchema;
	
	
	public ObjectTrackingProjectPO(ObjectTrackingProjectAO ao){
		super();
		this.restrictList = ao.determineRestrictList();
		this.projectMatrix = ao.determineProjectMatrix(this.restrictList);
		this.inputSchema = ao.getInputSchema();
		this.outputSchema = ao.getOutputSchema();
		
	}
	
//	public ObjectTrackingProjectPO(int[] restrictList, RealMatrix projectMatrix, RealMatrix projectVector, SDFSchema inputSchema, SDFSchema outputSchema) {
//		super(restrictList, projectMatrix, projectVector, inputSchema, outputSchema);
//	}
	
	public ObjectTrackingProjectPO(ObjectTrackingProjectPO<T> copy) {
		super();
		int length = copy.restrictList.length;
		restrictList = new int[length];
		System.arraycopy(copy.restrictList, 0, restrictList, 0, length);
		
		this.projectMatrix = copy.projectMatrix.copy();
		this.inputSchema = copy.inputSchema.clone();
	}
	
	@Override
	public ObjectTrackingProjectPO<T> clone() {
		return new ObjectTrackingProjectPO<T>(this);
	}
	
	@Override
	final protected void process_next(MVRelationalTuple<T> object, int port) {

		try {
			// restrict the original tuple and set the new metadata
			MVRelationalTuple<T> objectNew = (MVRelationalTuple<T>)object.restrict(this.restrictList, this.projectMatrix, false);
			
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
	
	@Override
	public OutputMode getOutputMode(){
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}
	
//	/**
//	 * This method deletes the prediction function from 
//	 * the metadata, if not all necessary attributes are
//	 * available after projection. Also the schema of
//	 * the prediction function will be updated and only
//	 * expressions affecting any of the output attributes
//	 * will be in the prediction function.
//	 * 
//	 * This cannot be done in advance, since every element
//	 * in a data stream can have a different prediction function.
//	 * 
//	 * TODO: Da die Menge an Prädiktionsfunktionen endlich ist,
//	 * wird man wohl auch das vorher berechnen können und dann
//	 * einfach nur schon aus den bekannten Prädiktionsfunktionen
//	 * auswählen, ob null gesetzt werden muss oder nicht.
//	 * 
//	 * @param mData The prediction function to be updated
//	 * @return the updated prediction function.
//	 * 
//	 * @deprecated We do not have to update the prediction function 
//	 * any more. This is because, no prediction function is contained
//	 * in the tuple. Only the key for the prediction function is contained
//	 * in the tuple. However, this key will not be changed by the project po.
//	 */
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
