/**
 * SetProcessProperty.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.apache.www.ode.pmapi;

public class SetProcessProperty  implements java.io.Serializable {
    private javax.xml.namespace.QName pid;

    private javax.xml.namespace.QName propertyName;

    private java.lang.String propertyValue;

    public SetProcessProperty() {
    }

    public SetProcessProperty(
           javax.xml.namespace.QName pid,
           javax.xml.namespace.QName propertyName,
           java.lang.String propertyValue) {
           this.pid = pid;
           this.propertyName = propertyName;
           this.propertyValue = propertyValue;
    }


    /**
     * Gets the pid value for this SetProcessProperty.
     * 
     * @return pid
     */
    public javax.xml.namespace.QName getPid() {
        return pid;
    }


    /**
     * Sets the pid value for this SetProcessProperty.
     * 
     * @param pid
     */
    public void setPid(javax.xml.namespace.QName pid) {
        this.pid = pid;
    }


    /**
     * Gets the propertyName value for this SetProcessProperty.
     * 
     * @return propertyName
     */
    public javax.xml.namespace.QName getPropertyName() {
        return propertyName;
    }


    /**
     * Sets the propertyName value for this SetProcessProperty.
     * 
     * @param propertyName
     */
    public void setPropertyName(javax.xml.namespace.QName propertyName) {
        this.propertyName = propertyName;
    }


    /**
     * Gets the propertyValue value for this SetProcessProperty.
     * 
     * @return propertyValue
     */
    public java.lang.String getPropertyValue() {
        return propertyValue;
    }


    /**
     * Sets the propertyValue value for this SetProcessProperty.
     * 
     * @param propertyValue
     */
    public void setPropertyValue(java.lang.String propertyValue) {
        this.propertyValue = propertyValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SetProcessProperty)) return false;
        SetProcessProperty other = (SetProcessProperty) obj;
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
            ((this.propertyName==null && other.getPropertyName()==null) || 
             (this.propertyName!=null &&
              this.propertyName.equals(other.getPropertyName()))) &&
            ((this.propertyValue==null && other.getPropertyValue()==null) || 
             (this.propertyValue!=null &&
              this.propertyValue.equals(other.getPropertyValue())));
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
        if (getPropertyName() != null) {
            _hashCode += getPropertyName().hashCode();
        }
        if (getPropertyValue() != null) {
            _hashCode += getPropertyValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SetProcessProperty.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.apache.org/ode/pmapi", ">setProcessProperty"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("propertyName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "propertyName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("propertyValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "propertyValue"));
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
