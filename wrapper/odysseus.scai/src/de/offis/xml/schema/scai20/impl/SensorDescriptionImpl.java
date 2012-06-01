/*
 * XML Type:  SensorDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.SensorDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML SensorDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class SensorDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.SensorDescription
{
    private static final long serialVersionUID = 1L;
    
    public SensorDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "name");
    private static final javax.xml.namespace.QName VIRTUAL$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "virtual");
    private static final javax.xml.namespace.QName SENSORDOMAIN$4 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorDomain");
    private static final javax.xml.namespace.QName SENSORTYPE$6 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorType");
    
    
    /**
     * Gets the "name" element
     */
    public java.lang.String getName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "name" element
     */
    public org.apache.xmlbeans.XmlString xgetName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "name" element
     */
    public void setName(java.lang.String name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NAME$0);
            }
            target.setStringValue(name);
        }
    }
    
    /**
     * Sets (as xml) the "name" element
     */
    public void xsetName(org.apache.xmlbeans.XmlString name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NAME$0);
            }
            target.set(name);
        }
    }
    
    /**
     * Gets the "virtual" element
     */
    public boolean getVirtual()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VIRTUAL$2, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "virtual" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetVirtual()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(VIRTUAL$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "virtual" element
     */
    public void setVirtual(boolean virtual)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VIRTUAL$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VIRTUAL$2);
            }
            target.setBooleanValue(virtual);
        }
    }
    
    /**
     * Sets (as xml) the "virtual" element
     */
    public void xsetVirtual(org.apache.xmlbeans.XmlBoolean virtual)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(VIRTUAL$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(VIRTUAL$2);
            }
            target.set(virtual);
        }
    }
    
    /**
     * Gets the "SensorDomain" element
     */
    public de.offis.xml.schema.scai20.Reference getSensorDomain()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(SENSORDOMAIN$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "SensorDomain" element
     */
    public void setSensorDomain(de.offis.xml.schema.scai20.Reference sensorDomain)
    {
        generatedSetterHelperImpl(sensorDomain, SENSORDOMAIN$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "SensorDomain" element
     */
    public de.offis.xml.schema.scai20.Reference addNewSensorDomain()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(SENSORDOMAIN$4);
            return target;
        }
    }
    
    /**
     * Gets the "SensorType" element
     */
    public de.offis.xml.schema.scai20.Reference getSensorType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(SENSORTYPE$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "SensorType" element
     */
    public void setSensorType(de.offis.xml.schema.scai20.Reference sensorType)
    {
        generatedSetterHelperImpl(sensorType, SENSORTYPE$6, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "SensorType" element
     */
    public de.offis.xml.schema.scai20.Reference addNewSensorType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(SENSORTYPE$6);
            return target;
        }
    }
}
