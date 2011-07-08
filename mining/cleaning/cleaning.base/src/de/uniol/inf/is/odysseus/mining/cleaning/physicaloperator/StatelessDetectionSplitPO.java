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
import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateless.IUnaryDetection;
import de.uniol.inf.is.odysseus.mining.metadata.IMiningMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;

public class StatelessDetectionSplitPO<T extends IMetaAttributeContainer<? extends IMiningMetadata>> extends AbstractDetectionSplitPO<T, IUnaryDetection<T>> {	

	public StatelessDetectionSplitPO(List<IUnaryDetection<T>> detections) {
		super(detections);
	}

	public StatelessDetectionSplitPO(StatelessDetectionSplitPO<T> detectionSplitPO) {
		super(detectionSplitPO.detections);
	}

	@Override
	public void process_open() throws OpenFailedException {	
		super.process_open();
		for(IUnaryDetection<T> d : this.detections){
			d.init(this.getOutputSchema());
		}
	}
	
	
	protected void process_next_failed(T object, int port, IUnaryDetection<T> detection){
		transfer(object, 0);
	}
	
	protected void process_next_passed(T object, int port){
		transfer(object, 0);
	}	

	@Override
	public StatelessDetectionSplitPO<T> clone() {
		return new StatelessDetectionSplitPO<T>(this);
	}

}
