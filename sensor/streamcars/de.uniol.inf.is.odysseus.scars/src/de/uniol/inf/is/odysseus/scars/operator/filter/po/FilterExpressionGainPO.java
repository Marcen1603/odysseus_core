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
/**
 *
 */
package de.uniol.inf.is.odysseus.scars.operator.filter.po;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.metadata.CovarianceExpressionHelper;
import de.uniol.inf.is.odysseus.scars.metadata.CovarianceHelper;
import de.uniol.inf.is.odysseus.scars.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.metadata.IStreamCarsExpressionVariable;


/**
 * @author dtwumasi
 * @param <StreamCarsMetaData>
 *
 */
public class FilterExpressionGainPO<M extends IGainIProbabilityIObjectTrackingLatencyIConnectionContainer> extends AbstractFilterExpressionPO<M> {

	private static final String METADATA_COV = "COVARIANCE";
	
	private TupleIndexPath scannedTupleIndexPath;
	private TupleIndexPath predictedTupleIndexPath;
	
	private List<String> restrictedPredVariables;
	private List<String> restrictedScanVariables;
	
	private CovarianceHelper predCovHelper;
	
	private CovarianceExpressionHelper covHelper;
	
	public FilterExpressionGainPO() {
		super();
	}

	public FilterExpressionGainPO(FilterExpressionGainPO<M> copy) {
		super(copy);
		this.predCovHelper = new CovarianceHelper(copy.predCovHelper);
		this.setPredictedTupleIndexPath(copy.getPredictedTupleIndexPath().clone());
		this.setScannedTupleIndexPath(copy.getScannedTupleIndexPath().clone());
		this.covHelper = copy.covHelper;
		
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		predCovHelper = new CovarianceHelper(this.getRestrictedPredVariables().toArray(new String[0]), this.getOutputSchema());
		covHelper = new CovarianceExpressionHelper();
	}

	@Override
	public FilterExpressionGainPO<M> clone() {
		return new FilterExpressionGainPO<M>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	@Override
	public MVTuple<M> computeAll(MVTuple<M> object) {
		
		// latency
		object.getMetadata().setObjectTrackingLatencyStart("Filter Gain");
		
		scannedTupleIndexPath = TupleIndexPath.fromSchemaIndexPath(this.getScannedObjectListSIPath(), object);
		predictedTupleIndexPath = TupleIndexPath.fromSchemaIndexPath(this.getPredictedObjectListSIPath(), object);
		
		for (IConnection connected : object.getMetadata().getConnectionList()) {
			connected.getLeftPath().updateValues(object);
			connected.getRightPath().updateValues(object);
			compute(object, connected);
		}
		
		// latency
		object.getMetadata().setObjectTrackingLatencyEnd("Filter Gain");
		
		return object;
	}
	
	@SuppressWarnings("unchecked")
	protected void compute(MVTuple<M> root, IConnection con) {
		
		for(IStreamCarsExpressionVariable variable : expression.getVariables()) {
			
			if(variable.isSchemaVariable() && variable.getMetadataInfo().equals(METADATA_COV)) {
				if(variable.isInList(scannedTupleIndexPath)) {
					variable.replaceVaryingIndex(con.getLeftPath().getLastTupleIndex().toInt());
					MVTuple<M> car = (MVTuple<M>)TupleIndexPath.fromSchemaIndexPath(variable.getSchemaIndexPath(), root).getTupleObject();
//					double[][] cov = car.getMetadata().getCovariance();
					double[][] cov = covHelper.getCovarianceForRestictedAttributes(this.restrictedScanVariables, car.getMetadata());
//					cov = scanCovHelper.getCovarianceForRestrictedVariables(cov);
					variable.bind(cov);
//					variable.bind(scanCovHelper.getCovarianceForRestrictedVariables(cov));
				} else if(variable.isInList(predictedTupleIndexPath)) {
					variable.replaceVaryingIndex(con.getRightPath().getLastTupleIndex().toInt());
					MVTuple<M> car = (MVTuple<M>)TupleIndexPath.fromSchemaIndexPath(variable.getSchemaIndexPath(), root).getTupleObject();
//					double[][] cov = car.getMetadata().getCovariance();
//					cov = predCovHelper.getCovarianceForRestrictedVariables(cov);
//					variable.bind(predCovHelper.getCovarianceForRestrictedVariables(cov));
					double[][] cov = covHelper.getCovarianceForRestictedAttributes(this.restrictedPredVariables, car.getMetadata());
					variable.bind(cov);
				}
			}
		}
		
		expression.evaluate();
		double[][] gain = (double[][])expression.getValue();
		MVTuple<M> scannedCar = (MVTuple<M>)con.getLeftPath().getTupleObject();
		scannedCar.getMetadata().setRestrictedList(getRestrictedScanVariables().toArray(new String[0]));
		MVTuple<M> predictedCar = (MVTuple<M>)con.getRightPath().getTupleObject();
		predictedCar.getMetadata().setRestrictedGain(gain, getRestrictedPredVariables().toArray(new String[0]));

	}
	
	public CovarianceHelper getCovHelper() {
		return predCovHelper;
	}

	public void setCovHelper(CovarianceHelper covHelper) {
		this.predCovHelper = covHelper;
	}
	
	public void setScannedTupleIndexPath(TupleIndexPath scannedTupleIndexPath) {
		this.scannedTupleIndexPath = scannedTupleIndexPath;
	}

	public TupleIndexPath getPredictedTupleIndexPath() {
		return predictedTupleIndexPath;
	}

	public void setPredictedTupleIndexPath(TupleIndexPath predictedTupleIndexPath) {
		this.predictedTupleIndexPath = predictedTupleIndexPath;

	}

	public TupleIndexPath getScannedTupleIndexPath() {
		return scannedTupleIndexPath;
	}
	
	public void setRestrictedPredVariables(List<String> restrictedVariables) {
		this.restrictedPredVariables = restrictedVariables;
	}
	
	public void setRestrictedScanVariables(List<String> restrictedVarialbes) {
		this.restrictedScanVariables = restrictedVarialbes;
	}
	
	public List<String> getRestrictedPredVariables() {
		return restrictedPredVariables;
	} 
	
	public List<String> getRestrictedScanVariables() {
		return restrictedScanVariables;
	}

}
