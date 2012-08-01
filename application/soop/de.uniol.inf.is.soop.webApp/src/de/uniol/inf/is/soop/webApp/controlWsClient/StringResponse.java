/**
 * StringResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.uniol.inf.is.soop.webApp.controlWsClient;

public class StringResponse  extends de.uniol.inf.is.soop.webApp.controlWsClient.Response  implements java.io.Serializable {
    private java.lang.String responseValue;

    public StringResponse() {
    }

    public StringResponse(
           boolean successful,
           java.lang.String responseValue) {
        super(
            successful);
        this.responseValue = responseValue;
    }


    /**
     * Gets the responseValue value for this StringResponse.
     * 
     * @return responseValue
     */
    public java.lang.String getResponseValue() {
        return responseValue;
    }


    /**
     * Sets the responseValue value for this StringResponse.
     * 
     * @param responseValue
     */
    public void setResponseValue(java.lang.String responseValue) {
        this.responseValue = responseValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StringResponse)) return false;
        StringResponse other = (StringResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.responseValue==null && other.getResponseValue()==null) || 
             (this.responseValue!=null &&
              this.responseValue.equals(other.getResponseValue())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getResponseValue() != null) {
            _hashCode += getResponseValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StringResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.control.soop.is.inf.uniol.de/", "stringResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "responseValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
