/*
 * XML Type:  SensorCategoryDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.SensorCategoryDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML SensorCategoryDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class SensorCategoryDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.SensorCategoryDescription
{
    private static final long serialVersionUID = 1L;
    
    public SensorCategoryDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "name");
    private static final javax.xml.namespace.QName PARENTSENSORCATEGORY$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ParentSensorCategory");
    private static final javax.xml.namespace.QName SENSORDOMAIN$4 = 
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
     * Gets the "ParentSensorCategory" element
     */
    public de.offis.xml.schema.scai20.Reference getParentSensorCategory()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(PARENTSENSORCATEGORY$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "ParentSensorCategory" element
     */
    public boolean isSetParentSensorCategory()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PARENTSENSORCATEGORY$2) != 0;
        }
    }
    
    /**
     * Sets the "ParentSensorCategory" element
     */
    public void setParentSensorCategory(de.offis.xml.schema.scai20.Reference parentSensorCategory)
    {
        generatedSetterHelperImpl(parentSensorCategory, PARENTSENSORCATEGORY$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ParentSensorCategory" element
     */
    public de.offis.xml.schema.scai20.Reference addNewParentSensorCategory()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(PARENTSENSORCATEGORY$2);
            return target;
        }
    }
    
    /**
     * Unsets the "ParentSensorCategory" element
     */
    public void unsetParentSensorCategory()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PARENTSENSORCATEGORY$2, 0);
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
            get_store().find_all_element_users(SENSORDOMAIN$4, targetList);
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
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(SENSORDOMAIN$4, i);
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
            return get_store().count_elements(SENSORDOMAIN$4);
        }
    }
    
    /**
     * Sets array of all "SensorDomain" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setSensorDomainArray(de.offis.xml.schema.scai20.Reference[] sensorDomainArray)
    {
        check_orphaned();
        arraySetterHelper(sensorDomainArray, SENSORDOMAIN$4);
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
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(SENSORDOMAIN$4, i);
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
            target = (de.offis.xml.schema.scai20.Reference)get_store().insert_element_user(SENSORDOMAIN$4, i);
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
            target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(SENSORDOMAIN$4);
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
            get_store().remove_element(SENSORDOMAIN$4, i);
        }
    }
}
