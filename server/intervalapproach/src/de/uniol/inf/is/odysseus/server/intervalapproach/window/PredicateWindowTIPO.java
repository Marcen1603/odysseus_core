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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PredicateWindowAO;

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

	private static final Logger LOG = LoggerFactory.getLogger(PredicateWindowTIPO.class);

	private final IPredicate<? super T> start;
	private final IPredicate<? super T> end;
	private final IPredicate<? super T> advance;
	private final int advanceSize;
	private final long maxWindowTime;
	private final boolean sameStarttime;
	private final Map<Object, Boolean> openedWindow = new HashMap<>();
	private final boolean keepEndElement;
	private final boolean useElementOnlyForStartOrEnd;
	private final boolean nesting;
	private final boolean keepTimeOrder;

	// With this option, a predicate window works like a session window.
	// A session ends when a heartbeat is received. Than, all stored elements will
	// be transferred.
	private boolean closeWindowWithHeartbeat;

	@SuppressWarnings("unchecked")
	public PredicateWindowTIPO(AbstractWindowAO windowao) {
		super(windowao);

		this.start = (IPredicate<? super T>) windowao.getStartCondition().clone();
		if (windowao.getEndCondition() != null) {
			this.end = (IPredicate<? super T>) windowao.getEndCondition().clone();
		} else {
			this.end = null;
		}
		if (windowao.getAdvanceCondition() != null) {
			this.advance = (IPredicate<? super T>) windowao.getAdvanceCondition().clone();
		}else {
			this.advance = null;
		}
		this.advanceSize = windowao.getAdvanceSize();
		
		if (windowao.getWindowSize() != null) {
			this.maxWindowTime = windowao.getBaseTimeUnit().convert(windowao.getWindowSize().getTime(),
					windowao.getWindowSize().getUnit());
		} else {
			this.maxWindowTime = 0;
		}
		this.sameStarttime = windowao.isSameStarttime();
		// needed, so all elements in the process_done phase get the same start
		// timestamp
		this.usesSlideParam = this.sameStarttime;

		this.keepEndElement = windowao.isKeepEndElement();
		this.useElementOnlyForStartOrEnd = windowao.isUseElementOnlyForStartOrEnd();

		if (windowao instanceof PredicateWindowAO) {
			this.closeWindowWithHeartbeat = ((PredicateWindowAO) windowao).getCloseWindowWithHeartbeat();
		}
		nesting = windowao.isNesting();
		keepTimeOrder = windowao.isKeepTimeOrder();
	}

	@Override
	protected void process_open() {
		openedWindow.clear();
		super.process_open();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process(T object, List<T> buffer, Object bufferId, PointInTime ts) {
		initBuffer(bufferId);
		if (LOG.isTraceEnabled()) {
			LOG.trace("New Object " + object);
			LOG.trace("Current buffer " + buffer);
		}
		// Test if elements need to be written
		boolean startEval = evaluateStartCondition(object, buffer);
		boolean elementForEndUsed = false;
		if (openedWindow.get(bufferId)) {
			// Two cases: end is set --> use end predicate
			// end is not set --> use negated start predicate
			// maximum window size reached
			boolean closeWindow = (end != null && (elementForEndUsed = evaluateEndCondition(object, buffer)))
					|| (end == null && !startEval)
					|| (maxWindowTime > 0 && !buffer.isEmpty()
							&& PointInTime.plus(buffer.get(0).getMetadata().getStart(), maxWindowTime)
									.afterOrEquals(object.getMetadata().getStart()));

			if (closeWindow) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("Found end element " + object);
				}
				if (keepEndElement) {
					// if element is used for start and for end, is must be cloned
					if (startEval) {
						appendData((T) object.clone(), bufferId, buffer);
					} else {
						appendData(object, bufferId, buffer);
					}
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
				if (useElementOnlyForStartOrEnd && elementForEndUsed) {
					if (LOG.isTraceEnabled()) {
						LOG.trace("Ignoring for start " + object);
					}
				} else {
					appendData(object, bufferId, buffer);
				}
			} else {
				transferArea.transfer(object, 1);
			}
		}
		ping();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// Session window mode, if closeWindowWithHeartbeat.
		// A session ends when a heartbeat is received. Than, all stored elements will
		// be transferred.
		if (closeWindowWithHeartbeat) {
			synchronized (getBuffers()) {
				for (Object bufferId : getBuffers().keySet()) {
					List<T> buffer = getBuffers().get(bufferId);
					if (!buffer.isEmpty()) {
						produceData(punctuation.getTime(), bufferId, buffer);
					}
				}
			}
		}

		super.processPunctuation(punctuation, port);
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
		List<T> nestedElements = null;
		if (nesting) {
			nestedElements = new ArrayList<>();
		}

		while (!buffer.isEmpty()) {
			T toTransfer = buffer.remove(0);
			if (startTS != null) {
				toTransfer.getMetadata().setStart(new PointInTime(startTS));
			}
			// We can produce tuple with no validity --> Do not send them
			if (endTimestamp.after(toTransfer.getMetadata().getStart())) {
				toTransfer.getMetadata().setEnd(endTimestamp);
				if (nesting) {
					nestedElements.add(toTransfer);
				}else {
					if (keepTimeOrder) {
						transferArea.transfer(toTransfer);
					}else {
						transfer(toTransfer);
					}
				}
			}
		}
		if (nesting && !nestedElements.isEmpty()) {
			transferNested(nestedElements, keepTimeOrder);
		}
		openedWindow.put(bufferId, false);
		// We need to determine the oldest element in all buffers and
		// send a punctuation to the transfer area
		ping();
	}

	protected void transferNested(List<T> nestedElements, boolean keepTimeOrder) {
		throw new RuntimeException("Sorry. Nesting is not supported for this kind of data.");
	}

	@Override
	protected void process_done() {
		// ignore done!
		transferArea.done(0);
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof PredicateWindowTIPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		PredicateWindowTIPO<IStreamObject<ITimeInterval>> other = (PredicateWindowTIPO<IStreamObject<ITimeInterval>>) ipo;
		if (!Objects.equal(this.start, other.start)) {
			return false;
		}
		if (!Objects.equal(this.end, other.end)) {
			return false;
		}
		if (this.maxWindowTime != other.maxWindowTime) {
			return false;
		}
		if (this.sameStarttime != other.sameStarttime) {
			return false;
		}
		if (this.keepEndElement != other.keepEndElement) {
			return false;
		}
		return super.isSemanticallyEqual(ipo);

	}

}
