package de.uniol.inf.is.odysseus.base;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.base.store.FileStore;
import de.uniol.inf.is.odysseus.base.store.IStore;
import de.uniol.inf.is.odysseus.base.store.MemoryStore;
import de.uniol.inf.is.odysseus.base.store.StoreException;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
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
	final private IStore<String, String> sourceTypeMap;

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
			} else {
				viewDefinitions = new MemoryStore<String, ILogicalOperator>();
				viewFromUser = new MemoryStore<String, User>();
				logicalViewDefinitions = new MemoryStore<String, ILogicalOperator>();
				entityMap = new MemoryStore<String, SDFEntity>();
				sourceTypeMap = new MemoryStore<String, String>();
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
		try {
			this.entityMap.put(uri, entity);
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}
	}

	public SDFEntity getEntity(String uri, User user) {
		SDFEntity ret = entityMap.get(uri);
		if (ret == null) {
			throw new IllegalArgumentException("no such entity: " + uri);
		}
		return ret;
	}

	public void addSourceType(String sourcename, String sourcetype) {
		try {
			sourceTypeMap.put(sourcename, sourcetype);
		} catch (StoreException e) {
			new RuntimeException(e);
		}

	}

	public String getSourceType(String sourcename) throws SQLException {
		String value = sourceTypeMap.get(sourcename);
		if (value == null) {
			throw new IllegalArgumentException("missing source type for: "
					+ sourcename);
		}
		return value;
	}

	public SDFSource getSource(String name) {
		try {
			String type = getSourceType(name);
			SDFSource source = new SDFSource(name, type);

			return source;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setView(String name, ILogicalOperator plan, User user) {
		if (viewDefinitions.containsKey(name)) {
			throw new RuntimeException("View " + name
					+ " already exists. Remove First");
		}
		try {
			viewDefinitions.put(name, plan);
			viewFromUser.put(name, user);
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}
		fireAddEvent(name, plan);
	}

	public ILogicalOperator getView(String name, User user) {
		if (this.logicalViewDefinitions.containsKey(name)) {
			return getLogicalView(name, user);
		} else {
			return getViewReference(name, user);
		}
	}

	public ILogicalOperator removeView(String name, User user) {
		if (this.logicalViewDefinitions.containsKey(name)) {
			return removeLogicalView(name, user);
		} else {
			ILogicalOperator op;
			try {
				op = viewDefinitions.remove(name);
				if (op != null) {
					viewFromUser.remove(name);
				}
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			if (op != null)
				fireRemoveEvent(name, op);
			return op;
		}
	}

	public ILogicalOperator removeLogicalView(String name, User user) {
		ILogicalOperator op;
		try {
			op = logicalViewDefinitions.remove(name);
			if (op != null) {
				viewFromUser.remove(name);
			}
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}
		if (op != null)
			fireRemoveEvent(name, op);
		return op;
	}

	public ILogicalOperator getViewForTransformation(String name) {
		return viewDefinitions.get(name);
	}

	private AccessAO getViewReference(String name, User user) {
		if (!this.viewDefinitions.containsKey(name)) {
			throw new IllegalArgumentException("no such view: " + name);
		}

		SDFSource source = getSource(name);
		AccessAO ao = new AccessAO(source);

		SDFEntity entity = getEntity(name, user);
		ao.setOutputSchema(entity.getAttributes());

		return ao;
	}

	public void setLogicalView(String name, ILogicalOperator topOperator,
			User user) {
		if (logicalViewDefinitions.containsKey(name)) {
			throw new RuntimeException("View " + name
					+ " already exists. Drop First");
		}
		try {
			this.logicalViewDefinitions.put(name, topOperator);
			viewFromUser.put(name, user);
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}
		fireAddEvent(name, topOperator);
	}

	@SuppressWarnings("unchecked")
	private ILogicalOperator getLogicalView(String name, User user) {
		ILogicalOperator logicalPlan = this.logicalViewDefinitions.get(name);
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>();
		@SuppressWarnings("rawtypes")
		AbstractGraphWalker walker = new AbstractGraphWalker();
		walker.prefixWalk(logicalPlan, copyVisitor);
		return copyVisitor.getResult();
	}

	public boolean isLogicalView(String name) {
		return this.logicalViewDefinitions.containsKey(name);
	}

	public boolean hasView(String name, User user) {
		return viewDefinitions.containsKey(name);
	}

	public Set<Entry<String, ILogicalOperator>> getViews() {
		Set<Entry<String, ILogicalOperator>> sources = new HashSet<Entry<String, ILogicalOperator>>();
		sources.addAll(viewDefinitions.entrySet());
		sources.addAll(logicalViewDefinitions.entrySet());
		return sources;
	}

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

	public boolean containsView(String viewName, User user) {
		return this.viewDefinitions.containsKey(viewName)
				|| this.logicalViewDefinitions.containsKey(viewName);
	}

	public boolean emptySourceTypeMap() {
		return sourceTypeMap.isEmpty();
	}

	public User getUserForView(String view) {
		return viewFromUser.get(view);
	}

}
