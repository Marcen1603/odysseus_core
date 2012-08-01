/**
 * DataWSService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.uniol.inf.is.soop.webApp.webservice;

public interface DataWSService extends javax.xml.rpc.Service {
    public java.lang.String getDataWSPortAddress();

    public de.uniol.inf.is.soop.webApp.webservice.DataWS getDataWSPort() throws javax.xml.rpc.ServiceException;

    public de.uniol.inf.is.soop.webApp.webservice.DataWS getDataWSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
