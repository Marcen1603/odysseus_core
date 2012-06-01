/*
 * XML Type:  SensorTypeDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.SensorTypeDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML SensorTypeDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class SensorTypeDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.SensorTypeDescription
{
    private static final long serialVersionUID = 1L;
    
    public SensorTypeDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "name");
    private static final javax.xml.namespace.QName ADAPTER$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "adapter");
    private static final javax.xml.namespace.QName DATASTREAMTYPE$4 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "DataStreamType");
    private static final javax.xml.namespace.QName CONFIGURATIONPARAMETER$6 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ConfigurationParameter");
    private static final javax.xml.namespace.QName SENSORCATEGORY$8 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorCategory");
    private static final javax.xml.namespace.QName SENSORDOMAIN$10 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SensorDomain");
    
    
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
     * Gets the "adapter" element
     */
    public java.lang.String getAdapter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ADAPTER$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "adapter" element
     */
    public org.apache.xmlbeans.XmlString xgetAdapter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ADAPTER$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "adapter" element
     */
    public void setAdapter(java.lang.String adapter)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ADAPTER$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ADAPTER$2);
            }
            target.setStringValue(adapter);
        }
    }
    
    /**
     * Sets (as xml) the "adapter" element
     */
    public void xsetAdapter(org.apache.xmlbeans.XmlString adapter)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ADAPTER$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ADAPTER$2);
            }
            target.set(adapter);
        }
    }
    
    /**
     * Gets the "DataStreamType" element
     */
    public de.offis.xml.schema.scai20.Reference getDataStreamType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(DATASTREAMTYPE$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "DataStreamType" element
     */
    public void setDataStreamType(de.offis.xml.schema.scai20.Reference dataStreamType)
    {
        generatedSetterHelperImpl(dataStreamType, DATASTREAMTYPE$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "DataStreamType" element
     */
    public de.offis.xml.schema.scai20.Reference addNewDataStreamType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(DATASTREAMTYPE$4);
            return target;
        }
    }
    
    /**
     * Gets array of all "ConfigurationParameter" elements
     */
    public de.offis.xml.schema.scai20.Reference[] getConfigurationParameterArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CONFIGURATIONPARAMETER$6, targetList);
            de.offis.xml.schema.scai20.Reference[] result = new de.offis.xml.schema.scai20.Reference[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "ConfigurationParameter" element
     */
    public de.offis.xml.schema.scai20.Reference getConfigurationParameterArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(CONFIGURATIONPARAMETER$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "ConfigurationParameter" element
     */
    public int sizeOfConfigurationParameterArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CONFIGURATIONPARAMETER$6);
        }
    }
    
    /**
     * Sets array of all "ConfigurationParameter" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setConfigurationParameterArray(de.offis.xml.schema.scai20.Reference[] configurationParameterArray)
    {
        check_orphaned();
        arraySetterHelper(configurationParameterArray, CONFIGURATIONPARAMETER$6);
    }
    
    /**
     * Sets ith "ConfigurationParameter" element
     */
    public void setConfigurationParameterArray(int i, de.offis.xml.schema.scai20.Reference configurationParameter)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(CONFIGURATIONPARAMETER$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(configurationParameter);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "ConfigurationParameter" element
     */
    public de.offis.xml.schema.scai20.Reference insertNewConfigurationParameter(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().insert_element_user(CONFIGURATIONPARAMETER$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "ConfigurationParameter" element
     */
    public de.offis.xml.schema.scai20.Reference addNewConfigurationParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(CONFIGURATIONPARAMETER$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "ConfigurationParameter" element
     */
    public void removeConfigurationParameter(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CONFIGURATIONPARAMETER$6, i);
        }
    }
    
    /**
     * Gets array of all "SensorCategory" elements
     */
    public de.offis.xml.schema.scai20.Reference[] getSensorCategoryArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(SENSORCATEGORY$8, targetList);
            de.offis.xml.schema.scai20.Reference[] result = new de.offis.xml.schema.scai20.Reference[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "SensorCategory" element
     */
    public de.offis.xml.schema.scai20.Reference getSensorCategoryArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(SENSORCATEGORY$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "SensorCategory" element
     */
    public int sizeOfSensorCategoryArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SENSORCATEGORY$8);
        }
    }
    
    /**
     * Sets array of all "SensorCategory" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setSensorCategoryArray(de.offis.xml.schema.scai20.Reference[] sensorCategoryArray)
    {
        check_orphaned();
        arraySetterHelper(sensorCategoryArray, SENSORCATEGORY$8);
    }
    
    /**
     * Sets ith "SensorCategory" element
     */
    public void setSensorCategoryArray(int i, de.offis.xml.schema.scai20.Reference sensorCategory)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(SENSORCATEGORY$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(sensorCategory);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "SensorCategory" element
     */
    public de.offis.xml.schema.scai20.Reference insertNewSensorCategory(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().insert_element_user(SENSORCATEGORY$8, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "SensorCategory" element
     */
    public de.offis.xml.schema.scai20.Reference addNewSensorCategory()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(SENSORCATEGORY$8);
            return target;
        }
    }
    
    /**
     * Removes the ith "SensorCategory" element
     */
    public void removeSensorCategory(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SENSORCATEGORY$8, i);
        }
    }
    
    /**
     * Gets array of all "SensorDomain" elements
     */
    public de.offis.xml.schema.scai20.Reference[] getSensorDomainArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(SENSORDOMAIN$10, targetList);
            de.offis.xml.schema.scai20.Reference[] result = new de.offis.xml.schema.scai20.Reference[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "SensorDomain" element
     */
    public de.offis.xml.schema.scai20.Reference getSensorDomainArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(SENSORDOMAIN$10, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "SensorDomain" element
     */
    public int sizeOfSensorDomainArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SENSORDOMAIN$10);
        }
    }
    
    /**
     * Sets array of all "SensorDomain" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setSensorDomainArray(de.offis.xml.schema.scai20.Reference[] sensorDomainArray)
    {
        check_orphaned();
        arraySetterHelper(sensorDomainArray, SENSORDOMAIN$10);
    }
    
    /**
     * Sets ith "SensorDomain" element
     */
    public void setSensorDomainArray(int i, de.offis.xml.schema.scai20.Reference sensorDomain)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(SENSORDOMAIN$10, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(sensorDomain);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "SensorDomain" element
     */
    public de.offis.xml.schema.scai20.Reference insertNewSensorDomain(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().insert_element_user(SENSORDOMAIN$10, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "SensorDomain" element
     */
    public de.offis.xml.schema.scai20.Reference addNewSensorDomain()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(SENSORDOMAIN$10);
            return target;
        }
    }
    
    /**
     * Removes the ith "SensorDomain" element
     */
    public void removeSensorDomain(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SENSORDOMAIN$10, i);
        }
    }
}
