/**
 * ListProcessesCustom.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.apache.www.ode.pmapi;

public class ListProcessesCustom  implements java.io.Serializable {
    private java.lang.String filter;

    private java.lang.String orderKeys;

    private java.lang.String customizer;

    public ListProcessesCustom() {
    }

    public ListProcessesCustom(
           java.lang.String filter,
           java.lang.String orderKeys,
           java.lang.String customizer) {
           this.filter = filter;
           this.orderKeys = orderKeys;
           this.customizer = customizer;
    }


    /**
     * Gets the filter value for this ListProcessesCustom.
     * 
     * @return filter
     */
    public java.lang.String getFilter() {
        return filter;
    }


    /**
     * Sets the filter value for this ListProcessesCustom.
     * 
     * @param filter
     */
    public void setFilter(java.lang.String filter) {
        this.filter = filter;
    }


    /**
     * Gets the orderKeys value for this ListProcessesCustom.
     * 
     * @return orderKeys
     */
    public java.lang.String getOrderKeys() {
        return orderKeys;
    }


    /**
     * Sets the orderKeys value for this ListProcessesCustom.
     * 
     * @param orderKeys
     */
    public void setOrderKeys(java.lang.String orderKeys) {
        this.orderKeys = orderKeys;
    }


    /**
     * Gets the customizer value for this ListProcessesCustom.
     * 
     * @return customizer
     */
    public java.lang.String getCustomizer() {
        return customizer;
    }


    /**
     * Sets the customizer value for this ListProcessesCustom.
     * 
     * @param customizer
     */
    public void setCustomizer(java.lang.String customizer) {
        this.customizer = customizer;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListProcessesCustom)) return false;
        ListProcessesCustom other = (ListProcessesCustom) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.filter==null && other.getFilter()==null) || 
             (this.filter!=null &&
              this.filter.equals(other.getFilter()))) &&
            ((this.orderKeys==null && other.getOrderKeys()==null) || 
             (this.orderKeys!=null &&
              this.orderKeys.equals(other.getOrderKeys()))) &&
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
        if (getFilter() != null) {
            _hashCode += getFilter().hashCode();
        }
        if (getOrderKeys() != null) {
            _hashCode += getOrderKeys().hashCode();
        }
        if (getCustomizer() != null) {
            _hashCode += getCustomizer().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ListProcessesCustom.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.apache.org/ode/pmapi", ">listProcessesCustom"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filter");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderKeys");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderKeys"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
