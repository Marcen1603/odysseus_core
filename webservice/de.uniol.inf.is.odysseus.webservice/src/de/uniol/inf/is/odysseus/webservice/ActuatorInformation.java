
package de.uniol.inf.is.odysseus.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActuatorInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActuatorInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="managerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="actuatorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActuatorInformation", propOrder = {
    "managerName",
    "actuatorName",
    "description"
})
public class ActuatorInformation {

    @XmlElement(required = true)
    protected String managerName;
    protected String actuatorName;
    @XmlElement(required = true)
    protected String description;

    /**
     * Gets the value of the actuatorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActuatorName() {
        return actuatorName;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the value of the managerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManagerName() {
        return managerName;
    }

    /**
     * Sets the value of the actuatorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActuatorName(String value) {
        this.actuatorName = value;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Sets the value of the managerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManagerName(String value) {
        this.managerName = value;
    }

}
