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
/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.Order;

/**
 * 
 * @author Dennis Geesen Created at: 29.05.2012
 */
public class ChangeCorrelatePO<K extends IMetaAttribute, R extends IStreamObject<K>> extends AbstractPipe<R, R> implements IProcessInternal<R>, IHasMetadataMergeFunction<K> {

	private R lasthigh;
	private R lastlow;
	private final IPredicate<R> leftHighPredicate;
	private final IPredicate<R> leftLowPredicate;
	private final IPredicate<R> rightHighPredicate;
	private final IPredicate<R> rightLowPredicate;
	private IDataMergeFunction<R> dataMerge;
	private IMetadataMergeFunction<K> metadataMerge;
	private IInputStreamSyncArea<R> inputStreamSyncArea;
	protected ITransferArea<R, R> outputTransferArea;
	

	public ChangeCorrelatePO(IPredicate<R> leftHighPredicate, IPredicate<R> leftLowPredicate, IPredicate<R> rightHighPredicate, IPredicate<R> rightLowPredicate,
			IInputStreamSyncArea<R> inputStreamSyncArea, ITransferArea<R, R> outputTransferArea) {
		super();
		this.leftHighPredicate = leftHighPredicate.clone();
		this.leftLowPredicate = leftLowPredicate.clone();
		this.rightHighPredicate = rightHighPredicate.clone();
		this.rightLowPredicate = rightLowPredicate.clone();
		this.inputStreamSyncArea = inputStreamSyncArea;
		this.outputTransferArea = outputTransferArea;
	}

	public ChangeCorrelatePO(ChangeCorrelatePO<K, R> changeCorrelatePO) {
		this.leftHighPredicate = changeCorrelatePO.leftHighPredicate.clone();
		this.leftLowPredicate = changeCorrelatePO.leftLowPredicate.clone();
		this.rightHighPredicate = changeCorrelatePO.rightHighPredicate.clone();
		this.rightLowPredicate = changeCorrelatePO.rightLowPredicate.clone();
		this.dataMerge = changeCorrelatePO.dataMerge.clone();
		this.metadataMerge = changeCorrelatePO.metadataMerge.clone();
		this.inputStreamSyncArea = changeCorrelatePO.inputStreamSyncArea.clone();
		this.outputTransferArea = changeCorrelatePO.outputTransferArea.clone();
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(R object, int port) {
		inputStreamSyncArea.newElement(object, port);
		outputTransferArea.newElement(object, port);
	}

	@Override
	public void process_internal(R object, int port) {
		if (port == 0) {
			if (this.leftHighPredicate.evaluate(object)) {
				this.lasthigh = object;
			} else if (this.leftLowPredicate.evaluate(object)) {
				this.lastlow = object;
			}
		} else {
			if (this.lasthigh != null && this.rightHighPredicate.evaluate(object)) {
				R newElement = merge(lasthigh, object, Order.LeftRight);
				transfer(newElement);
			} else if (this.lastlow != null && this.rightLowPredicate.evaluate(object)) {
				R newElement = merge(lastlow, object, Order.LeftRight);
				outputTransferArea.transfer(newElement);
			}
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		inputStreamSyncArea.init(this);
		outputTransferArea.init(this);
	}

	protected R merge(R left, R right, Order order) {
		// if (logger.isTraceEnabled()) {
		// logger.trace("JoinTIPO (" + hashCode() + ") start merging: " + left
		// + " AND " + right);
		// }
		R mergedData;
		K mergedMetadata;
		if (order == Order.LeftRight) {
			mergedData = dataMerge.merge(left, right);
			mergedMetadata = metadataMerge.mergeMetadata(left.getMetadata(), right.getMetadata());
		} else {
			mergedData = dataMerge.merge(right, left);
			mergedMetadata = metadataMerge.mergeMetadata(right.getMetadata(), left.getMetadata());
		}
		mergedData.setMetadata(mergedMetadata);
		return mergedData;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		inputStreamSyncArea.newHeartbeat(timestamp, port);
		outputTransferArea.newHeartbeat(timestamp, port);
	}

	@Override
	public void process_newHeartbeat(PointInTime pointInTime) {
		sendPunctuation(pointInTime);
	}

	
	@Override
	public AbstractPipe<R, R> clone() {
		return new ChangeCorrelatePO<K, R>(this);
	}

	public IDataMergeFunction<R> getDataMerge() {
		return dataMerge;
	}

	public IMetadataMergeFunction<K> getMetadataMerge() {
		return metadataMerge;
	}

	public void setDataMerge(IDataMergeFunction<R> dataMerge) {
		this.dataMerge = dataMerge;
	}

	public void setMetadataMerge(IMetadataMergeFunction<K> metadataMerge) {
		this.metadataMerge = metadataMerge;
	}


}
