
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
 *         &lt;element name="RemoveActuatorFault" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "removeActuatorFault"
})
@XmlRootElement(name = "RemoveActuatorFault")
public class RemoveActuatorFault {

    @XmlElement(name = "RemoveActuatorFault", required = true)
    protected String removeActuatorFault;

    /**
     * Gets the value of the removeActuatorFault property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoveActuatorFault() {
        return removeActuatorFault;
    }

    /**
     * Sets the value of the removeActuatorFault property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoveActuatorFault(String value) {
        this.removeActuatorFault = value;
    }

}
