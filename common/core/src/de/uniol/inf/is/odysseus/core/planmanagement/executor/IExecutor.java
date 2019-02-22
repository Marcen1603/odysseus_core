/**********************************************************************************
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.planmanagement.executor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.mep.IFunctionSignatur;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.SinkInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
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
 * IExecutor stellt die Hauptschnittstelle fuer externe Anwendungen zu Odysseus
 * da. Es werden Funktionen zur Bearbeitung von Anfragen, Steuerung der
 * Ausfuehrung und Konfigurationen zur Verfuegung gestellt. Weiterhin bietet
 * diese Schnittstelle diverse Moeglichkeiten, um Nachrichten ueber aenderungen
 * innerhalb von Odysseus zu erhalten.
 *
 * @author Wolf Bauer, Marco Grawunder
 *
 */
public interface IExecutor extends IClientPlanManager {

	/**
	 * List of predefined registered build configurations
	 */
	Collection<String> getQueryBuildConfigurationNames(ISession session);

	/**
	 * lists the IDs of all currently installed parsers, which can be used to
	 * translate a query.
	 *
	 * @throws PlanManagementException
	 */
	Set<String> getSupportedQueryParsers(ISession session)
			throws PlanManagementException;

	/**
	 * Lists the names of all available metadata
	 */
	Set<String> getMetadataNames(ISession session);

	/**
	 * A list of tokens like static key words for a certain query parser that
	 * can be, e.g. used for visualization.
	 *
	 * @param queryParser
	 *            the ID of the query parser
	 * @param user
	 *            current user
	 * @return a parser dependent key-multiple-values map
	 */
	Map<String, List<String>> getQueryParserTokens(String queryParser,
			ISession user);

	/**
	 * Returns a list of suggestions for a certain query parser based on a hint.
	 * This method strongly depends on the query parser
	 *
	 * @param queryParser
	 *            the ID of the parser
	 * @param hint
	 *            the hint that is used by the parser
	 * @param user
	 *            the current user
	 * @return a list of suggestions
	 */
	List<String> getQueryParserSuggestions(String queryParser,
			String hint, ISession user);


	/**
	 * Adds a new query
	 *
	 * @param query
	 *            The query defined as a string
	 * @param parserID
	 *            The ID of the parser that should be used to translate the
	 *            query
	 * @param queryBuildConfigurationName
	 *            The name of the the build configuration that should be used
	 *            during the query processing
	 *
	 * @param context
	 *            Allows to pass some context based information to the query
	 *            processing, e.g. current directories.
	 *
	 * @return A (potential empty) list of the IDs of the installed queries
	 * @throws PlanManagementException
	 */
	Collection<Integer> addQuery(String query, String parserID,
			ISession user, Context context)
			throws PlanManagementException;

	/**
	 * Way to run a generic command
	 *
	 * @param command
	 * @param caller
	 */
	void runCommand(String commandExpression, ISession caller);

	/**
	 * This method tries to translate the given query to a logical plan and
	 * delivers the output schema for the root operator Remark: It must be
	 * sure, that there in only one output operator, else the first one will be
	 * choosen that is found!
	 *
	 * @param query
	 *            The query text
	 * @param parserID
	 *            The parser to translate the query
	 * @param user
	 *            The caller of this method to validate user right
	 * @param port
	 *            The port for which the schema should be returned
	 * @return The output schema from the top operator for the given port
	 */
	SDFSchema determineOutputSchema(String query, String parserID,
			ISession user, int port, Context context);

	// TODO, Multiple roots
	// SDFSchema determinedOutputSchema(String query, String parserID,
	// ISession user, String queryBuildConfigurationName, String rootName, int
	// port);

	/**
	 * Returns the logical query for the given id
	 *
	 * @param id
	 *            The ID of the query
	 * @param session
	 *            The current user
	 * @return the logical query
	 */
	ILogicalQuery getLogicalQueryById(int id, ISession session);

	/**
	 * Returns the logical query for the given name
	 *
	 * @param name
	 *            The name of the query
	 * @param session
	 *            The current user
	 * @return the logical query
	 */
	ILogicalQuery getLogicalQueryByName(Resource name, ISession session);

	/**
	 * Returns the logical query for the given input. 
	 * This is a convenient method. The system first checks, if there is a
	 * logical query with the id idOrName the query will be returned. In other cases
	 * the system tries to find a query with the name idOrName. Could return null
	 * if no query with this id oder name is found.
	 *
	 * @param idOrName
	 *            The id or the name of the query
	 * @param session
	 *            The current user
	 * @return the logical query
	 */
	ILogicalQuery getLogicalQueryByString(String idOrName, ISession session);
	

	/**
	 * Returns all IDs of all logical queries for the given user
	 *
	 * @param session
	 *            The current user
	 * @return The IDs of the logical queries
	 */
	Collection<Integer> getLogicalQueryIds(ISession session);

