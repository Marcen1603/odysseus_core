
package de.uniol.inf.is.odysseus.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetActuatorSchemaFault" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getActuatorSchemaFault"
})
@XmlRootElement(name = "GetActuatorSchemaFault")
public class GetActuatorSchemaFault {

    @XmlElement(name = "GetActuatorSchemaFault", required = true)
    protected String getActuatorSchemaFault;

    /**
     * Gets the value of the getActuatorSchemaFault property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetActuatorSchemaFault() {
        return getActuatorSchemaFault;
    }

    /**
     * Sets the value of the getActuatorSchemaFault property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetActuatorSchemaFault(String value) {
        this.getActuatorSchemaFault = value;
    }

}
