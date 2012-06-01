/*
 * XML Type:  DataElementDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.DataElementDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML DataElementDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class DataElementDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataElementDescription
{
    private static final long serialVersionUID = 1L;
    
    public DataElementDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ATOMICELEMENT$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "AtomicElement");
    private static final javax.xml.namespace.QName COMPLEXELEMENT$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ComplexElement");
    
    
    /**
     * Gets the "AtomicElement" element
     */
    public de.offis.xml.schema.scai20.DataElementDescription.AtomicElement getAtomicElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataElementDescription.AtomicElement target = null;
            target = (de.offis.xml.schema.scai20.DataElementDescription.AtomicElement)get_store().find_element_user(ATOMICELEMENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "AtomicElement" element
     */
    public boolean isSetAtomicElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ATOMICELEMENT$0) != 0;
        }
    }
    
    /**
     * Sets the "AtomicElement" element
     */
    public void setAtomicElement(de.offis.xml.schema.scai20.DataElementDescription.AtomicElement atomicElement)
    {
        generatedSetterHelperImpl(atomicElement, ATOMICELEMENT$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "AtomicElement" element
     */
    public de.offis.xml.schema.scai20.DataElementDescription.AtomicElement addNewAtomicElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataElementDescription.AtomicElement target = null;
            target = (de.offis.xml.schema.scai20.DataElementDescription.AtomicElement)get_store().add_element_user(ATOMICELEMENT$0);
            return target;
        }
    }
    
    /**
     * Unsets the "AtomicElement" element
     */
    public void unsetAtomicElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ATOMICELEMENT$0, 0);
        }
    }
    
    /**
     * Gets the "ComplexElement" element
     */
    public de.offis.xml.schema.scai20.DataElementDescription.ComplexElement getComplexElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataElementDescription.ComplexElement target = null;
            target = (de.offis.xml.schema.scai20.DataElementDescription.ComplexElement)get_store().find_element_user(COMPLEXELEMENT$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "ComplexElement" element
     */
    public boolean isSetComplexElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COMPLEXELEMENT$2) != 0;
        }
    }
    
    /**
     * Sets the "ComplexElement" element
     */
    public void setComplexElement(de.offis.xml.schema.scai20.DataElementDescription.ComplexElement complexElement)
    {
        generatedSetterHelperImpl(complexElement, COMPLEXELEMENT$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ComplexElement" element
     */
    public de.offis.xml.schema.scai20.DataElementDescription.ComplexElement addNewComplexElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataElementDescription.ComplexElement target = null;
            target = (de.offis.xml.schema.scai20.DataElementDescription.ComplexElement)get_store().add_element_user(COMPLEXELEMENT$2);
            return target;
        }
    }
    
    /**
     * Unsets the "ComplexElement" element
     */
    public void unsetComplexElement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COMPLEXELEMENT$2, 0);
        }
    }
    /**
     * An XML AtomicElement(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class AtomicElementImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataElementDescription.AtomicElement
    {
        private static final long serialVersionUID = 1L;
        
        public AtomicElementImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName NAME$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "name");
        private static final javax.xml.namespace.QName DATATYPE$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "DataType");
        
        
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
         * Gets the "DataType" element
         */
        public de.offis.xml.schema.scai20.Reference getDataType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(DATATYPE$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "DataType" element
         */
        public void setDataType(de.offis.xml.schema.scai20.Reference dataType)
        {
            generatedSetterHelperImpl(dataType, DATATYPE$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "DataType" element
         */
        public de.offis.xml.schema.scai20.Reference addNewDataType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(DATATYPE$2);
                return target;
            }
        }
    }
    /**
     * An XML ComplexElement(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class ComplexElementImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataElementDescription.ComplexElement
    {
        private static final long serialVersionUID = 1L;
        
        public ComplexElementImpl(org.apache.xmlbeans.SchemaType sType)
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
}
