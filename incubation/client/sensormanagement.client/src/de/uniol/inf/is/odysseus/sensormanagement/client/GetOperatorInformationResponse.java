
package de.uniol.inf.is.odysseus.sensormanagement.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r getOperatorInformationResponse complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="getOperatorInformationResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}logicalOperatorInformationResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getOperatorInformationResponse", propOrder = {
    "_return"
})
@SuppressWarnings(value = { "all" })
public class GetOperatorInformationResponse {

    @XmlElement(name = "return")
    protected LogicalOperatorInformationResponse _return;

    /**
     * Ruft den Wert der return-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LogicalOperatorInformationResponse }
     *     
     */
    public LogicalOperatorInformationResponse getReturn() {
        return _return;
    }

    /**
     * Legt den Wert der return-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LogicalOperatorInformationResponse }
     *     
     */
    public void setReturn(LogicalOperatorInformationResponse value) {
        this._return = value;
    }

}
