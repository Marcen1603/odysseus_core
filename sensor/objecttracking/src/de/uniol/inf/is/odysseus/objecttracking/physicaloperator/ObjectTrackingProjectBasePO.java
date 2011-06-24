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

import java.util.ArrayList;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.monitoring.StaticValueMonitoringData;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingProjectAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * This is the superclass of the our project PO.
 * TODO: In einer Klasse vereinheitlichen
 * @author Andre Bolles
 * @deprecated Not used any more
 */
public class ObjectTrackingProjectBasePO<T extends IProbability> extends
		AbstractPipe<MVRelationalTuple<T>, MVRelationalTuple<T>> {

	
	int[] restrictList;
	RealMatrix projectMatrix;
	SDFAttributeList inputSchema;
	int[] inputMeasurementValuePositions;
	
	SDFAttributeList outputSchema;
	
	public ObjectTrackingProjectBasePO(ObjectTrackingProjectAO ao){
		this.restrictList = ao.determineRestrictList();
		this.projectMatrix = ao.determineProjectMatrix(this.restrictList);
		this.inputSchema = ao.getInputSchema();
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i<this.inputSchema.size(); i++){
			SDFAttribute attr = this.inputSchema.get(i);
			if(attr.getDatatype().isMeasurementValue()){
				list.add(new Integer(i));	
			}
		}
		
		this.inputMeasurementValuePositions = new int[list.size()];
		for(int i = 0; i<this.inputMeasurementValuePositions.length; i++){
			this.inputMeasurementValuePositions[i] = list.get(i);
		}
		
		this.outputSchema = ao.getOutputSchema();
	}
	
//	public ObjectTrackingProjectBasePO(int[] restrictList, RealMatrix projectMatrix, RealMatrix projectVector,
//			SDFAttributeList inputSchema, SDFAttributeList outputSchema) {
//		this.restrictList = restrictList;
//		this.projectMatrix = projectMatrix;
//		this.inputSchema = inputSchema;
//		this.outputSchema = outputSchema;
//		
//		final ObjectTrackingProjectBasePO<T> t = this;
//		this.addMonitoringData("selectivity", new StaticValueMonitoringData<Double>(t,
//				"selectivity", 1d));
//	}

	public ObjectTrackingProjectBasePO(ObjectTrackingProjectBasePO<T> copy) {
		super();
		int length = copy.restrictList.length;
		restrictList = new int[length];
		System.arraycopy(copy.restrictList, 0, restrictList, 0, length);
		
		this.projectMatrix = copy.projectMatrix.copy();
		this.inputSchema = copy.inputSchema.clone();
		
		final ObjectTrackingProjectBasePO<T> t = this;
		this.addMonitoringData("selectivity", new StaticValueMonitoringData<Double>(t,
				"selectivity", 1d));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void process_next(MVRelationalTuple<T> object, int port) {
		
		// restrict the original tuple and set the new metadata
		//object.findMeasurementValuePositions(this.inputSchema);
		object.setMeasurementValuePositions(this.inputMeasurementValuePositions);
		MVRelationalTuple objectNew = object.restrict(this.restrictList, this.projectMatrix, false);
		
		// transfer the new tuple
		transfer(objectNew);
	}

	@Override
	public ObjectTrackingProjectBasePO<T> clone() {
		return new ObjectTrackingProjectBasePO<T>(this);
	}
	
	/**
	 * If a multivariate projection has no matrix given, one
	 * has to be explicitly defined to be able to project the covariance
	 * matrix. In this case a normal relational projection will be done.
	 * That means that the projection matrix must contain only zeros. Furthermore
	 * it must be composed of the same number of rows as number of measurement output
	 * attributes. In every column of an input attribute to be projected out, there
	 * must be a ONE. 
	 * 
	 * Example: / 1 0 0 0 \
	 * 			\ 0 0 1 0 /
	 * @throws OpenFailedException
	 * @deprecated project matrix is calculated from restrict list 
	 */
	@Override
	public void process_open() throws OpenFailedException{
		super.process_open();
		
		if(projectMatrix == null){
			
			// first find out, how many measurement attributes exist
			// in the input schema
			int inAttrCount = 0;
			for(int i = 0; i<this.inputSchema.size(); i++){
				SDFAttribute attr = this.inputSchema.get(i);
				if(attr.getDatatype().isMeasurementValue()){
					inAttrCount++;	
				}
			}
			
			
			// then find out how many measurement attributes will
			// be in the output schema;
			int outAttrCount = 0;
			for(int i =0; i<this.restrictList.length; i++){
				if(this.inputSchema.get(this.restrictList[i]).getDatatype().isMeasurementValue()){
					outAttrCount++;
				}
			}
			
			// if there is no measurement attribute return
			if(outAttrCount == 0 || inAttrCount == 0){
				return;
			}
			
			double[][] matrix = new double[outAttrCount][inAttrCount];
			
			
			
			// fill up the matrix with zeros
			for(int i = 0; i<matrix.length; i++){
				for(int u = 0; u<matrix[i].length; u++){
					matrix[i][u] = 0;
				}
			}
			
			// for every out measurement attribute (ROW) set a 1.0 in
			// the column <pos>, where pos is the number of the measurement
			// attribute in the input schema. (see the example in the JavaDoc of this method)
			int count = 0;
			for(int i = 0; i<this.restrictList.length; i++){
				if(this.inputSchema.get(this.restrictList[i]).getDatatype().isMeasurementValue()){
					// It the measurement attribute is at position
					// x in the input schema I have to find out
					// how many measurement attributes are at
					// positions 0 ... x - 1
					int measurementInputPos = 0;
					for(int v = 0; v<this.restrictList[i]; v++){
						if(this.inputSchema.get(v).getDatatype().isMeasurementValue()){
							measurementInputPos++;
						}
					}
					matrix[count++][measurementInputPos] = 1.0;
				}
			}
			projectMatrix = new RealMatrixImpl(matrix);
			
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
}
