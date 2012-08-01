/**
 * DataWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.uniol.inf.is.soop.webApp.webservice;

public class DataWSServiceLocator extends org.apache.axis.client.Service implements de.uniol.inf.is.soop.webApp.webservice.DataWSService {

    public DataWSServiceLocator() {
    }


    public DataWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DataWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DataWSPort
    private java.lang.String DataWSPort_address = "http://0.0.0.0:9671/dataws";

    public java.lang.String getDataWSPortAddress() {
        return DataWSPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DataWSPortWSDDServiceName = "DataWSPort";

    public java.lang.String getDataWSPortWSDDServiceName() {
        return DataWSPortWSDDServiceName;
    }

    public void setDataWSPortWSDDServiceName(java.lang.String name) {
        DataWSPortWSDDServiceName = name;
    }

    public de.uniol.inf.is.soop.webApp.webservice.DataWS getDataWSPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DataWSPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDataWSPort(endpoint);
    }

    public de.uniol.inf.is.soop.webApp.webservice.DataWS getDataWSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de.uniol.inf.is.soop.webApp.webservice.DataWSPortBindingStub _stub = new de.uniol.inf.is.soop.webApp.webservice.DataWSPortBindingStub(portAddress, this);
            _stub.setPortName(getDataWSPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDataWSPortEndpointAddress(java.lang.String address) {
        DataWSPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de.uniol.inf.is.soop.webApp.webservice.DataWS.class.isAssignableFrom(serviceEndpointInterface)) {
                de.uniol.inf.is.soop.webApp.webservice.DataWSPortBindingStub _stub = new de.uniol.inf.is.soop.webApp.webservice.DataWSPortBindingStub(new java.net.URL(DataWSPort_address), this);
                _stub.setPortName(getDataWSPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("DataWSPort".equals(inputPortName)) {
            return getDataWSPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.webApp.soop.is.inf.uniol.de/", "DataWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.webApp.soop.is.inf.uniol.de/", "DataWSPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DataWSPort".equals(portName)) {
            setDataWSPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
