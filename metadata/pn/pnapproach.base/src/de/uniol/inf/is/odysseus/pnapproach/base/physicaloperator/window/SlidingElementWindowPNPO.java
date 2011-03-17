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
package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.planmanagement.IWindow;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

public class SlidingElementWindowPNPO<T extends IMetaAttributeContainer<IPosNeg>>
		extends AbstractPipe<T, T> implements IWindow {

	List<T> buffer = null;
	boolean forceElement = true;
	long windowSize;

	public SlidingElementWindowPNPO(long windowSize) {
		this.windowSize = windowSize;
		buffer = new LinkedList<T>();
	}

	public SlidingElementWindowPNPO(SlidingElementWindowPNPO<T> name) {
		this.windowSize = name.windowSize;
		this.buffer = name.buffer;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	protected synchronized void process_next(T object, int port) {
		buffer.add(object);
		if (buffer.size() == this.windowSize + 1) {
			T toReturn = buffer.remove(0);
			toReturn.getMetadata().setTimestamp(
					object.getMetadata().getTimestamp());
			this.transfer(toReturn);
		}
	}

	@Override
	public final void process_open() {
	}

	@Override
	public final void process_close() {
	}

	@Override
	public SlidingElementWindowPNPO<T> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public long getWindowSize() {
		return this.windowSize;
	}
	
	@Override
	public WindowType getWindowType() {
		return WindowType.TUPLE;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}

}
