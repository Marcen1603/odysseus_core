/**
 * ProcessClient.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.uniol.inf.soop.control.processClient;

public interface ProcessClient extends javax.xml.rpc.Service {
    public java.lang.String getcanonicPortAddress();

    public de.uniol.inf.soop.control.processClient.ProcessClientPortType getcanonicPort() throws javax.xml.rpc.ServiceException;

    public de.uniol.inf.soop.control.processClient.ProcessClientPortType getcanonicPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
