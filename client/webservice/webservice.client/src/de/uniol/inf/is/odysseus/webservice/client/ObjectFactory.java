
package de.uniol.inf.is.odysseus.webservice.client;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;					
				

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.uniol.inf.is.odysseus.webservice.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
@SuppressWarnings(value = { "all" })
public class ObjectFactory {

    private final static QName _GetName_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getName");
    private final static QName _GetStreamsAndViews_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getStreamsAndViews");
    private final static QName _StopQuery_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "stopQuery");
    private final static QName _GetInfosResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getInfosResponse");
    private final static QName _RemoveViewOrStreamByResource_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeViewOrStreamByResource");
    private final static QName _GetTransportValues_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getTransportValues");
    private final static QName _GetOperatorNamesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOperatorNamesResponse");
    private final static QName _GetInstalledSourcesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getInstalledSourcesResponse");
    private final static QName _RemoveSinkByNameResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeSinkByNameResponse");
    private final static QName _GetSupportedQueryParsersResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getSupportedQueryParsersResponse");
    private final static QName _GetCurrentSchedulerID_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getCurrentSchedulerID");
    private final static QName _GetSupportedQueryParsers_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getSupportedQueryParsers");
    private final static QName _GetLogicalQueryByName_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getLogicalQueryByName");
    private final static QName _GetOperatorInformation_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOperatorInformation");
    private final static QName _GetPlanByQueryID_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getPlanByQueryID");
    private final static QName _StartQueryResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "startQueryResponse");
    private final static QName _GetStoredProcedures_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getStoredProcedures");
    private final static QName _LoginResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "loginResponse");
    private final static QName _ContainsStoredProcedures_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "containsStoredProcedures");
    private final static QName _GetNameResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getNameResponse");
    private final static QName _SimpleGraph_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "simpleGraph");
    private final static QName _GetConnectionInformationWithMetadataResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getConnectionInformationWithMetadataResponse");
    private final static QName _GetLogicalQueryById_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getLogicalQueryById");
    private final static QName _StopExecutionResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "stopExecutionResponse");
    private final static QName _GetSinks_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getSinks");
    private final static QName _GetRegisteredAggregateFunctions_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredAggregateFunctions");
    private final static QName _GetInstalledQueriesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getInstalledQueriesResponse");
    private final static QName _AddStoredProcedure_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "addStoredProcedure");
    private final static QName _ContainsViewOrStreamByResource_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "containsViewOrStreamByResource");
    private final static QName _GetMetadataNames_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getMetadataNames");
    private final static QName _GetOutputSchemaBySource_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOutputSchemaBySource");
    private final static QName _InvalidUserDataException_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "InvalidUserDataException");
    private final static QName _GetSinksResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getSinksResponse");
    private final static QName _RemoveSinkByName_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeSinkByName");
    private final static QName _GetRegisteredAggregateFunctionsResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredAggregateFunctionsResponse");
    private final static QName _SuspendQuery_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "suspendQuery");
    private final static QName _GetQueryParserTokensResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getQueryParserTokensResponse");
    private final static QName _LogoutResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "logoutResponse");
    private final static QName _AddQuery2_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "addQuery2");
    private final static QName _ContainsViewOrStreamByResourceResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "containsViewOrStreamByResourceResponse");
    private final static QName _DetermineOutputSchemaException_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "DetermineOutputSchemaException");
    private final static QName _GetSources_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getSources");
    private final static QName _GetLogicalQueryByIdResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getLogicalQueryByIdResponse");
    private final static QName _GetOutputSchemaByQueryIdAndPort_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOutputSchemaByQueryIdAndPort");
    private final static QName _GetConnectionInformationWithSSL_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getConnectionInformationWithSSL");
    private final static QName _GetRegisteredBufferPlacementStrategiesIDs_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredBufferPlacementStrategiesIDs");
    private final static QName _GetRegisteredWrapperNamesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredWrapperNamesResponse");
    private final static QName _ReloadStoredQueriesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "reloadStoredQueriesResponse");
    private final static QName _GetStoredProcedureResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getStoredProcedureResponse");
    private final static QName _GetQueryParserSuggestionsResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getQueryParserSuggestionsResponse");
    private final static QName _GetStoredProceduresResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getStoredProceduresResponse");
    private final static QName _IsRunning_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "isRunning");
    private final static QName _RunCommand_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "runCommand");
    private final static QName _DetermineOutputSchemaResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "determineOutputSchemaResponse");
    private final static QName _DetermineOutputSchema_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "determineOutputSchema");
    private final static QName _SetSchedulerResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "setSchedulerResponse");
    private final static QName _GetRegisteredBufferPlacementStrategiesIDsResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredBufferPlacementStrategiesIDsResponse");
    private final static QName _ContainsViewOrStreamByNameResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "containsViewOrStreamByNameResponse");
    private final static QName _CreateQueryException_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "CreateQueryException");
    private final static QName _GetLogicalQueryIds_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getLogicalQueryIds");
    private final static QName _GetOperatorBuilderList_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOperatorBuilderList");
    private final static QName _StartAllClosedQueries_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "startAllClosedQueries");
    private final static QName _GetLogicalQueryPlan_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getLogicalQueryPlan");
    private final static QName _GetOperatorNames_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOperatorNames");
    private final static QName _GetPlanResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getPlanResponse");
    private final static QName _GetRegisteredDatatypes_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredDatatypes");
    private final static QName _IsValidSession_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "isValidSession");
    private final static QName _StartQuery_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "startQuery");
    private final static QName _GetInstalledQueries_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getInstalledQueries");
    private final static QName _ContainsStoredProceduresResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "containsStoredProceduresResponse");
    private final static QName _GetQueryBuildConfigurationNames_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getQueryBuildConfigurationNames");
    private final static QName _GetQueryStateResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getQueryStateResponse");
    private final static QName _GetLogicalQueryResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getLogicalQueryResponse");
    private final static QName _GetOutputSchemaByQueryIdResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOutputSchemaByQueryIdResponse");
    private final static QName _RemoveViewOrStreamByResourceResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeViewOrStreamByResourceResponse");
    private final static QName _StartAllClosedQueriesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "startAllClosedQueriesResponse");
    private final static QName _GetQueryBuildConfigurationNamesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getQueryBuildConfigurationNamesResponse");
    private final static QName _GetTransportValuesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getTransportValuesResponse");
    private final static QName _ReloadStoredQueries_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "reloadStoredQueries");
    private final static QName _GetOperatorBuilderListResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOperatorBuilderListResponse");
    private final static QName _GetQueryState_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getQueryState");
    private final static QName _RemoveStoredProcedure_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeStoredProcedure");
    private final static QName _StartExecution_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "startExecution");
    private final static QName _Logout_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "logout");
    private final static QName _GetQueryStates_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getQueryStates");
    private final static QName _StartExecutionResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "startExecutionResponse");
    private final static QName _GetOutputSchemaBySourceResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOutputSchemaBySourceResponse");
    private final static QName _RemoveQuery_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeQuery");
    private final static QName _GetMetadataNamesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getMetadataNamesResponse");
    private final static QName _ResumeQuery_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "resumeQuery");
    private final static QName _AddStoredProcedureResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "addStoredProcedureResponse");
    private final static QName _GetPlan_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getPlan");
    private final static QName _GetConnectionInformation_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getConnectionInformation");
    private final static QName _GetDataHandlerValues_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getDataHandlerValues");
    private final static QName _GetRegisteredSchedulingStrategiesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredSchedulingStrategiesResponse");
    private final static QName _GetCurrentSchedulerIDResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getCurrentSchedulerIDResponse");
    private final static QName _RemoveQueryResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeQueryResponse");
    private final static QName _RemoveSinkByResource_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeSinkByResource");
    private final static QName _StopExecution_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "stopExecution");
    private final static QName _GetConnectionInformationResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getConnectionInformationResponse");
    private final static QName _GetCurrentSchedulingStrategyIDResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getCurrentSchedulingStrategyIDResponse");
    private final static QName _ResumeQueryResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "resumeQueryResponse");
    private final static QName _GetProtocolValues_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getProtocolValues");
    private final static QName _GetWindowTypesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getWindowTypesResponse");
    private final static QName _GetOperatorInformations_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOperatorInformations");
    private final static QName _SuspendQueryResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "suspendQueryResponse");
    private final static QName _RemoveSinkByResourceResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeSinkByResourceResponse");
    private final static QName _QueryNotExistsException_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "QueryNotExistsException");
    private final static QName _GetInstalledSources_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getInstalledSources");
    private final static QName _GetSourcesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getSourcesResponse");
    private final static QName _GetLogicalQueryPlanResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getLogicalQueryPlanResponse");
    private final static QName _GetPlanByQueryIDResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getPlanByQueryIDResponse");
    private final static QName _GetQueryParserSuggestions_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getQueryParserSuggestions");
    private final static QName _GetRegisteredSchedulersResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredSchedulersResponse");
    private final static QName _GetCurrentSchedulingStrategyID_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getCurrentSchedulingStrategyID");
    private final static QName _GetLogicalQueryIdsResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getLogicalQueryIdsResponse");
    private final static QName _GetRegisteredSchedulers_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredSchedulers");
    private final static QName _IsValidSessionResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "isValidSessionResponse");
    private final static QName _GetLogicalQuery_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getLogicalQuery");
    private final static QName _GetStreamsAndViewsResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getStreamsAndViewsResponse");
    private final static QName _RemoveViewOrStreamByName_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeViewOrStreamByName");
    private final static QName _GetProtocolValuesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getProtocolValuesResponse");
    private final static QName _GetStoredProcedure_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getStoredProcedure");
    private final static QName _GetLogicalQueryByNameResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getLogicalQueryByNameResponse");
    private final static QName _GetOperatorInformationsResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOperatorInformationsResponse");
    private final static QName _Login_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "login");
    private final static QName _GetDataHandlerValuesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getDataHandlerValuesResponse");
    private final static QName _GetRegisteredSchedulingStrategies_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredSchedulingStrategies");
    private final static QName _GetInfos_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getInfos");
    private final static QName _StopQueryResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "stopQueryResponse");
    private final static QName _GetQueryStatesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getQueryStatesResponse");
    private final static QName _SetScheduler_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "setScheduler");
    private final static QName _GetRegisteredDatatypesResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredDatatypesResponse");
    private final static QName _GetConnectionInformationWithMetadata_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getConnectionInformationWithMetadata");
    private final static QName _AddQuery2Response_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "addQuery2Response");
    private final static QName _GetConnectionInformationWithPortsResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getConnectionInformationWithPortsResponse");
    private final static QName _RemoveViewOrStreamByNameResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeViewOrStreamByNameResponse");
    private final static QName _GetConnectionInformationWithPorts_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getConnectionInformationWithPorts");
    private final static QName _GetOutputSchemaByQueryId_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOutputSchemaByQueryId");
    private final static QName _RemoveStoredProcedureResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "removeStoredProcedureResponse");
    private final static QName _Login2Response_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "login2Response");
    private final static QName _RunCommandResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "runCommandResponse");
    private final static QName _GetWindowTypes_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getWindowTypes");
    private final static QName _GetOutputSchemaByQueryIdAndPortResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOutputSchemaByQueryIdAndPortResponse");
    private final static QName _GetQueryParserTokens_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getQueryParserTokens");
    private final static QName _GetRegisteredWrapperNames_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getRegisteredWrapperNames");
    private final static QName _IsRunningResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "isRunningResponse");
    private final static QName _GraphNode_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "graphNode");
    private final static QName _Login2_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "login2");
    private final static QName _ContainsViewOrStreamByName_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "containsViewOrStreamByName");
    private final static QName _GetConnectionInformationWithSSLResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getConnectionInformationWithSSLResponse");
    private final static QName _GetOperatorInformationResponse_QNAME = new QName("http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", "getOperatorInformationResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.uniol.inf.is.odysseus.webservice.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LogicalQueryInfo }
     * 
     */
    public LogicalQueryInfo createLogicalQueryInfo() {
        return new LogicalQueryInfo();
    }

    /**
     * Create an instance of {@link LogicalQueryInfo.UserParameters }
     * 
     */
    public LogicalQueryInfo.UserParameters createLogicalQueryInfoUserParameters() {
        return new LogicalQueryInfo.UserParameters();
    }

    /**
     * Create an instance of {@link GraphNode }
     * 
     */
    public GraphNode createGraphNode() {
        return new GraphNode();
    }

    /**
     * Create an instance of {@link GraphNode.ParameterInfos }
     * 
     */
    public GraphNode.ParameterInfos createGraphNodeParameterInfos() {
        return new GraphNode.ParameterInfos();
    }

    /**
     * Create an instance of {@link GraphNode.ChildsMap }
     * 
     */
    public GraphNode.ChildsMap createGraphNodeChildsMap() {
        return new GraphNode.ChildsMap();
    }

    /**
     * Create an instance of {@link StringArray }
     * 
     */
    public StringArray createStringArray() {
        return new StringArray();
    }

    /**
     * Create an instance of {@link GetQueryBuildConfigurationNamesResponse }
     * 
     */
    public GetQueryBuildConfigurationNamesResponse createGetQueryBuildConfigurationNamesResponse() {
        return new GetQueryBuildConfigurationNamesResponse();
    }

    /**
     * Create an instance of {@link StartAllClosedQueriesResponse }
     * 
     */
    public StartAllClosedQueriesResponse createStartAllClosedQueriesResponse() {
        return new StartAllClosedQueriesResponse();
    }

    /**
     * Create an instance of {@link RemoveViewOrStreamByResourceResponse }
     * 
     */
    public RemoveViewOrStreamByResourceResponse createRemoveViewOrStreamByResourceResponse() {
        return new RemoveViewOrStreamByResourceResponse();
    }

    /**
     * Create an instance of {@link GetTransportValuesResponse }
     * 
     */
    public GetTransportValuesResponse createGetTransportValuesResponse() {
        return new GetTransportValuesResponse();
    }

    /**
     * Create an instance of {@link ReloadStoredQueries }
     * 
     */
    public ReloadStoredQueries createReloadStoredQueries() {
        return new ReloadStoredQueries();
    }

    /**
     * Create an instance of {@link RemoveStoredProcedure }
     * 
     */
    public RemoveStoredProcedure createRemoveStoredProcedure() {
        return new RemoveStoredProcedure();
    }

    /**
     * Create an instance of {@link StartExecution }
     * 
     */
    public StartExecution createStartExecution() {
        return new StartExecution();
    }

    /**
     * Create an instance of {@link GetQueryState }
     * 
     */
    public GetQueryState createGetQueryState() {
        return new GetQueryState();
    }

    /**
     * Create an instance of {@link GetOperatorBuilderListResponse }
     * 
     */
    public GetOperatorBuilderListResponse createGetOperatorBuilderListResponse() {
        return new GetOperatorBuilderListResponse();
    }

    /**
     * Create an instance of {@link GetQueryStates }
     * 
     */
    public GetQueryStates createGetQueryStates() {
        return new GetQueryStates();
    }

    /**
     * Create an instance of {@link Logout }
     * 
     */
    public Logout createLogout() {
        return new Logout();
    }

    /**
     * Create an instance of {@link GetOutputSchemaBySourceResponse }
     * 
     */
    public GetOutputSchemaBySourceResponse createGetOutputSchemaBySourceResponse() {
        return new GetOutputSchemaBySourceResponse();
    }

    /**
     * Create an instance of {@link StartExecutionResponse }
     * 
     */
    public StartExecutionResponse createStartExecutionResponse() {
        return new StartExecutionResponse();
    }

    /**
     * Create an instance of {@link AddStoredProcedureResponse }
     * 
     */
    public AddStoredProcedureResponse createAddStoredProcedureResponse() {
        return new AddStoredProcedureResponse();
    }

    /**
     * Create an instance of {@link GetPlan }
     * 
     */
    public GetPlan createGetPlan() {
        return new GetPlan();
    }

    /**
     * Create an instance of {@link GetMetadataNamesResponse }
     * 
     */
    public GetMetadataNamesResponse createGetMetadataNamesResponse() {
        return new GetMetadataNamesResponse();
    }

    /**
     * Create an instance of {@link ResumeQuery }
     * 
     */
    public ResumeQuery createResumeQuery() {
        return new ResumeQuery();
    }

    /**
     * Create an instance of {@link RemoveQuery }
     * 
     */
    public RemoveQuery createRemoveQuery() {
        return new RemoveQuery();
    }

    /**
     * Create an instance of {@link GetConnectionInformation }
     * 
     */
    public GetConnectionInformation createGetConnectionInformation() {
        return new GetConnectionInformation();
    }

    /**
     * Create an instance of {@link GetDataHandlerValues }
     * 
     */
    public GetDataHandlerValues createGetDataHandlerValues() {
        return new GetDataHandlerValues();
    }

    /**
     * Create an instance of {@link GetRegisteredSchedulingStrategiesResponse }
     * 
     */
    public GetRegisteredSchedulingStrategiesResponse createGetRegisteredSchedulingStrategiesResponse() {
        return new GetRegisteredSchedulingStrategiesResponse();
    }

    /**
     * Create an instance of {@link GetCurrentSchedulerIDResponse }
     * 
     */
    public GetCurrentSchedulerIDResponse createGetCurrentSchedulerIDResponse() {
        return new GetCurrentSchedulerIDResponse();
    }

    /**
     * Create an instance of {@link RemoveQueryResponse }
     * 
     */
    public RemoveQueryResponse createRemoveQueryResponse() {
        return new RemoveQueryResponse();
    }

    /**
     * Create an instance of {@link RemoveSinkByResource }
     * 
     */
    public RemoveSinkByResource createRemoveSinkByResource() {
        return new RemoveSinkByResource();
    }

    /**
     * Create an instance of {@link StopExecution }
     * 
     */
    public StopExecution createStopExecution() {
        return new StopExecution();
    }

    /**
     * Create an instance of {@link GetCurrentSchedulingStrategyIDResponse }
     * 
     */
    public GetCurrentSchedulingStrategyIDResponse createGetCurrentSchedulingStrategyIDResponse() {
        return new GetCurrentSchedulingStrategyIDResponse();
    }

    /**
     * Create an instance of {@link GetConnectionInformationResponse }
     * 
     */
    public GetConnectionInformationResponse createGetConnectionInformationResponse() {
        return new GetConnectionInformationResponse();
    }

    /**
     * Create an instance of {@link GetOperatorInformations }
     * 
     */
    public GetOperatorInformations createGetOperatorInformations() {
        return new GetOperatorInformations();
    }

    /**
     * Create an instance of {@link SuspendQueryResponse }
     * 
     */
    public SuspendQueryResponse createSuspendQueryResponse() {
        return new SuspendQueryResponse();
    }

    /**
     * Create an instance of {@link GetProtocolValues }
     * 
     */
    public GetProtocolValues createGetProtocolValues() {
        return new GetProtocolValues();
    }

    /**
     * Create an instance of {@link GetWindowTypesResponse }
     * 
     */
    public GetWindowTypesResponse createGetWindowTypesResponse() {
        return new GetWindowTypesResponse();
    }

    /**
     * Create an instance of {@link ResumeQueryResponse }
     * 
     */
    public ResumeQueryResponse createResumeQueryResponse() {
        return new ResumeQueryResponse();
    }

    /**
     * Create an instance of {@link GetInstalledSources }
     * 
     */
    public GetInstalledSources createGetInstalledSources() {
        return new GetInstalledSources();
    }

    /**
     * Create an instance of {@link QueryNotExistsException }
     * 
     */
    public QueryNotExistsException createQueryNotExistsException() {
        return new QueryNotExistsException();
    }

    /**
     * Create an instance of {@link RemoveSinkByResourceResponse }
     * 
     */
    public RemoveSinkByResourceResponse createRemoveSinkByResourceResponse() {
        return new RemoveSinkByResourceResponse();
    }

    /**
     * Create an instance of {@link GetRegisteredSchedulersResponse }
     * 
     */
    public GetRegisteredSchedulersResponse createGetRegisteredSchedulersResponse() {
        return new GetRegisteredSchedulersResponse();
    }

    /**
     * Create an instance of {@link GetLogicalQueryPlanResponse }
     * 
     */
    public GetLogicalQueryPlanResponse createGetLogicalQueryPlanResponse() {
        return new GetLogicalQueryPlanResponse();
    }

    /**
     * Create an instance of {@link GetPlanByQueryIDResponse }
     * 
     */
    public GetPlanByQueryIDResponse createGetPlanByQueryIDResponse() {
        return new GetPlanByQueryIDResponse();
    }

    /**
     * Create an instance of {@link GetQueryParserSuggestions }
     * 
     */
    public GetQueryParserSuggestions createGetQueryParserSuggestions() {
        return new GetQueryParserSuggestions();
    }

    /**
     * Create an instance of {@link GetSourcesResponse }
     * 
     */
    public GetSourcesResponse createGetSourcesResponse() {
        return new GetSourcesResponse();
    }

    /**
     * Create an instance of {@link GetCurrentSchedulingStrategyID }
     * 
     */
    public GetCurrentSchedulingStrategyID createGetCurrentSchedulingStrategyID() {
        return new GetCurrentSchedulingStrategyID();
    }

    /**
     * Create an instance of {@link GetLogicalQueryIdsResponse }
     * 
     */
    public GetLogicalQueryIdsResponse createGetLogicalQueryIdsResponse() {
        return new GetLogicalQueryIdsResponse();
    }

    /**
     * Create an instance of {@link GetRegisteredSchedulers }
     * 
     */
    public GetRegisteredSchedulers createGetRegisteredSchedulers() {
        return new GetRegisteredSchedulers();
    }

    /**
     * Create an instance of {@link IsValidSessionResponse }
     * 
     */
    public IsValidSessionResponse createIsValidSessionResponse() {
        return new IsValidSessionResponse();
    }

    /**
     * Create an instance of {@link GetLogicalQuery }
     * 
     */
    public GetLogicalQuery createGetLogicalQuery() {
        return new GetLogicalQuery();
    }

    /**
     * Create an instance of {@link GetStreamsAndViewsResponse }
     * 
     */
    public GetStreamsAndViewsResponse createGetStreamsAndViewsResponse() {
        return new GetStreamsAndViewsResponse();
    }

    /**
     * Create an instance of {@link RemoveViewOrStreamByName }
     * 
     */
    public RemoveViewOrStreamByName createRemoveViewOrStreamByName() {
        return new RemoveViewOrStreamByName();
    }

    /**
     * Create an instance of {@link GetProtocolValuesResponse }
     * 
     */
    public GetProtocolValuesResponse createGetProtocolValuesResponse() {
        return new GetProtocolValuesResponse();
    }

    /**
     * Create an instance of {@link GetStoredProcedure }
     * 
     */
    public GetStoredProcedure createGetStoredProcedure() {
        return new GetStoredProcedure();
    }

    /**
     * Create an instance of {@link Login }
     * 
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link GetOperatorInformationsResponse }
     * 
     */
    public GetOperatorInformationsResponse createGetOperatorInformationsResponse() {
        return new GetOperatorInformationsResponse();
    }

    /**
     * Create an instance of {@link GetLogicalQueryByNameResponse }
     * 
     */
    public GetLogicalQueryByNameResponse createGetLogicalQueryByNameResponse() {
        return new GetLogicalQueryByNameResponse();
    }

    /**
     * Create an instance of {@link GetDataHandlerValuesResponse }
     * 
     */
    public GetDataHandlerValuesResponse createGetDataHandlerValuesResponse() {
        return new GetDataHandlerValuesResponse();
    }

    /**
     * Create an instance of {@link GetRegisteredSchedulingStrategies }
     * 
     */
    public GetRegisteredSchedulingStrategies createGetRegisteredSchedulingStrategies() {
        return new GetRegisteredSchedulingStrategies();
    }

    /**
     * Create an instance of {@link GetQueryStatesResponse }
     * 
     */
    public GetQueryStatesResponse createGetQueryStatesResponse() {
        return new GetQueryStatesResponse();
    }

    /**
     * Create an instance of {@link SetScheduler }
     * 
     */
    public SetScheduler createSetScheduler() {
        return new SetScheduler();
    }

    /**
     * Create an instance of {@link StopQueryResponse }
     * 
     */
    public StopQueryResponse createStopQueryResponse() {
        return new StopQueryResponse();
    }

    /**
     * Create an instance of {@link GetInfos }
     * 
     */
    public GetInfos createGetInfos() {
        return new GetInfos();
    }

    /**
     * Create an instance of {@link GetRegisteredDatatypesResponse }
     * 
     */
    public GetRegisteredDatatypesResponse createGetRegisteredDatatypesResponse() {
        return new GetRegisteredDatatypesResponse();
    }

    /**
     * Create an instance of {@link GetConnectionInformationWithMetadata }
     * 
     */
    public GetConnectionInformationWithMetadata createGetConnectionInformationWithMetadata() {
        return new GetConnectionInformationWithMetadata();
    }

    /**
     * Create an instance of {@link AddQuery2Response }
     * 
     */
    public AddQuery2Response createAddQuery2Response() {
        return new AddQuery2Response();
    }

    /**
     * Create an instance of {@link GetConnectionInformationWithPortsResponse }
     * 
     */
    public GetConnectionInformationWithPortsResponse createGetConnectionInformationWithPortsResponse() {
        return new GetConnectionInformationWithPortsResponse();
    }

    /**
     * Create an instance of {@link GetOutputSchemaByQueryId }
     * 
     */
    public GetOutputSchemaByQueryId createGetOutputSchemaByQueryId() {
        return new GetOutputSchemaByQueryId();
    }

    /**
     * Create an instance of {@link RemoveStoredProcedureResponse }
     * 
     */
    public RemoveStoredProcedureResponse createRemoveStoredProcedureResponse() {
        return new RemoveStoredProcedureResponse();
    }

    /**
     * Create an instance of {@link GetConnectionInformationWithPorts }
     * 
     */
    public GetConnectionInformationWithPorts createGetConnectionInformationWithPorts() {
        return new GetConnectionInformationWithPorts();
    }

    /**
     * Create an instance of {@link RemoveViewOrStreamByNameResponse }
     * 
     */
    public RemoveViewOrStreamByNameResponse createRemoveViewOrStreamByNameResponse() {
        return new RemoveViewOrStreamByNameResponse();
    }

    /**
     * Create an instance of {@link GetWindowTypes }
     * 
     */
    public GetWindowTypes createGetWindowTypes() {
        return new GetWindowTypes();
    }

    /**
     * Create an instance of {@link Login2Response }
     * 
     */
    public Login2Response createLogin2Response() {
        return new Login2Response();
    }

    /**
     * Create an instance of {@link RunCommandResponse }
     * 
     */
    public RunCommandResponse createRunCommandResponse() {
        return new RunCommandResponse();
    }

    /**
     * Create an instance of {@link GetOutputSchemaByQueryIdAndPortResponse }
     * 
     */
    public GetOutputSchemaByQueryIdAndPortResponse createGetOutputSchemaByQueryIdAndPortResponse() {
        return new GetOutputSchemaByQueryIdAndPortResponse();
    }

    /**
     * Create an instance of {@link GetRegisteredWrapperNames }
     * 
     */
    public GetRegisteredWrapperNames createGetRegisteredWrapperNames() {
        return new GetRegisteredWrapperNames();
    }

    /**
     * Create an instance of {@link GetQueryParserTokens }
     * 
     */
    public GetQueryParserTokens createGetQueryParserTokens() {
        return new GetQueryParserTokens();
    }

    /**
     * Create an instance of {@link IsRunningResponse }
     * 
     */
    public IsRunningResponse createIsRunningResponse() {
        return new IsRunningResponse();
    }

    /**
     * Create an instance of {@link ContainsViewOrStreamByName }
     * 
     */
    public ContainsViewOrStreamByName createContainsViewOrStreamByName() {
        return new ContainsViewOrStreamByName();
    }

    /**
     * Create an instance of {@link GetConnectionInformationWithSSLResponse }
     * 
     */
    public GetConnectionInformationWithSSLResponse createGetConnectionInformationWithSSLResponse() {
        return new GetConnectionInformationWithSSLResponse();
    }

    /**
     * Create an instance of {@link GetOperatorInformationResponse }
     * 
     */
    public GetOperatorInformationResponse createGetOperatorInformationResponse() {
        return new GetOperatorInformationResponse();
    }

    /**
     * Create an instance of {@link Login2 }
     * 
     */
    public Login2 createLogin2() {
        return new Login2();
    }

    /**
     * Create an instance of {@link StopQuery }
     * 
     */
    public StopQuery createStopQuery() {
        return new StopQuery();
    }

    /**
     * Create an instance of {@link GetName }
     * 
     */
    public GetName createGetName() {
        return new GetName();
    }

    /**
     * Create an instance of {@link GetStreamsAndViews }
     * 
     */
    public GetStreamsAndViews createGetStreamsAndViews() {
        return new GetStreamsAndViews();
    }

    /**
     * Create an instance of {@link GetInfosResponse }
     * 
     */
    public GetInfosResponse createGetInfosResponse() {
        return new GetInfosResponse();
    }

    /**
     * Create an instance of {@link GetTransportValues }
     * 
     */
    public GetTransportValues createGetTransportValues() {
        return new GetTransportValues();
    }

    /**
     * Create an instance of {@link RemoveViewOrStreamByResource }
     * 
     */
    public RemoveViewOrStreamByResource createRemoveViewOrStreamByResource() {
        return new RemoveViewOrStreamByResource();
    }

    /**
     * Create an instance of {@link GetInstalledSourcesResponse }
     * 
     */
    public GetInstalledSourcesResponse createGetInstalledSourcesResponse() {
        return new GetInstalledSourcesResponse();
    }

    /**
     * Create an instance of {@link GetOperatorNamesResponse }
     * 
     */
    public GetOperatorNamesResponse createGetOperatorNamesResponse() {
        return new GetOperatorNamesResponse();
    }

    /**
     * Create an instance of {@link GetCurrentSchedulerID }
     * 
     */
    public GetCurrentSchedulerID createGetCurrentSchedulerID() {
        return new GetCurrentSchedulerID();
    }

    /**
     * Create an instance of {@link GetSupportedQueryParsersResponse }
     * 
     */
    public GetSupportedQueryParsersResponse createGetSupportedQueryParsersResponse() {
        return new GetSupportedQueryParsersResponse();
    }

    /**
     * Create an instance of {@link RemoveSinkByNameResponse }
     * 
     */
    public RemoveSinkByNameResponse createRemoveSinkByNameResponse() {
        return new RemoveSinkByNameResponse();
    }

    /**
     * Create an instance of {@link GetOperatorInformation }
     * 
     */
    public GetOperatorInformation createGetOperatorInformation() {
        return new GetOperatorInformation();
    }

    /**
     * Create an instance of {@link GetLogicalQueryByName }
     * 
     */
    public GetLogicalQueryByName createGetLogicalQueryByName() {
        return new GetLogicalQueryByName();
    }

    /**
     * Create an instance of {@link GetSupportedQueryParsers }
     * 
     */
    public GetSupportedQueryParsers createGetSupportedQueryParsers() {
        return new GetSupportedQueryParsers();
    }

    /**
     * Create an instance of {@link GetStoredProcedures }
     * 
     */
    public GetStoredProcedures createGetStoredProcedures() {
        return new GetStoredProcedures();
    }

    /**
     * Create an instance of {@link StartQueryResponse }
     * 
     */
    public StartQueryResponse createStartQueryResponse() {
        return new StartQueryResponse();
    }

    /**
     * Create an instance of {@link GetPlanByQueryID }
     * 
     */
    public GetPlanByQueryID createGetPlanByQueryID() {
        return new GetPlanByQueryID();
    }

    /**
     * Create an instance of {@link ContainsStoredProcedures }
     * 
     */
    public ContainsStoredProcedures createContainsStoredProcedures() {
        return new ContainsStoredProcedures();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link GetConnectionInformationWithMetadataResponse }
     * 
     */
    public GetConnectionInformationWithMetadataResponse createGetConnectionInformationWithMetadataResponse() {
        return new GetConnectionInformationWithMetadataResponse();
    }

    /**
     * Create an instance of {@link SimpleGraph }
     * 
     */
    public SimpleGraph createSimpleGraph() {
        return new SimpleGraph();
    }

    /**
     * Create an instance of {@link GetNameResponse }
     * 
     */
    public GetNameResponse createGetNameResponse() {
        return new GetNameResponse();
    }

    /**
     * Create an instance of {@link GetRegisteredAggregateFunctions }
     * 
     */
    public GetRegisteredAggregateFunctions createGetRegisteredAggregateFunctions() {
        return new GetRegisteredAggregateFunctions();
    }

    /**
     * Create an instance of {@link GetSinks }
     * 
     */
    public GetSinks createGetSinks() {
        return new GetSinks();
    }

    /**
     * Create an instance of {@link GetLogicalQueryById }
     * 
     */
    public GetLogicalQueryById createGetLogicalQueryById() {
        return new GetLogicalQueryById();
    }

    /**
     * Create an instance of {@link StopExecutionResponse }
     * 
     */
    public StopExecutionResponse createStopExecutionResponse() {
        return new StopExecutionResponse();
    }

    /**
     * Create an instance of {@link GetInstalledQueriesResponse }
     * 
     */
    public GetInstalledQueriesResponse createGetInstalledQueriesResponse() {
        return new GetInstalledQueriesResponse();
    }

    /**
     * Create an instance of {@link AddStoredProcedure }
     * 
     */
    public AddStoredProcedure createAddStoredProcedure() {
        return new AddStoredProcedure();
    }

    /**
     * Create an instance of {@link ContainsViewOrStreamByResource }
     * 
     */
    public ContainsViewOrStreamByResource createContainsViewOrStreamByResource() {
        return new ContainsViewOrStreamByResource();
    }

    /**
     * Create an instance of {@link GetMetadataNames }
     * 
     */
    public GetMetadataNames createGetMetadataNames() {
        return new GetMetadataNames();
    }

    /**
     * Create an instance of {@link GetOutputSchemaBySource }
     * 
     */
    public GetOutputSchemaBySource createGetOutputSchemaBySource() {
        return new GetOutputSchemaBySource();
    }

    /**
     * Create an instance of {@link GetSinksResponse }
     * 
     */
    public GetSinksResponse createGetSinksResponse() {
        return new GetSinksResponse();
    }

    /**
     * Create an instance of {@link RemoveSinkByName }
     * 
     */
    public RemoveSinkByName createRemoveSinkByName() {
        return new RemoveSinkByName();
    }

    /**
     * Create an instance of {@link InvalidUserDataException }
     * 
     */
    public InvalidUserDataException createInvalidUserDataException() {
        return new InvalidUserDataException();
    }

    /**
     * Create an instance of {@link GetRegisteredAggregateFunctionsResponse }
     * 
     */
    public GetRegisteredAggregateFunctionsResponse createGetRegisteredAggregateFunctionsResponse() {
        return new GetRegisteredAggregateFunctionsResponse();
    }

    /**
     * Create an instance of {@link SuspendQuery }
     * 
     */
    public SuspendQuery createSuspendQuery() {
        return new SuspendQuery();
    }

    /**
     * Create an instance of {@link GetQueryParserTokensResponse }
     * 
     */
    public GetQueryParserTokensResponse createGetQueryParserTokensResponse() {
        return new GetQueryParserTokensResponse();
    }

    /**
     * Create an instance of {@link AddQuery2 }
     * 
     */
    public AddQuery2 createAddQuery2() {
        return new AddQuery2();
    }

    /**
     * Create an instance of {@link LogoutResponse }
     * 
     */
    public LogoutResponse createLogoutResponse() {
        return new LogoutResponse();
    }

    /**
     * Create an instance of {@link DetermineOutputSchemaException }
     * 
     */
    public DetermineOutputSchemaException createDetermineOutputSchemaException() {
        return new DetermineOutputSchemaException();
    }

    /**
     * Create an instance of {@link ContainsViewOrStreamByResourceResponse }
     * 
     */
    public ContainsViewOrStreamByResourceResponse createContainsViewOrStreamByResourceResponse() {
        return new ContainsViewOrStreamByResourceResponse();
    }

    /**
     * Create an instance of {@link GetLogicalQueryByIdResponse }
     * 
     */
    public GetLogicalQueryByIdResponse createGetLogicalQueryByIdResponse() {
        return new GetLogicalQueryByIdResponse();
    }

    /**
     * Create an instance of {@link GetSources }
     * 
     */
    public GetSources createGetSources() {
        return new GetSources();
    }

    /**
     * Create an instance of {@link GetConnectionInformationWithSSL }
     * 
     */
    public GetConnectionInformationWithSSL createGetConnectionInformationWithSSL() {
        return new GetConnectionInformationWithSSL();
    }

    /**
     * Create an instance of {@link GetRegisteredBufferPlacementStrategiesIDs }
     * 
     */
    public GetRegisteredBufferPlacementStrategiesIDs createGetRegisteredBufferPlacementStrategiesIDs() {
        return new GetRegisteredBufferPlacementStrategiesIDs();
    }

    /**
     * Create an instance of {@link GetOutputSchemaByQueryIdAndPort }
     * 
     */
    public GetOutputSchemaByQueryIdAndPort createGetOutputSchemaByQueryIdAndPort() {
        return new GetOutputSchemaByQueryIdAndPort();
    }

    /**
     * Create an instance of {@link GetQueryParserSuggestionsResponse }
     * 
     */
    public GetQueryParserSuggestionsResponse createGetQueryParserSuggestionsResponse() {
        return new GetQueryParserSuggestionsResponse();
    }

    /**
     * Create an instance of {@link GetStoredProceduresResponse }
     * 
     */
    public GetStoredProceduresResponse createGetStoredProceduresResponse() {
        return new GetStoredProceduresResponse();
    }

    /**
     * Create an instance of {@link IsRunning }
     * 
     */
    public IsRunning createIsRunning() {
        return new IsRunning();
    }

    /**
     * Create an instance of {@link GetStoredProcedureResponse }
     * 
     */
    public GetStoredProcedureResponse createGetStoredProcedureResponse() {
        return new GetStoredProcedureResponse();
    }

    /**
     * Create an instance of {@link GetRegisteredWrapperNamesResponse }
     * 
     */
    public GetRegisteredWrapperNamesResponse createGetRegisteredWrapperNamesResponse() {
        return new GetRegisteredWrapperNamesResponse();
    }

    /**
     * Create an instance of {@link ReloadStoredQueriesResponse }
     * 
     */
    public ReloadStoredQueriesResponse createReloadStoredQueriesResponse() {
        return new ReloadStoredQueriesResponse();
    }

    /**
     * Create an instance of {@link RunCommand }
     * 
     */
    public RunCommand createRunCommand() {
        return new RunCommand();
    }

    /**
     * Create an instance of {@link DetermineOutputSchema }
     * 
     */
    public DetermineOutputSchema createDetermineOutputSchema() {
        return new DetermineOutputSchema();
    }

    /**
     * Create an instance of {@link SetSchedulerResponse }
     * 
     */
    public SetSchedulerResponse createSetSchedulerResponse() {
        return new SetSchedulerResponse();
    }

    /**
     * Create an instance of {@link DetermineOutputSchemaResponse }
     * 
     */
    public DetermineOutputSchemaResponse createDetermineOutputSchemaResponse() {
        return new DetermineOutputSchemaResponse();
    }

    /**
     * Create an instance of {@link ContainsViewOrStreamByNameResponse }
     * 
     */
    public ContainsViewOrStreamByNameResponse createContainsViewOrStreamByNameResponse() {
        return new ContainsViewOrStreamByNameResponse();
    }

    /**
     * Create an instance of {@link GetRegisteredBufferPlacementStrategiesIDsResponse }
     * 
     */
    public GetRegisteredBufferPlacementStrategiesIDsResponse createGetRegisteredBufferPlacementStrategiesIDsResponse() {
        return new GetRegisteredBufferPlacementStrategiesIDsResponse();
    }

    /**
     * Create an instance of {@link GetLogicalQueryIds }
     * 
     */
    public GetLogicalQueryIds createGetLogicalQueryIds() {
        return new GetLogicalQueryIds();
    }

    /**
     * Create an instance of {@link CreateQueryException }
     * 
     */
    public CreateQueryException createCreateQueryException() {
        return new CreateQueryException();
    }

    /**
     * Create an instance of {@link StartAllClosedQueries }
     * 
     */
    public StartAllClosedQueries createStartAllClosedQueries() {
        return new StartAllClosedQueries();
    }

    /**
     * Create an instance of {@link GetOperatorBuilderList }
     * 
     */
    public GetOperatorBuilderList createGetOperatorBuilderList() {
        return new GetOperatorBuilderList();
    }

    /**
     * Create an instance of {@link GetLogicalQueryPlan }
     * 
     */
    public GetLogicalQueryPlan createGetLogicalQueryPlan() {
        return new GetLogicalQueryPlan();
    }

    /**
     * Create an instance of {@link GetOperatorNames }
     * 
     */
    public GetOperatorNames createGetOperatorNames() {
        return new GetOperatorNames();
    }

    /**
     * Create an instance of {@link IsValidSession }
     * 
     */
    public IsValidSession createIsValidSession() {
        return new IsValidSession();
    }

    /**
     * Create an instance of {@link StartQuery }
     * 
     */
    public StartQuery createStartQuery() {
        return new StartQuery();
    }

    /**
     * Create an instance of {@link GetRegisteredDatatypes }
     * 
     */
    public GetRegisteredDatatypes createGetRegisteredDatatypes() {
        return new GetRegisteredDatatypes();
    }

    /**
     * Create an instance of {@link GetPlanResponse }
     * 
     */
    public GetPlanResponse createGetPlanResponse() {
        return new GetPlanResponse();
    }

    /**
     * Create an instance of {@link GetQueryStateResponse }
     * 
     */
    public GetQueryStateResponse createGetQueryStateResponse() {
        return new GetQueryStateResponse();
    }

    /**
     * Create an instance of {@link GetQueryBuildConfigurationNames }
     * 
     */
    public GetQueryBuildConfigurationNames createGetQueryBuildConfigurationNames() {
        return new GetQueryBuildConfigurationNames();
    }

    /**
     * Create an instance of {@link ContainsStoredProceduresResponse }
     * 
     */
    public ContainsStoredProceduresResponse createContainsStoredProceduresResponse() {
        return new ContainsStoredProceduresResponse();
    }

    /**
     * Create an instance of {@link GetInstalledQueries }
     * 
     */
    public GetInstalledQueries createGetInstalledQueries() {
        return new GetInstalledQueries();
    }

    /**
     * Create an instance of {@link GetOutputSchemaByQueryIdResponse }
     * 
     */
    public GetOutputSchemaByQueryIdResponse createGetOutputSchemaByQueryIdResponse() {
        return new GetOutputSchemaByQueryIdResponse();
    }

    /**
     * Create an instance of {@link GetLogicalQueryResponse }
     * 
     */
    public GetLogicalQueryResponse createGetLogicalQueryResponse() {
        return new GetLogicalQueryResponse();
    }

    /**
     * Create an instance of {@link SourceInformation }
     * 
     */
    public SourceInformation createSourceInformation() {
        return new SourceInformation();
    }

    /**
     * Create an instance of {@link Context }
     * 
     */
    public Context createContext() {
        return new Context();
    }

    /**
     * Create an instance of {@link SdfAttributeInformation }
     * 
     */
    public SdfAttributeInformation createSdfAttributeInformation() {
        return new SdfAttributeInformation();
    }

    /**
     * Create an instance of {@link StringListResponse }
     * 
     */
    public StringListResponse createStringListResponse() {
        return new StringListResponse();
    }

    /**
     * Create an instance of {@link LogicalOperatorInformation }
     * 
     */
    public LogicalOperatorInformation createLogicalOperatorInformation() {
        return new LogicalOperatorInformation();
    }

    /**
     * Create an instance of {@link LogicalOperatorInformationResponse }
     * 
     */
    public LogicalOperatorInformationResponse createLogicalOperatorInformationResponse() {
        return new LogicalOperatorInformationResponse();
    }

    /**
     * Create an instance of {@link SdfSchemaInformation }
     * 
     */
    public SdfSchemaInformation createSdfSchemaInformation() {
        return new SdfSchemaInformation();
    }

    /**
     * Create an instance of {@link SdfDatatypeInformation }
     * 
     */
    public SdfDatatypeInformation createSdfDatatypeInformation() {
        return new SdfDatatypeInformation();
    }

    /**
     * Create an instance of {@link LogicalOperatorInformationListResponse }
     * 
     */
    public LogicalOperatorInformationListResponse createLogicalOperatorInformationListResponse() {
        return new LogicalOperatorInformationListResponse();
    }

    /**
     * Create an instance of {@link StoredProcedure }
     * 
     */
    public StoredProcedure createStoredProcedure() {
        return new StoredProcedure();
    }

    /**
     * Create an instance of {@link LogicalOperatorResponse }
     * 
     */
    public LogicalOperatorResponse createLogicalOperatorResponse() {
        return new LogicalOperatorResponse();
    }

    /**
     * Create an instance of {@link BooleanResponse }
     * 
     */
    public BooleanResponse createBooleanResponse() {
        return new BooleanResponse();
    }

    /**
     * Create an instance of {@link ConnectionInformationResponse }
     * 
     */
    public ConnectionInformationResponse createConnectionInformationResponse() {
        return new ConnectionInformationResponse();
    }

    /**
     * Create an instance of {@link SinkInformationWS }
     * 
     */
    public SinkInformationWS createSinkInformationWS() {
        return new SinkInformationWS();
    }

    /**
     * Create an instance of {@link ConnectionInformation }
     * 
     */
    public ConnectionInformation createConnectionInformation() {
        return new ConnectionInformation();
    }

    /**
     * Create an instance of {@link StringMapListEntry }
     * 
     */
    public StringMapListEntry createStringMapListEntry() {
        return new StringMapListEntry();
    }

    /**
     * Create an instance of {@link ViewInformationWS }
     * 
     */
    public ViewInformationWS createViewInformationWS() {
        return new ViewInformationWS();
    }

    /**
     * Create an instance of {@link SourceListResponse }
     * 
     */
    public SourceListResponse createSourceListResponse() {
        return new SourceListResponse();
    }

    /**
     * Create an instance of {@link StoredProcedureResponse }
     * 
     */
    public StoredProcedureResponse createStoredProcedureResponse() {
        return new StoredProcedureResponse();
    }

    /**
     * Create an instance of {@link Resource }
     * 
     */
    public Resource createResource() {
        return new Resource();
    }

    /**
     * Create an instance of {@link ParameterInfo }
     * 
     */
    public ParameterInfo createParameterInfo() {
        return new ParameterInfo();
    }

    /**
     * Create an instance of {@link ResourceInformation }
     * 
     */
    public ResourceInformation createResourceInformation() {
        return new ResourceInformation();
    }

    /**
     * Create an instance of {@link OperatorBuilderInformation }
     * 
     */
    public OperatorBuilderInformation createOperatorBuilderInformation() {
        return new OperatorBuilderInformation();
    }

    /**
     * Create an instance of {@link StringMapStringListResponse }
     * 
     */
    public StringMapStringListResponse createStringMapStringListResponse() {
        return new StringMapStringListResponse();
    }

    /**
     * Create an instance of {@link SdfDatatypeListResponse }
     * 
     */
    public SdfDatatypeListResponse createSdfDatatypeListResponse() {
        return new SdfDatatypeListResponse();
    }

    /**
     * Create an instance of {@link OperatorBuilderListResponse }
     * 
     */
    public OperatorBuilderListResponse createOperatorBuilderListResponse() {
        return new OperatorBuilderListResponse();
    }

    /**
     * Create an instance of {@link StoredProcedureListResponse }
     * 
     */
    public StoredProcedureListResponse createStoredProcedureListResponse() {
        return new StoredProcedureListResponse();
    }

    /**
     * Create an instance of {@link StringResponse }
     * 
     */
    public StringResponse createStringResponse() {
        return new StringResponse();
    }

    /**
     * Create an instance of {@link SdfSchemaResponse }
     * 
     */
    public SdfSchemaResponse createSdfSchemaResponse() {
        return new SdfSchemaResponse();
    }

    /**
     * Create an instance of {@link IntegerCollectionResponse }
     * 
     */
    public IntegerCollectionResponse createIntegerCollectionResponse() {
        return new IntegerCollectionResponse();
    }

    /**
     * Create an instance of {@link Pair }
     * 
     */
    public Pair createPair() {
        return new Pair();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link QueryResponse }
     * 
     */
    public QueryResponse createQueryResponse() {
        return new QueryResponse();
    }

    /**
     * Create an instance of {@link LogicalQueryInfo.UserParameters.Entry }
     * 
     */
    public LogicalQueryInfo.UserParameters.Entry createLogicalQueryInfoUserParametersEntry() {
        return new LogicalQueryInfo.UserParameters.Entry();
    }

    /**
     * Create an instance of {@link GraphNode.ParameterInfos.Entry }
     * 
     */
    public GraphNode.ParameterInfos.Entry createGraphNodeParameterInfosEntry() {
        return new GraphNode.ParameterInfos.Entry();
    }

    /**
     * Create an instance of {@link GraphNode.ChildsMap.Entry }
     * 
     */
    public GraphNode.ChildsMap.Entry createGraphNodeChildsMapEntry() {
        return new GraphNode.ChildsMap.Entry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getName")
    public JAXBElement<GetName> createGetName(GetName value) {
        return new JAXBElement<GetName>(_GetName_QNAME, GetName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStreamsAndViews }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getStreamsAndViews")
    public JAXBElement<GetStreamsAndViews> createGetStreamsAndViews(GetStreamsAndViews value) {
        return new JAXBElement<GetStreamsAndViews>(_GetStreamsAndViews_QNAME, GetStreamsAndViews.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "stopQuery")
    public JAXBElement<StopQuery> createStopQuery(StopQuery value) {
        return new JAXBElement<StopQuery>(_StopQuery_QNAME, StopQuery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInfosResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getInfosResponse")
    public JAXBElement<GetInfosResponse> createGetInfosResponse(GetInfosResponse value) {
        return new JAXBElement<GetInfosResponse>(_GetInfosResponse_QNAME, GetInfosResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveViewOrStreamByResource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeViewOrStreamByResource")
    public JAXBElement<RemoveViewOrStreamByResource> createRemoveViewOrStreamByResource(RemoveViewOrStreamByResource value) {
        return new JAXBElement<RemoveViewOrStreamByResource>(_RemoveViewOrStreamByResource_QNAME, RemoveViewOrStreamByResource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTransportValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getTransportValues")
    public JAXBElement<GetTransportValues> createGetTransportValues(GetTransportValues value) {
        return new JAXBElement<GetTransportValues>(_GetTransportValues_QNAME, GetTransportValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOperatorNamesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOperatorNamesResponse")
    public JAXBElement<GetOperatorNamesResponse> createGetOperatorNamesResponse(GetOperatorNamesResponse value) {
        return new JAXBElement<GetOperatorNamesResponse>(_GetOperatorNamesResponse_QNAME, GetOperatorNamesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInstalledSourcesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getInstalledSourcesResponse")
    public JAXBElement<GetInstalledSourcesResponse> createGetInstalledSourcesResponse(GetInstalledSourcesResponse value) {
        return new JAXBElement<GetInstalledSourcesResponse>(_GetInstalledSourcesResponse_QNAME, GetInstalledSourcesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveSinkByNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeSinkByNameResponse")
    public JAXBElement<RemoveSinkByNameResponse> createRemoveSinkByNameResponse(RemoveSinkByNameResponse value) {
        return new JAXBElement<RemoveSinkByNameResponse>(_RemoveSinkByNameResponse_QNAME, RemoveSinkByNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSupportedQueryParsersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getSupportedQueryParsersResponse")
    public JAXBElement<GetSupportedQueryParsersResponse> createGetSupportedQueryParsersResponse(GetSupportedQueryParsersResponse value) {
        return new JAXBElement<GetSupportedQueryParsersResponse>(_GetSupportedQueryParsersResponse_QNAME, GetSupportedQueryParsersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrentSchedulerID }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getCurrentSchedulerID")
    public JAXBElement<GetCurrentSchedulerID> createGetCurrentSchedulerID(GetCurrentSchedulerID value) {
        return new JAXBElement<GetCurrentSchedulerID>(_GetCurrentSchedulerID_QNAME, GetCurrentSchedulerID.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSupportedQueryParsers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getSupportedQueryParsers")
    public JAXBElement<GetSupportedQueryParsers> createGetSupportedQueryParsers(GetSupportedQueryParsers value) {
        return new JAXBElement<GetSupportedQueryParsers>(_GetSupportedQueryParsers_QNAME, GetSupportedQueryParsers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalQueryByName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getLogicalQueryByName")
    public JAXBElement<GetLogicalQueryByName> createGetLogicalQueryByName(GetLogicalQueryByName value) {
        return new JAXBElement<GetLogicalQueryByName>(_GetLogicalQueryByName_QNAME, GetLogicalQueryByName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOperatorInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOperatorInformation")
    public JAXBElement<GetOperatorInformation> createGetOperatorInformation(GetOperatorInformation value) {
        return new JAXBElement<GetOperatorInformation>(_GetOperatorInformation_QNAME, GetOperatorInformation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPlanByQueryID }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getPlanByQueryID")
    public JAXBElement<GetPlanByQueryID> createGetPlanByQueryID(GetPlanByQueryID value) {
        return new JAXBElement<GetPlanByQueryID>(_GetPlanByQueryID_QNAME, GetPlanByQueryID.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "startQueryResponse")
    public JAXBElement<StartQueryResponse> createStartQueryResponse(StartQueryResponse value) {
        return new JAXBElement<StartQueryResponse>(_StartQueryResponse_QNAME, StartQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStoredProcedures }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getStoredProcedures")
    public JAXBElement<GetStoredProcedures> createGetStoredProcedures(GetStoredProcedures value) {
        return new JAXBElement<GetStoredProcedures>(_GetStoredProcedures_QNAME, GetStoredProcedures.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "loginResponse")
    public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) {
        return new JAXBElement<LoginResponse>(_LoginResponse_QNAME, LoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContainsStoredProcedures }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "containsStoredProcedures")
    public JAXBElement<ContainsStoredProcedures> createContainsStoredProcedures(ContainsStoredProcedures value) {
        return new JAXBElement<ContainsStoredProcedures>(_ContainsStoredProcedures_QNAME, ContainsStoredProcedures.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getNameResponse")
    public JAXBElement<GetNameResponse> createGetNameResponse(GetNameResponse value) {
        return new JAXBElement<GetNameResponse>(_GetNameResponse_QNAME, GetNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimpleGraph }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "simpleGraph")
    public JAXBElement<SimpleGraph> createSimpleGraph(SimpleGraph value) {
        return new JAXBElement<SimpleGraph>(_SimpleGraph_QNAME, SimpleGraph.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionInformationWithMetadataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getConnectionInformationWithMetadataResponse")
    public JAXBElement<GetConnectionInformationWithMetadataResponse> createGetConnectionInformationWithMetadataResponse(GetConnectionInformationWithMetadataResponse value) {
        return new JAXBElement<GetConnectionInformationWithMetadataResponse>(_GetConnectionInformationWithMetadataResponse_QNAME, GetConnectionInformationWithMetadataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalQueryById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getLogicalQueryById")
    public JAXBElement<GetLogicalQueryById> createGetLogicalQueryById(GetLogicalQueryById value) {
        return new JAXBElement<GetLogicalQueryById>(_GetLogicalQueryById_QNAME, GetLogicalQueryById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopExecutionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "stopExecutionResponse")
    public JAXBElement<StopExecutionResponse> createStopExecutionResponse(StopExecutionResponse value) {
        return new JAXBElement<StopExecutionResponse>(_StopExecutionResponse_QNAME, StopExecutionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSinks }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getSinks")
    public JAXBElement<GetSinks> createGetSinks(GetSinks value) {
        return new JAXBElement<GetSinks>(_GetSinks_QNAME, GetSinks.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredAggregateFunctions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredAggregateFunctions")
    public JAXBElement<GetRegisteredAggregateFunctions> createGetRegisteredAggregateFunctions(GetRegisteredAggregateFunctions value) {
        return new JAXBElement<GetRegisteredAggregateFunctions>(_GetRegisteredAggregateFunctions_QNAME, GetRegisteredAggregateFunctions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInstalledQueriesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getInstalledQueriesResponse")
    public JAXBElement<GetInstalledQueriesResponse> createGetInstalledQueriesResponse(GetInstalledQueriesResponse value) {
        return new JAXBElement<GetInstalledQueriesResponse>(_GetInstalledQueriesResponse_QNAME, GetInstalledQueriesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddStoredProcedure }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "addStoredProcedure")
    public JAXBElement<AddStoredProcedure> createAddStoredProcedure(AddStoredProcedure value) {
        return new JAXBElement<AddStoredProcedure>(_AddStoredProcedure_QNAME, AddStoredProcedure.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContainsViewOrStreamByResource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "containsViewOrStreamByResource")
    public JAXBElement<ContainsViewOrStreamByResource> createContainsViewOrStreamByResource(ContainsViewOrStreamByResource value) {
        return new JAXBElement<ContainsViewOrStreamByResource>(_ContainsViewOrStreamByResource_QNAME, ContainsViewOrStreamByResource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMetadataNames }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getMetadataNames")
    public JAXBElement<GetMetadataNames> createGetMetadataNames(GetMetadataNames value) {
        return new JAXBElement<GetMetadataNames>(_GetMetadataNames_QNAME, GetMetadataNames.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOutputSchemaBySource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOutputSchemaBySource")
    public JAXBElement<GetOutputSchemaBySource> createGetOutputSchemaBySource(GetOutputSchemaBySource value) {
        return new JAXBElement<GetOutputSchemaBySource>(_GetOutputSchemaBySource_QNAME, GetOutputSchemaBySource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidUserDataException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "InvalidUserDataException")
    public JAXBElement<InvalidUserDataException> createInvalidUserDataException(InvalidUserDataException value) {
        return new JAXBElement<InvalidUserDataException>(_InvalidUserDataException_QNAME, InvalidUserDataException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSinksResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getSinksResponse")
    public JAXBElement<GetSinksResponse> createGetSinksResponse(GetSinksResponse value) {
        return new JAXBElement<GetSinksResponse>(_GetSinksResponse_QNAME, GetSinksResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveSinkByName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeSinkByName")
    public JAXBElement<RemoveSinkByName> createRemoveSinkByName(RemoveSinkByName value) {
        return new JAXBElement<RemoveSinkByName>(_RemoveSinkByName_QNAME, RemoveSinkByName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredAggregateFunctionsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredAggregateFunctionsResponse")
    public JAXBElement<GetRegisteredAggregateFunctionsResponse> createGetRegisteredAggregateFunctionsResponse(GetRegisteredAggregateFunctionsResponse value) {
        return new JAXBElement<GetRegisteredAggregateFunctionsResponse>(_GetRegisteredAggregateFunctionsResponse_QNAME, GetRegisteredAggregateFunctionsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuspendQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "suspendQuery")
    public JAXBElement<SuspendQuery> createSuspendQuery(SuspendQuery value) {
        return new JAXBElement<SuspendQuery>(_SuspendQuery_QNAME, SuspendQuery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryParserTokensResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getQueryParserTokensResponse")
    public JAXBElement<GetQueryParserTokensResponse> createGetQueryParserTokensResponse(GetQueryParserTokensResponse value) {
        return new JAXBElement<GetQueryParserTokensResponse>(_GetQueryParserTokensResponse_QNAME, GetQueryParserTokensResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogoutResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "logoutResponse")
    public JAXBElement<LogoutResponse> createLogoutResponse(LogoutResponse value) {
        return new JAXBElement<LogoutResponse>(_LogoutResponse_QNAME, LogoutResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddQuery2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "addQuery2")
    public JAXBElement<AddQuery2> createAddQuery2(AddQuery2 value) {
        return new JAXBElement<AddQuery2>(_AddQuery2_QNAME, AddQuery2 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContainsViewOrStreamByResourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "containsViewOrStreamByResourceResponse")
    public JAXBElement<ContainsViewOrStreamByResourceResponse> createContainsViewOrStreamByResourceResponse(ContainsViewOrStreamByResourceResponse value) {
        return new JAXBElement<ContainsViewOrStreamByResourceResponse>(_ContainsViewOrStreamByResourceResponse_QNAME, ContainsViewOrStreamByResourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DetermineOutputSchemaException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "DetermineOutputSchemaException")
    public JAXBElement<DetermineOutputSchemaException> createDetermineOutputSchemaException(DetermineOutputSchemaException value) {
        return new JAXBElement<DetermineOutputSchemaException>(_DetermineOutputSchemaException_QNAME, DetermineOutputSchemaException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSources }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getSources")
    public JAXBElement<GetSources> createGetSources(GetSources value) {
        return new JAXBElement<GetSources>(_GetSources_QNAME, GetSources.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalQueryByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getLogicalQueryByIdResponse")
    public JAXBElement<GetLogicalQueryByIdResponse> createGetLogicalQueryByIdResponse(GetLogicalQueryByIdResponse value) {
        return new JAXBElement<GetLogicalQueryByIdResponse>(_GetLogicalQueryByIdResponse_QNAME, GetLogicalQueryByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOutputSchemaByQueryIdAndPort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOutputSchemaByQueryIdAndPort")
    public JAXBElement<GetOutputSchemaByQueryIdAndPort> createGetOutputSchemaByQueryIdAndPort(GetOutputSchemaByQueryIdAndPort value) {
        return new JAXBElement<GetOutputSchemaByQueryIdAndPort>(_GetOutputSchemaByQueryIdAndPort_QNAME, GetOutputSchemaByQueryIdAndPort.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionInformationWithSSL }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getConnectionInformationWithSSL")
    public JAXBElement<GetConnectionInformationWithSSL> createGetConnectionInformationWithSSL(GetConnectionInformationWithSSL value) {
        return new JAXBElement<GetConnectionInformationWithSSL>(_GetConnectionInformationWithSSL_QNAME, GetConnectionInformationWithSSL.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredBufferPlacementStrategiesIDs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredBufferPlacementStrategiesIDs")
    public JAXBElement<GetRegisteredBufferPlacementStrategiesIDs> createGetRegisteredBufferPlacementStrategiesIDs(GetRegisteredBufferPlacementStrategiesIDs value) {
        return new JAXBElement<GetRegisteredBufferPlacementStrategiesIDs>(_GetRegisteredBufferPlacementStrategiesIDs_QNAME, GetRegisteredBufferPlacementStrategiesIDs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredWrapperNamesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredWrapperNamesResponse")
    public JAXBElement<GetRegisteredWrapperNamesResponse> createGetRegisteredWrapperNamesResponse(GetRegisteredWrapperNamesResponse value) {
        return new JAXBElement<GetRegisteredWrapperNamesResponse>(_GetRegisteredWrapperNamesResponse_QNAME, GetRegisteredWrapperNamesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReloadStoredQueriesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "reloadStoredQueriesResponse")
    public JAXBElement<ReloadStoredQueriesResponse> createReloadStoredQueriesResponse(ReloadStoredQueriesResponse value) {
        return new JAXBElement<ReloadStoredQueriesResponse>(_ReloadStoredQueriesResponse_QNAME, ReloadStoredQueriesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStoredProcedureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getStoredProcedureResponse")
    public JAXBElement<GetStoredProcedureResponse> createGetStoredProcedureResponse(GetStoredProcedureResponse value) {
        return new JAXBElement<GetStoredProcedureResponse>(_GetStoredProcedureResponse_QNAME, GetStoredProcedureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryParserSuggestionsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getQueryParserSuggestionsResponse")
    public JAXBElement<GetQueryParserSuggestionsResponse> createGetQueryParserSuggestionsResponse(GetQueryParserSuggestionsResponse value) {
        return new JAXBElement<GetQueryParserSuggestionsResponse>(_GetQueryParserSuggestionsResponse_QNAME, GetQueryParserSuggestionsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStoredProceduresResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getStoredProceduresResponse")
    public JAXBElement<GetStoredProceduresResponse> createGetStoredProceduresResponse(GetStoredProceduresResponse value) {
        return new JAXBElement<GetStoredProceduresResponse>(_GetStoredProceduresResponse_QNAME, GetStoredProceduresResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsRunning }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "isRunning")
    public JAXBElement<IsRunning> createIsRunning(IsRunning value) {
        return new JAXBElement<IsRunning>(_IsRunning_QNAME, IsRunning.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunCommand }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "runCommand")
    public JAXBElement<RunCommand> createRunCommand(RunCommand value) {
        return new JAXBElement<RunCommand>(_RunCommand_QNAME, RunCommand.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DetermineOutputSchemaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "determineOutputSchemaResponse")
    public JAXBElement<DetermineOutputSchemaResponse> createDetermineOutputSchemaResponse(DetermineOutputSchemaResponse value) {
        return new JAXBElement<DetermineOutputSchemaResponse>(_DetermineOutputSchemaResponse_QNAME, DetermineOutputSchemaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DetermineOutputSchema }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "determineOutputSchema")
    public JAXBElement<DetermineOutputSchema> createDetermineOutputSchema(DetermineOutputSchema value) {
        return new JAXBElement<DetermineOutputSchema>(_DetermineOutputSchema_QNAME, DetermineOutputSchema.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetSchedulerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "setSchedulerResponse")
    public JAXBElement<SetSchedulerResponse> createSetSchedulerResponse(SetSchedulerResponse value) {
        return new JAXBElement<SetSchedulerResponse>(_SetSchedulerResponse_QNAME, SetSchedulerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredBufferPlacementStrategiesIDsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredBufferPlacementStrategiesIDsResponse")
    public JAXBElement<GetRegisteredBufferPlacementStrategiesIDsResponse> createGetRegisteredBufferPlacementStrategiesIDsResponse(GetRegisteredBufferPlacementStrategiesIDsResponse value) {
        return new JAXBElement<GetRegisteredBufferPlacementStrategiesIDsResponse>(_GetRegisteredBufferPlacementStrategiesIDsResponse_QNAME, GetRegisteredBufferPlacementStrategiesIDsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContainsViewOrStreamByNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "containsViewOrStreamByNameResponse")
    public JAXBElement<ContainsViewOrStreamByNameResponse> createContainsViewOrStreamByNameResponse(ContainsViewOrStreamByNameResponse value) {
        return new JAXBElement<ContainsViewOrStreamByNameResponse>(_ContainsViewOrStreamByNameResponse_QNAME, ContainsViewOrStreamByNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateQueryException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "CreateQueryException")
    public JAXBElement<CreateQueryException> createCreateQueryException(CreateQueryException value) {
        return new JAXBElement<CreateQueryException>(_CreateQueryException_QNAME, CreateQueryException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalQueryIds }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getLogicalQueryIds")
    public JAXBElement<GetLogicalQueryIds> createGetLogicalQueryIds(GetLogicalQueryIds value) {
        return new JAXBElement<GetLogicalQueryIds>(_GetLogicalQueryIds_QNAME, GetLogicalQueryIds.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOperatorBuilderList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOperatorBuilderList")
    public JAXBElement<GetOperatorBuilderList> createGetOperatorBuilderList(GetOperatorBuilderList value) {
        return new JAXBElement<GetOperatorBuilderList>(_GetOperatorBuilderList_QNAME, GetOperatorBuilderList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartAllClosedQueries }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "startAllClosedQueries")
    public JAXBElement<StartAllClosedQueries> createStartAllClosedQueries(StartAllClosedQueries value) {
        return new JAXBElement<StartAllClosedQueries>(_StartAllClosedQueries_QNAME, StartAllClosedQueries.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalQueryPlan }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getLogicalQueryPlan")
    public JAXBElement<GetLogicalQueryPlan> createGetLogicalQueryPlan(GetLogicalQueryPlan value) {
        return new JAXBElement<GetLogicalQueryPlan>(_GetLogicalQueryPlan_QNAME, GetLogicalQueryPlan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOperatorNames }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOperatorNames")
    public JAXBElement<GetOperatorNames> createGetOperatorNames(GetOperatorNames value) {
        return new JAXBElement<GetOperatorNames>(_GetOperatorNames_QNAME, GetOperatorNames.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPlanResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getPlanResponse")
    public JAXBElement<GetPlanResponse> createGetPlanResponse(GetPlanResponse value) {
        return new JAXBElement<GetPlanResponse>(_GetPlanResponse_QNAME, GetPlanResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredDatatypes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredDatatypes")
    public JAXBElement<GetRegisteredDatatypes> createGetRegisteredDatatypes(GetRegisteredDatatypes value) {
        return new JAXBElement<GetRegisteredDatatypes>(_GetRegisteredDatatypes_QNAME, GetRegisteredDatatypes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsValidSession }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "isValidSession")
    public JAXBElement<IsValidSession> createIsValidSession(IsValidSession value) {
        return new JAXBElement<IsValidSession>(_IsValidSession_QNAME, IsValidSession.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "startQuery")
    public JAXBElement<StartQuery> createStartQuery(StartQuery value) {
        return new JAXBElement<StartQuery>(_StartQuery_QNAME, StartQuery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInstalledQueries }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getInstalledQueries")
    public JAXBElement<GetInstalledQueries> createGetInstalledQueries(GetInstalledQueries value) {
        return new JAXBElement<GetInstalledQueries>(_GetInstalledQueries_QNAME, GetInstalledQueries.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContainsStoredProceduresResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "containsStoredProceduresResponse")
    public JAXBElement<ContainsStoredProceduresResponse> createContainsStoredProceduresResponse(ContainsStoredProceduresResponse value) {
        return new JAXBElement<ContainsStoredProceduresResponse>(_ContainsStoredProceduresResponse_QNAME, ContainsStoredProceduresResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryBuildConfigurationNames }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getQueryBuildConfigurationNames")
    public JAXBElement<GetQueryBuildConfigurationNames> createGetQueryBuildConfigurationNames(GetQueryBuildConfigurationNames value) {
        return new JAXBElement<GetQueryBuildConfigurationNames>(_GetQueryBuildConfigurationNames_QNAME, GetQueryBuildConfigurationNames.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryStateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getQueryStateResponse")
    public JAXBElement<GetQueryStateResponse> createGetQueryStateResponse(GetQueryStateResponse value) {
        return new JAXBElement<GetQueryStateResponse>(_GetQueryStateResponse_QNAME, GetQueryStateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getLogicalQueryResponse")
    public JAXBElement<GetLogicalQueryResponse> createGetLogicalQueryResponse(GetLogicalQueryResponse value) {
        return new JAXBElement<GetLogicalQueryResponse>(_GetLogicalQueryResponse_QNAME, GetLogicalQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOutputSchemaByQueryIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOutputSchemaByQueryIdResponse")
    public JAXBElement<GetOutputSchemaByQueryIdResponse> createGetOutputSchemaByQueryIdResponse(GetOutputSchemaByQueryIdResponse value) {
        return new JAXBElement<GetOutputSchemaByQueryIdResponse>(_GetOutputSchemaByQueryIdResponse_QNAME, GetOutputSchemaByQueryIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveViewOrStreamByResourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeViewOrStreamByResourceResponse")
    public JAXBElement<RemoveViewOrStreamByResourceResponse> createRemoveViewOrStreamByResourceResponse(RemoveViewOrStreamByResourceResponse value) {
        return new JAXBElement<RemoveViewOrStreamByResourceResponse>(_RemoveViewOrStreamByResourceResponse_QNAME, RemoveViewOrStreamByResourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartAllClosedQueriesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "startAllClosedQueriesResponse")
    public JAXBElement<StartAllClosedQueriesResponse> createStartAllClosedQueriesResponse(StartAllClosedQueriesResponse value) {
        return new JAXBElement<StartAllClosedQueriesResponse>(_StartAllClosedQueriesResponse_QNAME, StartAllClosedQueriesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryBuildConfigurationNamesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getQueryBuildConfigurationNamesResponse")
    public JAXBElement<GetQueryBuildConfigurationNamesResponse> createGetQueryBuildConfigurationNamesResponse(GetQueryBuildConfigurationNamesResponse value) {
        return new JAXBElement<GetQueryBuildConfigurationNamesResponse>(_GetQueryBuildConfigurationNamesResponse_QNAME, GetQueryBuildConfigurationNamesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTransportValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getTransportValuesResponse")
    public JAXBElement<GetTransportValuesResponse> createGetTransportValuesResponse(GetTransportValuesResponse value) {
        return new JAXBElement<GetTransportValuesResponse>(_GetTransportValuesResponse_QNAME, GetTransportValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReloadStoredQueries }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "reloadStoredQueries")
    public JAXBElement<ReloadStoredQueries> createReloadStoredQueries(ReloadStoredQueries value) {
        return new JAXBElement<ReloadStoredQueries>(_ReloadStoredQueries_QNAME, ReloadStoredQueries.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOperatorBuilderListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOperatorBuilderListResponse")
    public JAXBElement<GetOperatorBuilderListResponse> createGetOperatorBuilderListResponse(GetOperatorBuilderListResponse value) {
        return new JAXBElement<GetOperatorBuilderListResponse>(_GetOperatorBuilderListResponse_QNAME, GetOperatorBuilderListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getQueryState")
    public JAXBElement<GetQueryState> createGetQueryState(GetQueryState value) {
        return new JAXBElement<GetQueryState>(_GetQueryState_QNAME, GetQueryState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveStoredProcedure }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeStoredProcedure")
    public JAXBElement<RemoveStoredProcedure> createRemoveStoredProcedure(RemoveStoredProcedure value) {
        return new JAXBElement<RemoveStoredProcedure>(_RemoveStoredProcedure_QNAME, RemoveStoredProcedure.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartExecution }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "startExecution")
    public JAXBElement<StartExecution> createStartExecution(StartExecution value) {
        return new JAXBElement<StartExecution>(_StartExecution_QNAME, StartExecution.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Logout }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "logout")
    public JAXBElement<Logout> createLogout(Logout value) {
        return new JAXBElement<Logout>(_Logout_QNAME, Logout.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryStates }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getQueryStates")
    public JAXBElement<GetQueryStates> createGetQueryStates(GetQueryStates value) {
        return new JAXBElement<GetQueryStates>(_GetQueryStates_QNAME, GetQueryStates.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartExecutionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "startExecutionResponse")
    public JAXBElement<StartExecutionResponse> createStartExecutionResponse(StartExecutionResponse value) {
        return new JAXBElement<StartExecutionResponse>(_StartExecutionResponse_QNAME, StartExecutionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOutputSchemaBySourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOutputSchemaBySourceResponse")
    public JAXBElement<GetOutputSchemaBySourceResponse> createGetOutputSchemaBySourceResponse(GetOutputSchemaBySourceResponse value) {
        return new JAXBElement<GetOutputSchemaBySourceResponse>(_GetOutputSchemaBySourceResponse_QNAME, GetOutputSchemaBySourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeQuery")
    public JAXBElement<RemoveQuery> createRemoveQuery(RemoveQuery value) {
        return new JAXBElement<RemoveQuery>(_RemoveQuery_QNAME, RemoveQuery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMetadataNamesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getMetadataNamesResponse")
    public JAXBElement<GetMetadataNamesResponse> createGetMetadataNamesResponse(GetMetadataNamesResponse value) {
        return new JAXBElement<GetMetadataNamesResponse>(_GetMetadataNamesResponse_QNAME, GetMetadataNamesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResumeQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "resumeQuery")
    public JAXBElement<ResumeQuery> createResumeQuery(ResumeQuery value) {
        return new JAXBElement<ResumeQuery>(_ResumeQuery_QNAME, ResumeQuery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddStoredProcedureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "addStoredProcedureResponse")
    public JAXBElement<AddStoredProcedureResponse> createAddStoredProcedureResponse(AddStoredProcedureResponse value) {
        return new JAXBElement<AddStoredProcedureResponse>(_AddStoredProcedureResponse_QNAME, AddStoredProcedureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPlan }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getPlan")
    public JAXBElement<GetPlan> createGetPlan(GetPlan value) {
        return new JAXBElement<GetPlan>(_GetPlan_QNAME, GetPlan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getConnectionInformation")
    public JAXBElement<GetConnectionInformation> createGetConnectionInformation(GetConnectionInformation value) {
        return new JAXBElement<GetConnectionInformation>(_GetConnectionInformation_QNAME, GetConnectionInformation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDataHandlerValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getDataHandlerValues")
    public JAXBElement<GetDataHandlerValues> createGetDataHandlerValues(GetDataHandlerValues value) {
        return new JAXBElement<GetDataHandlerValues>(_GetDataHandlerValues_QNAME, GetDataHandlerValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredSchedulingStrategiesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredSchedulingStrategiesResponse")
    public JAXBElement<GetRegisteredSchedulingStrategiesResponse> createGetRegisteredSchedulingStrategiesResponse(GetRegisteredSchedulingStrategiesResponse value) {
        return new JAXBElement<GetRegisteredSchedulingStrategiesResponse>(_GetRegisteredSchedulingStrategiesResponse_QNAME, GetRegisteredSchedulingStrategiesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrentSchedulerIDResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getCurrentSchedulerIDResponse")
    public JAXBElement<GetCurrentSchedulerIDResponse> createGetCurrentSchedulerIDResponse(GetCurrentSchedulerIDResponse value) {
        return new JAXBElement<GetCurrentSchedulerIDResponse>(_GetCurrentSchedulerIDResponse_QNAME, GetCurrentSchedulerIDResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeQueryResponse")
    public JAXBElement<RemoveQueryResponse> createRemoveQueryResponse(RemoveQueryResponse value) {
        return new JAXBElement<RemoveQueryResponse>(_RemoveQueryResponse_QNAME, RemoveQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveSinkByResource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeSinkByResource")
    public JAXBElement<RemoveSinkByResource> createRemoveSinkByResource(RemoveSinkByResource value) {
        return new JAXBElement<RemoveSinkByResource>(_RemoveSinkByResource_QNAME, RemoveSinkByResource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopExecution }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "stopExecution")
    public JAXBElement<StopExecution> createStopExecution(StopExecution value) {
        return new JAXBElement<StopExecution>(_StopExecution_QNAME, StopExecution.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionInformationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getConnectionInformationResponse")
    public JAXBElement<GetConnectionInformationResponse> createGetConnectionInformationResponse(GetConnectionInformationResponse value) {
        return new JAXBElement<GetConnectionInformationResponse>(_GetConnectionInformationResponse_QNAME, GetConnectionInformationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrentSchedulingStrategyIDResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getCurrentSchedulingStrategyIDResponse")
    public JAXBElement<GetCurrentSchedulingStrategyIDResponse> createGetCurrentSchedulingStrategyIDResponse(GetCurrentSchedulingStrategyIDResponse value) {
        return new JAXBElement<GetCurrentSchedulingStrategyIDResponse>(_GetCurrentSchedulingStrategyIDResponse_QNAME, GetCurrentSchedulingStrategyIDResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResumeQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "resumeQueryResponse")
    public JAXBElement<ResumeQueryResponse> createResumeQueryResponse(ResumeQueryResponse value) {
        return new JAXBElement<ResumeQueryResponse>(_ResumeQueryResponse_QNAME, ResumeQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProtocolValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getProtocolValues")
    public JAXBElement<GetProtocolValues> createGetProtocolValues(GetProtocolValues value) {
        return new JAXBElement<GetProtocolValues>(_GetProtocolValues_QNAME, GetProtocolValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetWindowTypesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getWindowTypesResponse")
    public JAXBElement<GetWindowTypesResponse> createGetWindowTypesResponse(GetWindowTypesResponse value) {
        return new JAXBElement<GetWindowTypesResponse>(_GetWindowTypesResponse_QNAME, GetWindowTypesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOperatorInformations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOperatorInformations")
    public JAXBElement<GetOperatorInformations> createGetOperatorInformations(GetOperatorInformations value) {
        return new JAXBElement<GetOperatorInformations>(_GetOperatorInformations_QNAME, GetOperatorInformations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuspendQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "suspendQueryResponse")
    public JAXBElement<SuspendQueryResponse> createSuspendQueryResponse(SuspendQueryResponse value) {
        return new JAXBElement<SuspendQueryResponse>(_SuspendQueryResponse_QNAME, SuspendQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveSinkByResourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeSinkByResourceResponse")
    public JAXBElement<RemoveSinkByResourceResponse> createRemoveSinkByResourceResponse(RemoveSinkByResourceResponse value) {
        return new JAXBElement<RemoveSinkByResourceResponse>(_RemoveSinkByResourceResponse_QNAME, RemoveSinkByResourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryNotExistsException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "QueryNotExistsException")
    public JAXBElement<QueryNotExistsException> createQueryNotExistsException(QueryNotExistsException value) {
        return new JAXBElement<QueryNotExistsException>(_QueryNotExistsException_QNAME, QueryNotExistsException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInstalledSources }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getInstalledSources")
    public JAXBElement<GetInstalledSources> createGetInstalledSources(GetInstalledSources value) {
        return new JAXBElement<GetInstalledSources>(_GetInstalledSources_QNAME, GetInstalledSources.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSourcesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getSourcesResponse")
    public JAXBElement<GetSourcesResponse> createGetSourcesResponse(GetSourcesResponse value) {
        return new JAXBElement<GetSourcesResponse>(_GetSourcesResponse_QNAME, GetSourcesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalQueryPlanResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getLogicalQueryPlanResponse")
    public JAXBElement<GetLogicalQueryPlanResponse> createGetLogicalQueryPlanResponse(GetLogicalQueryPlanResponse value) {
        return new JAXBElement<GetLogicalQueryPlanResponse>(_GetLogicalQueryPlanResponse_QNAME, GetLogicalQueryPlanResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPlanByQueryIDResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getPlanByQueryIDResponse")
    public JAXBElement<GetPlanByQueryIDResponse> createGetPlanByQueryIDResponse(GetPlanByQueryIDResponse value) {
        return new JAXBElement<GetPlanByQueryIDResponse>(_GetPlanByQueryIDResponse_QNAME, GetPlanByQueryIDResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryParserSuggestions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getQueryParserSuggestions")
    public JAXBElement<GetQueryParserSuggestions> createGetQueryParserSuggestions(GetQueryParserSuggestions value) {
        return new JAXBElement<GetQueryParserSuggestions>(_GetQueryParserSuggestions_QNAME, GetQueryParserSuggestions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredSchedulersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredSchedulersResponse")
    public JAXBElement<GetRegisteredSchedulersResponse> createGetRegisteredSchedulersResponse(GetRegisteredSchedulersResponse value) {
        return new JAXBElement<GetRegisteredSchedulersResponse>(_GetRegisteredSchedulersResponse_QNAME, GetRegisteredSchedulersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrentSchedulingStrategyID }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getCurrentSchedulingStrategyID")
    public JAXBElement<GetCurrentSchedulingStrategyID> createGetCurrentSchedulingStrategyID(GetCurrentSchedulingStrategyID value) {
        return new JAXBElement<GetCurrentSchedulingStrategyID>(_GetCurrentSchedulingStrategyID_QNAME, GetCurrentSchedulingStrategyID.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalQueryIdsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getLogicalQueryIdsResponse")
    public JAXBElement<GetLogicalQueryIdsResponse> createGetLogicalQueryIdsResponse(GetLogicalQueryIdsResponse value) {
        return new JAXBElement<GetLogicalQueryIdsResponse>(_GetLogicalQueryIdsResponse_QNAME, GetLogicalQueryIdsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredSchedulers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredSchedulers")
    public JAXBElement<GetRegisteredSchedulers> createGetRegisteredSchedulers(GetRegisteredSchedulers value) {
        return new JAXBElement<GetRegisteredSchedulers>(_GetRegisteredSchedulers_QNAME, GetRegisteredSchedulers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsValidSessionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "isValidSessionResponse")
    public JAXBElement<IsValidSessionResponse> createIsValidSessionResponse(IsValidSessionResponse value) {
        return new JAXBElement<IsValidSessionResponse>(_IsValidSessionResponse_QNAME, IsValidSessionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalQuery }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getLogicalQuery")
    public JAXBElement<GetLogicalQuery> createGetLogicalQuery(GetLogicalQuery value) {
        return new JAXBElement<GetLogicalQuery>(_GetLogicalQuery_QNAME, GetLogicalQuery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStreamsAndViewsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getStreamsAndViewsResponse")
    public JAXBElement<GetStreamsAndViewsResponse> createGetStreamsAndViewsResponse(GetStreamsAndViewsResponse value) {
        return new JAXBElement<GetStreamsAndViewsResponse>(_GetStreamsAndViewsResponse_QNAME, GetStreamsAndViewsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveViewOrStreamByName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeViewOrStreamByName")
    public JAXBElement<RemoveViewOrStreamByName> createRemoveViewOrStreamByName(RemoveViewOrStreamByName value) {
        return new JAXBElement<RemoveViewOrStreamByName>(_RemoveViewOrStreamByName_QNAME, RemoveViewOrStreamByName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProtocolValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getProtocolValuesResponse")
    public JAXBElement<GetProtocolValuesResponse> createGetProtocolValuesResponse(GetProtocolValuesResponse value) {
        return new JAXBElement<GetProtocolValuesResponse>(_GetProtocolValuesResponse_QNAME, GetProtocolValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStoredProcedure }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getStoredProcedure")
    public JAXBElement<GetStoredProcedure> createGetStoredProcedure(GetStoredProcedure value) {
        return new JAXBElement<GetStoredProcedure>(_GetStoredProcedure_QNAME, GetStoredProcedure.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogicalQueryByNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getLogicalQueryByNameResponse")
    public JAXBElement<GetLogicalQueryByNameResponse> createGetLogicalQueryByNameResponse(GetLogicalQueryByNameResponse value) {
        return new JAXBElement<GetLogicalQueryByNameResponse>(_GetLogicalQueryByNameResponse_QNAME, GetLogicalQueryByNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOperatorInformationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOperatorInformationsResponse")
    public JAXBElement<GetOperatorInformationsResponse> createGetOperatorInformationsResponse(GetOperatorInformationsResponse value) {
        return new JAXBElement<GetOperatorInformationsResponse>(_GetOperatorInformationsResponse_QNAME, GetOperatorInformationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "login")
    public JAXBElement<Login> createLogin(Login value) {
        return new JAXBElement<Login>(_Login_QNAME, Login.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDataHandlerValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getDataHandlerValuesResponse")
    public JAXBElement<GetDataHandlerValuesResponse> createGetDataHandlerValuesResponse(GetDataHandlerValuesResponse value) {
        return new JAXBElement<GetDataHandlerValuesResponse>(_GetDataHandlerValuesResponse_QNAME, GetDataHandlerValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredSchedulingStrategies }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredSchedulingStrategies")
    public JAXBElement<GetRegisteredSchedulingStrategies> createGetRegisteredSchedulingStrategies(GetRegisteredSchedulingStrategies value) {
        return new JAXBElement<GetRegisteredSchedulingStrategies>(_GetRegisteredSchedulingStrategies_QNAME, GetRegisteredSchedulingStrategies.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInfos }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getInfos")
    public JAXBElement<GetInfos> createGetInfos(GetInfos value) {
        return new JAXBElement<GetInfos>(_GetInfos_QNAME, GetInfos.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopQueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "stopQueryResponse")
    public JAXBElement<StopQueryResponse> createStopQueryResponse(StopQueryResponse value) {
        return new JAXBElement<StopQueryResponse>(_StopQueryResponse_QNAME, StopQueryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryStatesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getQueryStatesResponse")
    public JAXBElement<GetQueryStatesResponse> createGetQueryStatesResponse(GetQueryStatesResponse value) {
        return new JAXBElement<GetQueryStatesResponse>(_GetQueryStatesResponse_QNAME, GetQueryStatesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetScheduler }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "setScheduler")
    public JAXBElement<SetScheduler> createSetScheduler(SetScheduler value) {
        return new JAXBElement<SetScheduler>(_SetScheduler_QNAME, SetScheduler.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredDatatypesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredDatatypesResponse")
    public JAXBElement<GetRegisteredDatatypesResponse> createGetRegisteredDatatypesResponse(GetRegisteredDatatypesResponse value) {
        return new JAXBElement<GetRegisteredDatatypesResponse>(_GetRegisteredDatatypesResponse_QNAME, GetRegisteredDatatypesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionInformationWithMetadata }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getConnectionInformationWithMetadata")
    public JAXBElement<GetConnectionInformationWithMetadata> createGetConnectionInformationWithMetadata(GetConnectionInformationWithMetadata value) {
        return new JAXBElement<GetConnectionInformationWithMetadata>(_GetConnectionInformationWithMetadata_QNAME, GetConnectionInformationWithMetadata.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddQuery2Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "addQuery2Response")
    public JAXBElement<AddQuery2Response> createAddQuery2Response(AddQuery2Response value) {
        return new JAXBElement<AddQuery2Response>(_AddQuery2Response_QNAME, AddQuery2Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionInformationWithPortsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getConnectionInformationWithPortsResponse")
    public JAXBElement<GetConnectionInformationWithPortsResponse> createGetConnectionInformationWithPortsResponse(GetConnectionInformationWithPortsResponse value) {
        return new JAXBElement<GetConnectionInformationWithPortsResponse>(_GetConnectionInformationWithPortsResponse_QNAME, GetConnectionInformationWithPortsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveViewOrStreamByNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeViewOrStreamByNameResponse")
    public JAXBElement<RemoveViewOrStreamByNameResponse> createRemoveViewOrStreamByNameResponse(RemoveViewOrStreamByNameResponse value) {
        return new JAXBElement<RemoveViewOrStreamByNameResponse>(_RemoveViewOrStreamByNameResponse_QNAME, RemoveViewOrStreamByNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionInformationWithPorts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getConnectionInformationWithPorts")
    public JAXBElement<GetConnectionInformationWithPorts> createGetConnectionInformationWithPorts(GetConnectionInformationWithPorts value) {
        return new JAXBElement<GetConnectionInformationWithPorts>(_GetConnectionInformationWithPorts_QNAME, GetConnectionInformationWithPorts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOutputSchemaByQueryId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOutputSchemaByQueryId")
    public JAXBElement<GetOutputSchemaByQueryId> createGetOutputSchemaByQueryId(GetOutputSchemaByQueryId value) {
        return new JAXBElement<GetOutputSchemaByQueryId>(_GetOutputSchemaByQueryId_QNAME, GetOutputSchemaByQueryId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveStoredProcedureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "removeStoredProcedureResponse")
    public JAXBElement<RemoveStoredProcedureResponse> createRemoveStoredProcedureResponse(RemoveStoredProcedureResponse value) {
        return new JAXBElement<RemoveStoredProcedureResponse>(_RemoveStoredProcedureResponse_QNAME, RemoveStoredProcedureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login2Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "login2Response")
    public JAXBElement<Login2Response> createLogin2Response(Login2Response value) {
        return new JAXBElement<Login2Response>(_Login2Response_QNAME, Login2Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunCommandResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "runCommandResponse")
    public JAXBElement<RunCommandResponse> createRunCommandResponse(RunCommandResponse value) {
        return new JAXBElement<RunCommandResponse>(_RunCommandResponse_QNAME, RunCommandResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetWindowTypes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getWindowTypes")
    public JAXBElement<GetWindowTypes> createGetWindowTypes(GetWindowTypes value) {
        return new JAXBElement<GetWindowTypes>(_GetWindowTypes_QNAME, GetWindowTypes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOutputSchemaByQueryIdAndPortResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOutputSchemaByQueryIdAndPortResponse")
    public JAXBElement<GetOutputSchemaByQueryIdAndPortResponse> createGetOutputSchemaByQueryIdAndPortResponse(GetOutputSchemaByQueryIdAndPortResponse value) {
        return new JAXBElement<GetOutputSchemaByQueryIdAndPortResponse>(_GetOutputSchemaByQueryIdAndPortResponse_QNAME, GetOutputSchemaByQueryIdAndPortResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetQueryParserTokens }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getQueryParserTokens")
    public JAXBElement<GetQueryParserTokens> createGetQueryParserTokens(GetQueryParserTokens value) {
        return new JAXBElement<GetQueryParserTokens>(_GetQueryParserTokens_QNAME, GetQueryParserTokens.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegisteredWrapperNames }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getRegisteredWrapperNames")
    public JAXBElement<GetRegisteredWrapperNames> createGetRegisteredWrapperNames(GetRegisteredWrapperNames value) {
        return new JAXBElement<GetRegisteredWrapperNames>(_GetRegisteredWrapperNames_QNAME, GetRegisteredWrapperNames.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsRunningResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "isRunningResponse")
    public JAXBElement<IsRunningResponse> createIsRunningResponse(IsRunningResponse value) {
        return new JAXBElement<IsRunningResponse>(_IsRunningResponse_QNAME, IsRunningResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GraphNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "graphNode")
    public JAXBElement<GraphNode> createGraphNode(GraphNode value) {
        return new JAXBElement<GraphNode>(_GraphNode_QNAME, GraphNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "login2")
    public JAXBElement<Login2> createLogin2(Login2 value) {
        return new JAXBElement<Login2>(_Login2_QNAME, Login2 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContainsViewOrStreamByName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "containsViewOrStreamByName")
    public JAXBElement<ContainsViewOrStreamByName> createContainsViewOrStreamByName(ContainsViewOrStreamByName value) {
        return new JAXBElement<ContainsViewOrStreamByName>(_ContainsViewOrStreamByName_QNAME, ContainsViewOrStreamByName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConnectionInformationWithSSLResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getConnectionInformationWithSSLResponse")
    public JAXBElement<GetConnectionInformationWithSSLResponse> createGetConnectionInformationWithSSLResponse(GetConnectionInformationWithSSLResponse value) {
        return new JAXBElement<GetConnectionInformationWithSSLResponse>(_GetConnectionInformationWithSSLResponse_QNAME, GetConnectionInformationWithSSLResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOperatorInformationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/", name = "getOperatorInformationResponse")
    public JAXBElement<GetOperatorInformationResponse> createGetOperatorInformationResponse(GetOperatorInformationResponse value) {
        return new JAXBElement<GetOperatorInformationResponse>(_GetOperatorInformationResponse_QNAME, GetOperatorInformationResponse.class, null, value);
    }

}
