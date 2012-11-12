/********************************************************************************** 
 * Copyright 2012 The Odysseus Team
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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * A window that can be initialized with one or two predicates
 * 
 * The start predicates opens a window and collects elements until a) the end
 * predicate is true or b) if the end predicate is not set, the start predicate
 * gets false
 * 
 * A optional size information can be used to set the maximum time to wait until
 * a window closes.
 * 
 * @author Marco Grawunder
 * 
 * @param <T>
 */

public class PredicateWindowTIPO<T extends IStreamObject<ITimeInterval>>
		extends AbstractPipe<T, T> {

	private final List<T> queue = new LinkedList<T>();
	final private IPredicate<? super T> start;
	final private IPredicate<? super T> end;
	final private long maxWindowTime;
	final private boolean sameStarttime;
	private boolean openedWindow;

	public PredicateWindowTIPO(IPredicate<? super T> start,
			IPredicate<? super T> end, long maxWindowTime, boolean sameStarttime, TimeUnit unit) {
		this.start = start.clone();
		if (end != null) {
			this.end = end.clone();
		} else {
			this.end = null;
		}
		this.maxWindowTime = TimeUnit.MILLISECONDS.convert(maxWindowTime, unit);
		this.sameStarttime = sameStarttime;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void process_open() throws OpenFailedException {
		super.process_open();
		start.init();
		if (end != null) {
			end.init();
		}
	}

	@Override
	protected synchronized void process_next(T object, int port) {
		// Test if elements need to be written
		boolean startEval = start.evaluate(object);
		if (openedWindow) {
			// Two cases: end is set --> use end predicate
			// end is not set --> use negated start predicate
			// maximum window size reached
			if ((end != null && end.evaluate(object))
					|| !startEval
					|| (maxWindowTime > 0 && PointInTime.plus(
							queue.get(0).getMetadata().getStart(),
							maxWindowTime).afterOrEquals(
							object.getMetadata().getStart()))) {
				produceData(object.getMetadata().getStart());
			} else {
				appendData(object);
			}
		} else { // Test, if window needs to be opened
			if (startEval) {
				appendData(object);
			} else {
				transfer(object, 1);
			}
		}

	}

	private void appendData(T object) {
		openedWindow = true;
		queue.add(object);
	}

	private void produceData(PointInTime endTimestamp) {
		PointInTime start = null;
		if (sameStarttime && queue.size() > 0) {
			start = queue.get(0).getMetadata().getStart();
		}
		while (!queue.isEmpty()) {
			T toTransfer = queue.remove(0);
			if (start != null) {
				toTransfer.getMetadata().setStart(start);
			}
			toTransfer.getMetadata().setEnd(endTimestamp);
			transfer(toTransfer);
		}
		openedWindow = false;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		if (maxWindowTime > 0
				&& PointInTime.plus(queue.get(0).getMetadata().getStart(),
						maxWindowTime).afterOrEquals(timestamp)) {
			produceData(timestamp);
		}

	}

	@Override
	public AbstractPipe<T, T> clone() {
		throw new RuntimeException("Not implemented yet!");
	}

}
