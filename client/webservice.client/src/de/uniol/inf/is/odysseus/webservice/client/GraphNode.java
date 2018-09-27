
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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r graphNode complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="graphNode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="childs" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}graphNode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="childsMap">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}graphNode" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="className" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hash" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="open" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="outputSchema" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}sdfSchemaInformation" minOccurs="0"/>
 *         &lt;element name="ownerIDs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parameterInfos">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="pipe" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="sink" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "graphNode", propOrder = {
    "childs",
    "childsMap",
    "className",
    "hash",
    "id",
    "name",
    "open",
    "outputSchema",
    "ownerIDs",
    "parameterInfos",
    "pipe",
    "sink",
    "source"
})
@SuppressWarnings(value = { "all" })
public class GraphNode {

    @XmlElement(nillable = true)
    protected List<GraphNode> childs;
    @XmlElement(required = true)
    protected GraphNode.ChildsMap childsMap;
    protected String className;
    protected int hash;
    protected int id;
    protected String name;
    protected boolean open;
    protected SdfSchemaInformation outputSchema;
    protected String ownerIDs;
    @XmlElement(required = true)
    protected GraphNode.ParameterInfos parameterInfos;
    protected boolean pipe;
    protected boolean sink;
    protected boolean source;

    /**
     * Gets the value of the childs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the childs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChilds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GraphNode }
     * 
     * 
     */
    public List<GraphNode> getChilds() {
        if (childs == null) {
            childs = new ArrayList<GraphNode>();
        }
        return this.childs;
    }

    /**
     * Ruft den Wert der childsMap-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GraphNode.ChildsMap }
     *     
     */
    public GraphNode.ChildsMap getChildsMap() {
        return childsMap;
    }

    /**
     * Legt den Wert der childsMap-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GraphNode.ChildsMap }
     *     
     */
    public void setChildsMap(GraphNode.ChildsMap value) {
        this.childsMap = value;
    }

    /**
     * Ruft den Wert der className-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassName() {
        return className;
    }

    /**
     * Legt den Wert der className-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassName(String value) {
        this.className = value;
    }

    /**
     * Ruft den Wert der hash-Eigenschaft ab.
     * 
     */
    public int getHash() {
        return hash;
    }

    /**
     * Legt den Wert der hash-Eigenschaft fest.
     * 
     */
    public void setHash(int value) {
        this.hash = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

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
     * Ruft den Wert der open-Eigenschaft ab.
     * 
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Legt den Wert der open-Eigenschaft fest.
     * 
     */
    public void setOpen(boolean value) {
        this.open = value;
    }

    /**
     * Ruft den Wert der outputSchema-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SdfSchemaInformation }
     *     
     */
    public SdfSchemaInformation getOutputSchema() {
        return outputSchema;
    }

    /**
     * Legt den Wert der outputSchema-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SdfSchemaInformation }
     *     
     */
    public void setOutputSchema(SdfSchemaInformation value) {
        this.outputSchema = value;
    }

    /**
     * Ruft den Wert der ownerIDs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerIDs() {
        return ownerIDs;
    }

    /**
     * Legt den Wert der ownerIDs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerIDs(String value) {
        this.ownerIDs = value;
    }

    /**
     * Ruft den Wert der parameterInfos-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GraphNode.ParameterInfos }
     *     
     */
    public GraphNode.ParameterInfos getParameterInfos() {
        return parameterInfos;
    }

    /**
     * Legt den Wert der parameterInfos-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GraphNode.ParameterInfos }
     *     
     */
    public void setParameterInfos(GraphNode.ParameterInfos value) {
        this.parameterInfos = value;
    }

    /**
     * Ruft den Wert der pipe-Eigenschaft ab.
     * 
     */
    public boolean isPipe() {
        return pipe;
    }

    /**
     * Legt den Wert der pipe-Eigenschaft fest.
     * 
     */
    public void setPipe(boolean value) {
        this.pipe = value;
    }

    /**
     * Ruft den Wert der sink-Eigenschaft ab.
     * 
     */
    public boolean isSink() {
        return sink;
    }

    /**
     * Legt den Wert der sink-Eigenschaft fest.
     * 
     */
    public void setSink(boolean value) {
        this.sink = value;
    }

    /**
     * Ruft den Wert der source-Eigenschaft ab.
     * 
     */
    public boolean isSource() {
        return source;
    }

    /**
     * Legt den Wert der source-Eigenschaft fest.
     * 
     */
    public void setSource(boolean value) {
        this.source = value;
    }


    /**
     * <p>Java-Klasse f�r anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}graphNode" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class ChildsMap {

        protected List<GraphNode.ChildsMap.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GraphNode.ChildsMap.Entry }
         * 
         * 
         */
        public List<GraphNode.ChildsMap.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<GraphNode.ChildsMap.Entry>();
            }
            return this.entry;
        }


        /**
         * <p>Java-Klasse f�r anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}graphNode" minOccurs="0"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Entry {

            protected GraphNode key;
            protected Integer value;

            /**
             * Ruft den Wert der key-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link GraphNode }
             *     
             */
            public GraphNode getKey() {
                return key;
            }

            /**
             * Legt den Wert der key-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link GraphNode }
             *     
             */
            public void setKey(GraphNode value) {
                this.key = value;
            }

            /**
             * Ruft den Wert der value-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getValue() {
                return value;
            }

            /**
             * Legt den Wert der value-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setValue(Integer value) {
                this.value = value;
            }

        }

    }


    /**
     * <p>Java-Klasse f�r anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class ParameterInfos {

        protected List<GraphNode.ParameterInfos.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GraphNode.ParameterInfos.Entry }
         * 
         * 
         */
        public List<GraphNode.ParameterInfos.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<GraphNode.ParameterInfos.Entry>();
            }
            return this.entry;
        }


        /**
         * <p>Java-Klasse f�r anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Entry {

            protected String key;
            protected String value;

            /**
             * Ruft den Wert der key-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getKey() {
                return key;
            }

            /**
             * Legt den Wert der key-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setKey(String value) {
                this.key = value;
            }

            /**
             * Ruft den Wert der value-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Legt den Wert der value-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

        }

    }

}
