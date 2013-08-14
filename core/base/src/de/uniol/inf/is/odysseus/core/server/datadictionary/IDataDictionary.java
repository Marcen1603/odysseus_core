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

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public interface IDataDictionary {

	String createUserUri(String resource, ISession caller);

	// -------------------------------------------------------------------------
	// View and Stream Management
	// -------------------------------------------------------------------------

	ILogicalOperator getViewOrStream(String viewname, ISession caller)
			throws DataDictionaryException;

	boolean containsViewOrStream(String viewName, ISession user);

	Set<Entry<String, ILogicalOperator>> getStreamsAndViews(ISession caller);

	// -------------------------------------------------------------------------
	// View Management
	// -------------------------------------------------------------------------

	Set<Entry<String, ILogicalOperator>> getViews(ISession caller);

	ILogicalOperator getView(String viewname, ISession caller);

	// -------------------------------------------------------------------------
	// Stream Management
	// -------------------------------------------------------------------------

	Set<Entry<String, ILogicalOperator>> getStreams(ISession caller);

	ILogicalOperator getStreamForTransformation(String name, ISession caller);

	StreamAO getStream(String viewname, ISession caller)
			throws DataDictionaryException;

	// -------------------------------------------------------------------------
	// Sink Management
	// -------------------------------------------------------------------------

	Set<Entry<String, ILogicalOperator>> getSinks(ISession caller);

	ILogicalOperator getSinkTop(String sinkname, ISession caller)
			throws DataDictionaryException;

	ILogicalOperator getSinkInput(String sinkname, ISession caller)
			throws DataDictionaryException;

	ILogicalOperator getSinkForTransformation(String name, ISession caller);

	boolean containsSink(String sinkname, ISession caller);

	// -------------------------------------------------------------------------
	// Datatype Management
	// -------------------------------------------------------------------------
	SDFDatatype getDatatype(String name) throws DataDictionaryException;

	Set<String> getDatatypes();

	boolean existsDatatype(String dtName);
	
	// TODO: Should possible be removed?
	void removeDatatype(String name) throws DataDictionaryException;

	// -------------------------------------------------------------------------
	// Query Management
	// -------------------------------------------------------------------------

	ILogicalQuery getQuery(int id, ISession caller);

	String getQueryBuildConfigName(int id);

	List<ILogicalQuery> getQueries(IUser user, ISession caller);


	// -------------------------------------------------------------------------
	// Rights Management
	// -------------------------------------------------------------------------

	IUser getCreator(String resource);

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

	boolean containsOperator(String id);

	IPhysicalOperator getOperator(String id);

	// ------------------------------------------
	// Physical sinks and sources (from WrapperPlanFactory)
	// ------------------------------------------
	ISource<?> getAccessPlan(String uri);

	Map<String, ISource<?>> getSources();

	ISink<?> getSinkplan(String sinkName);

	// ----------------------------------------------------------
	// DD Listener
	// ----------------------------------------------------------

	void addListener(IDataDictionaryListener listener);

	void removeListener(IDataDictionaryListener listener);

	// -------------------------------------------------------------------------
	// Stored Procedure Management
	// -------------------------------------------------------------------------
	
	boolean containsStoredProcedure(String name, ISession user);

	StoredProcedure getStoredProcedure(String name, ISession user);

	List<StoredProcedure> getStoredProcedures(ISession caller);

	// -------------------------------------------------------------------------
	// Init 
	// -------------------------------------------------------------------------
	
	IDataDictionary createInstance(ITenant t);
}