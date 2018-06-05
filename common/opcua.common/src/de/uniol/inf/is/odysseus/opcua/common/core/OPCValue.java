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
package de.uniol.inf.is.odysseus.opcua.common.core;

/**
 * One OPC UA value.
 *
 * @param <T>
 *            the generic type
 */
public class OPCValue<T> {

	/** The time stamp. */
	private Long timestamp;

	/** The value. */
	private T value;

	/** The quality. */
	private Integer quality;

	/** The error. */
	private Long error;

	/**
	 * Instantiates a new OPC value.
	 *
	 * @param timestamp
	 *            the time stamp
	 * @param value
	 *            the value
	 * @param quality
	 *            the quality
	 * @param error
	 *            the error
	 */
	public OPCValue(Long timestamp, T value, Integer quality, Long error) {
		this.timestamp = timestamp;
		this.value = value;
		this.quality = quality;
		this.error = error;
	}

	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public Long getError() {
		return error;
	}

	/**
	 * Gets the time stamp.
	 *
	 * @return the time stamp
	 */
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * Gets the quality.
	 *
	 * @return the quality
	 */
	public Integer getQuality() {
		return quality;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value + "[timestamp=" + timestamp + ", quality=" + quality + ", error=" + error + "]";
	}
}