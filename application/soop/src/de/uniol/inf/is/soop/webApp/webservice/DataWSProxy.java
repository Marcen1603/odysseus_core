package de.uniol.inf.is.soop.webApp.webservice;

public class DataWSProxy implements de.uniol.inf.is.soop.webApp.webservice.DataWS {
  private String _endpoint = null;
  private de.uniol.inf.is.soop.webApp.webservice.DataWS dataWS = null;
  
  public DataWSProxy() {
    _initDataWSProxy();
  }
  
  public DataWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initDataWSProxy();
  }
  
  private void _initDataWSProxy() {
    try {
      dataWS = (new de.uniol.inf.is.soop.webApp.webservice.DataWSServiceLocator()).getDataWSPort();
      if (dataWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)dataWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)dataWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (dataWS != null)
      ((javax.xml.rpc.Stub)dataWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public de.uniol.inf.is.soop.webApp.webservice.DataWS getDataWS() {
    if (dataWS == null)
      _initDataWSProxy();
    return dataWS;
  }
  
  public de.uniol.inf.is.soop.webApp.webservice.StringResponse pushData(java.lang.String token, java.lang.String payload) throws java.rmi.RemoteException{
    if (dataWS == null)
      _initDataWSProxy();
    return dataWS.pushData(token, payload);
  }
  
  
}