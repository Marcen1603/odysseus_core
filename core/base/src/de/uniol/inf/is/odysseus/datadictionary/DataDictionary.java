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
import de.uniol.inf.is.odysseus.usermanagement.NullUserException;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.util.RemoveOwnersGraphVisitor;

public class DataDictionary implements IDataDictionary {

	protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}

	private List<IDataDictionaryListener> listeners = new ArrayList<IDataDictionaryListener>();
	final private IStore<String, ILogicalOperator> streamDefinitions;
	final private IStore<String, User> viewOrStreamFromUser;
	final private IStore<String, ILogicalOperator> viewDefinitions;
	final private IStore<String, SDFEntity> entityMap;
	final private IStore<String, User> entityFromUser;
	final private IStore<String, String> sourceTypeMap;

	DataDictionary() {
		try {
			if (Boolean.parseBoolean(OdysseusDefaults.get("storeDataDictionary"))) {
				streamDefinitions = new FileStore<String, ILogicalOperator>(
						OdysseusDefaults.get("streamDefinitionsFilename"));
				viewOrStreamFromUser = new FileStore<String, User>(
						OdysseusDefaults.get("streamOrViewFromUserFilename"));
				viewDefinitions = new FileStore<String, ILogicalOperator>(
						OdysseusDefaults.get("viewDefinitionsFilename"));
				entityMap = new FileStore<String, SDFEntity>(
						OdysseusDefaults.get("entitiesFilename"));
				sourceTypeMap = new FileStore<String, String>(
						OdysseusDefaults.get("sourceTypeMapFilename"));
				entityFromUser = new FileStore<String, User>(
						OdysseusDefaults.get("entityFromUserFilename"));
			} else {
				streamDefinitions = new MemoryStore<String, ILogicalOperator>();
				viewOrStreamFromUser = new MemoryStore<String, User>();
				viewDefinitions = new MemoryStore<String, ILogicalOperator>();
				entityMap = new MemoryStore<String, SDFEntity>();
				entityFromUser = new MemoryStore<String, User>();
				sourceTypeMap = new MemoryStore<String, String>();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// ----------------------------------------------------------------------------
	// Entity Management
	// ----------------------------------------------------------------------------

	@Override
	public void addEntity(String uri, SDFEntity entity, User user)
			throws HasNoPermissionException {
		if (AccessControl.hasPermission(DataDictionaryAction.ADD_ENTITY,
				DataDictionaryAction.alias, user)) {
			try {
				this.entityMap.put(uri, entity);
				this.entityFromUser.put(uri, user);

			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new HasNoPermissionException("User " + user.getUsername()
					+ "has no permission to add entities.");
		}
	}

	@Override
	public SDFEntity getEntity(String uri, User caller)
			throws HasNoPermissionException {
		if (checkObjectAccess(uri, caller, DataDictionaryAction.GET_ENTITY)) {
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

	// TODO: REMOVE Entity

	/**
	 * returns the username from the creator of the given entity
	 * 
	 * @param entityuri
	 * @return username
	 * @throws HasNoPermissionException
	 */
	// no restric
	@Override
	public String getUserForEntity(String entityuri) {
		User user = this.entityFromUser.get(entityuri);
		return user!=null?user.getUsername():null;
	}

	// ----------------------------------------------------------------------------
	// Source Management
	// ----------------------------------------------------------------------------

	// no restirc
	@Override
	public void addSourceType(String sourcename, String sourcetype) {
		sourceTypeMap.put(sourcename, sourcetype);
	}

	private String getSourceType(String sourcename) throws SQLException {
		String value = sourceTypeMap.get(sourcename);
		if (value == null) {
			throw new IllegalArgumentException("missing source type for: "
					+ sourcename);
		}
		return value;
	}

	// no restric
	@Override
	public SDFSource createSDFSource(String sourcename) {
		try {
			String type = getSourceType(sourcename);
			SDFSource source = new SDFSource(sourcename, type);

			return source;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
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
			User caller) {
		if (AccessControl.hasPermission(DataDictionaryAction.ADD_VIEW,
				DataDictionaryAction.alias, caller)) {
			if (viewDefinitions.containsKey(viewname)) {
				throw new RuntimeException("View " + viewname
						+ " already exists. Drop First");
			}
			try {
				// Remove Owner from View
				RemoveOwnersGraphVisitor<ILogicalOperator> visitor = new RemoveOwnersGraphVisitor<ILogicalOperator>();
				@SuppressWarnings("rawtypes")
				AbstractGraphWalker walker = new AbstractGraphWalker();
				walker.prefixWalk(topOperator, visitor);
			
				this.viewDefinitions.put(viewname, topOperator);
				viewOrStreamFromUser.put(viewname, caller);
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			fireAddEvent(viewname, topOperator);
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to add a view.");
		}
	}

	@SuppressWarnings("unchecked")
	private ILogicalOperator getView(String viewname, User caller) {
		ILogicalOperator logicalPlan = this.viewDefinitions.get(viewname);
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(null);
		@SuppressWarnings("rawtypes")
		AbstractGraphWalker walker = new AbstractGraphWalker();
		walker.prefixWalk(logicalPlan, copyVisitor);		
		return copyVisitor.getResult();
	}

	private ILogicalOperator removeView(String viewname, User caller) {
		ILogicalOperator op;
		try {
			op = viewDefinitions.remove(viewname);
			if (op != null) {
				viewOrStreamFromUser.remove(viewname);
			}
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}
		if (op != null)
			fireRemoveEvent(viewname, op);
		return op;
	}

	// no restric
	@Override
	public boolean isView(String name) {
		return this.viewDefinitions.containsKey(name);
	}

	// ------------------------------------------------------------------------
	// Stream Management
	// ------------------------------------------------------------------------

	@Override
	public void setStream(String streamname, ILogicalOperator plan, User caller) {
		if (AccessControl.hasPermission(DataDictionaryAction.ADD_STREAM,
				DataDictionaryAction.alias, caller)) {
			if (streamDefinitions.containsKey(streamname)) {
				throw new RuntimeException("Stream " + streamname
						+ " already exists. Remove First");
			}
			try {
				streamDefinitions.put(streamname, plan);
				viewOrStreamFromUser.put(streamname, caller);
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			fireAddEvent(streamname, plan);
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to set a new view.");
		}
	}

	// no restric
	private AccessAO getStream(String viewname, User caller) {
		if (!this.streamDefinitions.containsKey(viewname)) {
			throw new IllegalArgumentException("no such view: " + viewname);
		}

		SDFSource source = createSDFSource(viewname);
		AccessAO ao = new AccessAO(source);

		SDFEntity entity = getEntity(viewname, caller);
		ao.setOutputSchema(entity.getAttributes());

		return ao;
	}

	@Override
	public ILogicalOperator getStreamForTransformation(String name, User caller) {
		checkViewAccess(name, caller, DataDictionaryAction.READ);
		return streamDefinitions.get(name);
	}

	// no restric
	@Override
	public boolean hasStream(String name, User user) {
		return streamDefinitions.containsKey(name);
	}

	// ------------------------------------------------------------------------
	// Access View or Streams
	// ------------------------------------------------------------------------

	@Override
	public Set<Entry<String, ILogicalOperator>> getStreamsAndViews(User caller) {
		Set<Entry<String, ILogicalOperator>> sources = new HashSet<Entry<String, ILogicalOperator>>();

		for (Entry<String, ILogicalOperator> viewEntry : streamDefinitions
				.entrySet()) {
			try {
				checkViewAccess(viewEntry.getKey(), caller,
						DataDictionaryAction.READ);
				sources.add(viewEntry);
			} catch (HasNoPermissionException e) {
				// ignore
			}
		}
		for (Entry<String, ILogicalOperator> viewEntry : viewDefinitions
				.entrySet()) {
			try {
				checkViewAccess(viewEntry.getKey(), caller,
						DataDictionaryAction.READ);
				sources.add(viewEntry);
			} catch (HasNoPermissionException e) {
				// ignore
			}
		}
		if (sources.isEmpty()
				&& !(this.streamDefinitions.isEmpty() && this.viewDefinitions
						.isEmpty())) {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission on any View.");
		}
		return sources;
	}

	@Override
	public ILogicalOperator getViewOrStream(String viewname, User caller) {
		if (this.viewDefinitions.containsKey(viewname)) {
			checkViewAccess(viewname, caller, DataDictionaryAction.READ);
			return getView(viewname, caller);
		} else {
			checkViewAccess(viewname, caller, DataDictionaryAction.READ);
			return getStream(viewname, caller);
		}
	}

	@Override
	public ILogicalOperator removeViewOrStream(String viewname, User caller) {
		if (this.viewDefinitions.containsKey(viewname)) {
			checkViewAccess(viewname, caller, DataDictionaryAction.REMOVE_VIEW);
			return removeView(viewname, caller);
		} else {
			ILogicalOperator op;
			checkViewAccess(viewname, caller,
					DataDictionaryAction.REMOVE_STREAM);
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
	public boolean containsViewOrStream(String viewName, User user) {
		return this.streamDefinitions.containsKey(viewName)
				|| this.viewDefinitions.containsKey(viewName);
	}

	// no restric
	@Override
	public User getUserForViewOrStream(String view) {
		return viewOrStreamFromUser.get(view);
	}

	// ----------------------------------------------------------------------------
	// Rights Management
	// ----------------------------------------------------------------------------

	/**
	 * return true if the given user has permission to access the given object
	 * in a certain way (action). Object can be a source or entity.
	 * 
	 * @param uri
	 * @param caller
	 * @param action
	 * @return boolean
	 */
	private boolean checkObjectAccess(String uri, User caller,
			DataDictionaryAction action) {
		return AccessControl.hasPermission(action, uri, caller)
				|| isCreatorOfObject(caller.getUsername(), uri)
				|| hasSuperAction(action, DataDictionaryAction.alias, caller);
	}

	/**
	 * return true if the given user has permission to access the given view in
	 * a certain way (action).
	 * 
	 * @param uri
	 * @param caller
	 * @param action
	 * @return boolean
	 */
	private void checkViewAccess(String viewOrSource, User caller,
			DataDictionaryAction action) {
		if (AccessControl.hasPermission(action, viewOrSource, caller)
		// is owner
				|| isCreatorOfView(caller.getUsername(),
						viewOrSource)
				|| hasSuperAction(action, DataDictionaryAction.alias, caller)) {
			return;
		}
		throw new HasNoPermissionException("User " + caller.getUsername()
				+ " has not the permission '" + action + "' on Source/View '"
				+ viewOrSource);
	}

	/**
	 * checks if the given user has higher permission as the given action.
	 * Calls the corresponding method in the action class.
	 * 
	 * @param action
	 * @param objecturi
	 * @param user
	 * @return boolean
	 */
	@Override
	public boolean hasSuperAction(DataDictionaryAction action,
			String objecturi, User user) {
		return AccessControl
				.hasPermission(DataDictionaryAction.hasSuperAction(action),
						objecturi, user);
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
		if (objecturi == null){
			return false;
		}
		if (username != null && !username.isEmpty()) {
			String user = getUserForEntity(
					objecturi);
			if (user == null || user.isEmpty()) {
				User userObj = getUserForViewOrStream(objecturi);
				user = userObj != null ? userObj.getUsername() : null;
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
		if (viewname == null){
			return false;
		}
		if (!username.isEmpty()) {
			User user = getUserForViewOrStream(viewname);
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

}