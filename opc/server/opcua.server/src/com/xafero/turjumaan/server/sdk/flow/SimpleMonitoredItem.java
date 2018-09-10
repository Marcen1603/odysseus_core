/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package com.xafero.turjumaan.server.sdk.flow;

import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.MonitoringMode;
import com.inductiveautomation.opcua.stack.core.types.structured.MonitoredItemNotification;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.IMonitoredItem;
import com.xafero.turjumaan.server.sdk.api.INodeReader;

/**
 * The simple monitored item.
 */
class SimpleMonitoredItem implements IMonitoredItem {

	/**
	 * The interface for defining push callbacks.
	 */
	static interface IPushCallback {

		/**
		 * Push the monitored item notification.
		 *
		 * @param notif
		 *            the notification
		 */
		void push(MonitoredItemNotification notif);
	}

	/** The monitored item id. */
	private final UInteger monitoredItemId;

	/** The revised sampling interval. */
	private final Double revisedSamplingInterval;

	/** The revised queue size. */
	private final UInteger revisedQueueSize;

	/** The node. */
	private final NodeId node;

	/** The attribute. */
	private final AttributeIds attribute;

	/** The mode. */
	@SuppressWarnings("unused")
	private final MonitoringMode mode;

	/** The client handle. */
	private final UInteger clientHandle;

	/** The reader. */
	private final INodeReader reader;

	/** The pusher. */
	private final IPushCallback pusher;

	/**
	 * Instantiates a new simple monitored item.
	 *
	 * @param id
	 *            the id
	 * @param node
	 *            the node
	 * @param attribute
	 *            the attribute
	 * @param mode
	 *            the mode
	 * @param samplingInterval
	 *            the sampling interval
	 * @param queueSize
	 *            the queue size
	 * @param clientHandle
	 *            the client handle
	 * @param reader
	 *            the reader
	 * @param pusher
	 *            the pusher
	 */
	SimpleMonitoredItem(UInteger id, NodeId node, AttributeIds attribute, MonitoringMode mode, Double samplingInterval,
			UInteger queueSize, UInteger clientHandle, INodeReader reader, IPushCallback pusher) {
		this.monitoredItemId = id;
		this.revisedSamplingInterval = samplingInterval;
		this.revisedQueueSize = queueSize;
		this.node = node;
		this.attribute = attribute;
		this.mode = mode;
		this.clientHandle = clientHandle;
		this.reader = reader;
		this.pusher = pusher;
	}

	@Override
	public UInteger getMonitoredItemId() {
		return monitoredItemId;
	}

	@Override
	public Double getRevisedSamplingInterval() {
		return revisedSamplingInterval;
	}

	@Override
	public UInteger getRevisedQueueSize() {
		return revisedQueueSize;
	}

	@Override
	public void run() {
		if (Thread.currentThread().isInterrupted())
			return;
		DataValue current = reader.read(node, attribute);
		MonitoredItemNotification notif = new MonitoredItemNotification(clientHandle, current);
		pusher.push(notif);
	}
}