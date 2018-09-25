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

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

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
	public SlidingPeriodicWindowTIPO(AbstractWindowAO logical) {
		super(logical);
		this.windowSlide = logical.getBaseTimeUnit().convert(logical.getWindowSlide().getTime(), logical.getWindowSlide().getUnit());
		setName(getName() + " slide=" + windowSlide);
	}

	 public SlidingPeriodicWindowTIPO(TimeUnit baseTimeUnit,TimeValueItem windowSize, TimeValueItem windowSlide, SDFSchema inputSchema) {
		super(WindowType.TIME,baseTimeUnit,windowSize, null,windowSlide,false, null,inputSchema);
		this.windowSlide = baseTimeUnit.convert(windowSlide.getTime(), windowSlide.getUnit());
		setName(getName() + " slide=" + windowSlide);
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
			if (isInOrder()){
				sendPunctuation(Heartbeat.createNewHeartbeat(object.getMetadata().getStart()));
			}
		} else {
			object.getMetadata().setStartAndEnd(p_start, p_end);
			transfer(object);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// FIXME: Implement me
		long slide = (punctuation.getTime().getMainPoint() / this.windowSlide)+1;
		PointInTime p_start = new PointInTime(slide * this.windowSlide - this.windowSize);
		sendPunctuation(Heartbeat.createNewHeartbeat(p_start));
//		sendPunctuation(punctuation);
	}

}
