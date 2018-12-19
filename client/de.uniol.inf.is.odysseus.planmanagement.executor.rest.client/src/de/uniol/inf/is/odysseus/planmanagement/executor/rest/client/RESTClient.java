package de.uniol.inf.is.odysseus.planmanagement.executor.rest.client;

import java.net.SocketAddress;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.SinkInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IClientExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

/**
 * Client that uses the REST interface to communicate with the Odysseus Server
 * 
 * @author Marco Grawunder
 *
 */

public class RESTClient implements IClientExecutor, IExecutor, IOperatorOwner {

	@Override
	public boolean connect(String connectString) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ISession login(String username, byte[] password, String tenantname, String connectString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SocketAddress> getSocketConnectionInformation(int queryId, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void removeQuery(int queryID, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeQuery(Resource queryName, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startQuery(int queryID, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startQuery(Resource queryName, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopQuery(int queryID, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopQuery(Resource queryName, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void suspendQuery(int queryID, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void suspendQuery(Resource queryName, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void resumeQuery(int queryID, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void resumeQuery(Resource queryName, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void partialQuery(int queryID, int sheddingFactor, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void partialQuery(Resource queryName, int sheddingFactor, ISession caller) throws PlanManagementException {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(IOperatorOwner arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ISession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void done(IOwnedOperator op) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<String> getQueryBuildConfigurationNames(ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getSupportedQueryParsers(ISession session) throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getMetadataNames(ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getQueryParserTokens(String queryParser, ISession user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getQueryParserSuggestions(String queryParser, String hint, ISession user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Integer> addQuery(String query, String parserID, ISession user, Context context)
			throws PlanManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runCommand(String commandExpression, ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public SDFSchema determineOutputSchema(String query, String parserID, ISession user, int port, Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILogicalQuery getLogicalQueryById(int id, ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILogicalQuery getLogicalQueryByName(Resource name, ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Integer> getLogicalQueryIds(ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryState getQueryState(int queryID, ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryState getQueryState(String queryName, ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QueryState> getQueryStates(List<Integer> id, List<ISession> session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPhysicalOperator> getPhysicalRoots(int queryID, ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Integer> startAllClosedQueries(ISession user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredBufferPlacementStrategiesIDs(ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredSchedulingStrategies(ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredSchedulers(ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setScheduler(String scheduler, String schedulerStrategy, ISession session) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCurrentSchedulerID(ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentSchedulingStrategyID(ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SDFDatatype> getRegisteredDatatypes(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredWrapperNames(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredAggregateFunctions(Class<? extends IStreamObject> datamodel, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredAggregateFunctions(String datamodel, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISession login(String username, byte[] password, String tenantname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISession login(String username, byte[] password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logout(ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid(ISession session) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ILogicalPlan removeSink(String name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILogicalPlan removeSink(Resource name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeViewOrStream(String name, ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeViewOrStream(Resource name, ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ViewInformation> getStreamsAndViewsInformation(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SinkInformation> getSinks(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsViewOrStream(Resource name, ISession caller) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsViewOrStream(String name, ISession caller) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reloadStoredQueries(ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public SDFSchema getOutputSchema(int queryId, ISession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addStoredProcedure(String name, StoredProcedure proc, ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeStoredProcedure(String name, ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public StoredProcedure getStoredProcedure(String name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StoredProcedure> getStoredProcedures(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsStoredProcedures(String name, ISession caller) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getOperatorNames(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LogicalOperatorInformation> getOperatorInformations(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicalOperatorInformation getOperatorInformation(String name, ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IUser> getUsers(ISession caller) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUpdateEventListener(IUpdateEventListener listener, String type, ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeUpdateEventListener(IUpdateEventListener listener, String type, ISession caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<String> getUdfs() {
		// TODO Auto-generated method stub
		return null;
	}

}
