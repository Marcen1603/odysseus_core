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

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.xafero.turjumaan.server.sdk.api.INodeReader;
import com.xafero.turjumaan.server.sdk.api.ISubscription;
import com.xafero.turjumaan.server.sdk.api.ISubscriptionManager;

/**
 * The subscription repository.
 */
public class SubscriptionRepository implements ISubscriptionManager, Closeable {

	/** The subscription counter. */
	private final AtomicLong subscrCnt;

	/** The subscriptions. */
	private final Map<UInteger, ISubscription> subscriptions;

	/** The pool. */
	private final ScheduledExecutorService pool;

	/** The reader. */
	private final INodeReader reader;

	/**
	 * Instantiates a new subscription repository.
	 *
	 * @param reader
	 *            the reader
	 */
	public SubscriptionRepository(INodeReader reader) {
		subscrCnt = new AtomicLong(0L);
		subscriptions = new HashMap<UInteger, ISubscription>();
		pool = Executors.newScheduledThreadPool(1);
		this.reader = reader;
	}

	@Override
	public ISubscription newSubscription(Double reqPubInterval, UInteger reqLifetime, UInteger reqMaxKeepAlive) {
		UInteger id;
		ISubscription subscr;
		subscriptions.put(id = uint(subscrCnt.incrementAndGet()),
				subscr = new SimpleSubscription(id, reqPubInterval, reqLifetime, reqMaxKeepAlive, pool, reader));
		return subscr;
	}

	@Override
	public ISubscription getSubscription(UInteger subscrId) {
		return subscriptions.getOrDefault(subscrId, null);
	}

	@Override
	public Iterable<ISubscription> getAllSubscriptions() {
		return subscriptions.values();
	}

	@Override
	public boolean cancelSubscription(UInteger subscrId) {
		if (!subscriptions.containsKey(subscrId))
			return false;
		ISubscription subscr = subscriptions.get(subscrId);
		subscr.cancel();
		return true;
	}

	@Override
	public void close() throws IOException {
		for (ISubscription subscr : subscriptions.values())
			subscr.cancel();
		subscriptions.clear();
		pool.shutdownNow();
		pool.shutdown();
	}
}