
package de.uniol.inf.is.odysseus.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for queryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queryResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}response">
 *       &lt;sequence>
 *         &lt;element name="numberOfRoots" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="responseValue" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}logicalQueryInfo" minOccurs="0"/>
 *         &lt;element name="running" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryResponse", propOrder = {
    "numberOfRoots",
    "responseValue",
    "running",
    "username"
})
public class QueryResponse
    extends Response
{

    protected int numberOfRoots;
    protected LogicalQueryInfo responseValue;
    protected boolean running;
    protected String username;

    /**
     * Gets the value of the numberOfRoots property.
     * 
     */
    public int getNumberOfRoots() {
        return numberOfRoots;
    }

    /**
     * Sets the value of the numberOfRoots property.
     * 
     */
    public void setNumberOfRoots(int value) {
        this.numberOfRoots = value;
    }

    /**
     * Gets the value of the responseValue property.
     * 
     * @return
     *     possible object is
     *     {@link LogicalQueryInfo }
     *     
     */
    public LogicalQueryInfo getResponseValue() {
        return responseValue;
    }

    /**
     * Sets the value of the responseValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link LogicalQueryInfo }
     *     
     */
    public void setResponseValue(LogicalQueryInfo value) {
        this.responseValue = value;
    }

    /**
     * Gets the value of the running property.
     * 
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets the value of the running property.
     * 
     */
    public void setRunning(boolean value) {
        this.running = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

}
