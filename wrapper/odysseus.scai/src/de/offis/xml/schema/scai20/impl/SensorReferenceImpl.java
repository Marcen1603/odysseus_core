/*
 * XML Type:  SensorReference
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.SensorReference
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML SensorReference(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class SensorReferenceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.SensorReference
{
    private static final long serialVersionUID = 1L;
    
    public SensorReferenceImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SENSORID$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorID");
    private static final javax.xml.namespace.QName NAMEREFERENCE$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "NameReference");
    
    
    /**
     * Gets the "SensorID" element
     */
    public java.lang.String getSensorID()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORID$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "SensorID" element
     */
    public org.apache.xmlbeans.XmlString xgetSensorID()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORID$0, 0);
            return target;
        }
    }
    
    /**
     * True if has "SensorID" element
     */
    public boolean isSetSensorID()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SENSORID$0) != 0;
        }
    }
    
    /**
     * Sets the "SensorID" element
     */
    public void setSensorID(java.lang.String sensorID)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORID$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SENSORID$0);
            }
            target.setStringValue(sensorID);
        }
    }
    
    /**
     * Sets (as xml) the "SensorID" element
     */
    public void xsetSensorID(org.apache.xmlbeans.XmlString sensorID)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORID$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SENSORID$0);
            }
            target.set(sensorID);
        }
    }
    
    /**
     * Unsets the "SensorID" element
     */
    public void unsetSensorID()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SENSORID$0, 0);
        }
    }
    
    /**
     * Gets the "NameReference" element
     */
    public de.offis.xml.schema.scai20.SensorReference.NameReference getNameReference()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.SensorReference.NameReference target = null;
            target = (de.offis.xml.schema.scai20.SensorReference.NameReference)get_store().find_element_user(NAMEREFERENCE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "NameReference" element
     */
    public boolean isSetNameReference()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(NAMEREFERENCE$2) != 0;
        }
    }
    
    /**
     * Sets the "NameReference" element
     */
    public void setNameReference(de.offis.xml.schema.scai20.SensorReference.NameReference nameReference)
    {
        generatedSetterHelperImpl(nameReference, NAMEREFERENCE$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "NameReference" element
     */
    public de.offis.xml.schema.scai20.SensorReference.NameReference addNewNameReference()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.SensorReference.NameReference target = null;
            target = (de.offis.xml.schema.scai20.SensorReference.NameReference)get_store().add_element_user(NAMEREFERENCE$2);
            return target;
        }
    }
    
    /**
     * Unsets the "NameReference" element
     */
    public void unsetNameReference()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(NAMEREFERENCE$2, 0);
        }
    }
    /**
     * An XML NameReference(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class NameReferenceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.SensorReference.NameReference
    {
        private static final long serialVersionUID = 1L;
        
        public NameReferenceImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName SENSORNAME$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorName");
        private static final javax.xml.namespace.QName SENSORDOMAINNAME$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorDomainName");
        
        
        /**
         * Gets the "SensorName" element
         */
        public java.lang.String getSensorName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORNAME$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "SensorName" element
         */
        public org.apache.xmlbeans.XmlString xgetSensorName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORNAME$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "SensorName" element
         */
        public void setSensorName(java.lang.String sensorName)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORNAME$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SENSORNAME$0);
                }
                target.setStringValue(sensorName);
            }
        }
        
        /**
         * Sets (as xml) the "SensorName" element
         */
        public void xsetSensorName(org.apache.xmlbeans.XmlString sensorName)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORNAME$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SENSORNAME$0);
                }
                target.set(sensorName);
            }
        }
        
        /**
         * Gets the "SensorDomainName" element
         */
        public java.lang.String getSensorDomainName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORDOMAINNAME$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "SensorDomainName" element
         */
        public org.apache.xmlbeans.XmlString xgetSensorDomainName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORDOMAINNAME$2, 0);
                return target;
            }
        }
        
        /**
         * Sets the "SensorDomainName" element
         */
        public void setSensorDomainName(java.lang.String sensorDomainName)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORDOMAINNAME$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SENSORDOMAINNAME$2);
                }
                target.setStringValue(sensorDomainName);
            }
        }
        
        /**
         * Sets (as xml) the "SensorDomainName" element
         */
        public void xsetSensorDomainName(org.apache.xmlbeans.XmlString sensorDomainName)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORDOMAINNAME$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SENSORDOMAINNAME$2);
                }
                target.set(sensorDomainName);
            }
        }
    }
}
