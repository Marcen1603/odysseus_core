package de.uniol.inf.is.odysseus.datadictionary;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.store.FileStore;
import de.uniol.inf.is.odysseus.store.IStore;
import de.uniol.inf.is.odysseus.store.MemoryStore;
import de.uniol.inf.is.odysseus.store.StoreException;
import de.uniol.inf.is.odysseus.usermanagement.AccessControl;
import de.uniol.inf.is.odysseus.usermanagement.HasNoPermissionException;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.UserManagementAction;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.CopyLogicalGraphVisitor;

public class DataDictionary {

	// TODO: REMOVE Entity
	// TODO: REMOVE Source (queries? entities?)

	protected static Logger _logger = null;

	static private DataDictionary instance = null;

	public synchronized static DataDictionary getInstance() {
		if (instance == null) {
			// logger.debug("Create new DataDictionary");
			instance = new DataDictionary();
		}
		return instance;
	}

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}

	private List<IDataDictionaryListener> listeners = new ArrayList<IDataDictionaryListener>();
	final private IStore<String, ILogicalOperator> viewDefinitions;
	final private IStore<String, User> viewFromUser;
	final private IStore<String, ILogicalOperator> logicalViewDefinitions;
	final private IStore<String, SDFEntity> entityMap;
	final private IStore<String, User> entityFromUser;
	final private IStore<String, String> sourceTypeMap;

	final private IStore<String, User> sourceFromUser;

	private DataDictionary() {
		try {
			if (OdysseusDefaults.storeDataDictionary) {
				viewDefinitions = new FileStore<String, ILogicalOperator>(
						OdysseusDefaults.viewDefinitionsFilename);
				viewFromUser = new FileStore<String, User>(
						OdysseusDefaults.viewFromUserFilename);
				logicalViewDefinitions = new FileStore<String, ILogicalOperator>(
						OdysseusDefaults.logicalViewDefinitionsFilename);
				entityMap = new FileStore<String, SDFEntity>(
						OdysseusDefaults.entitiesFilename);
				sourceTypeMap = new FileStore<String, String>(
						OdysseusDefaults.sourceTypeMapFilename);
				entityFromUser = new FileStore<String, User>(
						OdysseusDefaults.entityFromUserFilename);
				sourceFromUser = new FileStore<String, User>(
						OdysseusDefaults.sourceFromUserFilename);
			} else {
				viewDefinitions = new MemoryStore<String, ILogicalOperator>();
				viewFromUser = new MemoryStore<String, User>();
				logicalViewDefinitions = new MemoryStore<String, ILogicalOperator>();
				entityMap = new MemoryStore<String, SDFEntity>();
				entityFromUser = new MemoryStore<String, User>();
				sourceTypeMap = new MemoryStore<String, String>();
				sourceFromUser = new MemoryStore<String, User>();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addEntity(String uri, SDFEntity entity, User user)
			throws HasNoPermissionException {
		if (AccessControl.hasPermission(DataDictionaryAction.ADD_ENTITY,
				DataDictionaryAction.alias, user)) {
			try {
				this.entityMap.put(uri, entity);
				this.entityFromUser.put(uri, user);

				// User permission auf Objekt geben
				User system = UserManagement.getInstance().getSuperUser();
				UserManagement.getInstance().grantPermission(system,
						user.getUsername(), DataDictionaryAction.GET_ENTITY,
						uri);
				UserManagement.getInstance().grantPermission(system,
						user.getUsername(), UserManagementAction.GRANT, uri);
				UserManagement.getInstance().grantPermission(system,
						user.getUsername(), UserManagementAction.REVOKE, uri);
				UserManagement.getInstance().grantPermission(system,
						user.getUsername(), DataDictionaryAction.REMOVE_ENTITY,
						uri);
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new HasNoPermissionException("User " + user.getUsername()
					+ "has no permission to add entities.");
		}
	}

	public void addListener(IDataDictionaryListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("listener is null");

		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void addSourceType(String sourcename, String sourcetype, User user) {
		try {
			if (AccessControl.hasPermission(
					DataDictionaryAction.ADD_SOURCETYPE,
					DataDictionaryAction.alias, user)) {

				sourceTypeMap.put(sourcename, sourcetype);
				sourceFromUser.put(sourcename, user);

				// Recht auf die Source
				User system = UserManagement.getInstance().getSuperUser();
				UserManagement.getInstance().grantPermission(system,
						user.getUsername(), DataDictionaryAction.GET_SOURCE,
						sourcename);
				UserManagement.getInstance().grantPermission(system,
						user.getUsername(), DataDictionaryAction.REMOVE_SOURCE,
						sourcename);
			} else {
				throw new HasNoPermissionException("User " + user.toString()
						+ " has no permission to add sourcetypes.");
			}
		} catch (StoreException e) {
			new RuntimeException(e);
		}

	}

	/**
	 * return true if the given user has permission to access the given object
	 * in a certain way (action). Object can be a source or entity.
	 * 
	 * @param uri
	 * @param caller
	 * @param action
	 * @return boolean
	 */
	private boolean allowedToAccessObjects(String uri, User caller,
			DataDictionaryAction action) {
		return AccessControl.hasPermission(action, uri, caller)
				|| AccessControl.isCreatorOfObject(caller.getUsername(), uri)
				|| hasSuperOperation(action, DataDictionaryAction.alias, caller);
	}

	/**
	 * return true if the given user has permission to access the given view in
	 * a certain way (action). Object can be a source or entity.
	 * 
	 * @param uri
	 * @param caller
	 * @param action
	 * @return boolean
	 */
	private boolean allowedToAccessView(String key, User caller,
			DataDictionaryAction action) {
		return AccessControl.hasPermission(action, key, caller)
				// is owner
				|| AccessControl.isCreatorOfView(caller.getUsername(), key)
				|| hasSuperOperation(action, DataDictionaryAction.alias, caller);
	}

	@Deprecated
	public void clearViews() {
		for (Entry<String, ILogicalOperator> entry : this.viewDefinitions
				.entrySet())
			fireRemoveEvent(entry.getKey(), entry.getValue());

		try {
			this.viewDefinitions.clear();
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}

	}

	// no restric
	public boolean containsView(String viewName, User user) {
		return this.viewDefinitions.containsKey(viewName)
				|| this.logicalViewDefinitions.containsKey(viewName);
	}

	// no restric
	public boolean emptySourceTypeMap() {
		return sourceTypeMap.isEmpty();
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

	public SDFEntity getEntity(String uri, User caller)
			throws HasNoPermissionException {
		if (allowedToAccessObjects(uri, caller, DataDictionaryAction.GET_ENTITY)) {
			SDFEntity ret = entityMap.get(uri);
			if (ret == null) {
				throw new IllegalArgumentException("no such entity: " + uri);
			}
			return ret;
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to get entity '" + uri + "'.");
		}
	}

	@SuppressWarnings("unchecked")
	// no restric
	private ILogicalOperator getLogicalView(String viewname, User caller) {
		ILogicalOperator logicalPlan = this.logicalViewDefinitions
				.get(viewname);
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>();
		@SuppressWarnings("rawtypes")
		AbstractGraphWalker walker = new AbstractGraphWalker();
		walker.prefixWalk(logicalPlan, copyVisitor);
		return copyVisitor.getResult();
	}

	public SDFSource getSource(String sourcename, User caller) {
		if (allowedToAccessObjects(sourcename, caller,
				DataDictionaryAction.GET_SOURCE)) {
			try {
				String type = getSourceType(sourcename, caller);
				SDFSource source = new SDFSource(sourcename, type);

				return source;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			throw new HasNoPermissionException(
					"has no permission to get source '" + sourcename + "'.");
		}
	}

	private String getSourceType(String sourcename, User caller)
			throws SQLException {
		String value = sourceTypeMap.get(sourcename);
		if (value == null) {
			throw new IllegalArgumentException("missing source type for: "
					+ sourcename);
		}
		return value;
	}

	/**
	 * returns the username for the given entityname
	 * 
	 * @param entityuri
	 * @return Username
	 * @throws HasNoPermissionException
	 */
	// no restric
	public String getUserForEntity(String entityuri) {
		return this.entityFromUser.get(entityuri).getUsername();
	}

	/**
	 * returns the username for the given sourcename
	 * 
	 * @param sourcename
	 * @return Username
	 * @throws HasNoPermissionException
	 */
	// no restric
	public String getUserForSource(String sourcename) {
		return this.sourceFromUser.get(sourcename).getUsername();
	}

	// no restric
	public User getUserForView(String view) {
		return viewFromUser.get(view);
	}

	public ILogicalOperator getView(String viewname, User caller) {
		if (allowedToAccessView(viewname, caller, DataDictionaryAction.GET_VIEW)) {
			if (this.logicalViewDefinitions.containsKey(viewname)) {
				return getLogicalView(viewname, caller);
			} else {
				return getViewReference(viewname, caller);
			}
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to get this view '" + viewname + "'.");
		}
	}

	public ILogicalOperator getViewForTransformation(String name, User caller) {
		if (allowedToAccessView(name, caller,
				DataDictionaryAction.GET_VIEW_FOR_TRANFORMATION)) {
			return viewDefinitions.get(name);
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to get view '" + name
					+ "' for transformation.");
		}
	}

	// no restric
	private AccessAO getViewReference(String viewname, User caller) {
		if (!this.viewDefinitions.containsKey(viewname)) {
			throw new IllegalArgumentException("no such view: " + viewname);
		}

		SDFSource source = getSource(viewname, caller);
		AccessAO ao = new AccessAO(source);

		SDFEntity entity = getEntity(viewname, caller);
		ao.setOutputSchema(entity.getAttributes());

		return ao;
	}

	public Set<Entry<String, ILogicalOperator>> getViews(User caller) {
		Set<Entry<String, ILogicalOperator>> sources = new HashSet<Entry<String, ILogicalOperator>>();

		for (Entry<String, ILogicalOperator> viewEntry : viewDefinitions
				.entrySet()) {
			if (allowedToAccessView(viewEntry.getKey(), caller,
					DataDictionaryAction.GET_VIEW)) {
				sources.add(viewEntry);
			}
		}
		for (Entry<String, ILogicalOperator> viewEntry : logicalViewDefinitions
				.entrySet()) {
			if (allowedToAccessView(viewEntry.getKey(), caller,
					DataDictionaryAction.GET_VIEW)) {
				sources.add(viewEntry);
			}
		}
		if (sources.isEmpty()) {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission on any View.");
		}
		return sources;
	}

	/**
	 * returns true if the given user has higher permission as the given
	 * operation
	 * 
	 * @param operation
	 * @param object
	 * @param user
	 * @return boolean
	 */
	public boolean hasSuperOperation(DataDictionaryAction operation,
			String object, User user) {
		return AccessControl.hasPermission(
				DataDictionaryAction.hasSuperAction(operation), object, user);
	}

	// no restric
	public boolean hasView(String name, User user) {
		return viewDefinitions.containsKey(name);
	}

	// no restric
	public boolean isLogicalView(String name) {
		return this.logicalViewDefinitions.containsKey(name);
	}

	// no restirc
	public void removeListener(IDataDictionaryListener listener) {
		listeners.remove(listener);
	}

	private ILogicalOperator removeLogicalView(String viewname, User caller) {
		ILogicalOperator op;
		try {
			op = logicalViewDefinitions.remove(viewname);
			if (op != null) {
				viewFromUser.remove(viewname);
			}
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}
		if (op != null)
			fireRemoveEvent(viewname, op);
		return op;
	}

	public ILogicalOperator removeView(String viewname, User caller) {
		if (allowedToAccessView(viewname, caller,
				DataDictionaryAction.REMOVE_VIEW)) {
			if (this.logicalViewDefinitions.containsKey(viewname)) {
				return removeLogicalView(viewname, caller);
			} else {
				ILogicalOperator op;
				try {
					op = viewDefinitions.remove(viewname);
					if (op != null) {
						viewFromUser.remove(viewname);
					}
				} catch (StoreException e) {
					throw new RuntimeException(e);
				}
				if (op != null)
					fireRemoveEvent(viewname, op);
				return op;
			}
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to remove this view.");
		}
	}

	public void setLogicalView(String viewname, ILogicalOperator topOperator,
			User caller) {
		if (AccessControl.hasPermission(DataDictionaryAction.ADD_LOGICAL_VIEW,
				viewname, caller)
				|| AccessControl
						.isCreatorOfView(caller.getUsername(), viewname)) {
			if (logicalViewDefinitions.containsKey(viewname)) {
				throw new RuntimeException("View " + viewname
						+ " already exists. Drop First");
			}
			try {
				this.logicalViewDefinitions.put(viewname, topOperator);
				viewFromUser.put(viewname, caller);
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			fireAddEvent(viewname, topOperator);
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to add a logical view.");
		}
	}

	public void setView(String viewname, ILogicalOperator plan, User caller) {
		if (AccessControl.hasPermission(DataDictionaryAction.ADD_VIEW,
				DataDictionaryAction.alias, caller)) {
			if (viewDefinitions.containsKey(viewname)) {
				throw new RuntimeException("View " + viewname
						+ " already exists. Remove First");
			}
			try {
				viewDefinitions.put(viewname, plan);
				viewFromUser.put(viewname, caller);

				// Rechte
				User system = UserManagement.getInstance().getSuperUser();
				UserManagement.getInstance().grantPermission(system,
						caller.getUsername(), DataDictionaryAction.GET_VIEW,
						viewname);
				UserManagement.getInstance().grantPermission(system,
						caller.getUsername(), DataDictionaryAction.REMOVE_VIEW,
						viewname);
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			fireAddEvent(viewname, plan);
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to set a new view.");
		}
	}

}