package de.uniol.inf.soop.control.processClient;

public class ProcessClientPortTypeProxy implements de.uniol.inf.soop.control.processClient.ProcessClientPortType {
  private String _endpoint = null;
  private de.uniol.inf.soop.control.processClient.ProcessClientPortType processClientPortType = null;
  
  public ProcessClientPortTypeProxy() {
    _initSoopMissionsStepOverStepOverProcessClientPortTypeProxy();
  }
  
  public ProcessClientPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initSoopMissionsStepOverStepOverProcessClientPortTypeProxy();
  }
  
  private void _initSoopMissionsStepOverStepOverProcessClientPortTypeProxy() {
    try {
      processClientPortType = (new de.uniol.inf.soop.control.processClient.ProcessClientLocator()).getcanonicPort();
      if (processClientPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)processClientPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)processClientPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (processClientPortType != null)
      ((javax.xml.rpc.Stub)processClientPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public de.uniol.inf.soop.control.processClient.ProcessClientPortType getSoopMissionsStepOverStepOverProcessClientPortType() {
    if (processClientPortType == null)
      _initSoopMissionsStepOverStepOverProcessClientPortTypeProxy();
    return processClientPortType;
  }
  
  public org.example.www.WorkflowCallMessages.GotoNextResponse Signal__leave_step(java.lang.Object body) throws java.rmi.RemoteException{
    if (processClientPortType == null)
      _initSoopMissionsStepOverStepOverProcessClientPortTypeProxy();
    return processClientPortType.Signal__leave_step(body);
  }
  
  public org.example.www.WorkflowCallMessages.StartMissionResponse startMission(org.example.www.WorkflowCallMessages.StartMissionRequest body) throws java.rmi.RemoteException{
    if (processClientPortType == null)
      _initSoopMissionsStepOverStepOverProcessClientPortTypeProxy();
    return processClientPortType.startMission(body);
  }
  
  
}