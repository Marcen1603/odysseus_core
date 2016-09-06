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
package de.uniol.inf.is.odysseus.server.opcua.wrappers;

import com.inductiveautomation.opcua.stack.core.types.builtin.DateTime;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.xafero.turjumaan.core.sdk.util.StatusUtils;
import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.NotCacheable;

/**
 * One data item in a Data object.
 */
@Description("A data item")
public class DataItem {

	/** The status. */
	public StatusCode status;

	/** The time. */
	public DateTime time;

	/** The value. */
	public Double value;

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	@NotCacheable
	@Description("Latest floating-point number received")
	public Double getValue() {
		return value;
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	@NotCacheable
	@Description("Latest date of change")
	public DateTime getTime() {
		return time;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	@NotCacheable
	@Description("Latest status of change")
	public String getStatus() {
		return StatusUtils.toString(status);
	}
}