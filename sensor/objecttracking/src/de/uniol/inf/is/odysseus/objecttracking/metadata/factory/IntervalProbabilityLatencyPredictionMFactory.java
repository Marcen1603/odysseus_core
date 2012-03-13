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
package de.uniol.inf.is.odysseus.objecttracking.metadata.factory;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IntervalProbabilityLatencyPrediction;
@SuppressWarnings({"rawtypes"})
/**
 * This class generates a new IntervalProbabilityLatencyPrediction object.
 * It fills latencyStart and the covariance. The start timestamp must be
 * set by another MFactory object. The prediction function will
 * be set by the SetPredictionOperator.
 * 
 * @author Andre Bolles
 * @deprecated Sollte eigentlich nirgends mehr verwendet werden. Es muss ja
 *             nun eine Metadatum erzeugt werden, dass ILatency, IProbability,
 *             ITimeInterval, IApplicationTime und IPredictionFunctionKey
 *             erfüllt
 *
 */
@Deprecated
public class IntervalProbabilityLatencyPredictionMFactory extends AbstractMetadataUpdater<IntervalProbabilityLatencyPrediction, MVRelationalTuple<IntervalProbabilityLatencyPrediction>>{

	SDFSchema schema;
	
	public IntervalProbabilityLatencyPredictionMFactory(SDFSchema schema){
		this.schema = schema;
	}
	
	@Override
	public void updateMetadata(MVRelationalTuple<IntervalProbabilityLatencyPrediction> inElem) {
		inElem.getMetadata().setLatencyStart(System.nanoTime());
		
		double[][] cov = null;
		int counter = 0;
		for(SDFAttribute attr: this.schema){
			if(attr.getDatatype().isMeasurementValue()){
				List covariance = ((SDFAttribute)attr).getAddInfo();
				if(cov == null){
					cov = new double[covariance.size()][covariance.size()];
				}
				
				for(int i = 0; i<covariance.size(); i++){
					cov[counter][i] = (Double)covariance.get(i);
				}
				counter++;
			}
		}
		
		inElem.getMetadata().setCovariance(cov);
	}

}
