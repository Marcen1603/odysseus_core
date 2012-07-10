/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.scars.metadata;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.helper.CovarianceMapper;
import de.uniol.inf.is.odysseus.scars.util.helper.TypeCaster;

public class LinearPredictionFunction<M extends IProbability> implements IPredictionFunction<M>{

	private SDFSchema scanSchema;
	private SDFSchema timeSchema;
	private CovarianceMapper mapper;
	
	private PredictionExpression[] expressions;
	private double[][] noiseMatrix;
	
	@Override
	public void init(SDFSchema scanSchema, SDFSchema timeSchema) {
		this.scanSchema = scanSchema;
		this.timeSchema = timeSchema;
		mapper = new CovarianceMapper(scanSchema);
		for(PredictionExpression exp : expressions) {
			exp.initAttributePaths(scanSchema);
			exp.initAttributePaths(timeSchema);
		}
	}

	@Override
	public void predictData(MVTuple<M> scanRootTuple, MVTuple<M> timeTuple, int currentIndex) {
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
	public void predictMetadata(M metadata, MVTuple<M> scanRootTuple, MVTuple<M> timeTuple, int currentIndex) {
		
		double[][] sigma = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];
		for(int row=0; row<sigma.length; row++) {
			for(int col=0; col<sigma[row].length; col++) {
				sigma[row][col] = metadata.getCovariance()[row][col];
			}
		}
//		printMatrix("START", sigma);
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
//			System.out.println("H * Sigma...matrix row index: " + covRow);
//			System.out.println("H * Sigma...target attr name: " + expressions[index].getTargetAttributeName());
			
			for(int col=0; col<tmpCov.length; col++) {
				for(String attrName : expressions[index].getAttributeNames(scanSchema)) {
					int covAttrIndex = mapper.getCovarianceIndex(attrName);
					if(covAttrIndex != -1) {
						expressions[index].bindVariable(attrName, sigma[covAttrIndex][col]);
//						System.out.println("H * Sigma...bind:[" + covAttrIndex + "] [" + col + "] " + attrName + "= " + sigma[covAttrIndex][col]);
					} else {
						// not found in covmatrix so it usually reflects a time value
						// TODO use paths in tuples (for time etc.)
						int[] attrPath = expressions[index].getAttributePath(attrName);
						expressions[index].bindVariable(attrName, resolveValue(attrPath, scanRootTuple));
//						System.out.println("H * Sigma...bind: " + attrName + "= " + resolveValue(attrPath, scanRootTuple));
					}
				}
				
				for(String attrName : expressions[index].getAttributeNames(timeSchema)) {
					int[] attrPath = expressions[index].getAttributePath(attrName);
					expressions[index].bindVariable(attrName, resolveValue(attrPath, timeTuple));
//					System.out.println("H * Sigma...bind: " + attrName + "= " + resolveValue(attrPath, timeTuple));
				}
				
				expressions[index].evaluate();
				tmpCov[covRow][col] = expressions[index].getTargetDoubleValue();
//				System.out.println("H * Sigma...result: " + "[" + covRow + "][" + col + "] = " +  tmpCov[covRow][col]);
			}
		}
//		printMatrix("H * Sigma...", tmpCov);
		
