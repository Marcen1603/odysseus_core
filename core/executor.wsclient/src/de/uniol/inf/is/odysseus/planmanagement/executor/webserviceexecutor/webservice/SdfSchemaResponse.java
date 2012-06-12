
package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sdfSchemaResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sdfSchemaResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/}response">
 *       &lt;sequence>
 *         &lt;element name="responseValue" type="{http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/}sdfSchemaInformation" minOccurs="0"/>
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
public class SdfSchemaResponse
    extends Response
{

    protected SdfSchemaInformation responseValue;

    /**
     * Gets the value of the responseValue property.
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
     * Sets the value of the responseValue property.
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
