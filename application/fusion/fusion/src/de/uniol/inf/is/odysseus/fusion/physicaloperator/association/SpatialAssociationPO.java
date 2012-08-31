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
package de.uniol.inf.is.odysseus.fusion.physicaloperator.association;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.fusion.metadata.IFusionProbability;
import de.uniol.inf.is.odysseus.fusion.util.Mahalanobis;

public class SpatialAssociationPO extends AbstractPipe<Tuple<? extends IFusionProbability>, Tuple<? extends IFusionProbability>> {

	private static Tuple<? extends IFusionProbability> current = null;
	private static Tuple<? extends IFusionProbability> min = null;
	private static double minDist = Double.NaN;

	public SpatialAssociationPO(SDFSchema outputSchema) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(Tuple<? extends IFusionProbability> tuple, int port) {
		// hold the new measured tuple
		if (port == 0) {
			current = tuple;
		}
		if (port == 1) {
			if (current != null) {
				Point pointA = (Point) current.getAttribute(2);
				Point pointB = (Point) tuple.getAttribute(2);

				double[] dA = { pointA.getX(), pointA.getY(), 0, 0 };
				double[] dB = { pointB.getX(), pointB.getY(), 0, 0 };

				//Only the predicted tuple has the variance
				double dist = Mahalanobis.mahalanobis_simple(dA, dB, tuple.getMetadata().getTransition_matrix().getArray());
				if (dist < minDist) {
					minDist = dist;
					min = tuple;
					min.setAttributes(current.getAttributes());
				}

			}
			
			// Last tuple of the prediction is the new measurement(reference)
			// Dirty, but works because the stream is sorted.
			if (current.equals(tuple)) {
				if (min != null){
					transfer(min);
				}
				min = null;
				current = null;
			}
		}
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

}
