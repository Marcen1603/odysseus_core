/*
 * XML Type:  ModulesDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.ModulesDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20;


/**
 * An XML ModulesDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public interface ModulesDescription extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ModulesDescription.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("modulesdescription3efbtype");
    
    /**
     * Gets the "AccessControl" element
     */
    boolean getAccessControl();
    
    /**
     * Gets (as xml) the "AccessControl" element
     */
    org.apache.xmlbeans.XmlBoolean xgetAccessControl();
    
    /**
     * True if has "AccessControl" element
     */
    boolean isSetAccessControl();
    
    /**
     * Sets the "AccessControl" element
     */
    void setAccessControl(boolean accessControl);
    
    /**
     * Sets (as xml) the "AccessControl" element
     */
    void xsetAccessControl(org.apache.xmlbeans.XmlBoolean accessControl);
    
    /**
     * Unsets the "AccessControl" element
     */
    void unsetAccessControl();
    
    /**
     * Gets the "Identification" element
     */
    boolean getIdentification();
    
    /**
     * Gets (as xml) the "Identification" element
     */
    org.apache.xmlbeans.XmlBoolean xgetIdentification();
    
    /**
     * True if has "Identification" element
     */
    boolean isSetIdentification();
    
    /**
     * Sets the "Identification" element
     */
    void setIdentification(boolean identification);
    
    /**
     * Sets (as xml) the "Identification" element
     */
    void xsetIdentification(org.apache.xmlbeans.XmlBoolean identification);
    
    /**
     * Unsets the "Identification" element
     */
    void unsetIdentification();
    
    /**
     * Gets the "Measurements" element
     */
    boolean getMeasurements();
    
    /**
     * Gets (as xml) the "Measurements" element
     */
    org.apache.xmlbeans.XmlBoolean xgetMeasurements();
    
    /**
     * True if has "Measurements" element
     */
    boolean isSetMeasurements();
    
    /**
     * Sets the "Measurements" element
     */
    void setMeasurements(boolean measurements);
    
    /**
     * Sets (as xml) the "Measurements" element
     */
    void xsetMeasurements(org.apache.xmlbeans.XmlBoolean measurements);
    
    /**
     * Unsets the "Measurements" element
     */
    void unsetMeasurements();
    
    /**
     * Gets the "SensorControl" element
     */
    boolean getSensorControl();
    
    /**
     * Gets (as xml) the "SensorControl" element
     */
    org.apache.xmlbeans.XmlBoolean xgetSensorControl();
    
    /**
     * True if has "SensorControl" element
     */
    boolean isSetSensorControl();
    
    /**
     * Sets the "SensorControl" element
     */
    void setSensorControl(boolean sensorControl);
    
    /**
     * Sets (as xml) the "SensorControl" element
     */
    void xsetSensorControl(org.apache.xmlbeans.XmlBoolean sensorControl);
    
    /**
     * Unsets the "SensorControl" element
     */
    void unsetSensorControl();
    
    /**
     * Gets the "SensorRegistryControl" element
     */
    boolean getSensorRegistryControl();
    
    /**
     * Gets (as xml) the "SensorRegistryControl" element
     */
    org.apache.xmlbeans.XmlBoolean xgetSensorRegistryControl();
    
    /**
     * True if has "SensorRegistryControl" element
     */
    boolean isSetSensorRegistryControl();
    
    /**
     * Sets the "SensorRegistryControl" element
     */
    void setSensorRegistryControl(boolean sensorRegistryControl);
    
    /**
     * Sets (as xml) the "SensorRegistryControl" element
     */
    void xsetSensorRegistryControl(org.apache.xmlbeans.XmlBoolean sensorRegistryControl);
    
    /**
     * Unsets the "SensorRegistryControl" element
     */
    void unsetSensorRegistryControl();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.offis.xml.schema.scai20.ModulesDescription newInstance() {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.offis.xml.schema.scai20.ModulesDescription parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.offis.xml.schema.scai20.ModulesDescription parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.offis.xml.schema.scai20.ModulesDescription parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.ModulesDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.ModulesDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.ModulesDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
