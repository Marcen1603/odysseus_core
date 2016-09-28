
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
 * <p>Java-Klasse f�r addStoredProcedure complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="addStoredProcedure">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="storedprocedure" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}storedProcedure" minOccurs="0"/>
 *         &lt;element name="securitytoken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addStoredProcedure", propOrder = {
    "name",
    "storedprocedure",
    "securitytoken"
})
@SuppressWarnings(value = { "all" })
public class AddStoredProcedure {

    protected String name;
    protected StoredProcedure storedprocedure;
    protected String securitytoken;

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der storedprocedure-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link StoredProcedure }
     *     
     */
    public StoredProcedure getStoredprocedure() {
        return storedprocedure;
    }

    /**
     * Legt den Wert der storedprocedure-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link StoredProcedure }
     *     
     */
    public void setStoredprocedure(StoredProcedure value) {
        this.storedprocedure = value;
    }

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

}
