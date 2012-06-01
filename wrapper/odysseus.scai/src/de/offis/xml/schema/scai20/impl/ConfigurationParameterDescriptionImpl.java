/*
 * XML Type:  ConfigurationParameterDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.ConfigurationParameterDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML ConfigurationParameterDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class ConfigurationParameterDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.ConfigurationParameterDescription
{
    private static final long serialVersionUID = 1L;
    
    public ConfigurationParameterDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "name");
    private static final javax.xml.namespace.QName OPTIONAL$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "optional");
    private static final javax.xml.namespace.QName ATOMICPARAMETER$4 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "AtomicParameter");
    private static final javax.xml.namespace.QName COMPLEXPARAMETER$6 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ComplexParameter");
    private static final javax.xml.namespace.QName SEQUENCEPARAMETER$8 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "SequenceParameter");
    
    
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
     * Gets the "optional" element
     */
    public boolean getOptional()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(OPTIONAL$2, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "optional" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetOptional()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(OPTIONAL$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "optional" element
     */
    public void setOptional(boolean optional)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(OPTIONAL$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(OPTIONAL$2);
            }
            target.setBooleanValue(optional);
        }
    }
    
    /**
     * Sets (as xml) the "optional" element
     */
    public void xsetOptional(org.apache.xmlbeans.XmlBoolean optional)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(OPTIONAL$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(OPTIONAL$2);
            }
            target.set(optional);
        }
    }
    
    /**
     * Gets the "AtomicParameter" element
     */
    public de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter getAtomicParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter target = null;
            target = (de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter)get_store().find_element_user(ATOMICPARAMETER$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "AtomicParameter" element
     */
    public boolean isSetAtomicParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ATOMICPARAMETER$4) != 0;
        }
    }
    
    /**
     * Sets the "AtomicParameter" element
     */
    public void setAtomicParameter(de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter atomicParameter)
    {
        generatedSetterHelperImpl(atomicParameter, ATOMICPARAMETER$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "AtomicParameter" element
     */
    public de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter addNewAtomicParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter target = null;
            target = (de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter)get_store().add_element_user(ATOMICPARAMETER$4);
            return target;
        }
    }
    
    /**
     * Unsets the "AtomicParameter" element
     */
    public void unsetAtomicParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ATOMICPARAMETER$4, 0);
        }
    }
    
    /**
     * Gets the "ComplexParameter" element
     */
    public de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter getComplexParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter target = null;
            target = (de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter)get_store().find_element_user(COMPLEXPARAMETER$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "ComplexParameter" element
     */
    public boolean isSetComplexParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COMPLEXPARAMETER$6) != 0;
        }
    }
    
    /**
     * Sets the "ComplexParameter" element
     */
    public void setComplexParameter(de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter complexParameter)
    {
        generatedSetterHelperImpl(complexParameter, COMPLEXPARAMETER$6, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ComplexParameter" element
     */
    public de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter addNewComplexParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter target = null;
            target = (de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter)get_store().add_element_user(COMPLEXPARAMETER$6);
            return target;
        }
    }
    
    /**
     * Unsets the "ComplexParameter" element
     */
    public void unsetComplexParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COMPLEXPARAMETER$6, 0);
        }
    }
    
    /**
     * Gets the "SequenceParameter" element
     */
    public de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter getSequenceParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter target = null;
            target = (de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter)get_store().find_element_user(SEQUENCEPARAMETER$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "SequenceParameter" element
     */
    public boolean isSetSequenceParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SEQUENCEPARAMETER$8) != 0;
        }
    }
    
    /**
     * Sets the "SequenceParameter" element
     */
    public void setSequenceParameter(de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter sequenceParameter)
    {
        generatedSetterHelperImpl(sequenceParameter, SEQUENCEPARAMETER$8, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "SequenceParameter" element
     */
    public de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter addNewSequenceParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter target = null;
            target = (de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter)get_store().add_element_user(SEQUENCEPARAMETER$8);
            return target;
        }
    }
    
    /**
     * Unsets the "SequenceParameter" element
     */
    public void unsetSequenceParameter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SEQUENCEPARAMETER$8, 0);
        }
    }
    /**
     * An XML AtomicParameter(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class AtomicParameterImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter
    {
        private static final long serialVersionUID = 1L;
        
        public AtomicParameterImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName UOM$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "uom");
        private static final javax.xml.namespace.QName IDENTIFIER$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "identifier");
        private static final javax.xml.namespace.QName DATATYPE$4 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "DataType");
        
        
        /**
         * Gets the "uom" element
         */
        public java.lang.String getUom()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UOM$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "uom" element
         */
        public org.apache.xmlbeans.XmlString xgetUom()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(UOM$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "uom" element
         */
        public void setUom(java.lang.String uom)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UOM$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(UOM$0);
                }
                target.setStringValue(uom);
            }
        }
        
        /**
         * Sets (as xml) the "uom" element
         */
        public void xsetUom(org.apache.xmlbeans.XmlString uom)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(UOM$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(UOM$0);
                }
                target.set(uom);
            }
        }
        
        /**
         * Gets the "identifier" element
         */
        public boolean getIdentifier()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDENTIFIER$2, 0);
                if (target == null)
                {
                    return false;
                }
                return target.getBooleanValue();
            }
        }
        
        /**
         * Gets (as xml) the "identifier" element
         */
        public org.apache.xmlbeans.XmlBoolean xgetIdentifier()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(IDENTIFIER$2, 0);
                return target;
            }
        }
        
        /**
         * Sets the "identifier" element
         */
        public void setIdentifier(boolean identifier)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDENTIFIER$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(IDENTIFIER$2);
                }
                target.setBooleanValue(identifier);
            }
        }
        
        /**
         * Sets (as xml) the "identifier" element
         */
        public void xsetIdentifier(org.apache.xmlbeans.XmlBoolean identifier)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(IDENTIFIER$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(IDENTIFIER$2);
                }
                target.set(identifier);
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
                target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(DATATYPE$4, 0);
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
            generatedSetterHelperImpl(dataType, DATATYPE$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
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
                target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(DATATYPE$4);
                return target;
            }
        }
    }
    /**
     * An XML ComplexParameter(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class ComplexParameterImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter
    {
        private static final long serialVersionUID = 1L;
        
        public ComplexParameterImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName CONFIGURATIONPARAMETER$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ConfigurationParameter");
        
        
        /**
         * Gets array of all "ConfigurationParameter" elements
         */
        public de.offis.xml.schema.scai20.Reference[] getConfigurationParameterArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(CONFIGURATIONPARAMETER$0, targetList);
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
                target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(CONFIGURATIONPARAMETER$0, i);
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
                return get_store().count_elements(CONFIGURATIONPARAMETER$0);
            }
        }
        
        /**
         * Sets array of all "ConfigurationParameter" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setConfigurationParameterArray(de.offis.xml.schema.scai20.Reference[] configurationParameterArray)
        {
            check_orphaned();
            arraySetterHelper(configurationParameterArray, CONFIGURATIONPARAMETER$0);
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
                target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(CONFIGURATIONPARAMETER$0, i);
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
                target = (de.offis.xml.schema.scai20.Reference)get_store().insert_element_user(CONFIGURATIONPARAMETER$0, i);
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
                target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(CONFIGURATIONPARAMETER$0);
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
                get_store().remove_element(CONFIGURATIONPARAMETER$0, i);
            }
        }
    }
    /**
     * An XML SequenceParameter(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class SequenceParameterImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter
    {
        private static final long serialVersionUID = 1L;
        
        public SequenceParameterImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MINLENGTH$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "minlength");
        private static final javax.xml.namespace.QName MAXLENGTH$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "maxlength");
        private static final javax.xml.namespace.QName CONFIGURATIONPARAMETER$4 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ConfigurationParameter");
        
        
        /**
         * Gets the "minlength" element
         */
        public int getMinlength()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MINLENGTH$0, 0);
                if (target == null)
                {
                    return 0;
                }
                return target.getIntValue();
            }
        }
        
        /**
         * Gets (as xml) the "minlength" element
         */
        public org.apache.xmlbeans.XmlInt xgetMinlength()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(MINLENGTH$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "minlength" element
         */
        public void setMinlength(int minlength)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MINLENGTH$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MINLENGTH$0);
                }
                target.setIntValue(minlength);
            }
        }
        
        /**
         * Sets (as xml) the "minlength" element
         */
        public void xsetMinlength(org.apache.xmlbeans.XmlInt minlength)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(MINLENGTH$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(MINLENGTH$0);
                }
                target.set(minlength);
            }
        }
        
        /**
         * Gets the "maxlength" element
         */
        public int getMaxlength()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAXLENGTH$2, 0);
                if (target == null)
                {
                    return 0;
                }
                return target.getIntValue();
            }
        }
        
        /**
         * Gets (as xml) the "maxlength" element
         */
        public org.apache.xmlbeans.XmlInt xgetMaxlength()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(MAXLENGTH$2, 0);
                return target;
            }
        }
        
        /**
         * Sets the "maxlength" element
         */
        public void setMaxlength(int maxlength)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAXLENGTH$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MAXLENGTH$2);
                }
                target.setIntValue(maxlength);
            }
        }
        
        /**
         * Sets (as xml) the "maxlength" element
         */
        public void xsetMaxlength(org.apache.xmlbeans.XmlInt maxlength)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(MAXLENGTH$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(MAXLENGTH$2);
                }
                target.set(maxlength);
            }
        }
        
        /**
         * Gets the "ConfigurationParameter" element
         */
        public de.offis.xml.schema.scai20.Reference getConfigurationParameter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(CONFIGURATIONPARAMETER$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "ConfigurationParameter" element
         */
        public void setConfigurationParameter(de.offis.xml.schema.scai20.Reference configurationParameter)
        {
            generatedSetterHelperImpl(configurationParameter, CONFIGURATIONPARAMETER$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "ConfigurationParameter" element
         */
        public de.offis.xml.schema.scai20.Reference addNewConfigurationParameter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(CONFIGURATIONPARAMETER$4);
                return target;
            }
        }
    }
}
