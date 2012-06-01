/*
 * XML Type:  OperatorLinkDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.OperatorLinkDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20;


/**
 * An XML OperatorLinkDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public interface OperatorLinkDescription extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(OperatorLinkDescription.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("operatorlinkdescription4cf2type");
    
    /**
     * Gets the "Source" element
     */
    de.offis.xml.schema.scai20.OperatorLinkDescription.Source getSource();
    
    /**
     * Sets the "Source" element
     */
    void setSource(de.offis.xml.schema.scai20.OperatorLinkDescription.Source source);
    
    /**
     * Appends and returns a new empty "Source" element
     */
    de.offis.xml.schema.scai20.OperatorLinkDescription.Source addNewSource();
    
    /**
     * Gets the "Destination" element
     */
    de.offis.xml.schema.scai20.OperatorLinkDescription.Destination getDestination();
    
    /**
     * Sets the "Destination" element
     */
    void setDestination(de.offis.xml.schema.scai20.OperatorLinkDescription.Destination destination);
    
    /**
     * Appends and returns a new empty "Destination" element
     */
    de.offis.xml.schema.scai20.OperatorLinkDescription.Destination addNewDestination();
    
    /**
     * An XML Source(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface Source extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Source.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("source6989elemtype");
        
        /**
         * Gets the "InputOperator" element
         */
        de.offis.xml.schema.scai20.Reference getInputOperator();
        
        /**
         * True if has "InputOperator" element
         */
        boolean isSetInputOperator();
        
        /**
         * Sets the "InputOperator" element
         */
        void setInputOperator(de.offis.xml.schema.scai20.Reference inputOperator);
        
        /**
         * Appends and returns a new empty "InputOperator" element
         */
        de.offis.xml.schema.scai20.Reference addNewInputOperator();
        
        /**
         * Unsets the "InputOperator" element
         */
        void unsetInputOperator();
        
        /**
         * Gets the "ServiceOperator" element
         */
        de.offis.xml.schema.scai20.Reference getServiceOperator();
        
        /**
         * True if has "ServiceOperator" element
         */
        boolean isSetServiceOperator();
        
        /**
         * Sets the "ServiceOperator" element
         */
        void setServiceOperator(de.offis.xml.schema.scai20.Reference serviceOperator);
        
        /**
         * Appends and returns a new empty "ServiceOperator" element
         */
        de.offis.xml.schema.scai20.Reference addNewServiceOperator();
        
        /**
         * Unsets the "ServiceOperator" element
         */
        void unsetServiceOperator();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.OperatorLinkDescription.Source newInstance() {
              return (de.offis.xml.schema.scai20.OperatorLinkDescription.Source) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.OperatorLinkDescription.Source newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.OperatorLinkDescription.Source) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML Destination(@http://xml.offis.de/schema/SCAI-2.0).
     *
     * This is a complex type.
     */
    public interface Destination extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Destination.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("destinationdfccelemtype");
        
        /**
         * Gets the "ServiceOperator" element
         */
        de.offis.xml.schema.scai20.Reference getServiceOperator();
        
        /**
         * True if has "ServiceOperator" element
         */
        boolean isSetServiceOperator();
        
        /**
         * Sets the "ServiceOperator" element
         */
        void setServiceOperator(de.offis.xml.schema.scai20.Reference serviceOperator);
        
        /**
         * Appends and returns a new empty "ServiceOperator" element
         */
        de.offis.xml.schema.scai20.Reference addNewServiceOperator();
        
        /**
         * Unsets the "ServiceOperator" element
         */
        void unsetServiceOperator();
        
        /**
         * Gets the "OutputOperator" element
         */
        de.offis.xml.schema.scai20.Reference getOutputOperator();
        
        /**
         * True if has "OutputOperator" element
         */
        boolean isSetOutputOperator();
        
        /**
         * Sets the "OutputOperator" element
         */
        void setOutputOperator(de.offis.xml.schema.scai20.Reference outputOperator);
        
        /**
         * Appends and returns a new empty "OutputOperator" element
         */
        de.offis.xml.schema.scai20.Reference addNewOutputOperator();
        
        /**
         * Unsets the "OutputOperator" element
         */
        void unsetOutputOperator();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static de.offis.xml.schema.scai20.OperatorLinkDescription.Destination newInstance() {
              return (de.offis.xml.schema.scai20.OperatorLinkDescription.Destination) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static de.offis.xml.schema.scai20.OperatorLinkDescription.Destination newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (de.offis.xml.schema.scai20.OperatorLinkDescription.Destination) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.offis.xml.schema.scai20.OperatorLinkDescription newInstance() {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.OperatorLinkDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.OperatorLinkDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
