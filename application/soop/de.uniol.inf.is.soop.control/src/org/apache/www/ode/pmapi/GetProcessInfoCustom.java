/**
 * GetProcessInfoCustom.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.apache.www.ode.pmapi;

public class GetProcessInfoCustom  implements java.io.Serializable {
    private javax.xml.namespace.QName pid;

    private java.lang.String customizer;

    public GetProcessInfoCustom() {
    }

    public GetProcessInfoCustom(
           javax.xml.namespace.QName pid,
           java.lang.String customizer) {
           this.pid = pid;
           this.customizer = customizer;
    }


    /**
     * Gets the pid value for this GetProcessInfoCustom.
     * 
     * @return pid
     */
    public javax.xml.namespace.QName getPid() {
        return pid;
    }


    /**
     * Sets the pid value for this GetProcessInfoCustom.
     * 
     * @param pid
     */
    public void setPid(javax.xml.namespace.QName pid) {
        this.pid = pid;
    }


    /**
     * Gets the customizer value for this GetProcessInfoCustom.
     * 
     * @return customizer
     */
    public java.lang.String getCustomizer() {
        return customizer;
    }


    /**
     * Sets the customizer value for this GetProcessInfoCustom.
     * 
     * @param customizer
     */
    public void setCustomizer(java.lang.String customizer) {
        this.customizer = customizer;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetProcessInfoCustom)) return false;
        GetProcessInfoCustom other = (GetProcessInfoCustom) obj;
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
            ((this.customizer==null && other.getCustomizer()==null) || 
             (this.customizer!=null &&
              this.customizer.equals(other.getCustomizer())));
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
        if (getCustomizer() != null) {
            _hashCode += getCustomizer().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetProcessInfoCustom.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.apache.org/ode/pmapi", ">getProcessInfoCustom"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customizer");
        elemField.setXmlName(new javax.xml.namespace.QName("", "customizer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
