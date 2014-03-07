
package de.uniol.inf.is.odysseus.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getLogicalQueryPlan complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getLogicalQueryPlan">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="securitytoken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="queryID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getLogicalQueryPlan", propOrder = {
    "securitytoken",
    "queryID"
})
public class GetLogicalQueryPlan {

    protected String securitytoken;
    protected Integer queryID;

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

    /**
     * Gets the value of the queryID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getQueryID() {
        return queryID;
    }

    /**
     * Sets the value of the queryID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setQueryID(Integer value) {
        this.queryID = value;
    }

}
