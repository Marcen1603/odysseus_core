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
package com.xafero.turjumaan.server.sdk.util;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseResultMask;
import com.inductiveautomation.opcua.stack.core.types.structured.Argument;
import com.inductiveautomation.opcua.stack.core.types.structured.BrowseDescription;
import com.inductiveautomation.opcua.stack.core.types.structured.CallMethodRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.ReadValueId;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.inductiveautomation.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import com.inductiveautomation.opcua.stack.core.types.structured.WriteValue;
import com.xafero.turjumaan.core.sdk.conv.UaTypeConverter;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;

/**
 * The utilities for logging.
 */
public final class LogUtils {

	/**
	 * Instantiates a new logging utility.
	 */
	private LogUtils() {
	}

	/**
	 * Convert read values to a string.
	 *
	 * @param rvals
	 *            the read values
	 * @return the string
	 */
	public static String toString(Object[] rvals) {
		return Arrays.stream(rvals).map(n -> toString(n)).collect(Collectors.joining(", "));
	}

	/**
	 * Convert to string.
	 *
	 * @param val
	 *            the value
	 * @return the string
	 */
	public static String toString(Object val) {
		if (val instanceof ReadValueId)
			return toString((ReadValueId) val);
		if (val instanceof WriteValue)
			return toString((WriteValue) val);
		if (val instanceof BrowseDescription)
			return toString((BrowseDescription) val);
		if (val instanceof SubscriptionAcknowledgement)
			return toString((SubscriptionAcknowledgement) val);
		if (val instanceof MonitoredItemCreateRequest)
			return toString((MonitoredItemCreateRequest) val);
		if (val instanceof CallMethodRequest)
			return toString((CallMethodRequest) val);
		if (val instanceof Variant)
			return toString((Variant) val);
		if (val instanceof Argument)
			return toString((Argument) val);
		if (val instanceof ReferenceNode)
			return toString((ReferenceNode) val);
		return val.toString();
	}

	/**
	 * Convert a reference node to string.
	 *
	 * @param val
	 *            the value
	 * @return the string
	 */
	private static String toString(ReferenceNode val) {
		// Extract fields
		String refType = val.getReferenceTypeId().toParseableString();
		Boolean inverse = val.getIsInverse();
		String target = val.getTargetId().toParseableString();
		// Now concatenate this
		return (refType != null ? "refType=" + refType + ", " : "")
				+ (inverse != null ? "inverse=" + inverse + ", " : "") + (target != null ? "target=" + target : "");
	}

	/**
	 * Convert an argument to string.
	 *
	 * @param val
	 *            the value
	 * @return the string
	 */
	private static String toString(Argument val) {
		// Extract fields
		Integer rank = val.getValueRank();
		UInteger[] dims = val.getArrayDimensions();
		String type = val.getDataType().toParseableString();
		String desc = UaTypeConverter.toStr(val.getDescription());
		String name = val.getName();
		// Now concatenate this
		return (name != null ? "name=" + name + ", " : "") + (type != null ? "dataType=" + type + ", " : "")
				+ (rank != null ? "valueRank=" + rank + ", " : "")
				+ (dims != null ? "arrayDimensions=" + Arrays.toString(dims) + ", " : "")
				+ (desc != null ? "description=" + desc : "");
	}

	/**
	 * Convert a variant to string.
	 *
	 * @param val
	 *            the value
	 * @return the string
	 */
	private static String toString(Variant val) {
		return UaTypeConverter.toStr(val.getValue());
	}

	/**
	 * Convert method request to string.
	 *
	 * @param val
	 *            the value
	 * @return the string
	 */
	private static String toString(CallMethodRequest val) {
		return val.getMethodId().toParseableString() + "(" + toString(val.getInputArguments()) + ")" + "@"
				+ val.getObjectId().toParseableString();
	}

	/**
	 * Convert a monitored item to string.
	 *
	 * @param val
	 *            the value
	 * @return the string
	 */
	private static String toString(MonitoredItemCreateRequest val) {
		return toString(val.getItemToMonitor()) + "_" + val.getMonitoringMode();
	}

	/**
	 * Convert an acknowledgement to string.
	 *
	 * @param val
	 *            the value
	 * @return the string
	 */
	private static String toString(SubscriptionAcknowledgement val) {
		return val.getSubscriptionId() + "~" + val.getSequenceNumber();
	}

	/**
	 * Convert a description to string.
	 *
	 * @param val
	 *            the value
	 * @return the string
	 */
	private static String toString(BrowseDescription val) {
		return val.getNodeId().toParseableString() + "^" + BrowseResultMask.from(val.getResultMask().intValue()) + " "
				+ val.getBrowseDirection();
	}

	/**
	 * Convert a write value to string.
	 *
	 * @param val
	 *            the value
	 * @return the string
	 */
	private static String toString(WriteValue val) {
		return val.getNodeId().toParseableString() + "=" + val.getValue().getValue().getValue();
	}

	/**
	 * Convert a read value to string.
	 *
	 * @param val
	 *            the value
	 * @return the string
	 */
	private static String toString(ReadValueId val) {
		return val.getNodeId().toParseableString() + "^" + AttributeIds.findById(val.getAttributeId());
	}
}