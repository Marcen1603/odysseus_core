
package de.uniol.inf.is.odysseus.sensormanagement.client;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SensorService", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/")
@XmlSeeAlso({
    ObjectFactory.class
})
@SuppressWarnings(value = { "all" })
	public interface SensorService {


    /**
     * 
     * @param securityToken
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @WebResult(name = "sensorTypes", targetNamespace = "")
    @RequestWrapper(localName = "getSensorTypes", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.GetSensorTypes")
    @ResponseWrapper(localName = "getSensorTypesResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.GetSensorTypesResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorTypesRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorTypesResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorTypes/Fault/InvalidUserDataException")
    })
    public List<String> getSensorTypes(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken)
        throws InvalidUserDataException_Exception
    ;

    /**
     * 
     * @param securityToken
     * @param name
     * @param loggingDirectory
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "initService", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.InitService")
    @ResponseWrapper(localName = "initServiceResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.InitServiceResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/initServiceRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/initServiceResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/initService/Fault/InvalidUserDataException")
    })
    public void initService(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken,
        @WebParam(name = "name", targetNamespace = "")
        String name,
        @WebParam(name = "loggingDirectory", targetNamespace = "")
        String loggingDirectory)
        throws InvalidUserDataException_Exception
    ;

    /**
     * 
     * @param securityToken
     * @param sensorXml
     * @return
     *     returns java.lang.String
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addSensor", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.AddSensor")
    @ResponseWrapper(localName = "addSensorResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.AddSensorResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/addSensorRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/addSensorResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/addSensor/Fault/InvalidUserDataException")
    })
    public String addSensor(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken,
        @WebParam(name = "sensorXml", targetNamespace = "")
        String sensorXml)
        throws InvalidUserDataException_Exception
    ;

    /**
     * 
     * @param securityToken
     * @param sensorXml
     * @param sensorId
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "modifySensor", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.ModifySensor")
    @ResponseWrapper(localName = "modifySensorResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.ModifySensorResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/modifySensorRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/modifySensorResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/modifySensor/Fault/InvalidUserDataException")
    })
    public void modifySensor(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken,
        @WebParam(name = "sensorId", targetNamespace = "")
        String sensorId,
        @WebParam(name = "sensorXml", targetNamespace = "")
        String sensorXml)
        throws InvalidUserDataException_Exception
    ;

    /**
     * 
     * @param securityToken
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @WebResult(name = "sensorIds", targetNamespace = "")
    @RequestWrapper(localName = "getSensorIds", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.GetSensorIds")
    @ResponseWrapper(localName = "getSensorIdsResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.GetSensorIdsResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorIdsRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorIdsResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorIds/Fault/InvalidUserDataException")
    })
    public List<String> getSensorIds(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken)
        throws InvalidUserDataException_Exception
    ;

    /**
     * 
     * @param securityToken
     * @param sensorId
     * @return
     *     returns java.lang.String
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @WebResult(name = "sensorXml", targetNamespace = "")
    @RequestWrapper(localName = "getSensorById", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.GetSensorById")
    @ResponseWrapper(localName = "getSensorByIdResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.GetSensorByIdResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorByIdRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorByIdResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorById/Fault/InvalidUserDataException")
    })
    public String getSensorById(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken,
        @WebParam(name = "sensorId", targetNamespace = "")
        String sensorId)
        throws InvalidUserDataException_Exception
    ;

    /**
     * 
     * @param securityToken
     * @param sensorType
     * @return
     *     returns java.lang.String
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @WebResult(name = "sensorTypeXml", targetNamespace = "")
    @RequestWrapper(localName = "getSensorType", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.GetSensorType")
    @ResponseWrapper(localName = "getSensorTypeResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.GetSensorTypeResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorTypeRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorTypeResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/getSensorType/Fault/InvalidUserDataException")
    })
    public String getSensorType(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken,
        @WebParam(name = "sensorType", targetNamespace = "")
        String sensorType)
        throws InvalidUserDataException_Exception
    ;

    /**
     * 
     * @param securityToken
     * @param sensorId
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "removeSensor", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.RemoveSensor")
    @ResponseWrapper(localName = "removeSensorResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.RemoveSensorResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/removeSensorRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/removeSensorResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/removeSensor/Fault/InvalidUserDataException")
    })
    public void removeSensor(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken,
        @WebParam(name = "sensorId", targetNamespace = "")
        String sensorId)
        throws InvalidUserDataException_Exception
    ;

    /**
     * 
     * @param securityToken
     * @param sensorId
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "startLogging", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.StartLogging")
    @ResponseWrapper(localName = "startLoggingResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.StartLoggingResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/startLoggingRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/startLoggingResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/startLogging/Fault/InvalidUserDataException")
    })
    public void startLogging(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken,
        @WebParam(name = "sensorId", targetNamespace = "")
        String sensorId)
        throws InvalidUserDataException_Exception
    ;

    /**
     * 
     * @param securityToken
     * @param sensorId
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "stopLogging", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.StopLogging")
    @ResponseWrapper(localName = "stopLoggingResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.StopLoggingResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/stopLoggingRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/stopLoggingResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/stopLogging/Fault/InvalidUserDataException")
    })
    public void stopLogging(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken,
        @WebParam(name = "sensorId", targetNamespace = "")
        String sensorId)
        throws InvalidUserDataException_Exception
    ;

    /**
     * 
     * @param securityToken
     * @param targetHost
     * @param targetPort
     * @param sensorId
     * @return
     *     returns java.lang.String
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @WebResult(name = "streamUrl", targetNamespace = "")
    @RequestWrapper(localName = "startLiveView", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.StartLiveView")
    @ResponseWrapper(localName = "startLiveViewResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.StartLiveViewResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/startLiveViewRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/startLiveViewResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/startLiveView/Fault/InvalidUserDataException")
    })
    public String startLiveView(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken,
        @WebParam(name = "sensorId", targetNamespace = "")
        String sensorId,
        @WebParam(name = "targetHost", targetNamespace = "")
        String targetHost,
        @WebParam(name = "targetPort", targetNamespace = "")
        int targetPort)
        throws InvalidUserDataException_Exception
    ;

    /**
     * 
     * @param securityToken
     * @param sensorId
     * @throws InvalidUserDataException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "stopLiveView", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.StopLiveView")
    @ResponseWrapper(localName = "stopLiveViewResponse", targetNamespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.sensormanagement.client.StopLiveViewResponse")
    @Action(input = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/stopLiveViewRequest", output = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/stopLiveViewResponse", fault = {
        @FaultAction(className = InvalidUserDataException_Exception.class, value = "http://server.sensormanagement.odysseus.is.inf.uniol.de/SensorService/stopLiveView/Fault/InvalidUserDataException")
    })
    public void stopLiveView(
        @WebParam(name = "securityToken", targetNamespace = "")
        String securityToken,
        @WebParam(name = "sensorId", targetNamespace = "")
        String sensorId)
        throws InvalidUserDataException_Exception
    ;

}
