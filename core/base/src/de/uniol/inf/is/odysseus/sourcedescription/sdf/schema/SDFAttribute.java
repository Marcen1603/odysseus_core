/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.unit.SDFUnit;

/**
 * This class represents Attributes. Each Attribute can have two parts: A global
 * unique source name and a source unique attribute name.
 * 
 * 
 * @author Marco Grawunder, Andre Bolles
 * 
 */

public class SDFAttribute extends SDFElement implements
		Comparable<SDFAttribute>, Serializable, IClone {

	/**
	 * used for measurement values
	 */
	private List<?> covariance;

	private static final long serialVersionUID = -5128455072793206061L;
	
	final private SDFDatatype datatype;
	final private Map<String, SDFDatatypeConstraint> dtConstraints;
	final private SDFUnit unit;

	/**
	 * Creates a new SDFAttribute from sourceName and AttributeName
	 * 
	 * @param sourceName
	 * @param attributeName
	 */
	public SDFAttribute(String sourceName, String attributeName, SDFDatatype dt) {
		super(sourceName, attributeName);
		this.datatype = dt;
		unit = null;
		this.dtConstraints = null;
	}

	/**
	 * Copy Construktor
	 * 
	 * @param attribute
	 */
	public SDFAttribute(SDFAttribute copy) {
		super(copy);
		this.datatype = copy.datatype;
		this.dtConstraints = copy.dtConstraints;
		this.unit = copy.unit;
	}

	public SDFAttribute(String newSourceName, String newAttributeName,
			SDFAttribute sdfAttribute) {
		super(newSourceName, newAttributeName);
		this.datatype = sdfAttribute.datatype;
		this.dtConstraints = sdfAttribute.dtConstraints;
		this.unit = sdfAttribute.unit;		
	}

	public SDFAttribute(String sourceName, String attrName, SDFDatatype datatype,
			SDFUnit unit, Map<String, SDFDatatypeConstraint> dtConstraints) {
		super(sourceName,attrName);
		this.datatype = datatype;
		this.unit = unit;
		this.dtConstraints = dtConstraints;
	}

	public SDFAttribute(String sourceName, String attrName, SDFDatatype attribType,
			SDFUnit unit, Map<String, SDFDatatypeConstraint> dtConstraints,
			List<?> covariance) {
		super(sourceName,attrName);
		this.datatype = attribType;
		this.unit = unit;
		this.dtConstraints = dtConstraints;
		this.covariance = covariance;
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

	// SDFAttribute is immutable!!
	// public void setCovariance(List<?> cov) {
	// this.covariance = cov;
	// }

	public List<?> getCovariance() {
		return this.covariance;
	}

	/**
	 * Compares current attribute with attr, returns true if both sourceNames
	 * are not null and equals and attributeNames are equals If one of the
	 * sourceName is null, only attributeNames are compared
	 * 
	 * @param attr
	 * @return true if attributeNames/sourceNames are equal
	 */
	public boolean equalsCQL(SDFElement attr) {
		// TODO: WOFUER DER AUSKOMMENTIERTE CODE? damit kann es sein, dass
		// sourcename mit attributename verglichen wird ...
		if (this.getURIWithoutQualName() != null
				&& attr.getURIWithoutQualName() != null) {
			if (!this.getSourceName().equals(attr.getURIWithoutQualName())) {
				return false;
			}
			// return this.getAttributeName().equals(attr.getAttributeName());
		}
		return this.getAttributeName().equals(attr.getQualName());
		// else {
		//
		// // Combinations
		// // TODO: Problem is: name can be as sourceName or as attributeName
		// String t1 =
		// this.getSourceName()!=null?this.getSourceName():this.getAttributeName();
		// String t2 =
		// attr.getSourceName()!=null?attr.getSourceName():attr.getAttributeName();
		// if (t1 != null) {
		// return t1.equals(t2);
		// }else{
		// return false;
		// }
		// }
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((getAttributeName() == null) ? 0 : getAttributeName()
						.hashCode());
		result = prime * result
				+ ((covariance == null) ? 0 : covariance.hashCode());
		result = prime * result
				+ ((getSourceName() == null) ? 0 : getSourceName().hashCode());
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
		if (covariance == null) {
			if (other.covariance != null)
				return false;
		} else if (!covariance.equals(other.covariance))
			return false;
		// TODO: Kurzfristiger Hack (attribute names are equal and only one attribute has a source)
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

	public SDFAttribute clone(String newSourceName, String newAttributeName) {
		return new SDFAttribute(newSourceName, newAttributeName, this);
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

	// // TODO: Anpassen!
	@Override
	public String toString() {
		if (getSourceName() != null) {
			return getSourceName() + "." + getAttributeName();
		} else {
			return getAttributeName();
		}
	}

	//
	// // TODO: Anpassen!
	// @Override
	// private String getURI() {
	// return toString();
	// }
	//
	// // TODO: Anpassen!
	// private String getURI(boolean prettyPrint) {
	// return getURI(prettyPrint, ".");
	// }

	public String getPointURI() {
		return getURI(false, ".");
	}

	public String toPointString() {
		return getPointURI();
	}
	
	public SDFDatatype getDatatype() {
		return datatype;
	}

	public SDFDatatypeConstraint getDtConstraint(String uri) {
		return dtConstraints.get(uri);
	}

	public Collection<SDFDatatypeConstraint> getDtConstraints() {
		return dtConstraints.values();
	}

	public SDFUnit getUnit() {
		return unit;
	}

}