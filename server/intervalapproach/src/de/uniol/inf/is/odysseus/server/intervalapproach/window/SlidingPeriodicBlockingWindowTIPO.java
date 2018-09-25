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
/*
 * SlidingDeltaWindowPO.java
 *
 * Created on 13. November 2007, 13:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.uniol.inf.is.odysseus.server.intervalapproach.window;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;

/**
 * This is the physical sliding delta window po. It returns elements after a
 * period of time (delta) and is blocking all the other time.
 * 
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>, Marco
 *         Grawunder
 */
public class SlidingPeriodicBlockingWindowTIPO<R extends IStreamObject<? extends ITimeInterval>>
		extends AbstractWindowTIPO<R> {

	/**
	 * This is the number of slides that have been processed.
	 */
	private long lastSlide = 1;

	/**
	 * What are the slides in which elements are processed
	 */
	final private long windowSlide;

	/**
	 * list with buffered elements
	 */
	private LinkedList<IStreamable> inputBuffer = new LinkedList<IStreamable>();

	/** Creates a new instance of SlidingDeltaWindowPO */
	public SlidingPeriodicBlockingWindowTIPO(AbstractWindowAO logical) {
		super(logical);
		this.windowSlide = logical.getBaseTimeUnit().convert(
				logical.getWindowSlide().getTime(), logical.getWindowSlide().getUnit());
		setName(getName() + " slide=" + windowSlide);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		lastSlide = -1;
		inputBuffer.clear();
	}

	@Override
	public void process_next(R object, int port) {
		synchronized (inputBuffer) {
			process(object.getMetadata().getStart());
			// Do not add before processing old slide!
			this.inputBuffer.add(object);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		synchronized (inputBuffer) {
			this.inputBuffer.add(punctuation);
		}
	}

	@SuppressWarnings("unchecked")
	private void process(PointInTime point) {
		synchronized (inputBuffer) {
			long delta = this.windowSlide;
			long winSize = this.windowSize;
			long pointInTime = point.getMainPoint();
			// first check if elements have to be removed and/or delivered
			long slide = pointInTime / delta;
			if (slide > this.lastSlide) {

				// Calc the start and end point of the window for all elements
				// that will be transfered
				// They are all in the last slide!

				long comp_t_start = lastSlide * delta - winSize;
				PointInTime p_start = new PointInTime(Math.max(comp_t_start, 0));
				PointInTime p_end = new PointInTime(lastSlide * delta);

				// 1. Remove all non punctuation elements that are not within
				// the window size!
				// Keep punctuations!
				while (!inputBuffer.isEmpty()) {
					IStreamable elem = inputBuffer.getFirst();
					if (!elem.isPunctuation()) {
						if (((R) elem).getMetadata().getStart().before(p_start)) {
							inputBuffer.removeFirst();
						} else {
							break;
						}
					}
				}

				// 2. all elements before p_end need to be processed
				while (!inputBuffer.isEmpty()) {
					// This is not needed because elements with the next
					// condition
					// cannot be added before this processing is done!
					// && inputBuffer.getFirst().getMetadata().getStart()
					// .before(p_end)) {
					IStreamable elem = inputBuffer.removeFirst();

					if (elem.isPunctuation()) {
						IPunctuation p = (IPunctuation) elem;
						sendPunctuation(p.clone(p_start));
					} else {
						R r = (R) elem;
						r.getMetadata().setStart(p_start);
						r.getMetadata().setEnd(p_end);
						transfer(r);
					}
				}
				// Send punctuation. New start is known!
				if (lastSlide > 0 && isInOrder()) {
					sendPunctuation(Heartbeat.createNewHeartbeat(p_end));
				}

				this.lastSlide = slide;

			}
		}

	}
}
