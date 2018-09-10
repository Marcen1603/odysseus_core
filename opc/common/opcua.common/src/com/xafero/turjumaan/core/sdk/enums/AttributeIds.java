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
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * OPC UA Part 6 / A.1 Attribute Ids / See Table A.1 - Identifiers assigned to
 * Attributes
 */
public enum AttributeIds implements UaEnumeration {

	/** The node identifier. */
	NodeId(1),
	/** The type of the node. */
	NodeClass(2),
	/** Browse name. */
	BrowseName(3),
	/** Display name. */
	DisplayName(4),
	/** Description. */
	Description(5),
	/** If it's writable at all. */
	WriteMask(6),
	/** If it's writable by the current user. */
	UserWriteMask(7),
	/** Is the node abstract. */
	IsAbstract(8),
	/** Is it symmetric. */
	Symmetric(9),
	/** Inverse name. */
	InverseName(10),
	/** It doesn't contain loops. */
	ContainsNoLoops(11),
	/** Event notifier. */
	EventNotifier(12),
	/** The value. */
	Value(13),
	/** The data type. */
	DataType(14),
	/** The value rank. */
	ValueRank(15),
	/** The array dimensions. */
	ArrayDimensions(16),
	/** The access level. */
	AccessLevel(17),
	/** The user access level. */
	UserAccessLevel(18),
	/** The minimum sampling interval. */
	MinimumSamplingInterval(19),
	/** If it is historizing. */
	Historizing(20),
	/** The executable flag. */
	Executable(21),
	/** If it is executable by the current user. */
	UserExecutable(22);

	/** The identifier. */
	private final UInteger identifier;

	/**
	 * Instantiates a new attribute ids.
	 *
	 * @param identifier
	 *            the identifier
	 */
	private AttributeIds(int identifier) {
		this.identifier = UInteger.valueOf(identifier);
	}

	/**
	 * Gets the identifier.
	 *
	 * @return the identifier
	 */
	public UInteger getIdentifier() {
		return identifier;
	}

	/**
	 * Find the attribute by its id.
	 *
	 * @param id
	 *            the id
	 * @return the attribute
	 */
	public static AttributeIds findById(UInteger id) {
		for (AttributeIds val : values())
			if (val.getIdentifier().equals(id))
				return val;
		return null;
	}

	/**
	 * Find attribute by name.
	 *
	 * @param name
	 *            the name
	 * @return the attribute
	 */
	public static AttributeIds findByName(String name) {
		for (AttributeIds val : values())
			if (val.name().equalsIgnoreCase(name) || ("_" + val.name()).equalsIgnoreCase(name))
				return val;
		throw new UnsupportedOperationException(name);
	}

	@Override
	public int getValue() {
		return identifier.intValue();
	}
}