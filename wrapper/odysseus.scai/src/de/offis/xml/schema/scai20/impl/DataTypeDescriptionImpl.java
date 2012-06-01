/*
 * XML Type:  DataTypeDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.DataTypeDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML DataTypeDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class DataTypeDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataTypeDescription
{
    private static final long serialVersionUID = 1L;
    
    public DataTypeDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "name");
    private static final javax.xml.namespace.QName STRINGTYPE$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "StringType");
    private static final javax.xml.namespace.QName DECIMALTYPE$4 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "DecimalType");
    private static final javax.xml.namespace.QName BINARYTYPE$6 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "BinaryType");
    private static final javax.xml.namespace.QName LISTTYPE$8 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ListType");
    private static final javax.xml.namespace.QName ENUMTYPE$10 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "EnumType");
    
    
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
     * Gets the "StringType" element
     */
    public de.offis.xml.schema.scai20.DataTypeDescription.StringType getStringType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataTypeDescription.StringType target = null;
            target = (de.offis.xml.schema.scai20.DataTypeDescription.StringType)get_store().find_element_user(STRINGTYPE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "StringType" element
     */
    public boolean isSetStringType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(STRINGTYPE$2) != 0;
        }
    }
    
    /**
     * Sets the "StringType" element
     */
    public void setStringType(de.offis.xml.schema.scai20.DataTypeDescription.StringType stringType)
    {
        generatedSetterHelperImpl(stringType, STRINGTYPE$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "StringType" element
     */
    public de.offis.xml.schema.scai20.DataTypeDescription.StringType addNewStringType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataTypeDescription.StringType target = null;
            target = (de.offis.xml.schema.scai20.DataTypeDescription.StringType)get_store().add_element_user(STRINGTYPE$2);
            return target;
        }
    }
    
    /**
     * Unsets the "StringType" element
     */
    public void unsetStringType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(STRINGTYPE$2, 0);
        }
    }
    
    /**
     * Gets the "DecimalType" element
     */
    public de.offis.xml.schema.scai20.DataTypeDescription.DecimalType getDecimalType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataTypeDescription.DecimalType target = null;
            target = (de.offis.xml.schema.scai20.DataTypeDescription.DecimalType)get_store().find_element_user(DECIMALTYPE$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "DecimalType" element
     */
    public boolean isSetDecimalType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DECIMALTYPE$4) != 0;
        }
    }
    
    /**
     * Sets the "DecimalType" element
     */
    public void setDecimalType(de.offis.xml.schema.scai20.DataTypeDescription.DecimalType decimalType)
    {
        generatedSetterHelperImpl(decimalType, DECIMALTYPE$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "DecimalType" element
     */
    public de.offis.xml.schema.scai20.DataTypeDescription.DecimalType addNewDecimalType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataTypeDescription.DecimalType target = null;
            target = (de.offis.xml.schema.scai20.DataTypeDescription.DecimalType)get_store().add_element_user(DECIMALTYPE$4);
            return target;
        }
    }
    
    /**
     * Unsets the "DecimalType" element
     */
    public void unsetDecimalType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DECIMALTYPE$4, 0);
        }
    }
    
    /**
     * Gets the "BinaryType" element
     */
    public de.offis.xml.schema.scai20.DataTypeDescription.BinaryType getBinaryType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataTypeDescription.BinaryType target = null;
            target = (de.offis.xml.schema.scai20.DataTypeDescription.BinaryType)get_store().find_element_user(BINARYTYPE$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "BinaryType" element
     */
    public boolean isSetBinaryType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(BINARYTYPE$6) != 0;
        }
    }
    
    /**
     * Sets the "BinaryType" element
     */
    public void setBinaryType(de.offis.xml.schema.scai20.DataTypeDescription.BinaryType binaryType)
    {
        generatedSetterHelperImpl(binaryType, BINARYTYPE$6, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "BinaryType" element
     */
    public de.offis.xml.schema.scai20.DataTypeDescription.BinaryType addNewBinaryType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataTypeDescription.BinaryType target = null;
            target = (de.offis.xml.schema.scai20.DataTypeDescription.BinaryType)get_store().add_element_user(BINARYTYPE$6);
            return target;
        }
    }
    
    /**
     * Unsets the "BinaryType" element
     */
    public void unsetBinaryType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(BINARYTYPE$6, 0);
        }
    }
    
    /**
     * Gets the "ListType" element
     */
    public de.offis.xml.schema.scai20.DataTypeDescription.ListType getListType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataTypeDescription.ListType target = null;
            target = (de.offis.xml.schema.scai20.DataTypeDescription.ListType)get_store().find_element_user(LISTTYPE$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "ListType" element
     */
    public boolean isSetListType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(LISTTYPE$8) != 0;
        }
    }
    
    /**
     * Sets the "ListType" element
     */
    public void setListType(de.offis.xml.schema.scai20.DataTypeDescription.ListType listType)
    {
        generatedSetterHelperImpl(listType, LISTTYPE$8, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ListType" element
     */
    public de.offis.xml.schema.scai20.DataTypeDescription.ListType addNewListType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataTypeDescription.ListType target = null;
            target = (de.offis.xml.schema.scai20.DataTypeDescription.ListType)get_store().add_element_user(LISTTYPE$8);
            return target;
        }
    }
    
    /**
     * Unsets the "ListType" element
     */
    public void unsetListType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(LISTTYPE$8, 0);
        }
    }
    
    /**
     * Gets the "EnumType" element
     */
    public de.offis.xml.schema.scai20.DataTypeDescription.EnumType getEnumType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataTypeDescription.EnumType target = null;
            target = (de.offis.xml.schema.scai20.DataTypeDescription.EnumType)get_store().find_element_user(ENUMTYPE$10, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "EnumType" element
     */
    public boolean isSetEnumType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ENUMTYPE$10) != 0;
        }
    }
    
    /**
     * Sets the "EnumType" element
     */
    public void setEnumType(de.offis.xml.schema.scai20.DataTypeDescription.EnumType enumType)
    {
        generatedSetterHelperImpl(enumType, ENUMTYPE$10, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "EnumType" element
     */
    public de.offis.xml.schema.scai20.DataTypeDescription.EnumType addNewEnumType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.DataTypeDescription.EnumType target = null;
            target = (de.offis.xml.schema.scai20.DataTypeDescription.EnumType)get_store().add_element_user(ENUMTYPE$10);
            return target;
        }
    }
    
    /**
     * Unsets the "EnumType" element
     */
    public void unsetEnumType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ENUMTYPE$10, 0);
        }
    }
    /**
     * An XML StringType(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class StringTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataTypeDescription.StringType
    {
        private static final long serialVersionUID = 1L;
        
        public StringTypeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MIN$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "min");
        private static final javax.xml.namespace.QName MAX$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "max");
        private static final javax.xml.namespace.QName REGEX$4 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "regex");
        private static final javax.xml.namespace.QName DEFAULTVALUE$6 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "defaultvalue");
        
        
        /**
         * Gets the "min" element
         */
        public long getMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    return 0L;
                }
                return target.getLongValue();
            }
        }
        
        /**
         * Gets (as xml) the "min" element
         */
        public org.apache.xmlbeans.XmlUnsignedInt xgetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MIN$0, 0);
                return target;
            }
        }
        
        /**
         * True if has "min" element
         */
        public boolean isSetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MIN$0) != 0;
            }
        }
        
        /**
         * Sets the "min" element
         */
        public void setMin(long min)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MIN$0);
                }
                target.setLongValue(min);
            }
        }
        
        /**
         * Sets (as xml) the "min" element
         */
        public void xsetMin(org.apache.xmlbeans.XmlUnsignedInt min)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_element_user(MIN$0);
                }
                target.set(min);
            }
        }
        
        /**
         * Unsets the "min" element
         */
        public void unsetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MIN$0, 0);
            }
        }
        
        /**
         * Gets the "max" element
         */
        public long getMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    return 0L;
                }
                return target.getLongValue();
            }
        }
        
        /**
         * Gets (as xml) the "max" element
         */
        public org.apache.xmlbeans.XmlUnsignedInt xgetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MAX$2, 0);
                return target;
            }
        }
        
        /**
         * True if has "max" element
         */
        public boolean isSetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MAX$2) != 0;
            }
        }
        
        /**
         * Sets the "max" element
         */
        public void setMax(long max)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MAX$2);
                }
                target.setLongValue(max);
            }
        }
        
        /**
         * Sets (as xml) the "max" element
         */
        public void xsetMax(org.apache.xmlbeans.XmlUnsignedInt max)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_element_user(MAX$2);
                }
                target.set(max);
            }
        }
        
        /**
         * Unsets the "max" element
         */
        public void unsetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MAX$2, 0);
            }
        }
        
        /**
         * Gets the "regex" element
         */
        public java.lang.String getRegex()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REGEX$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "regex" element
         */
        public org.apache.xmlbeans.XmlString xgetRegex()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REGEX$4, 0);
                return target;
            }
        }
        
        /**
         * True if has "regex" element
         */
        public boolean isSetRegex()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(REGEX$4) != 0;
            }
        }
        
        /**
         * Sets the "regex" element
         */
        public void setRegex(java.lang.String regex)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REGEX$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(REGEX$4);
                }
                target.setStringValue(regex);
            }
        }
        
        /**
         * Sets (as xml) the "regex" element
         */
        public void xsetRegex(org.apache.xmlbeans.XmlString regex)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REGEX$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(REGEX$4);
                }
                target.set(regex);
            }
        }
        
        /**
         * Unsets the "regex" element
         */
        public void unsetRegex()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(REGEX$4, 0);
            }
        }
        
        /**
         * Gets the "defaultvalue" element
         */
        public java.lang.String getDefaultvalue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEFAULTVALUE$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "defaultvalue" element
         */
        public org.apache.xmlbeans.XmlString xgetDefaultvalue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DEFAULTVALUE$6, 0);
                return target;
            }
        }
        
        /**
         * Sets the "defaultvalue" element
         */
        public void setDefaultvalue(java.lang.String defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEFAULTVALUE$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DEFAULTVALUE$6);
                }
                target.setStringValue(defaultvalue);
            }
        }
        
        /**
         * Sets (as xml) the "defaultvalue" element
         */
        public void xsetDefaultvalue(org.apache.xmlbeans.XmlString defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DEFAULTVALUE$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DEFAULTVALUE$6);
                }
                target.set(defaultvalue);
            }
        }
    }
    /**
     * An XML DecimalType(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class DecimalTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataTypeDescription.DecimalType
    {
        private static final long serialVersionUID = 1L;
        
        public DecimalTypeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MIN$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "min");
        private static final javax.xml.namespace.QName MAX$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "max");
        private static final javax.xml.namespace.QName SCALE$4 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "scale");
        private static final javax.xml.namespace.QName DEFAULTVALUE$6 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "defaultvalue");
        
        
        /**
         * Gets the "min" element
         */
        public float getMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    return 0.0f;
                }
                return target.getFloatValue();
            }
        }
        
        /**
         * Gets (as xml) the "min" element
         */
        public org.apache.xmlbeans.XmlFloat xgetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(MIN$0, 0);
                return target;
            }
        }
        
        /**
         * True if has "min" element
         */
        public boolean isSetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MIN$0) != 0;
            }
        }
        
        /**
         * Sets the "min" element
         */
        public void setMin(float min)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MIN$0);
                }
                target.setFloatValue(min);
            }
        }
        
        /**
         * Sets (as xml) the "min" element
         */
        public void xsetMin(org.apache.xmlbeans.XmlFloat min)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlFloat)get_store().add_element_user(MIN$0);
                }
                target.set(min);
            }
        }
        
        /**
         * Unsets the "min" element
         */
        public void unsetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MIN$0, 0);
            }
        }
        
        /**
         * Gets the "max" element
         */
        public float getMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    return 0.0f;
                }
                return target.getFloatValue();
            }
        }
        
        /**
         * Gets (as xml) the "max" element
         */
        public org.apache.xmlbeans.XmlFloat xgetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(MAX$2, 0);
                return target;
            }
        }
        
        /**
         * True if has "max" element
         */
        public boolean isSetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MAX$2) != 0;
            }
        }
        
        /**
         * Sets the "max" element
         */
        public void setMax(float max)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MAX$2);
                }
                target.setFloatValue(max);
            }
        }
        
        /**
         * Sets (as xml) the "max" element
         */
        public void xsetMax(org.apache.xmlbeans.XmlFloat max)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlFloat)get_store().add_element_user(MAX$2);
                }
                target.set(max);
            }
        }
        
        /**
         * Unsets the "max" element
         */
        public void unsetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MAX$2, 0);
            }
        }
        
        /**
         * Gets the "scale" element
         */
        public float getScale()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SCALE$4, 0);
                if (target == null)
                {
                    return 0.0f;
                }
                return target.getFloatValue();
            }
        }
        
        /**
         * Gets (as xml) the "scale" element
         */
        public org.apache.xmlbeans.XmlFloat xgetScale()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(SCALE$4, 0);
                return target;
            }
        }
        
        /**
         * True if has "scale" element
         */
        public boolean isSetScale()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(SCALE$4) != 0;
            }
        }
        
        /**
         * Sets the "scale" element
         */
        public void setScale(float scale)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SCALE$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SCALE$4);
                }
                target.setFloatValue(scale);
            }
        }
        
        /**
         * Sets (as xml) the "scale" element
         */
        public void xsetScale(org.apache.xmlbeans.XmlFloat scale)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(SCALE$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlFloat)get_store().add_element_user(SCALE$4);
                }
                target.set(scale);
            }
        }
        
        /**
         * Unsets the "scale" element
         */
        public void unsetScale()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(SCALE$4, 0);
            }
        }
        
        /**
         * Gets the "defaultvalue" element
         */
        public float getDefaultvalue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEFAULTVALUE$6, 0);
                if (target == null)
                {
                    return 0.0f;
                }
                return target.getFloatValue();
            }
        }
        
        /**
         * Gets (as xml) the "defaultvalue" element
         */
        public org.apache.xmlbeans.XmlFloat xgetDefaultvalue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(DEFAULTVALUE$6, 0);
                return target;
            }
        }
        
        /**
         * Sets the "defaultvalue" element
         */
        public void setDefaultvalue(float defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEFAULTVALUE$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DEFAULTVALUE$6);
                }
                target.setFloatValue(defaultvalue);
            }
        }
        
        /**
         * Sets (as xml) the "defaultvalue" element
         */
        public void xsetDefaultvalue(org.apache.xmlbeans.XmlFloat defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(DEFAULTVALUE$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlFloat)get_store().add_element_user(DEFAULTVALUE$6);
                }
                target.set(defaultvalue);
            }
        }
    }
    /**
     * An XML BinaryType(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class BinaryTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataTypeDescription.BinaryType
    {
        private static final long serialVersionUID = 1L;
        
        public BinaryTypeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MIN$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "min");
        private static final javax.xml.namespace.QName MAX$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "max");
        private static final javax.xml.namespace.QName DEFAULTVALUE$4 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "defaultvalue");
        
        
        /**
         * Gets the "min" element
         */
        public long getMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    return 0L;
                }
                return target.getLongValue();
            }
        }
        
        /**
         * Gets (as xml) the "min" element
         */
        public org.apache.xmlbeans.XmlUnsignedInt xgetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MIN$0, 0);
                return target;
            }
        }
        
        /**
         * True if has "min" element
         */
        public boolean isSetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MIN$0) != 0;
            }
        }
        
        /**
         * Sets the "min" element
         */
        public void setMin(long min)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MIN$0);
                }
                target.setLongValue(min);
            }
        }
        
        /**
         * Sets (as xml) the "min" element
         */
        public void xsetMin(org.apache.xmlbeans.XmlUnsignedInt min)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_element_user(MIN$0);
                }
                target.set(min);
            }
        }
        
        /**
         * Unsets the "min" element
         */
        public void unsetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MIN$0, 0);
            }
        }
        
        /**
         * Gets the "max" element
         */
        public long getMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    return 0L;
                }
                return target.getLongValue();
            }
        }
        
        /**
         * Gets (as xml) the "max" element
         */
        public org.apache.xmlbeans.XmlUnsignedInt xgetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MAX$2, 0);
                return target;
            }
        }
        
        /**
         * True if has "max" element
         */
        public boolean isSetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MAX$2) != 0;
            }
        }
        
        /**
         * Sets the "max" element
         */
        public void setMax(long max)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MAX$2);
                }
                target.setLongValue(max);
            }
        }
        
        /**
         * Sets (as xml) the "max" element
         */
        public void xsetMax(org.apache.xmlbeans.XmlUnsignedInt max)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_element_user(MAX$2);
                }
                target.set(max);
            }
        }
        
        /**
         * Unsets the "max" element
         */
        public void unsetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MAX$2, 0);
            }
        }
        
        /**
         * Gets the "defaultvalue" element
         */
        public byte[] getDefaultvalue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEFAULTVALUE$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getByteArrayValue();
            }
        }
        
        /**
         * Gets (as xml) the "defaultvalue" element
         */
        public org.apache.xmlbeans.XmlBase64Binary xgetDefaultvalue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBase64Binary target = null;
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(DEFAULTVALUE$4, 0);
                return target;
            }
        }
        
        /**
         * Sets the "defaultvalue" element
         */
        public void setDefaultvalue(byte[] defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEFAULTVALUE$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DEFAULTVALUE$4);
                }
                target.setByteArrayValue(defaultvalue);
            }
        }
        
        /**
         * Sets (as xml) the "defaultvalue" element
         */
        public void xsetDefaultvalue(org.apache.xmlbeans.XmlBase64Binary defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBase64Binary target = null;
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(DEFAULTVALUE$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(DEFAULTVALUE$4);
                }
                target.set(defaultvalue);
            }
        }
    }
    /**
     * An XML ListType(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class ListTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataTypeDescription.ListType
    {
        private static final long serialVersionUID = 1L;
        
        public ListTypeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MIN$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "min");
        private static final javax.xml.namespace.QName MAX$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "max");
        private static final javax.xml.namespace.QName ALLOWEDVALUE$4 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "allowedvalue");
        private static final javax.xml.namespace.QName DEFAULTVALUE$6 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "defaultvalue");
        
        
        /**
         * Gets the "min" element
         */
        public long getMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    return 0L;
                }
                return target.getLongValue();
            }
        }
        
        /**
         * Gets (as xml) the "min" element
         */
        public org.apache.xmlbeans.XmlUnsignedInt xgetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MIN$0, 0);
                return target;
            }
        }
        
        /**
         * True if has "min" element
         */
        public boolean isSetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MIN$0) != 0;
            }
        }
        
        /**
         * Sets the "min" element
         */
        public void setMin(long min)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MIN$0);
                }
                target.setLongValue(min);
            }
        }
        
        /**
         * Sets (as xml) the "min" element
         */
        public void xsetMin(org.apache.xmlbeans.XmlUnsignedInt min)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_element_user(MIN$0);
                }
                target.set(min);
            }
        }
        
        /**
         * Unsets the "min" element
         */
        public void unsetMin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MIN$0, 0);
            }
        }
        
        /**
         * Gets the "max" element
         */
        public long getMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    return 0L;
                }
                return target.getLongValue();
            }
        }
        
        /**
         * Gets (as xml) the "max" element
         */
        public org.apache.xmlbeans.XmlUnsignedInt xgetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MAX$2, 0);
                return target;
            }
        }
        
        /**
         * True if has "max" element
         */
        public boolean isSetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MAX$2) != 0;
            }
        }
        
        /**
         * Sets the "max" element
         */
        public void setMax(long max)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MAX$2);
                }
                target.setLongValue(max);
            }
        }
        
        /**
         * Sets (as xml) the "max" element
         */
        public void xsetMax(org.apache.xmlbeans.XmlUnsignedInt max)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlUnsignedInt target = null;
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(MAX$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_element_user(MAX$2);
                }
                target.set(max);
            }
        }
        
        /**
         * Unsets the "max" element
         */
        public void unsetMax()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MAX$2, 0);
            }
        }
        
        /**
         * Gets array of all "allowedvalue" elements
         */
        public java.lang.String[] getAllowedvalueArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(ALLOWEDVALUE$4, targetList);
                java.lang.String[] result = new java.lang.String[targetList.size()];
                for (int i = 0, len = targetList.size() ; i < len ; i++)
                    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
                return result;
            }
        }
        
        /**
         * Gets ith "allowedvalue" element
         */
        public java.lang.String getAllowedvalueArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ALLOWEDVALUE$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) array of all "allowedvalue" elements
         */
        public org.apache.xmlbeans.XmlString[] xgetAllowedvalueArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(ALLOWEDVALUE$4, targetList);
                org.apache.xmlbeans.XmlString[] result = new org.apache.xmlbeans.XmlString[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets (as xml) ith "allowedvalue" element
         */
        public org.apache.xmlbeans.XmlString xgetAllowedvalueArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ALLOWEDVALUE$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "allowedvalue" element
         */
        public int sizeOfAllowedvalueArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(ALLOWEDVALUE$4);
            }
        }
        
        /**
         * Sets array of all "allowedvalue" element
         */
        public void setAllowedvalueArray(java.lang.String[] allowedvalueArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(allowedvalueArray, ALLOWEDVALUE$4);
            }
        }
        
        /**
         * Sets ith "allowedvalue" element
         */
        public void setAllowedvalueArray(int i, java.lang.String allowedvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ALLOWEDVALUE$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.setStringValue(allowedvalue);
            }
        }
        
        /**
         * Sets (as xml) array of all "allowedvalue" element
         */
        public void xsetAllowedvalueArray(org.apache.xmlbeans.XmlString[]allowedvalueArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(allowedvalueArray, ALLOWEDVALUE$4);
            }
        }
        
        /**
         * Sets (as xml) ith "allowedvalue" element
         */
        public void xsetAllowedvalueArray(int i, org.apache.xmlbeans.XmlString allowedvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ALLOWEDVALUE$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(allowedvalue);
            }
        }
        
        /**
         * Inserts the value as the ith "allowedvalue" element
         */
        public void insertAllowedvalue(int i, java.lang.String allowedvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = 
                    (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(ALLOWEDVALUE$4, i);
                target.setStringValue(allowedvalue);
            }
        }
        
        /**
         * Appends the value as the last "allowedvalue" element
         */
        public void addAllowedvalue(java.lang.String allowedvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ALLOWEDVALUE$4);
                target.setStringValue(allowedvalue);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "allowedvalue" element
         */
        public org.apache.xmlbeans.XmlString insertNewAllowedvalue(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(ALLOWEDVALUE$4, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "allowedvalue" element
         */
        public org.apache.xmlbeans.XmlString addNewAllowedvalue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ALLOWEDVALUE$4);
                return target;
            }
        }
        
        /**
         * Removes the ith "allowedvalue" element
         */
        public void removeAllowedvalue(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(ALLOWEDVALUE$4, i);
            }
        }
        
        /**
         * Gets array of all "defaultvalue" elements
         */
        public java.lang.String[] getDefaultvalueArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(DEFAULTVALUE$6, targetList);
                java.lang.String[] result = new java.lang.String[targetList.size()];
                for (int i = 0, len = targetList.size() ; i < len ; i++)
                    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
                return result;
            }
        }
        
        /**
         * Gets ith "defaultvalue" element
         */
        public java.lang.String getDefaultvalueArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEFAULTVALUE$6, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) array of all "defaultvalue" elements
         */
        public org.apache.xmlbeans.XmlString[] xgetDefaultvalueArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(DEFAULTVALUE$6, targetList);
                org.apache.xmlbeans.XmlString[] result = new org.apache.xmlbeans.XmlString[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets (as xml) ith "defaultvalue" element
         */
        public org.apache.xmlbeans.XmlString xgetDefaultvalueArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DEFAULTVALUE$6, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "defaultvalue" element
         */
        public int sizeOfDefaultvalueArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(DEFAULTVALUE$6);
            }
        }
        
        /**
         * Sets array of all "defaultvalue" element
         */
        public void setDefaultvalueArray(java.lang.String[] defaultvalueArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(defaultvalueArray, DEFAULTVALUE$6);
            }
        }
        
        /**
         * Sets ith "defaultvalue" element
         */
        public void setDefaultvalueArray(int i, java.lang.String defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEFAULTVALUE$6, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.setStringValue(defaultvalue);
            }
        }
        
        /**
         * Sets (as xml) array of all "defaultvalue" element
         */
        public void xsetDefaultvalueArray(org.apache.xmlbeans.XmlString[]defaultvalueArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(defaultvalueArray, DEFAULTVALUE$6);
            }
        }
        
        /**
         * Sets (as xml) ith "defaultvalue" element
         */
        public void xsetDefaultvalueArray(int i, org.apache.xmlbeans.XmlString defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DEFAULTVALUE$6, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(defaultvalue);
            }
        }
        
        /**
         * Inserts the value as the ith "defaultvalue" element
         */
        public void insertDefaultvalue(int i, java.lang.String defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = 
                    (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(DEFAULTVALUE$6, i);
                target.setStringValue(defaultvalue);
            }
        }
        
        /**
         * Appends the value as the last "defaultvalue" element
         */
        public void addDefaultvalue(java.lang.String defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DEFAULTVALUE$6);
                target.setStringValue(defaultvalue);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "defaultvalue" element
         */
        public org.apache.xmlbeans.XmlString insertNewDefaultvalue(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(DEFAULTVALUE$6, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "defaultvalue" element
         */
        public org.apache.xmlbeans.XmlString addNewDefaultvalue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DEFAULTVALUE$6);
                return target;
            }
        }
        
        /**
         * Removes the ith "defaultvalue" element
         */
        public void removeDefaultvalue(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(DEFAULTVALUE$6, i);
            }
        }
    }
    /**
     * An XML EnumType(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class EnumTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataTypeDescription.EnumType
    {
        private static final long serialVersionUID = 1L;
        
        public EnumTypeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName DEFAULTVALUE$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "defaultvalue");
        private static final javax.xml.namespace.QName ALLOWEDVALUE$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "allowedvalue");
        
        
        /**
         * Gets the "defaultvalue" element
         */
        public java.lang.String getDefaultvalue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEFAULTVALUE$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "defaultvalue" element
         */
        public org.apache.xmlbeans.XmlString xgetDefaultvalue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DEFAULTVALUE$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "defaultvalue" element
         */
        public void setDefaultvalue(java.lang.String defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DEFAULTVALUE$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DEFAULTVALUE$0);
                }
                target.setStringValue(defaultvalue);
            }
        }
        
        /**
         * Sets (as xml) the "defaultvalue" element
         */
        public void xsetDefaultvalue(org.apache.xmlbeans.XmlString defaultvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DEFAULTVALUE$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DEFAULTVALUE$0);
                }
                target.set(defaultvalue);
            }
        }
        
        /**
         * Gets array of all "allowedvalue" elements
         */
        public de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue[] getAllowedvalueArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(ALLOWEDVALUE$2, targetList);
                de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue[] result = new de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "allowedvalue" element
         */
        public de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue getAllowedvalueArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue target = null;
                target = (de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue)get_store().find_element_user(ALLOWEDVALUE$2, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "allowedvalue" element
         */
        public int sizeOfAllowedvalueArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(ALLOWEDVALUE$2);
            }
        }
        
        /**
         * Sets array of all "allowedvalue" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setAllowedvalueArray(de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue[] allowedvalueArray)
        {
            check_orphaned();
            arraySetterHelper(allowedvalueArray, ALLOWEDVALUE$2);
        }
        
        /**
         * Sets ith "allowedvalue" element
         */
        public void setAllowedvalueArray(int i, de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue allowedvalue)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue target = null;
                target = (de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue)get_store().find_element_user(ALLOWEDVALUE$2, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(allowedvalue);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "allowedvalue" element
         */
        public de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue insertNewAllowedvalue(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue target = null;
                target = (de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue)get_store().insert_element_user(ALLOWEDVALUE$2, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "allowedvalue" element
         */
        public de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue addNewAllowedvalue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue target = null;
                target = (de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue)get_store().add_element_user(ALLOWEDVALUE$2);
                return target;
            }
        }
        
        /**
         * Removes the ith "allowedvalue" element
         */
        public void removeAllowedvalue(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(ALLOWEDVALUE$2, i);
            }
        }
        /**
         * An XML allowedvalue(@http://xml.offis.de/schema/SCAI-2.0).
         *
         * This is a complex type.
         */
        public static class AllowedvalueImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.DataTypeDescription.EnumType.Allowedvalue
        {
            private static final long serialVersionUID = 1L;
            
            public AllowedvalueImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName ORDINAL$0 = 
                new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ordinal");
            private static final javax.xml.namespace.QName NAME$2 = 
                new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "name");
            
            
            /**
             * Gets the "ordinal" element
             */
            public long getOrdinal()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ORDINAL$0, 0);
                    if (target == null)
                    {
                      return 0L;
                    }
                    return target.getLongValue();
                }
            }
            
            /**
             * Gets (as xml) the "ordinal" element
             */
            public org.apache.xmlbeans.XmlUnsignedInt xgetOrdinal()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlUnsignedInt target = null;
                    target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(ORDINAL$0, 0);
                    return target;
                }
            }
            
            /**
             * Sets the "ordinal" element
             */
            public void setOrdinal(long ordinal)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ORDINAL$0, 0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ORDINAL$0);
                    }
                    target.setLongValue(ordinal);
                }
            }
            
            /**
             * Sets (as xml) the "ordinal" element
             */
            public void xsetOrdinal(org.apache.xmlbeans.XmlUnsignedInt ordinal)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlUnsignedInt target = null;
                    target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(ORDINAL$0, 0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_element_user(ORDINAL$0);
                    }
                    target.set(ordinal);
                }
            }
            
            /**
             * Gets the "name" element
             */
            public java.lang.String getName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$2, 0);
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
                    target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$2, 0);
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
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$2, 0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NAME$2);
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
                    target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$2, 0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NAME$2);
                    }
                    target.set(name);
                }
            }
        }
    }
}
