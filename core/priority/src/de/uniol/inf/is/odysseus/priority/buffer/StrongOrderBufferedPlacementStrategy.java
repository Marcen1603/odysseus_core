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
package de.uniol.inf.is.odysseus.priority.buffer;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.planmanagement.bufferplacement.standardbufferplacementstrategy.StandardBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class StrongOrderBufferedPlacementStrategy extends
		StandardBufferPlacementStrategy {

	private final int maxPrio;

	public StrongOrderBufferedPlacementStrategy() {
		this.maxPrio = 1;
	}

	@Override
	protected IBuffer<?> createNewBuffer() {
		return new StrongOrderBufferedPipe<IMetaAttributeContainer<? extends IPriority>>(
				this.maxPrio);
	}
	
	@Override
	protected IBuffer<?> createNewSourceBuffer() {
		return createNewBuffer();
	}


	@Override
	public String getName() {
		return "Strong Order Buffered Placement Strategy (fixed priorities)";
	}

}
