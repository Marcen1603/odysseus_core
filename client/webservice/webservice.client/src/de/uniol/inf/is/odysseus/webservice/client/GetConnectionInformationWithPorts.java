
package de.uniol.inf.is.odysseus.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getConnectionInformationWithPorts complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getConnectionInformationWithPorts">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="securitytoken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="queryId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="minPort" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="maxPort" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getConnectionInformationWithPorts", propOrder = {
    "securitytoken",
    "queryId",
    "minPort",
    "maxPort"
})
public class GetConnectionInformationWithPorts {

    protected String securitytoken;
    protected int queryId;
    protected int minPort;
    protected int maxPort;

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
     * Gets the value of the queryId property.
     * 
     */
    public int getQueryId() {
        return queryId;
    }

    /**
     * Sets the value of the queryId property.
     * 
     */
    public void setQueryId(int value) {
        this.queryId = value;
    }

    /**
     * Gets the value of the minPort property.
     * 
     */
    public int getMinPort() {
        return minPort;
    }

    /**
     * Sets the value of the minPort property.
     * 
     */
    public void setMinPort(int value) {
        this.minPort = value;
    }

    /**
     * Gets the value of the maxPort property.
     * 
     */
    public int getMaxPort() {
        return maxPort;
    }

    /**
     * Sets the value of the maxPort property.
     * 
     */
    public void setMaxPort(int value) {
        this.maxPort = value;
    }

}
