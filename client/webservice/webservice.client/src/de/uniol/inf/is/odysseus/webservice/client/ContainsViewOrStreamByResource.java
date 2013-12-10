
package de.uniol.inf.is.odysseus.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for containsViewOrStreamByResource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="containsViewOrStreamByResource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ri" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}resourceInformation" minOccurs="0"/>
 *         &lt;element name="securitytoken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "containsViewOrStreamByResource", propOrder = {
    "ri",
    "securitytoken"
})
public class ContainsViewOrStreamByResource {

    protected ResourceInformation ri;
    protected String securitytoken;

    /**
     * Gets the value of the ri property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceInformation }
     *     
     */
    public ResourceInformation getRi() {
        return ri;
    }

    /**
     * Sets the value of the ri property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceInformation }
     *     
     */
    public void setRi(ResourceInformation value) {
        this.ri = value;
    }

    /**
     * Gets the value of the securitytoken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecuritytoken() {
        return securitytoken;
    }

    /**
     * Sets the value of the securitytoken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecuritytoken(String value) {
        this.securitytoken = value;
    }

}
