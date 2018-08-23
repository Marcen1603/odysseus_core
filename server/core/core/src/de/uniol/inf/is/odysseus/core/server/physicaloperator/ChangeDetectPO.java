/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

/**
 * This operator can reduce traffic. It lets an event pass if its different than
 * the last event. A heartbeat generation strategy can be used.
 * 
 * @author Marco Grawunder
 * 
 * @param <R>
 *            The type of the objects to filter
 */
public class ChangeDetectPO<R extends IStreamObject<?>> extends
		AbstractPipe<R, R> {

	static final Logger logger = LoggerFactory.getLogger(ChangeDetectPO.class);

	private Map<Object, R> lastElements = new HashMap<>();

	protected IHeartbeatGenerationStrategy<R> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<R>();
	protected boolean deliverFirstElement = false;
	protected boolean sendLastOfSameObjects = false;
	protected IGroupProcessor<R, R> groupProcessor = null;
	protected long suppressedElements = 0;

	public ChangeDetectPO() {
	}

	public ChangeDetectPO(ChangeDetectPO<R> other) {
		super(other);
		this.lastElements = Maps.newHashMap(other.lastElements);
		this.heartbeatGenerationStrategy = other.heartbeatGenerationStrategy
				.clone();
		this.deliverFirstElement = other.deliverFirstElement;
		this.groupProcessor = other.groupProcessor;
		this.suppressedElements = other.suppressedElements;
		this.sendLastOfSameObjects = other.sendLastOfSameObjects;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override public boolean deliversStoredElement(int outputPort) { 
		return true; 
	}

	public void setGroupProcessor(IGroupProcessor<R, R> groupProcessor) {
		this.groupProcessor = groupProcessor;
	}

	@Override
	protected void process_next(R object, int port) {
		// logger.debug("Process next: "+object);
		R newLastElement = null;
		R lastElem = null;
		Object groupID = null;
		// Optimization: Use HashMap only if grouping is used
		if (groupProcessor != null) {
			groupID = groupProcessor.getGroupID(object);
		} else {
			groupID = 0l;
		}

		lastElem = lastElements.get(groupID);
		newLastElement = lastElem;

		if (lastElem == null) {
			newLastElement = object;
			if (deliverFirstElement) {
				transferInternal(object, 0);
			}
		} else {
			if (object != null && areDifferent(object, lastElem)) {
				newLastElement = object;
				if (sendLastOfSameObjects) {
					transferInternal(lastElem, 0);
				} else {
					transferInternal(object, 0);
				}
			} else {
				heartbeatGenerationStrategy.generateHeartbeat(object, this);
				suppressedElements++;
				if (sendLastOfSameObjects){
					newLastElement = object;
				}
				// Send all objects which would be lost to port 1
				transferInternal(object, 1);
			}
		}
		lastElements.put(groupID, newLastElement);
	}

	@Override
	protected void process_done() {
		if (sendLastOfSameObjects){
			for (Entry<Object, R> e: lastElements.entrySet()){
				transferInternal(e.getValue(), 0);
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	protected void transferInternal(R object, int port) {
		transfer(enrichObject(object, suppressedElements), port);
		this.suppressedElements = 0;
	}

	protected R enrichObject(R object, long suppressedElements) {
		return object;
	}

	protected boolean areDifferent(R object, R lastElement) {
		return !object.equals(lastElement);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.suppressedElements = 0;
		this.lastElements.clear();
		if (groupProcessor != null) {
			groupProcessor.init();
		}
	}

	public IHeartbeatGenerationStrategy<R> getHeartbeatGenerationStrategy() {
		return heartbeatGenerationStrategy;
	}

	public void setHeartbeatGenerationStrategy(IHeartbeatGenerationStrategy<R> heartbeatGenerationStrategy) {
		this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
	}

	public void setDeliverFirstElement(boolean deliverFirstElement) {
		this.deliverFirstElement = deliverFirstElement;
	}

	public void setSendLastOfSameObjects(boolean sendLastOfSameObjects) {
		this.sendLastOfSameObjects = sendLastOfSameObjects;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ChangeDetectPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		ChangeDetectPO<R> rppo = (ChangeDetectPO<R>) ipo;
		if (this.deliverFirstElement == rppo.deliverFirstElement
				&& this.heartbeatGenerationStrategy.equals(rppo.heartbeatGenerationStrategy)
				&& ((this.groupProcessor != null && this.groupProcessor.equals(rppo.groupProcessor))
						|| (this.groupProcessor == null && rppo.groupProcessor == null))) {
			return true;
		}

		return false;
	}

}