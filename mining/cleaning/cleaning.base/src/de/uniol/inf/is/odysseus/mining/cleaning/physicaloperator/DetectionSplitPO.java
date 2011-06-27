/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.cleaning.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.mining.cleaning.model.IDetection;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;

public class DetectionSplitPO<T> extends AbstractPipe<T, T> {

	private List<IDetection<T>> detections;

	public DetectionSplitPO(List<IDetection<T>> detections) {
		this.detections = detections;
	}

	public DetectionSplitPO(DetectionSplitPO<T> detectionSplitPO) {
		this.detections = detectionSplitPO.detections;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp, port);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void process_open() throws OpenFailedException {
		super.process_open();
		for(IDetection<T> d : this.detections){
			d.init();
		}
	}

	@Override
	protected void process_next(T object, int port) {
		for (IDetection<T> d : this.detections) {			
			if (!d.getPredicate().evaluate(object)) {
				// value is ok -> transfer normally
				transfer(object, 0);
			} else {
				// value is not ok -> mark for correction and transfer to other
				// output
				object = markAsFailure(object);
				transfer(object, 1);
			}
		}
	}

	private T markAsFailure(T object) {
		System.out.println("FEHLER gefunden: "+object);
		return object;
	}

	@Override
	public DetectionSplitPO<T> clone() {
		return new DetectionSplitPO<T>(this);
	}

}
