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
package com.xafero.turjumaan.core.sdk.enums;

import com.inductiveautomation.opcua.stack.core.serialization.UaEnumeration;

/**
 * OPC UA Part 3 / 5.6.2 Variable NodeClass / See Table 8 - Variable NodeClass
 */
public enum ValueRanks implements UaEnumeration {

	/** The value is an array with one dimension. */
	OneDimension(1),

	/** The value is an array with one or more dimensions. */
	OneOrMoreDimensions(0),

	/**
	 * The value is not an array. NOTE: All DataTypes are considered to be
	 * scalar, even if they have array-like semantics like ByteString and
	 * String.
	 */
	Scalar(-1),

	/** The value can be a scalar or an array with any number of dimensions. */
	Any(-2),

	/** The value can be a scalar or a one dimensional array. */
	ScalarOrOneDimension(-3);

	/** The identifier. */
	private final int identifier;

	/**
	 * Instantiates a new value ranks.
	 *
	 * @param identifier
	 *            the identifier
	 */
	private ValueRanks(int identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets the identifier.
	 *
	 * @return the identifier
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * Find rank by id.
	 *
	 * @param id
	 *            the id
	 * @return the rank
	 */
	public static ValueRanks findById(int id) {
		for (ValueRanks val : values())
			if (val.getIdentifier() == id)
				return val;
		return null;
	}

	/**
	 * Find rank by name.
	 *
	 * @param name
	 *            the name
	 * @return the rank
	 */
	public static ValueRanks findByName(String name) {
		for (ValueRanks val : values())
			if (val.name().equalsIgnoreCase(name))
				return val;
		throw new UnsupportedOperationException(name);
	}

	@Override
	public int getValue() {
		return identifier;
	}
}