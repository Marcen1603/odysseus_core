
package de.uniol.inf.is.odysseus.webservice.client;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;					
				

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r queryResponse complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="queryResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}response">
 *       &lt;sequence>
 *         &lt;element name="queryState" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}queryState" minOccurs="0"/>
 *         &lt;element name="responseValue" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}logicalQueryInfo" minOccurs="0"/>
 *         &lt;element name="roots" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryResponse", propOrder = {
    "queryState",
    "responseValue",
    "roots",
    "username"
})
@SuppressWarnings(value = { "all" })
public class QueryResponse
    extends Response
{

    @XmlSchemaType(name = "string")
    protected QueryState queryState;
    protected LogicalQueryInfo responseValue;
    @XmlElement(nillable = true)
    protected List<String> roots;
    protected String username;

    /**
     * Ruft den Wert der queryState-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link QueryState }
     *     
     */
    public QueryState getQueryState() {
        return queryState;
    }

    /**
     * Legt den Wert der queryState-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryState }
     *     
     */
    public void setQueryState(QueryState value) {
        this.queryState = value;
    }

    /**
     * Ruft den Wert der responseValue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LogicalQueryInfo }
     *     
     */
    public LogicalQueryInfo getResponseValue() {
        return responseValue;
    }

    /**
     * Legt den Wert der responseValue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LogicalQueryInfo }
     *     
     */
    public void setResponseValue(LogicalQueryInfo value) {
        this.responseValue = value;
    }

    /**
     * Gets the value of the roots property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roots property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoots().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRoots() {
        if (roots == null) {
            roots = new ArrayList<String>();
        }
        return this.roots;
    }

    /**
     * Ruft den Wert der username-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Legt den Wert der username-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

}
