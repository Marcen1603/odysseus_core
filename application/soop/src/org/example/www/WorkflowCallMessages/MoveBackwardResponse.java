/**
 * MoveBackwardResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.WorkflowCallMessages;

public class MoveBackwardResponse  implements java.io.Serializable {
    private java.lang.String succsessful;

    public MoveBackwardResponse() {
    }

    public MoveBackwardResponse(
           java.lang.String succsessful) {
           this.succsessful = succsessful;
    }


    /**
     * Gets the succsessful value for this MoveBackwardResponse.
     * 
     * @return succsessful
     */
    public java.lang.String getSuccsessful() {
        return succsessful;
    }


    /**
     * Sets the succsessful value for this MoveBackwardResponse.
     * 
     * @param succsessful
     */
    public void setSuccsessful(java.lang.String succsessful) {
        this.succsessful = succsessful;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MoveBackwardResponse)) return false;
        MoveBackwardResponse other = (MoveBackwardResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.succsessful==null && other.getSuccsessful()==null) || 
             (this.succsessful!=null &&
              this.succsessful.equals(other.getSuccsessful())));
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
        if (getSuccsessful() != null) {
            _hashCode += getSuccsessful().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MoveBackwardResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.example.org/WorkflowCallMessages", ">MoveBackwardResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("succsessful");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/WorkflowCallMessages", "succsessful"));
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
