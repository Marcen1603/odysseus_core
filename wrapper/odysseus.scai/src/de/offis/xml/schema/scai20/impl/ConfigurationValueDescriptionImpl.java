/*
 * XML Type:  ConfigurationValueDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.ConfigurationValueDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML ConfigurationValueDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class ConfigurationValueDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.ConfigurationValueDescription
{
    private static final long serialVersionUID = 1L;
    
    public ConfigurationValueDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "name");
    private static final javax.xml.namespace.QName INDEX$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "index");
    private static final javax.xml.namespace.QName VALUE$4 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "value");
    private static final javax.xml.namespace.QName CONFIGURATIONVALUE$6 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ConfigurationValue");
    
    
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
     * Gets the "index" element
     */
    public int getIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INDEX$2, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "index" element
     */
    public org.apache.xmlbeans.XmlInt xgetIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(INDEX$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "index" element
     */
    public boolean isSetIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INDEX$2) != 0;
        }
    }
    
    /**
     * Sets the "index" element
     */
    public void setIndex(int index)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INDEX$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(INDEX$2);
            }
            target.setIntValue(index);
        }
    }
    
    /**
     * Sets (as xml) the "index" element
     */
    public void xsetIndex(org.apache.xmlbeans.XmlInt index)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(INDEX$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(INDEX$2);
            }
            target.set(index);
        }
    }
    
    /**
     * Unsets the "index" element
     */
    public void unsetIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INDEX$2, 0);
        }
    }
    
    /**
     * Gets the "value" element
     */
    public java.lang.String getValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "value" element
     */
    public org.apache.xmlbeans.XmlString xgetValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUE$4, 0);
            return target;
        }
    }
    
    /**
     * True if has "value" element
     */
    public boolean isSetValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(VALUE$4) != 0;
        }
    }
    
    /**
     * Sets the "value" element
     */
    public void setValue(java.lang.String value)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUE$4);
            }
            target.setStringValue(value);
        }
    }
    
    /**
     * Sets (as xml) the "value" element
     */
    public void xsetValue(org.apache.xmlbeans.XmlString value)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUE$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(VALUE$4);
            }
            target.set(value);
        }
    }
    
    /**
     * Unsets the "value" element
     */
    public void unsetValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(VALUE$4, 0);
        }
    }
    
    /**
     * Gets array of all "ConfigurationValue" elements
     */
    public de.offis.xml.schema.scai20.ConfigurationValueDescription[] getConfigurationValueArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CONFIGURATIONVALUE$6, targetList);
            de.offis.xml.schema.scai20.ConfigurationValueDescription[] result = new de.offis.xml.schema.scai20.ConfigurationValueDescription[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "ConfigurationValue" element
     */
    public de.offis.xml.schema.scai20.ConfigurationValueDescription getConfigurationValueArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ConfigurationValueDescription target = null;
            target = (de.offis.xml.schema.scai20.ConfigurationValueDescription)get_store().find_element_user(CONFIGURATIONVALUE$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "ConfigurationValue" element
     */
    public int sizeOfConfigurationValueArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CONFIGURATIONVALUE$6);
        }
    }
    
    /**
     * Sets array of all "ConfigurationValue" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setConfigurationValueArray(de.offis.xml.schema.scai20.ConfigurationValueDescription[] configurationValueArray)
    {
        check_orphaned();
        arraySetterHelper(configurationValueArray, CONFIGURATIONVALUE$6);
    }
    
    /**
     * Sets ith "ConfigurationValue" element
     */
    public void setConfigurationValueArray(int i, de.offis.xml.schema.scai20.ConfigurationValueDescription configurationValue)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ConfigurationValueDescription target = null;
            target = (de.offis.xml.schema.scai20.ConfigurationValueDescription)get_store().find_element_user(CONFIGURATIONVALUE$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(configurationValue);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "ConfigurationValue" element
     */
    public de.offis.xml.schema.scai20.ConfigurationValueDescription insertNewConfigurationValue(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ConfigurationValueDescription target = null;
            target = (de.offis.xml.schema.scai20.ConfigurationValueDescription)get_store().insert_element_user(CONFIGURATIONVALUE$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "ConfigurationValue" element
     */
    public de.offis.xml.schema.scai20.ConfigurationValueDescription addNewConfigurationValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ConfigurationValueDescription target = null;
            target = (de.offis.xml.schema.scai20.ConfigurationValueDescription)get_store().add_element_user(CONFIGURATIONVALUE$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "ConfigurationValue" element
     */
    public void removeConfigurationValue(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CONFIGURATIONVALUE$6, i);
        }
    }
}
