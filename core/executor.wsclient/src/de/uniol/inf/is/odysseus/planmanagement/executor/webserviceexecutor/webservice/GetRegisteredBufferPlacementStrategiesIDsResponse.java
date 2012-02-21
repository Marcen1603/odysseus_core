
package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getRegisteredBufferPlacementStrategiesIDsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getRegisteredBufferPlacementStrategiesIDsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/}stringListResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getRegisteredBufferPlacementStrategiesIDsResponse", propOrder = {
    "_return"
})
public class GetRegisteredBufferPlacementStrategiesIDsResponse {

    @XmlElement(name = "return")
    protected StringListResponse _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link StringListResponse }
     *     
     */
    public StringListResponse getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link StringListResponse }
     *     
     */
    public void setReturn(StringListResponse value) {
        this._return = value;
    }

}
