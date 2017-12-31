package de.uniol.inf.is.odysseus.parser.cql2.generator.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import java.util.List;

public class QueryCache implements Cache {
	
	public enum Type {
		PROJECTION_ATTRIBUTE(0), 
		PROJECTION_SOURCE(1), 
		QUERY_AGGREGATION(2), 
		QUERY_EXPRESSION(3),
		QUERY_SUBQUERY(4),
		
		QUERY_ATTRIBUTE(0),
		;

		final int ID;

		Type(int id) {
			ID = id;
		}
	}

	public void putProjectionAttributes(SimpleSelect query, String[] attributes) {
		putProjectionAttributes(query, Arrays.asList(attributes));
	}

	public void putProjectionAttributes(SimpleSelect query, Collection<String> attributes) {
		putCollection(query, attributes, Type.PROJECTION_ATTRIBUTE);
	}

	public Collection<String> getProjectionAttributes(SimpleSelect query) {
		return (Collection<String>) getCollection(query, Type.PROJECTION_ATTRIBUTE);
	}

	public void putProjectionSources(SimpleSelect query, String[] sources) {
		putProjectionSources(query, Arrays.asList(sources));
	}

	public void putProjectionSources(SimpleSelect query, Collection<String> sources) {
		putCollection(query, sources, Type.PROJECTION_SOURCE);
	}

	public Collection<String> getProjectionSourcess(SimpleSelect query) {
		return (Collection<String>) getCollection(query, Type.PROJECTION_SOURCE);
	}
/////
	
	private Map<String, QueryCacheSubQueryEntry> subQueryEntries = new HashMap<>();
	
	public static class QueryCacheSubQueryEntry {
		
		public String parent;
		public NestedSource subQuery;
		public Collection<QueryCacheAttributeEntry> attributes;
		
		public QueryCacheSubQueryEntry(String parent, NestedSource subQuery, Collection<QueryCacheAttributeEntry> attributes) {
			super();
			this.parent = parent;
			this.subQuery = subQuery;
			this.attributes = attributes;
		}
		
		
		
	}

//	public void putSubQuerySources(String name, Collection<QueryCacheAttributeEntry> attributes) {
//		subQueryEntries.put(name, attributes);
////		putMap(name, values.stream().map(e -> e.sources).collect(Collectors.toList()), QueryCache.Type.QUERY_SUBQUERY);
//	}

	public void putSubQuerySources(NestedSource subQuery) {
	
		String name = subQuery.getAlias().getName();
		subQueryEntries.put(name, new QueryCacheSubQueryEntry(name, subQuery, attributeEntries.get(subQuery.getStatement().getSelect())));
		

	}
////
	
	public static class QueryCacheAttributeEntry {
		
		public String name;
		public Collection<String> sources;
		
		public QueryCacheAttributeEntry(String name, Collection<String> sources) {
			super();
			this.name = name;
			this.sources = sources;
		}
		
	}
	
	
	private Map<SimpleSelect, Collection<QueryCacheAttributeEntry>> attributeEntries = new HashMap<>();
	
	public void putQueryAttributes(SimpleSelect query, Collection<QueryCacheAttributeEntry> entry) {
		attributeEntries.put(query, entry);
	}
	
	public Collection<QueryCacheAttributeEntry> getQueryAttributes(SimpleSelect query) {
		return attributeEntries.containsKey(query) ? attributeEntries.get(query) : null;
	}
	
	
//	public void putQueryAttributes(SimpleSelect query, Map<String, Collection<String>> attributes) {
//		putMap(query, attributes, Type.QUERY_ATTRIBUTE);
//	}

//	public Map<String, Collection<String>> getQueryAttributes(SimpleSelect query) {
//		return (Map<String, Collection<String>>) getCollection(query, Type.QUERY_ATTRIBUTE);
//	}

	
	/////
	public Collection<SelectExpression> getQueryAggregations(SimpleSelect query) {
		return (Collection<SelectExpression>) getCollection(query, Type.QUERY_AGGREGATION);
	}


	public void putQueryAggregations(SimpleSelect query, Collection<SelectExpression> aggregations) {
		putCollection(query, aggregations, Type.QUERY_AGGREGATION);
	}
	
	public Collection<SelectExpression> getQueryExpressions(SimpleSelect query) {
		return (Collection<SelectExpression>) getCollection(query, Type.QUERY_EXPRESSION);
	}

	public void putQueryExpressions(SimpleSelect query, Collection<SelectExpression> expressions) {
		putCollection(query, expressions, Type.QUERY_EXPRESSION);
	}

	private final Logger log = LoggerFactory.getLogger(QueryCache.class);
	private final int colIndex = 5;
	private final int mapIndex = 1;

	private Map<SimpleSelect, Collection<?>[]> memory;
	private Map<?, Map<?, ?>[]> memory2;

