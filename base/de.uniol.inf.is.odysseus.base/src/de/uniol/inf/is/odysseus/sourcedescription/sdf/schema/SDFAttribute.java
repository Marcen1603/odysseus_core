package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.io.Serializable;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public class SDFAttribute extends SDFSchemaElement implements Comparable<SDFAttribute>, Serializable {

	/**
	 * An attribute consists of two parts, the sourceName and the attributeName
	 */
	private String sourceName;
	private String attributeName;
	
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
	 * AttributeName must not contain "." (i.e. the String after the last "." is used 
	 * as AttributeName
	 * @param sourceAndAttributeName
	 */
	public SDFAttribute(String sourceAndAttributeName) {
		super(sourceAndAttributeName);
		init(sourceAndAttributeName);
	}
	
	/**
	 * Creates a new SDFAttribute from a String "SourceName.AttributeName" where
	 * AttributeName must not contain "." (i.e. the String after the last "." is used 
	 * as AttributeName
	 * Each SDFAttribute can have SubAttributes (to allow NF2-Attributes) 
	 * @param sourceAndAttributeName
	 * @param subattributes
	 */
	public SDFAttribute(String sourceAndAttributeName, SDFAttributeList subattributes){
		super(sourceAndAttributeName);
		init(sourceAndAttributeName);
		this.subattributes = subattributes;
	}
	
	/**
	 * Creates a new SDFAttribute from sourceName and AttributeName
	 * @param sourceName
	 * @param attributeName
	 */
	public SDFAttribute(String sourceName, String attributeName) {
		super(sourceName == null ? attributeName : sourceName + "."
				+ attributeName);
		this.sourceName = sourceName;
		this.attributeName = attributeName;
	}
	
	/**
	 * Copy Construktor
	 * @param attribute
	 */
	public SDFAttribute(SDFAttribute attribute) {
		super(attribute);
		this.sourceName = attribute.sourceName;
		this.attributeName = attribute.attributeName;
		this.subattributes = attribute.subattributes == null ? null : attribute.subattributes.clone();
	}

	/**
	 * Methode to share code between constructors
	 * @param sourceAndAttributeName
	 */
	private void init(String sourceAndAttributeName) {
		int pointPos = sourceAndAttributeName.lastIndexOf(".");
		if (pointPos > 0){
			this.sourceName = sourceAndAttributeName.substring(0,pointPos);
			this.attributeName = sourceAndAttributeName.substring(pointPos+1);
		}else{
			this.sourceName = null;
			this.attributeName = sourceAndAttributeName;
		}
		updateName();
	}

	
	/**
	 * Adds a new Subattribute to the end of the current subattribute list
	 * @param subAttr
	 */
	public void addSubattribute(SDFAttribute subAttr){
		if(this.subattributes == null){
			this.subattributes = new SDFAttributeList();
		}
		
		this.subattributes.add(subAttr);
	}
	
// Diese Methode macht so keinen Sinn!
	//	/**
//	 * Overwrites oder sets a Subattribute to Position pos in subattribute list
//	 * 
//	 * @param pos
//	 * @param subAttr
//	 */
//	public void addSubattribute(int pos, SDFAttribute subAttr){
//		if(this.subattributes == null){
//			this.subattributes = new SDFAttributeList();
//		}
//		
//		if(pos > this.subattributes.size()){
//			throw new RuntimeException("Not enough subattributes avaiable. Cannot add new subattribute at position " + pos);
//		}
//		this.subattributes.add(pos, subAttr);
//	}
	
	/**
	 * Removes subattribute
	 */
	public boolean removeSubattribute(SDFAttribute subAttr){
		if(this.subattributes == null){
			return false;
		}
		
		return this.subattributes.remove(subAttr);
	}
	
	/**
	 * removes Subattribute at position pos
	 * @param pos
	 * @return
	 */
	public boolean removeSubattribute(int pos){
		if(this.subattributes == null){
			return false;
		}
		
		return this.subattributes.remove(pos) != null;
	}
	
	/**
	 * Use a list von Attributes as subattributes
	 * @param subAttrs
	 */
	public void setSubattributes(SDFAttributeList subAttrs){
		this.subattributes = subAttrs;
	}
	
	/**
	 * Remove all subattributes
	 */
	public void clearSubattributes(){
		this.subattributes = new SDFAttributeList();
	}

	/**
	 * Get Sourcename-Part of the Attribute
	 * @return
	 */
	public String getSourceName() {
		return this.sourceName;
	}
	
	/**
	 * Set Sourcename-Part of the Attribute
	 * @param sourceName
	 */

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
		updateName();
	}

	/**
	 * Sets AttributeName part
	 * @param attributeName
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
		updateName();
	}

	/**
	 * Returns the name of the attribute without source information.
	 * To get the complete name use getURI oder getSourceName()
	 * @see SDFElement
	 * @return
	 */
	public String getAttributeName() {
		return this.attributeName;
	}
	
	public void setCovariance(ArrayList<?> cov){
		this.covariance = cov;
	}
	
	public ArrayList<?> getCovariance(){
		return this.covariance;
	}

	private void updateName() {
		setURI(sourceName == null ? attributeName : sourceName + "."
				+ attributeName);
	}
	
	/**
	 * Compares current attribute with attr, returns true if 
	 * both sourceNames are not null and equals and attributeNames are
	 * equals
	 * If one of the sourceName is null, only attributeNames are compared 
	 * @param attr
	 * @return true if attributeNames/sourceNames are equal
	 */
	public boolean equalsCQL(SDFAttribute attr) {
		if (this.sourceName != null && attr.sourceName != null) {
			if (!this.sourceName.equals(attr.sourceName)) {
				return false;
			}
		} 
		return this.attributeName.equals(attr.attributeName);
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((attributeName == null) ? 0 : attributeName.hashCode());
		result = prime * result
				+ ((covariance == null) ? 0 : covariance.hashCode());
		result = prime * result
				+ ((sourceName == null) ? 0 : sourceName.hashCode());
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
		if (attributeName == null) {
			if (other.attributeName != null)
				return false;
		} else if (!attributeName.equals(other.attributeName))
			return false;
		if (covariance == null) {
			if (other.covariance != null)
				return false;
		} else if (!covariance.equals(other.covariance))
			return false;
		// TODO: Kurzfristiger Hack
		if (sourceName == null || other.sourceName == null){
			return true;
		}
		if (sourceName == null) {
			if (other.sourceName != null)
				return false;
		} else if (!sourceName.equals(other.sourceName))
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
		if (sourceName != null && o.getSourceName() != null){
			comp = sourceName.compareTo(o.getSourceName());
		}
		if (comp == 0){
			comp = attributeName.compareTo(o.getAttributeName());
		}
		return comp;
	}
	
}