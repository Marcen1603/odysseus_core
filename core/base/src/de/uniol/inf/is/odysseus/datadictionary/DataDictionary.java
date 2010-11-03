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
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.CopyLogicalGraphVisitor;

public class DataDictionary {

	// TODO: Zugriffsberechtigungen realisieren

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}

	static private DataDictionary instance = null;

	static private String filePrefix = System.getProperty("user.home")
			+ "/odysseus/";
	private boolean useFileStore = false;

	private List<IDataDictionaryListener> listeners = new ArrayList<IDataDictionaryListener>();

	final private IStore<String, ILogicalOperator> viewDefinitions;
	final private IStore<String, User> viewFromUser;
	final private IStore<String, ILogicalOperator> logicalViewDefinitions;
	final private IStore<String, SDFEntity> entityMap;
	final private IStore<String, User> entityFromUser;
	final private IStore<String, String> sourceTypeMap;
	final private IStore<String, User> sourceFromUser;

	public void clear() {
		try {
			this.viewDefinitions.clear();
			this.logicalViewDefinitions.clear();
			this.entityMap.clear();
			this.sourceTypeMap.clear();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private DataDictionary() {
		try {
			if (useFileStore) {
				viewDefinitions = new FileStore<String, ILogicalOperator>(
						filePrefix + "viewDefinitions.store");
				viewFromUser = new FileStore<String, User>(filePrefix
						+ "viewFromUser.store");
				logicalViewDefinitions = new FileStore<String, ILogicalOperator>(
						filePrefix + "logicalViewDefinitions.store");
				entityMap = new FileStore<String, SDFEntity>(filePrefix
						+ "entities.store");
				sourceTypeMap = new FileStore<String, String>(filePrefix
						+ "sourceTypeMap.store");
				entityFromUser = new FileStore<String, User>(filePrefix
						+ "entityFromUser.store");
				sourceFromUser = new FileStore<String, User>(filePrefix
						+ "sourceFromUser.store");
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

	public void addListener(IDataDictionaryListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("listener is null");

		if (!listeners.contains(listener))
			listeners.add(listener);
	}

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

	public synchronized static DataDictionary getInstance() {
		if (instance == null) {
			// logger.debug("Create new DataDictionary");
			instance = new DataDictionary();
		}
		return instance;
	}

	public void addEntity(String uri, SDFEntity entity, User user) {
		// throws HasNoPermissionException {
		// if (AccessControl.hasPermission(DataDictionaryAction.ADD_ENTITY,
		// "DataDictionary", user)) {
		try {
			this.entityMap.put(uri, entity);
			this.entityFromUser.put(uri, user);
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}
		// } else {
		// throw new HasNoPermissionException("User " + user.getUsername()
		// + "has no permission to add entities.");
		// }
	}

	/**
	 * returns the username for the given entityname
	 * 
	 * @param entityuri
	 * @return Username
	 * @throws HasNoPermissionException
	 */
	public String getUserForEntity(String entityuri) {
		// no restric
		return this.entityFromUser.get(entityuri).getUsername();
	}

	public SDFEntity getEntity(String uri, User caller)
			throws HasNoPermissionException {

		// if (AccessControl.hasPermission(DataDictionaryAction.GET_ENTITY, uri,
		// caller) || instance.getUserForEntity(uri).equals(caller)) {
		if (true) {
			SDFEntity ret = entityMap.get(uri);
			if (ret == null) {
				throw new IllegalArgumentException("no such entity: " + uri);
			}
			return ret;
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to get entity" + uri);
		}
	}

	public void addSourceType(String sourcename, String sourcetype, User user)
			throws HasNoPermissionException {
		try {
			if (AccessControl
					.hasPermission(DataDictionaryAction.ADD_SOURCETYPE,
							"DataDictionary", user)) {
				sourceTypeMap.put(sourcename, sourcetype);
				sourceFromUser.put(sourcename, user);
			} else {
				throw new HasNoPermissionException("User " + user.toString()
						+ " has no permission to add sourcetypes.");
			}
		} catch (StoreException e) {
			new RuntimeException(e);
		}

	}

	/**
	 * returns the username for the given sourcename
	 * 
	 * @param sourcename
	 * @return Username
	 * @throws HasNoPermissionException
	 */
	public String getUserForSource(String sourcename) {
		// no restric
		return this.sourceFromUser.get(sourcename).getUsername();
	}

	public String getSourceType(String sourcename, User caller)
			throws SQLException, HasNoPermissionException {
		// if (AccessControl.hasPermission(DataDictionaryAction.GET_SOURCETYPE,
		// sourcename, caller)) {
		if (true) {
			String value = sourceTypeMap.get(sourcename);
			if (value == null) {
				throw new IllegalArgumentException("missing source type for: "
						+ sourcename);
			}
			return value;
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to get sourcetype");
		}
	}

	public SDFSource getSource(String sourcename, User caller)
			throws HasNoPermissionException {
		// if (AccessControl.hasPermission(DataDictionaryAction.GET_SOURCE,
		// sourcename, caller)
		// || instance.getUserForSource(sourcename).equals(caller)) {
		if (true) {
			try {
				String type = getSourceType(sourcename, caller);
				SDFSource source = new SDFSource(sourcename, type);

				return source;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			throw new HasNoPermissionException("has no permission");
		}
	}

	public void setView(String viewname, ILogicalOperator plan, User caller)
			throws HasNoPermissionException {
		if (AccessControl.hasPermission(DataDictionaryAction.SET_VIEW,
				"DataDictionary", caller)) {
			if (viewDefinitions.containsKey(viewname)) {
				throw new RuntimeException("View " + viewname
						+ " already exists. Remove First");
			}
			try {
				viewDefinitions.put(viewname, plan);
				viewFromUser.put(viewname, caller);
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			fireAddEvent(viewname, plan);
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to set a new view.");
		}
	}

	public ILogicalOperator getView(String viewname, User caller)
			throws HasNoPermissionException {
		// TODO superOperation
		// if (AccessControl.hasPermission(DataDictionaryAction.GET_VIEW,
		// viewname, caller)
		// || instance.getUserForView(viewname).equals(caller)) {
		if (true) {
			if (this.logicalViewDefinitions.containsKey(viewname)) {
				return getLogicalView(viewname, caller);
			} else {
				return getViewReference(viewname, caller);
			}
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to get this view.");
		}
	}

	public ILogicalOperator removeView(String viewname, User caller)
			throws HasNoPermissionException {
		if (AccessControl.hasPermission(DataDictionaryAction.REMOVE_VIEW,
				viewname, caller)
				|| instance.getUserForView(viewname).equals(caller)) {
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

	public ILogicalOperator removeLogicalView(String viewname, User caller)
			throws HasNoPermissionException {
		if (AccessControl.hasPermission(DataDictionaryAction.REMOVE_LOGIC_VIEW,
				viewname, caller)
				|| instance.getUserForView(viewname).equals(caller)) {
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
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to remove this logic view.");
		}
	}

	public ILogicalOperator getViewForTransformation(String name) {
		// TODO abfrage ?
		return viewDefinitions.get(name);
	}

	private AccessAO getViewReference(String viewname, User caller)
			throws HasNoPermissionException {
		// TODO superOperation
		// if (AccessControl.hasPermission(
		// DataDictionaryAction.GET_VIEW_REFERENCE, viewname, caller)
		// || instance.getUserForView(viewname).equals(caller)) {
		if (true) {
			if (!this.viewDefinitions.containsKey(viewname)) {
				throw new IllegalArgumentException("no such view: " + viewname);
			}

			SDFSource source = getSource(viewname, caller);
			AccessAO ao = new AccessAO(source);

			SDFEntity entity = getEntity(viewname, caller);
			ao.setOutputSchema(entity.getAttributes());

			return ao;
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to get view reference.");
		}
	}

	public void setLogicalView(String viewname, ILogicalOperator topOperator,
			User caller) throws HasNoPermissionException {
		// if (AccessControl.hasPermission(DataDictionaryAction.SET_LOGIC_VIEW,
		// viewname, caller)
		// || instance.getUserForView(viewname).equals(caller)) {
		if (true) {
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
			// TODO
			throw new HasNoPermissionException("");
		}
	}

	@SuppressWarnings("unchecked")
	private ILogicalOperator getLogicalView(String viewname, User caller)
			throws HasNoPermissionException {
		// TODO superOperation
		// if (AccessControl.hasPermission(DataDictionaryAction.GET_LOGIC_VIEW,
		// viewname, caller)
		// || instance.getUserForView(viewname).equals(caller)) {
		if (true) {
			ILogicalOperator logicalPlan = this.logicalViewDefinitions
					.get(viewname);
			CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>();
			@SuppressWarnings("rawtypes")
			AbstractGraphWalker walker = new AbstractGraphWalker();
			walker.prefixWalk(logicalPlan, copyVisitor);
			return copyVisitor.getResult();
		} else {
			// TODO
			throw new HasNoPermissionException("");
		}
	}

	public boolean isLogicalView(String name) {
		// TODO abfrage ?
		return this.logicalViewDefinitions.containsKey(name);
	}

	public boolean hasView(String name, User user) {
		// TODO abfrage ?
		return viewDefinitions.containsKey(name);
	}

	public Set<Entry<String, ILogicalOperator>> getViews(User caller)
			throws HasNoPermissionException {
		// TODO superOperation
		// if (AccessControl.hasPermission(DataDictionaryAction.GET_VIEWS,
		// "DataDictionary", caller)) {
		if (true) {
			Set<Entry<String, ILogicalOperator>> sources = new HashSet<Entry<String, ILogicalOperator>>();
			sources.addAll(viewDefinitions.entrySet());
			sources.addAll(logicalViewDefinitions.entrySet());
			return sources;
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to get views.");
		}
	}

	public void clearViews() {
		// TODO depricated ?
		for (Entry<String, ILogicalOperator> entry : this.viewDefinitions
				.entrySet())
			fireRemoveEvent(entry.getKey(), entry.getValue());

		try {
			this.viewDefinitions.clear();
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}

	}

	public boolean containsView(String viewName, User user) {
		// TODO abfrage ?
		return this.viewDefinitions.containsKey(viewName)
				|| this.logicalViewDefinitions.containsKey(viewName);
	}

	public boolean emptySourceTypeMap() {
		// TODO abfrage ?
		return sourceTypeMap.isEmpty();
	}

	public User getUserForView(String view) {
		// TODO abfrage ?
		return viewFromUser.get(view);
	}

}
