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
package com.xafero.turjumaan.server.sdk.api;

import java.io.Closeable;

import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * The interface for managing for subscriptions.
 */
public interface ISubscriptionManager extends Closeable {

	/**
	 * Create a new subscription.
	 *
	 * @param reqPubInterval
	 *            the request publish interval
	 * @param reqLifetime
	 *            the request lifetime
	 * @param reqMaxKeepAlive
	 *            the request max keep alive
	 * @return the subscription
	 */
	ISubscription newSubscription(Double reqPubInterval, UInteger reqLifetime, UInteger reqMaxKeepAlive);

	/**
	 * Gets all subscriptions.
	 *
	 * @return all subscriptions
	 */
	Iterable<ISubscription> getAllSubscriptions();

	/**
	 * Gets the subscription by id.
	 *
	 * @param subscrId
	 *            the subscription id
	 * @return the subscription
	 */
	ISubscription getSubscription(UInteger subscrId);

	/**
	 * Cancel subscription by id.
	 *
	 * @param subscrId
	 *            the subscription id
	 * @return true, if successful
	 */
	boolean cancelSubscription(UInteger subscrId);

}