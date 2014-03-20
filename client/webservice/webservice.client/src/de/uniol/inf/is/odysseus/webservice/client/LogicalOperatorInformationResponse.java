
package de.uniol.inf.is.odysseus.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;


/**
 * <p>Java class for logicalOperatorInformationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="logicalOperatorInformationResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}response">
 *       &lt;sequence>
 *         &lt;element name="responseValue" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}logicalOperatorInformation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "logicalOperatorInformationResponse", propOrder = {
    "responseValue"
})
public class LogicalOperatorInformationResponse
    extends Response
{

    protected LogicalOperatorInformation responseValue;

    /**
     * Gets the value of the responseValue property.
     * 
     * @return
     *     possible object is
     *     {@link LogicalOperatorInformation }
     *     
     */
    public LogicalOperatorInformation getResponseValue() {
        return responseValue;
    }

    /**
     * Sets the value of the responseValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link LogicalOperatorInformation }
     *     
     */
    public void setResponseValue(LogicalOperatorInformation value) {
        this.responseValue = value;
    }

}
