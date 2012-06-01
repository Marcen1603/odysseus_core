/*
 * XML Type:  AccessorDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.AccessorDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML AccessorDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class AccessorDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.AccessorDescription
{
    private static final long serialVersionUID = 1L;
    
    public AccessorDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName USER$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "User");
    private static final javax.xml.namespace.QName SENSORDOMAIN$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorDomain");
    
    
    /**
     * Gets the "User" element
     */
    public de.offis.xml.schema.scai20.Reference getUser()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(USER$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "User" element
     */
    public boolean isSetUser()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(USER$0) != 0;
        }
    }
    
    /**
     * Sets the "User" element
     */
    public void setUser(de.offis.xml.schema.scai20.Reference user)
    {
        generatedSetterHelperImpl(user, USER$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "User" element
     */
    public de.offis.xml.schema.scai20.Reference addNewUser()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(USER$0);
            return target;
        }
    }
    
    /**
     * Unsets the "User" element
     */
    public void unsetUser()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(USER$0, 0);
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
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(SENSORDOMAIN$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "SensorDomain" element
     */
    public boolean isSetSensorDomain()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SENSORDOMAIN$2) != 0;
        }
    }
    
    /**
     * Sets the "SensorDomain" element
     */
    public void setSensorDomain(de.offis.xml.schema.scai20.Reference sensorDomain)
    {
        generatedSetterHelperImpl(sensorDomain, SENSORDOMAIN$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
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
            target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(SENSORDOMAIN$2);
            return target;
        }
    }
    
    /**
     * Unsets the "SensorDomain" element
     */
    public void unsetSensorDomain()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SENSORDOMAIN$2, 0);
        }
    }
}
