/*
 * XML Type:  OperatorDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.OperatorDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML OperatorDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class OperatorDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.OperatorDescription
{
    private static final long serialVersionUID = 1L;
    
    public OperatorDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "name");
    private static final javax.xml.namespace.QName PROPERTY$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Property");
    private static final javax.xml.namespace.QName INPUTOPERATOR$4 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "InputOperator");
    private static final javax.xml.namespace.QName OUTPUTOPERATOR$6 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "OutputOperator");
    private static final javax.xml.namespace.QName SERVICEOPERATOR$8 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ServiceOperator");
    
    
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
     * Gets array of all "Property" elements
     */
    public de.offis.xml.schema.scai20.OperatorDescription.Property[] getPropertyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PROPERTY$2, targetList);
            de.offis.xml.schema.scai20.OperatorDescription.Property[] result = new de.offis.xml.schema.scai20.OperatorDescription.Property[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Property" element
     */
    public de.offis.xml.schema.scai20.OperatorDescription.Property getPropertyArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorDescription.Property target = null;
            target = (de.offis.xml.schema.scai20.OperatorDescription.Property)get_store().find_element_user(PROPERTY$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Property" element
     */
    public int sizeOfPropertyArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PROPERTY$2);
        }
    }
    
    /**
     * Sets array of all "Property" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setPropertyArray(de.offis.xml.schema.scai20.OperatorDescription.Property[] propertyArray)
    {
        check_orphaned();
        arraySetterHelper(propertyArray, PROPERTY$2);
    }
    
    /**
     * Sets ith "Property" element
     */
    public void setPropertyArray(int i, de.offis.xml.schema.scai20.OperatorDescription.Property property)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorDescription.Property target = null;
            target = (de.offis.xml.schema.scai20.OperatorDescription.Property)get_store().find_element_user(PROPERTY$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(property);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Property" element
     */
    public de.offis.xml.schema.scai20.OperatorDescription.Property insertNewProperty(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorDescription.Property target = null;
            target = (de.offis.xml.schema.scai20.OperatorDescription.Property)get_store().insert_element_user(PROPERTY$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Property" element
     */
    public de.offis.xml.schema.scai20.OperatorDescription.Property addNewProperty()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorDescription.Property target = null;
            target = (de.offis.xml.schema.scai20.OperatorDescription.Property)get_store().add_element_user(PROPERTY$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "Property" element
     */
    public void removeProperty(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PROPERTY$2, i);
        }
    }
    
    /**
     * Gets the "InputOperator" element
     */
    public de.offis.xml.schema.scai20.OperatorDescription.InputOperator getInputOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorDescription.InputOperator target = null;
            target = (de.offis.xml.schema.scai20.OperatorDescription.InputOperator)get_store().find_element_user(INPUTOPERATOR$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "InputOperator" element
     */
    public boolean isSetInputOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INPUTOPERATOR$4) != 0;
        }
    }
    
    /**
     * Sets the "InputOperator" element
     */
    public void setInputOperator(de.offis.xml.schema.scai20.OperatorDescription.InputOperator inputOperator)
    {
        generatedSetterHelperImpl(inputOperator, INPUTOPERATOR$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "InputOperator" element
     */
    public de.offis.xml.schema.scai20.OperatorDescription.InputOperator addNewInputOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorDescription.InputOperator target = null;
            target = (de.offis.xml.schema.scai20.OperatorDescription.InputOperator)get_store().add_element_user(INPUTOPERATOR$4);
            return target;
        }
    }
    
    /**
     * Unsets the "InputOperator" element
     */
    public void unsetInputOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INPUTOPERATOR$4, 0);
        }
    }
    
    /**
     * Gets the "OutputOperator" element
     */
    public de.offis.xml.schema.scai20.OperatorDescription.OutputOperator getOutputOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorDescription.OutputOperator target = null;
            target = (de.offis.xml.schema.scai20.OperatorDescription.OutputOperator)get_store().find_element_user(OUTPUTOPERATOR$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "OutputOperator" element
     */
    public boolean isSetOutputOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(OUTPUTOPERATOR$6) != 0;
        }
    }
    
    /**
     * Sets the "OutputOperator" element
     */
    public void setOutputOperator(de.offis.xml.schema.scai20.OperatorDescription.OutputOperator outputOperator)
    {
        generatedSetterHelperImpl(outputOperator, OUTPUTOPERATOR$6, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "OutputOperator" element
     */
    public de.offis.xml.schema.scai20.OperatorDescription.OutputOperator addNewOutputOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorDescription.OutputOperator target = null;
            target = (de.offis.xml.schema.scai20.OperatorDescription.OutputOperator)get_store().add_element_user(OUTPUTOPERATOR$6);
            return target;
        }
    }
    
    /**
     * Unsets the "OutputOperator" element
     */
    public void unsetOutputOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(OUTPUTOPERATOR$6, 0);
        }
    }
    
    /**
     * Gets the "ServiceOperator" element
     */
    public de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator getServiceOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator target = null;
            target = (de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator)get_store().find_element_user(SERVICEOPERATOR$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "ServiceOperator" element
     */
    public boolean isSetServiceOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SERVICEOPERATOR$8) != 0;
        }
    }
    
    /**
     * Sets the "ServiceOperator" element
     */
    public void setServiceOperator(de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator serviceOperator)
    {
        generatedSetterHelperImpl(serviceOperator, SERVICEOPERATOR$8, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "ServiceOperator" element
     */
    public de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator addNewServiceOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator target = null;
            target = (de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator)get_store().add_element_user(SERVICEOPERATOR$8);
            return target;
        }
    }
    
    /**
     * Unsets the "ServiceOperator" element
     */
    public void unsetServiceOperator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SERVICEOPERATOR$8, 0);
        }
    }
    /**
     * An XML Property(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class PropertyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.OperatorDescription.Property
    {
        private static final long serialVersionUID = 1L;
        
        public PropertyImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName KEY$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "key");
        private static final javax.xml.namespace.QName VALUE$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "value");
        
        
        /**
         * Gets the "key" element
         */
        public java.lang.String getKey()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(KEY$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "key" element
         */
        public org.apache.xmlbeans.XmlString xgetKey()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(KEY$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "key" element
         */
        public void setKey(java.lang.String key)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(KEY$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(KEY$0);
                }
                target.setStringValue(key);
            }
        }
        
        /**
         * Sets (as xml) the "key" element
         */
        public void xsetKey(org.apache.xmlbeans.XmlString key)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(KEY$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(KEY$0);
                }
                target.set(key);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE$2, 0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUE$2, 0);
                return target;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUE$2);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUE$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(VALUE$2);
                }
                target.set(value);
            }
        }
    }
    /**
     * An XML InputOperator(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class InputOperatorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.OperatorDescription.InputOperator
    {
        private static final long serialVersionUID = 1L;
        
        public InputOperatorImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName SENSOR$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Sensor");
        
        
        /**
         * Gets the "Sensor" element
         */
        public de.offis.xml.schema.scai20.SensorReference getSensor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.SensorReference target = null;
                target = (de.offis.xml.schema.scai20.SensorReference)get_store().find_element_user(SENSOR$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "Sensor" element
         */
        public void setSensor(de.offis.xml.schema.scai20.SensorReference sensor)
        {
            generatedSetterHelperImpl(sensor, SENSOR$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "Sensor" element
         */
        public de.offis.xml.schema.scai20.SensorReference addNewSensor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.SensorReference target = null;
                target = (de.offis.xml.schema.scai20.SensorReference)get_store().add_element_user(SENSOR$0);
                return target;
            }
        }
    }
    /**
     * An XML OutputOperator(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class OutputOperatorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.OperatorDescription.OutputOperator
    {
        private static final long serialVersionUID = 1L;
        
        public OutputOperatorImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName SENSOR$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Sensor");
        
        
        /**
         * Gets the "Sensor" element
         */
        public de.offis.xml.schema.scai20.SensorReference getSensor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.SensorReference target = null;
                target = (de.offis.xml.schema.scai20.SensorReference)get_store().find_element_user(SENSOR$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "Sensor" element
         */
        public void setSensor(de.offis.xml.schema.scai20.SensorReference sensor)
        {
            generatedSetterHelperImpl(sensor, SENSOR$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "Sensor" element
         */
        public de.offis.xml.schema.scai20.SensorReference addNewSensor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.SensorReference target = null;
                target = (de.offis.xml.schema.scai20.SensorReference)get_store().add_element_user(SENSOR$0);
                return target;
            }
        }
    }
    /**
     * An XML ServiceOperator(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class ServiceOperatorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator
    {
        private static final long serialVersionUID = 1L;
        
        public ServiceOperatorImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName OPERATORTYPE$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "OperatorType");
        
        
        /**
         * Gets the "OperatorType" element
         */
        public de.offis.xml.schema.scai20.Reference getOperatorType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(OPERATORTYPE$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "OperatorType" element
         */
        public void setOperatorType(de.offis.xml.schema.scai20.Reference operatorType)
        {
            generatedSetterHelperImpl(operatorType, OPERATORTYPE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "OperatorType" element
         */
        public de.offis.xml.schema.scai20.Reference addNewOperatorType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(OPERATORTYPE$0);
                return target;
            }
        }
    }
}