	public QueryCache() {
		memory = new ConcurrentHashMap<>();
		memory2 = new ConcurrentHashMap<>();
	}

//	@SuppressWarnings("unchecked")
//	private <K, V> void putObjects(K key, V value, Type type) {
//
//		int index = type.ID;
//		int maxIndex = -1;
//		Map<K, V> mem = null;
//
//		if (value instanceof Collection || value instanceof String[]) {
//			mem = (Map<K, V>) memory;
//			maxIndex = colIndex;
//		} else if (value instanceof Map) {
//			mem = (Map<K, V>) memory2;
//			maxIndex = mapIndex;
//		}
//
//		Object[] objectArray;
//		if (mem.containsKey(key)) {
//			objectArray = (V[]) mem.get(key);
//			objectArray[index] = value;
//		} else {
//			objectArray = new Object[maxIndex];
//			objectArray[index] = value;
//			mem.put(key, (V) objectArray);
//		}
//
//	}
	
	//SimpleSelectODO maybe add a add-method for collection

	@SuppressWarnings({ "rawtypes" })
	private Collection<?> getCollection(SimpleSelect key, Type type) {

		log.info("HEY");
		
		if (memory.containsKey(key)) {
			Collection<?> col = memory.get(key)[type.ID];
			log.info("col=" + col);
			log.info("->" + col.getClass());
			return col;
		}

		log.debug("could not find " + type + " for " + key + ": returned new ArrayList");
		Collection<?> m =  new ArrayList();
		return m;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <K1, K2, V extends Map<?, ?>> V getMap(K1 key1, K2 key2, Type type) {

		if (memory2.containsKey(key1)) {
			return (V) memory2.get(key1)[type.ID].get(key2);
		}

		log.debug("could not find " + type + " for (" + key1 + "," + key2 + "): returned new HashMap");
		return (V) new HashMap();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <M extends Map<?, ?>, V> M getMap(@NotNull SimpleSelect key1, Type type) {

		if (memory2.containsKey(key1)) {
			return (M) memory2.get(key1)[type.ID];
		}

		log.debug("could not find " + type + " for " + key1 + " : returned new HashMap");
		return (M) new HashMap();
	}
	
	@SuppressWarnings("unchecked")
	private <K, V> V getAllByType(Type type) {
		Collection<V> collection = new ArrayList<>();
		for (Entry<?, Collection<?>[]> entry : memory.entrySet()) {
			V[] colArray = (V[]) entry.getValue();
			if (colArray != null) {
				collection.addAll(Arrays.asList(colArray));
			}
		}
		return (V) collection;
	}
	
	public Object get(Object... objects) {

		if (objects.length == 3) {
			return getMap((SimpleSelect) objects[0], objects[1], (Type) objects[2]);
		} else if (objects.length == 2) {
			if(((Type) objects[1]).equals(Type.QUERY_ATTRIBUTE)) {
				return getMap((SimpleSelect) objects[0], (Type) objects[1]);
			}
			return getCollection((SimpleSelect) objects[0], (Type) objects[1]);
		} else {
			log.debug("invalid arguments:" + objects.toString());
		}

		return null;
	}

	public Collection<?> getAll(Object obj) {
		return getAllByType((Type) obj);
	}

	
	private void putCollection(SimpleSelect query, Collection<?> values, Type type) {

		int index = type.ID;
		int maxIndex = -1;
		Map<SimpleSelect, Collection<?>[]> mem = null;

		mem = memory;
		maxIndex = colIndex;

		Collection<?>[] objectArray;
		if (mem.containsKey(query)) {
			objectArray = mem.get(query);
			objectArray[index] = values;
		} else {
			objectArray = new Collection[maxIndex];
			objectArray[index] = values;
			mem.put(query, (Collection<?>[]) objectArray);
		}
		
	}
	
	private void putMap(Object key, Map<?,?> values, Type type) {
		
		
		
		int index = type.ID;
		int maxIndex = -1;
		Map<Object, Map<?, ?>[]> mem = null;

		mem = (Map<Object, Map<?, ?>[]>) memory2;
		maxIndex = mapIndex;

		Map<?,?>[] objectArray;
		if (mem.containsKey(key)) {
			objectArray = mem.get(key);
			objectArray[index] = values;
		} else {
			objectArray = new Map[maxIndex];
			objectArray[index] = values;
			mem.put(key, objectArray);
		}
	}
	
//	private void put(Object... objects) {
//
//		if (objects.length == 3) {
//			putObjects((SimpleSelect) objects[0], objects[1], (Type) objects[2]);
//		} else {
//			LOGGER.debug("invalid arguments:" + objects.toString());
//		}
//
//	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <M> void flushMe() {
		
		for (Type type : Type.values()) {
			M m = (M) getAll(type);
			if (m instanceof Map) {
				((Map) m).clear();
			} else if (m instanceof Collection) {
				((Collection) m).clear();
			}
		}

		memory.clear();
		memory2.clear();
	}
	
	@Override
	public void flush() {
		flushMe();
	}


}