package de.uniol.inf.is.odysseus.wsenrich.util;

public class XmlElement {
	
	/**
	 * The Name of the Xml Element
	 */
	private String elementName;
	
	/**
	 * The Content of the Element
	 */
	private String elementContent;
	
	/**
	 * The Datatype of the Element, 
	 * can be null, if there´s no 
	 * specified Datatype in the Xml Document
	 */
	private String elementDatatype;
	
	/**
	 * Constructor for a XmlElement The XmlElement contains:
	 * @param elementName the Element name
	 * @param elementContent the Content of the Element
	 * @param elementDatatype the Datatype of the Element
	 */
	public XmlElement(String elementName, String elementContent, String elementDatatype) {
		
		this.elementName = elementName;
		this.elementContent = elementContent;
		
		if(elementDatatype.equals("") || elementDatatype == null)  
			this.elementDatatype = "";
		else
			this.elementDatatype = elementDatatype;
	}
	
	/**
	 * @return the Element´s name
	 */
	public String getElementName() {
		return this.elementName;
	}
	
	/**
	 * @return the Content of the Element
	 */
	public String getElementContent() {
		return this.elementContent;
	}
	
	/**
	 * @return the Datatype of the Element
	 */
	public String getElementDatatype() {
		return this.elementDatatype;
	}
	
	/**
	 * Setter for the Elementname
	 * @param elementName
	 */
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	
	/**
	 * Setter for the Content of the Element
	 * @param elementContent
	 */
	public void setElementContent(String elementContent) {
		this.elementContent = elementContent;
	}
	
	/**
	 * Setter for the Datatype of the Element
	 * @param elementDatatype
	 */
	public void setElementDatatype(String elementDatatype) {
		this.elementDatatype = elementDatatype;
	}

}
