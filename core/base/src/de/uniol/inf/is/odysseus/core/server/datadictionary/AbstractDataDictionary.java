/** Copyright [2011] [The Odysseus Team]
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.ExecutorPermission;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.StoreException;
import de.uniol.inf.is.odysseus.core.server.usermanagement.NullUserException;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.server.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.RemoveOwnersGraphVisitor;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

abstract public class AbstractDataDictionary implements IDataDictionary {

	protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}

	private List<IDataDictionaryListener> listeners = new ArrayList<IDataDictionaryListener>();
	protected IStore<String, ILogicalOperator> streamDefinitions;
	protected IStore<String, IUser> viewOrStreamFromUser;
	protected IStore<String, ILogicalOperator> viewDefinitions;
	protected IStore<String, SDFSchema> entityMap;
	protected IStore<String, IUser> entityFromUser;
	protected IStore<String, String> sourceTypeMap;
	protected IStore<String, SDFDatatype> datatypes;
	protected IStore<Integer, ILogicalQuery> savedQueries;
	protected IStore<Integer, IUser> savedQueriesForUser;
	protected IStore<Integer, String> savedQueriesBuildParameterName;

	
	protected IStore<String, ILogicalOperator> sinkDefinitions;
	protected IStore<String, IUser> sinkFromUser;

	public AbstractDataDictionary() {
	}

	protected void initDatatypes() {
		/**
		 * fill in the built-in datatypes
		 */
		if (datatypes.entrySet().size() == 0) {
			SDFDatatype.registerDefaultTypes(this);
		}
	}

	// ----------------------------------------------------------------------------
	// Entity Management
	// ----------------------------------------------------------------------------

	@Override
	public void addEntitySchema(String uri, SDFSchema entity, ISession caller)
			throws PermissionException {
		if (hasPermission(caller, DataDictionaryPermission.ADD_ENTITY)) {
			try {
				this.entityMap.put(uri, entity);
				this.entityFromUser.put(uri, caller.getUser());
				fireDataDictionaryChangedEvent();
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ "has no permission to add entities.");
		}
	}

	@Override
	public SDFSchema getEntitySchema(String uri, ISession caller)
			throws PermissionException, DataDictionaryException {
		checkAccessRights(uri, caller, DataDictionaryPermission.GET_ENTITY);
		SDFSchema ret = entityMap.get(uri);
		if (ret == null) {
			throw new DataDictionaryException("no such entity: " + uri);
		}
		return ret;
	}

	// TODO: REMOVE Entity

	/**
	 * returns the username from the creator of the given entity
	 * 
	 * @param entityuri
	 * @return username
	 * @throws PermissionException
	 */
	// no restric
	@Override
	public String getUserForEntity(String entityuri) {
		IUser user = this.entityFromUser.get(entityuri);
		return user != null ? user.getName() : null;
	}

	// ----------------------------------------------------------------------------
	// Source Management
	// ----------------------------------------------------------------------------

	// no restrict
	@Override
	public void addSourceType(String sourcename, String sourcetype) {
		sourceTypeMap.put(sourcename, sourcetype);
	}

	@Override
	public String getSourceType(String sourcename) throws DataDictionaryException {
		String value = sourceTypeMap.get(sourcename);
		if (value == null) {
			throw new DataDictionaryException("missing source type for: "
					+ sourcename);
		}
		return value;
	}

	// no restric
	@Override
	public SDFSource createSDFSource(String sourcename) throws DataDictionaryException {
		String type = getSourceType(sourcename);
		SDFSource source = new SDFSource(sourcename, type);

		return source;
	}

	// no restric
	@Override
	public boolean emptySourceTypeMap() {
		return sourceTypeMap.isEmpty();
	}

	// ------------------------------------------------------------------------
	// View Management
	// ------------------------------------------------------------------------

	@Override
	@SuppressWarnings("unchecked")
	public void setView(String viewname, ILogicalOperator topOperator,
			ISession caller) throws DataDictionaryException {
		if (hasPermission(caller, DataDictionaryPermission.ADD_VIEW)) {
			if (viewDefinitions.containsKey(viewname)) {
				throw new DataDictionaryException("View " + viewname
						+ " already exists. Drop First");
			}
			try {
				// Remove Owner from View
				RemoveOwnersGraphVisitor<ILogicalOperator> visitor = new RemoveOwnersGraphVisitor<ILogicalOperator>();
				@SuppressWarnings("rawtypes")
				AbstractGraphWalker walker = new AbstractGraphWalker();
				walker.prefixWalk(topOperator, visitor);

				this.viewDefinitions.put(viewname, topOperator);
				viewOrStreamFromUser.put(viewname, caller.getUser());
				fireDataDictionaryChangedEvent();
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			fireAddEvent(viewname, topOperator);
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has no permission to add a view.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILogicalOperator getView(String viewname, ISession caller) {
		if (this.viewDefinitions.containsKey(viewname)) {
			checkAccessRights(viewname, caller, DataDictionaryPermission.READ);
			ILogicalOperator logicalPlan = this.viewDefinitions.get(viewname);
			CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(
					null);
			@SuppressWarnings("rawtypes")
			AbstractGraphWalker walker = new AbstractGraphWalker();
			walker.prefixWalk(logicalPlan, copyVisitor);
			return copyVisitor.getResult();
		} else {
			return null;
		}
	}

	private ILogicalOperator removeView(String viewname, ISession caller) {
		ILogicalOperator op;
		try {
			op = viewDefinitions.remove(viewname);
			if (op != null) {
				viewOrStreamFromUser.remove(viewname);
			}
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}
		if (op != null) {
			fireRemoveEvent(viewname, op);
			fireDataDictionaryChangedEvent();
		}
		return op;
	}

	// no restric
	@Override
	public boolean isView(String name) {
		return this.viewDefinitions.containsKey(name);
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getViews(ISession caller) {
		return getDefinitions(caller, viewDefinitions);
	}

	// ------------------------------------------------------------------------
	// Stream Management
	// ------------------------------------------------------------------------

	@Override
	public void setStream(String streamname, ILogicalOperator plan,
			ISession caller) throws DataDictionaryException {
		if (hasPermission(caller, DataDictionaryPermission.ADD_STREAM)) {
			if (!this.entityMap.containsKey(streamname)) {
				throw new DataDictionaryException("No entity for " + streamname
						+ ". Add entity before adding access operator.");
			}
			if (streamDefinitions.containsKey(streamname)) {
				throw new DataDictionaryException("Stream " + streamname
						+ " already exists. Remove First");
			}
			try {
				streamDefinitions.put(streamname, plan);
				viewOrStreamFromUser.put(streamname, caller.getUser());
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			fireAddEvent(streamname, plan);
			fireDataDictionaryChangedEvent();
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has no permission to set a new view.");
		}
	}

	@Override
	public AccessAO getStream(String viewname, ISession caller) throws DataDictionaryException {
		checkAccessRights(viewname, caller, DataDictionaryPermission.READ);

		if (!this.streamDefinitions.containsKey(viewname)) {
			throw new DataDictionaryException("no such view: " + viewname);
		}

		SDFSource source = createSDFSource(viewname);
		AccessAO ao = new AccessAO(source);

		SDFSchema entity = getEntitySchema(viewname, caller);
		ao.setOutputSchema(entity);

		return ao;
	}

	@Override
	public ILogicalOperator getStreamForTransformation(String name,
			ISession caller) {
		checkAccessRights(name, caller, DataDictionaryPermission.READ);
		return streamDefinitions.get(name);
	}

	// no restric
	@Override
	public boolean existsSource(String name) {
		return streamDefinitions.containsKey(name);
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getStreams(ISession caller) {
		return getDefinitions(caller, streamDefinitions);
	}

	// ------------------------------------------------------------------------
	// Access View or Streams
	// ------------------------------------------------------------------------

	@Override
	public Set<Entry<String, ILogicalOperator>> getStreamsAndViews(
			ISession caller) {
		Set<Entry<String, ILogicalOperator>> sources = new HashSet<Entry<String, ILogicalOperator>>();

		sources.addAll(getStreams(caller));
		sources.addAll(getViews(caller));

		return sources;
	}

	private Set<Entry<String, ILogicalOperator>> getDefinitions(
			ISession caller, IStore<String, ILogicalOperator> definitions) {
		Set<Entry<String, ILogicalOperator>> sources = new HashSet<Entry<String, ILogicalOperator>>();
		for (Entry<String, ILogicalOperator> viewEntry : definitions.entrySet()) {
			if (hasAccessRights(viewEntry.getKey(), caller,
					DataDictionaryPermission.READ)) {
				sources.add(viewEntry);
			}
		}
		return sources;
	}

	@Override
	public ILogicalOperator getViewOrStream(String viewname, ISession caller) throws DataDictionaryException {
		if (this.viewDefinitions.containsKey(viewname)) {
			return getView(viewname, caller);
		} else {
			return getStream(viewname, caller);
		}
	}

	@Override
	public ILogicalOperator removeViewOrStream(String viewname, ISession caller) {
		if (this.viewDefinitions.containsKey(viewname)) {
			checkAccessRights(viewname, caller,
					DataDictionaryPermission.REMOVE_VIEW);
			return removeView(viewname, caller);
		} else {
			ILogicalOperator op;
			checkAccessRights(viewname, caller,
					DataDictionaryPermission.REMOVE_STREAM);
			op = streamDefinitions.remove(viewname);
			if (op != null) {
				viewOrStreamFromUser.remove(viewname);
			}
			if (op != null)
				fireRemoveEvent(viewname, op);
			return op;
		}
	}

	// no restric
	@Override
	public boolean containsViewOrStream(String viewName, ISession user) {
		return this.streamDefinitions.containsKey(viewName)
				|| this.viewDefinitions.containsKey(viewName);
	}

	// no restric
	@Override
	public IUser getCreator(String resource) {
		IUser ret = viewOrStreamFromUser.get(resource);
		if (ret == null) {
			ret = sinkFromUser.get(resource);
		}
		return ret;

	}

	// ----------------------------------------------------------------------------
	// Datatype Management
	// ----------------------------------------------------------------------------

//	@Override
//	public void addDatatype(String name, SDFDatatype dt, ISession caller) {
//		if (hasPermission(caller, DataDictionaryPermission.ADD_DATATYPE)) {
//			addDatatype(name, dt);
//		} else {
//			throw new PermissionException("User " + caller.getUser().getName()
//					+ " has not the permission to create new data types");
//		}
//	}

	@Override
	public void addDatatype(String name, SDFDatatype dt){
		if (!this.datatypes.containsKey(name.toLowerCase())) {
			this.datatypes.put(name.toLowerCase(), dt);
			fireDataDictionaryChangedEvent();
		} else {
			System.out.println(this.datatypes);
			throw new DataDictionaryException("Type '" + name
					+ "' already exists.");
		}
	}
	
	@Override
	public void removeDatatype(String name) throws DataDictionaryException {
		if (this.datatypes.containsKey(name.toLowerCase())) {
			this.datatypes.remove(name.toLowerCase());
			fireDataDictionaryChangedEvent();
		} else {
			throw new DataDictionaryException("Type '" + name
					+ "' not exists.");
		}
	}

	@Override
	public SDFDatatype getDatatype(String dtName) throws DataDictionaryException {
		if (this.datatypes.containsKey(dtName.toLowerCase())) {
			return this.datatypes.get(dtName.toLowerCase());
		} else {
			throw new DataDictionaryException("No such datatype: " + dtName);
		}
	}

	@Override
	public Set<String> getDatatypes() {
		return this.datatypes.keySet();
	}

	@Override
	public boolean existsDatatype(String dtName) {
		return this.datatypes.containsKey(dtName.toLowerCase());
	}

	// ----------------------------------------------------------------------------
	// Sink Management
	// ----------------------------------------------------------------------------

	@Override
	public void addSink(String sinkname, ILogicalOperator sink, ISession caller) throws DataDictionaryException {
		if (!this.sinkDefinitions.containsKey(sinkname)) {
			this.sinkDefinitions.put(sinkname, sink);
			this.sinkFromUser.put(sinkname, caller.getUser());
			fireDataDictionaryChangedEvent();
		} else {
			throw new DataDictionaryException("Sink name already used");
		}
	}

	@Override
	public ILogicalOperator getSinkTop(String sinkname, ISession caller) throws DataDictionaryException {
		if (this.sinkDefinitions.containsKey(sinkname)) {
			return sinkDefinitions.get(sinkname);
		} else {
			throw new DataDictionaryException("No such sink defined");
		}
	}

	@Override
	public ILogicalOperator getSinkInput(String sinkname, ISession caller) throws DataDictionaryException {
		ILogicalOperator sinkTop = getSinkTop(sinkname, caller);
		ILogicalOperator ret = sinkTop;
		while (ret.getSubscribedToSource().size() > 0) {
			ret = ret.getSubscribedToSource(0).getTarget();
		}
		return ret;
	}

	@Override
	public boolean existsSink(String sinkname) {
		return this.sinkDefinitions.containsKey(sinkname);
	}

	public IUser getUserForSink(String sinkname) {
		return this.sinkFromUser.get(sinkname);
	}

	// ----------------------------------------------------------------------------
	// Logical Plan Management
	// ----------------------------------------------------------------------------

	@Override
	public void addQuery(ILogicalQuery q, ISession caller, String buildParameterName) {
		this.savedQueries.put(q.getID(), q);
		this.savedQueriesForUser.put(q.getID(), caller.getUser());
		this.savedQueriesBuildParameterName.put(q.getID(), buildParameterName);
	}

	@Override
	public ILogicalQuery getQuery(int id, ISession caller) {
		if (hasPermission(caller, ExecutorPermission.ADD_QUERY, ExecutorPermission.objectURI)) {
			return this.savedQueries.get(id);
		} else {
			return null;
		}
	}

	@Override
	public List<ILogicalQuery> getQueries(IUser user, ISession caller) {
		List<ILogicalQuery> queries = new ArrayList<ILogicalQuery>();
		for (Entry<Integer, IUser> e : savedQueriesForUser.entrySet()) {
			if (e.getValue().equals(user)) {
				ILogicalQuery query = getQuery(e.getKey(), caller);
				if (query != null) {
					queries.add(query);
				}
			}
		}
		return queries;
	}

	@Override
	public String getQueryBuildConfigName(int id){
		return savedQueriesBuildParameterName.get(id);
	}
	
	@Override
	public void removeQuery(ILogicalQuery q, ISession caller) {
		this.savedQueries.remove(q.getID());
		this.savedQueriesForUser.remove(q.getID());
		this.savedQueriesBuildParameterName.remove(q.getID());
	}

	// ----------------------------------------------------------------------------
	// Listener Management
	// ----------------------------------------------------------------------------

	// no restric
	@Override
	public void addListener(IDataDictionaryListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("listener is null");

		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	// no restirc
	@Override
	public void removeListener(IDataDictionaryListener listener) {
		listeners.remove(listener);
	}

	private void fireAddEvent(String name, ILogicalOperator op) {
		for (IDataDictionaryListener listener : listeners) {
			try {
				listener.addedViewDefinition(this, name, op);
			} catch (Exception ex) {
				getLogger().error("Error during executing listener", ex);
			}
		}
	}

	private void fireRemoveEvent(String name, ILogicalOperator op) {
		for (IDataDictionaryListener listener : listeners) {
			try {
				listener.removedViewDefinition(this, name, op);
			} catch (Exception ex) {
				getLogger().error("Error during executing listener", ex);
			}
		}
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getSinks(ISession caller) {
		return this.sinkDefinitions.entrySet();
	}

	private void fireDataDictionaryChangedEvent() {
		for (IDataDictionaryListener listener : listeners) {
			listener.dataDictionaryChanged(this);
		}
	}

	@Override
	public ILogicalOperator removeSink(String name, ISession caller) {
		ILogicalOperator op = this.sinkDefinitions.remove(name);
		fireDataDictionaryChangedEvent();
		return op;
	}

	// -------------------------------------------------------------------------------------
	// Hilfsmethoden f�r Rechtemanagement
	// -------------------------------------------------------------------------------------

	/**
	 * return true if the given user has permission to access the given view in
	 * a certain way (action).
	 * 
	 * @param uri
	 * @param caller
	 * @param action
	 * @return boolean
	 */
	private boolean hasAccessRights(String resource, ISession caller,
			DataDictionaryPermission action) {
		if (hasPermission(caller, action, resource)
				// is owner
				|| isCreatorOfView(caller.getUser().getName(), resource)
				|| hasSuperAction(action, caller)) {
			return true;
		} else {
			return false;
		}
	}

	private void checkAccessRights(String resource, ISession caller,
			DataDictionaryPermission action) {
		if (!hasAccessRights(resource, caller, action)) {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has not the permission '" + action + "' on resource '"
					+ resource);
		}
	}

	private boolean hasPermission(ISession caller, IPermission permission,
			String objectURI) {
		return UserManagement.getUsermanagement().hasPermission(caller,
				permission, objectURI);
	}

	private boolean hasPermission(ISession caller, IPermission permission) {
		return hasPermission(caller, permission,
				DataDictionaryPermission.objectURI);
	}

	/**
	 * checks if the given user has higher permission as the given action. Calls
	 * the corresponding method in the action class.
	 * 
	 * @param action
	 * @param objecturi
	 * @param user
	 * @return boolean
	 */
	@Override
	public boolean hasSuperAction(DataDictionaryPermission action, ISession user) {
		return hasPermission(user,
				DataDictionaryPermission.hasSuperAction(action));
	}

	/**
	 * returns true if username equals creator of the given objecturi
	 * 
	 * @param username
	 * @param objecturi
	 * @return
	 */
	@Override
	public boolean isCreatorOfObject(String username, String objecturi) {
		if (objecturi == null) {
			return false;
		}
		if (username != null && !username.isEmpty()) {
			String user = getUserForEntity(objecturi);
			if (user == null || user.isEmpty()) {
				IUser userObj = getCreator(objecturi);
				user = userObj != null ? userObj.getName() : null;
			}
			if (user != null && !user.isEmpty()) {
				if (user.equals(username)) {
					return true;
				}
			}
			return false;
		} else {
			throw new NullUserException("Username is empty.");
		}
	}

	/**
	 * returns true if username euqlas creator of the given view
	 * 
	 * @param username
	 * @param viewname
	 * @return
	 */
	public boolean isCreatorOfView(String username, String viewname) {
		if (viewname == null) {
			return false;
		}
		if (!username.isEmpty()) {
			IUser user = getCreator(viewname);
			if (user != null) {
				if (user.getName().equals(username)) {
					return true;
				}
			}
			return false;
		} else {
			throw new NullUserException("Username is empty.");
		}
	}
	
	// ------------------------------------------------------------------------------
	// Methods for physical sinks and sources (moved from WrapperPlanFactory)
	// ------------------------------------------------------------------------------
	
	private static Map<String, ISource<?>> sources = new HashMap<String, ISource<?>>();
	private static Map<String, ISink<?>> sinks = new HashMap<String, ISink<?>>();

	@Override
	public synchronized ISource<?> getAccessPlan(String uri) {
		ISource<?> po = sources.get(uri);
		return po;
	}

	@Override
	public synchronized void putAccessPlan(String uri, ISource<?> s) {
		sources.put(uri, s);
	}
	
	@Override
	public synchronized Map<String, ISource<?>> getSources() {
		return sources;
	}

	@Override
	public synchronized void clearSources() {
		sources.clear();
	}

	@Override
	public void removeClosedSources() {
		Iterator<Entry<String, ISource<?>>> it = sources.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, ISource<?>> curEntry = it.next();
			if (!curEntry.getValue().hasOwner()) {
				it.remove();
			}
		}
	}
	
	@Override
	public void removeClosedSinks(){
		Iterator<Entry<String, ISink<?>>> it = sinks.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, ISink<?>> curEntry = it.next();
			if(!curEntry.getValue().hasOwner()){
				it.remove();
			}
		}
	}

	@Override
	public ISink<?> getSink(String sinkName) {
		return sinks.get(sinkName);
	}

	@Override
	public void putSink(String name, ISink<?> sinkPO) {
		sinks.put(name, sinkPO);
	}


}