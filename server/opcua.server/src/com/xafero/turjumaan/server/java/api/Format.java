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
package com.xafero.turjumaan.server.java.api;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.XmlElement;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.xafero.turjumaan.core.sdk.enums.ValueRanks;

/**
 * The response format of the related node.
 */
public enum Format {

	/** The JavaScript Object Notation (JSON) format. */
	JSON(ValueRanks.Scalar, Identifiers.String),

	/** The Extensible Markup Language (XML) format. */
	XML(ValueRanks.Scalar, Identifiers.XmlElement),

	/** The Properties File format. */
	Properties(ValueRanks.Scalar, Identifiers.String),

	/**
	 * The Object.toString() output
	 */
	ToString(ValueRanks.Scalar, Identifiers.String);

	/** The rank. */
	private final ValueRanks rank;

	/** The type. */
	private final NodeId type;

	/**
	 * Instantiates a new format with rank and type.
	 *
	 * @param rank
	 *            the rank
	 * @param type
	 *            the type
	 */
	private Format(ValueRanks rank, NodeId type) {
		this.rank = rank;
		this.type = type;
	}

	/**
	 * Gets the value rank.
	 *
	 * @return the rank
	 */
	public ValueRanks getRank() {
		return rank;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public NodeId getType() {
		return type;
	}

	/**
	 * Convert a raw object according to the current format.
	 *
	 * @param raw
	 *            the raw
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	public Object convert(Object raw) throws Exception {
		Object val;
		switch (this) {
		case JSON:
			val = toJson(raw);
			break;
		case Properties:
			val = toProps(raw);
			break;
		case ToString:
			val = toStr(raw);
			break;
		case XML:
			val = XmlElement.of(toXML(raw));
			break;
		default:
			throw new UnsupportedOperationException(this.name() + "!");
		}
		return val;
	}

	/** The Constant XML. */
	private static final XStream xml = new XStream();

	/** The Constant JSON. */
	private static final Gson json = (new GsonBuilder()).create();

	/**
	 * Convert to properties.
	 *
	 * @param raw
	 *            the raw
	 * @return the properties's content
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private String toProps(Object raw) throws IOException {
		// Cast to map and copy into properties
		Map<?, ?> map = (Map<?, ?>) raw;
		java.util.Properties props = new java.util.Properties();
		props.putAll(map);
		// Write properties into memory
		Writer wr;
		props.store(wr = new StringWriter(), null);
		wr.flush();
		wr.close();
		// Only return the string
		return wr.toString();
	}

	/**
	 * Convert to XML.
	 *
	 * @param obj
	 *            the object
	 * @return the XML's content
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static String toXML(Object obj) throws IOException {
		Class<?> type = obj.getClass();
		// If non-generic, add to aliases
		String name = type.getSimpleName();
		name = name.replace("[]", "Array");
		if (type.getTypeParameters().length == 0)
			xml.alias(name, type);
		if (type.isArray()) {
			Class<?> cmpType = type.getComponentType();
			name = cmpType.getSimpleName();
			xml.alias(name, cmpType);
		}
		// Write XML into memory
		Writer wr;
		xml.marshal(obj, new CompactWriter(wr = new StringWriter()));
		wr.flush();
		wr.close();
		// Only return the string
		return wr.toString();
	}

	/**
	 * Convert to JSON.
	 *
	 * @param obj
	 *            the object
	 * @return the JSON's content
	 */
	private static String toJson(Object obj) {
		return json.toJson(obj);
	}

	/**
	 * Convert to string.
	 *
	 * @param raw
	 *            the raw
	 * @return the ToString result
	 */
	private static String toStr(Object raw) {
		if (raw.getClass().isArray())
			return Arrays.toString((Object[]) raw);
		return raw.toString();
	}
}