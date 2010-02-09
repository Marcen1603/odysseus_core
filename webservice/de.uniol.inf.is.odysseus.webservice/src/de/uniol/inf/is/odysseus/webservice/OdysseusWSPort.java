package de.uniol.inf.is.odysseus.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.5
 * Tue Feb 09 14:01:03 CET 2010
 * Generated source version: 2.2.5
 * 
 */
 
@WebService(targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "OdysseusWSPort")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface OdysseusWSPort {

    @WebResult(name = "queryID", targetNamespace = "", partName = "queryID")
    @WebMethod(operationName = "CreateStatement", action = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/CreateStatement")
    public java.lang.String createStatement(
        @WebParam(partName = "query", name = "query", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
        QueryType query
    ) throws StatementQueryFault, StatementServiceFault;

    @WebResult(name = "actuatorName", targetNamespace = "", partName = "actuatorName")
    @WebMethod(operationName = "CreateActuator", action = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/CreateActuator")
    public java.lang.String createActuator(
        @WebParam(partName = "actuator", name = "actuator", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
        ActuatorInformation actuator
    ) throws ActuatorServiceFault, ActuatorFault, ActuatorManagerFault;

    @WebResult(name = "status", targetNamespace = "", partName = "status")
    @WebMethod(operationName = "CreateSource", action = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/CreateSource")
    public java.lang.String createSource(
        @WebParam(partName = "sourceDescription", name = "sourceDescription", targetNamespace = "")
        java.lang.String sourceDescription
    ) throws SourceQueryFault, SourceServiceFault;

    @XmlList
    @WebResult(name = "schema", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", partName = "schema")
    @WebMethod(operationName = "GetSchema", action = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/GetSchema")
    public java.lang.String[] getSchema(
        @WebParam(partName = "id", name = "id", targetNamespace = "")
        java.lang.String id
    ) throws SchemaServiceFault, SchemaIDFault;

    @WebResult(name = "status", targetNamespace = "", partName = "status")
    @WebMethod(operationName = "RemoveActuator", action = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/RemoveActuator")
    public java.lang.String removeActuator(
        @WebParam(partName = "actuator", name = "actuatorName", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
        ActuatorReducedInformation actuator
    ) throws RemoveActuatorFault;
}
