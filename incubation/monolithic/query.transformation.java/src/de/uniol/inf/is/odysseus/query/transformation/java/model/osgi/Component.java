package de.uniol.inf.is.odysseus.query.transformation.java.model.osgi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="implementation" form="unqualified">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="reference" form="unqualified">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="bind" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="cardinality" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="interface" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="policy" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="unbind" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "implementation",
    "reference",
    "service"
})
@XmlRootElement(name = "component")
public class Component {

    @XmlElement(required = true)
    protected Component.Implementation implementation;
    @XmlElement(required = false)
    protected Component.Reference reference;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlElement(required = false)
    protected Component.Service service;

    /**
     * Gets the value of the implementation property.
     * 
     * @return
     *     possible object is
     *     {@link Component.Implementation }
     *     
     */
    public Component.Implementation getImplementation() {
        return implementation;
    }

    /**
     * Sets the value of the implementation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Component.Implementation }
     *     
     */
    public void setImplementation(Component.Implementation value) {
        this.implementation = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link Component.Reference }
     *     
     */
    public Component.Reference getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link Component.Reference }
     *     
     */
    public void setReference(Component.Reference value) {
        this.reference = value;
    }

    /**
     * Gets the value of the name property.
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
     * Sets the value of the name property.
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
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link Component.Service }
     *     
     */
    public Component.Service getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link Component.Service }
     *     
     */
    public void setService(Component.Service value) {
        this.service = value;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Implementation {

        @XmlAttribute(name = "class")
        protected String clazz;

        /**
         * Gets the value of the clazz property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getClazz() {
            return clazz;
        }

        /**
         * Sets the value of the clazz property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setClazz(String value) {
            this.clazz = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="bind" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="cardinality" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="interface" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="policy" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="unbind" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Reference {

        @XmlAttribute(name = "bind")
        protected String bind;
        @XmlAttribute(name = "cardinality")
        protected String cardinality;
        @XmlAttribute(name = "interface")
        protected String _interface;
        @XmlAttribute(name = "policy")
        protected String policy;
        @XmlAttribute(name = "unbind")
        protected String unbind;

        /**
         * Gets the value of the bind property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBind() {
            return bind;
        }

        /**
         * Sets the value of the bind property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBind(String value) {
            this.bind = value;
        }

        /**
         * Gets the value of the cardinality property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCardinality() {
            return cardinality;
        }

        /**
         * Sets the value of the cardinality property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCardinality(String value) {
            this.cardinality = value;
        }

        /**
         * Gets the value of the interface property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getInterface() {
            return _interface;
        }

        /**
         * Sets the value of the interface property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setInterface(String value) {
            this._interface = value;
        }

        /**
         * Gets the value of the policy property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPolicy() {
            return policy;
        }

        /**
         * Sets the value of the policy property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPolicy(String value) {
            this.policy = value;
        }

        /**
         * Gets the value of the unbind property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUnbind() {
            return unbind;
        }

        /**
         * Sets the value of the unbind property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUnbind(String value) {
            this.unbind = value;
        }

    }
    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "provide"
    })
    public static class Service {

        protected List<Component.Service.Provide> provide;

        /**
         * Gets the value of the provide property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the provide property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProvide().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Component.Service.Provide }
         * 
         * 
         */
        public List<Component.Service.Provide> getProvide() {
            if (provide == null) {
                provide = new ArrayList<Component.Service.Provide>();
            }
            return this.provide;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="interface" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Provide {

            @XmlAttribute(name = "interface")
            protected String _interface;

            /**
             * Gets the value of the interface property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getInterface() {
                return _interface;
            }

            /**
             * Sets the value of the interface property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setInterface(String value) {
                this._interface = value;
            }

        }

    }

}
