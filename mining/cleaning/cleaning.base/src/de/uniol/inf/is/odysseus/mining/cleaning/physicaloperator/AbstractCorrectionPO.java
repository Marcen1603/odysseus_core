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

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.mining.cleaning.correction.ICorrection;
import de.uniol.inf.is.odysseus.mining.metadata.IMiningMetadata;

/**
 * 
 * @author Dennis Geesen Created at: 11.07.2011
 */
public abstract class AbstractCorrectionPO<T extends IMetaAttributeContainer<? extends IMiningMetadata>, C extends ICorrection<T>> extends AbstractPipe<T, T> {

	protected List<C> corrections;

	public AbstractCorrectionPO(List<C> corrections) {
		this.corrections = corrections;
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

	
	protected void process_next(T object, T replaceWith, int port) {
		for (C c : this.corrections) {
			if(object.getMetadata().isDetectedAttribute(c.getAttribute())){
				object = c.correctedValue(object);
				//null means that the object was marked to for discarding
				if(object==null){
					return;
				}
				markAsCorrected(object, c);
			}
		}
		transfer(object);
	}

	protected abstract T getValueForCorrection(T original, C c);

	protected T markAsCorrected(T object, C correction) {		
		object.getMetadata().setCorrectedAttribute(correction.getAttribute(), true);
		return object;
	}

}
