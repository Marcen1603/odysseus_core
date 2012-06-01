/*
 * XML Type:  ModulesDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.ModulesDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML ModulesDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class ModulesDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.ModulesDescription
{
    private static final long serialVersionUID = 1L;
    
    public ModulesDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ACCESSCONTROL$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "AccessControl");
    private static final javax.xml.namespace.QName IDENTIFICATION$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Identification");
    private static final javax.xml.namespace.QName MEASUREMENTS$4 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Measurements");
    private static final javax.xml.namespace.QName SENSORCONTROL$6 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorControl");
    private static final javax.xml.namespace.QName SENSORREGISTRYCONTROL$8 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorRegistryControl");
    
    
    /**
     * Gets the "AccessControl" element
     */
    public boolean getAccessControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ACCESSCONTROL$0, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "AccessControl" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetAccessControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(ACCESSCONTROL$0, 0);
            return target;
        }
    }
    
    /**
     * True if has "AccessControl" element
     */
    public boolean isSetAccessControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ACCESSCONTROL$0) != 0;
        }
    }
    
    /**
     * Sets the "AccessControl" element
     */
    public void setAccessControl(boolean accessControl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ACCESSCONTROL$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ACCESSCONTROL$0);
            }
            target.setBooleanValue(accessControl);
        }
    }
    
    /**
     * Sets (as xml) the "AccessControl" element
     */
    public void xsetAccessControl(org.apache.xmlbeans.XmlBoolean accessControl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(ACCESSCONTROL$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(ACCESSCONTROL$0);
            }
            target.set(accessControl);
        }
    }
    
    /**
     * Unsets the "AccessControl" element
     */
    public void unsetAccessControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ACCESSCONTROL$0, 0);
        }
    }
    
    /**
     * Gets the "Identification" element
     */
    public boolean getIdentification()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDENTIFICATION$2, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "Identification" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetIdentification()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(IDENTIFICATION$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "Identification" element
     */
    public boolean isSetIdentification()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(IDENTIFICATION$2) != 0;
        }
    }
    
    /**
     * Sets the "Identification" element
     */
    public void setIdentification(boolean identification)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDENTIFICATION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(IDENTIFICATION$2);
            }
            target.setBooleanValue(identification);
        }
    }
    
    /**
     * Sets (as xml) the "Identification" element
     */
    public void xsetIdentification(org.apache.xmlbeans.XmlBoolean identification)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(IDENTIFICATION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(IDENTIFICATION$2);
            }
            target.set(identification);
        }
    }
    
    /**
     * Unsets the "Identification" element
     */
    public void unsetIdentification()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(IDENTIFICATION$2, 0);
        }
    }
    
    /**
     * Gets the "Measurements" element
     */
    public boolean getMeasurements()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MEASUREMENTS$4, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "Measurements" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetMeasurements()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(MEASUREMENTS$4, 0);
            return target;
        }
    }
    
    /**
     * True if has "Measurements" element
     */
    public boolean isSetMeasurements()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(MEASUREMENTS$4) != 0;
        }
    }
    
    /**
     * Sets the "Measurements" element
     */
    public void setMeasurements(boolean measurements)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MEASUREMENTS$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MEASUREMENTS$4);
            }
            target.setBooleanValue(measurements);
        }
    }
    
    /**
     * Sets (as xml) the "Measurements" element
     */
    public void xsetMeasurements(org.apache.xmlbeans.XmlBoolean measurements)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(MEASUREMENTS$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(MEASUREMENTS$4);
            }
            target.set(measurements);
        }
    }
    
    /**
     * Unsets the "Measurements" element
     */
    public void unsetMeasurements()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(MEASUREMENTS$4, 0);
        }
    }
    
    /**
     * Gets the "SensorControl" element
     */
    public boolean getSensorControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORCONTROL$6, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "SensorControl" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetSensorControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(SENSORCONTROL$6, 0);
            return target;
        }
    }
    
    /**
     * True if has "SensorControl" element
     */
    public boolean isSetSensorControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SENSORCONTROL$6) != 0;
        }
    }
    
    /**
     * Sets the "SensorControl" element
     */
    public void setSensorControl(boolean sensorControl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORCONTROL$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SENSORCONTROL$6);
            }
            target.setBooleanValue(sensorControl);
        }
    }
    
    /**
     * Sets (as xml) the "SensorControl" element
     */
    public void xsetSensorControl(org.apache.xmlbeans.XmlBoolean sensorControl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(SENSORCONTROL$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(SENSORCONTROL$6);
            }
            target.set(sensorControl);
        }
    }
    
    /**
     * Unsets the "SensorControl" element
     */
    public void unsetSensorControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SENSORCONTROL$6, 0);
        }
    }
    
    /**
     * Gets the "SensorRegistryControl" element
     */
    public boolean getSensorRegistryControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORREGISTRYCONTROL$8, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "SensorRegistryControl" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetSensorRegistryControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(SENSORREGISTRYCONTROL$8, 0);
            return target;
        }
    }
    
    /**
     * True if has "SensorRegistryControl" element
     */
    public boolean isSetSensorRegistryControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SENSORREGISTRYCONTROL$8) != 0;
        }
    }
    
    /**
     * Sets the "SensorRegistryControl" element
     */
    public void setSensorRegistryControl(boolean sensorRegistryControl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORREGISTRYCONTROL$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SENSORREGISTRYCONTROL$8);
            }
            target.setBooleanValue(sensorRegistryControl);
        }
    }
    
    /**
     * Sets (as xml) the "SensorRegistryControl" element
     */
    public void xsetSensorRegistryControl(org.apache.xmlbeans.XmlBoolean sensorRegistryControl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(SENSORREGISTRYCONTROL$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(SENSORREGISTRYCONTROL$8);
            }
            target.set(sensorRegistryControl);
        }
    }
    
    /**
     * Unsets the "SensorRegistryControl" element
     */
    public void unsetSensorRegistryControl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SENSORREGISTRYCONTROL$8, 0);
        }
    }
}
