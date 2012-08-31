/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.fusion.physicaloperator.prediction;

import java.util.Map.Entry;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.fusion.metadata.IFusionProbability;
import de.uniol.inf.is.odysseus.fusion.store.context.FusionContextStore;
import de.uniol.inf.is.odysseus.fusion.util.matrix.Matrix;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class SpatialKalmanPredictionPO extends AbstractPipe<Tuple<? extends IFusionProbability>, Tuple<? extends IFusionProbability>> {

	public SpatialKalmanPredictionPO(SDFSchema outputSchema) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(Tuple<? extends IFusionProbability> tuple, int port) {
		PointInTime time = ((ITimeInterval) tuple.getMetadata()).getStart();
		Tuple<? extends IFusionProbability> storeTuple = new Tuple<IFusionProbability>(tuple.getAttributes(), false);

		if (!FusionContextStore.getStoreMap().isEmpty()) {
			// Predict all tuple in the context store
			for (Entry<Integer, Tuple<? extends IFusionProbability>> storeEntry : FusionContextStore.getStoreMap().entrySet()) {
				storeTuple = (Tuple<? extends IFusionProbability>) storeEntry.getValue();

				// compute prediction cycles
				PointInTime storeTime = storeTuple.getMetadata().getStart();
				long predictionCycles = time.minus(storeTime).getMainPoint();

				Coordinate cordinate = predict(storeTuple.getMetadata(), (int) predictionCycles);

				Point centroid = storeTuple.getAttribute(2);
				centroid = GeometryFactory.createPointFromInternalCoord(cordinate, centroid);
				storeTuple.setAttribute(2, centroid);
				transfer(storeTuple);
			}
		}
		transfer(tuple);
		process_done();
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractPipe<Tuple<? extends IFusionProbability>, Tuple<? extends IFusionProbability>> clone() {
		return this.clone();
	}

	private Coordinate predict(IFusionProbability metadata, int cycle) {
		// Predicte the stored tuple for the millisecond in cycle
		for (int i = -1; i < cycle; i++) {
			// (1) Project the state ahead
			// update the state: x'(k) = A*x(k)
			metadata.setState_pre(metadata.getTransition_matrix().times(metadata.getState_post()));
			if (metadata.getControl_matrix() != null && metadata.getCp() > 0) {
				// x'(k) = x'(k) + B*u(k)
				metadata.setState_pre(metadata.getControl_matrix().gemm(metadata.getControl_matrix(), metadata.getState_pre(), 1, 1));
			}
			// (2) Project the error covariance ahead
			// update error covariance matrices: temp1 = A*P(k)
			Matrix error_cov = updateCovariance(metadata);
			metadata.setError_cov_pre(error_cov);
		}
		Coordinate point = new Coordinate(metadata.getState_pre().get(0, 0), metadata.getState_pre().get(1, 0));
		return point;
	}

	private Matrix updateCovariance(IFusionProbability metadata) {
		Matrix temp = metadata.getTransition_matrix().times(metadata.getError_cov_post());
		// P'(k) = temp1*At + Q
		return temp.gemm(metadata.getTransition_matrix().transpose(), metadata.getProcess_noise_cov(), 1, 1);
	}

}
