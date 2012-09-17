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
package de.uniol.inf.is.odysseus.objecttracking.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.latency.Latency;

@SuppressWarnings({"rawtypes","unchecked"})
/**
 * This is a metadata item, that implements TimeInterval, IProbability and IPredictionFunction.
 * It works only for relational tuples.
 * @deprecated Do not use this class any longer, since not the whole prediction function has to
 * be carried by a tuple but only a key, that can be used to identify the correct prediction
 * functions in the operators.
 */
@Deprecated
public class IntervalProbabilityLatencyPrediction<T extends MetaAttributeContainer<M>, M extends IProbability>
		extends TimeInterval implements IProbabilityPredictionFunction<T, M>,
		IProbability, ILatency {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8162178186554923626L;
	/**
	 * Since we use probabilities in this metadata, the prediction function
	 * should also consider the probabilities.
	 * 
	 */
	private IProbabilityPredictionFunction<T, M> predFct;

	public IPredictionFunction<T, M> getPredFct() {
		return predFct;
	}

	public void setPredFct(IProbabilityPredictionFunction<T, M> predFct) {
		this.predFct = predFct;
	}

	public IProbability getProb() {
		return prob;
	}

	public void setProb(IProbability prob) {
		this.prob = prob;
	}

	private IProbability prob;
	private ILatency latency;

	public IntervalProbabilityLatencyPrediction() {
		this.prob = new Probability();
		this.latency = new Latency();
		// TODO pr�fen, ob diese eine Klasse ausreicht
		// wenn man verschiedene Klassen f�r PredictionFunctions braucht, dann
		// muss man irgendwie einen Unterscheidungsmechanismus
		// einbauen, der entscheidet, wann welches Objekt
		// erzeugt wird
		this.predFct = new LinearProbabilityPredictionFunction();
	}

	public IntervalProbabilityLatencyPrediction(
			IntervalProbabilityLatencyPrediction copy) {
		super(copy);
		this.prob = (IProbability) copy.prob.clone();
		this.latency = copy.latency.clone();
		this.predFct = (IProbabilityPredictionFunction) copy.predFct.clone();
	}

	public IntervalProbabilityLatencyPrediction(
			IProbabilityPredictionFunction<T, M> predFct, IProbability prob,
			ILatency latency) {
		this.predFct = predFct;
		this.prob = prob;
		this.latency = latency;
	}

	@Override
	public T predictData(SDFSchema schema, T object, PointInTime t) {
		// if there is no prediction function, return
		// return the original schema
		if (this.predFct == null) {
			return (T) object.clone();
		}

		return this.predFct.predictData(schema, object, t);
	}

	@Override
	public M predictMetadata(SDFSchema schema, T object, PointInTime t) {
		// if there is no prediction function,
		// return the original metadata
		if (this.predFct == null) {
			return (M) object.getMetadata().clone();
		}

		return this.predFct.predictMetadata(schema, object, t);
	}

	@Override
	public T predictAll(SDFSchema schema, T object, PointInTime t) {
		return this.predFct.predictAll(schema, object, t);
	}

	@Override
	public double[][] getCovariance() {
		return this.prob.getCovariance();
	}

	@Override
	public void setCovariance(double[][] sigma) {
		this.prob.setCovariance(sigma);
	}

	@Override
	public long getLatency() {
		// TODO Auto-generated method stub
		return this.latency.getLatency();
	}

	@Override
	public long getLatencyEnd() {
		// TODO Auto-generated method stub
		return this.latency.getLatencyEnd();
	}

	@Override
	public long getLatencyStart() {
		// TODO Auto-generated method stub
		return this.latency.getLatencyStart();
	}

	@Override
	public void setLatencyEnd(long timestamp) {
		this.latency.setLatencyEnd(timestamp);
	}

	@Override
	public void setLatencyStart(long timestamp) {
		this.latency.setLatencyStart(timestamp);

	}

	@Override
	public SDFExpression[] getExpressions() {
		// TODO Auto-generated method stub
		if (this.predFct == null) {
			return null;
		}
		return this.predFct.getExpressions();
	}

	@Override
	public int[][] getVariables() {
		if (this.predFct != null) {
			return this.predFct.getVariables();
		}
		return null;
	}

	@Override
	public void setVariables(int[][] vars) {
		if (this.predFct != null) {
			this.predFct.setVariables(vars);
		}
	}

	@Override
	public void setExpressions(SDFExpression[] expressions) {
		this.predFct.setExpressions(expressions);

	}

	@Override
	public void setTimeInterval(ITimeInterval timeInterval) {
		this.predFct.setTimeInterval(timeInterval);

	}

	@Override
	public IntervalProbabilityLatencyPrediction clone() {
		return new IntervalProbabilityLatencyPrediction(this);
	}

	@Override
	public void initVariables() {
		if (this.predFct != null) {
			this.predFct.initVariables();
		}

	}

	@Override
	public void setNoiseMatrix(double[][] noiseMatrix) {
		if (this.predFct != null) {
			this.predFct.setNoiseMatrix(noiseMatrix);
		}

	}

	@Override
	public void setRestrictList(int[] restrictList) {
		if (this.predFct != null) {
			this.predFct.setRestrictList(restrictList);
		}
	}

	@Override
	public int[] getMVAttributeIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMVAttributeIndices(int[] indices) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<int[]> getAttributePaths() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttributePaths(ArrayList<int[]> paths) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getIndexOfKovMatrix(int[] path) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAttributeMapping(List<String> indices) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCovarianceIndex(String fullAttributeName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAttributeName(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAttributMapping() {
		// TODO Auto-generated method stub
		return null;
	}

}
