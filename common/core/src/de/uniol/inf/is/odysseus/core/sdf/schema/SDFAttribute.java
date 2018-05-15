/**********************************************************************************
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.sdf.schema;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;

/**
 * This class represent an immutable attribute. each attribute must have two
 * parts: A global unique source name and a source unique attribute name.
 *
 * @author Marco Grawunder, Andre Bolles
 *
 */

public class SDFAttribute extends SDFElement implements Comparable<SDFAttribute>, Serializable, IClone {

	private static final long serialVersionUID = -5128455072793206061L;

	public static final String THIS = "THIS";

	/**
	 * additional info that can be added to this attribute
	 */
	private List<?> annotations;

	/**
	 * The data type of the attribute
	 */
	final private SDFDatatype datatype;

	/**
	 * The optional unit of the attribute
	 */
	final private SDFUnit unit;

	/**
	 * A set of data type constraints further restricting the set of possible values
	 */
	final private Map<String, SDFConstraint> dtConstraints;

	/**
	 * Can be used for different thinks, e.g. position in statemap
	 */
	private int number = -1;

	// /**
	// * Create a new SDFAttribute
	// *
	// * @param sourceName
	// * The source name of the attribute
	// * @param attributeName
	// * The attribute name in the source
	// * @param datatype
	// * The data type of this attribute
	// */
	// public SDFAttribute(String sourceName, String attributeName,
	// SDFDatatype datatype) {
	// this(sourceName, attributeName, datatype, null, null);
	// }

	public SDFAttribute(String sourceName, String attributeName, SDFDatatype datatype) {
		this(sourceName, attributeName, datatype, null, (Collection<SDFConstraint>) null);
	}

	public SDFAttribute(SDFAttribute toCopyFrom, SDFDatatype newDatatype) {
		this(toCopyFrom.getSourceName(), toCopyFrom.getAttributeName(), newDatatype, toCopyFrom.getUnit(),
				new LinkedList<>(toCopyFrom.dtConstraints.values()));
	}

	public SDFAttribute(SDFAttribute toCopyFrom, Collection<SDFConstraint> newConstraints) {
		this(toCopyFrom.getSourceName(), toCopyFrom.getAttributeName(), toCopyFrom.getDatatype(), toCopyFrom.getUnit(),
				newConstraints);
	}

	/**
	 * Creates a new SDFAttribute
	 *
	 * @param sourceName
	 *            The source name of the attribute
	 * @param attributeName
	 *            The attribute name in the source
	 * @param datatype
	 *            The data type of this attribute
	 * @param unit
	 *            The unit of this attribute
	 * @param dtConstraints
	 *            A set of constraints further restricting this attribute values
	 */
	public SDFAttribute(String sourceName, String attributeName, SDFDatatype datatype, SDFUnit unit,
			Collection<SDFConstraint> dtConstraints) {
		this(sourceName, attributeName, datatype, unit, dtConstraints, null);
	}

	public SDFAttribute(String sourceName, String attributeName, SDFDatatype datatype, SDFUnit unit,
			Map<String, SDFConstraint> dtConstraints) {
		this(sourceName, attributeName, datatype, unit, dtConstraints.values(), null);
	}

	/**
	 * Create a new SDFAttribute
	 *
	 * @param sourceName
	 *            The source name of the attribute
	 * @param attributeName
	 *            The attribute name in the source
	 * @param datatype
	 *            The data type of this attribute
	 * @param unit
	 *            The unit of this attribute
	 * @param dtConstraints
	 *            A set of constraints further restricting this attribute values
	 * @param additionalInfo
	 *            A list representing free definable additional informations
	 */
	public SDFAttribute(String sourceName, String attributeName, SDFDatatype attribType, SDFUnit unit,
			Collection<SDFConstraint> dtConstraints, List<?> addInfo) {
		super(sourceName, attributeName);
		this.datatype = attribType;
		this.unit = unit;
		this.dtConstraints = new HashMap<>();
		if (dtConstraints != null) {
			for (SDFConstraint c : dtConstraints) {
				this.dtConstraints.put(c.getURI(), c);
			}
		}
		this.annotations = addInfo;
	}

	/**
	 * Creates a new Attribute from another attribute, preserving everything but the
	 * names
	 *
	 * @param newSourceName
	 *            The new source name of the attribute
	 * @param newAttributeName
	 *            The new attribute name
	 * @param sdfAttribute
	 *            The attribute containing the other informations like data type,
	 *            unit etc.
	 */
	public SDFAttribute(String newSourceName, String newAttributeName, SDFAttribute sdfAttribute) {
		super(newSourceName, newAttributeName);
		this.datatype = sdfAttribute.datatype;
		this.dtConstraints = sdfAttribute.dtConstraints;
		this.unit = sdfAttribute.unit;
		this.annotations = sdfAttribute.annotations;
	}

