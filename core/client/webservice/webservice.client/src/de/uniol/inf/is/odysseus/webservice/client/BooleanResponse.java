
package de.uniol.inf.is.odysseus.webservice.client;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;					
				

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r booleanResponse complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="booleanResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}response">
 *       &lt;sequence>
 *         &lt;element name="responseValue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "booleanResponse", propOrder = {
    "responseValue"
})
@SuppressWarnings(value = { "all" })
public class BooleanResponse
    extends Response
{

    protected boolean responseValue;

    /**
     * Ruft den Wert der responseValue-Eigenschaft ab.
     * 
     */
    public boolean isResponseValue() {
        return responseValue;
    }

    /**
     * Legt den Wert der responseValue-Eigenschaft fest.
     * 
     */
    public void setResponseValue(boolean value) {
        this.responseValue = value;
    }

}
