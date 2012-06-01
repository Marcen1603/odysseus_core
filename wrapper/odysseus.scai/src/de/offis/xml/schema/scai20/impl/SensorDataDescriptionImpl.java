/*
 * XML Type:  SensorDataDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.SensorDataDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML SensorDataDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class SensorDataDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.SensorDataDescription
{
    private static final long serialVersionUID = 1L;
    
    public SensorDataDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIMESTAMP$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "timeStamp");
    private static final javax.xml.namespace.QName SENSORNAME$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorName");
    private static final javax.xml.namespace.QName SENSORDOMAINNAME$4 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorDomainName");
    private static final javax.xml.namespace.QName OLDERVALUES$6 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "olderValues");
    private static final javax.xml.namespace.QName DATASTREAMELEMENT$8 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "DataStreamElement");
    
    
    /**
     * Gets the "timeStamp" element
     */
    public java.util.Calendar getTimeStamp()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIMESTAMP$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    /**
     * Gets (as xml) the "timeStamp" element
     */
    public org.apache.xmlbeans.XmlDateTime xgetTimeStamp()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(TIMESTAMP$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "timeStamp" element
     */
    public void setTimeStamp(java.util.Calendar timeStamp)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIMESTAMP$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TIMESTAMP$0);
            }
            target.setCalendarValue(timeStamp);
        }
    }
    
    /**
     * Sets (as xml) the "timeStamp" element
     */
    public void xsetTimeStamp(org.apache.xmlbeans.XmlDateTime timeStamp)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(TIMESTAMP$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDateTime)get_store().add_element_user(TIMESTAMP$0);
            }
            target.set(timeStamp);
        }
    }
    
    /**
     * Gets the "SensorName" element
     */
    public java.lang.String getSensorName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORNAME$2, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORNAME$2, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORNAME$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SENSORNAME$2);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORNAME$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SENSORNAME$2);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORDOMAINNAME$4, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORDOMAINNAME$4, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENSORDOMAINNAME$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SENSORDOMAINNAME$4);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENSORDOMAINNAME$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SENSORDOMAINNAME$4);
            }
            target.set(sensorDomainName);
        }
    }
    
    /**
     * Gets the "olderValues" element
     */
    public int getOlderValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(OLDERVALUES$6, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "olderValues" element
     */
    public org.apache.xmlbeans.XmlInt xgetOlderValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(OLDERVALUES$6, 0);
            return target;
        }
    }
    
    /**
     * True if has "olderValues" element
     */
    public boolean isSetOlderValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(OLDERVALUES$6) != 0;
        }
    }
    
    /**
     * Sets the "olderValues" element
     */
    public void setOlderValues(int olderValues)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(OLDERVALUES$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(OLDERVALUES$6);
            }
            target.setIntValue(olderValues);
        }
    }
    
    /**
     * Sets (as xml) the "olderValues" element
     */
    public void xsetOlderValues(org.apache.xmlbeans.XmlInt olderValues)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(OLDERVALUES$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(OLDERVALUES$6);
            }
            target.set(olderValues);
        }
    }
    
    /**
     * Unsets the "olderValues" element
     */
    public void unsetOlderValues()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(OLDERVALUES$6, 0);
        }
    }
    
    /**
     * Gets array of all "DataStreamElement" elements
     */
    public de.offis.xml.schema.scai20.DataElementValueDescription[] getDataStreamElementArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DATASTREAMELEMENT$8, targetList);
            de.offis.xml.schema.scai20.DataElementValueDescription[] result = new de.offis.xml.schema.scai20.DataElementValueDescription[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "DataStreamElement" element
     */
    public de.offis.xml.schema.scai20.DataElementValueDescription getDataStreamElementArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataElementValueDescription target = null;
            target = (de.offis.xml.schema.scai20.DataElementValueDescription)get_store().find_element_user(DATASTREAMELEMENT$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "DataStreamElement" element
     */
    public int sizeOfDataStreamElementArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DATASTREAMELEMENT$8);
        }
    }
    
    /**
     * Sets array of all "DataStreamElement" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setDataStreamElementArray(de.offis.xml.schema.scai20.DataElementValueDescription[] dataStreamElementArray)
    {
        check_orphaned();
        arraySetterHelper(dataStreamElementArray, DATASTREAMELEMENT$8);
    }
    
    /**
     * Sets ith "DataStreamElement" element
     */
    public void setDataStreamElementArray(int i, de.offis.xml.schema.scai20.DataElementValueDescription dataStreamElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataElementValueDescription target = null;
            target = (de.offis.xml.schema.scai20.DataElementValueDescription)get_store().find_element_user(DATASTREAMELEMENT$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(dataStreamElement);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "DataStreamElement" element
     */
    public de.offis.xml.schema.scai20.DataElementValueDescription insertNewDataStreamElement(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataElementValueDescription target = null;
            target = (de.offis.xml.schema.scai20.DataElementValueDescription)get_store().insert_element_user(DATASTREAMELEMENT$8, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "DataStreamElement" element
     */
    public de.offis.xml.schema.scai20.DataElementValueDescription addNewDataStreamElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataElementValueDescription target = null;
            target = (de.offis.xml.schema.scai20.DataElementValueDescription)get_store().add_element_user(DATASTREAMELEMENT$8);
            return target;
        }
    }
    
    /**
     * Removes the ith "DataStreamElement" element
     */
    public void removeDataStreamElement(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DATASTREAMELEMENT$8, i);
        }
    }
}
