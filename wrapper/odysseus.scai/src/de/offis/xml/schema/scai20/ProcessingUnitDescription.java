/*
 * XML Type:  ProcessingUnitDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.ProcessingUnitDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20;


/**
 * An XML ProcessingUnitDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public interface ProcessingUnitDescription extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ProcessingUnitDescription.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("processingunitdescriptiond599type");
    
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
     * Gets the "address" element
     */
    java.lang.String getAddress();
    
    /**
     * Gets (as xml) the "address" element
     */
    org.apache.xmlbeans.XmlAnyURI xgetAddress();
    
    /**
     * Sets the "address" element
     */
    void setAddress(java.lang.String address);
    
    /**
     * Sets (as xml) the "address" element
     */
    void xsetAddress(org.apache.xmlbeans.XmlAnyURI address);
    
    /**
     * Gets the "version" element
     */
    double getVersion();
    
    /**
     * Gets (as xml) the "version" element
     */
    org.apache.xmlbeans.XmlDouble xgetVersion();
    
    /**
     * Sets the "version" element
     */
    void setVersion(double version);
    
    /**
     * Sets (as xml) the "version" element
     */
    void xsetVersion(org.apache.xmlbeans.XmlDouble version);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription newInstance() {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.ProcessingUnitDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.ProcessingUnitDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
