
package de.uniol.inf.is.odysseus.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sourceInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sourceInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ownerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="schema" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}sdfSchemaInformation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sourceInformation", propOrder = {
    "name",
    "ownerId",
    "schema"
})
public class SourceInformation {

    protected String name;
    protected String ownerId;
    protected SdfSchemaInformation schema;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the ownerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Sets the value of the ownerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerId(String value) {
        this.ownerId = value;
    }

    /**
     * Gets the value of the schema property.
     * 
     * @return
     *     possible object is
     *     {@link SdfSchemaInformation }
     *     
     */
    public SdfSchemaInformation getSchema() {
        return schema;
    }

    /**
     * Sets the value of the schema property.
     * 
     * @param value
     *     allowed object is
     *     {@link SdfSchemaInformation }
     *     
     */
    public void setSchema(SdfSchemaInformation value) {
        this.schema = value;
    }

}
