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
package de.uniol.inf.is.odysseus.server.intervalapproach.window;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;

public class SlidingElementWindowTIPO<T extends IStreamObject<ITimeInterval>> extends AbstractWindowTIPO<T> {

	Logger LOG = LoggerFactory.getLogger(SlidingElementWindowTIPO.class);

	List<IStreamable> _buffer = null;
	boolean forceElement = true;
	private long elemsToRemoveFromStream;
	private final long advance;

	private int realelementsize = 0;

	public SlidingElementWindowTIPO(AbstractWindowAO ao) {
		super(ao);
		_buffer = new LinkedList<IStreamable>();
		advance = windowAdvance > 0 ? windowAdvance : 1;
	}

	public SlidingElementWindowTIPO(SlidingElementWindowTIPO<T> po) {
		super(po);
		this._buffer = po._buffer;
		advance = windowAdvance > 0 ? windowAdvance : 1;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T object, int port) {

		if (elemsToRemoveFromStream > 0) {
			elemsToRemoveFromStream--;
		} else {
			synchronized (_buffer) {
				_buffer.add(object);
				this.realelementsize++;
			}
			processBuffer(_buffer, object);
		}
	}

	protected void processBuffer(List<IStreamable> buffer, T object) {
		// Fall testen, dass der Strom zu Ende ist ...
		// Fenster hat die maximale Groesse erreicht
		// inclusive etwaiger punctuations...
		synchronized (_buffer) {
			if (this.realelementsize == (this.windowSize + 1)) {
				// jetzt advance-Elemente rauswerfen

				long elemsToSend = advance;
				// Problem: Fenster ist kleiner als Schrittlaenge -->
				// dann nur alle Elemente aus dem Fenster werfen
				// und Tupel solange verwerfen bis advance wieder erreicht
				if (windowSize < windowAdvance) {
					elemsToSend = windowSize;
					elemsToRemoveFromStream = windowAdvance - windowSize;
				}
				transferBuffer(buffer, elemsToSend, object);
				if (elemsToRemoveFromStream > 0) {
					elemsToRemoveFromStream--;
					// Geht, da noch genau 1 Element im Buffer ist!
					buffer.clear();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void transferBuffer(List<IStreamable> buffer, long numberofelements, T object) {
		// we have to recognize the number of punctuations so far
		synchronized (_buffer) {
			Iterator<IStreamable> bufferIter = buffer.iterator();
			IStreamable elem = buffer.get(0);
			PointInTime start = elem.isPunctuation() ? ((IPunctuation) elem).getTime() : ((T) elem).getMetadata().getStart();
			for (int i = 0; i < numberofelements; i++) {
				IStreamable toReturn = bufferIter.next();
				bufferIter.remove();
				// If slide param is used give all elements of the window
				// the same start timestamp
				if (!usesAdvanceParam) {
					if (toReturn.isPunctuation()) {
						toReturn = ((IPunctuation) toReturn).clone(start);
					} else {
						((T) toReturn).getMetadata().setStart(start);
					}
				}
				if (toReturn.isPunctuation()) {
					sendPunctuation((IPunctuation) toReturn);
					i--;
				} else {
					if (((T) toReturn).getMetadata().getStart().before(object.getMetadata().getStart())) {
						((T) toReturn).getMetadata().setEnd(object.getMetadata().getStart());
						
						transfer((T) toReturn);
					} else {
						LOG.warn("Element " + toReturn + " removed because missing granularity");
					}
					this.realelementsize--;
				}

			}
		}
	}

	@Override
	protected void process_done() {
		synchronized (_buffer) {
			if (!_buffer.isEmpty()) {
				// get the last inserted real object and use this for transfering...

				for (int i = _buffer.size() - 1; i >= 0; i--) {
					IStreamable object = _buffer.get(i);
					if (!object.isPunctuation()) {
						@SuppressWarnings("unchecked")
						T elem = (T) object;
						transferBuffer(_buffer, this.realelementsize, elem);
						return;
					}
				}
				LOG.warn("Nothing written out since there were just punctuations in the window");
			}
		}
	}

	@Override
	public SlidingElementWindowTIPO<T> clone() {
		return new SlidingElementWindowTIPO<T>(this);
	}

	@Override
	public void process_open() {
		if (isPartitioned()) {
			throw new RuntimeException("Partioning not supported in this class");
		}
	}

	@Override
	public void process_close() {
		synchronized (_buffer) {
			this._buffer.clear();
			this.realelementsize = 0;
		}		
	}

	@Override
	public long getElementsStored1() {
		return this.realelementsize;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		synchronized (_buffer) {
			this._buffer.add(punctuation);
		}		
	}

}
