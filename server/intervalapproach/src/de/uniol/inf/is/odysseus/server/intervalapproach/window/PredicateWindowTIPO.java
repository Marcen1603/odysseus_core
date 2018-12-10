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

import com.google.common.base.Objects;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * A window that can be initialized with one or two predicates
 *
 * The start predicates opens a window and collects elements until a) the end
 * predicate is true or b) if the end predicate is not set, the start predicate
 * gets false, optionally respects a partition condition with attributes
 *
 * A optional size information can be used to set the maximum time to wait until
 * a window closes.
 *
 *
 * @author Marco Grawunder
 *
 * @param <T>
 */

public class PredicateWindowTIPO<T extends IStreamObject<ITimeInterval>> extends AbstractPartitionedWindowTIPO<T> {

	private final IPredicate<? super T> start;
	private final IPredicate<? super T> end;
	private final long maxWindowTime;
	private final boolean sameStarttime;
	private final Map<Object, Boolean> openedWindow = new HashMap<>();
	private final boolean keepEndElement;

	@SuppressWarnings("unchecked")
	public PredicateWindowTIPO(AbstractWindowAO windowao) {
		super(windowao);

		this.start = (IPredicate<? super T>) windowao.getStartCondition().clone();
		if (windowao.getEndCondition() != null) {
			this.end = (IPredicate<? super T>) windowao.getEndCondition().clone();
		} else {
			this.end = null;
		}
		if (windowao.getWindowSize() != null) {
			this.maxWindowTime = windowao.getBaseTimeUnit().convert(windowao.getWindowSize().getTime(), windowao.getWindowSize().getUnit());
		} else {
			this.maxWindowTime = 0;
		}
		this.sameStarttime = windowao.isSameStarttime();
		// needed, so all elements in the process_done phase get the same start
		// timestamp
		this.usesSlideParam = this.sameStarttime;

		this.keepEndElement = windowao.isKeepEndElement();
	}

	@Override
	protected void process_open(){
		openedWindow.clear();
		super.process_open();
	}

	@Override
	protected void process(T object, List<T> buffer, Object bufferId, PointInTime ts) {
		initBuffer(bufferId);
		// Test if elements need to be written
		boolean startEval = evaluateStartCondition(object,buffer);
		if (openedWindow.get(bufferId)) {
			// Two cases: end is set --> use end predicate
			// end is not set --> use negated start predicate
			// maximum window size reached

			if ((end != null && evaluateEndCondition(object,buffer)) 
					|| (end == null && !startEval)
					|| (maxWindowTime > 0 && !buffer.isEmpty() && 
						PointInTime.plus(buffer.get(0).getMetadata().getStart(), maxWindowTime).afterOrEquals(object.getMetadata().getStart()))) {
				if (keepEndElement) {
					appendData(object, bufferId, buffer);
				}
				openedWindow.put(bufferId, false);
				produceData(object.getMetadata().getStart(), bufferId, buffer);
			} else {
				appendData(object, bufferId, buffer);
			}
		}
		// the same tuple could be start and end, so do not use else
		if (!openedWindow.get(bufferId)) {
			if (startEval) {
				appendData(object, bufferId, buffer);
			} else {
				transferArea.transfer(object, 1);
			}
		}

	}

	private void initBuffer(Object bufferId) {
		Boolean opened = openedWindow.get(bufferId);
		if (opened == null) {
			openedWindow.put(bufferId, false);
		}
	}

	protected Boolean evaluateStartCondition(T object, List<T> buffer) {
		return start.evaluate(object);
	}

	protected Boolean evaluateEndCondition(T object, List<T> buffer) {
		return end.evaluate(object);
	}
	
	private void appendData(T object, Object bufferId, List<T> buffer) {
		openedWindow.put(bufferId, true);
		buffer.add(object);
	}

	private void produceData(PointInTime endTimestamp, Object bufferId, List<T> buffer) {
		PointInTime startTS = null;
		if (sameStarttime && !buffer.isEmpty()) {
			startTS = buffer.get(0).getMetadata().getStart();
		}
		while (!buffer.isEmpty()) {
			T toTransfer = buffer.remove(0);
			if (startTS != null) {
				toTransfer.getMetadata().setStart(new PointInTime(startTS));
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
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof PredicateWindowTIPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		PredicateWindowTIPO<IStreamObject<ITimeInterval>> other = (PredicateWindowTIPO<IStreamObject<ITimeInterval>>) ipo;
		if (!Objects.equal(this.start,other.start)) {
			return false;
		}
		if (!Objects.equal(this.end, other.end)){
			return false;
		}
		if (this.maxWindowTime != other.maxWindowTime){
			return false;
		}
		if (this.sameStarttime != other.sameStarttime){
			return false;
		}
		if (this.keepEndElement != other.keepEndElement){
			return false;
		}
		return super.isSemanticallyEqual(ipo);

	}

}
