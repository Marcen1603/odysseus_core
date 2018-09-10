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
package com.xafero.caadiga.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import com.xafero.caadiga.model.ExtensionObject.LocalizedText;

/**
 * The value.
 */
public class Value {

	/** The string value. */
	@XStreamAlias("String")
	private StringValue stringValue;

	/** The int32 value. */
	@XStreamAlias("Int32")
	private Int32Value int32Value;

	/** The uint32 value. */
	@XStreamAlias("UInt32")
	private UInt32Value uint32Value;

	/** The list of ext objs. */
	@XStreamAlias("ListOfExtensionObject")
	private ListOfExtensionObject listOfExtObjs;

	/** The list of local texts. */
	@XStreamAlias("ListOfLocalizedText")
	private ListOfLocalizedText listOfLocalTexts;

	/** The byte string. */
	@XStreamAlias("ByteString")
	private ByteStringValue byteString;

	/**
	 * Gets the string value.
	 *
	 * @return the string value
	 */
	public StringValue getStringValue() {
		return stringValue;
	}

	/**
	 * Gets the int32 value.
	 *
	 * @return the int32 value
	 */
	public Int32Value getInt32Value() {
		return int32Value;
	}

	/**
	 * Gets the uint32 value.
	 *
	 * @return the uint32 value
	 */
	public UInt32Value getUint32Value() {
		return uint32Value;
	}

	/**
	 * Gets the list of ext objs.
	 *
	 * @return the list of ext objs
	 */
	public ListOfExtensionObject getListOfExtObjs() {
		return listOfExtObjs;
	}

	/**
	 * Gets the list of local texts.
	 *
	 * @return the list of local texts
	 */
	public ListOfLocalizedText getListOfLocalTexts() {
		return listOfLocalTexts;
	}

	/**
	 * Gets the byte string.
	 *
	 * @return the byte string
	 */
	public ByteStringValue getByteString() {
		return byteString;
	}

	/**
	 * An abstract base value.
	 */
	private static abstract class AbstractValue {

		/** The xmlns. */
		@XStreamAsAttribute
		final String xmlns = "http://opcfoundation.org/UA/2008/02/Types.xsd";
	}

	/**
	 * The string value.
	 */
	@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "value" })
	public static class StringValue extends AbstractValue {

		/** The value. */
		private String value;

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
	}

	/**
	 * A list of extension objects.
	 */
	public static class ListOfExtensionObject extends AbstractValue {

		/** The extension objects. */
		@XStreamImplicit(itemFieldName = "ExtensionObject")
		private List<ExtensionObject> extensionObjects;

		/**
		 * Gets the extension objects.
		 *
		 * @return the extension objects
		 */
		public List<ExtensionObject> getExtensionObjects() {
			return extensionObjects;
		}
	}

	/**
	 * A list of localized texts.
	 */
	public static class ListOfLocalizedText extends AbstractValue {

		/** The localized texts. */
		@XStreamImplicit(itemFieldName = "LocalizedText")
		private List<LocalizedText> localizedTexts;

		/**
		 * Gets the localized texts.
		 *
		 * @return the localized texts
		 */
		public List<LocalizedText> getLocalizedTexts() {
			return localizedTexts;
		}
	}

	/**
	 * The integer 32-bit value.
	 */
	@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "value" })
	public static class Int32Value extends AbstractValue {

		/** The value. */
		private int value;

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public int getValue() {
			return value;
		}
	}

	/**
	 * The unsigned integer 32-bit value.
	 */
	@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "value" })
	public static class UInt32Value extends AbstractValue {

		/** The value. */
		private long value;

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public long getValue() {
			return value;
		}
	}

	/**
	 * The byte string value.
	 */
	@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "value" })
	public static class ByteStringValue extends AbstractValue {

		/** The value. */
		private byte[] value;

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public byte[] getValue() {
			return value;
		}
	}
}