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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

/**
 * This operator can reduce traffic. It lets an event pass if its different than
 * the last event. A heartbeat generation strategie can be used.
 * 
 * @author Marco Grawunder
 * 
 * @param <R>
 *            The type of the objects to filter
 */
public class ChangeDetectPO<R extends IStreamObject<?>> extends
		AbstractPipe<R, R> {

	static final Logger logger = LoggerFactory.getLogger(ChangeDetectPO.class);

	private R lastElement = null;
	private Map<Long, R> lastElements = new HashMap<>();
	private IHeartbeatGenerationStrategy<R> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<R>();
	private boolean deliverFirstElement = false;
	private IGroupProcessor<R, R> groupProcessor = null;
	private long suppressedElements = 0; 

	public ChangeDetectPO() {
	}

	public ChangeDetectPO(AbstractPipe<R, R> pipe) {
		super(pipe);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	public void setGroupProcessor(IGroupProcessor<R, R> groupProcessor) {
		this.groupProcessor = groupProcessor;
	}

	@Override
	protected void process_next(R object, int port) {
		// logger.debug("Process next: "+object);
		R newLastElement = null;
		R lastElem = null;
		Long groupID = null;
		// Optimization: Use HashMap only if grouping is used
		if (groupProcessor != null) {
			groupID = groupProcessor.getGroupID(object);
			lastElem = lastElements.get(groupID);
		} else {
			lastElem = lastElement;
		}

		if (lastElem == null) {
			newLastElement = object;
			if (deliverFirstElement) {
				transferInternal(object);
			}
		} else {
			if (object != null && areDifferent(object, lastElem)) {
				newLastElement = object;
				transferInternal(object);
			} else {
				heartbeatGenerationStrategy.generateHeartbeat(object, this);
				suppressedElements++;
			}
		}

		if (newLastElement != null) {
			if (groupID != null) {
				lastElements.put(groupID, newLastElement);
			} else {
				lastElement = newLastElement;
			}
		}
	}


	private void transferInternal(R object) {
		transfer(enrichObject(object, suppressedElements));
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
		this.lastElements.clear();
		if (groupProcessor != null) {
			groupProcessor.init();
		}
	}

	@Override
	public AbstractPipe<R, R> clone() {
		return new ChangeDetectPO<R>(this);
	}

	public IHeartbeatGenerationStrategy<R> getHeartbeatGenerationStrategy() {
		return heartbeatGenerationStrategy;
	}

	public void setHeartbeatGenerationStrategy(
			IHeartbeatGenerationStrategy<R> heartbeatGenerationStrategy) {
		this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
	}

	public void setDeliverFirstElement(boolean deliverFirstElement) {
		this.deliverFirstElement = deliverFirstElement;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ChangeDetectPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		ChangeDetectPO<R> rppo = (ChangeDetectPO<R>) ipo;
		if (this.deliverFirstElement == rppo.deliverFirstElement
				&& this.heartbeatGenerationStrategy
						.equals(rppo.heartbeatGenerationStrategy) &&
						((this.groupProcessor != null && this.groupProcessor.equals(rppo.groupProcessor)) || 
						(this.groupProcessor == null && rppo.groupProcessor == null))
						) {
			return true;
		}

		return false;
	}

}
