
package de.uniol.inf.is.odysseus.webservice.client;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;					
				

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r response complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="response">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="successful" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "response", propOrder = {
    "successful"
})
@XmlSeeAlso({
    StringListResponse.class,
    LogicalOperatorInformationResponse.class,
    LogicalOperatorInformationListResponse.class,
    LogicalOperatorResponse.class,
    BooleanResponse.class,
    ConnectionInformationResponse.class,
    SourceListResponse.class,
    StoredProcedureResponse.class,
    StringMapStringListResponse.class,
    SdfDatatypeListResponse.class,
    OperatorBuilderListResponse.class,
    StoredProcedureListResponse.class,
    StringResponse.class,
    SdfSchemaResponse.class,
    IntegerCollectionResponse.class,
    QueryResponse.class
})
@SuppressWarnings(value = { "all" })
public class Response {

    protected boolean successful;

    /**
     * Ruft den Wert der successful-Eigenschaft ab.
     * 
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Legt den Wert der successful-Eigenschaft fest.
     * 
     */
    public void setSuccessful(boolean value) {
        this.successful = value;
    }

}
