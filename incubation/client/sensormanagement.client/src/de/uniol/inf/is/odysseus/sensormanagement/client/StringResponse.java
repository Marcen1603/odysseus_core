
package de.uniol.inf.is.odysseus.sensormanagement.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r stringResponse complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="stringResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}response">
 *       &lt;sequence>
 *         &lt;element name="responseValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stringResponse", propOrder = {
    "responseValue"
})
@SuppressWarnings(value = { "all" })
public class StringResponse
    extends Response
{

    protected String responseValue;

    /**
     * Ruft den Wert der responseValue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseValue() {
        return responseValue;
    }

    /**
     * Legt den Wert der responseValue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseValue(String value) {
        this.responseValue = value;
    }

}
