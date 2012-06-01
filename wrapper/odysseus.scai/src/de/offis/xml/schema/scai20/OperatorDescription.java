/*
 * XML Type:  OperatorDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.OperatorDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20;


/**
 * An XML OperatorDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public interface OperatorDescription extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(OperatorDescription.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("operatordescriptiond1cctype");
    
    /**
     * Gets the "name" element
     */
    java.lang.String getName();
    
    /**
     * Gets (as xml) the "name" element
     */
    org.apache.xmlbeans.XmlString xgetName();
    
    /**
     * Sets the "name" element
     */
    void setName(java.lang.String name);
    
    /**
     * Sets (as xml) the "name" element
     */
    void xsetName(org.apache.xmlbeans.XmlString name);
    
    /**
     * Gets array of all "Property" elements
     */
    de.offis.xml.schema.scai20.OperatorDescription.Property[] getPropertyArray();
    
    /**
     * Gets ith "Property" element
     */
    de.offis.xml.schema.scai20.OperatorDescription.Property getPropertyArray(int i);
    
    /**
     * Returns number of "Property" element
     */
    int sizeOfPropertyArray();
    
    /**
     * Sets array of all "Property" element
     */
    void setPropertyArray(de.offis.xml.schema.scai20.OperatorDescription.Property[] propertyArray);
    
    /**
     * Sets ith "Property" element
     */
    void setPropertyArray(int i, de.offis.xml.schema.scai20.OperatorDescription.Property property);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Property" element
     */
    de.offis.xml.schema.scai20.OperatorDescription.Property insertNewProperty(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Property" element
     */
    de.offis.xml.schema.scai20.OperatorDescription.Property addNewProperty();
    
    /**
     * Removes the ith "Property" element
     */
    void removeProperty(int i);
    
    /**
     * Gets the "InputOperator" element
     */
    de.offis.xml.schema.scai20.OperatorDescription.InputOperator getInputOperator();
    
    /**
     * True if has "InputOperator" element
     */
    boolean isSetInputOperator();
    
    /**
     * Sets the "InputOperator" element
     */
    void setInputOperator(de.offis.xml.schema.scai20.OperatorDescription.InputOperator inputOperator);
    
    /**
     * Appends and returns a new empty "InputOperator" element
     */
    de.offis.xml.schema.scai20.OperatorDescription.InputOperator addNewInputOperator();
    
    /**
     * Unsets the "InputOperator" element
     */
    void unsetInputOperator();
    
    /**
     * Gets the "OutputOperator" element
     */
    de.offis.xml.schema.scai20.OperatorDescription.OutputOperator getOutputOperator();
    
    /**
     * True if has "OutputOperator" element
     */
    boolean isSetOutputOperator();
    
    /**
     * Sets the "OutputOperator" element
     */
    void setOutputOperator(de.offis.xml.schema.scai20.OperatorDescription.OutputOperator outputOperator);
    
    /**
     * Appends and returns a new empty "OutputOperator" element
     */
    de.offis.xml.schema.scai20.OperatorDescription.OutputOperator addNewOutputOperator();
    
    /**
     * Unsets the "OutputOperator" element
     */
    void unsetOutputOperator();
    
    /**
     * Gets the "ServiceOperator" element
     */
    de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator getServiceOperator();
    
    /**
     * True if has "ServiceOperator" element
     */
    boolean isSetServiceOperator();
    
    /**
     * Sets the "ServiceOperator" element
     */
    void setServiceOperator(de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator serviceOperator);
    
    /**
     * Appends and returns a new empty "ServiceOperator" element
     */
    de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator addNewServiceOperator();
    
    /**
     * Unsets the "ServiceOperator" element
     */
    void unsetServiceOperator();
    
    /**
     * An XML Property(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface Property extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Property.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("property7d1delemtype");
        
        /**
         * Gets the "key" element
         */
        java.lang.String getKey();
        
        /**
         * Gets (as xml) the "key" element
         */
        org.apache.xmlbeans.XmlString xgetKey();
        
        /**
         * Sets the "key" element
         */
        void setKey(java.lang.String key);
        
        /**
         * Sets (as xml) the "key" element
         */
        void xsetKey(org.apache.xmlbeans.XmlString key);
        
        /**
         * Gets the "value" element
         */
        java.lang.String getValue();
        
        /**
         * Gets (as xml) the "value" element
         */
        org.apache.xmlbeans.XmlString xgetValue();
        
        /**
         * Sets the "value" element
         */
        void setValue(java.lang.String value);
        
        /**
         * Sets (as xml) the "value" element
         */
        void xsetValue(org.apache.xmlbeans.XmlString value);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.OperatorDescription.Property newInstance() {
              return (de.offis.xml.schema.scai20.OperatorDescription.Property) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.OperatorDescription.Property newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.OperatorDescription.Property) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML InputOperator(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface InputOperator extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(InputOperator.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("inputoperator8626elemtype");
        
        /**
         * Gets the "Sensor" element
         */
        de.offis.xml.schema.scai20.SensorReference getSensor();
        
        /**
         * Sets the "Sensor" element
         */
        void setSensor(de.offis.xml.schema.scai20.SensorReference sensor);
        
        /**
         * Appends and returns a new empty "Sensor" element
         */
        de.offis.xml.schema.scai20.SensorReference addNewSensor();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.OperatorDescription.InputOperator newInstance() {
              return (de.offis.xml.schema.scai20.OperatorDescription.InputOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.OperatorDescription.InputOperator newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.OperatorDescription.InputOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML OutputOperator(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface OutputOperator extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(OutputOperator.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("outputoperator3e0delemtype");
        
        /**
         * Gets the "Sensor" element
         */
        de.offis.xml.schema.scai20.SensorReference getSensor();
        
        /**
         * Sets the "Sensor" element
         */
        void setSensor(de.offis.xml.schema.scai20.SensorReference sensor);
        
        /**
         * Appends and returns a new empty "Sensor" element
         */
        de.offis.xml.schema.scai20.SensorReference addNewSensor();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.OperatorDescription.OutputOperator newInstance() {
              return (de.offis.xml.schema.scai20.OperatorDescription.OutputOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.OperatorDescription.OutputOperator newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.OperatorDescription.OutputOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML ServiceOperator(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface ServiceOperator extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ServiceOperator.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("serviceoperator1c51elemtype");
        
        /**
         * Gets the "OperatorType" element
         */
        de.offis.xml.schema.scai20.Reference getOperatorType();
        
        /**
         * Sets the "OperatorType" element
         */
        void setOperatorType(de.offis.xml.schema.scai20.Reference operatorType);
        
        /**
         * Appends and returns a new empty "OperatorType" element
         */
        de.offis.xml.schema.scai20.Reference addNewOperatorType();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator newInstance() {
              return (de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.OperatorDescription.ServiceOperator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.offis.xml.schema.scai20.OperatorDescription newInstance() {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.offis.xml.schema.scai20.OperatorDescription parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.offis.xml.schema.scai20.OperatorDescription parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorDescription parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.OperatorDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.OperatorDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.OperatorDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