	/**
	 * Return the current state of the query with the given queryID
	 * @param queryID The query id for which the state should be retrieved
	 * @param session
	 * @return
	 */
	QueryState getQueryState(int queryID, ISession session);

	/**
	 * Return the current state of the query with the given queryname
	 * @param queryName
	 * @param session
	 * @return
	 */
	QueryState getQueryState(String queryName, ISession session);


	/**
	 * Returns the current state of queries
	 * @param id a list of query ids for which the state should be delivered
	 * @param sessions for each query id the corresponding session information
	 * @return a list of query states where the order is the same as in the input list
	 */
	List<QueryState> getQueryStates(List<Integer> id, List<ISession> session);

	/**
	 * Returns all root operators of the physical query that has the given
	 * queryID
	 *
	 * @param queryID
	 *            The ID of the query
	 * @param session
	 *            The current user
	 * @return All physical root operators
	 */
	List<IPhysicalOperator> getPhysicalRoots(int queryID,
			ISession session);

	/**
	 * Starts all queries that are currently not running
	 *
	 * @param session
	 *            The current user
	 * @return List of queries that could be started
	 */
	Collection<Integer> startAllClosedQueries(ISession user);

	/**
	 * Provides a set of all registered buffer placement strategies
	 *
	 * @param session
	 *            The current user
	 *
	 * @return A set of IDs of the strategies
	 */
	Set<String> getRegisteredBufferPlacementStrategiesIDs(
			ISession session);

	/**
	 * Provides a set of all registered scheduling strategies
	 *
	 * @param session
	 *            The current user
	 *
	 * @return A set of IDs of the scheduling strategies
	 */
	Set<String> getRegisteredSchedulingStrategies(ISession session);

	/**
	 * Provides a set of all registered schedulers
	 *
	 * @param session
	 *            The current user
	 * @return Set of scheduler IDs
	 */
	Set<String> getRegisteredSchedulers(ISession session);

	/**
	 * Sets the the scheduler with a scheduling strategy which should be used
	 * for creating a concrete scheduler.
	 *
	 * @param scheduler
	 *            The scheduler factory which should be used for creating
	 *            concrete scheduler.
	 * @param schedulerStrategy
	 *            The scheduling strategy factory which should be used by
	 *            scheduler for creating concrete scheduler.
	 * @param session
	 *            The current user
	 */
	void setScheduler(String scheduler, String schedulerStrategy,
			ISession session);

	/**
	 * Get the current active scheduler represented by an id.
	 *
	 * @param session
	 *            The current user
	 *
	 * @return id of the current scheduler
	 */
	String getCurrentSchedulerID(ISession session);

	/**
	 * Get the current active scheduling strategy factory represented by an id.
	 *
	 * @param session
	 *            The current user
	 * @return id of the current active scheduling strategy factory
	 */
	String getCurrentSchedulingStrategyID(ISession session);

	/**
	 * Returns all registered data types
	 *
	 * @param caller
	 *            The current user
	 * @return The set of data types
	 */
	Set<SDFDatatype> getRegisteredDatatypes(ISession caller);

	/**
	 * Returns all registered wrapper names
	 *
	 * @param caller
	 *            The current user
	 * @return A set of wrapper IDs
	 */
	Set<String> getRegisteredWrapperNames(ISession caller);

	/**
	 * Returns all registered aggregated functions for a certain data model
	 *
	 * @param datamodel
	 *            The data model
	 * @param caller
	 *            The current user
	 * @return A list of the function
	 */
	@Deprecated
	Set<String> getRegisteredAggregateFunctions(
			@SuppressWarnings("rawtypes") Class<? extends IStreamObject> datamodel,
			ISession caller);

	/**
	 * Returns all registered aggregated functions for a certain data model
	 *
	 * @param datamodel
	 *            The data model
	 * @param caller
	 *            The current user
	 * @return A list of the function
	 */
	
	Set<String> getRegisteredAggregateFunctions(String datamodel,
			ISession caller);

	/**
	 * The name of the executor
	 *
	 * @return the name
	 */
	String getName();

	// Facade
	// Session Management methods
	/**
	 * Logs a user in and creates a session
	 *
	 * @param username
	 *            The user name
	 * @param password
	 *            The password of the user
	 * @param tenantname
	 *            The tenant
	 * @return the session of the logged in user
	 */
	ISession login(String username, byte[] password, String tenantname);

	/**
	 * Logs a user in and creates a session (with default tenant)
	 *
	 * @param username
	 *            The user name
	 * @param password
	 *            The password of the user
	 * @return the session of the logged in user
	 */
	ISession login(String username, byte[] password);


	/**
	 * Logs the user out
	 *
	 * @param caller
	 *            The user to be logged out
	 */
	void logout(ISession caller);

	/**
	 * Check if the given session is still valid
	 * @param session
	 * @return true, if session
	 */
	boolean isValid(ISession session);

