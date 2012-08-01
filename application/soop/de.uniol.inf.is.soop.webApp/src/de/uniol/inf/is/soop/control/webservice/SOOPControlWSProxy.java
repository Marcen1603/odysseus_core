package de.uniol.inf.is.soop.control.webservice;

public class SOOPControlWSProxy implements de.uniol.inf.is.soop.control.webservice.SOOPControlWS {
  private String _endpoint = null;
  private de.uniol.inf.is.soop.control.webservice.SOOPControlWS sOOPControlWS = null;
  
  public SOOPControlWSProxy() {
    _initSOOPControlWSProxy();
  }
  
  public SOOPControlWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initSOOPControlWSProxy();
  }
  
  private void _initSOOPControlWSProxy() {
    try {
      sOOPControlWS = (new de.uniol.inf.is.soop.control.webservice.SOOPControlWSServiceLocator()).getSOOPControlWSPort();
      if (sOOPControlWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sOOPControlWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sOOPControlWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sOOPControlWS != null)
      ((javax.xml.rpc.Stub)sOOPControlWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public de.uniol.inf.is.soop.control.webservice.SOOPControlWS getSOOPControlWS() {
    if (sOOPControlWS == null)
      _initSOOPControlWSProxy();
    return sOOPControlWS;
  }
  
  public de.uniol.inf.is.soop.control.webservice.StringResponse login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException{
    if (sOOPControlWS == null)
      _initSOOPControlWSProxy();
    return sOOPControlWS.login(username, password);
  }
  
  public de.uniol.inf.is.soop.control.webservice.StringResponse startProcess(java.lang.String token, java.lang.String processId) throws java.rmi.RemoteException{
    if (sOOPControlWS == null)
      _initSOOPControlWSProxy();
    return sOOPControlWS.startProcess(token, processId);
  }
  
  public de.uniol.inf.is.soop.control.webservice.StringResponse forwardButtonPressed(java.lang.String token, java.lang.String instanceId) throws java.rmi.RemoteException{
    if (sOOPControlWS == null)
      _initSOOPControlWSProxy();
    return sOOPControlWS.forwardButtonPressed(token, instanceId);
  }
  
  public de.uniol.inf.is.soop.control.webservice.StringResponse getInstalledProcesses(java.lang.String token, java.lang.String workflowEngineId) throws java.rmi.RemoteException{
    if (sOOPControlWS == null)
      _initSOOPControlWSProxy();
    return sOOPControlWS.getInstalledProcesses(token, workflowEngineId);
  }
  
  
}