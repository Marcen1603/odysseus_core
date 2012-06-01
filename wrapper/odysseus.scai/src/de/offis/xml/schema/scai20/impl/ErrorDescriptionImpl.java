/*
 * XML Type:  ErrorDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.ErrorDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML ErrorDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class ErrorDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.ErrorDescription
{
    private static final long serialVersionUID = 1L;
    
    public ErrorDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SENSORDEAD$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorDead");
    private static final javax.xml.namespace.QName NAN$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "NAN");
    private static final javax.xml.namespace.QName BATTERYDEAD$4 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "BatteryDead");
    private static final javax.xml.namespace.QName UNKNOWN$6 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Unknown");
    
    
    /**
     * Gets the "SensorDead" element
     */
    public java.lang.String getSensorDead()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORDEAD$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "SensorDead" element
     */
    public org.apache.xmlbeans.XmlString xgetSensorDead()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORDEAD$0, 0);
            return target;
        }
    }
    
    /**
     * True if has "SensorDead" element
     */
    public boolean isSetSensorDead()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SENSORDEAD$0) != 0;
        }
    }
    
    /**
     * Sets the "SensorDead" element
     */
    public void setSensorDead(java.lang.String sensorDead)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORDEAD$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SENSORDEAD$0);
            }
            target.setStringValue(sensorDead);
        }
    }
    
    /**
     * Sets (as xml) the "SensorDead" element
     */
    public void xsetSensorDead(org.apache.xmlbeans.XmlString sensorDead)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORDEAD$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SENSORDEAD$0);
            }
            target.set(sensorDead);
        }
    }
    
    /**
     * Unsets the "SensorDead" element
     */
    public void unsetSensorDead()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SENSORDEAD$0, 0);
        }
    }
    
    /**
     * Gets the "NAN" element
     */
    public java.lang.String getNAN()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAN$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "NAN" element
     */
    public org.apache.xmlbeans.XmlString xgetNAN()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAN$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "NAN" element
     */
    public boolean isSetNAN()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(NAN$2) != 0;
        }
    }
    
    /**
     * Sets the "NAN" element
     */
    public void setNAN(java.lang.String nan)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAN$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NAN$2);
            }
            target.setStringValue(nan);
        }
    }
    
    /**
     * Sets (as xml) the "NAN" element
     */
    public void xsetNAN(org.apache.xmlbeans.XmlString nan)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAN$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NAN$2);
            }
            target.set(nan);
        }
    }
    
    /**
     * Unsets the "NAN" element
     */
    public void unsetNAN()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(NAN$2, 0);
        }
    }
    
    /**
     * Gets the "BatteryDead" element
     */
    public java.lang.String getBatteryDead()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BATTERYDEAD$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "BatteryDead" element
     */
    public org.apache.xmlbeans.XmlString xgetBatteryDead()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BATTERYDEAD$4, 0);
            return target;
        }
    }
    
    /**
     * True if has "BatteryDead" element
     */
    public boolean isSetBatteryDead()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(BATTERYDEAD$4) != 0;
        }
    }
    
    /**
     * Sets the "BatteryDead" element
     */
    public void setBatteryDead(java.lang.String batteryDead)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BATTERYDEAD$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(BATTERYDEAD$4);
            }
            target.setStringValue(batteryDead);
        }
    }
    
    /**
     * Sets (as xml) the "BatteryDead" element
     */
    public void xsetBatteryDead(org.apache.xmlbeans.XmlString batteryDead)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BATTERYDEAD$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(BATTERYDEAD$4);
            }
            target.set(batteryDead);
        }
    }
    
    /**
     * Unsets the "BatteryDead" element
     */
    public void unsetBatteryDead()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(BATTERYDEAD$4, 0);
        }
    }
    
    /**
     * Gets the "Unknown" element
     */
    public java.lang.String getUnknown()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UNKNOWN$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Unknown" element
     */
    public org.apache.xmlbeans.XmlString xgetUnknown()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(UNKNOWN$6, 0);
            return target;
        }
    }
    
    /**
     * True if has "Unknown" element
     */
    public boolean isSetUnknown()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(UNKNOWN$6) != 0;
        }
    }
    
    /**
     * Sets the "Unknown" element
     */
    public void setUnknown(java.lang.String unknown)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UNKNOWN$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(UNKNOWN$6);
            }
            target.setStringValue(unknown);
        }
    }
    
    /**
     * Sets (as xml) the "Unknown" element
     */
    public void xsetUnknown(org.apache.xmlbeans.XmlString unknown)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(UNKNOWN$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(UNKNOWN$6);
            }
            target.set(unknown);
        }
    }
    
    /**
     * Unsets the "Unknown" element
     */
    public void unsetUnknown()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(UNKNOWN$6, 0);
        }
    }
}
