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
package de.uniol.inf.is.odysseus.loadshedding.bufferplacement;

import java.util.Collection;

import de.uniol.inf.is.odysseus.loadshedding.DirectLoadSheddingBuffer;
import de.uniol.inf.is.odysseus.loadshedding.LoadManager;
import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.priority.PriorityPO;
import de.uniol.inf.is.odysseus.priority.buffer.DirectInterlinkBufferedPipe;

@SuppressWarnings({"rawtypes","unchecked"})
public class LoadSheddingBufferPlacement extends
		AbstractBufferPlacementStrategy {

	boolean placeLoadShedder = false;

	@Override
	protected boolean bufferNeeded(
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink, ISink<?> sink) {

		if (childSink instanceof PriorityPO) {
			placeLoadShedder = true;
		} else {
			placeLoadShedder = false;
		}

		if (placeLoadShedder) {
			return true;
		}

		return subscriptions.size() > 1
				|| childSink.getSubscribedToSource().size() > 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IBuffer<?> createNewBuffer() {
		if (!placeLoadShedder) {
			return new DirectInterlinkBufferedPipe();
		}

		return new DirectLoadSheddingBuffer();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initBuffer(IBuffer buffer) {
		if (buffer instanceof DirectLoadSheddingBuffer) {
			LoadManager.getInstance(null).addLoadShedder(
					(DirectLoadSheddingBuffer) buffer);
		}
	}

	@Override
	public String getName() {
		return "A4 Load Shedding Buffer Placement";
	}

}
