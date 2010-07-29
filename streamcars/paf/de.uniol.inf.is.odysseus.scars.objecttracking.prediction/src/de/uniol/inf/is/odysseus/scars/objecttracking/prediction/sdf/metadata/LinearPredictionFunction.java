package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.PredictionExpression;
import de.uniol.inf.is.odysseus.scars.util.CovarianceMapper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class LinearPredictionFunction<M extends IProbability> implements IPredictionFunction<M>{

	private SDFAttributeList scanSchema;
	private SDFAttributeList timeSchema;
	private CovarianceMapper mapper;
	
	private PredictionExpression[] expressions;
	private double[][] noiseMatrix;
	
	
	@Override
	public void init(SDFAttributeList scanSchema, SDFAttributeList timeSchema) {
		this.scanSchema = scanSchema;
		this.timeSchema = timeSchema;
		mapper = new CovarianceMapper(scanSchema);
		for(PredictionExpression exp : expressions) {
			exp.initAttributePaths(scanSchema);
			exp.initAttributePaths(timeSchema);
		}
	}

	@Override
	public void predictData(MVRelationalTuple<M> scanRootTuple, MVRelationalTuple<M> timeTuple, int currentIndex) {
		for(int index=0; index<expressions.length; index++) {
			expressions[index].replaceVaryingAttributeIndex(scanSchema, currentIndex);
			expressions[index].replaceVaryingAttributeIndex(timeSchema, currentIndex);
			
			for(String attrName : expressions[index].getAttributeNames(scanSchema)) {
				int[] attrPath = expressions[index].getAttributePath(attrName);
				expressions[index].bindVariable(attrName, resolveValue(attrPath, scanRootTuple));
			}
			
			for(String attrName : expressions[index].getAttributeNames(timeSchema)) {
				int[] attrPath = expressions[index].getAttributePath(attrName);
				expressions[index].bindVariable(attrName, resolveValue(attrPath, timeTuple));
			}
			expressions[index].evaluate();
			int[] targetPath = expressions[index].getTargetPath();
			Object targetValue = expressions[index].getTargetValue();
			setValue(targetPath, scanRootTuple, targetValue);
		}
	}
	
	@Override
	public void predictMetadata(M metadata, MVRelationalTuple<M> scanRootTuple, MVRelationalTuple<M> timeTuple, int currentIndex) {
		System.out.println("predict Metadata");
		
		double[][] sigma = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];
		for(int row=0; row<sigma.length; row++) {
			for(int col=0; col<sigma[row].length; col++) {
				sigma[row][col] = metadata.getCovariance()[row][col];
			}
		}
		
		// H * Sigma...
		double[][] tmpCov = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];
		for(int row=0; row<sigma.length; row++) {
			for(int col=0; col<sigma[row].length; col++) {
				tmpCov[row][col] = sigma[row][col];
			}
		}
		
		for(int index=0; index<expressions.length; index++) {
			expressions[index].replaceVaryingAttributeIndex(scanSchema, index);
			expressions[index].replaceVaryingAttributeIndex(timeSchema, index);
			int covRow = mapper.getCovarianceIndex(expressions[index].getTargetAttributeName());
			
			for(int col=0; col<tmpCov.length; col++) {
				for(String attrName : expressions[index].getAttributeNames(scanSchema)) {
					int covAttrIndex = mapper.getCovarianceIndex(attrName);
					if(covAttrIndex != -1) {
						expressions[index].bindVariable(attrName, sigma[covAttrIndex][col]);
					} else {
						// not found in covmatrix so it usually reflects a time value
						// TODO use paths in tuples (for time etc.)
						int[] attrPath = expressions[index].getAttributePath(attrName);
						expressions[index].bindVariable(attrName, resolveValue(attrPath, scanRootTuple));
					}
				}
				
				for(String attrName : expressions[index].getAttributeNames(timeSchema)) {
					int[] attrPath = expressions[index].getAttributePath(attrName);
					expressions[index].bindVariable(attrName, resolveValue(attrPath, timeTuple));
				}
				expressions[index].evaluate();
				tmpCov[covRow][col] = expressions[index].getTargetDoubleValue();
			}
		}
		
		// ... * H^T
		double[][] tmpCov2 = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];
		for(int row=0; row<sigma.length; row++) {
			for(int col=0; col<sigma[row].length; col++) {
				tmpCov2[row][col] = tmpCov[row][col];
			}
		}
		for(int index=0; index<expressions.length; index++) {
			int covCol = metadata.getIndexOfKovMatrix(expressions[index].getTargetPath());
			
			for(int row=0; row<tmpCov[covCol].length; row++) {
				for(String attrName : expressions[index].getAttributeNames(scanSchema)) {
					int[] attrPath = expressions[index].getAttributePath(attrName);
					int covAttrIndex = metadata.getIndexOfKovMatrix(attrPath);
					if(covAttrIndex != -1) {
						expressions[index].bindVariable(attrName, tmpCov[row][covAttrIndex]);
					} else {
						// not found in covmatrix so it usually reflects a time value
						// TODO use paths in tuples (for time etc.)
						expressions[index].bindVariable(attrName, resolveValue(attrPath, scanRootTuple));
					}
				}
				for(String attrName : expressions[index].getAttributeNames(timeSchema)) {
					int[] attrPath = expressions[index].getAttributePath(attrName);
					expressions[index].bindVariable(attrName, resolveValue(attrPath, timeTuple));
				}
				expressions[index].evaluate();
				tmpCov[row][covCol] = expressions[index].getTargetDoubleValue();
			}
		}
		
		// ... + Q
		if(this.noiseMatrix != null){
			RealMatrix cov = new RealMatrixImpl(tmpCov2);
			RealMatrix q = new RealMatrixImpl(this.noiseMatrix);
			tmpCov2 = cov.add(q).getData();
		}
		metadata.setCovariance(tmpCov2);
	}
	
	protected Object resolveValue(int[] path, MVRelationalTuple<M> root) {
		Object currentTuple = root;
		for(int depth=0; depth<path.length; depth++) {
			if(currentTuple instanceof MVRelationalTuple<?>) {
				currentTuple = ((MVRelationalTuple<?>) currentTuple).getAttribute(path[depth]);
			}
		}
		return currentTuple;
	}
	
	protected void setValue(int[] path, MVRelationalTuple<M> root, Object value) {
		MVRelationalTuple<?> currentTuple = root;
		for(int depth=0; depth<path.length-1; depth++) {
			currentTuple = ((MVRelationalTuple<?>) currentTuple).<MVRelationalTuple<?>>getAttribute(path[depth]);
		}
		currentTuple.setAttribute(path[path.length-1], value);
	}
	
	@Override
	public void setExpressions(PredictionExpression[] expressions) {
		this.expressions = expressions;
	}
	
	@Override
	public void setNoiseMatrix(double[][] noiseMatrix) {
		this.noiseMatrix = noiseMatrix;
	}






}
