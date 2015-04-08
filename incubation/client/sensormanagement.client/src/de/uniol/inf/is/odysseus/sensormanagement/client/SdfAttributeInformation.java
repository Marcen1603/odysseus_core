
package de.uniol.inf.is.odysseus.sensormanagement.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r sdfAttributeInformation complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="sdfAttributeInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sourcename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attributename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="datatype" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}sdfDatatypeInformation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sdfAttributeInformation", propOrder = {
    "sourcename",
    "attributename",
    "datatype"
})
@SuppressWarnings(value = { "all" })
public class SdfAttributeInformation {

    protected String sourcename;
    protected String attributename;
    protected SdfDatatypeInformation datatype;

    /**
     * Ruft den Wert der sourcename-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcename() {
        return sourcename;
    }

    /**
     * Legt den Wert der sourcename-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcename(String value) {
        this.sourcename = value;
    }

    /**
     * Ruft den Wert der attributename-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttributename() {
        return attributename;
    }

    /**
     * Legt den Wert der attributename-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttributename(String value) {
        this.attributename = value;
    }

    /**
     * Ruft den Wert der datatype-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SdfDatatypeInformation }
     *     
     */
    public SdfDatatypeInformation getDatatype() {
        return datatype;
    }

    /**
     * Legt den Wert der datatype-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SdfDatatypeInformation }
     *     
     */
    public void setDatatype(SdfDatatypeInformation value) {
        this.datatype = value;
    }

}
