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

import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * The interface for monitored items of a subscription.
 */
public interface IMonitoredItem extends Runnable {

	/**
	 * Gets the monitored item id.
	 *
	 * @return the monitored item id
	 */
	UInteger getMonitoredItemId();

	/**
	 * Gets the revised sampling interval.
	 *
	 * @return the revised sampling interval
	 */
	Double getRevisedSamplingInterval();

	/**
	 * Gets the revised queue size.
	 *
	 * @return the revised queue size
	 */
	UInteger getRevisedQueueSize();

}