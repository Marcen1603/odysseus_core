
package de.uniol.inf.is.odysseus.sensormanagement.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r logicalOperatorInformationResponse complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
@SuppressWarnings(value = { "all" })
public class LogicalOperatorInformationResponse
    extends Response
{

    protected LogicalOperatorInformation responseValue;

    /**
     * Ruft den Wert der responseValue-Eigenschaft ab.
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
     * Legt den Wert der responseValue-Eigenschaft fest.
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
