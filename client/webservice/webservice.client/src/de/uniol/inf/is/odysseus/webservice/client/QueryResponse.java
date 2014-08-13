
package de.uniol.inf.is.odysseus.webservice.client;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
				

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for queryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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

    protected QueryState queryState;
    protected LogicalQueryInfo responseValue;
    @XmlElement(nillable = true)
    protected List<String> roots;
    protected String username;

    /**
     * Gets the value of the queryState property.
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
     * Sets the value of the queryState property.
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
     * Gets the value of the responseValue property.
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
     * Sets the value of the responseValue property.
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
     * Gets the value of the username property.
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
     * Sets the value of the username property.
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