	// Facade for DataDictionary
	/**
	 * Removes a sink using its simple name
	 *
	 * @param name
	 *            The name of the sink
	 * @param caller
	 *            The current user
	 * @return The operator of the sink that was removed
	 */
	ILogicalPlan removeSink(String name, ISession caller);

	/**
	 * Removes a sink using its resource name
	 *
	 * @param name
	 *            The resource description of the sink
	 * @param caller
	 *            The current user
	 * @return The operator of the sink that was removed
	 */
	ILogicalPlan removeSink(Resource name, ISession caller);

	/**
	 * Removes a view or stream using its simple name
	 *
	 * @param name
	 *            The name of the stream or view
	 * @param caller
	 *            The current user
	 */
	void removeViewOrStream(String name, ISession caller);

	/**
	 * Removes a view or stream using its resource description
	 *
	 * @param name
	 *            The resource description of the stream or view
	 * @param caller
	 *            The current user
	 */
	void removeViewOrStream(Resource name, ISession caller);

	List<ViewInformation> getStreamsAndViewsInformation(ISession caller);

	/**
	 * Delivers a user-dependent set of all installed sinks
	 *
	 * A sink is named by a resource description and is represented by its top
	 * most logical operator
	 *
	 * @param caller
	 *            The current user
	 * @return Pairs of resource description and the top operator
	 */
	List<SinkInformation> getSinks(ISession caller);

	/**
	 * Checks whether the stream or view exists.
	 *
	 * @param name
	 *            The resource description to be checked
	 * @param caller
	 *            the current user
	 * @return true, if the view or stream exists
	 */
	boolean containsViewOrStream(Resource name, ISession caller);

	/**
	 * Checks whether the stream or view exists.
	 *
	 * @param name
	 *            The name to be checked
	 * @param caller
	 *            the current user
	 * @return true, if the view or stream exists
	 */
	boolean containsViewOrStream(String name, ISession caller);

	/**
	 * Checks whether the sink exists
	 * @param name The name of the sink
	 * @param caller The current user
	 * @return true, if the sink exits
	 */
	boolean containsSink(String name, ISession caller);
	
	/**
	 * Invokes to reload stored queries.
	 *
	 * @param caller
	 *            The current user
	 */
	void reloadStoredQueries(ISession caller);

	/**
	 * Returns the output schema of the query identified by the given ID
	 *
	 * @param queryId
	 *            The ID of the query
	 * @param session
	 *            The current user
	 * @return the schema of the query
	 */
	SDFSchema getOutputSchema(int queryId, ISession session);

	/**
	 * Adds a new stored procedure, which should be saved
	 *
	 * @param name
	 *            A unique name of the procedure
	 * @param proc
	 *            The stored procedure
	 * @param caller
	 *            The current user
	 */
	void addStoredProcedure(String name, StoredProcedure proc,
			ISession caller);

	/**
	 * Removes a saved procedure
	 *
	 * @param name
	 *            The name of the stored procedure that should be removed
	 * @param caller
	 *            The current user
	 */
	void removeStoredProcedure(String name, ISession caller);

	/**
	 * Gets the stored procedure by its name
	 *
	 * @param name
	 *            The name of the procedure
	 * @param caller
	 *            The current user
	 * @return The stored procedure
	 */
	StoredProcedure getStoredProcedure(String name, ISession caller);

	/**
	 * Returns the list of all saved stored procedures for the user
	 *
	 * @param caller
	 *            The current user
	 * @return A list of stored procedures
	 */
	List<StoredProcedure> getStoredProcedures(ISession caller);

	/**
	 * Checks whether a stored procedure exists
	 *
	 * @param name
	 *            The name of the stored procedure
	 * @param caller
	 *            The current user
	 * @return true, if there is a stored procedure with this name
	 */
	boolean containsStoredProcedures(String name, ISession caller);

	/**
	 * Gets the name of all installed operators
	 *
	 * @param caller
	 *            The current user
	 * @return A list of the names
	 */
	List<String> getOperatorNames(ISession caller);

	/**
	 * Gets a description of all installed operators that can be used e.g. in
	 * PQL
	 *
	 * @param caller
	 *            The current user
	 * @return a list of operator information
	 */
	List<LogicalOperatorInformation> getOperatorInformations(
			ISession caller);

	/**
	 * Gets the logical description of a certain operator
	 *
	 * @param name
	 *            The name of the operator
	 * @param caller
	 *            The current user
	 * @return a description of the logical operator
	 */
	LogicalOperatorInformation getOperatorInformation(String name,
			ISession caller);


	
	Set<IFunctionSignatur> getMepFunctions();
	
	
	// User Management

	/**
	 * @param caller
	 *
	 */
	List<IUser> getUsers(ISession caller);

	/**
	 * Generic Event Listener (for updates in views)
	 */

	void addUpdateEventListener(IUpdateEventListener listener, String type,
			ISession caller);

	void removeUpdateEventListener(IUpdateEventListener listener, String type,
			ISession caller);

	Collection<String> getUdfs();


}
