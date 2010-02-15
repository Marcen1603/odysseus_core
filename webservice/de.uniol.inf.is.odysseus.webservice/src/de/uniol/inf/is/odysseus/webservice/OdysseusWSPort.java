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
 * Mon Feb 15 14:26:42 CET 2010
 * Generated source version: 2.2.5
 * 
 */
 
@WebService(targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "OdysseusWSPort")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface OdysseusWSPort {

    @WebResult(name = "actuatorName", targetNamespace = "", partName = "actuatorName")
    @WebMethod(operationName = "CreateActuator", action = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/CreateActuator")
    public java.lang.String createActuator(
        @WebParam(partName = "actuator", name = "actuator", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
        ActuatorInformation actuator
    ) throws ActuatorServiceFault, ActuatorFault, ActuatorManagerFault;

    @WebResult(name = "queryID", targetNamespace = "", partName = "queryID")
    @WebMethod(operationName = "AddStatement", action = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/AddStatement")
    public int addStatement(
        @WebParam(partName = "query", name = "query", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
        QueryType query
    ) throws StatementQueryFault, StatementServiceFault;

    @WebResult(name = "status", targetNamespace = "", partName = "status")
    @WebMethod(operationName = "CreateSource", action = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/CreateSource")
    public java.lang.String createSource(
        @WebParam(partName = "sourceDescription", name = "source", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
        java.lang.String sourceDescription
    ) throws SourceQueryFault, SourceServiceFault;

    @XmlList
    @WebResult(name = "schema", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", partName = "schema")
    @WebMethod(operationName = "GetSchema", action = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/GetSchema")
    public java.lang.String[] getSchema(
        @WebParam(partName = "queryID", name = "id", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
        java.lang.String queryID
    ) throws SchemaServiceFault, SchemaIDFault;

    @WebResult(name = "null", targetNamespace = "", partName = "null")
    @WebMethod(operationName = "RemoveStatement")
    public java.lang.String removeStatement(
        @WebParam(partName = "queryID", name = "queryID", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
        java.math.BigInteger queryID
    ) throws RemoveStatementFault;

    @WebResult(name = "null", targetNamespace = "", partName = "null")
    @WebMethod(operationName = "RemoveActuator", action = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/RemoveActuator")
    public java.lang.String removeActuator(
        @WebParam(partName = "actuator", name = "actuatorName", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
        ActuatorReducedInformation actuator
    ) throws RemoveActuatorFault;
}
