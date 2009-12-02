package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class CQLAttribute extends SDFAttribute {

	private static final long serialVersionUID = -8675473536366904094L;

	private String sourceName;

	private String attributeName;
	
	/**
	 * used for measurement values 
	 */
	private ArrayList<?> covariance;

	public CQLAttribute(String sourceName, String attributeName) {
		super(sourceName == null ? attributeName : sourceName + "."
				+ attributeName);
		this.sourceName = sourceName;
		this.attributeName = attributeName;
	}

	public CQLAttribute(String sourceAndAttributeName){
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
	
	public CQLAttribute(CQLAttribute attribute) {
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

	@Override
	public CQLAttribute clone() {
		return new CQLAttribute(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		// TODO typen ueberpruefen
		if (obj instanceof CQLAttribute) {
			return this.attributeName.equals( ((CQLAttribute) obj)
					.getAttributeName());
		}

		return this.getURI(false).equals(obj);
	}

	public boolean equalsCQL(CQLAttribute attr) {
		if (this.sourceName != null && attr.sourceName != null) {
			if (!this.sourceName.equals(attr.sourceName)) {
				return false;
			}
		} 
		return this.attributeName.equals(attr.attributeName);
	}

	public static void main(String[] args) {
		String test = "Marco.Grawunder.Test";
		CQLAttribute attr = new CQLAttribute(test);
		System.out.println(attr.getSourceName());
		System.out.println(attr.getAttributeName());
		
	}
}
