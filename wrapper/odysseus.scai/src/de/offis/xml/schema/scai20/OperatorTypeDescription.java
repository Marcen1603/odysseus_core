/*
 * XML Type:  OperatorTypeDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.OperatorTypeDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20;


/**
 * An XML OperatorTypeDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public interface OperatorTypeDescription extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(OperatorTypeDescription.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("operatortypedescription05b2type");
    
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
     * Gets the "metaType" element
     */
    java.lang.String getMetaType();
    
    /**
     * Gets (as xml) the "metaType" element
     */
    org.apache.xmlbeans.XmlString xgetMetaType();
    
    /**
     * Sets the "metaType" element
     */
    void setMetaType(java.lang.String metaType);
    
    /**
     * Sets (as xml) the "metaType" element
     */
    void xsetMetaType(org.apache.xmlbeans.XmlString metaType);
    
    /**
     * Gets array of all "Property" elements
     */
    de.offis.xml.schema.scai20.OperatorTypeDescription.Property[] getPropertyArray();
    
    /**
     * Gets ith "Property" element
     */
    de.offis.xml.schema.scai20.OperatorTypeDescription.Property getPropertyArray(int i);
    
    /**
     * Returns number of "Property" element
     */
    int sizeOfPropertyArray();
    
    /**
     * Sets array of all "Property" element
     */
    void setPropertyArray(de.offis.xml.schema.scai20.OperatorTypeDescription.Property[] propertyArray);
    
    /**
     * Sets ith "Property" element
     */
    void setPropertyArray(int i, de.offis.xml.schema.scai20.OperatorTypeDescription.Property property);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Property" element
     */
    de.offis.xml.schema.scai20.OperatorTypeDescription.Property insertNewProperty(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Property" element
     */
    de.offis.xml.schema.scai20.OperatorTypeDescription.Property addNewProperty();
    
    /**
     * Removes the ith "Property" element
     */
    void removeProperty(int i);
    
    /**
     * Gets the "Description" element
     */
    java.lang.String getDescription();
    
    /**
     * Gets (as xml) the "Description" element
     */
    org.apache.xmlbeans.XmlString xgetDescription();
    
    /**
     * True if has "Description" element
     */
    boolean isSetDescription();
    
    /**
     * Sets the "Description" element
     */
    void setDescription(java.lang.String description);
    
    /**
     * Sets (as xml) the "Description" element
     */
    void xsetDescription(org.apache.xmlbeans.XmlString description);
    
    /**
     * Unsets the "Description" element
     */
    void unsetDescription();
    
    /**
     * An XML Property(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface Property extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Property.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("propertya083elemtype");
        
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
         * Gets the "readOnly" element
         */
        boolean getReadOnly();
        
        /**
         * Gets (as xml) the "readOnly" element
         */
        org.apache.xmlbeans.XmlBoolean xgetReadOnly();
        
        /**
         * Sets the "readOnly" element
         */
        void setReadOnly(boolean readOnly);
        
        /**
         * Sets (as xml) the "readOnly" element
         */
        void xsetReadOnly(org.apache.xmlbeans.XmlBoolean readOnly);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.OperatorTypeDescription.Property newInstance() {
              return (de.offis.xml.schema.scai20.OperatorTypeDescription.Property) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.OperatorTypeDescription.Property newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.OperatorTypeDescription.Property) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.offis.xml.schema.scai20.OperatorTypeDescription newInstance() {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.OperatorTypeDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.OperatorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
