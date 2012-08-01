package de.uniol.inf.is.soop.webApp.controlWsClient;

import java.rmi.RemoteException;

public class SOOPControlWSProxy implements de.uniol.inf.is.soop.webApp.controlWsClient.SOOPControlWSPort {
  private String _endpoint = null;
  private de.uniol.inf.is.soop.webApp.controlWsClient.SOOPControlWSPort sOOPControlWSPort = null;
  
  public SOOPControlWSProxy() {
    _initSOOPControlWSProxy();
  }
  
  public SOOPControlWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initSOOPControlWSProxy();
  }
  
  private void _initSOOPControlWSProxy() {
    try {
      sOOPControlWSPort = (new de.uniol.inf.is.soop.webApp.controlWsClient.SOOPControlWSServiceLocator()).getSOOPControlWSPort();
      if (sOOPControlWSPort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sOOPControlWSPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sOOPControlWSPort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sOOPControlWSPort != null)
      ((javax.xml.rpc.Stub)sOOPControlWSPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public de.uniol.inf.is.soop.webApp.controlWsClient.SOOPControlWSPort getSOOPControlWS() {
    if (sOOPControlWSPort == null)
      _initSOOPControlWSProxy();
    return sOOPControlWSPort;
  }
  
  public de.uniol.inf.is.soop.webApp.controlWsClient.StringResponse login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException{
    if (sOOPControlWSPort == null)
      _initSOOPControlWSProxy();
    return sOOPControlWSPort.login(username, password);
  }
  
  public de.uniol.inf.is.soop.webApp.controlWsClient.StringResponse getInstalledProcesses(java.lang.String token, java.lang.String workflowEngineId) throws java.rmi.RemoteException{
    if (sOOPControlWSPort == null)
      _initSOOPControlWSProxy();
    return sOOPControlWSPort.getInstalledProcesses(token, workflowEngineId);
  }
  
  public de.uniol.inf.is.soop.webApp.controlWsClient.StringResponse startProcess(java.lang.String token, java.lang.String processId) throws java.rmi.RemoteException{
    if (sOOPControlWSPort == null)
      _initSOOPControlWSProxy();
    return sOOPControlWSPort.startProcess(token, processId);
  }
  
  public de.uniol.inf.is.soop.webApp.controlWsClient.StringResponse forwardButtonPressed(java.lang.String token, java.lang.String instanceId) throws java.rmi.RemoteException{
	    if (sOOPControlWSPort == null)
	      _initSOOPControlWSProxy();
	    return sOOPControlWSPort.forwardButtonPressed(token, instanceId);
	  }

@Override
public StringResponse backwardButtonPressed(String token, String instanceId) throws java.rmi.RemoteException{
	if (sOOPControlWSPort == null)
		_initSOOPControlWSProxy();
  	return sOOPControlWSPort.forwardButtonPressed(token, instanceId);
}
  
  
}