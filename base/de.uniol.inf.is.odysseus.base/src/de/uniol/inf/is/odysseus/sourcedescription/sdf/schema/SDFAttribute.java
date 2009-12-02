package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.io.Serializable;
import java.util.ArrayList;

public class SDFAttribute extends SDFSchemaElement implements Comparable<SDFAttribute>, Serializable {

	private String sourceName;
	private String attributeName;
	
	/**
	 * used for measurement values 
	 */
	private ArrayList<?> covariance;
	
	private static final long serialVersionUID = -5128455072793206061L;

	public SDFAttribute(String sourceName, String attributeName) {
		super(sourceName == null ? attributeName : sourceName + "."
				+ attributeName);
		this.sourceName = sourceName;
		this.attributeName = attributeName;
	}
	
	public SDFAttribute(String sourceAndAttributeName) {
		super(sourceAndAttributeName);
		int pointPos = sourceAndAttributeName.lastIndexOf(".");
		if (pointPos > 0){
			this.sourceName = sourceAndAttributeName.substring(0,pointPos);
			this.attributeName = sourceAndAttributeName.substring(pointPos+1);
		}else{
			this.sourceName = null;
			this.attributeName = sourceAndAttributeName;
		}

	}
	
	public SDFAttribute(SDFAttribute attribute) {
		super(attribute);
		this.sourceName = attribute.sourceName;
		this.attributeName = attribute.attributeName;
	}

	public String getSourceName() {
		return this.sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
		updateName();
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
		updateName();
	}

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
		return this.getURI(false).compareTo(o.getURI(false));
	}
	
}