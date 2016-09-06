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

import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.types.builtin.DateTime;
import com.inductiveautomation.opcua.stack.core.types.builtin.DiagnosticInfo;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExtensionObject;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.MonitoringMode;
import com.inductiveautomation.opcua.stack.core.types.structured.DataChangeNotification;
import com.inductiveautomation.opcua.stack.core.types.structured.MonitoredItemNotification;
import com.inductiveautomation.opcua.stack.core.types.structured.NotificationMessage;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.IMonitoredItem;
import com.xafero.turjumaan.server.sdk.api.INodeReader;
import com.xafero.turjumaan.server.sdk.api.ISubscription;
import com.xafero.turjumaan.server.sdk.flow.SimpleMonitoredItem.IPushCallback;

/**
 * The simple subscription.
 */
class SimpleSubscription implements ISubscription, IPushCallback {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(SimpleSubscription.class);

	/** The subscription id. */
	private final UInteger subscriptionId;

	/** The revised pub interval. */
	private final Double revisedPubInterval;

	/** The revised lifetime. */
	private final UInteger revisedLifetime;

	/** The revised max keep alive. */
	private final UInteger revisedMaxKeepAlive;

	/** The notifications. */
	private final Queue<MonitoredItemNotification> notifications;

	/** The item counter. */
	private final AtomicLong itemCnt;

	/** The items. */
	private final Map<UInteger, IMonitoredItem> items;

	/** The pool. */
	private final ScheduledExecutorService pool;

	/** The futures. */
	private final List<ScheduledFuture<?>> futures;

	/** The reader. */
	private final INodeReader reader;

	/** The sequence number. */
	private final AtomicLong seqNumber;

	/**
	 * Instantiates a new simple subscription.
	 *
	 * @param subscriptionId
	 *            the subscription id
	 * @param revisedPubInterval
	 *            the revised pub interval
	 * @param revisedLifetime
	 *            the revised lifetime
	 * @param revisedMaxKeepAlive
	 *            the revised max keep alive
	 * @param pool
	 *            the pool
	 * @param reader
	 *            the reader
	 */
	SimpleSubscription(UInteger subscriptionId, Double revisedPubInterval, UInteger revisedLifetime,
			UInteger revisedMaxKeepAlive, ScheduledExecutorService pool, INodeReader reader) {
		this.subscriptionId = subscriptionId;
		this.revisedPubInterval = revisedPubInterval;
		this.revisedLifetime = revisedLifetime;
		this.revisedMaxKeepAlive = revisedMaxKeepAlive;
		this.notifications = new LinkedList<>();
		this.itemCnt = new AtomicLong(0L);
		this.items = new HashMap<>();
		this.pool = pool;
		this.futures = new LinkedList<>();
		this.reader = reader;
		this.seqNumber = new AtomicLong(0L);
	}

	@Override
	public UInteger getSubscriptionId() {
		return subscriptionId;
	}

	@Override
	public Double getRevisedPubInterval() {
		return revisedPubInterval;
	}

	@Override
	public UInteger getRevisedLifetime() {
		return revisedLifetime;
	}

	@Override
	public UInteger getRevisedMaxKeepAlive() {
		return revisedMaxKeepAlive;
	}

	@Override
	public void modify(Double reqPubInterval, UInteger reqLifetime, UInteger reqMaxKeepAlive) {
		// TODO: Check and maybe modify parameters?
	}

	@Override
	public boolean hasNotificationMessage() {
		return !notifications.isEmpty();
	}

	@Override
	public NotificationMessage nextNotificationMessage() {
		synchronized (notifications) {
			if (notifications.isEmpty())
				return null;
			// Fetch data and delete it
			MonitoredItemNotification[] items = notifications
					.toArray(new MonitoredItemNotification[notifications.size()]);
			notifications.clear();
			// Build one data change notification
			UInteger seqNr = uint(seqNumber.incrementAndGet());
			DateTime pubTime = DateTime.now();
			DiagnosticInfo[] diagInfos = null;
			ExtensionObject[] notifData = new ExtensionObject[1];
			notifData[0] = new ExtensionObject(new DataChangeNotification(items, diagInfos));
			// Finish notification message
			NotificationMessage notifMsg = new NotificationMessage(seqNr, pubTime, notifData);
			return notifMsg;
		}
	}

	@Override
	public UInteger[] getAvailableSeqNums() {
		if (notifications.isEmpty())
			return new UInteger[0];
		return new UInteger[] { uint(seqNumber.get() + 1) };
	}

	@Override
	public void cancel() {
		for (ScheduledFuture<?> future : futures)
			future.cancel(true);
		futures.clear();
		items.clear();
		notifications.clear();
	}

	@Override
	public IMonitoredItem newMonitoredItem(NodeId node, AttributeIds attribute, MonitoringMode mode,
			Double samplingInterval, UInteger queueSize, UInteger clientHandle) {
		UInteger id;
		IMonitoredItem item;
		items.put(id = uint(itemCnt.incrementAndGet()), item = new SimpleMonitoredItem(id, node, attribute, mode,
				samplingInterval, queueSize, clientHandle, reader, this));
		long period = item.getRevisedSamplingInterval().longValue();
		if (period <= 0)
			log.warn("Period less or equal to zero! {} for {}/{}", period, node.toParseableString(), attribute);
		else
			futures.add(pool.scheduleAtFixedRate(item, period, period, TimeUnit.MILLISECONDS));
		return item;
	}

	@Override
	public void push(MonitoredItemNotification notif) {
		notifications.add(notif);
	}
}