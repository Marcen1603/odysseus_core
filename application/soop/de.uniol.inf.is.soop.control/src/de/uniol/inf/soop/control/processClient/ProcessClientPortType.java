/**
 * ProcessClientPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.uniol.inf.soop.control.processClient;

public interface ProcessClientPortType extends java.rmi.Remote {
    public org.example.www.WorkflowCallMessages.GotoNextResponse Signal__leave_step(java.lang.Object body) throws java.rmi.RemoteException;
    public org.example.www.WorkflowCallMessages.StartMissionResponse startMission(org.example.www.WorkflowCallMessages.StartMissionRequest body) throws java.rmi.RemoteException;
}
