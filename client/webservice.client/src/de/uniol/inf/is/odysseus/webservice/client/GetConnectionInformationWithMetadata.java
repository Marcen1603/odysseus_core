
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
 * <p>Java-Klasse f�r getConnectionInformationWithMetadata complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="getConnectionInformationWithMetadata">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="securitytoken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="queryId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nullValues" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="withMetadata" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getConnectionInformationWithMetadata", propOrder = {
    "securitytoken",
    "queryId",
    "nullValues",
    "withMetadata"
})
@SuppressWarnings(value = { "all" })
public class GetConnectionInformationWithMetadata {

    protected String securitytoken;
    protected int queryId;
    protected boolean nullValues;
    protected boolean withMetadata;

    /**
     * Ruft den Wert der securitytoken-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecuritytoken() {
        return securitytoken;
    }

    /**
     * Legt den Wert der securitytoken-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecuritytoken(String value) {
        this.securitytoken = value;
    }

    /**
     * Ruft den Wert der queryId-Eigenschaft ab.
     * 
     */
    public int getQueryId() {
        return queryId;
    }

    /**
     * Legt den Wert der queryId-Eigenschaft fest.
     * 
     */
    public void setQueryId(int value) {
        this.queryId = value;
    }

    /**
     * Ruft den Wert der nullValues-Eigenschaft ab.
     * 
     */
    public boolean isNullValues() {
        return nullValues;
    }

    /**
     * Legt den Wert der nullValues-Eigenschaft fest.
     * 
     */
    public void setNullValues(boolean value) {
        this.nullValues = value;
    }

    /**
     * Ruft den Wert der withMetadata-Eigenschaft ab.
     * 
     */
    public boolean isWithMetadata() {
        return withMetadata;
    }

    /**
     * Legt den Wert der withMetadata-Eigenschaft fest.
     * 
     */
    public void setWithMetadata(boolean value) {
        this.withMetadata = value;
    }

}
