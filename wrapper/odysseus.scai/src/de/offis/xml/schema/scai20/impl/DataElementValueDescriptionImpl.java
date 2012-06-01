/*
 * XML Type:  DataElementValueDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.DataElementValueDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML DataElementValueDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class DataElementValueDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataElementValueDescription
{
    private static final long serialVersionUID = 1L;
    
    public DataElementValueDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ERROR$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Error");
    private static final javax.xml.namespace.QName DATA$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Data");
    private static final javax.xml.namespace.QName PATH$4 = 
        new javax.xml.namespace.QName("", "path");
    private static final javax.xml.namespace.QName QUALITY$6 = 
        new javax.xml.namespace.QName("", "quality");
    
    
    /**
     * Gets the "Error" element
     */
    public de.offis.xml.schema.scai20.ErrorDescription getError()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ErrorDescription target = null;
            target = (de.offis.xml.schema.scai20.ErrorDescription)get_store().find_element_user(ERROR$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Error" element
     */
    public boolean isSetError()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ERROR$0) != 0;
        }
    }
    
    /**
     * Sets the "Error" element
     */
    public void setError(de.offis.xml.schema.scai20.ErrorDescription error)
    {
        generatedSetterHelperImpl(error, ERROR$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Error" element
     */
    public de.offis.xml.schema.scai20.ErrorDescription addNewError()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ErrorDescription target = null;
            target = (de.offis.xml.schema.scai20.ErrorDescription)get_store().add_element_user(ERROR$0);
            return target;
        }
    }
    
    /**
     * Unsets the "Error" element
     */
    public void unsetError()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ERROR$0, 0);
        }
    }
    
    /**
     * Gets the "Data" element
     */
    public java.lang.String getData()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATA$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Data" element
     */
    public org.apache.xmlbeans.XmlString xgetData()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATA$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Data" element
     */
    public void setData(java.lang.String data)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATA$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATA$2);
            }
            target.setStringValue(data);
        }
    }
    
    /**
     * Sets (as xml) the "Data" element
     */
    public void xsetData(org.apache.xmlbeans.XmlString data)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATA$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATA$2);
            }
            target.set(data);
        }
    }
    
    /**
     * Gets the "path" attribute
     */
    public java.lang.String getPath()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PATH$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "path" attribute
     */
    public org.apache.xmlbeans.XmlString xgetPath()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PATH$4);
            return target;
        }
    }
    
    /**
     * Sets the "path" attribute
     */
    public void setPath(java.lang.String path)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PATH$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PATH$4);
            }
            target.setStringValue(path);
        }
    }
    
    /**
     * Sets (as xml) the "path" attribute
     */
    public void xsetPath(org.apache.xmlbeans.XmlString path)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PATH$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PATH$4);
            }
            target.set(path);
        }
    }
    
    /**
     * Gets the "quality" attribute
     */
    public java.math.BigDecimal getQuality()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(QUALITY$6);
            if (target == null)
            {
                return null;
            }
            return target.getBigDecimalValue();
        }
    }
    
    /**
     * Gets (as xml) the "quality" attribute
     */
    public org.apache.xmlbeans.XmlDecimal xgetQuality()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDecimal target = null;
            target = (org.apache.xmlbeans.XmlDecimal)get_store().find_attribute_user(QUALITY$6);
            return target;
        }
    }
    
    /**
     * True if has "quality" attribute
     */
    public boolean isSetQuality()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(QUALITY$6) != null;
        }
    }
    
    /**
     * Sets the "quality" attribute
     */
    public void setQuality(java.math.BigDecimal quality)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(QUALITY$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(QUALITY$6);
            }
            target.setBigDecimalValue(quality);
        }
    }
    
    /**
     * Sets (as xml) the "quality" attribute
     */
    public void xsetQuality(org.apache.xmlbeans.XmlDecimal quality)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDecimal target = null;
            target = (org.apache.xmlbeans.XmlDecimal)get_store().find_attribute_user(QUALITY$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDecimal)get_store().add_attribute_user(QUALITY$6);
            }
            target.set(quality);
        }
    }
    
    /**
     * Unsets the "quality" attribute
     */
    public void unsetQuality()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(QUALITY$6);
        }
    }
}
