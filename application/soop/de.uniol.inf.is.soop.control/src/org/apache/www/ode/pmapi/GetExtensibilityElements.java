/**
 * GetExtensibilityElements.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.apache.www.ode.pmapi;

public class GetExtensibilityElements  implements java.io.Serializable {
    private javax.xml.namespace.QName pid;

    private org.apache.www.ode.pmapi.AidsType aids;

    public GetExtensibilityElements() {
    }

    public GetExtensibilityElements(
           javax.xml.namespace.QName pid,
           org.apache.www.ode.pmapi.AidsType aids) {
           this.pid = pid;
           this.aids = aids;
    }


    /**
     * Gets the pid value for this GetExtensibilityElements.
     * 
     * @return pid
     */
    public javax.xml.namespace.QName getPid() {
        return pid;
    }


    /**
     * Sets the pid value for this GetExtensibilityElements.
     * 
     * @param pid
     */
    public void setPid(javax.xml.namespace.QName pid) {
        this.pid = pid;
    }


    /**
     * Gets the aids value for this GetExtensibilityElements.
     * 
     * @return aids
     */
    public org.apache.www.ode.pmapi.AidsType getAids() {
        return aids;
    }


    /**
     * Sets the aids value for this GetExtensibilityElements.
     * 
     * @param aids
     */
    public void setAids(org.apache.www.ode.pmapi.AidsType aids) {
        this.aids = aids;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExtensibilityElements)) return false;
        GetExtensibilityElements other = (GetExtensibilityElements) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.pid==null && other.getPid()==null) || 
             (this.pid!=null &&
              this.pid.equals(other.getPid()))) &&
            ((this.aids==null && other.getAids()==null) || 
             (this.aids!=null &&
              this.aids.equals(other.getAids())));
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
        if (getPid() != null) {
            _hashCode += getPid().hashCode();
        }
        if (getAids() != null) {
            _hashCode += getAids().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExtensibilityElements.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.apache.org/ode/pmapi", ">getExtensibilityElements"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aids");
        elemField.setXmlName(new javax.xml.namespace.QName("", "aids"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.apache.org/ode/pmapi", "aidsType"));
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
