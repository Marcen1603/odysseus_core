
package de.uniol.inf.is.odysseus.webservice.client;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.collection.Pair;
				

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for storedProcedureResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="storedProcedureResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}response">
 *       &lt;sequence>
 *         &lt;element name="responseValue" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}storedProcedure" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "storedProcedureResponse", propOrder = {
    "responseValue"
})
@SuppressWarnings(value = { "all" })
public class StoredProcedureResponse
    extends Response
{

    protected StoredProcedure responseValue;

    /**
     * Gets the value of the responseValue property.
     * 
     * @return
     *     possible object is
     *     {@link StoredProcedure }
     *     
     */
    public StoredProcedure getResponseValue() {
        return responseValue;
    }

    /**
     * Sets the value of the responseValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link StoredProcedure }
     *     
     */
    public void setResponseValue(StoredProcedure value) {
        this.responseValue = value;
    }

}
