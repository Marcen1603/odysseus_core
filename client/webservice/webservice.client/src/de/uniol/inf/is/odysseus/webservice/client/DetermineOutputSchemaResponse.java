
package de.uniol.inf.is.odysseus.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for determineOutputSchemaResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="determineOutputSchemaResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}sdfSchemaResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "determineOutputSchemaResponse", propOrder = {
    "_return"
})
public class DetermineOutputSchemaResponse {

    @XmlElement(name = "return")
    protected SdfSchemaResponse _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link SdfSchemaResponse }
     *     
     */
    public SdfSchemaResponse getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link SdfSchemaResponse }
     *     
     */
    public void setReturn(SdfSchemaResponse value) {
        this._return = value;
    }

}
