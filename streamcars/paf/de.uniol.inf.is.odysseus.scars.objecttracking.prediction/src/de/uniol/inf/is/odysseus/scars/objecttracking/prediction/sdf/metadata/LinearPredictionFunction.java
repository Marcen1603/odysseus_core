package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class LinearPredictionFunction<M extends IProbability> implements IPredictionFunction<M>{

	private SDFExpression[] expressions;
	private double[][] noiseMatrix;
	
	@Override
	public MVRelationalTuple<M> predictData(MVRelationalTuple<M> object, PointInTime currentTime, PointInTime timeToPredict) {
		if(currentTime.before(timeToPredict)) {
			throw new IllegalArgumentException("the time to predict from, is newer then the current time");
		}
		
		if(expressions == null) {
			return object.clone();
		}
		
		Object[] objAttributes = getAttributesToPredict(object);
		Object[] predictedAttributes = new Object[objAttributes.length];
		long deltaTime = currentTime.getMainPoint() - timeToPredict.getMainPoint();
		
		for(int attrIndex=0; attrIndex<objAttributes.length; attrIndex++) {
			
			if(expressions[attrIndex] != null) {
				int[] expressionAttrIndices = expressions[attrIndex].getAttributePositions();
				Object[] expressionVariables = getExpressionVariables(expressionAttrIndices, objAttributes, deltaTime);
				this.expressions[attrIndex].bindVariables(expressionVariables);
				predictedAttributes[attrIndex] = expressions[attrIndex].getValue();
			} else {
				predictedAttributes[attrIndex] = objAttributes[attrIndex];
			}
		}
		
		MVRelationalTuple<M> predictedObject = new MVRelationalTuple<M>(object);
		predictedObject.setAttributes(predictedAttributes);
		
		return predictedObject;
	}
	
	private Object[] getAttributesToPredict(MVRelationalTuple<M> object) {
		Object[] values = new Object[object.getAttributeCount()];
		for(int i = 0; i<object.getAttributeCount(); i++){
			values[i] = object.getAttribute(i);
		}
		return values;
	}
	
	private Object[] getExpressionVariables(int[] expressionAttrIndices, Object[] attributesToPredict, long deltaTime) {
		Object[] expressionVariables = new Object[expressionAttrIndices.length];
		for(int i=0; i<expressionAttrIndices.length; i++) {
			if(expressionAttrIndices[i] != -1) {
				expressionVariables[i] = attributesToPredict[expressionAttrIndices[i]];
			} else {
				expressionVariables[i] = deltaTime;
			}
		}
		return expressionVariables;
	}

	@SuppressWarnings("unchecked")
	@Override
	public M predictMetadata(M metadata, PointInTime currentTime, PointInTime timeToPredict) {
		if(currentTime.before(timeToPredict)) {
			throw new IllegalArgumentException("the time to predict from, is newer then the current time");
		}
		if(expressions == null || metadata.getCovariance() == null) {
			return (M) metadata.clone();
		}
		
		double[][] sigma = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];
		long deltaTime = currentTime.getMainPoint() - timeToPredict.getMainPoint();
		int[] mvAttrIndices = metadata.getMVAttributeIndices();
		
		for(int row=0; row<sigma.length; row++) {
			for(int col=0; col<sigma[row].length; col++) {
				sigma[row][col] = metadata.getCovariance()[row][col];
			}
		}
		
		// H * Sigma...
		double[][] tmpCov = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];
		
		for(int row=0; row<tmpCov.length; row++) {
			for(int col=0; col<tmpCov[0].length; col++) {
				
				if(expressions[mvAttrIndices[row]] != null) {
					int[] exprAttrIndices = expressions[mvAttrIndices[row]].getAttributePositions();
					Object[] exprAttrVals = new Object[exprAttrIndices.length];
					
					for(int i=0; i<exprAttrIndices.length; i++) {
						if(exprAttrIndices[i] != -1) {
							int matrixIndex = convertAttrIndexToMatrixIndex(exprAttrIndices[i], exprAttrIndices);
							exprAttrVals[i] = sigma[row][matrixIndex];
						} else {
							exprAttrVals[i] = deltaTime;
						}
					}
					
					this.expressions[mvAttrIndices[row]].bindVariables(exprAttrVals);
					tmpCov[row][col] = expressions[mvAttrIndices[row]].getValue();
				} else {
					tmpCov[row][col] = sigma[row][col];
				}
			}
		}
		
		// ... * H^T
		double[][] tmpCov2 = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];
		for(int row=0; row<tmpCov.length; row++) {
			for(int col=0; col<tmpCov[0].length; col++) {
				
				if(expressions[mvAttrIndices[col]] != null) {
					int[] exprAttrIndices = expressions[mvAttrIndices[row]].getAttributePositions();
					Object[] exprAttrVals = new Object[exprAttrIndices.length];
					
					for(int i=0; i<exprAttrIndices.length; i++) {
						if(exprAttrIndices[i] != -1) {
							int matrixIndex = convertAttrIndexToMatrixIndex(exprAttrIndices[i], exprAttrIndices);
							exprAttrVals[i] = sigma[matrixIndex][col];
						} else {
							exprAttrVals[i] = deltaTime;
						}
					}
					this.expressions[mvAttrIndices[col]].bindVariables(exprAttrVals);
					tmpCov2[row][col] = this.expressions[mvAttrIndices[col]].getValue();
				}
			}
		}
		
		// ... + Q
		if(this.noiseMatrix != null){
			RealMatrix cov = new RealMatrixImpl(tmpCov2);
			RealMatrix q = new RealMatrixImpl(this.noiseMatrix);
			tmpCov2 = cov.add(q).getData();
		}
		
		M newMetadata = null;
		newMetadata = (M)metadata.clone();
		newMetadata.setCovariance(tmpCov2);
		
		return newMetadata;
	}
	
	private int convertAttrIndexToMatrixIndex(int attrIndex, int[] attributeIndices) {
		for(int matrixIndex=0; matrixIndex<attributeIndices.length; matrixIndex++) {
			if(attrIndex == attributeIndices[matrixIndex]) {
				return matrixIndex;
			}
		}
		return -1;
	}

	@Override
	public void setExpressions(SDFExpression[] expressions) {
		this.expressions = expressions;
	}

	@Override
	public void setNoiseMatrix(double[][] noiseMatrix) {
		this.noiseMatrix = noiseMatrix;
	}
	


}
