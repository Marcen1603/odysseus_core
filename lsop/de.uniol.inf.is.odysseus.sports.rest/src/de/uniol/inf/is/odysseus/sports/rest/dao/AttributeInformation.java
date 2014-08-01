package de.uniol.inf.is.odysseus.sports.rest.dao;


/**
 * This class represents an attribute of a schema
 * @author Thore Stratmann
 */
public class AttributeInformation{
	
	private String name;
	private String dataType;

	public AttributeInformation() {}

	public AttributeInformation(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
	}
	

	public String getName() {
        return this.name;
	}

	public void setName(String name) {
        this.name = name;
	}

	public String getDataType() {
        return this.dataType;
	}

	public void setDataType(String dataType) {
        this.dataType = dataType;
	}
}
