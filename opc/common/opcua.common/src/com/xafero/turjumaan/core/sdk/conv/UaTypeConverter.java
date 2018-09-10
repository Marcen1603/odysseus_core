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
package com.xafero.turjumaan.core.sdk.conv;

import java.lang.reflect.Array;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import com.inductiveautomation.opcua.stack.core.types.builtin.ByteString;
import com.inductiveautomation.opcua.stack.core.types.builtin.DateTime;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.QualifiedName;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.XmlElement;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UByte;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.ULong;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UShort;
import com.xafero.turjumaan.core.sdk.util.BasicUtils;

/**
 * The UA type converter.
 */
public final class UaTypeConverter {

	/** The Constant base64prefix. */
	private static final String base64prefix = "{base64}";

	/** The Constant utcPrefix. */
	private static final String utcPrefix = "{utc}";

	/** The Constant statusPrefix. */
	private static final String statusPrefix = "{status}";

	/** The Constant arrayPrefix. */
	private static final String arrayPrefix = "{array}";

	/** The Constant enumPrefix. */
	private static final String enumPrefix = "{enum}";

	/**
	 * Instantiates a new UA type converter.
	 */
	private UaTypeConverter() {
	}

	/**
	 * Convert a string to an object.
	 *
	 * @param value
	 *            the value
	 * @return the object
	 */
	public static Object fromStr(String value) {
		if (value.startsWith(arrayPrefix)) {
			String astr = value.replace(arrayPrefix, "");
			char sep = astr.charAt(0);
			astr = astr.substring(2);
			String[] pts = astr.split(Pattern.quote(sep + ""));
			Object firstVal = fromStr(pts[0]);
			Class<?> first = firstVal.getClass();
			Object array = Array.newInstance(first, pts.length);
			Array.set(array, 0, firstVal);
			for (int i = 1; i < pts.length; i++)
				Array.set(array, i, fromStr(pts[i]));
			return array;
		}
		if (value.equals("TRUE"))
			return Boolean.TRUE;
		if (value.equals("FALSE"))
			return Boolean.FALSE;
		if (value.matches("svr=[0-9]+;nsu=(.)+;(i|s|b|g)=(.)+"))
			return ExpandedNodeId.parse(value);
		if (value.matches("ns=[0-9]+;name=(.)+")) {
			String[] pts = value.replace("ns=", "").replace("name=", "").split(";", 2);
			return new QualifiedName(Integer.parseInt(pts[0]), pts[1].equals("?") ? null : pts[1]);
		}
		if (value.matches("l=(.)*;t=(.)+")) {
			String[] pts = value.replace("l=", "").replace("t=", "").split(";", 2);
			return new LocalizedText(pts[0].isEmpty() || pts[0].equals("?") ? null : pts[0],
					pts[1].equals("?") ? null : pts[1]);
		}
		if (value.matches("(ns=[0-9]+;)?(i|s|b|g)=(.)+"))
			return NodeId.parse(value);
		if (value.matches("<(.)+/>") || value.matches("<(.)+>(.)+</(.)+>"))
			return XmlElement.of(value);
		if (value.startsWith(enumPrefix)) {
			String es = value.replace(enumPrefix, "");
			QualifiedName qn = (QualifiedName) fromStr(es);
			return BasicUtils.getEnum(qn.getName(), qn.getNamespaceIndex().intValue());
		}
		if (value.startsWith(base64prefix)) {
			String bs = value.replace(base64prefix, "");
			return bs.equals("?") ? ByteString.NULL_VALUE : ByteString.of(DatatypeConverter.parseBase64Binary(bs));
		}
		if (value.startsWith(utcPrefix))
			return new DateTime(Long.parseLong(value.replace(utcPrefix, "")));
		if (value.startsWith(statusPrefix))
			return new StatusCode(Long.parseLong(value.replace(statusPrefix, "")));
		if (value.matches("-?[0-9]+(\\.[0-9]+)?f"))
			return Float.parseFloat(value.replace("f", ""));
		if (value.matches("-?[0-9]+\\.[0-9]+"))
			return Double.parseDouble(value);
		if (value.matches("-?[0-9]+ul"))
			return ULong.valueOf(value.replace("ul", ""));
		if (value.matches("-?[0-9]+us"))
			return UShort.valueOf(value.replace("us", ""));
		if (value.matches("-?[0-9]+ub"))
			return UByte.valueOf(value.replace("ub", ""));
		if (value.matches("-?[0-9]+u"))
			return UInteger.valueOf(value.replace("u", ""));
		if (value.matches("-?[0-9]+l"))
			return Long.parseLong(value.replace("l", ""));
		if (value.matches("-?[0-9]+s"))
			return Short.parseShort(value.replace("s", ""));
		if (value.matches("-?[0-9]+b"))
			return Byte.parseByte(value.replace("b", ""));
		if (value.matches("-?[0-9]+"))
			return Integer.parseInt(value);
		return value.trim();
	}

	/**
	 * Convert an object to a string.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String toStr(Object value) {
		if (value.getClass().isArray()) {
			StringBuilder bld = new StringBuilder();
			char sep = '|';
			for (int i = 0; i < Array.getLength(value); i++) {
				Object item = Array.get(value, i);
				if (bld.length() >= 1)
					bld.append(sep);
				bld.append(toStr(item));
			}
			return arrayPrefix + sep + " " + bld;
		}
		if (value instanceof String)
			return (String) value;
		if (value instanceof LocalizedText) {
			LocalizedText lt = (LocalizedText) value;
			return String.format("l=%s;t=%s", lt.getLocale() == null ? "?" : lt.getLocale(),
					lt.getText() == null ? "?" : lt.getText());
		}
		if (value instanceof QualifiedName) {
			QualifiedName qn = (QualifiedName) value;
			return String.format("ns=%s;name=%s", qn.getNamespaceIndex(), qn.getName() == null ? "?" : qn.getName());
		}
		if (value instanceof NodeId)
			return ((NodeId) value).toParseableString();
		if (value instanceof ExpandedNodeId)
			return ((ExpandedNodeId) value).toParseableString();
		if (value instanceof XmlElement)
			return ((XmlElement) value).getFragment();
		if (value instanceof ByteString) {
			ByteString bs = (ByteString) value;
			return base64prefix + (bs.isNull() ? "?" : DatatypeConverter.printBase64Binary(bs.bytes()));
		}
		if (value instanceof DateTime)
			return utcPrefix + ((DateTime) value).getUtcTime();
		if (value instanceof StatusCode)
			return statusPrefix + ((StatusCode) value).getValue();
		if (value instanceof Enum) {
			Enum<?> enumVal = (Enum<?>) value;
			return enumPrefix + toStr(new QualifiedName(enumVal.ordinal(), enumVal.getDeclaringClass().getName()));
		}
		if (value instanceof Boolean)
			return value.toString().toUpperCase();
		if (value instanceof Byte)
			return value + "b";
		if (value instanceof Short)
			return value + "s";
		if (value instanceof Integer)
			return value.toString();
		if (value instanceof Long)
			return value + "l";
		if (value instanceof Float)
			return (value + "f").replace(".0f", "f");
		if (value instanceof Double)
			return value.toString();
		if (value instanceof UByte)
			return value + "ub";
		if (value instanceof UShort)
			return value + "us";
		if (value instanceof UInteger)
			return value + "u";
		if (value instanceof ULong)
			return value + "ul";
		throw new UnsupportedOperationException(value + " [" + value.getClass().getSimpleName() + "]!");
	}
}