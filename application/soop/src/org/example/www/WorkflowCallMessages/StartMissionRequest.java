/**
 * StartMissionRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.WorkflowCallMessages;

public class StartMissionRequest  implements java.io.Serializable {
    private java.lang.String username;

    private java.lang.String password;
    
    private java.lang.String dsmsUrl;
    
    private java.lang.String sensorRegistryUrl;

    public StartMissionRequest() {
    }

    public StartMissionRequest(
           java.lang.String username,
           java.lang.String password,
           java.lang.String dsmsUrl,
           java.lang.String sensorRegistryUrl) {
           this.username = username;
           this.password = password;
           this.dsmsUrl = dsmsUrl;
           this.sensorRegistryUrl = sensorRegistryUrl;
    }


    /**
     * Gets the username value for this StartMissionRequest.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }


    /**
     * Sets the username value for this StartMissionRequest.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }


    /**
     * Gets the password value for this StartMissionRequest.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this StartMissionRequest.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    
    
    /**
	 * @return the dsmsUrl
	 */
	public java.lang.String getDsmsUrl() {
		return dsmsUrl;
	}

	/**
	 * @param dsmsUrl the dsmsUrl to set
	 */
	public void setDsmsUrl(java.lang.String dsmsUrl) {
		this.dsmsUrl = dsmsUrl;
	}

	/**
	 * @return the sensorRegistryUrl
	 */
	public java.lang.String getSensorRegistryUrl() {
		return sensorRegistryUrl;
	}

	/**
	 * @param sensorRegistryUrl the sensorRegistryUrl to set
	 */
	public void setSensorRegistryUrl(java.lang.String sensorRegistryUrl) {
		this.sensorRegistryUrl = sensorRegistryUrl;
	}



	private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StartMissionRequest)) return false;
        StartMissionRequest other = (StartMissionRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.username==null && other.getUsername()==null) || 
             (this.username!=null &&
              this.username.equals(other.getUsername()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            ((this.dsmsUrl==null && other.getDsmsUrl()==null) || 
             (this.dsmsUrl!=null &&
              this.dsmsUrl.equals(other.getDsmsUrl()))) &&
            ((this.sensorRegistryUrl==null && other.getSensorRegistryUrl()==null) || 
             (this.sensorRegistryUrl!=null &&
              this.sensorRegistryUrl.equals(other.getSensorRegistryUrl())));
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
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        
        if (getDsmsUrl() != null) {
            _hashCode += getDsmsUrl().hashCode();
        }
        if (getSensorRegistryUrl() != null) {
            _hashCode += getSensorRegistryUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StartMissionRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.example.org/WorkflowCallMessages", ">StartMissionRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/WorkflowCallMessages", "username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/WorkflowCallMessages", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dsmsUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/WorkflowCallMessages", "dsmsUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sensorRegistryUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/WorkflowCallMessages", "sensorRegistryUrl"));
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
