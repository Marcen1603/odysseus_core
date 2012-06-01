/*
 * XML Type:  DataStreamTypeDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.DataStreamTypeDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML DataStreamTypeDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class DataStreamTypeDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataStreamTypeDescription
{
    private static final long serialVersionUID = 1L;
    
    public DataStreamTypeDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "name");
    private static final javax.xml.namespace.QName DATAELEMENT$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "DataElement");
    
    
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
     * Gets array of all "DataElement" elements
     */
    public de.offis.xml.schema.scai20.Reference[] getDataElementArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DATAELEMENT$2, targetList);
            de.offis.xml.schema.scai20.Reference[] result = new de.offis.xml.schema.scai20.Reference[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "DataElement" element
     */
    public de.offis.xml.schema.scai20.Reference getDataElementArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(DATAELEMENT$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "DataElement" element
     */
    public int sizeOfDataElementArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DATAELEMENT$2);
        }
    }
    
    /**
     * Sets array of all "DataElement" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setDataElementArray(de.offis.xml.schema.scai20.Reference[] dataElementArray)
    {
        check_orphaned();
        arraySetterHelper(dataElementArray, DATAELEMENT$2);
    }
    
    /**
     * Sets ith "DataElement" element
     */
    public void setDataElementArray(int i, de.offis.xml.schema.scai20.Reference dataElement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(DATAELEMENT$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(dataElement);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "DataElement" element
     */
    public de.offis.xml.schema.scai20.Reference insertNewDataElement(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().insert_element_user(DATAELEMENT$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "DataElement" element
     */
    public de.offis.xml.schema.scai20.Reference addNewDataElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.Reference target = null;
            target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(DATAELEMENT$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "DataElement" element
     */
    public void removeDataElement(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DATAELEMENT$2, i);
        }
    }
}
