
package de.uniol.inf.is.odysseus.webservice.client;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;					
				

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sdfDatatypeInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sdfDatatypeInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="uri" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}kindOfDatatype" minOccurs="0"/>
 *         &lt;element name="subtype" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}sdfDatatypeInformation" minOccurs="0"/>
 *         &lt;element name="subSchema" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}sdfSchemaInformation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sdfDatatypeInformation", propOrder = {
    "uri",
    "type",
    "subtype",
    "subSchema"
})
@SuppressWarnings(value = { "all" })
public class SdfDatatypeInformation {

    protected String uri;
    @XmlSchemaType(name = "string")
    protected KindOfDatatype type;
    protected SdfDatatypeInformation subtype;
    protected SdfSchemaInformation subSchema;

    /**
     * Gets the value of the uri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUri() {
        return uri;
    }

    /**
     * Sets the value of the uri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUri(String value) {
        this.uri = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link KindOfDatatype }
     *     
     */
    public KindOfDatatype getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link KindOfDatatype }
     *     
     */
    public void setType(KindOfDatatype value) {
        this.type = value;
    }

    /**
     * Gets the value of the subtype property.
     * 
     * @return
     *     possible object is
     *     {@link SdfDatatypeInformation }
     *     
     */
    public SdfDatatypeInformation getSubtype() {
        return subtype;
    }

    /**
     * Sets the value of the subtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link SdfDatatypeInformation }
     *     
     */
    public void setSubtype(SdfDatatypeInformation value) {
        this.subtype = value;
    }

    /**
     * Gets the value of the subSchema property.
     * 
     * @return
     *     possible object is
     *     {@link SdfSchemaInformation }
     *     
     */
    public SdfSchemaInformation getSubSchema() {
        return subSchema;
    }

    /**
     * Sets the value of the subSchema property.
     * 
     * @param value
     *     allowed object is
     *     {@link SdfSchemaInformation }
     *     
     */
    public void setSubSchema(SdfSchemaInformation value) {
        this.subSchema = value;
    }

}