	public SDFAttribute(String name, String attributeName, SDFAttribute a, SDFUnit unit,
			List<SDFConstraint> dtConstraints) {
		super(name, attributeName);
		this.datatype = a.datatype;
		this.dtConstraints = new HashMap<>();
		if (dtConstraints != null) {
			for (SDFConstraint c : dtConstraints) {
				this.dtConstraints.put(c.getURI(), c);
			}
		}
		this.unit = unit;
	}

	/**
	 * Get Sourcename-Part of the Attribute
	 *
	 * @return
	 */
	public String getSourceName() {
		return getURIWithoutQualName();
	}

	/**
	 * Returns the name of the attribute without source information. To get the
	 * complete name use getURI oder getSourceName()
	 *
	 * @see SDFElement
	 * @return
	 */
	public String getAttributeName() {
		return getQualName();
	}

	/**
	 * Returns the additional informations of this attribute
	 *
	 * @return
	 */
	public List<?> getAnnotations() {
		return Collections.unmodifiableList(this.annotations);
	}

	/**
	 * returns the data type of this attribute
	 *
	 * @return
	 */
	public SDFDatatype getDatatype() {
		return datatype;
	}

	/**
	 * returns a specific data type constraint
	 *
	 * @param uri
	 * @return
	 */
	public SDFConstraint getDtConstraint(String uri) {
		return dtConstraints.get(uri);
	}

	/**
	 * returns the set of all data type constraints
	 *
	 * @return
	 */
	public Collection<SDFConstraint> getDtConstraints() {
		return dtConstraints.values();
	}

	/**
	 * Return the unit of this attribute, can be null
	 *
	 * @return
	 */
	public SDFUnit getUnit() {
		return unit;
	}

	/**
	 * Compares current attribute with attr, returns true if both sourceNames are
	 * not null and equals and attributeNames are equals If one of the sourceName is
	 * null, only attributeNames are compared
	 *
	 * @param attr
	 * @return true if attributeNames/sourceNames are equal
	 */
	public boolean equalsCQL(SDFElement attr) {
		if (this.getURIWithoutQualName() != null && attr.getURIWithoutQualName() != null) {
			if (!this.getSourceName().equals(attr.getURIWithoutQualName())) {
				return false;
			}
		}
		return this.getAttributeName().equals(attr.getQualName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getAttributeName() == null) ? 0 : getAttributeName().hashCode());
		result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
		result = prime * result + ((getSourceName() == null) ? 0 : getSourceName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SDFAttribute other = (SDFAttribute) obj;
		if (getAttributeName() == null) {
			if (other.getAttributeName() != null)
				return false;
		} else if (!getAttributeName().equals(other.getAttributeName()))
			return false;
		if (annotations == null) {
			if (other.annotations != null)
				return false;
		} else if (!annotations.equals(other.annotations))
			return false;
		// attribute names are equal and only one attribute has a source
		// same as equalsCQL
		if (getSourceName() == null || other.getSourceName() == null) {
			return true;
		}
		if (getSourceName() == null) {
			if (other.getSourceName() != null)
				return false;
		} else if (!getSourceName().equals(other.getSourceName()))
			return false;
		return true;
	}

	@Override
	public SDFAttribute clone() {
		return this;
	}

	/**
	 * Creates a new SDFAttribute with a new source name and a new attribute name
	 * from this attribute, keeping all other information (like data type or unit)
	 *
	 * @param newSourceName
	 * @param newAttributeName
	 * @return
	 */
	public SDFAttribute clone(String newSourceName, String newAttributeName) {
		return new SDFAttribute(newSourceName, newAttributeName, this);
	}

	/**
	 * Creates a new SDFAttribute with a new datatype from this attribute, keeping
	 * all other information
	 */
	public SDFAttribute clone(SDFDatatype dt) {
		return new SDFAttribute(this, dt);
	}

	private SDFAttribute(SDFAttribute attr, int number) {
		this(attr.getSourceName(), attr.getAttributeName(), attr);
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public SDFAttribute clone(int pos) {
		return new SDFAttribute(this, pos);
	}

	@Override
	public int compareTo(SDFAttribute o) {
		int comp = 0;
		if (getSourceName() != null && o.getSourceName() != null) {
			comp = getSourceName().compareTo(o.getSourceName());
		}
		if (comp == 0) {
			comp = getAttributeName().compareTo(o.getAttributeName());
		}
		return comp;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		if (getSourceName() != null) {
			ret.append(getSourceName());
		}
		ret.append(".").append(getAttributeName());
		if (unit != null) {
			ret.append("[").append(unit).append("]");
		}
		if (number >= 0) {
			ret.append(" ").append(number);
		}

		return ret.toString();
	}

	public static final String replaceSpecialChars(String input) {
		return input.replace("$", "root").replace("*", "_").replace(".", "_").replace("[", "_").replace("]", "_")
				.replace("'", "_").replace(")", "_").replace("(", "_").replace("?", "_");
	}

	public SDFAttribute createNewWithName(String newName) {
		return new SDFAttribute(this.getSourceName(), newName, this);
	}
}