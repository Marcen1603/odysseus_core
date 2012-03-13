
package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "WebserviceServer", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/")
@XmlSeeAlso({
    net.java.dev.jaxb.array.ObjectFactory.class,
    de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.ObjectFactory.class
})
public interface WebserviceServer {


    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getName", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetName")
    @ResponseWrapper(localName = "getNameResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetNameResponse")
    public StringResponse getName(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.BooleanResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "isRunning", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.IsRunning")
    @ResponseWrapper(localName = "isRunningResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.IsRunningResponse")
    public BooleanResponse isRunning(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param query
     * @param transformationconfig
     * @param parser
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.IntegerCollectionResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addQuery", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.AddQuery")
    @ResponseWrapper(localName = "addQueryResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.AddQueryResponse")
    public IntegerCollectionResponse addQuery(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken,
        @WebParam(name = "parser", targetNamespace = "")
        String parser,
        @WebParam(name = "query", targetNamespace = "")
        String query,
        @WebParam(name = "transformationconfig", targetNamespace = "")
        String transformationconfig);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringListResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getInstalledSources", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetInstalledSources")
    @ResponseWrapper(localName = "getInstalledSourcesResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetInstalledSourcesResponse")
    public StringListResponse getInstalledSources(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringListResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getInstalledQueries", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetInstalledQueries")
    @ResponseWrapper(localName = "getInstalledQueriesResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetInstalledQueriesResponse")
    public StringListResponse getInstalledQueries(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param id
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.QueryResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getLogicalQuery", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetLogicalQuery")
    @ResponseWrapper(localName = "getLogicalQueryResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetLogicalQueryResponse")
    public QueryResponse getLogicalQuery(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken,
        @WebParam(name = "id", targetNamespace = "")
        String id);

    /**
     * 
     * @param queryID
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.Response
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "removeQuery", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.RemoveQuery")
    @ResponseWrapper(localName = "removeQueryResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.RemoveQueryResponse")
    public Response removeQuery(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken,
        @WebParam(name = "queryID", targetNamespace = "")
        int queryID);

    /**
     * 
     * @param queryID
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.Response
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "startQuery", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StartQuery")
    @ResponseWrapper(localName = "startQueryResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StartQueryResponse")
    public Response startQuery(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken,
        @WebParam(name = "queryID", targetNamespace = "")
        int queryID);

    /**
     * 
     * @param queryID
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.Response
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "stopQuery", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StopQuery")
    @ResponseWrapper(localName = "stopQueryResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StopQueryResponse")
    public Response stopQuery(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken,
        @WebParam(name = "queryID", targetNamespace = "")
        int queryID);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.Response
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "stopExecution", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StopExecution")
    @ResponseWrapper(localName = "stopExecutionResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StopExecutionResponse")
    public Response stopExecution(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getInfos", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetInfos")
    @ResponseWrapper(localName = "getInfosResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetInfosResponse")
    public StringResponse getInfos(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.Response
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "startAllClosedQueries", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StartAllClosedQueries")
    @ResponseWrapper(localName = "startAllClosedQueriesResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StartAllClosedQueriesResponse")
    public Response startAllClosedQueries(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringListResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getRegisteredBufferPlacementStrategiesIDs", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetRegisteredBufferPlacementStrategiesIDs")
    @ResponseWrapper(localName = "getRegisteredBufferPlacementStrategiesIDsResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetRegisteredBufferPlacementStrategiesIDsResponse")
    public StringListResponse getRegisteredBufferPlacementStrategiesIDs(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringListResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getRegisteredSchedulingStrategies", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetRegisteredSchedulingStrategies")
    @ResponseWrapper(localName = "getRegisteredSchedulingStrategiesResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetRegisteredSchedulingStrategiesResponse")
    public StringListResponse getRegisteredSchedulingStrategies(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringListResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getRegisteredSchedulers", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetRegisteredSchedulers")
    @ResponseWrapper(localName = "getRegisteredSchedulersResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetRegisteredSchedulersResponse")
    public StringListResponse getRegisteredSchedulers(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param scheduler
     * @param schedulerStrategy
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.Response
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "setScheduler", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.SetScheduler")
    @ResponseWrapper(localName = "setSchedulerResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.SetSchedulerResponse")
    public Response setScheduler(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken,
        @WebParam(name = "scheduler", targetNamespace = "")
        String scheduler,
        @WebParam(name = "scheduler_strategy", targetNamespace = "")
        String schedulerStrategy);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.SimpleGraph
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getPlan", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetPlan")
    @ResponseWrapper(localName = "getPlanResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetPlanResponse")
    public SimpleGraph getPlan(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getCurrentSchedulerID", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetCurrentSchedulerID")
    @ResponseWrapper(localName = "getCurrentSchedulerIDResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetCurrentSchedulerIDResponse")
    public StringResponse getCurrentSchedulerID(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getCurrentSchedulingStrategyID", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetCurrentSchedulingStrategyID")
    @ResponseWrapper(localName = "getCurrentSchedulingStrategyIDResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetCurrentSchedulingStrategyIDResponse")
    public StringResponse getCurrentSchedulingStrategyID(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.Response
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "startExecution", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StartExecution")
    @ResponseWrapper(localName = "startExecutionResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StartExecutionResponse")
    public Response startExecution(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringListResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getQueryBuildConfigurationNames", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetQueryBuildConfigurationNames")
    @ResponseWrapper(localName = "getQueryBuildConfigurationNamesResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetQueryBuildConfigurationNamesResponse")
    public StringListResponse getQueryBuildConfigurationNames(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

    /**
     * 
     * @param username
     * @param password
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringResponse
     */
    @WebMethod
    @WebResult(name = "securitytoken", targetNamespace = "")
    @RequestWrapper(localName = "login", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.Login")
    @ResponseWrapper(localName = "loginResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.LoginResponse")
    public StringResponse login(
        @WebParam(name = "username", targetNamespace = "")
        String username,
        @WebParam(name = "password", targetNamespace = "")
        String password);

    /**
     * 
     * @param securitytoken
     * @return
     *     returns de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.StringListResponse
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSupportedQueryParsers", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetSupportedQueryParsers")
    @ResponseWrapper(localName = "getSupportedQueryParsersResponse", targetNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/", className = "de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.GetSupportedQueryParsersResponse")
    public StringListResponse getSupportedQueryParsers(
        @WebParam(name = "securitytoken", targetNamespace = "")
        String securitytoken);

}
