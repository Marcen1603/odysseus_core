/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.intervalapproach.window;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;

public class SlidingElementWindowTIPO<T extends IStreamObject<ITimeInterval>>
		extends AbstractWindowTIPO<T> {

	List<T> _buffer = null;
	boolean forceElement = true;
	private long elemsToRemoveFromStream;
	private final long advance;

	public SlidingElementWindowTIPO(WindowAO ao) {
		super(ao);
		_buffer = new LinkedList<T>();
		advance = windowAdvance>0?windowAdvance:1;
	}

	public SlidingElementWindowTIPO(SlidingElementWindowTIPO<T> po) {
		super(po);
		this._buffer = po._buffer;
		advance = windowAdvance>0?windowAdvance:1;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected synchronized void process_next(T object, int port) {

		if (elemsToRemoveFromStream > 0) {
			elemsToRemoveFromStream--;
		} else {
			_buffer.add(object);
			processBuffer(_buffer, object);
		}
	}

	protected synchronized void processBuffer(List<T> buffer, T object) {
		// Fall testen, dass der Strom zu Ende ist ...
		// Fenster hat die maximale Groesse erreicht
		if (buffer.size() == this.windowSize + 1) {
			// jetzt advance-Elemente rauswerfen
			Iterator<T> bufferIter = buffer.iterator();
			long elemsToSend = advance;
			// Problem: Fenster ist kleiner als Schrittlaenge -->
			// dann nur alle Elemente aus dem Fenster werfen
			// und Tupel solange verwerfen bis advance wieder erreicht
			if (windowSize < windowAdvance) {
				elemsToSend = windowSize;
				elemsToRemoveFromStream = windowAdvance - windowSize;
			}
			// In cases of 
			PointInTime start = buffer.get(0).getMetadata().getStart();
			for (int i = 0; i < elemsToSend; i++) {
				T toReturn = bufferIter.next();
				bufferIter.remove();
				// If slide param is used give all elements of the window
				// the same start timestamp
				if (!usesAdvanceParam){
					toReturn.getMetadata().setStart(start);
				}
				toReturn.getMetadata().setEnd(object.getMetadata().getStart());
				transfer(toReturn);
			}
			if (elemsToRemoveFromStream > 0) {
				elemsToRemoveFromStream--;
				// Geht, da noch genau 1 Element im Buffer ist!
				buffer.clear();
			}
		}
	}

	// TODO: Was tut man mit Element-Fenster, wenn der Strom zu Ende ist?
	// @Override
	// protected void process_done() {
	// // Alle noch im Buffer enthaltenen Elemente rausschreiben?
	// super.process_done();
	// }

	@Override
	public SlidingElementWindowTIPO<T> clone() {
		return new SlidingElementWindowTIPO<T>(this);
	}

	@Override
	public void process_open() {
		if (isPartitioned())
			throw new RuntimeException("Partioning not supported in this class");
	}

	@Override
	public void process_close() {
		this._buffer.clear();
	}
	
}
