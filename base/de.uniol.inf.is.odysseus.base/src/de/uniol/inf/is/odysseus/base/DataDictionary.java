package de.uniol.inf.is.odysseus.base;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

/**
 * TODO neu machen :). ausserdem: atm werden ressourcen bei exceptions nicht
 * richtig aufgeraeumt.
 */
public class DataDictionary {

//	private static final Logger logger = LoggerFactory
//			.getLogger(DataDictionary.class);

	static private DataDictionary instance = null;

	private Map<String, ILogicalOperator> viewDefinitions = new HashMap<String, ILogicalOperator>();
	private Map<String, ILogicalOperator> logicalViewDefinitions = new HashMap<String, ILogicalOperator>();

	public Map<String, SDFEntity> entityMap = new HashMap<String, SDFEntity>();

	public Map<String, String> sourceTypeMap = new HashMap<String, String>();

	private DataDictionary() {
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
	}


	public SDFAttributeList getViewOutputSchema(String name) {
		return viewDefinitions.get(name).getOutputSchema();
	}

	public ILogicalOperator getViewForTransformation(String name) {
		return viewDefinitions.get(name);
	}
	
	public void setLogicalView(String name, ILogicalOperator topOperator){
		this.logicalViewDefinitions.put(name, topOperator);
	}
	
	public ILogicalOperator getLogicalView(String name) {
		return this.logicalViewDefinitions.get(name);
	}

	public boolean hasView(String name){
		return viewDefinitions.containsKey(name);
	}
	
	public Set<Entry<String, ILogicalOperator>> getViews() {
		return viewDefinitions.entrySet();
	}
	
	public void clearViews() {
		this.viewDefinitions.clear();
	}

	public boolean containsView(String viewName) {
		return this.viewDefinitions.containsKey(viewName) || this.logicalViewDefinitions.containsKey(viewName);
	}
}
