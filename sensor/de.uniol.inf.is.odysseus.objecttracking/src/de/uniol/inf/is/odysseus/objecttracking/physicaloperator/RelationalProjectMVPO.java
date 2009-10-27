package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.monitoring.StaticValueMonitoringData;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * @author Andre Bolles
 */
public class RelationalProjectMVPO<T extends IProbability> extends
		AbstractPipe<MVRelationalTuple<T>, MVRelationalTuple<T>> {

	
	int[] restrictList;
	RealMatrix projectMatrix;
	RealMatrix projectVector;
	SDFAttributeList inputSchema;
	
	public RelationalProjectMVPO(int[] restrictList, RealMatrix projectMatrix, RealMatrix projectVector, SDFAttributeList inputSchema) {
		this.restrictList = restrictList;
		this.projectMatrix = projectMatrix;
		this.projectVector = projectVector;
		this.inputSchema = inputSchema;
		
		final RelationalProjectMVPO<T> t = this;
		this.addMonitoringData("selectivity", new StaticValueMonitoringData<Double>(t,
				"selectivity", 1d));
	}

	public RelationalProjectMVPO(RelationalProjectMVPO<T> copy) {
		super();
		int length = copy.restrictList.length;
		restrictList = new int[length];
		System.arraycopy(copy.restrictList, 0, restrictList, 0, length);
		
		this.projectMatrix = copy.projectMatrix;
		this.projectVector = copy.projectVector;
		this.inputSchema = copy.inputSchema;
		
		final RelationalProjectMVPO<T> t = this;
		this.addMonitoringData("selectivity", new StaticValueMonitoringData<Double>(t,
				"selectivity", 1d));
	}

	@Override
	protected void process_next(MVRelationalTuple<T> object, int port) {
		
		// first project the metadata:
		
		IProbability cov = object.getMetadata();
		if(cov.getCovariance() != null){
			RealMatrixImpl c = new RealMatrixImpl(cov.getCovariance());
			if(this.projectMatrix != null){
				double[][] covProjected = this.projectMatrix.multiply(c).multiply(this.projectMatrix.transpose()).getData();
				cov.setCovariance(covProjected);
			}
		}
		
		// restrict the original tuple and set the new metadata
		object.findMeasurementValuePositions(this.inputSchema);
		MVRelationalTuple objectNew = object.restrict(this.restrictList, this.projectMatrix, this.projectVector, null, this.inputSchema);
		objectNew.setMetadata(cov);
		
		// transfer the new tuple
		transfer(objectNew);
	}

	@Override
	public RelationalProjectMVPO<T> clone() {
		return new RelationalProjectMVPO<T>(this);
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
				if(SDFDatatypes.isMeasurementValue(attr.getDatatype())){
					inAttrCount++;	
				}
			}
			
			
			// then find out how many measurement attributes will
			// be in the output schema;
			int outAttrCount = 0;
			for(int i =0; i<this.restrictList.length; i++){
				if(SDFDatatypes.isMeasurementValue(this.inputSchema.get(this.restrictList[i]).getDatatype())){
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
				if(SDFDatatypes.isMeasurementValue(this.inputSchema.get(this.restrictList[i]).getDatatype())){
					// It the measurement attribute is at position
					// x in the input schema I have to find out
					// how many measurement attributes are at
					// positions 0 ... x - 1
					int measurementInputPos = 0;
					for(int v = 0; v<this.restrictList[i]; v++){
						if(SDFDatatypes.isMeasurementValue(this.inputSchema.get(v).getDatatype())){
							measurementInputPos++;
						}
					}
					matrix[count++][measurementInputPos] = 1.0;
				}
			}
			projectMatrix = new RealMatrixImpl(matrix);
			
		}
	}
	
	public OutputMode getOutputMode(){
		return OutputMode.MODIFIED_INPUT;
	}
}
