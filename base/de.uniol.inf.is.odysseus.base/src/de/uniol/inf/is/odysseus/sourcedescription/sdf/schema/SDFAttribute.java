package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.io.Serializable;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

/**
 * This class represents Attributes. Each Attribute can have two parts: A global
 * unique source name and a source unique attribute name.
 * 
 * Each attribute can have a list of subattributes to support NF2 based
 * attributes
 * 
 * @author Marco Grawunder, Andre Bolles
 * 
 */

public class SDFAttribute extends SDFSchemaElement implements
		Comparable<SDFAttribute>, Serializable {

	/**
	 * An attribute consists of two parts, the sourceName and the attributeName
	 */
	// private String sourceName;
	// private String attributeName;

	/**
	 * For NF2 we need subattributes
	 */
	private SDFAttributeList subattributes;

	/**
	 * used for measurement values
	 */
	private ArrayList<?> covariance;

	private static final long serialVersionUID = -5128455072793206061L;

	/**
	 * Creates a new SDFAttribute from a String "SourceName.AttributeName" where
	 * AttributeName must not contain "." (i.e. the String after the last "." is
	 * used as AttributeName
	 * 
	 * @param sourceAndAttributeName
	 */
	public SDFAttribute(String sourceAndAttributeName) {
		super();
		initAttribute(sourceAndAttributeName);
	}

	/**
	 * Creates a new SDFAttribute from a String "SourceName.AttributeName" where
	 * AttributeName must not contain "." (i.e. the String after the last "." is
	 * used as AttributeName Each SDFAttribute can have SubAttributes (to allow
	 * NF2-Attributes)
	 * 
	 * @param sourceAndAttributeName
	 * @param subattributes
	 */
	public SDFAttribute(String sourceAndAttributeName,
			SDFAttributeList subattributes) {
		super();
		initAttribute(sourceAndAttributeName);
		this.subattributes = subattributes;
	}

	/**
	 * Creates a new SDFAttribute from sourceName and AttributeName
	 * 
	 * @param sourceName
	 * @param attributeName
	 */
	public SDFAttribute(String sourceName, String attributeName) {
		super();
		setSourceName(sourceName);
		setAttributeName(attributeName);
	}

	/**
	 * Copy Construktor
	 * 
	 * @param attribute
	 */
	public SDFAttribute(SDFAttribute attribute) {
		super(attribute);
		this.subattributes = attribute.subattributes == null ? null
				: attribute.subattributes.clone();
	}

	/**
	 * Methode to share code between constructors
	 * 
	 * @param sourceAndAttributeName
	 */
	private void initAttribute(String sourceAndAttributeName) {
		int pointPos = sourceAndAttributeName.lastIndexOf(".");
		if (pointPos > 0) {
			setSourceName(sourceAndAttributeName.substring(0, pointPos));
			setAttributeName(sourceAndAttributeName.substring(pointPos + 1));
		} else {
			setSourceName(null);
			setAttributeName(sourceAndAttributeName);
		}
	}

	/**
	 * Adds a new Subattribute to the end of the current subattribute list
	 * 
	 * @param subAttr
	 */
	public void addSubattribute(SDFAttribute subAttr) {
		if (this.subattributes == null) {
			this.subattributes = new SDFAttributeList();
		}

		this.subattributes.add(subAttr);
	}

	// Diese Methode macht so keinen Sinn!
	// /**
	// * Overwrites oder sets a Subattribute to Position pos in subattribute
	// list
	// *
	// * @param pos
	// * @param subAttr
	// */
	// public void addSubattribute(int pos, SDFAttribute subAttr){
	// if(this.subattributes == null){
	// this.subattributes = new SDFAttributeList();
	// }
	//		
	// if(pos > this.subattributes.size()){
	// throw new
	// RuntimeException("Not enough subattributes avaiable. Cannot add new subattribute at position "
	// + pos);
	// }
	// this.subattributes.add(pos, subAttr);
	// }

	/**
	 * Removes subattribute
	 */
	public boolean removeSubattribute(SDFAttribute subAttr) {
		if (this.subattributes == null) {
			return false;
		}

		return this.subattributes.remove(subAttr);
	}

	/**
	 * removes Subattribute at position pos
	 * 
	 * @param pos
	 * @return
	 */
	public boolean removeSubattribute(int pos) {
		if (this.subattributes == null) {
			return false;
		}

		return this.subattributes.remove(pos) != null;
	}

	/**
	 * Use a list von Attributes as subattributes
	 * 
	 * @param subAttrs
	 */
	public void setSubattributes(SDFAttributeList subAttrs) {
		this.subattributes = subAttrs;
	}

	/**
	 * Remove all subattributes
	 */
	public void clearSubattributes() {
		this.subattributes = new SDFAttributeList();
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
	 * Set Sourcename-Part of the Attribute
	 * 
	 * @param sourceName
	 */

	public void setSourceName(String sourceName) {
		setURIWithoutQualName(sourceName);
	}

	/**
	 * Sets AttributeName part
	 * 
	 * @param attributeName
	 */
	public void setAttributeName(String attributeName) {
		setQualName(attributeName);
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

	public void setCovariance(ArrayList<?> cov) {
		this.covariance = cov;
	}

	public ArrayList<?> getCovariance() {
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
	public boolean equalsCQL(SDFAttribute attr) {
		if (this.getSourceName() != null && attr.getSourceName() != null) {
			if (!this.getSourceName().equals(attr.getSourceName())) {
				return false;
			}
			return this.getAttributeName().equals(attr.getAttributeName());
		} else {
			// Combinations
			// TODO: Problem is: name can be as sourceName or as attributeName
			String t1 = this.getSourceName()!=null?this.getSourceName():this.getAttributeName();
			String t2 = attr.getSourceName()!=null?attr.getSourceName():attr.getAttributeName();
			if (t1 != null) {
				return t1.equals(t2);
			}else{
				return false;
			}
		}
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
		// TODO: Kurzfristiger Hack
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
		return new SDFAttribute(this);
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

}