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
package de.uniol.inf.is.odysseus.core.server.datadictionary;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public interface IDataDictionary {

	Resource getResource(String resourceName, ISession caller);

	// -------------------------------------------------------------------------
	// View and Stream Management
	// -------------------------------------------------------------------------

	ILogicalPlan getViewOrStream(String viewname, ISession caller) throws DataDictionaryException;

	Resource getViewOrStreamName(String sourceName, ISession caller);

	boolean containsViewOrStream(String viewName, ISession user);

	boolean containsViewOrStream(Resource viewName, ISession user);

	Set<Entry<Resource, ILogicalPlan>> getStreamsAndViews(ISession caller);

	Set<Entry<Resource, IAccessAO>> getAccessAOs(ISession caller);

	// -------------------------------------------------------------------------
	// View Management
	// -------------------------------------------------------------------------

	Set<Entry<Resource, ILogicalPlan>> getViews(ISession caller);

	ILogicalPlan getView(String viewname, ISession caller);

	ILogicalPlan getView(Resource view, ISession caller);

	// -------------------------------------------------------------------------
	// Stream Management
	// -------------------------------------------------------------------------

	Set<Entry<Resource, ILogicalPlan>> getStreams(ISession caller);

	StreamAO getStream(String viewname, ISession caller) throws DataDictionaryException;

	ILogicalPlan getStreamForTransformation(Resource name, ISession caller);

	ILogicalPlan getStreamForTransformation(String streamName, ISession caller);

	// -------------------------------------------------------------------------
	// Sink Management
	// -------------------------------------------------------------------------

	Set<Entry<Resource, ILogicalPlan>> getSinks(ISession caller);

	ILogicalPlan getSinkTop(String sinkname, ISession caller) throws DataDictionaryException;

	ILogicalPlan getSinkForTransformation(Resource name, ISession caller);

	ILogicalPlan getSinkForTransformation(String name, ISession caller);

	boolean containsSink(Resource sinkname, ISession caller);

	boolean containsSink(String sinkname, ISession caller);

	// -------------------------------------------------------------------------
	// Datatype Management
	// -------------------------------------------------------------------------
	SDFDatatype getDatatype(String name) throws DataDictionaryException;

	SDFDatatype getDatatype(SDFDatatype upperType, List<SDFDatatype> subTypeList);

	Set<String> getDatatypeNames();

	Set<SDFDatatype> getDatatypes();

	boolean existsDatatype(String dtName);

	// -------------------------------------------------------------------------
	// Query Management
	// -------------------------------------------------------------------------

	// returns the query text
	String getQuery(int id, ISession caller);

	String getQueryBuildConfigName(int id);

	// returns the query texts
	List<String> getQueries(IUser user, ISession caller);

	// -------------------------------------------------------------------------
	// Rights Management
	// -------------------------------------------------------------------------

	/**
	 * checks if the given user has higher permission as the given action. Calls
	 * the corresponding method in the action class.
	 *
	 * @param action
	 * @param objecturi
	 * @param user
	 * @return boolean
	 */
	boolean hasSuperAction(DataDictionaryPermission action, ISession user);

	// boolean isCreatorOfObject(String caller, String objecturi);

	// ----------------------------------------------------------
	// Operatormanagement
	// ----------------------------------------------------------

	boolean containsOperator(Resource id);

	IPhysicalOperator getOperator(Resource id, ISession caller);

	// ------------------------------------------
	// Physical sinks and sources (from WrapperPlanFactory)
	// ------------------------------------------
	// ISource<?> getAccessPlan(String uri);

	ISource<?> getAccessPlan(Resource streamname);

	Map<Resource, ISource<?>> getSources();

	ISink<?> getSinkplan(Resource sinkName);

	// ----------------------------------------------------------
	// DD Listener
	// ----------------------------------------------------------

	void addListener(IDataDictionaryListener listener);

	void removeListener(IDataDictionaryListener listener);

	void addSinkListener(IDataDictionarySinkListener listener);

	void removeSinkListener(IDataDictionarySinkListener listener);

	// -------------------------------------------------------------------------
	// Stored Procedure Management
	// -------------------------------------------------------------------------

	boolean containsStoredProcedure(String name, ISession user);

	StoredProcedure getStoredProcedure(String name, ISession user);

	List<StoredProcedure> getStoredProcedures(ISession caller);

	// ---------------------------------------------------------------------------
	// Stores
	// ---------------------------------------------------------------------------
	boolean containsStore(String name, ISession user);

	IStore<String, Object> getStore(String name, ISession user);

	List<IStore<String, Object>> getStores(ISession user);


	// -------------------------------------------------------------------------
	// Init
	// -------------------------------------------------------------------------

	IDataDictionary createInstance(ITenant t);

	// Store management
	List<IStreamObject<?>> getStore(Resource name);
	
	// Registry Management
	ITransportHandlerRegistry getTransportHandlerRegistry(ISession user);
	IProtocolHandlerRegistry getProtocolHandlerRegistry(ISession user);
	IDataHandlerRegistry getDataHandlerRegistry(ISession user);
	
	

}