		// ... * H^T
		double[][] tmpCov2 = new double[metadata.getCovariance().length][metadata.getCovariance()[0].length];
		for(int row=0; row<sigma.length; row++) {
			for(int col=0; col<sigma[row].length; col++) {
				tmpCov2[row][col] = tmpCov[row][col];
			}
		}
		for(int index=0; index<expressions.length; index++) {
			int covCol = mapper.getCovarianceIndex(expressions[index].getTargetAttributeName());
//			System.out.println("... * H^T...matrix col index: " + covCol);
//			System.out.println("... * H^T...target attr name: " + expressions[index].getTargetAttributeName());
			
			for(int row=0; row<tmpCov[covCol].length; row++) {
				for(String attrName : expressions[index].getAttributeNames(scanSchema)) {
					
					int covAttrIndex = mapper.getCovarianceIndex(attrName);
					if(covAttrIndex != -1) {
						expressions[index].bindVariable(attrName, tmpCov[row][covAttrIndex]);
//						System.out.println("... * H^T...bind:[" + row + "] [" + covAttrIndex + "] " + attrName + "= " + sigma[row][covAttrIndex]);
					} else {
						// not found in covmatrix so it usually reflects a time value
						// TODO use paths in tuples (for time etc.)
						int[] attrPath = expressions[index].getAttributePath(attrName);
						expressions[index].bindVariable(attrName, resolveValue(attrPath, scanRootTuple));
//						System.out.println("... * H^T...bind: " + attrName + "= " + resolveValue(attrPath, scanRootTuple));
					}
				}
				for(String attrName : expressions[index].getAttributeNames(timeSchema)) {
					int[] attrPath = expressions[index].getAttributePath(attrName);
					expressions[index].bindVariable(attrName, resolveValue(attrPath, timeTuple));
//					System.out.println("... * H^T...bind: " + attrName + "= " + resolveValue(attrPath, timeTuple));
				}
				expressions[index].evaluate();
				tmpCov2[row][covCol] = expressions[index].getTargetDoubleValue();
				if(tmpCov2[row][covCol] < 0) {
					tmpCov2[row][covCol] = tmpCov2[row][covCol] * -1; 
				}
//				System.out.println("... * H^T...result: " + "[" + row + "][" + covCol + "] = " +  tmpCov2[row][covCol]);
			}
		}
		
//		printMatrix("... * H^T...", tmpCov2);
		
		// ... + Q
		if(this.noiseMatrix != null){
			RealMatrix cov = new RealMatrixImpl(tmpCov2);
			RealMatrix q = new RealMatrixImpl(this.noiseMatrix);
			tmpCov2 = cov.add(q).getData();
		}
		metadata.setCovariance(tmpCov2);
	}
	
	protected Object resolveValue(int[] path, MVTuple<M> root) {
		Object currentTuple = root;
		for(int depth=0; depth<path.length; depth++) {
			if(currentTuple instanceof MVTuple<?>) {
				currentTuple = ((MVTuple<?>) currentTuple).getAttribute(path[depth]);
			} else if(currentTuple instanceof List) {
				currentTuple = ((List<?>)currentTuple).get(path[depth]);
			}
		}
		return currentTuple;
	}
	
	protected void setValue(int[] path, MVTuple<M> root, Object value) {
		Object currentAttr = root;
		int depth = path.length-1;
		for(int i=0; i<depth; i++) {
			if(currentAttr instanceof MVTuple) {
				currentAttr = ((MVTuple<?>)currentAttr).getAttribute(path[i]);
			} else if(currentAttr instanceof List) {
				currentAttr = ((List<?>)currentAttr).get(path[i]);
			}
		}
		
		if(currentAttr instanceof MVTuple) {
			Object oldValue = ((MVTuple<?>)currentAttr).getAttribute(path[path.length - 1]);
			Object newValue = TypeCaster.cast(value, oldValue);
			((MVTuple<?>)currentAttr).setAttribute(path[depth], newValue);
		}
	}
	
	@Override
	public void setExpressions(PredictionExpression[] expressions) {
		this.expressions = expressions;
	}
	
	@Override
	public void setNoiseMatrix(double[][] noiseMatrix) {
		this.noiseMatrix = noiseMatrix;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "LinearPredictionFunction [scanSchema="
				+ scanSchema
				+ ", timeSchema="
				+ timeSchema
				+ ", mapper="
				+ mapper
				+ ", expressions="
				+ (expressions != null ? Arrays.asList(expressions).subList(0,
						Math.min(expressions.length, maxLen)) : null)
				+ ", noiseMatrix="
				+ (noiseMatrix != null ? Arrays.asList(noiseMatrix).subList(0,
						Math.min(noiseMatrix.length, maxLen)) : null) + "]";
	}

	@Override
	public PredictionExpression[] getExpressions() {
		return expressions;
	}
}
