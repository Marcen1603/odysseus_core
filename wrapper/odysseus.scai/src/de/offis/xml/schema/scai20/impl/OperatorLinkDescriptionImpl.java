/*
 * XML Type:  OperatorLinkDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.OperatorLinkDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20.impl;
/**
 * An XML OperatorLinkDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public class OperatorLinkDescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.OperatorLinkDescription
{
    private static final long serialVersionUID = 1L;
    
    public OperatorLinkDescriptionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SOURCE$0 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Source");
    private static final javax.xml.namespace.QName DESTINATION$2 = 
        new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "Destination");
    
    
    /**
     * Gets the "Source" element
     */
    public de.offis.xml.schema.scai20.OperatorLinkDescription.Source getSource()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorLinkDescription.Source target = null;
            target = (de.offis.xml.schema.scai20.OperatorLinkDescription.Source)get_store().find_element_user(SOURCE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Source" element
     */
    public void setSource(de.offis.xml.schema.scai20.OperatorLinkDescription.Source source)
    {
        generatedSetterHelperImpl(source, SOURCE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Source" element
     */
    public de.offis.xml.schema.scai20.OperatorLinkDescription.Source addNewSource()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorLinkDescription.Source target = null;
            target = (de.offis.xml.schema.scai20.OperatorLinkDescription.Source)get_store().add_element_user(SOURCE$0);
            return target;
        }
    }
    
    /**
     * Gets the "Destination" element
     */
    public de.offis.xml.schema.scai20.OperatorLinkDescription.Destination getDestination()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorLinkDescription.Destination target = null;
            target = (de.offis.xml.schema.scai20.OperatorLinkDescription.Destination)get_store().find_element_user(DESTINATION$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Destination" element
     */
    public void setDestination(de.offis.xml.schema.scai20.OperatorLinkDescription.Destination destination)
    {
        generatedSetterHelperImpl(destination, DESTINATION$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Destination" element
     */
    public de.offis.xml.schema.scai20.OperatorLinkDescription.Destination addNewDestination()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.offis.xml.schema.scai20.OperatorLinkDescription.Destination target = null;
            target = (de.offis.xml.schema.scai20.OperatorLinkDescription.Destination)get_store().add_element_user(DESTINATION$2);
            return target;
        }
    }
    /**
     * An XML Source(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class SourceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.OperatorLinkDescription.Source
    {
        private static final long serialVersionUID = 1L;
        
        public SourceImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName INPUTOPERATOR$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "InputOperator");
        private static final javax.xml.namespace.QName SERVICEOPERATOR$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ServiceOperator");
        
        
        /**
         * Gets the "InputOperator" element
         */
        public de.offis.xml.schema.scai20.Reference getInputOperator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(INPUTOPERATOR$0, 0);
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
                return get_store().count_elements(INPUTOPERATOR$0) != 0;
            }
        }
        
        /**
         * Sets the "InputOperator" element
         */
        public void setInputOperator(de.offis.xml.schema.scai20.Reference inputOperator)
        {
            generatedSetterHelperImpl(inputOperator, INPUTOPERATOR$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "InputOperator" element
         */
        public de.offis.xml.schema.scai20.Reference addNewInputOperator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(INPUTOPERATOR$0);
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
                get_store().remove_element(INPUTOPERATOR$0, 0);
            }
        }
        
        /**
         * Gets the "ServiceOperator" element
         */
        public de.offis.xml.schema.scai20.Reference getServiceOperator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(SERVICEOPERATOR$2, 0);
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
                return get_store().count_elements(SERVICEOPERATOR$2) != 0;
            }
        }
        
        /**
         * Sets the "ServiceOperator" element
         */
        public void setServiceOperator(de.offis.xml.schema.scai20.Reference serviceOperator)
        {
            generatedSetterHelperImpl(serviceOperator, SERVICEOPERATOR$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "ServiceOperator" element
         */
        public de.offis.xml.schema.scai20.Reference addNewServiceOperator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(SERVICEOPERATOR$2);
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
                get_store().remove_element(SERVICEOPERATOR$2, 0);
            }
        }
    }
    /**
     * An XML Destination(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public static class DestinationImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.offis.xml.schema.scai20.OperatorLinkDescription.Destination
    {
        private static final long serialVersionUID = 1L;
        
        public DestinationImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName SERVICEOPERATOR$0 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "ServiceOperator");
        private static final javax.xml.namespace.QName OUTPUTOPERATOR$2 = 
            new javax.xml.namespace.QName("http://xml.offis.de/schema/SCAI-2.0", "OutputOperator");
        
        
        /**
         * Gets the "ServiceOperator" element
         */
        public de.offis.xml.schema.scai20.Reference getServiceOperator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(SERVICEOPERATOR$0, 0);
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
                return get_store().count_elements(SERVICEOPERATOR$0) != 0;
            }
        }
        
        /**
         * Sets the "ServiceOperator" element
         */
        public void setServiceOperator(de.offis.xml.schema.scai20.Reference serviceOperator)
        {
            generatedSetterHelperImpl(serviceOperator, SERVICEOPERATOR$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "ServiceOperator" element
         */
        public de.offis.xml.schema.scai20.Reference addNewServiceOperator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(SERVICEOPERATOR$0);
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
                get_store().remove_element(SERVICEOPERATOR$0, 0);
            }
        }
        
        /**
         * Gets the "OutputOperator" element
         */
        public de.offis.xml.schema.scai20.Reference getOutputOperator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().find_element_user(OUTPUTOPERATOR$2, 0);
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
                return get_store().count_elements(OUTPUTOPERATOR$2) != 0;
            }
        }
        
        /**
         * Sets the "OutputOperator" element
         */
        public void setOutputOperator(de.offis.xml.schema.scai20.Reference outputOperator)
        {
            generatedSetterHelperImpl(outputOperator, OUTPUTOPERATOR$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "OutputOperator" element
         */
        public de.offis.xml.schema.scai20.Reference addNewOutputOperator()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.offis.xml.schema.scai20.Reference target = null;
                target = (de.offis.xml.schema.scai20.Reference)get_store().add_element_user(OUTPUTOPERATOR$2);
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
                get_store().remove_element(OUTPUTOPERATOR$2, 0);
            }
        }
    }
}
