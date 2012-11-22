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

package de.uniol.inf.is.odysseus.intervalapproach.window;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;

/**
 * This is the physical sliding delta window po. This window is used to change
 * the granularity of the time stamps. THIS IS THE LATENCY OPTIMIZED NON BLOCKING VERSION
 * 
 * 
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>, Marco
 *         Grawunder
 */
public class SlidingPeriodicWindowTIPO<R extends IStreamObject<? extends ITimeInterval>>
		extends AbstractWindowTIPO<R> {

	/**
	 * What are the slides in which elements are processed
	 */
	final private long windowSlide;

	/** Creates a new instance of SlidingDeltaWindowPO */
	public SlidingPeriodicWindowTIPO(WindowAO logical) {
		super(logical);
		this.windowSlide = TimeUnit.MILLISECONDS.convert(logical.getWindowSlide(), logical.getTimeUnit());
		setName(getName() + " slide=" + windowSlide);
	}

	public SlidingPeriodicWindowTIPO(SlidingPeriodicWindowTIPO<R> original) {
		super(original);
		this.windowSlide = original.windowSlide;
	}

	@Override
	public SlidingPeriodicWindowTIPO<R> clone() {
		return new SlidingPeriodicWindowTIPO<R>(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void process_next(R object, int port) {
		long pointInTime = object.getMetadata().getStart().getMainPoint();

		long slide = (pointInTime / this.windowSlide)+1;
		PointInTime p_start = new PointInTime(slide * this.windowSlide - this.windowSize);
		PointInTime p_end = new PointInTime(slide * this.windowSlide);

		// Check if elements outside any window
		if (object.getMetadata().getStart().before(p_start)) {
			sendPunctuation(object.getMetadata().getStart());
		} else {
			object.getMetadata().setStartAndEnd(p_start, p_end);
			transfer(object);
		}
	}

	@Override
	public synchronized void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
}
