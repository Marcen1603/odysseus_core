/**
 * GetProcessInfoCustomResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.apache.www.ode.pmapi;

public class GetProcessInfoCustomResponse  implements java.io.Serializable {
    private org.apache.www.ode.pmapi.types._2006._08._02.TProcessInfo processInfo;

    public GetProcessInfoCustomResponse() {
    }

    public GetProcessInfoCustomResponse(
           org.apache.www.ode.pmapi.types._2006._08._02.TProcessInfo processInfo) {
           this.processInfo = processInfo;
    }


    /**
     * Gets the processInfo value for this GetProcessInfoCustomResponse.
     * 
     * @return processInfo
     */
    public org.apache.www.ode.pmapi.types._2006._08._02.TProcessInfo getProcessInfo() {
        return processInfo;
    }


    /**
     * Sets the processInfo value for this GetProcessInfoCustomResponse.
     * 
     * @param processInfo
     */
    public void setProcessInfo(org.apache.www.ode.pmapi.types._2006._08._02.TProcessInfo processInfo) {
        this.processInfo = processInfo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetProcessInfoCustomResponse)) return false;
        GetProcessInfoCustomResponse other = (GetProcessInfoCustomResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.processInfo==null && other.getProcessInfo()==null) || 
             (this.processInfo!=null &&
              this.processInfo.equals(other.getProcessInfo())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getProcessInfo() != null) {
            _hashCode += getProcessInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetProcessInfoCustomResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.apache.org/ode/pmapi", ">getProcessInfoCustomResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "process-info"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.apache.org/ode/pmapi/types/2006/08/02/", "tProcessInfo"));
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
