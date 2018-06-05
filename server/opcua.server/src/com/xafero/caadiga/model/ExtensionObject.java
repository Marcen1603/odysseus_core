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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The extension object.
 */
public class ExtensionObject {

	/** The body. */
	@XStreamAlias("Body")
	private Body body;

	/** The type id. */
	@XStreamAlias("TypeId")
	private TypeId typeId;

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * Gets the type id.
	 *
	 * @return the type id
	 */
	public TypeId getTypeId() {
		return typeId;
	}

	/**
	 * The body of an extension.
	 */
	public static class Body {

		/** The enum value type. */
		@XStreamAlias("EnumValueType")
		private EnumValueType enumValueType;

		/** The argument. */
		@XStreamAlias("Argument")
		private Argument argument;

		/**
		 * Gets the enum value type.
		 *
		 * @return the enum value type
		 */
		public EnumValueType getEnumValueType() {
			return enumValueType;
		}

		/**
		 * Gets the argument.
		 *
		 * @return the argument
		 */
		public Argument getArgument() {
			return argument;
		}
	}

	/**
	 * The argument of a body.
	 */
	public static class Argument {

		/** The description. */
		@XStreamAlias("Description")
		private LocalizedText description;

		/** The value rank. */
		@XStreamAlias("ValueRank")
		private int valueRank;

		/** The data type. */
		@XStreamAlias("DataType")
		private TypeId dataType;

		/** The name. */
		@XStreamAlias("Name")
		private String name;

		/** The array dimensions. */
		@XStreamAlias("ArrayDimensions")
		private String arrayDimensions;

		/**
		 * Gets the description.
		 *
		 * @return the description
		 */
		public LocalizedText getDescription() {
			return description;
		}

		/**
		 * Gets the value rank.
		 *
		 * @return the value rank
		 */
		public int getValueRank() {
			return valueRank;
		}

		/**
		 * Gets the data type.
		 *
		 * @return the data type
		 */
		public TypeId getDataType() {
			return dataType;
		}

		/**
		 * Gets the array dimensions.
		 *
		 * @return the array dimensions
		 */
		public String getArrayDimensions() {
			return arrayDimensions;
		}

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}
	}

	/**
	 * The type id of an extension.
	 */
	public static class TypeId {

		/** The identifier. */
		@XStreamAlias("Identifier")
		private String identifier;

		/**
		 * Gets the identifier.
		 *
		 * @return the identifier
		 */
		public String getIdentifier() {
			return identifier;
		}
	}

	/**
	 * The enum value type extension.
	 */
	public static class EnumValueType {

		/** The value. */
		@XStreamAlias("Value")
		private int value;

		/** The description. */
		@XStreamAlias("Description")
		private LocalizedText description;

		/** The display name. */
		@XStreamAlias("DisplayName")
		private LocalizedText displayName;

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public int getValue() {
			return value;
		}

		/**
		 * Gets the description.
		 *
		 * @return the description
		 */
		public LocalizedText getDescription() {
			return description;
		}

		/**
		 * Gets the display name.
		 *
		 * @return the display name
		 */
		public LocalizedText getDisplayName() {
			return displayName;
		}
	}

	/**
	 * The localized text extension.
	 */
	public static class LocalizedText {

		/** The p5. */
		@XStreamAsAttribute
		@XStreamAlias("xmlns:p5")
		final String p5 = "http://www.w3.org/2001/XMLSchema-instance";

		/** The nil. */
		@XStreamAsAttribute
		@XStreamAlias("p5:nil")
		private Boolean nil;

		/** The locale. */
		@XStreamAlias("Locale")
		private String locale;

		/** The text. */
		@XStreamAlias("Text")
		private String text;

		/**
		 * Gets the locale.
		 *
		 * @return the locale
		 */
		public String getLocale() {
			return locale;
		}

		/**
		 * Gets the text.
		 *
		 * @return the text
		 */
		public String getText() {
			return text;
		}
	}
}