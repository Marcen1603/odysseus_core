package de.uniol.inf.is.odysseus.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.CopyLogicalGraphVisitor;

/**
 * TODO neu machen :). ausserdem: atm werden ressourcen bei exceptions nicht
 * richtig aufgeraeumt.
 */
public class DataDictionary {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}
	
	static private DataDictionary instance = null;

	private List<IDataDictionaryListener> listeners = new ArrayList<IDataDictionaryListener>();
	
	private Map<String, ILogicalOperator> viewDefinitions = new HashMap<String, ILogicalOperator>();
	private Map<String, ILogicalOperator> logicalViewDefinitions = new HashMap<String, ILogicalOperator>();

	public Map<String, SDFEntity> entityMap = new HashMap<String, SDFEntity>();

	public Map<String, String> sourceTypeMap = new HashMap<String, String>();

	private DataDictionary() {
	}

	public void addListener( IDataDictionaryListener listener ) {
		if( listener == null ) 
			throw new IllegalArgumentException("listener is null");
		
		if( !listeners.contains(listener))
			listeners.add(listener);
	}
	
	public void removeListener( IDataDictionaryListener listener ) {
		listeners.remove(listener);
	}
	
	private void fireAddEvent( String name, ILogicalOperator op ) {
		for( IDataDictionaryListener listener : listeners ) {
			try {
				listener.addedViewDefinition(this, name, op);
			} catch( Exception ex ) {
				getLogger().error("Error during executing listener", ex);
			}
		}
	}
	
	private void fireRemoveEvent( String name, ILogicalOperator op ) {
		for( IDataDictionaryListener listener : listeners ) {
			try {
				listener.removedViewDefinition(this, name, op);
			} catch( Exception ex ) {
				getLogger().error("Error during executing listener", ex);
			}
		}
	}
	
	public synchronized static DataDictionary getInstance() {
		if (instance == null) {
//			logger.debug("Create new DataDictionary");
			instance = new DataDictionary();
		}
		return instance;
	}

	public SDFEntity getEntity(String uri) {
		SDFEntity ret = entityMap.get(uri);
		if (ret == null) {
			throw new IllegalArgumentException("no such entity: " + uri);
		}
		return ret;
	}

	private String typeOfSource(String source) throws SQLException {
		String value = sourceTypeMap.get(source);
		if (value == null) {
			throw new IllegalArgumentException("missing source type for: "
					+ source);
		}
		return value;
	}

	public SDFSource getSource(String name) {
		try {
			String type = typeOfSource(name);
			SDFSource source = new SDFSource(name, type);

			return source;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setView(String name, ILogicalOperator plan) {
		viewDefinitions.put(name, plan);
		fireAddEvent(name, plan);
	}

	public ILogicalOperator getView(String name) {
		if (this.logicalViewDefinitions.containsKey(name)){
			return getLogicalView(name);
		} else { 
			return getViewReference(name);
		}
	}
	
	public ILogicalOperator removeView(String name) {
		ILogicalOperator op = viewDefinitions.remove(name);
		if( op != null ) 
			fireRemoveEvent(name, op);
		return op;
	}
	
	public ILogicalOperator getViewForTransformation(String name) {
		return viewDefinitions.get(name);
	}
	
	private AccessAO getViewReference(String name) {
		if (!this.viewDefinitions.containsKey(name)) {
			throw new IllegalArgumentException("no such view: " + name);
		}
		
		SDFSource source = getSource(name);
		AccessAO ao = new AccessAO(source);
		
		SDFEntity entity = getEntity(name);
		ao.setOutputSchema(entity.getAttributes());
		
		return ao;
	}
	
	public void setLogicalView(String name, ILogicalOperator topOperator){
		this.logicalViewDefinitions.put(name, topOperator);
		fireAddEvent(name, topOperator);
	}
	
	@SuppressWarnings("unchecked")
	private ILogicalOperator getLogicalView(String name) {
		ILogicalOperator logicalPlan = this.logicalViewDefinitions.get(name);
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>();
		AbstractGraphWalker walker = new AbstractGraphWalker();
		walker.prefixWalk(logicalPlan, copyVisitor);
		return copyVisitor.getResult();
	}

	public boolean hasView(String name){
		return viewDefinitions.containsKey(name);
	}
	
	public Set<Entry<String, ILogicalOperator>> getViews() {
		Set<Entry<String, ILogicalOperator>> sources = new HashSet<Entry<String,ILogicalOperator>>();
		sources.addAll(viewDefinitions.entrySet());
		sources.addAll(logicalViewDefinitions.entrySet());
		return sources;
	}
	
	public void clearViews() {
		
		for( Entry<String, ILogicalOperator> entry : this.viewDefinitions.entrySet() )
			fireRemoveEvent(entry.getKey(), entry.getValue());
		
		this.viewDefinitions.clear();
		
	}

	public boolean containsView(String viewName) {
		return this.viewDefinitions.containsKey(viewName) || this.logicalViewDefinitions.containsKey(viewName);
	}
}
