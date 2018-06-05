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
package com.xafero.turjumaan.server.ua;

import com.inductiveautomation.opcua.stack.core.types.builtin.ByteString;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExtensionObject;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.structured.Argument;
import com.inductiveautomation.opcua.stack.core.types.structured.EnumValueType;
import com.xafero.caadiga.model.Value;

/**
 * The utilities for handling values.
 */
public final class ValueUtils {

	/**
	 * Instantiates a new value utility.
	 */
	private ValueUtils() {
	}

	/**
	 * Unpack the value type.
	 *
	 * @param value
	 *            the value
	 * @param dataType
	 *            the data type
	 * @param rank
	 *            the rank
	 * @return the object
	 */
	public static Object unpack(Value value, String dataType, Integer rank) {
		switch (dataType) {
		case "ByteString":
			return new ByteString(value.getByteString().getValue());
		case "String":
			return value.getStringValue().getValue();
		case "Int32":
			return value.getInt32Value().getValue();
		case "UInt32":
			return value.getUint32Value().getValue();
		case "i=7594": // EnumValueType
			ExtensionObject[] exts = new ExtensionObject[value.getListOfExtObjs().getExtensionObjects().size()];
			for (int i = 0; i < exts.length; i++)
				exts[i] = toStack(value.getListOfExtObjs().getExtensionObjects().get(i));
			return exts;
		case "LocalizedText":
			if (rank != null && rank == 1) {
				LocalizedText[] locals = new LocalizedText[value.getListOfLocalTexts().getLocalizedTexts().size()];
				for (int i = 0; i < locals.length; i++)
					locals[i] = toStack(value.getListOfLocalTexts().getLocalizedTexts().get(i));
				return locals;
			}
		case "i=120": // NamingRuleType
			return value.getInt32Value().getValue();
		case "i=296": // Argument
			exts = new ExtensionObject[value.getListOfExtObjs().getExtensionObjects().size()];
			for (int i = 0; i < exts.length; i++)
				exts[i] = toArgStack(value.getListOfExtObjs().getExtensionObjects().get(i));
			return exts;
		default:
			throw new UnsupportedOperationException(dataType + " " + rank);
		}
	}

	/**
	 * Convert to extension object to stack.
	 *
	 * @param exo
	 *            the NodeSet object
	 * @return the extension object
	 */
	private static ExtensionObject toArgStack(com.xafero.caadiga.model.ExtensionObject exo) {
		com.xafero.caadiga.model.ExtensionObject.Argument arg = exo.getBody().getArgument();
		Argument ga = new Argument(arg.getName(), NodeId.parse(arg.getDataType().getIdentifier()), arg.getValueRank(),
				toUInts(arg.getArrayDimensions()), toStack(arg.getDescription()));
		NodeId typeId = NodeId.parse(exo.getTypeId().getIdentifier());
		return new ExtensionObject(ga, typeId);
	}

	/**
	 * Convert dimensions to unsigned integer array.
	 *
	 * @param dims
	 *            the dimensions
	 * @return the unsigned integer[]
	 */
	private static UInteger[] toUInts(String dims) {
		if (dims.isEmpty())
			return null;
		String[] pts = dims.split(",");
		UInteger[] dim = new UInteger[pts.length];
		for (int i = 0; i < dim.length; i++)
			dim[i] = UInteger.valueOf(pts[i]);
		return dim;
	}

	/**
	 * Convert to stack object.
	 *
	 * @param exo
	 *            the NodeSet object
	 * @return the extension object
	 */
	private static ExtensionObject toStack(com.xafero.caadiga.model.ExtensionObject exo) {
		com.xafero.caadiga.model.ExtensionObject.EnumValueType ev = exo.getBody().getEnumValueType();
		EnumValueType et = new EnumValueType((long) ev.getValue(), toStack(ev.getDisplayName()),
				toStack(ev.getDescription()));
		NodeId typeId = NodeId.parse(exo.getTypeId().getIdentifier());
		return new ExtensionObject(et, typeId);
	}

	/**
	 * Convert to stack object.
	 *
	 * @param ltext
	 *            the text
	 * @return the localized text
	 */
	private static LocalizedText toStack(com.xafero.caadiga.model.ExtensionObject.LocalizedText ltext) {
		return new LocalizedText(ltext.getLocale(), ltext.getText());
	}
}