/*
 * XML Type:  SensorDataDescription
 * Namespace: http://xml.offis.de/schema/SCAI-2.0
 * Java type: de.offis.xml.schema.scai20.SensorDataDescription
 *
 * Automatically generated - do not modify.
 */
package de.offis.xml.schema.scai20;


/**
 * An XML SensorDataDescription(@http://xml.offis.de/schema/SCAI-2.0).
 *
 * This is a complex type.
 */
public interface SensorDataDescription extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SensorDataDescription.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s109BD2E209E66F4C72EED306A262866A").resolveHandle("sensordatadescriptionba2ctype");
    
    /**
     * Gets the "timeStamp" element
     */
    java.util.Calendar getTimeStamp();
    
    /**
     * Gets (as xml) the "timeStamp" element
     */
    org.apache.xmlbeans.XmlDateTime xgetTimeStamp();
    
    /**
     * Sets the "timeStamp" element
     */
    void setTimeStamp(java.util.Calendar timeStamp);
    
    /**
     * Sets (as xml) the "timeStamp" element
     */
    void xsetTimeStamp(org.apache.xmlbeans.XmlDateTime timeStamp);
    
    /**
     * Gets the "SensorName" element
     */
    java.lang.String getSensorName();
    
    /**
     * Gets (as xml) the "SensorName" element
     */
    org.apache.xmlbeans.XmlString xgetSensorName();
    
    /**
     * Sets the "SensorName" element
     */
    void setSensorName(java.lang.String sensorName);
    
    /**
     * Sets (as xml) the "SensorName" element
     */
    void xsetSensorName(org.apache.xmlbeans.XmlString sensorName);
    
    /**
     * Gets the "SensorDomainName" element
     */
    java.lang.String getSensorDomainName();
    
    /**
     * Gets (as xml) the "SensorDomainName" element
     */
    org.apache.xmlbeans.XmlString xgetSensorDomainName();
    
    /**
     * Sets the "SensorDomainName" element
     */
    void setSensorDomainName(java.lang.String sensorDomainName);
    
    /**
     * Sets (as xml) the "SensorDomainName" element
     */
    void xsetSensorDomainName(org.apache.xmlbeans.XmlString sensorDomainName);
    
    /**
     * Gets the "olderValues" element
     */
    int getOlderValues();
    
    /**
     * Gets (as xml) the "olderValues" element
     */
    org.apache.xmlbeans.XmlInt xgetOlderValues();
    
    /**
     * True if has "olderValues" element
     */
    boolean isSetOlderValues();
    
    /**
     * Sets the "olderValues" element
     */
    void setOlderValues(int olderValues);
    
    /**
     * Sets (as xml) the "olderValues" element
     */
    void xsetOlderValues(org.apache.xmlbeans.XmlInt olderValues);
    
    /**
     * Unsets the "olderValues" element
     */
    void unsetOlderValues();
    
    /**
     * Gets array of all "DataStreamElement" elements
     */
    de.offis.xml.schema.scai20.DataElementValueDescription[] getDataStreamElementArray();
    
    /**
     * Gets ith "DataStreamElement" element
     */
    de.offis.xml.schema.scai20.DataElementValueDescription getDataStreamElementArray(int i);
    
    /**
     * Returns number of "DataStreamElement" element
     */
    int sizeOfDataStreamElementArray();
    
    /**
     * Sets array of all "DataStreamElement" element
     */
    void setDataStreamElementArray(de.offis.xml.schema.scai20.DataElementValueDescription[] dataStreamElementArray);
    
    /**
     * Sets ith "DataStreamElement" element
     */
    void setDataStreamElementArray(int i, de.offis.xml.schema.scai20.DataElementValueDescription dataStreamElement);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "DataStreamElement" element
     */
    de.offis.xml.schema.scai20.DataElementValueDescription insertNewDataStreamElement(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "DataStreamElement" element
     */
    de.offis.xml.schema.scai20.DataElementValueDescription addNewDataStreamElement();
    
    /**
     * Removes the ith "DataStreamElement" element
     */
    void removeDataStreamElement(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static de.offis.xml.schema.scai20.SensorDataDescription newInstance() {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.offis.xml.schema.scai20.SensorDataDescription parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (de.offis.xml.schema.scai20.SensorDataDescription) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
