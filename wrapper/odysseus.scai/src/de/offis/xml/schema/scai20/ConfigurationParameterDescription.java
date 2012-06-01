/*
 * XML Type:  ConfigurationParameterDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.ConfigurationParameterDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20;


/**
 * An XML ConfigurationParameterDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public interface ConfigurationParameterDescription extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ConfigurationParameterDescription.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("configurationparameterdescriptionaafdtype");
    
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
     * Gets the "optional" element
     */
    boolean getOptional();
    
    /**
     * Gets (as xml) the "optional" element
     */
    org.apache.xmlbeans.XmlBoolean xgetOptional();
    
    /**
     * Sets the "optional" element
     */
    void setOptional(boolean optional);
    
    /**
     * Sets (as xml) the "optional" element
     */
    void xsetOptional(org.apache.xmlbeans.XmlBoolean optional);
    
    /**
     * Gets the "AtomicParameter" element
     */
    de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter getAtomicParameter();
    
    /**
     * True if has "AtomicParameter" element
     */
    boolean isSetAtomicParameter();
    
    /**
     * Sets the "AtomicParameter" element
     */
    void setAtomicParameter(de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter atomicParameter);
    
    /**
     * Appends and returns a new empty "AtomicParameter" element
     */
    de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter addNewAtomicParameter();
    
    /**
     * Unsets the "AtomicParameter" element
     */
    void unsetAtomicParameter();
    
    /**
     * Gets the "ComplexParameter" element
     */
    de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter getComplexParameter();
    
    /**
     * True if has "ComplexParameter" element
     */
    boolean isSetComplexParameter();
    
    /**
     * Sets the "ComplexParameter" element
     */
    void setComplexParameter(de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter complexParameter);
    
    /**
     * Appends and returns a new empty "ComplexParameter" element
     */
    de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter addNewComplexParameter();
    
    /**
     * Unsets the "ComplexParameter" element
     */
    void unsetComplexParameter();
    
    /**
     * Gets the "SequenceParameter" element
     */
    de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter getSequenceParameter();
    
    /**
     * True if has "SequenceParameter" element
     */
    boolean isSetSequenceParameter();
    
    /**
     * Sets the "SequenceParameter" element
     */
    void setSequenceParameter(de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter sequenceParameter);
    
    /**
     * Appends and returns a new empty "SequenceParameter" element
     */
    de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter addNewSequenceParameter();
    
    /**
     * Unsets the "SequenceParameter" element
     */
    void unsetSequenceParameter();
    
    /**
     * An XML AtomicParameter(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface AtomicParameter extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AtomicParameter.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("atomicparameterdb07elemtype");
        
        /**
         * Gets the "uom" element
         */
        java.lang.String getUom();
        
        /**
         * Gets (as xml) the "uom" element
         */
        org.apache.xmlbeans.XmlString xgetUom();
        
        /**
         * Sets the "uom" element
         */
        void setUom(java.lang.String uom);
        
        /**
         * Sets (as xml) the "uom" element
         */
        void xsetUom(org.apache.xmlbeans.XmlString uom);
        
        /**
         * Gets the "identifier" element
         */
        boolean getIdentifier();
        
        /**
         * Gets (as xml) the "identifier" element
         */
        org.apache.xmlbeans.XmlBoolean xgetIdentifier();
        
        /**
         * Sets the "identifier" element
         */
        void setIdentifier(boolean identifier);
        
        /**
         * Sets (as xml) the "identifier" element
         */
        void xsetIdentifier(org.apache.xmlbeans.XmlBoolean identifier);
        
        /**
         * Gets the "DataType" element
         */
        de.offis.xml.schema.scai20.Reference getDataType();
        
        /**
         * Sets the "DataType" element
         */
        void setDataType(de.offis.xml.schema.scai20.Reference dataType);
        
        /**
         * Appends and returns a new empty "DataType" element
         */
        de.offis.xml.schema.scai20.Reference addNewDataType();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter newInstance() {
              return (de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML ComplexParameter(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface ComplexParameter extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ComplexParameter.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("complexparameteref92elemtype");
        
        /**
         * Gets array of all "ConfigurationParameter" elements
         */
        de.offis.xml.schema.scai20.Reference[] getConfigurationParameterArray();
        
        /**
         * Gets ith "ConfigurationParameter" element
         */
        de.offis.xml.schema.scai20.Reference getConfigurationParameterArray(int i);
        
        /**
         * Returns number of "ConfigurationParameter" element
         */
        int sizeOfConfigurationParameterArray();
        
        /**
         * Sets array of all "ConfigurationParameter" element
         */
        void setConfigurationParameterArray(de.offis.xml.schema.scai20.Reference[] configurationParameterArray);
        
        /**
         * Sets ith "ConfigurationParameter" element
         */
        void setConfigurationParameterArray(int i, de.offis.xml.schema.scai20.Reference configurationParameter);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "ConfigurationParameter" element
         */
        de.offis.xml.schema.scai20.Reference insertNewConfigurationParameter(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "ConfigurationParameter" element
         */
        de.offis.xml.schema.scai20.Reference addNewConfigurationParameter();
        
        /**
         * Removes the ith "ConfigurationParameter" element
         */
        void removeConfigurationParameter(int i);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter newInstance() {
              return (de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML SequenceParameter(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface SequenceParameter extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SequenceParameter.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("sequenceparameter9631elemtype");
        
        /**
         * Gets the "minlength" element
         */
        int getMinlength();
        
        /**
         * Gets (as xml) the "minlength" element
         */
        org.apache.xmlbeans.XmlInt xgetMinlength();
        
        /**
         * Sets the "minlength" element
         */
        void setMinlength(int minlength);
        
        /**
         * Sets (as xml) the "minlength" element
         */
        void xsetMinlength(org.apache.xmlbeans.XmlInt minlength);
        
        /**
         * Gets the "maxlength" element
         */
        int getMaxlength();
        
        /**
         * Gets (as xml) the "maxlength" element
         */
        org.apache.xmlbeans.XmlInt xgetMaxlength();
        
        /**
         * Sets the "maxlength" element
         */
        void setMaxlength(int maxlength);
        
        /**
         * Sets (as xml) the "maxlength" element
         */
        void xsetMaxlength(org.apache.xmlbeans.XmlInt maxlength);
        
        /**
         * Gets the "ConfigurationParameter" element
         */
        de.offis.xml.schema.scai20.Reference getConfigurationParameter();
        
        /**
         * Sets the "ConfigurationParameter" element
         */
        void setConfigurationParameter(de.offis.xml.schema.scai20.Reference configurationParameter);
        
        /**
         * Appends and returns a new empty "ConfigurationParameter" element
         */
        de.offis.xml.schema.scai20.Reference addNewConfigurationParameter();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter newInstance() {
              return (de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription newInstance() {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.ConfigurationParameterDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.ConfigurationParameterDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
