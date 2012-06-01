/*
 * XML Type:  SensorTypeDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.SensorTypeDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20;


/**
 * An XML SensorTypeDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public interface SensorTypeDescription extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SensorTypeDescription.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("sensortypedescription489ctype");
    
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
     * Gets the "adapter" element
     */
    java.lang.String getAdapter();
    
    /**
     * Gets (as xml) the "adapter" element
     */
    org.apache.xmlbeans.XmlString xgetAdapter();
    
    /**
     * Sets the "adapter" element
     */
    void setAdapter(java.lang.String adapter);
    
    /**
     * Sets (as xml) the "adapter" element
     */
    void xsetAdapter(org.apache.xmlbeans.XmlString adapter);
    
    /**
     * Gets the "DataStreamType" element
     */
    de.offis.xml.schema.scai20.Reference getDataStreamType();
    
    /**
     * Sets the "DataStreamType" element
     */
    void setDataStreamType(de.offis.xml.schema.scai20.Reference dataStreamType);
    
    /**
     * Appends and returns a new empty "DataStreamType" element
     */
    de.offis.xml.schema.scai20.Reference addNewDataStreamType();
    
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
     * Gets array of all "SensorCategory" elements
     */
    de.offis.xml.schema.scai20.Reference[] getSensorCategoryArray();
    
    /**
     * Gets ith "SensorCategory" element
     */
    de.offis.xml.schema.scai20.Reference getSensorCategoryArray(int i);
    
    /**
     * Returns number of "SensorCategory" element
     */
    int sizeOfSensorCategoryArray();
    
    /**
     * Sets array of all "SensorCategory" element
     */
    void setSensorCategoryArray(de.offis.xml.schema.scai20.Reference[] sensorCategoryArray);
    
    /**
     * Sets ith "SensorCategory" element
     */
    void setSensorCategoryArray(int i, de.offis.xml.schema.scai20.Reference sensorCategory);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "SensorCategory" element
     */
    de.offis.xml.schema.scai20.Reference insertNewSensorCategory(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "SensorCategory" element
     */
    de.offis.xml.schema.scai20.Reference addNewSensorCategory();
    
    /**
     * Removes the ith "SensorCategory" element
     */
    void removeSensorCategory(int i);
    
    /**
     * Gets array of all "SensorDomain" elements
     */
    de.offis.xml.schema.scai20.Reference[] getSensorDomainArray();
    
    /**
     * Gets ith "SensorDomain" element
     */
    de.offis.xml.schema.scai20.Reference getSensorDomainArray(int i);
    
    /**
     * Returns number of "SensorDomain" element
     */
    int sizeOfSensorDomainArray();
    
    /**
     * Sets array of all "SensorDomain" element
     */
    void setSensorDomainArray(de.offis.xml.schema.scai20.Reference[] sensorDomainArray);
    
    /**
     * Sets ith "SensorDomain" element
     */
    void setSensorDomainArray(int i, de.offis.xml.schema.scai20.Reference sensorDomain);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "SensorDomain" element
     */
    de.offis.xml.schema.scai20.Reference insertNewSensorDomain(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "SensorDomain" element
     */
    de.offis.xml.schema.scai20.Reference addNewSensorDomain();
    
    /**
     * Removes the ith "SensorDomain" element
     */
    void removeSensorDomain(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.offis.xml.schema.scai20.SensorTypeDescription newInstance() {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.SensorTypeDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.SensorTypeDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
