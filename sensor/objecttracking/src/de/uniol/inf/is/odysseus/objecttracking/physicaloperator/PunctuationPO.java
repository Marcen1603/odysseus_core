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
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.objecttracking.ILatencyProbabilityTimeInterval;

/**
 * This operator inserts a punctuation after each x-th element in the
 * stream. This can be parameterized in PQLHack.
 * 
 * @author Andre Bolles
 *
 * @param <T>
 * @param <M>
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class PunctuationPO<T extends IMetaAttributeContainer<M>, M extends ILatencyProbabilityTimeInterval> extends AbstractPipe<T, T>{

	private int punctuationElemCount;
	
	public PunctuationPO(int puncCount){
		super();
		this.punctuationElemCount = puncCount;
	}
	
	private PunctuationPO(PunctuationPO old){
		super(old);
		this.punctuationElemCount = old.punctuationElemCount;
	}
	
	@Override
	public AbstractPipe<T, T> clone() {
		return new PunctuationPO(this);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return AbstractPipe.OutputMode.MODIFIED_INPUT;
	}

	int elemCount; 
	@Override
	protected void process_next(T object, int port) {
		this.transfer(object);
		this.elemCount++;
		if(this.elemCount % this.punctuationElemCount == 0){
			this.sendPunctuation(object.getMetadata().getStart());
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
		
	}
	
}
