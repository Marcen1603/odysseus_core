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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.ExecutorPermission;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.StoreException;
import de.uniol.inf.is.odysseus.core.server.usermanagement.NullUserException;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.server.util.ClearPhysicalSubscriptionsLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.RemoveIdLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.RemoveOwnersGraphVisitor;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

abstract public class AbstractDataDictionary implements IDataDictionary,
		IDataDictionaryWritable {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractDataDictionary.class);

	private final List<IDataDictionaryListener> listeners = Lists
			.newArrayList();
	private IStore<Resource, ILogicalOperator> streamDefinitions;
	// private IStore<Resource, IUser> viewOrStreamFromUser;
	private IStore<Resource, ILogicalOperator> viewDefinitions;
	private IStore<Resource, IUser> entityFromUser;
	private IStore<Resource, HashMap<String, ArrayList<Resource>>> entityUsedBy;
	private IStore<String, SDFDatatype> datatypes;
	private IStore<Integer, ILogicalQuery> savedQueries;
	private IStore<Integer, IUser> savedQueriesForUser;

	private IStore<Integer, String> savedQueriesBuildParameterName;

	private IStore<Resource, ILogicalOperator> sinkDefinitions;
	// private IStore<Resource, IUser> sinkFromUser;

	private IStore<Resource, StoredProcedure> storedProcedures;
	private IStore<Resource, IUser> storedProceduresFromUser;

	// --------------------------------------------------------------------------
	// Transient fields
	// --------------------------------------------------------------------------

	transient private final Map<Resource, ISource<?>> accessPlans = Maps
			.newHashMap();
	transient private final Map<Resource, ISink<?>> sinks = Maps.newHashMap();
	transient private final Map<Resource, ISource<?>> accessAOs = Maps
			.newHashMap();
	transient private final Map<Resource, IPhysicalOperator> operators = Maps
			.newHashMap();

	protected ITenant tenant;

	private enum EntityType {
		STREAM, VIEW, QUERY
	};

	public AbstractDataDictionary(ITenant t) {
		if (t != null) {
			this.tenant = t;
			init();
		}
	}

	private void init() {
		streamDefinitions = Preconditions.checkNotNull(
				createStreamDefinitionsStore(),
				"Store for streamDefinitions must not be null.");
		// viewOrStreamFromUser = Preconditions.checkNotNull(
		// createViewOrStreamFromUserStore(),
		// "Store for viewOrStreamFromUser must not be null.");
		viewDefinitions = Preconditions.checkNotNull(
				createViewDefinitionsStore(),
				"Store for viewDefinitions must not be null.");
		entityFromUser = Preconditions.checkNotNull(
				createEntityFromUserStore(),
				"Store for entityFromUser must not be null.");
		entityUsedBy = Preconditions.checkNotNull(createEntityUsedByStore(),
				"Store for entityUsedBy must not be null.");
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
		// sinkFromUser = Preconditions.checkNotNull(createSinkFromUserStore(),
		// "Store for sinkFromUser must not be null.");

		storedProcedures = Preconditions.checkNotNull(
				createStoredProceduresStore(),
				"Store for storedProcedures must not be null.");
		storedProceduresFromUser = Preconditions.checkNotNull(
				createStoredProceduresFromUserStore(),
				"Store for storedProceduresFromUser must not be null.");
	}

	// Methods that must be overwritten to create stores
	protected abstract IStore<Resource, ILogicalOperator> createStreamDefinitionsStore();

	protected abstract IStore<Resource, IUser> createViewOrStreamFromUserStore();

	protected abstract IStore<Resource, ILogicalOperator> createViewDefinitionsStore();

	protected abstract IStore<Resource, HashMap<String, ArrayList<Resource>>> createEntityUsedByStore();

	protected abstract IStore<Resource, IUser> createEntityFromUserStore();

	protected abstract IStore<String, SDFDatatype> createDatatypesStore();

	protected abstract IStore<Integer, ILogicalQuery> createSavedQueriesStore();

	protected abstract IStore<Integer, IUser> createSavedQueriesForUserStore();

	protected abstract IStore<Integer, String> createSavedQueriesBuildParameterNameStore();

	protected abstract IStore<Resource, ILogicalOperator> createSinkDefinitionsStore();

	protected abstract IStore<Resource, IUser> createSinkFromUserStore();

	protected abstract IStore<Resource, StoredProcedure> createStoredProceduresStore();

	protected abstract IStore<Resource, IUser> createStoredProceduresFromUserStore();

	// -----------------------------------------------------------------
	// Help method
	// -----------------------------------------------------------------

	/**
	 * Creates a new Resource object
	 * 
	 * @param resource
	 * @param caller
	 * @return
	 */
	private Resource createResource(String resource, ISession caller) {
		return new Resource(caller.getUser(), resource);
	}

	/**
	 * Retrieves a user name for an entity
	 * 
	 * @param entityuri
	 * @return
	 */
	private String getUserForEntity(Resource entityuri) {
		IUser user = this.entityFromUser.get(entityuri);
		return user != null ? user.getName() : null;
	}

	/**
	 * This method return the full name for a registered source if it exists
	 * (e.g. System.nexmark:bid
	 * 
	 * @param resourceName
	 *            The name of the resource to be search (e.g. System.nexmark:bid
	 *            or nexmark:bid)
	 * @param caller
	 *            Who tries to retrieve the information. If resource has no
	 *            user, this users name will be used
	 * @param store
	 *            In which store to look for the resource
	 * @return
	 */
	private Resource getResourceName(String resourceName, ISession caller,
			IStore<Resource, ?> store) {
		Resource name = null;
		if (resourceName.contains(".")) {
			name = new Resource(resourceName);
		} else {
			if (name == null || !store.containsKey(name)) {
				name = createResource(resourceName, caller);
			}
		}
		if (store.containsKey(name)) {
			return name;
		} else {
			return null;
		}
	}

	// ----------------------------------------------------------------------------
	// Entity Management
	// ----------------------------------------------------------------------------

	/**
	 * 
	 * @param plan
	 * @param identifier
	 * @param entityType
	 * @param caller
	 */
	@SuppressWarnings("unchecked")
	private void addEntityForPlan(ILogicalOperator plan, Resource identifier,
			EntityType entityType, ISession caller) {
		String type = entityType.toString();
		CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<ILogicalOperator>(
				AbstractAccessAO.class);
		@SuppressWarnings("rawtypes")
		GenericGraphWalker collectWalker = new GenericGraphWalker();
		collectWalker.prefixWalk(plan, collVisitor);
		for (ILogicalOperator acc : collVisitor.getResult()) {
			String name = acc.getName();
			Resource uri = createResource(name, caller);
			if (!entityType.equals(EntityType.QUERY)) {
				// in a stream or view definition, the entity may not be defined
				// before!
				createEntity(uri, caller, type, identifier);
			} else {
				// in a query we either reuse the entity or it may be new
				// defined.

				// so, if there is already an entity defined, we remember us as
				// new user for the entity
				if (this.entityFromUser.containsKey(uri)) {
					if (!this.entityUsedBy.get(uri).containsKey(type)) {
						ArrayList<Resource> list = new ArrayList<>();
						this.entityUsedBy.get(uri).put(type, list);
					}
					this.entityUsedBy.get(uri).get(type).add(identifier);
				} else {
					// else, we want to create a new entity
					createEntity(uri, caller, type, identifier);
				}
			}
		}
		fireDataDictionaryChangedEvent();
	}

	/**
	 * 
	 * @param uri
	 * @param caller
	 * @param type
	 * @param identifier
	 */
	private void createEntity(Resource uri, ISession caller, String type,
			Resource identifier) {
		if (this.entityFromUser.containsKey(uri)) {
			throw new IllegalArgumentException(
					"There is already a view or stream with sourcename " + uri
							+ ".");
		}
		this.entityFromUser.put(uri, caller.getUser());
		this.entityUsedBy.put(uri, new HashMap<String, ArrayList<Resource>>());
		ArrayList<Resource> list = new ArrayList<>();
		list.add(identifier);
		this.entityUsedBy.get(uri).put(type, list);
	}

	/**
	 * 
	 * @param plan
	 * @param identifier
	 * @param entityType
	 * @param caller
	 */
	@SuppressWarnings("unchecked")
	private void removeEntityForPlan(ILogicalOperator plan,
			Resource identifier, EntityType entityType, ISession caller) {
		String type = entityType.toString();
		CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<ILogicalOperator>(
				AbstractAccessAO.class);
		@SuppressWarnings("rawtypes")
		GenericGraphWalker collectWalker = new GenericGraphWalker();
		collectWalker.prefixWalk(plan, collVisitor);
		for (ILogicalOperator acc : collVisitor.getResult()) {
			String name = acc.getName();
			Resource uri = createResource(name, caller);
			if (this.entityUsedBy.containsKey(uri)) {
				if (this.entityUsedBy.get(uri).containsKey(type)) {
					this.entityUsedBy.get(uri).get(type).remove(identifier);
					if (this.entityUsedBy.get(uri).get(type).isEmpty()) {
						this.entityUsedBy.get(uri).remove(type);
					}
				}
			}
		}
		// System.out.println(this.entityFromUser);
		// check, whether entity is not used anymore, so we totally remove it
		Iterator<Entry<Resource, HashMap<String, ArrayList<Resource>>>> iterEntity = this.entityUsedBy
				.entrySet().iterator();
		while (iterEntity.hasNext()) {
			Entry<Resource, HashMap<String, ArrayList<Resource>>> e = iterEntity
					.next();
			boolean empty = true;
			for (Entry<String, ArrayList<Resource>> typeEntry : e.getValue()
					.entrySet()) {
				if (!typeEntry.getValue().isEmpty()) {
					empty = false;
				}
			}
			if (empty) {
				synchronized (this.entityFromUser) {
					this.entityFromUser.remove(e.getKey());
				}
				iterEntity.remove();
			}
		}
		// System.out.println(this.entityFromUser);

		fireDataDictionaryChangedEvent();

	}

	// ------------------------------------------------------------------------
	// View Management
	// ------------------------------------------------------------------------

	@Override
	@SuppressWarnings("unchecked")
	public void setView(String view, ILogicalOperator topOperator,
			ISession caller) throws DataDictionaryException {
		if (view.contains(".")) {
			throw new IllegalArgumentException(
					"A '.' is not allowed in view names!");
		}

		if (hasPermission(caller, DataDictionaryPermission.ADD_VIEW)) {

			Resource viewname = createResource(view, caller);
			if (viewDefinitions.containsKey(viewname)) {
				throw new DataDictionaryException("View " + viewname
						+ " already exists. Drop First");
			}
			try {
				// Remove Owner from View
				RemoveOwnersGraphVisitor<ILogicalOperator> visitor = new RemoveOwnersGraphVisitor<ILogicalOperator>();
				@SuppressWarnings("rawtypes")
				GenericGraphWalker walker = new GenericGraphWalker();
				walker.prefixWalk(topOperator, visitor);
				synchronized (viewDefinitions) {
					this.viewDefinitions.put(viewname, topOperator);
					// viewOrStreamFromUser.put(viewname, caller.getUser());
					addEntityForPlan(topOperator, viewname, EntityType.VIEW,
							caller);
				}
				fireDataDictionaryChangedEvent();
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			// Set Type of view to name of view
			// MG: REMOVED
			// if (topOperator.getOutputSchema() != null) {
			// SDFSchema oldSchema = topOperator.getOutputSchema();
			// SDFSchema newSchema = SDFSchema.changeSourceName(oldSchema,
			// viewname.getShortString(caller.getUser().getName()));
			// topOperator.setOutputSchema(newSchema);
			// }
			fireViewAddEvent(viewname, topOperator);
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has no permission to add a view.");
		}
	}

	@Override
	public ILogicalOperator getView(String view, ISession caller) {
		Resource viewname = getResourceName(view, caller, viewDefinitions);
		return getView(viewname, caller);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILogicalOperator getView(Resource view, ISession caller) {
		if (view != null) {
			checkAccessRights(view, caller, DataDictionaryPermission.READ);
			ILogicalOperator logicalPlan = this.viewDefinitions.get(view);
			CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(
					(IOperatorOwner) null);
			@SuppressWarnings("rawtypes")
			GenericGraphWalker walker = new GenericGraphWalker();
			walker.prefixWalk(logicalPlan, copyVisitor);
			return copyVisitor.getResult();
		}

		return null;
	}

	private ILogicalOperator removeView(String view, ISession caller) {
		Resource viewname = getResourceName(view, caller, viewDefinitions);
		return removeView(viewname, caller);
	}

	@SuppressWarnings("unchecked")
	private ILogicalOperator removeView(Resource viewname, ISession caller) {
		ILogicalOperator op = null;
		if (viewname != null) {
			try {
				synchronized (viewDefinitions) {
					op = viewDefinitions.remove(viewname);
					// viewOrStreamFromUser.remove(viewname);
				}
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			if (op != null) {
				// Remove registered ids
				RemoveIdLogicalGraphVisitor<ILogicalOperator> visitor = new RemoveIdLogicalGraphVisitor<ILogicalOperator>(
						this, caller);
				@SuppressWarnings("rawtypes")
				GenericGraphWalker walker = new GenericGraphWalker();
				walker.prefixWalk(op, visitor);
				removeEntityForPlan(op, viewname, EntityType.VIEW, caller);
				fireViewRemoveEvent(viewname, op);
				fireDataDictionaryChangedEvent();
			}
		}
		return op;
	}

	@Override
	public Set<Entry<Resource, ILogicalOperator>> getViews(ISession caller) {
		return getDefinitions(caller, viewDefinitions);
	}

	// ------------------------------------------------------------------------
	// Stream Management
	// ------------------------------------------------------------------------

	@Override
	public void setStream(String strname, ILogicalOperator plan, ISession caller)
			throws DataDictionaryException {
		if (strname.contains(".")) {
			throw new IllegalArgumentException(
					"A '.' is not allowed in stream names!");
		}
		if (hasPermission(caller, DataDictionaryPermission.ADD_STREAM)) {

			Resource streamname = createResource(strname, caller);

			if (streamDefinitions.containsKey(streamname)) {
				throw new DataDictionaryException("Stream " + streamname
						+ " already exists. Remove First");
			}
			synchronized (streamDefinitions) {
				try {
					streamDefinitions.put(streamname, plan);
					// viewOrStreamFromUser.put(streamname, caller.getUser());
					addEntityForPlan(plan, streamname, EntityType.STREAM,
							caller);
				} catch (StoreException e) {
					throw new RuntimeException(e);
				}
			}
			fireViewAddEvent(streamname, plan);
			fireDataDictionaryChangedEvent();
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has no permission to set a new view.");
		}
	}

	@Override
	public StreamAO getStream(String stream, ISession caller)
			throws DataDictionaryException {
		StreamAO ao = null;
		Resource streamname = getResourceName(stream, caller, streamDefinitions);

		if (streamname != null) {

			checkAccessRights(streamname, caller, DataDictionaryPermission.READ);

			ao = new StreamAO(streamname);
			ArrayList<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			SDFSchema outSchema = this.streamDefinitions.get(streamname)
					.getOutputSchema();
			for (SDFAttribute old : this.streamDefinitions.get(streamname)
					.getOutputSchema()) {
				attributes.add(new SDFAttribute(streamname.toString(), old
						.getAttributeName(), old));
			}
			SDFSchema schema = SDFSchema
					.changeSourceName(outSchema, streamname
							.getShortString(caller.getUser().getName()), true);
			ao.setOutputSchema(schema);
		}
		return ao;
	}

	private ILogicalOperator getResourceForTransformation(String resourceName,
			ISession caller, IStore<Resource, ILogicalOperator> store) {
		ILogicalOperator op = null;
		Resource name = getResourceName(resourceName, caller, store);
		if (name != null) {
			checkAccessRights(name, caller, DataDictionaryPermission.READ);
			op = removePhysicalSubscriptions(store.get(name));
		}
		return op;
	}

	@Override
	public ILogicalOperator getStreamForTransformation(String streamName,
			ISession caller) {
		return getResourceForTransformation(streamName, caller,
				streamDefinitions);
	}

	@Override
	public ILogicalOperator getStreamForTransformation(Resource name,
			ISession caller) {
		return streamDefinitions.get(name);
	}

	private ILogicalOperator removeStream(String streamname, ISession caller) {
		Resource stream = getResourceName(streamname, caller, streamDefinitions);
		return removeStream(stream, caller);

	}

	@SuppressWarnings("unchecked")
	private ILogicalOperator removeStream(Resource stream, ISession caller) {
		LOG.trace("Try to remove Stream " + stream + " for " + caller);
		if (LOG.isTraceEnabled()) {
			StringBuffer buffer = new StringBuffer();
			streamDefinitions.dumpTo(buffer);
			// buffer.append("--------------------------------------");
			// viewOrStreamFromUser.dumpTo(buffer);
			LOG.trace(buffer.toString());

		}
		ILogicalOperator op = null;
		if (stream != null) {
			checkAccessRights(stream, caller,
					DataDictionaryPermission.REMOVE_STREAM);
			op = streamDefinitions.remove(stream);
			// viewOrStreamFromUser.remove(stream);

			// Remove plan from wrapper plan factory
			removeAccessPlan(stream);
			// Remove registered ids
			RemoveIdLogicalGraphVisitor<ILogicalOperator> visitor = new RemoveIdLogicalGraphVisitor<ILogicalOperator>(
					this, caller);
			@SuppressWarnings("rawtypes")
			GenericGraphWalker walker = new GenericGraphWalker();
			walker.prefixWalk(op, visitor);
			removeEntityForPlan(op, stream, EntityType.STREAM, caller);
			fireViewRemoveEvent(stream, op);
		}
		return op;
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
	public Set<Entry<Resource, ILogicalOperator>> getStreams(ISession caller) {
		return getDefinitions(caller, streamDefinitions);
	}

	// ------------------------------------------------------------------------
	// Access View or Streams
	// ------------------------------------------------------------------------

	@Override
	public Set<Entry<Resource, ILogicalOperator>> getStreamsAndViews(
			ISession caller) {
		Set<Entry<Resource, ILogicalOperator>> sources = new HashSet<Entry<Resource, ILogicalOperator>>();

		sources.addAll(getStreams(caller));
		sources.addAll(getViews(caller));

		return sources;
	}

	private Set<Entry<Resource, ILogicalOperator>> getDefinitions(
			ISession caller, IStore<Resource, ILogicalOperator> definitions) {
		Set<Entry<Resource, ILogicalOperator>> sources = new HashSet<Entry<Resource, ILogicalOperator>>();
		synchronized (definitions) {
			for (Entry<Resource, ILogicalOperator> viewEntry : definitions
					.entrySet()) {
				if (hasAccessRights(viewEntry.getKey(), caller,
						DataDictionaryPermission.READ)) {
					sources.add(viewEntry);
				}
			}
		}
		return sources;
	}

	@Override
	public ILogicalOperator getViewOrStream(String viewname, ISession caller)
			throws DataDictionaryException {

		ILogicalOperator op = getView(viewname, caller);
		if (op == null) {
			op = getStream(viewname, caller);
		}

		if (op == null) {
			throw new DataDictionaryException("Could not find view or stream "
					+ viewname);
		}

		return op;
	}

	@Override
	public Resource getViewOrStreamName(String sourceName, ISession caller) {
		Resource viewname = getResourceName(sourceName, caller, viewDefinitions);

		if (viewname == null) {
			viewname = getResourceName(sourceName, caller, streamDefinitions);
		}

		return viewname;
	}

	@Override
	public Resource getResource(String resourceName, ISession caller) {
		Resource resource = getViewOrStreamName(resourceName, caller);
		if (resource == null) {
			resource = getResourceName(resourceName, caller, sinkDefinitions);
		}
		return resource;
	}

	@Override
	public ILogicalOperator removeViewOrStream(String viewname, ISession caller) {
		ILogicalOperator ret = removeView(viewname, caller);
		// no view found
		if (ret == null) {
			ret = removeStream(viewname, caller);
		}
		if (ret == null) {
			LOG.warn("Trying to remove not registered view/stream " + viewname);
		}
		return ret;
	}

	@Override
	public ILogicalOperator removeViewOrStream(Resource viewname,
			ISession caller) {
		ILogicalOperator ret = removeView(viewname, caller);
		if (ret == null) {
			ret = removeStream(viewname, caller);
		}
		return ret;
	}

	// no restric
	@Override
	public boolean containsViewOrStream(String viewName, ISession user) {
		Resource name = getResourceName(viewName, user, viewDefinitions);
		if (name == null) {
			name = getResourceName(viewName, user, streamDefinitions);
		}
		return name != null;
	}

	@Override
	public boolean containsViewOrStream(Resource viewName, ISession user) {
		return viewDefinitions.containsKey(viewName)
				|| streamDefinitions.containsKey(viewName);
	}

	private String getCreator(Resource resource) {
		return resource.getUser();

	}

	// ----------------------------------------------------------------------------
	// Datatype Management
	// ----------------------------------------------------------------------------

	private void addDatatype(String name, SDFDatatype dt) {
		LOG.trace("Add new Datatype " + name + " " + dt);
		if (!this.datatypes.containsKey(name.toLowerCase())) {
			this.datatypes.put(name.toLowerCase(), dt);
			fireDataDictionaryChangedEvent();
		} else {
			throw new DataDictionaryException("Type '" + name
					+ "' already exists.");
		}
	}

	@Override
	public void addDatatype(SDFDatatype dt) {
		addDatatype(dt.getURI(), dt);
	}

	@Override
	public void removeDatatype(SDFDatatype dt) throws DataDictionaryException {
		removeDatatype(dt.getURI());
	}

	private void removeDatatype(String name) throws DataDictionaryException {
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
	public SDFDatatype getDatatype(SDFDatatype type, List<SDFDatatype> subTypes) {
		String key = type + "_" + subTypes;
		SDFDatatype datatype = this.datatypes.get(key);
		if (datatype == null) {
			if (type == SDFDatatype.LIST) {
				if (subTypes.size() != 1) {
					throw new IllegalArgumentException("List must have a type");
				}
				SDFDatatype subType = subTypes.get(0);
				datatype = new SDFDatatype("List", KindOfDatatype.LIST, subType);
				datatypes.put(key, datatype);
			} else {
				final KindOfDatatype kind;
				if (type == SDFDatatype.TUPLE || type == SDFDatatype.NTUPLE) {
					kind = KindOfDatatype.TUPLE;
				} else {
					kind = KindOfDatatype.GENERIC;
				}
				if (subTypes.size() != 1) {
					// Create schema from list of types
					List<SDFAttribute> attributes = new LinkedList<SDFAttribute>();
					for (int i = 0; i < subTypes.size(); i++) {
						attributes.add(new SDFAttribute("", "_" + i, subTypes
								.get(0), (SDFUnit) null,
								(Collection<SDFConstraint>) null));
					}
					datatype = new SDFDatatype(type.getURI(), kind,
							new SDFSchema("", Tuple.class, attributes));
				} else {
					datatype = new SDFDatatype(type.getURI(), kind,
							subTypes.get(0));
				}
			}
		}
		return datatype;
	}

	@Override
	public Set<String> getDatatypeNames() {
		return this.datatypes.keySet();
	}

	@Override
	public Set<SDFDatatype> getDatatypes() {
		return new HashSet<SDFDatatype>(this.datatypes.values());
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
		addSink(createResource(sinkname, caller), sink, caller);
	}

	@Override
	public void addSink(Resource sinkname, ILogicalOperator sink,
			ISession caller) throws DataDictionaryException {
		if (sinkname.getResourceName().contains(".")) {
			throw new IllegalArgumentException(
					"A '.' is not allowed in sink names!");
		}

		if (!this.sinkDefinitions.containsKey(sinkname)) {
			this.sinkDefinitions.put(sinkname, sink);
			// this.sinkFromUser.put(sinkname, caller.getUser());
			fireDataDictionaryChangedEvent();
		} else {
			throw new DataDictionaryException("Sink name already used");
		}

	}

	@Override
	public ILogicalOperator getSinkForTransformation(Resource name,
			ISession caller) {
		return sinkDefinitions.get(name);
	}

	@Override
	public ILogicalOperator getSinkForTransformation(String name,
			ISession caller) {
		return getResourceForTransformation(name, caller, sinkDefinitions);
	}

	@Override
	public ILogicalOperator getSinkTop(String sinkname, ISession caller)
			throws DataDictionaryException {
		Resource sink = getResourceName(sinkname, caller, sinkDefinitions);
		if (sink != null) {
			return removePhysicalSubscriptions(sinkDefinitions.get(sink));
		} else {
			throw new DataDictionaryException("No such sink defined");
		}
	}

	@Override
	public ILogicalOperator getSinkInput(String sinkname, ISession caller)
			throws DataDictionaryException {
		ILogicalOperator sinkTop = getSinkTop(sinkname, caller);
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
		return getResourceName(sinkname, caller, sinkDefinitions) != null;
	}

	@Override
	public boolean containsSink(Resource sinkname, ISession caller) {
		return sinkDefinitions.containsKey(sinkname);
	}

	@Override
	public ILogicalOperator removeSink(String name, ISession caller) {
		return removeSink(getResourceName(name, caller, sinkDefinitions),
				caller);
	}

	@Override
	public ILogicalOperator removeSink(Resource name, ISession caller) {
		ILogicalOperator op = this.sinkDefinitions.remove(name);
		fireDataDictionaryChangedEvent();
		return op;
	}

	// protected IUser getUserForSink(String name, ISession caller) {
	// Resource sink = getResourceName(name, caller, sinkDefinitions);
	// IUser user = this.sinkFromUser.get(sink);
	// return user;
	// }

	@Override
	public Set<Entry<Resource, ILogicalOperator>> getSinks(ISession caller) {
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
		addEntityForPlan(q.getLogicalPlan(),
				createResource(Integer.toString(q.getID()), caller),
				EntityType.QUERY, caller);
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
		List<ILogicalQuery> queries = Lists.newArrayList();
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
		removeEntityForPlan(q.getLogicalPlan(),
				createResource(Integer.toString(q.getID()), caller),
				EntityType.QUERY, caller);
	}

	// ----------------------------------------------------------------------------
	// Listener Management
	// ----------------------------------------------------------------------------

	@Override
	public void addListener(IDataDictionaryListener listener) {
		Preconditions.checkNotNull(listener,
				"Listener to add to data dictionary must not be null!");

		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	@Override
	public void removeListener(IDataDictionaryListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	protected final void fireViewAddEvent(Resource name, ILogicalOperator op) {
		synchronized (listeners) {
			for (IDataDictionaryListener listener : listeners) {
				try {
					listener.addedViewDefinition(this, name.toString(), op);
				} catch (Throwable ex) {
					LOG.error("Error during executing listener", ex);
				}
			}
		}
	}

	protected final void fireViewRemoveEvent(Resource name, ILogicalOperator op) {
		synchronized (listeners) {
			for (IDataDictionaryListener listener : listeners) {
				try {
					listener.removedViewDefinition(this, name.toString(), op);
				} catch (Throwable ex) {
					LOG.error("Error during executing listener", ex);
				}
			}
		}
	}

	private void fireDataDictionaryChangedEvent() {
		synchronized (listeners) {
			for (IDataDictionaryListener listener : listeners) {
				try {
					LOG.trace(
							"Fire data dictionary changed event listener : {}",
							listener);

					listener.dataDictionaryChanged(this);
				} catch (Throwable throwable) {
					LOG.error("Exception in listener of data dictionary",
							throwable);
				}
			}
		}
	}

	// -------------------------------------------------------------------------------------
	// Hilfsmethoden f�r Rechtemanagement
	// -------------------------------------------------------------------------------------

	/**
	 * return true if the given user has permission to access the given view in
	 * a certain way (action).
	 * 
	 * @param uri
	 *            --> Fully qualified with user name --> user.name
	 * @param caller
	 * @param action
	 * @return boolean
	 */
	private boolean hasAccessRights(Resource resource, ISession caller,
			DataDictionaryPermission action) {
		if (hasPermission(caller, action, resource)
				|| isCreatorOfView(caller.getUser().getName(), resource)
				|| isCreatorOfObject(caller.getUser().getName(), resource)
				|| isCreatorOfStoredProcedure(caller, resource)
				|| hasSuperAction(action, caller)) {
			return true;
		}
		return false;
	}

	private boolean isCreatorOfStoredProcedure(ISession caller,
			Resource resource) {
		if (this.storedProceduresFromUser.containsKey(resource)) {
			return this.storedProceduresFromUser.get(resource).equals(
					caller.getUser());
		}
		return false;
	}

	private void checkAccessRights(Resource resource, ISession caller,
			DataDictionaryPermission action) {
		if (!hasAccessRights(resource, caller, action)) {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has not the permission '" + action + "' on resource '"
					+ resource);
		}
	}

	private boolean hasPermission(ISession caller, IPermission permission,
			Resource objectURI) {
		return UserManagementProvider.getUsermanagement(true).hasPermission(
				caller, permission, objectURI.toString());
	}

	private boolean hasPermission(ISession caller, IPermission permission) {
		return UserManagementProvider.getUsermanagement(true).hasPermission(
				caller, permission, DataDictionaryPermission.objectURI);
	}

	private boolean hasPermission(ISession caller, IPermission permission,
			String uri) {
		return UserManagementProvider.getUsermanagement(true).hasPermission(
				caller, permission, uri);
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
	private boolean isCreatorOfObject(String username, Resource objecturi) {
		if (objecturi == null) {
			return false;
		}
		if (username != null && !username.isEmpty()) {
			String user = getUserForEntity(objecturi);
			if (user == null || user.isEmpty()) {
				String userObj = getCreator(objecturi);

				user = userObj != null ? userObj : null;
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
	private boolean isCreatorOfView(String username, Resource viewname) {
		if (viewname == null) {
			return false;
		}
		if (!username.isEmpty()) {
			String user = getCreator(viewname);
			if (user != null) {
				if (user.equals(username)) {
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
	public synchronized ISource<?> getAccessPlan(Resource uri) {
		ISource<?> po = accessPlans.get(uri);
		return po;
	}

	@Override
	public synchronized void putAccessPlan(Resource uri, ISource<?> s) {
		if (accessPlans.containsKey(uri)) {
			throw new IllegalArgumentException("Sourcename " + uri
					+ " already registred! Remove first");
		}
		accessPlans.put(uri, s);
	}

	@Override
	public void putAccessAO(Resource name, ISource<?> access) {
		if (accessAOs.containsKey(name)) {
			throw new IllegalArgumentException("AccessAO " + name
					+ " already registred! Remove first");
		}
		accessAOs.put(name, access);
	}

	@Override
	public ISource<?> getAccessAO(Resource name) {
		return accessAOs.get(name);
	}

	@Override
	public synchronized void removeAccessPlan(Resource uri) {
		accessPlans.remove(uri);
	}

	@Override
	public synchronized Map<Resource, ISource<?>> getSources() {
		return accessPlans;
	}

	@Override
	public synchronized void clearSources() {
		accessPlans.clear();
	}

	@Override
	public void removeClosedSources() {
		Iterator<Entry<Resource, ISource<?>>> it = accessPlans.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<Resource, ISource<?>> curEntry = it.next();
			if (!curEntry.getValue().hasOwner()) {
				curEntry.getValue().unsubscribeFromAllSinks();
				it.remove();

			}
		}
		it = accessAOs.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Resource, ISource<?>> curEntry = it.next();
			if (!curEntry.getValue().hasOwner()) {
				curEntry.getValue().unsubscribeFromAllSinks();
				it.remove();
			}
		}

	}

	@Override
	public void removeClosedSinks() {
		Iterator<Entry<Resource, ISink<?>>> it = sinks.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Resource, ISink<?>> curEntry = it.next();
			if (!curEntry.getValue().hasOwner()) {
				it.remove();
			}
		}
	}

	@Override
	public ISink<?> getSinkplan(Resource sinkName) {
		return sinks.get(sinkName);
	}

	@Override
	public void putSinkplan(Resource name, ISink<?> sinkPO) {
		sinks.put(name, sinkPO);
	}

	// --------------------------------------------------------
	// Operator Management
	// ---------------------------------------------------------

	@Override
	public void setOperator(Resource id, IPhysicalOperator physical) {
		operators.put(id, physical);
	}

	@Override
	public boolean containsOperator(Resource id) {
		return operators.containsKey(id);
	}

	@Override
	public void removeOperator(Resource id) {
		operators.remove(id);
	}

	@Override
	public IPhysicalOperator getOperator(Resource id, ISession caller) {
		if (id.getUser() == caller.getUser().getName()) {
			return operators.get(id);
		} else {
			return null;
		}

	}

	// -------------------------------------------------------------------------
	// Stored Procedure Management
	// -------------------------------------------------------------------------
	@Override
	public void addStoredProcedure(StoredProcedure procedure, ISession caller) {
		if (hasPermission(caller, DataDictionaryPermission.ADD_STORED_PROCEDURE)) {
			Resource nameNormalized = createResource(procedure.getName(),
					caller);
			if (!this.storedProcedures.containsKey(nameNormalized)) {
				this.storedProcedures.put(nameNormalized, procedure);
				this.storedProceduresFromUser.put(nameNormalized,
						caller.getUser());
				fireDataDictionaryChangedEvent();
			} else {
				throw new DataDictionaryException(
						"Stored procedure with name \"" + nameNormalized
								+ "\" already used");
			}
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ "has no permission to add procedures.");
		}
	}

	@Override
	public void removeStoredProcedure(String procedureName, ISession caller) {
		if (hasPermission(caller,
				DataDictionaryPermission.REMOVE_STORED_PROCEDURE)) {
			Resource procedure = getResourceName(procedureName, caller,
					storedProcedures);
			if (procedure != null) {
				this.storedProcedures.remove(procedure);
				this.storedProceduresFromUser.remove(procedure);
				fireDataDictionaryChangedEvent();
			} else {
				throw new DataDictionaryException(
						"Stored procedure name does not exist");
			}
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ "has no permission to remove procedures.");
		}

	}

	@Override
	public boolean containsStoredProcedure(String name, ISession user) {
		return getResourceName(name, user, storedProcedures) != null;
	}

	@Override
	public StoredProcedure getStoredProcedure(String name, ISession user) {
		Resource procedure = getResourceName(name, user, storedProcedures);
		if (procedure != null) {
			if (hasAccessRights(procedure, user,
					DataDictionaryPermission.EXECUTE)) {
				return this.storedProcedures.get(procedure);
			} else {
				throw new PermissionException("User "
						+ user.getUser().getName()
						+ "has no permission to remove procedures.");
			}
		} else {
			throw new DataDictionaryException("Stored procedure " + name
					+ " does not exist");
		}
	}

	@Override
	public List<StoredProcedure> getStoredProcedures(ISession caller) {
		List<StoredProcedure> list = new ArrayList<>();
		for (Entry<Resource, IUser> e : this.storedProceduresFromUser
				.entrySet()) {
			if (e.getValue().equals(caller.getUser())) {
				StoredProcedure sp = this.storedProcedures.get(e.getKey());
				list.add(sp);
			}
		}
		return list;
	}

}