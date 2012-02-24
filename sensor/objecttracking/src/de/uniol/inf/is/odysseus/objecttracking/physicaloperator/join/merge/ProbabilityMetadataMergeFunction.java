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
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.merge;

import de.uniol.inf.is.odysseus.core.server.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;


public class ProbabilityMetadataMergeFunction<M extends IProbability> implements IInlineMetadataMergeFunction<M>{
	
	/**
	 * The covariance between left and right.
	 */
	private double[][] covLeftRight;
	
	public ProbabilityMetadataMergeFunction(double[][] covLeftRight){
		this.covLeftRight = covLeftRight;
	}
	
	private ProbabilityMetadataMergeFunction(ProbabilityMetadataMergeFunction<M> original){
		if(original.covLeftRight != null){
			this.covLeftRight = new double[original.covLeftRight.length][original.covLeftRight[0].length];
			for(int i = 0; i<original.covLeftRight.length; i++){
				for(int u = 0; u<original.covLeftRight[i].length; u++){
					this.covLeftRight[i][u] = original.covLeftRight[i][u];
				}
			}
		}
	}
	
	@Override
	public ProbabilityMetadataMergeFunction<M> clone(){
		return new ProbabilityMetadataMergeFunction<M>(this);
	}

	@Override
	public void mergeInto(M res, M left, M right) {
		// Intervall und Latency werden in anderen Funktionen gemerged
		// TimeIntervalInlineMetadataMergeFunction und LatencyMergeFunction
//		M res = (M)left.clone();
//		res.setStart(PointInTime.max(left.getStart(), right.getStart()));
//		res.setEnd(PointInTime.min(left.getEnd(), right.getEnd()));
//		res.setLatencyStart(Math.min(left.getLatencyStart(), right.getLatencyStart()));
		
		// calculating the new covariance
		if(left.getCovariance() != null && right.getCovariance() != null){
			int leftLength = left.getCovariance().length;
			int rightLength = right.getCovariance().length;
			
			double[][] resCov = new double[leftLength + rightLength]
			                               [leftLength + rightLength];
			
			/* Generating
			 * (sigma1 covLeftRight)
			 * (covLeftRight' sigma2)
			 * 
			 */
			for(int i = 0; i<resCov.length; i++){
				for(int u = 0; u<resCov[i].length; u++){
					if(i<leftLength && u < leftLength){
						resCov[i][u] = left.getCovariance()[i][u];
					}
					else if(i>=leftLength && u >= leftLength){
						resCov[i][u] = right.getCovariance()[i-leftLength][u-leftLength];
					}
					else if(this.covLeftRight != null && i< leftLength && u >= leftLength){
						resCov[i][u] = this.covLeftRight[i-leftLength][u];
					}
					else if(this.covLeftRight != null && i >= leftLength && u < leftLength){
						// insert the transpose of the covariance
						resCov[i][u] = this.covLeftRight[u-leftLength][i];
					}
				}
			}
			
			res.setCovariance(resCov);
		}
		// if only the left has a covariance add
		// this one to the output element
		else if(left.getCovariance() != null && right.getCovariance() == null){
			res.setCovariance(left.getCovariance());
		}
		// the same for the right one
		else if(left.getCovariance() == null && right.getCovariance() != null){
			res.setCovariance(right.getCovariance());
		}
		
	}

}
