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

package de.uniol.inf.is.odysseus.server.intervalapproach.window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
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
		extends AbstractPartitionedWindowTIPO<T> {

	final private IPredicate<? super T> start;
	final private IPredicate<? super T> end;
	final private long maxWindowTime;
	final private boolean sameStarttime;
	private Map<Long, Boolean> openedWindow = new HashMap<>();

	@SuppressWarnings("unchecked")
	public PredicateWindowTIPO(AbstractWindowAO windowao) {
		super(windowao);
		IPredicate<?> start = windowao.getStartCondition();
		IPredicate<?> end = windowao.getEndCondition();
		TimeValueItem maxWindowTime = windowao.getWindowSize();
		
		this.start =  (IPredicate<? super T>) start.clone();
		if (end != null) {
			this.end = (IPredicate<? super T>) end.clone();
		} else {
			this.end = null;
		}
		if (maxWindowTime != null) {
			this.maxWindowTime = windowao.getBaseTimeUnit().convert(maxWindowTime.getTime(),
					maxWindowTime.getUnit());
		} else {
			this.maxWindowTime = 0;
		}
		this.sameStarttime = windowao.isSameStarttime();
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
	protected void process(T object, List<T> buffer, Long bufferId, PointInTime ts) {
		// Test if elements need to be written
		boolean startEval = start.evaluate(object);
		Boolean opened = openedWindow.get(bufferId);
		if (opened == null){
			openedWindow.put(bufferId, false);
		}
		if (openedWindow.get(bufferId)) {
			// Two cases: end is set --> use end predicate
			// end is not set --> use negated start predicate
			// maximum window size reached
			if ((end != null && end.evaluate(object))
					|| (end == null && !startEval)
					|| (maxWindowTime > 0 && PointInTime.plus(
							buffer.get(0).getMetadata().getStart(),
							maxWindowTime).afterOrEquals(
							object.getMetadata().getStart()))) {
				produceData(object.getMetadata().getStart(), bufferId, buffer);
			} else {
				appendData(object, bufferId, buffer);
			}
		} else { // Test, if window needs to be opened
			if (startEval) {
				appendData(object, bufferId, buffer);
			} else {
				transferArea.transfer(object, 1);
			}
		}
		
	}
	
	
	private void appendData(T object, Long bufferId, List<T> buffer) {
		openedWindow.put(bufferId, true);
		buffer.add(object);
	}

	private void produceData(PointInTime endTimestamp, Long bufferId, List<T> buffer) {
		PointInTime start = null;
		if (sameStarttime && buffer.size() > 0) {
			start = buffer.get(0).getMetadata().getStart();
		}
		while (!buffer.isEmpty()) {
			T toTransfer = buffer.remove(0);
			if (start != null) {
				toTransfer.getMetadata().setStart(start);
			}
			// We can produce tuple with no validity --> Do not send them
			if (endTimestamp.after(toTransfer.getMetadata().getStart())) {
				toTransfer.getMetadata().setEnd(endTimestamp);
				transferArea.transfer(toTransfer);
			}
		}
		openedWindow.put(bufferId, false);
		// We need to determine the oldest element in all buffers and
		// send a punctuation to the transfer area
		ping();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

			// FIXME: Implement me again
		// Need to iterate over all buffers
		//		if (maxWindowTime > 0){
//			
//			PointInTime.plus(queue.get(0).getMetadata().getStart(),
//						maxWindowTime).afterOrEquals(punctuation.getTime())) {
//			produceData(punctuation.getTime());
//		}

	}

	@Override
	public AbstractPipe<T, T> clone() {
		throw new RuntimeException("Not implemented yet!");
	}




}
