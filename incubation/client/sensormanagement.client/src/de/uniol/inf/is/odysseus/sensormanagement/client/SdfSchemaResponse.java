
package de.uniol.inf.is.odysseus.sensormanagement.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r sdfSchemaResponse complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="sdfSchemaResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}response">
 *       &lt;sequence>
 *         &lt;element name="responseValue" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}sdfSchemaInformation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sdfSchemaResponse", propOrder = {
    "responseValue"
})
@SuppressWarnings(value = { "all" })
public class SdfSchemaResponse
    extends Response
{

    protected SdfSchemaInformation responseValue;

    /**
     * Ruft den Wert der responseValue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SdfSchemaInformation }
     *     
     */
    public SdfSchemaInformation getResponseValue() {
        return responseValue;
    }

    /**
     * Legt den Wert der responseValue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SdfSchemaInformation }
     *     
     */
    public void setResponseValue(SdfSchemaInformation value) {
        this.responseValue = value;
    }

}
