/*
 * XML Type:  PermissionDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.PermissionDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20;


/**
 * An XML PermissionDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public interface PermissionDescription extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(PermissionDescription.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("permissiondescription4b81type");
    
    /**
     * Gets the "Permission" element
     */
    de.offis.xml.schema.scai20.PermissionDescription.Permission getPermission();
    
    /**
     * Sets the "Permission" element
     */
    void setPermission(de.offis.xml.schema.scai20.PermissionDescription.Permission permission);
    
    /**
     * Appends and returns a new empty "Permission" element
     */
    de.offis.xml.schema.scai20.PermissionDescription.Permission addNewPermission();
    
    /**
     * Gets array of all "Property" elements
     */
    de.offis.xml.schema.scai20.PermissionDescription.Property[] getPropertyArray();
    
    /**
     * Gets ith "Property" element
     */
    de.offis.xml.schema.scai20.PermissionDescription.Property getPropertyArray(int i);
    
    /**
     * Returns number of "Property" element
     */
    int sizeOfPropertyArray();
    
    /**
     * Sets array of all "Property" element
     */
    void setPropertyArray(de.offis.xml.schema.scai20.PermissionDescription.Property[] propertyArray);
    
    /**
     * Sets ith "Property" element
     */
    void setPropertyArray(int i, de.offis.xml.schema.scai20.PermissionDescription.Property property);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Property" element
     */
    de.offis.xml.schema.scai20.PermissionDescription.Property insertNewProperty(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Property" element
     */
    de.offis.xml.schema.scai20.PermissionDescription.Property addNewProperty();
    
    /**
     * Removes the ith "Property" element
     */
    void removeProperty(int i);
    
    /**
     * Gets the "inheritable" element
     */
    boolean getInheritable();
    
    /**
     * Gets (as xml) the "inheritable" element
     */
    org.apache.xmlbeans.XmlBoolean xgetInheritable();
    
    /**
     * True if has "inheritable" element
     */
    boolean isSetInheritable();
    
    /**
     * Sets the "inheritable" element
     */
    void setInheritable(boolean inheritable);
    
    /**
     * Sets (as xml) the "inheritable" element
     */
    void xsetInheritable(org.apache.xmlbeans.XmlBoolean inheritable);
    
    /**
     * Unsets the "inheritable" element
     */
    void unsetInheritable();
    
    /**
     * An XML Permission(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface Permission extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Permission.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("permission3d8celemtype");
        
        /**
         * Gets the "grant" element
         */
        java.lang.String getGrant();
        
        /**
         * Gets (as xml) the "grant" element
         */
        org.apache.xmlbeans.XmlString xgetGrant();
        
        /**
         * True if has "grant" element
         */
        boolean isSetGrant();
        
        /**
         * Sets the "grant" element
         */
        void setGrant(java.lang.String grant);
        
        /**
         * Sets (as xml) the "grant" element
         */
        void xsetGrant(org.apache.xmlbeans.XmlString grant);
        
        /**
         * Unsets the "grant" element
         */
        void unsetGrant();
        
        /**
         * Gets the "withdraw" element
         */
        java.lang.String getWithdraw();
        
        /**
         * Gets (as xml) the "withdraw" element
         */
        org.apache.xmlbeans.XmlString xgetWithdraw();
        
        /**
         * True if has "withdraw" element
         */
        boolean isSetWithdraw();
        
        /**
         * Sets the "withdraw" element
         */
        void setWithdraw(java.lang.String withdraw);
        
        /**
         * Sets (as xml) the "withdraw" element
         */
        void xsetWithdraw(org.apache.xmlbeans.XmlString withdraw);
        
        /**
         * Unsets the "withdraw" element
         */
        void unsetWithdraw();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.PermissionDescription.Permission newInstance() {
              return (de.offis.xml.schema.scai20.PermissionDescription.Permission) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.PermissionDescription.Permission newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.PermissionDescription.Permission) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML Property(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface Property extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Property.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("property6692elemtype");
        
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
            public static de.offis.xml.schema.scai20.PermissionDescription.Property newInstance() {
              return (de.offis.xml.schema.scai20.PermissionDescription.Property) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.PermissionDescription.Property newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.PermissionDescription.Property) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.offis.xml.schema.scai20.PermissionDescription newInstance() {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.offis.xml.schema.scai20.PermissionDescription parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.offis.xml.schema.scai20.PermissionDescription parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.offis.xml.schema.scai20.PermissionDescription parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.PermissionDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.PermissionDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.PermissionDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
