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

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.ExecutorPermission;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.StoreException;
import de.uniol.inf.is.odysseus.core.server.usermanagement.NullUserException;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.server.util.ClearPhysicalSubscriptionsLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.RemoveOwnersGraphVisitor;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

abstract public class AbstractDataDictionary implements IDataDictionary {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDataDictionary.class);

	private List<IDataDictionaryListener> listeners = new ArrayList<IDataDictionaryListener>();
	protected IStore<String, ILogicalOperator> streamDefinitions;
	protected IStore<String, IUser> viewOrStreamFromUser;
	protected IStore<String, ILogicalOperator> viewDefinitions;
	protected IStore<String, SDFSchema> entityMap;
	protected IStore<String, IUser> entityFromUser;
	protected IStore<String, SDFDatatype> datatypes;
	protected IStore<Integer, ILogicalQuery> savedQueries;
	protected IStore<Integer, IUser> savedQueriesForUser;
	protected IStore<Integer, String> savedQueriesBuildParameterName;

	protected IStore<String, ILogicalOperator> sinkDefinitions;
	protected IStore<String, IUser> sinkFromUser;

	// --------------------------------------------------------------------------
	// Transient fields
	// --------------------------------------------------------------------------
	
	private Map<String, ISource<?>> sources = new HashMap<String, ISource<?>>();
	private Map<String, ISink<?>> sinks = new HashMap<String, ISink<?>>();
	
	private Map<String, IPhysicalOperator> operators = new HashMap<>();
	
	public AbstractDataDictionary() {
		createStores();

		initDatatypes();
	}

	protected void initDatatypes() {
		if (datatypes.entrySet().size() == 0) {
			SDFDatatype.registerDefaultTypes(this);
		}
	}

	private void createStores() {
		streamDefinitions = Preconditions.checkNotNull(
				createStreamDefinitionsStore(),
				"Store for streamDefinitions must not be null.");
		viewOrStreamFromUser = Preconditions.checkNotNull(
				createViewOrStreamFromUserStore(),
				"Store for viewOrStreamFromUser must not be null.");
		viewDefinitions = Preconditions.checkNotNull(
				createViewDefinitionsStore(),
				"Store for viewDefinitions must not be null.");
		entityMap = Preconditions.checkNotNull(createEntityMapStore(),
				"Store for entityMap must not be null.");
		entityFromUser = Preconditions.checkNotNull(
				createEntityFromUserStore(),
				"Store for entityFromUser must not be null.");
		datatypes = Preconditions.checkNotNull(createDatatypesStore(),
				"Store for datatypes must not be null.");
		savedQueries = Preconditions.checkNotNull(createSavedQueriesStore(),
				"Store for savedQueries must not be null.");
		savedQueriesForUser = Preconditions.checkNotNull(
				createSavedQueriesForUserStore(),
				"Store for savedQueriesForUser must not be null.");
		savedQueriesBuildParameterName = Preconditions.checkNotNull(
				createSavedQueriesBuildParameterNameStore(),
				"Store for savedQueriesBuildParameterName must not be null.");
		sinkDefinitions = Preconditions.checkNotNull(
				createSinkDefinitionsStore(),
				"Store for sinkDefinitions must not be null.");
		sinkFromUser = Preconditions.checkNotNull(createSinkFromUserStore(),
				"Store for sinkFromUser must not be null.");
	}

	protected abstract IStore<String, ILogicalOperator> createStreamDefinitionsStore();

	protected abstract IStore<String, IUser> createViewOrStreamFromUserStore();

	protected abstract IStore<String, ILogicalOperator> createViewDefinitionsStore();

	protected abstract IStore<String, SDFSchema> createEntityMapStore();

	protected abstract IStore<String, IUser> createEntityFromUserStore();

	protected abstract IStore<String, SDFDatatype> createDatatypesStore();

	protected abstract IStore<Integer, ILogicalQuery> createSavedQueriesStore();

	protected abstract IStore<Integer, IUser> createSavedQueriesForUserStore();

	protected abstract IStore<Integer, String> createSavedQueriesBuildParameterNameStore();

	protected abstract IStore<String, ILogicalOperator> createSinkDefinitionsStore();

	protected abstract IStore<String, IUser> createSinkFromUserStore();

	// -----------------------------------------------------------------

	@Override
	public String createUserUri(String resource, ISession caller) {
		return caller.getUser().getName() + "." + resource;
	}

	private String getUserForEntity(String entityuri) {
		IUser user = this.entityFromUser.get(entityuri);
		return user != null ? user.getName() : null;
	}

	// ----------------------------------------------------------------------------
	// Entity Management
	// ----------------------------------------------------------------------------

	@Override
	public void addEntitySchema(String uri, SDFSchema entity, ISession caller)
			throws PermissionException {
		if (hasPermission(caller, DataDictionaryPermission.ADD_ENTITY)) {
			try {
				String uriToStore = createUserUri(uri, caller);
				this.entityMap.put(uriToStore, entity);
				this.entityFromUser.put(uriToStore, caller.getUser());
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
			ret = entityMap.get(createUserUri(uri, caller));
		}
		if (ret == null) {
			throw new DataDictionaryException("no such entity: " + uri
					+ " for user " + caller.getUser().getName());
		}
		return ret;
	}

	// TODO: REMOVE Entity


	// ------------------------------------------------------------------------
	// View Management
	// ------------------------------------------------------------------------

	@Override
	@SuppressWarnings("unchecked")
	public void setView(String view, ILogicalOperator topOperator,
			ISession caller) throws DataDictionaryException {
		if (hasPermission(caller, DataDictionaryPermission.ADD_VIEW)) {
			String viewNameNormalized = createUserUri(view, caller);
			if (viewDefinitions.containsKey(viewNameNormalized)) {
				throw new DataDictionaryException("View " + viewNameNormalized
						+ " already exists. Drop First");
			}
			try {
				// Remove Owner from View
				RemoveOwnersGraphVisitor<ILogicalOperator> visitor = new RemoveOwnersGraphVisitor<ILogicalOperator>();
				@SuppressWarnings("rawtypes")
				GenericGraphWalker walker = new GenericGraphWalker();
				walker.prefixWalk(topOperator, visitor);
				synchronized (viewDefinitions) {
					this.viewDefinitions.put(viewNameNormalized, topOperator);
					viewOrStreamFromUser.put(viewNameNormalized,
							caller.getUser());
				}
				fireDataDictionaryChangedEvent();
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			// Set Type of view to name of view
			if (topOperator.getOutputSchema() != null) {
				SDFSchema oldSchema = topOperator.getOutputSchema();
				SDFSchema newSchema = new SDFSchema(viewNameNormalized,
						oldSchema.getAttributes());
				topOperator.setOutputSchema(newSchema);
			}
			fireAddEvent(viewNameNormalized, topOperator);
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has no permission to add a view.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILogicalOperator getView(String view, ISession caller) {
		String viewname;
		boolean found = false;
		if (viewDefinitions.containsKey(view)) {
			viewname = view;
			found = true;
		} else {
			viewname = createUserUri(view, caller);
			found = this.viewDefinitions.containsKey(viewname);
		}

		if (found) {
			checkAccessRights(viewname, caller, DataDictionaryPermission.READ);
			ILogicalOperator logicalPlan = this.viewDefinitions.get(viewname);
			CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(
					null);
			@SuppressWarnings("rawtypes")
			GenericGraphWalker walker = new GenericGraphWalker();
			walker.prefixWalk(logicalPlan, copyVisitor);
			return copyVisitor.getResult();
		}

		return null;
	}

	private ILogicalOperator removeView(String viewname, ISession caller) {
		ILogicalOperator op;
		try {
			synchronized (viewDefinitions) {
				op = viewDefinitions.remove(viewname);
				if (op != null) {
					viewOrStreamFromUser.remove(viewname);
				} else {
					String view = createUserUri(viewname, caller);
					op = viewDefinitions.remove(view);
					if (op != null) {
						viewOrStreamFromUser.remove(view);
					}
				}
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

	// // no restric
	// @Override
	// public boolean isView(String name) {
	// return this.viewDefinitions.containsKey(name);
	// }

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
			if (!this.entityMap.containsKey(streamname)
					&& !this.entityMap.containsKey(createUserUri(streamname,
							caller))) {
				throw new DataDictionaryException("No entity for "
						+ createUserUri(streamname, caller)
						+ ". Add entity before adding access operator.");
			}
			if (streamDefinitions.containsKey(streamname)
					|| streamDefinitions.containsKey(createUserUri(streamname,
							caller))) {
				throw new DataDictionaryException("Stream "
						+ createUserUri(streamname, caller)
						+ " already exists. Remove First");
			}
			synchronized (streamDefinitions) {
				try {
					streamDefinitions
							.put(createUserUri(streamname, caller), plan);
					viewOrStreamFromUser.put(createUserUri(streamname, caller),
							caller.getUser());
				} catch (StoreException e) {
					throw new RuntimeException(e);
				}
			}
			fireAddEvent(createUserUri(streamname, caller), plan);
			fireDataDictionaryChangedEvent();
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has no permission to set a new view.");
		}
	}

	@Override
	public AccessAO getStream(String viewname, ISession caller)
			throws DataDictionaryException {
		checkAccessRights(viewname, caller, DataDictionaryPermission.READ);

		if (!this.streamDefinitions.containsKey(viewname)) {
			if (!this.streamDefinitions.containsKey(createUserUri(viewname,
					caller))) {
				throw new DataDictionaryException("no such view: " + viewname);
			} else {
				viewname = createUserUri(viewname, caller);
			}
		}

		AccessAO ao = new AccessAO(viewname, "", null);
		ao.setName(viewname);

		SDFSchema entity = getEntitySchema(viewname, caller);
		ao.setOutputSchema(entity);

		return ao;
	}

	@Override
	public ILogicalOperator getStreamForTransformation(String name, ISession caller) {
		checkAccessRights(name, caller, DataDictionaryPermission.READ);
		// TODO: This is not very good ...
		// Do not copy Plan! Remove potential physical subscription
		ILogicalOperator iLogicalOperator = streamDefinitions.get(name);
		if (iLogicalOperator == null) {
			iLogicalOperator = streamDefinitions.get(createUserUri(name, caller));
		}
		if (iLogicalOperator != null) {
			return removePhysicalSubscriptions(iLogicalOperator);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private ILogicalOperator removePhysicalSubscriptions(ILogicalOperator stream) {
		ClearPhysicalSubscriptionsLogicalGraphVisitor<ILogicalOperator> copyVisitor = new ClearPhysicalSubscriptionsLogicalGraphVisitor<ILogicalOperator>();
		@SuppressWarnings("rawtypes")
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(stream, copyVisitor);
		return stream;
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
		synchronized (definitions) {
			for (Entry<String, ILogicalOperator> viewEntry : definitions
					.entrySet()) {
				if (hasAccessRights(viewEntry.getKey(), caller,
						DataDictionaryPermission.READ) || hasAccessRights(createUserUri(viewEntry.getKey(), caller), caller, DataDictionaryPermission.READ)) {
					sources.add(viewEntry);
				}
			}
		}
		return sources;
	}

	@Override
	public ILogicalOperator getViewOrStream(String viewname, ISession caller)
			throws DataDictionaryException {
		if (this.viewDefinitions.containsKey(viewname)) {
			return getView(viewname, caller);
		}
		if (this.viewDefinitions.containsKey(createUserUri(viewname, caller))) {
			return getView(createUserUri(viewname, caller), caller);
		}

		return getStream(viewname, caller);
	}

	@Override
	public ILogicalOperator removeViewOrStream(String viewname, ISession caller) {
		if (this.viewDefinitions.containsKey(viewname)) {
			checkAccessRights(viewname, caller,
					DataDictionaryPermission.REMOVE_VIEW);
			return removeView(viewname, caller);
		}
		if (this.viewDefinitions.containsKey(createUserUri(viewname, caller))) {
			checkAccessRights(createUserUri(viewname, caller), caller,
					DataDictionaryPermission.REMOVE_VIEW);
			return removeView(createUserUri(viewname, caller), caller);
		}

		synchronized (streamDefinitions) {
			if (this.streamDefinitions.containsKey(viewname) || streamDefinitions.containsKey(createUserUri(viewname, caller))) {
				ILogicalOperator op;
				checkAccessRights(viewname, caller,
						DataDictionaryPermission.REMOVE_STREAM);
				op = streamDefinitions.remove(viewname);
				if (op != null) {
					viewOrStreamFromUser.remove(viewname);
				} else {
					op = streamDefinitions
							.remove(createUserUri(viewname, caller));
					if (op != null) {
						viewOrStreamFromUser.remove(createUserUri(viewname,
								caller));
					}
				}
				if (op != null) {
					fireRemoveEvent(viewname, op);
				}
				return op;
			}
		}
		return null;

	}

	// no restric
	@Override
	public boolean containsViewOrStream(String viewName, ISession user) {
		return this.streamDefinitions.containsKey(viewName)
				|| this.streamDefinitions.containsKey(createUserUri(viewName,
						user))
				|| this.viewDefinitions.containsKey(viewName)
				|| this.viewDefinitions
						.containsKey(createUserUri(viewName, user));
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

	// @Override
	// public void addDatatype(String name, SDFDatatype dt, ISession caller) {
	// if (hasPermission(caller, DataDictionaryPermission.ADD_DATATYPE)) {
	// addDatatype(name, dt);
	// } else {
	// throw new PermissionException("User " + caller.getUser().getName()
	// + " has not the permission to create new data types");
	// }
	// }

	@Override
	public void addDatatype(String name, SDFDatatype dt) {
		LOG.debug("Add new Datatype " + name + " " + dt);
		if (!this.datatypes.containsKey(name.toLowerCase())) {
			this.datatypes.put(name.toLowerCase(), dt);
			fireDataDictionaryChangedEvent();
		} else {
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
			throw new DataDictionaryException("Type '" + name + "' not exists.");
		}
	}

	@Override
	public SDFDatatype getDatatype(String dtName)
			throws DataDictionaryException {
		if (this.datatypes.containsKey(dtName.toLowerCase())) {
			return this.datatypes.get(dtName.toLowerCase());
		}

		throw new DataDictionaryException("No such datatype: " + dtName);
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
	public void addSink(String sinkname, ILogicalOperator sink, ISession caller)
			throws DataDictionaryException {
		String sinkNameNormalized = createUserUri(sinkname, caller);
		if (!this.sinkDefinitions.containsKey(sinkNameNormalized)) {
			this.sinkDefinitions.put(sinkNameNormalized, sink);
			this.sinkFromUser.put(sinkNameNormalized, caller.getUser());
			fireDataDictionaryChangedEvent();
		} else {
			throw new DataDictionaryException("Sink name already used");
		}
	}

	@Override
	public ILogicalOperator getSinkForTransformation(String name, ISession caller) {
		checkAccessRights(name, caller, DataDictionaryPermission.READ);
		// TODO: This is not very good ...
		// Do not copy Plan! Remove potential physical subscription		
		ILogicalOperator iLogicalOperator = sinkDefinitions.get(name);
		if (iLogicalOperator == null) {
			iLogicalOperator = sinkDefinitions.get(createUserUri(name, caller));
		}
		if (iLogicalOperator != null) {
			return removePhysicalSubscriptions(iLogicalOperator);
		} else {
			return null;
		}
	}
	
	@Override
	public ILogicalOperator getSinkTop(String sinkname, ISession caller)
			throws DataDictionaryException {
		if (this.sinkDefinitions.containsKey(sinkname)) {
			return removePhysicalSubscriptions(sinkDefinitions.get(sinkname));
		}
		if (this.sinkDefinitions.containsKey(createUserUri(sinkname, caller))) {
			return removePhysicalSubscriptions(sinkDefinitions.get(createUserUri(
					sinkname, caller)));
		}

		throw new DataDictionaryException("No such sink defined");
	}

	@Override
	public ILogicalOperator getSinkInput(String sinkname, ISession caller)
			throws DataDictionaryException {
		ILogicalOperator sinkTop = getSinkTop(sinkname, caller);
		if (sinkTop == null) {
			sinkTop = getSinkTop(createUserUri(sinkname, caller), caller);
		}
		ILogicalOperator ret = sinkTop;
		if (ret != null) {
			while (ret.getSubscribedToSource().size() > 0) {
				ret = ret.getSubscribedToSource(0).getTarget();
			}
		}
		return ret;
	}

	@Override
	public boolean containsSink(String sinkname, ISession caller) {
		return this.sinkDefinitions.containsKey(sinkname) || this.sinkDefinitions.containsKey(createUserUri(sinkname, caller));
	}

	@Override
	public ILogicalOperator removeSink(String name, ISession caller) {
		ILogicalOperator op = this.sinkDefinitions.remove(name);
		if (op == null){
			op = this.sinkDefinitions.remove(createUserUri(name, caller));
		}
		fireDataDictionaryChangedEvent();
		return op;
	}
	
	protected IUser getUserForSink(String sinkname, ISession caller) {
		IUser user = this.sinkFromUser.get(sinkname);
		if (user == null){
			this.sinkFromUser.get(createUserUri(sinkname, caller));
		}
		return user;
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getSinks(ISession caller) {
		return this.sinkDefinitions.entrySet();
	}

	
	// ----------------------------------------------------------------------------
	// Logical Plan Management
	// ----------------------------------------------------------------------------

	@Override
	public void addQuery(ILogicalQuery q, ISession caller,
			String buildParameterName) {
		this.savedQueries.put(q.getID(), q);
		this.savedQueriesForUser.put(q.getID(), caller.getUser());
		this.savedQueriesBuildParameterName.put(q.getID(), buildParameterName);
	}

	@Override
	public ILogicalQuery getQuery(int id, ISession caller) {
		if (hasPermission(caller, ExecutorPermission.ADD_QUERY,
				ExecutorPermission.objectURI)) {
			return this.savedQueries.get(id);
		}

		return null;
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
	public String getQueryBuildConfigName(int id) {
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

	@Override
	public void addListener(IDataDictionaryListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener is null");
		}

		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(IDataDictionaryListener listener) {
		listeners.remove(listener);
	}

	private void fireAddEvent(String name, ILogicalOperator op) {
		for (IDataDictionaryListener listener : listeners) {
			try {
				listener.addedViewDefinition(this, name, op);
			} catch (Throwable ex) {
				LOG.error("Error during executing listener", ex);
			}
		}
	}

	private void fireRemoveEvent(String name, ILogicalOperator op) {
		for (IDataDictionaryListener listener : listeners) {
			try {
				listener.removedViewDefinition(this, name, op);
			} catch (Throwable ex) {
				LOG.error("Error during executing listener", ex);
			}
		}
	}


	private void fireDataDictionaryChangedEvent() {
		for (IDataDictionaryListener listener : listeners) {
			listener.dataDictionaryChanged(this);
		}
	}

	// -------------------------------------------------------------------------------------
	// Hilfsmethoden f�r Rechtemanagement
	// -------------------------------------------------------------------------------------

	/**
	 * return true if the given user has permission to access the given view in
	 * a certain way (action).
	 * 
	 * @param uri --> Fully qualified with user name --> user.name
	 * @param caller
	 * @param action
	 * @return boolean
	 */
	private boolean hasAccessRights(String resource, ISession caller,
			DataDictionaryPermission action) {
		if (hasPermission(caller, action, resource)
				// is owner
				|| isCreatorOfView(caller.getUser().getName(), resource)
				|| isCreatorOfObject(caller.getUser().getName(), resource)
				|| hasSuperAction(action, caller)) {
			return true;
		}
		return false;
	}

	private void checkAccessRights(String resource, ISession caller,
			DataDictionaryPermission action) {
		if (!hasAccessRights(resource, caller, action)
				&& !hasAccessRights(createUserUri(resource, caller), caller,
						action)) {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has not the permission '" + action + "' on resource '"
					+ resource);
		}
	}

	private boolean hasPermission(ISession caller,
			IPermission permission, String objectURI) {
		return UserManagement.getUsermanagement().hasPermission(caller,
				permission, objectURI)
				|| UserManagement.getUsermanagement().hasPermission(caller,
						permission, createUserUri(objectURI, caller));
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
	private boolean isCreatorOfObject(String username, String objecturi) {
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
		}

		throw new NullUserException("Username is empty.");
	}

	/**
	 * returns true if username euqlas creator of the given view
	 * 
	 * @param username
	 * @param viewname
	 * @return
	 */
	private boolean isCreatorOfView(String username, String viewname) {
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
		}

		throw new NullUserException("Username is empty.");
	}

	// ------------------------------------------------------------------------------
	// Methods for physical sinks and sources (moved from WrapperPlanFactory)
	// ------------------------------------------------------------------------------


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
		while (it.hasNext()) {
			Entry<String, ISource<?>> curEntry = it.next();
			if (!curEntry.getValue().hasOwner()) {
				curEntry.getValue().unsubscribeFromAllSinks();
				it.remove();

			}
		}
	}

	@Override
	public void removeClosedSinks() {
		Iterator<Entry<String, ISink<?>>> it = sinks.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, ISink<?>> curEntry = it.next();
			if (!curEntry.getValue().hasOwner()) {
				it.remove();
			}
		}
	}

	@Override
	public ISink<?> getSinkplan(String sinkName) {
		return sinks.get(sinkName);
	}

	@Override
	public void putSinkplan(String name, ISink<?> sinkPO) {
		sinks.put(name, sinkPO);
	}
	
	// --------------------------------------------------------
	// Operator Management
	// ---------------------------------------------------------
	
	@Override
	public void setOperator(String id, IPhysicalOperator physical) {
		operators.put(id, physical);
	}
	
	@Override
	public boolean containsOperator(String id) {
		return operators.containsKey(id);
	}
	
	@Override
	public void removeOperator(String id) {
		operators.remove(id);
	}
	
	@Override
	public IPhysicalOperator getOperator(String id) {
		// FIXME: Potential security risk! We can add our source to
		// other queries --> Need a concept!!
		return operators.get(id);
	}

}