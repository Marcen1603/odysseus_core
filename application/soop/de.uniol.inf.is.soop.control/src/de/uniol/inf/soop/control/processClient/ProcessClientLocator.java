/**
 * ProcessClientLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.uniol.inf.soop.control.processClient;

public class ProcessClientLocator extends org.apache.axis.client.Service implements de.uniol.inf.soop.control.processClient.ProcessClient {

    public ProcessClientLocator() {
    }


    public ProcessClientLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ProcessClientLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for canonicPort
    private java.lang.String canonicPort_address = "http://localhost:8080/ode/processes/SoopMissions/StepOver/StepOverProcess/Client";

    public java.lang.String getcanonicPortAddress() {
        return canonicPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String canonicPortWSDDServiceName = "canonicPort";

    public java.lang.String getcanonicPortWSDDServiceName() {
        return canonicPortWSDDServiceName;
    }

    public void setcanonicPortWSDDServiceName(java.lang.String name) {
        canonicPortWSDDServiceName = name;
    }

    public de.uniol.inf.soop.control.processClient.ProcessClientPortType getcanonicPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(canonicPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getcanonicPort(endpoint);
    }

    public de.uniol.inf.soop.control.processClient.ProcessClientPortType getcanonicPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de.uniol.inf.soop.control.processClient.CanonicBindingForClientStub _stub = new de.uniol.inf.soop.control.processClient.CanonicBindingForClientStub(portAddress, this);
            _stub.setPortName(getcanonicPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setcanonicPortEndpointAddress(java.lang.String address) {
        canonicPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de.uniol.inf.soop.control.processClient.ProcessClientPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                de.uniol.inf.soop.control.processClient.CanonicBindingForClientStub _stub = new de.uniol.inf.soop.control.processClient.CanonicBindingForClientStub(new java.net.URL(canonicPort_address), this);
                _stub.setPortName(getcanonicPortWSDDServiceName());
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
        if ("canonicPort".equals(inputPortName)) {
            return getcanonicPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://de.offis.soop.missions/StepOver/StepOverProcess", "SoopMissions/StepOver/StepOverProcess/Client");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://de.offis.soop.missions/StepOver/StepOverProcess", "canonicPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("canonicPort".equals(portName)) {
            setcanonicPortEndpointAddress(address);
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
