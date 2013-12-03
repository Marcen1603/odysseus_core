
package de.uniol.inf.is.odysseus.webservice.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for operatorBuilderListResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="operatorBuilderListResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}response">
 *       &lt;sequence>
 *         &lt;element name="responseValue" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}operatorBuilderInformation" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "operatorBuilderListResponse", propOrder = {
    "responseValue"
})
public class OperatorBuilderListResponse
    extends Response
{

    @XmlElement(nillable = true)
    protected List<OperatorBuilderInformation> responseValue;

    /**
     * Gets the value of the responseValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the responseValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResponseValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OperatorBuilderInformation }
     * 
     * 
     */
    public List<OperatorBuilderInformation> getResponseValue() {
        if (responseValue == null) {
            responseValue = new ArrayList<OperatorBuilderInformation>();
        }
        return this.responseValue;
    }

}
