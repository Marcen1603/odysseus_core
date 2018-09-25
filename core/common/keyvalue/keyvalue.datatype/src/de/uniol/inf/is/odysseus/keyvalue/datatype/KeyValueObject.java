package de.uniol.inf.is.odysseus.keyvalue.datatype;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import de.undercouch.bson4jackson.BsonFactory;
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.INamedAttributeStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * This class is used to represent objects as key value pairs (similar to JSON).
 *
 * @author Marco Grawunder, Tobias Brandt
 *
 * @param <T>
 */

public class KeyValueObject<T extends IMetaAttribute> extends AbstractStreamObject<T>
		implements Serializable, INamedAttributeStreamObject<T> {

	final private static long serialVersionUID = -94667746890198612L;
	final static private ObjectMapper mapper = new ObjectMapper();
	final static private ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());
	final static private XmlMapper xmlMapper = new XmlMapper();
	final static private ObjectMapper bsonMapper = new ObjectMapper(new BsonFactory());

	///final static private

	final static private JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

	private JsonNode node;
	private Map<String, Object> flat = new TreeMap<>();
	private boolean configured;

	final protected static Logger LOG = LoggerFactory.getLogger(KeyValueObject.class);

	public static KeyValueObject<IMetaAttribute> createInstance() {
		return new KeyValueObject<>();
	}

	public static KeyValueObject<IMetaAttribute> createInstance(Map<String, Object> event) {
		return new KeyValueObject<>(event);
	}

	public static KeyValueObject<IMetaAttribute> createInstance(String json) {
		return new KeyValueObject<>(json);
	}

	public static KeyValueObject<IMetaAttribute> createInstance(KeyValueObject<IMetaAttribute> other) {
		return new KeyValueObject<>(other);
	}

	public static KeyValueObject<IMetaAttribute> createFromXML(InputStream input) {
		KeyValueObject<IMetaAttribute> obj = createInstance();
		obj.initWithXML(input);
		return obj;
	}

	public static KeyValueObject<IMetaAttribute> createFromXML(String input) {
		KeyValueObject<IMetaAttribute> obj = createInstance();
		obj.initWithXML(input);
		return obj;
	}

	// ----------------------------------------------------------------------------------------------------------------

	public KeyValueObject() {
		setNode(nodeFactory.objectNode());
	}

	private KeyValueObject(Map<String, Object> map) {
		ObjectNode node = nodeFactory.objectNode();
		if (map != null && map.size() > 0) {
			for (Entry<String, Object> e : map.entrySet()) {
				node.set(e.getKey(), mapper.convertValue(e.getValue(), JsonNode.class));
			}
		}
		setNode(node);
	}

	private KeyValueObject(String json) {
		try {
			this.node = jsonMapper.reader().readTree(json);
			this.setNode(this.node);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private KeyValueObject(KeyValueObject<T> other) {
		super(other);
		this.setNode(other.node.deepCopy());
	}

	private void setNode(JsonNode node) {
		this.node = node;
		this.toKeyValue(node, this.flat, "");
	}

	private void initWithXML(InputStream input) {
		try {
			this.node = xmlMapper.reader().readTree(input);
			this.setNode(this.node);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void initWithXML(String xml) {
		try {
			this.node = xmlMapper.reader().readTree(xml);
			this.setNode(this.node);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// -----------------------------------------------
	// attribute methods
	// ----------------------------------------------

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getAttribute(String key) {
		return (K) this.flat.get(key);
	}

	public Number getNumberAttribute(String key) {
		return (Number) this.flat.get(key);
	}

	public Map<String, Object> path2(String path) {
		Map<String, Object> res = new TreeMap<>();
		if (path.startsWith("/")) {
			findPath(node, res, "", "." + path.substring(1));
		} else {
			throw new IllegalArgumentException("Currently only pathes starting at the top '/' are allowed");
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> path(String path) {
		if (!configured) {
			Configuration.setDefaults(new Configuration.Defaults() {

				private final JsonProvider jsonProvider = new JacksonJsonNodeJsonProvider();
				private final MappingProvider mappingProvider = new JacksonMappingProvider();

				@Override
				public JsonProvider jsonProvider() {
					return jsonProvider;
				}

				@Override
				public MappingProvider mappingProvider() {
					return mappingProvider;
				}

				@Override
				public Set<Option> options() {
					return EnumSet.noneOf(Option.class);
				}
			});
			configured = true;
		}
		JsonNode res = JsonPath.read(node, path);
		// Result could be an array of elements
		if (res.isArray()) {
			@SuppressWarnings("rawtypes")
			List ret = new ArrayList<>(((ArrayNode) res).size());
			for (JsonNode n : ((ArrayNode) res)) {
				if (n.isArray() || n.isObject()) {
					KeyValueObject<IMetaAttribute> kv = createInstance();
					kv.setNode(n.deepCopy());
					ret.add(kv);
				} else {
					if (n.isNumber()) {
						ret.add(n.numberValue());
					} else {
						ret.add(n.toString());
					}
				}
			}
			return ret;
		}
		// or a single element (Object, Number or String)
		List<Object> ret = new ArrayList<>(1);
		if (res.isObject()) {
			KeyValueObject<IMetaAttribute> kv = createInstance();
			kv.setNode(res);
			ret.add(kv);
		} else if (res.isNumber()) {
			ret.add(res.numberValue());
		} else {
			ret.add(res.textValue());
		}
		return ret;
	}

	private void findPath(JsonNode node, Map<String, Object> result, String path, String searchPath) {
		if (!node.isNull()) {
			if (path.equals(searchPath)) {
				result.put(path.substring(1), node);
			} else {
				// e.g. array inside of array
				if (node.isArray()) {
					for (int i = 0; i < node.size(); i++) {
						findPath(node.get(i), result, path + "[" + i + "]", searchPath);
					}
				} else {
					Iterator<Map.Entry<String, JsonNode>> it = node.fields();
					while (it.hasNext()) {
						Map.Entry<String, JsonNode> entry = it.next();
						JsonNode n = entry.getValue();
						String newPath = path + "." + entry.getKey();
						if (newPath.equals(searchPath)) {
							result.put(newPath.substring(1), n);
						} else {
							if (n.isObject()) {
								findPath(n, result, newPath, searchPath);
							} else if (n.isArray()) {
								for (int i = 0; i < n.size(); i++) {
									findPath(n.get(i), result, newPath + "[" + i + "]", searchPath);
								}
							}
						}
					}
				}
			}
		}
	}

	public final Map<String, Object> getAsKeyValueMap() {
		if (flat.size() == 0) {
			toKeyValue(node, flat, "");
		}
		return flat;
	}

	private void toKeyValue(JsonNode node, Map<String, Object> result, String path) {
		if (!node.isNull()) {
			if (!node.isContainerNode()) {
				// remove first "."
				result.put(path.substring(1), toObject(node));
			} else {
				// e.g. array inside of array
				if (node.isArray()) {
					for (int i = 0; i < node.size(); i++) {
						toKeyValue(node.get(i), result, path + "[" + i + "]");
					}
				} else {
					Iterator<Map.Entry<String, JsonNode>> it = node.fields();
					while (it.hasNext()) {
						Map.Entry<String, JsonNode> entry = it.next();
						JsonNode n = entry.getValue();
						if (n.isObject()) {
							toKeyValue(n, result, path + "." + entry.getKey());
						} else if (n.isArray()) {
							for (int i = 0; i < n.size(); i++) {
								toKeyValue(n.get(i), result, path + "." + entry.getKey() + "[" + i + "]");
							}
						} else {
							String p = path + "." + entry.getKey();
							// remove first "."
							result.put(p.substring(1), toObject(n));
						}
					}
				}
			}
		}
	}

	private Object toObject(JsonNode v) {
		if (v != null) {
			if (v.isNumber()) {
				return v.numberValue();
			}
			return v.asText();
		} else {
			return null;
		}
	}

	public final boolean containsKey(String key) {
		return this.flat.containsKey(key);
	}

	/**
	 * Set the value to given key. If there had been a value already, it will be
	 * overridden.
	 *
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setAttribute(String key, Object value) {
		// Must create hierarchy based on "."!
		String subkeys[] = key.split("\\.");
		JsonNode father = node;
		for (int i = 0; i < subkeys.length; i++) {

			if (i == subkeys.length - 1) {
				if (father.isArray()) {
					((ArrayNode) father).add(mapper.convertValue(value, JsonNode.class));
				} else {
					int start = subkeys[i].indexOf("[") + 1;
					if (start > 0) {
						String lkey = subkeys[i].substring(0, start - 1);
						int pos = Integer.parseInt(subkeys[i].substring(start, subkeys[i].indexOf("]")));
						JsonNode subnode = father.get(lkey);
						if (pos < ((ArrayNode) subnode).size()) {
							((ArrayNode) subnode).set(pos, mapper.convertValue(value, JsonNode.class));
						} else {
							((ArrayNode) subnode).add(mapper.convertValue(value, JsonNode.class));
						}
					} else {
						if (value instanceof KeyValueObject) {
							((ObjectNode) father).set(subkeys[i],
									((KeyValueObject<IMetaAttribute>) value).node.deepCopy());
						} else if (value instanceof Object[]) {
							ArrayNode node = new ArrayNode(nodeFactory);
							if (((Object[]) value)[0] instanceof KeyValueObject) {
								for (Object o : (Object[]) value) {
									processKeyValueObjectSublist(value, node, o);
								}
							} else {
								node = convertToJsonArray(value);
							}
							((ObjectNode) father).set(subkeys[i], node);
						} else if (value instanceof List) {
							ArrayNode node = new ArrayNode(nodeFactory);
							if (((List<Object>)value).size() > 0 && ((List<Object>) value).get(0) instanceof KeyValueObject) {
								for (Object o : (List<Object>) value) {
									processKeyValueObjectSublist(value, node, o);
								}
							} else {
								node = convertToJsonArray(value);
							}
							((ObjectNode) father).set(subkeys[i], node);

						} else {
							((ObjectNode) father).set(subkeys[i], mapper.convertValue(value, JsonNode.class));
						}
					}
				}
			} else {
				int start = subkeys[i].indexOf("[") + 1;
				JsonNode subNode;
				if (start > 0) {
					String lkey = subkeys[i].substring(0, start - 1);
					int pos = Integer.parseInt(subkeys[i].substring(start, subkeys[i].indexOf("]")));
					subNode = father.get(lkey);
					subNode = ((ArrayNode) subNode).get(pos);
				} else {
					subNode = father.get(subkeys[i]);
				}

				if (subNode == null) {
					if (subkeys[i].endsWith("]")) {
						subNode = nodeFactory.arrayNode();
					} else {
						subNode = nodeFactory.objectNode();
					}
					if (father.isArray()) {
						((ArrayNode) father).add(subNode);
					} else {
						((ObjectNode) father).set(subkeys[i], subNode);
					}
				}
				father = subNode;
			}
		}
		this.flat.put(key, value);

	}

	@SuppressWarnings("unchecked")
	private void processKeyValueObjectSublist(Object value, ArrayNode node, Object o) {
		if (o instanceof KeyValueObject) {
			node.add(((KeyValueObject<IMetaAttribute>) o).node.deepCopy());
		} else {
			throw new IllegalArgumentException("This method can only be used with KeyValueObjets!");
		}
	}

	private ArrayNode convertToJsonArray(Object value) {
		try {
			String arrayToJson = jsonMapper.writeValueAsString(value);
			return (ArrayNode) jsonMapper.readTree(arrayToJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object removeAttribute(String key) {
		String[] subkeys = key.split("\\.");
		JsonNode subnode = node;
		for (int i = 0; i < subkeys.length - 1; i++) {

			int start = subkeys[i].indexOf("[") + 1;
			if (start > 0) {
				int pos = Integer.parseInt(subkeys[i].substring(start, subkeys[i].indexOf("]")));
				ArrayNode arrayNode = ((ArrayNode) subnode.get(subkeys[i].substring(0, start - 1)));
				subnode = arrayNode.get(pos);
			} else {
				subnode = subnode.get(subkeys[i]);
			}
		}
		String lastKey = subkeys[subkeys.length - 1];
		if (subnode.isArray()) {
			subnode = subnode.get(lastKey);
			int start = lastKey.indexOf("[") + 1;
			if (start > 0) {
				int pos = Integer.parseInt(lastKey.substring(start, lastKey.indexOf("]")));
				((ArrayNode) subnode).remove(pos);
			} else {
				((ArrayNode) subnode).removeAll();
			}
		} else {

			int start = lastKey.indexOf("[") + 1;
			if (start > 0) {
				subnode = subnode.get(lastKey.substring(0, start - 1));
				int pos = Integer.parseInt(lastKey.substring(start, lastKey.indexOf("]")));
				((ArrayNode) subnode).remove(pos);
			} else {
				((ObjectNode) subnode).remove(lastKey);
			}

		}
		return this.flat.remove(key);
	}

	public int size() {
		return flat.size();
	}

	@Override
	protected IStreamObject<T> process_merge(IStreamObject<T> left, IStreamObject<T> right, Order order) {
		JsonNode m = merge(((KeyValueObject<T>) left).node.deepCopy(), ((KeyValueObject<T>) right).node);
		KeyValueObject<T> merged = new KeyValueObject<T>();
		merged.setNode(m);
		return merged;
	}

	public boolean isEmpty() {
		if (node.size() <= 0) {
			return true;
		} else {
			return false;
		}
	}

	// ---------------------------------
	// output methods
	// ---------------------------------

	@Override
	public String toString() {
		return toString(true);
	}

	@Override
	public String toString(boolean withMetadata) {
		StringBuffer retBuff = new StringBuffer();
		try {
			retBuff.append(mapper.writeValueAsString(node));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if (withMetadata && getMetadata() != null) {
			retBuff.append(";").append(getMetadata().toString());
		}
		return retBuff.toString();
	}

	public String toStringWithNewlines() {
		StringBuffer retBuff = new StringBuffer();
		try {
			retBuff.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retBuff.toString();
	}

	public String getAsXML() {
		try {
			return xmlMapper.writeValueAsString(this.node);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte[] getAsBSON() {
		try {
			return bsonMapper.writeValueAsBytes(node);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	// ------------------------------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		KeyValueObject<T> other = (KeyValueObject<T>) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}

	@Override
	public KeyValueObject<T> clone() {
		return new KeyValueObject<T>(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractStreamObject<T> newInstance() {
		return new KeyValueObject<>();
	}

	// @SuppressWarnings("unchecked")
	// public Map<String, Object> getAttributesAsNestedMap() {
	// Map<String, Object> map = new LinkedHashMap<String, Object>();
	// Map<String, Object> tmpMap;
	// Set<Entry<String, Object>> entrySet = this.attributes.entrySet();
	// for (Entry<String, Object> entry : entrySet) {
	// String[] path = entry.getKey().split("\\.");
	// tmpMap = map;
	// for (int i = 0; i < path.length - 1; i++) {
	// if (tmpMap.get(path[i]) != null) {
	// tmpMap = (Map<String, Object>) tmpMap.get(path[i]);
	// } else {
	// Map<String, Object> newMap = new LinkedHashMap<String, Object>();
	// tmpMap.put(path[i], newMap);
	// tmpMap = newMap;
	// }
	// }
	// tmpMap.put(path[path.length - 1], entry.getValue());
	// }
	// return map;
	// }

	/**
	 * Like normal equals-method but has a tolerance for double and float
	 * comparisons.
	 *
	 * @param o
	 * @return
	 */
	@Override
	public final boolean equalsTolerance(Object o, double tolerance) {
		// TODO: THIS IS VERY EXPENSIVE!!!

		if (!(o instanceof KeyValueObject)) {
			return false;
		}
		KeyValueObject<?> t = (KeyValueObject<?>) o;
		if (this.size() != t.size()) {
			return false;
		}
		Iterator<String> it = this.getAsKeyValueMap().keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Object attr = this.getAttribute(key);
			Object theirAttr = t.getAttribute(key);
			// test if attributes are not null and equal
			// or both null (order is imporantant!)
			if (attr != null) {
				if (attr instanceof Double && theirAttr instanceof Double) {
					if (Math.abs((Double) attr - (Double) theirAttr) > tolerance) {
						return false;
					}
				} else if (attr instanceof Float && theirAttr instanceof Float) {
					if (Math.abs((Float) attr - (Float) theirAttr) > tolerance) {
						return false;
					}
				} else {
					if (!attr.equals(theirAttr)) {
						return false;
					}
				}
			} else {
				if (theirAttr != null) {
					return false;
				}
			}
		}
		return true;
	}

	// ------------------------------------------------------------------------------------------------
	// static helper
	// ------------------------------------------------------------------------------------------------

	/**
	 * Converts a tuple into a keyValue / JSON object
	 *
	 * @param tuple
	 *            The tuple to convert.
	 * @param schema
	 *            The schema of the incoming tuple
	 * @return a keyValue object with the content of the tuple
	 */
	public static KeyValueObject<IMetaAttribute> fromTuple(Tuple<IMetaAttribute> tuple, SDFSchema schema) {
		KeyValueObject<IMetaAttribute> keyValueObject = new KeyValueObject<>();

		if (tuple.getMetadata() != null) {
			// It is possible that tuples do not have meta data (e.g., inner /
			// nested tuples)
			keyValueObject.setMetadata(tuple.getMetadata().clone());
		}

		/*
		 * Go through all attributes of the tuple and put the content into the
		 * KeyValueObject
		 */
		int position = 0;
		for (SDFAttribute a : schema) {

			if (a.getDatatype().isTuple()) {

				// Nested structure (tuple in tuple)
				Tuple<IMetaAttribute> innerTuple = tuple.getAttribute(position);
				KeyValueObject<IMetaAttribute> innerKeyValueObject = KeyValueObject.fromTuple(innerTuple,
						a.getDatatype().getSchema());

				// Add the nested object to the parent
				keyValueObject.setAttribute(a.getAttributeName(), innerKeyValueObject);

			} else {
				// Normal (not nested) attribute
				try {
					keyValueObject.setAttribute(a.getAttributeName(), tuple.getAttribute(position));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			position++;
		}
		return keyValueObject;
	}

	/**
	 * Converts a tuple into a keyValue / JSON object matching a given template.
	 * Attributes which are not used in the template will be ignored.
	 *
	 * @param tuple
	 *            The tuple to convert.
	 * @param schema
	 *            The schema of the tuple
	 * @param template
	 *            Template needs to have variables with <> brackets around them. The
	 *            names of the variables need to match the schema.
	 * @return a keyValue object with the content of the tuple
	 */
	public static KeyValueObject<IMetaAttribute> fromTupleWithTemplate(Tuple<IMetaAttribute> tuple, SDFSchema schema,
			String template) {

		/*
		 * Go through all attributes of the tuple and put the content into the template
		 */
		int position = 0;
		for (SDFAttribute a : schema) {

			if (a.getDatatype().isTuple()) {

				// Nested structure (tuple in tuple)
				Tuple<IMetaAttribute> innerTuple = tuple.getAttribute(position);
				KeyValueObject<IMetaAttribute> innerKeyValueObject = KeyValueObject.fromTuple(innerTuple,
						a.getDatatype().getSchema());
				String keyValueString = innerKeyValueObject.toString();
				template.replaceAll("<" + a.getAttributeName() + ">", keyValueString);

			} else {
				// Normal (not nested) attribute
				try {
					if(tuple.getAttribute(position) instanceof String) {
						template = template.replaceAll("<" + a.getAttributeName() + ">", "\"" + tuple.getAttribute(position) + "\"");
					}else {
						template = template.replaceAll("<" + a.getAttributeName() + ">", "" + tuple.getAttribute(position));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			position++;
		}

		KeyValueObject<IMetaAttribute> keyValueObject = KeyValueObject.createInstance(template);
		if (tuple.getMetadata() != null) {
			// It is possible that tuples do not have meta data (e.g., inner /
			// nested tuples)
			keyValueObject.setMetadata(tuple.getMetadata().clone());
		}

		return keyValueObject;
	}

	static private void parse(JsonNode rootNode, Map<String, Object> map, String path) {
		Iterator<Entry<String, JsonNode>> nodeIterator = rootNode.fields();
		while (nodeIterator.hasNext()) {
			Entry<String, JsonNode> nodeEntry = nodeIterator.next();
			JsonNode node = nodeEntry.getValue();
			String key = nodeEntry.getKey();
			String newPath;
			if (path.equals("")) {
				newPath = key;
			} else {
				newPath = path + "." + key;
			}
			if (node.isArray()) {
				map.putAll(parseArray(node, map, newPath));
			} else {
				if (node.size() > 0) {
					parse(node, map, newPath);
				} else {
					if (node.isInt()) {
						map.put(newPath, node.asInt());
					} else if (node.isTextual()) {
						map.put(newPath, node.asText());
					} else if (node.isBoolean()) {
						map.put(newPath, node.asBoolean());
					} else if (node.isDouble()) {
						map.put(newPath, node.asDouble());
					} else if (node.isLong()) {
						map.put(newPath, node.asLong());
					}
				}
			}
		}
	}

	static private Map<String, Object> parseArray(JsonNode rootNode, Map<String, Object> map, String path) {
		Map<String, Object> resultList = new HashMap<String, Object>();
		Iterator<JsonNode> elements = rootNode.elements();
		int pos = 0;
		while (elements.hasNext()) {
			JsonNode node = elements.next();
			String key = path + "[" + pos + "]";
			if (node.isInt()) {
				resultList.put(key, node.asInt());
			} else if (node.isTextual()) {
				resultList.put(key, node.asText());
			} else if (node.isBoolean()) {
				resultList.put(key, node.asBoolean());
			} else if (node.isDouble()) {
				resultList.put(key, node.asDouble());
			} else if (node.isLong()) {
				resultList.put(key, node.asLong());
			} else if (node.isObject()) {
				Map<String, Object> subMap = new HashMap<String, Object>();
				parse(node, subMap, key);
				resultList.putAll(subMap);
			}
			pos++;
		}
		return resultList;
	}

	private static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {

		Iterator<String> fieldNames = updateNode.fieldNames();
		while (fieldNames.hasNext()) {

			String fieldName = fieldNames.next();
			JsonNode jsonNode = mainNode.get(fieldName);
			// if field exists and is an embedded object
			if (jsonNode != null && jsonNode.isObject()) {
				merge(jsonNode, updateNode.get(fieldName));
			} else {
				if (mainNode instanceof ObjectNode) {
					// Overwrite field
					JsonNode value = updateNode.get(fieldName);
					((ObjectNode) mainNode).set(fieldName, value);
				}
			}

		}

		return mainNode;
	}

}
