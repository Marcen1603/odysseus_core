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

import de.uniol.inf.is.odysseus.core.datadictionary.IAddDataType;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

public interface IDataDictionary extends IAddDataType {

	String createUserUri(String resource, ISession caller);
	
	// -------------------------------------------------------------------------
	// Entity Management
	// -------------------------------------------------------------------------
	void addEntitySchema(String uri, SDFSchema entity, ISession user)
			throws PermissionException;

	SDFSchema getEntitySchema(String uri, ISession caller)
			throws PermissionException, DataDictionaryException;

	// -------------------------------------------------------------------------
	// View and Stream Management
	// -------------------------------------------------------------------------

	ILogicalOperator getViewOrStream(String viewname, ISession caller)
			throws DataDictionaryException;

	ILogicalOperator removeViewOrStream(String viewname, ISession caller);

	boolean containsViewOrStream(String viewName, ISession user);

	Set<Entry<String, ILogicalOperator>> getStreamsAndViews(ISession caller);

	// -------------------------------------------------------------------------
	// View Management
	// -------------------------------------------------------------------------

	// boolean isView(String name);

	void setView(String viewname, ILogicalOperator topOperator, ISession caller)
			throws DataDictionaryException;

	Set<Entry<String, ILogicalOperator>> getViews(ISession caller);

	ILogicalOperator getView(String viewname, ISession caller);

	// -------------------------------------------------------------------------
	// Stream Management
	// -------------------------------------------------------------------------

	void setStream(String streamname, ILogicalOperator plan, ISession caller)
			throws DataDictionaryException;

	Set<Entry<String, ILogicalOperator>> getStreams(ISession caller);

	ILogicalOperator getStreamForTransformation(String name, ISession caller);

	AccessAO getStream(String viewname, ISession caller)
			throws DataDictionaryException;

	// -------------------------------------------------------------------------
	// Sink Management
	// -------------------------------------------------------------------------

	void addSink(String sinkname, ILogicalOperator sink, ISession caller)
			throws DataDictionaryException;

	ILogicalOperator removeSink(String name, ISession caller);

	Set<Entry<String, ILogicalOperator>> getSinks(ISession caller);

	ILogicalOperator getSinkTop(String sinkname, ISession caller)
			throws DataDictionaryException;

	ILogicalOperator getSinkInput(String sinkname, ISession caller)
			throws DataDictionaryException;

	boolean existsSink(String sinkname, ISession caller);

	// -------------------------------------------------------------------------
	// Datatype Management
	// -------------------------------------------------------------------------
	void removeDatatype(String name) throws DataDictionaryException;

	SDFDatatype getDatatype(String name) throws DataDictionaryException;

	Set<String> getDatatypes();

	boolean existsDatatype(String dtName);

	// -------------------------------------------------------------------------
	// Query Management
	// -------------------------------------------------------------------------

	void addQuery(ILogicalQuery q, ISession caller, String queryBuildConfigName);

	ILogicalQuery getQuery(int id, ISession caller);

	String getQueryBuildConfigName(int id);

	List<ILogicalQuery> getQueries(IUser user, ISession caller);

	void removeQuery(ILogicalQuery q, ISession caller);

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

//	boolean isCreatorOfObject(String caller, String objecturi);

	// ----------------------------------------------------------
	// Operatormanagement
	// ----------------------------------------------------------
	
	boolean containsOperator(String id);
	void setOperator(String id, IPhysicalOperator physical);
	void removeOperator(String id);
	IPhysicalOperator getOperator(String id);
	
	// ------------------------------------------
	// Physical sinks and sources (from WrapperPlanFactory)
	// ------------------------------------------
	ISource<?> getAccessPlan(String uri);

	void putAccessPlan(String uri, ISource<?> s);

	Map<String, ISource<?>> getSources();

	void clearSources();

	void removeClosedSources();

	void removeClosedSinks();

	ISink<?> getSinkplan(String sinkName);

	void putSinkplan(String name, ISink<?> sinkPO);

	// ----------------------------------------------------------
	// DD Listener
	// ----------------------------------------------------------

	void addListener(IDataDictionaryListener listener);

	void removeListener(IDataDictionaryListener listener);




}