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

import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.MonitoringMode;
import com.inductiveautomation.opcua.stack.core.types.structured.NotificationMessage;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;

/**
 * The interface for subscriptions.
 */
public interface ISubscription {

	/**
	 * Gets the subscription id.
	 *
	 * @return the subscription id
	 */
	UInteger getSubscriptionId();

	/**
	 * Gets the revised pub interval.
	 *
	 * @return the revised pub interval
	 */
	Double getRevisedPubInterval();

	/**
	 * Gets the revised lifetime.
	 *
	 * @return the revised lifetime
	 */
	UInteger getRevisedLifetime();

	/**
	 * Gets the revised max keep alive.
	 *
	 * @return the revised max keep alive
	 */
	UInteger getRevisedMaxKeepAlive();

	/**
	 * Modify this subscription.
	 *
	 * @param reqPubInterval
	 *            the request publish interval
	 * @param reqLifetime
	 *            the request lifetime
	 * @param reqMaxKeepAlive
	 *            the request max keep alive
	 */
	void modify(Double reqPubInterval, UInteger reqLifetime, UInteger reqMaxKeepAlive);

	/**
	 * Checks for notification message.
	 *
	 * @return true if there's one left
	 */
	boolean hasNotificationMessage();

	/**
	 * Get next notification message.
	 *
	 * @return the notification message
	 */
	NotificationMessage nextNotificationMessage();

	/**
	 * Gets the available sequence numbers.
	 *
	 * @return the available sequence numbers
	 */
	UInteger[] getAvailableSeqNums();

	/**
	 * Cancel the subscription.
	 */
	void cancel();

	/**
	 * Add a new monitored item.
	 *
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
	 * @return the monitored item
	 */
	IMonitoredItem newMonitoredItem(NodeId node, AttributeIds attribute, MonitoringMode mode, Double samplingInterval,
			UInteger queueSize, UInteger clientHandle);

}