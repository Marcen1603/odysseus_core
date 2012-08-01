/**
 * DataWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.uniol.inf.is.soop.webApp.controlWsClient;

public interface SOOPControlWSPort extends java.rmi.Remote {
    public de.uniol.inf.is.soop.webApp.controlWsClient.StringResponse login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public de.uniol.inf.is.soop.webApp.controlWsClient.StringResponse getInstalledProcesses(java.lang.String token, java.lang.String workflowEngineId) throws java.rmi.RemoteException;
    public de.uniol.inf.is.soop.webApp.controlWsClient.StringResponse startProcess(java.lang.String token, java.lang.String processId) throws java.rmi.RemoteException;
    public de.uniol.inf.is.soop.webApp.controlWsClient.StringResponse forwardButtonPressed(java.lang.String token, java.lang.String instanceId) throws java.rmi.RemoteException;
    public de.uniol.inf.is.soop.webApp.controlWsClient.StringResponse backwardButtonPressed(java.lang.String token, java.lang.String instanceId) throws java.rmi.RemoteException;
}
