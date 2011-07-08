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

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.IDetection;
import de.uniol.inf.is.odysseus.mining.metadata.IMiningMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;

/**
 * 
 * @author Dennis Geesen
 * Created at: 07.07.2011
 */
public abstract class AbstractDetectionPO<T extends IMetaAttributeContainer<? extends IMiningMetadata>, D extends IDetection<T>> extends AbstractPipe<T, T> {

	protected List<D> detections;

	public AbstractDetectionPO(List<D> detections) {
		this.detections = detections;
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
	}

	@Override
	protected void process_next(T object, int port){
		for (D d : this.detections) {			
			if (d.getPredicate().evaluate(object)) {				
				markAsFailure(object);
				process_next_failed(object, port, d);
				return;
			}
		}
		process_next_passed(object, port);
	}
		
	protected void process_next(T object, T testObject, int port){
		for (D d : this.detections) {			
			if (d.getPredicate().evaluate(object, testObject)) {				
				markAsFailure(object);
				process_next_failed(object, port, d);
				return;
			}
		}
		process_next_passed(object, port);
	}
	
	protected abstract void process_next_failed(T object, int port, D detection);
	
	protected abstract void process_next_passed(T object, int port);

	protected T markAsFailure(T object) {		
		object.getMetadata().setDetected(true);
		return object;
	}	
}
