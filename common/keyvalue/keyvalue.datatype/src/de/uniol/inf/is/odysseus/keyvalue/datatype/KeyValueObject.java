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
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
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
 * This class is used to represent objects as key value pairs (similar to JSON)
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
	final static private ObjectMapper xmlMapper = new ObjectMapper(new XmlFactory());
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
							if (((List<Object>) value).get(0) instanceof KeyValueObject) {
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
			return xmlMapper.writeValueAsString(node);
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
					template = template.replaceAll("<" + a.getAttributeName() + ">", "" + tuple.getAttribute(position));
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

	/**
	 * For quick testing purposes
	 *
	 * @param args
	 *            Not used.
	 */
	public static void main(String[] args) {
		/// String json1 = "{ \"results\": [ { \"columns\": [ \"path\", \"totalCost\" ],
		/// \"data\": [ { \"row\": [ [ { \"serviceLine\": \"\", \"gtype\": 1, \"town\":
		/// \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/ZOB/BusPlatforms/C\",
		/// \"bbox\": [ 10.891812, 49.893757, 10.891812, 49.893757 ], \"latitude\":
		/// \"49.893757\", \"postalCode\": \"96047\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"geom\":
		/// \"POINT (10.891812 49.893757)\", \"platform\": \"Steig C\", \"mode\":
		/// \"Pedestrian\", \"number\": \"4-6\", \"headingDirection\": \"\", \"street\":
		/// \"Promenadestrasse\", \"stopName\": \"Bamberg, ZOB\", \"longitude\":
		/// \"10.891812 \" }, { \"costWaitingTime\": 5, \"cost\": 9, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 2 }, {
		/// \"scheduleName\": \"Bamberg, ZOB nach Schesslitz, Bahnhof\",
		/// \"serviceLine\": \"MO9000000003\", \"gtype\": 1, \"bbox\": [ 10.891812,
		/// 49.893757, 10.891812, 49.893757 ], \"postalCode\": \"96047\", \"latitude\":
		/// \"49.893757\", \"validFrom\": 20161001, \"geom\": \"POINT (10.891812
		/// 49.893757)\", \"platform\": \"\", \"mode\": \"Carpooling\", \"number\":
		/// \"4-6\", \"headingDirection\": \"Schesslitz, Bahnhof\", \"street\":
		/// \"Promenadestrasse\", \"friday\": [ 735 ], \"wednesday\": [ 735 ],
		/// \"stopName\": \"Bamberg, ZOB\", \"monday\": [ 735 ], \"longitude\":
		/// \"10.891812 \", \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Carpooling/DE/BY/Bamberg/ZOB/MO9000000003\",
		/// \"thursday\": [ 735 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"tuesday\": [
		/// 735 ], \"validUntil\": 20170331, \"comment\": \"\" }, { \"costWaitingTime\":
		/// 0, \"cost\": 26, \"change\": 0, \"costPrice\": 3, \"fareStage\": \"\",
		/// \"costTravelTime\": 20 }, { \"scheduleName\": \"Bamberg, ZOB nach
		/// Schesslitz, Bahnhof\", \"serviceLine\": \"MO9000000003\", \"gtype\": 1,
		/// \"bbox\": [ 11.027739, 49.972342, 11.027739, 49.972342 ], \"postalCode\":
		/// \"96110\", \"latitude\": \"49.972342\", \"validFrom\": 20161001, \"geom\":
		/// \"POINT (11.027739 49.972342)\", \"platform\": \"\", \"mode\":
		/// \"Carpooling\", \"number\": \"6\", \"headingDirection\": \"Schesslitz,
		/// Bahnhof\", \"street\": \"Bamberger Strasse\", \"friday\": [ 755 ],
		/// \"wednesday\": [ 755 ], \"stopName\": \"Schesslitz, Bahnhof\", \"monday\": [
		/// 755 ], \"longitude\": \"11.027739 \", \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Carpooling/DE/BY/Schesslitz/Bahnhof/MO9000000003\",
		/// \"thursday\": [ 755 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"tuesday\": [ 755 ], \"validUntil\": 20170331, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1, \"costPrice\": 0,
		/// \"fareStage\": \"\", \"costTravelTime\": 0 }, { \"serviceLine\": \"\",
		/// \"gtype\": 1, \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Schesslitz/Bahnhof/BusPlatforms/-\",
		/// \"bbox\": [ 11.027739, 49.972342, 11.027739, 49.972342 ], \"latitude\":
		/// \"49.972342\", \"postalCode\": \"96110\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"geom\": \"POINT (11.027739 49.972342)\", \"platform\": \"-\", \"mode\":
		/// \"Pedestrian\", \"number\": \"6\", \"headingDirection\": \"\", \"street\":
		/// \"Bamberger Strasse\", \"stopName\": \"Schesslitz, Bahnhof\", \"longitude\":
		/// \"11.027739 \" } ], 37 ], \"meta\": [ [ { \"id\": 34, \"type\": \"node\",
		/// \"deleted\": false }, { \"id\": 92, \"type\": \"relationship\", \"deleted\":
		/// false }, { \"id\": 46, \"type\": \"node\", \"deleted\": false }, { \"id\":
		/// 93, \"type\": \"relationship\", \"deleted\": false }, { \"id\": 47,
		/// \"type\": \"node\", \"deleted\": false }, { \"id\": 94, \"type\":
		/// \"relationship\", \"deleted\": false }, { \"id\": 38, \"type\": \"node\",
		/// \"deleted\": false } ], null ] }, { \"row\": [ [ { \"serviceLine\": \"\",
		/// \"gtype\": 1, \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/ZOB/BusPlatforms/A\",
		/// \"bbox\": [ 10.891812, 49.893757, 10.891812, 49.893757 ], \"latitude\":
		/// \"49.893757\", \"postalCode\": \"96047\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"geom\":
		/// \"POINT (10.891812 49.893757)\", \"platform\": \"Steig A\", \"mode\":
		/// \"Pedestrian\", \"number\": \"4-6\", \"headingDirection\": \"\", \"street\":
		/// \"Promenadestrasse\", \"stopName\": \"Bamberg, ZOB\", \"longitude\":
		/// \"10.891812 \" }, { \"costWaitingTime\": 5, \"cost\": 7, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0 }, {
		/// \"scheduleName\": \"Bamberg ZOB - Bahnhof - Gartenstadt\", \"serviceLine\":
		/// \"901\", \"saturday\": [ 630, 700, 730, 800, 830, 900, 915, 930, 945, 1000,
		/// 1015, 1030, 1045, 1100, 1115, 1130, 1145, 1200, 1215, 1230, 1245, 1300,
		/// 1315, 1330, 1345, 1400, 1415, 1430, 1445, 1500, 1530, 1600, 1630, 1700,
		/// 1730, 1800, 1830, 1900, 1930 ], \"gtype\": 1, \"bbox\": [ 10.891812,
		/// 49.893757, 10.891812, 49.893757 ], \"postalCode\": \"96047\", \"latitude\":
		/// \"49.893757\", \"validFrom\": 20151213, \"geom\": \"POINT (10.891812
		/// 49.893757)\", \"platform\": \"Steig A\", \"mode\": \"Bus\", \"number\":
		/// \"4-6\", \"headingDirection\": \"Bamberg Gartenstadt\", \"street\":
		/// \"Promenadestrasse\", \"friday\": [ 515, 550, 615, 630, 645, 700, 715, 730,
		/// 745, 800, 815, 830, 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100,
		/// 1115, 1130, 1145, 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400,
		/// 1415, 1430, 1445, 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700,
		/// 1715, 1730, 1745, 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ],
		/// \"wednesday\": [ 515, 550, 615, 630, 645, 700, 715, 730, 745, 800, 815, 830,
		/// 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100, 1115, 1130, 1145,
		/// 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400, 1415, 1430, 1445,
		/// 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700, 1715, 1730, 1745,
		/// 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ], \"stopName\": \"Bamberg,
		/// ZOB\", \"longitude\": \"10.891812 \", \"monday\": [ 515, 550, 615, 630, 645,
		/// 700, 715, 730, 745, 800, 815, 830, 845, 900, 915, 930, 945, 1000, 1015,
		/// 1030, 1045, 1100, 1115, 1130, 1145, 1200, 1215, 1230, 1245, 1300, 1315,
		/// 1330, 1345, 1400, 1415, 1430, 1445, 1500, 1515, 1530, 1545, 1600, 1615,
		/// 1630, 1645, 1700, 1715, 1730, 1745, 1800, 1815, 1830, 1845, 1900, 1915,
		/// 1930, 1945 ], \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/ZOB/BusPlatforms/A/901/Bamberg_Gartenstadt\",
		/// \"thursday\": [ 515, 550, 615, 630, 645, 700, 715, 730, 745, 800, 815, 830,
		/// 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100, 1115, 1130, 1145,
		/// 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400, 1415, 1430, 1445,
		/// 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700, 1715, 1730, 1745,
		/// 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"sunday\": [
		/// 800, 830, 900, 930, 1000, 1030, 1100, 1130, 1200, 1230, 1300, 1330, 1400,
		/// 1430, 1500, 1530, 1600, 1630, 1700, 1730, 1800, 1830, 1900, 1930 ],
		/// \"tuesday\": [ 515, 550, 615, 630, 645, 700, 715, 730, 745, 800, 815, 830,
		/// 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100, 1115, 1130, 1145,
		/// 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400, 1415, 1430, 1445,
		/// 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700, 1715, 1730, 1745,
		/// 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ], \"validUntil\": -1,
		/// \"comment\": \"\" }, { \"costWaitingTime\": 0, \"cost\": 6.2, \"change\": 0,
		/// \"costPrice\": 0, \"fareStage\": \"D\", \"costTravelTime\": 5 }, {
		/// \"scheduleName\": \"Bamberg ZOB - Bahnhof - Gartenstadt\", \"saturday\": [
		/// 635, 705, 735, 805, 835, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105,
		/// 1120, 1135, 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405,
		/// 1420, 1435, 1450, 1505, 1535, 1605, 1635, 1705, 1735, 1805, 1835, 1905, 1935
		/// ], \"serviceLine\": \"901\", \"gtype\": 1, \"bbox\": [ 10.897798, 49.900962,
		/// 10.897798, 49.900962 ], \"postalCode\": \"96052\", \"latitude\":
		/// \"49.900962\", \"validFrom\": 20151213, \"geom\": \"POINT (10.897798
		/// 49.900962)\", \"platform\": \"Steig 5\", \"mode\": \"Bus\", \"number\":
		/// \"31\", \"headingDirection\": \"Bamberg Gartenstadt\", \"street\":
		/// \"Ludwigstrasse\", \"friday\": [ 520, 530, 555, 620, 635, 650, 705, 720,
		/// 735, 750, 805, 820, 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050,
		/// 1105, 1120, 1135, 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350,
		/// 1405, 1420, 1435, 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650,
		/// 1705, 1720, 1735, 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ],
		/// \"wednesday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"stopName\":
		/// \"Bamberg, Bahnhof/Ludwigstrasse\", \"longitude\": \"10.897798 \",
		/// \"monday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"town\":
		/// \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/Bahnhof/BusPlatforms/5/901/Bamberg_Gartenstadt\",
		/// \"thursday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\",
		/// \"sunday\": [ 805, 835, 905, 935, 1005, 1035, 1105, 1135, 1205, 1235, 1305,
		/// 1335, 1405, 1435, 1505, 1535, 1605, 1635, 1705, 1735, 1805, 1835, 1905, 1935
		/// ], \"tuesday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805,
		/// 820, 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"validUntil\": -1,
		/// \"comment\": \"\" }, { \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0 }, {
		/// \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/Bahnhof/BusPlatforms/5\",
		/// \"bbox\": [ 10.897798, 49.900962, 10.897798, 49.900962 ], \"latitude\":
		/// \"49.900962\", \"postalCode\": \"96052\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\", \"geom\":
		/// \"POINT (10.897798 49.900962)\", \"platform\": \"Steig 5\", \"mode\":
		/// \"Pedestrian\", \"number\": \"31\", \"headingDirection\": \"\", \"street\":
		/// \"Ludwigstrasse\", \"stopName\": \"Bamberg, Bahnhof/Ludwigstrasse\",
		/// \"longitude\": \"10.897798 \" }, { \"costWaitingTime\": 0, \"cost\": 1,
		/// \"change\": 0, \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 1
		/// }, { \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\",
		/// \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/Bahnhof/BusPlatforms/6\",
		/// \"bbox\": [ 10.897798, 49.900962, 10.897798, 49.900962 ], \"latitude\":
		/// \"49.900962\", \"postalCode\": \"96052\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\", \"geom\":
		/// \"POINT (10.897798 49.900962)\", \"platform\": \"Steig 6\", \"mode\":
		/// \"Pedestrian\", \"number\": \"31\", \"headingDirection\": \"\", \"street\":
		/// \"Ludwigstrasse\", \"stopName\": \"Bamberg, Bahnhof/Ludwigstrasse\",
		/// \"longitude\": \"10.897798 \" }, { \"costWaitingTime\": 5, \"cost\": 7,
		/// \"change\": 1, \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0
		/// }, { \"scheduleName\": \"Bamberg Stadion - Bahnhof - ZOB\", \"saturday\": [
		/// 650, 720, 750, 820, 850, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120,
		/// 1135, 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420,
		/// 1435, 1450, 1505, 1520, 1550, 1620, 1650, 1720, 1750, 1820, 1850, 1920, 1950
		/// ], \"serviceLine\": \"902\", \"gtype\": 1, \"bbox\": [ 10.897798, 49.900962,
		/// 10.897798, 49.900962 ], \"postalCode\": \"96052\", \"latitude\":
		/// \"49.900962\", \"validFrom\": 20151213, \"geom\": \"POINT (10.897798
		/// 49.900962)\", \"platform\": \"Steig 6\", \"mode\": \"Bus\", \"number\":
		/// \"31\", \"headingDirection\": \"Bamberg ZOB\", \"street\":
		/// \"Ludwigstrasse\", \"friday\": [ 520, 550, 605, 620, 635, 650, 705, 720,
		/// 735, 750, 805, 820, 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050,
		/// 1105, 1120, 1135, 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350,
		/// 1405, 1420, 1435, 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650,
		/// 1705, 1720, 1735, 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ],
		/// \"wednesday\": [ 520, 550, 605, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"stopName\":
		/// \"Bamberg, Bahnhof/Ludwigstrasse\", \"longitude\": \"10.897798 \",
		/// \"monday\": [ 520, 550, 605, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"town\":
		/// \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/Bahnhof/BusPlatforms/6/902/Bamberg_ZOB\",
		/// \"thursday\": [ 520, 550, 605, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\",
		/// \"sunday\": [ 750, 850, 950, 1050, 1150, 1220, 1250, 1320, 1350, 1420, 1450,
		/// 1520, 1550, 1620, 1650, 1720, 1750, 1820, 1850, 1920, 1950 ], \"tuesday\": [
		/// 520, 550, 605, 620, 635, 650, 705, 720, 735, 750, 805, 820, 835, 850, 905,
		/// 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135, 1150, 1205, 1220,
		/// 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435, 1450, 1505, 1520,
		/// 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735, 1750, 1805, 1820,
		/// 1835, 1850, 1905, 1920, 1935, 1950 ], \"validUntil\": -1, \"comment\": \"\"
		/// }, { \"costWaitingTime\": 0, \"cost\": 7.2, \"change\": 0, \"costPrice\": 0,
		/// \"fareStage\": \"D\", \"costTravelTime\": 6 }, { \"scheduleName\": \"Bamberg
		/// Stadion - Bahnhof - ZOB\", \"serviceLine\": \"902\", \"saturday\": [ 656,
		/// 726, 756, 826, 856, 926, 941, 956, 1011, 1026, 1041, 1056, 1111, 1126, 1141,
		/// 1156, 1211, 1226, 1241, 1256, 1311, 1326, 1341, 1356, 1411, 1426, 1441,
		/// 1456, 1511, 1526, 1556, 1626, 1656, 1726, 1756, 1826, 1856, 1926, 1956 ],
		/// \"gtype\": 1, \"bbox\": [ 10.891812, 49.893757, 10.891812, 49.893757 ],
		/// \"postalCode\": \"96047\", \"latitude\": \"49.893757\", \"validFrom\":
		/// 20151213, \"geom\": \"POINT (10.891812 49.893757)\", \"platform\": \"Steig
		/// C\", \"mode\": \"Bus\", \"number\": \"4-6\", \"headingDirection\": \"Bamberg
		/// ZOB\", \"street\": \"Promenadestrasse\", \"friday\": [ 526, 556, 611, 626,
		/// 641, 656, 711, 726, 741, 756, 811, 826, 841, 856, 911, 926, 941, 956, 1011,
		/// 1026, 1041, 1056, 1111, 1126, 1141, 1156, 1211, 1226, 1241, 1256, 1311,
		/// 1326, 1341, 1356, 1411, 1426, 1441, 1456, 1511, 1526, 1541, 1556, 1611,
		/// 1626, 1641, 1656, 1711, 1726, 1741, 1756, 1811, 1826, 1841, 1856, 1911,
		/// 1926, 1941, 1956 ], \"wednesday\": [ 526, 556, 611, 626, 641, 656, 711, 726,
		/// 741, 756, 811, 826, 841, 856, 911, 926, 941, 956, 1011, 1026, 1041, 1056,
		/// 1111, 1126, 1141, 1156, 1211, 1226, 1241, 1256, 1311, 1326, 1341, 1356,
		/// 1411, 1426, 1441, 1456, 1511, 1526, 1541, 1556, 1611, 1626, 1641, 1656,
		/// 1711, 1726, 1741, 1756, 1811, 1826, 1841, 1856, 1911, 1926, 1941, 1956 ],
		/// \"stopName\": \"Bamberg, ZOB\", \"longitude\": \"10.891812 \", \"monday\": [
		/// 526, 556, 611, 626, 641, 656, 711, 726, 741, 756, 811, 826, 841, 856, 911,
		/// 926, 941, 956, 1011, 1026, 1041, 1056, 1111, 1126, 1141, 1156, 1211, 1226,
		/// 1241, 1256, 1311, 1326, 1341, 1356, 1411, 1426, 1441, 1456, 1511, 1526,
		/// 1541, 1556, 1611, 1626, 1641, 1656, 1711, 1726, 1741, 1756, 1811, 1826,
		/// 1841, 1856, 1911, 1926, 1941, 1956 ], \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/ZOB/BusPlatforms/C/902/Bamberg_ZOB\",
		/// \"thursday\": [ 526, 556, 611, 626, 641, 656, 711, 726, 741, 756, 811, 826,
		/// 841, 856, 911, 926, 941, 956, 1011, 1026, 1041, 1056, 1111, 1126, 1141,
		/// 1156, 1211, 1226, 1241, 1256, 1311, 1326, 1341, 1356, 1411, 1426, 1441,
		/// 1456, 1511, 1526, 1541, 1556, 1611, 1626, 1641, 1656, 1711, 1726, 1741,
		/// 1756, 1811, 1826, 1841, 1856, 1911, 1926, 1941, 1956 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"sunday\": [
		/// 756, 856, 956, 1056, 1156, 1226, 1256, 1326, 1356, 1426, 1456, 1526, 1556,
		/// 1626, 1656, 1726, 1756, 1826, 1856, 1926, 1956 ], \"tuesday\": [ 526, 556,
		/// 611, 626, 641, 656, 711, 726, 741, 756, 811, 826, 841, 856, 911, 926, 941,
		/// 956, 1011, 1026, 1041, 1056, 1111, 1126, 1141, 1156, 1211, 1226, 1241, 1256,
		/// 1311, 1326, 1341, 1356, 1411, 1426, 1441, 1456, 1511, 1526, 1541, 1556,
		/// 1611, 1626, 1641, 1656, 1711, 1726, 1741, 1756, 1811, 1826, 1841, 1856,
		/// 1911, 1926, 1941, 1956 ], \"validUntil\": -1, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1, \"costPrice\": 0,
		/// \"fareStage\": \"\", \"costTravelTime\": 0 }, { \"serviceLine\": \"\",
		/// \"gtype\": 1, \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/ZOB/BusPlatforms/C\",
		/// \"bbox\": [ 10.891812, 49.893757, 10.891812, 49.893757 ], \"latitude\":
		/// \"49.893757\", \"postalCode\": \"96047\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"geom\":
		/// \"POINT (10.891812 49.893757)\", \"platform\": \"Steig C\", \"mode\":
		/// \"Pedestrian\", \"number\": \"4-6\", \"headingDirection\": \"\", \"street\":
		/// \"Promenadestrasse\", \"stopName\": \"Bamberg, ZOB\", \"longitude\":
		/// \"10.891812 \" }, { \"costWaitingTime\": 5, \"cost\": 9, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 2 }, {
		/// \"scheduleName\": \"Bamberg, ZOB nach Schesslitz, Bahnhof\",
		/// \"serviceLine\": \"MO9000000003\", \"gtype\": 1, \"bbox\": [ 10.891812,
		/// 49.893757, 10.891812, 49.893757 ], \"postalCode\": \"96047\", \"latitude\":
		/// \"49.893757\", \"validFrom\": 20161001, \"geom\": \"POINT (10.891812
		/// 49.893757)\", \"platform\": \"\", \"mode\": \"Carpooling\", \"number\":
		/// \"4-6\", \"headingDirection\": \"Schesslitz, Bahnhof\", \"street\":
		/// \"Promenadestrasse\", \"friday\": [ 735 ], \"wednesday\": [ 735 ],
		/// \"stopName\": \"Bamberg, ZOB\", \"monday\": [ 735 ], \"longitude\":
		/// \"10.891812 \", \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Carpooling/DE/BY/Bamberg/ZOB/MO9000000003\",
		/// \"thursday\": [ 735 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"tuesday\": [
		/// 735 ], \"validUntil\": 20170331, \"comment\": \"\" }, { \"costWaitingTime\":
		/// 0, \"cost\": 26, \"change\": 0, \"costPrice\": 3, \"fareStage\": \"\",
		/// \"costTravelTime\": 20 }, { \"scheduleName\": \"Bamberg, ZOB nach
		/// Schesslitz, Bahnhof\", \"serviceLine\": \"MO9000000003\", \"gtype\": 1,
		/// \"bbox\": [ 11.027739, 49.972342, 11.027739, 49.972342 ], \"postalCode\":
		/// \"96110\", \"latitude\": \"49.972342\", \"validFrom\": 20161001, \"geom\":
		/// \"POINT (11.027739 49.972342)\", \"platform\": \"\", \"mode\":
		/// \"Carpooling\", \"number\": \"6\", \"headingDirection\": \"Schesslitz,
		/// Bahnhof\", \"street\": \"Bamberger Strasse\", \"friday\": [ 755 ],
		/// \"wednesday\": [ 755 ], \"stopName\": \"Schesslitz, Bahnhof\", \"monday\": [
		/// 755 ], \"longitude\": \"11.027739 \", \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Carpooling/DE/BY/Schesslitz/Bahnhof/MO9000000003\",
		/// \"thursday\": [ 755 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"tuesday\": [ 755 ], \"validUntil\": 20170331, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1, \"costPrice\": 0,
		/// \"fareStage\": \"\", \"costTravelTime\": 0 }, { \"serviceLine\": \"\",
		/// \"gtype\": 1, \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Schesslitz/Bahnhof/BusPlatforms/-\",
		/// \"bbox\": [ 11.027739, 49.972342, 11.027739, 49.972342 ], \"latitude\":
		/// \"49.972342\", \"postalCode\": \"96110\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"geom\": \"POINT (11.027739 49.972342)\", \"platform\": \"-\", \"mode\":
		/// \"Pedestrian\", \"number\": \"6\", \"headingDirection\": \"\", \"street\":
		/// \"Bamberger Strasse\", \"stopName\": \"Schesslitz, Bahnhof\", \"longitude\":
		/// \"11.027739 \" } ], 69.4 ], \"meta\": [ [ { \"id\": 33, \"type\": \"node\",
		/// \"deleted\": false }, { \"id\": 48, \"type\": \"relationship\", \"deleted\":
		/// false }, { \"id\": 5, \"type\": \"node\", \"deleted\": false }, { \"id\":
		/// 49, \"type\": \"relationship\", \"deleted\": false }, { \"id\": 4, \"type\":
		/// \"node\", \"deleted\": false }, { \"id\": 50, \"type\": \"relationship\",
		/// \"deleted\": false }, { \"id\": 30, \"type\": \"node\", \"deleted\": false
		/// }, { \"id\": 118, \"type\": \"relationship\", \"deleted\": false }, {
		/// \"id\": 31, \"type\": \"node\", \"deleted\": false }, { \"id\": 80,
		/// \"type\": \"relationship\", \"deleted\": false }, { \"id\": 10, \"type\":
		/// \"node\", \"deleted\": false }, { \"id\": 81, \"type\": \"relationship\",
		/// \"deleted\": false }, { \"id\": 11, \"type\": \"node\", \"deleted\": false
		/// }, { \"id\": 82, \"type\": \"relationship\", \"deleted\": false }, { \"id\":
		/// 34, \"type\": \"node\", \"deleted\": false }, { \"id\": 92, \"type\":
		/// \"relationship\", \"deleted\": false }, { \"id\": 46, \"type\": \"node\",
		/// \"deleted\": false }, { \"id\": 93, \"type\": \"relationship\", \"deleted\":
		/// false }, { \"id\": 47, \"type\": \"node\", \"deleted\": false }, { \"id\":
		/// 94, \"type\": \"relationship\", \"deleted\": false }, { \"id\": 38,
		/// \"type\": \"node\", \"deleted\": false } ], null ] }, { \"row\": [ [ {
		/// \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/ZOB/BusPlatforms/A\",
		/// \"bbox\": [ 10.891812, 49.893757, 10.891812, 49.893757 ], \"latitude\":
		/// \"49.893757\", \"postalCode\": \"96047\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"geom\":
		/// \"POINT (10.891812 49.893757)\", \"platform\": \"Steig A\", \"mode\":
		/// \"Pedestrian\", \"number\": \"4-6\", \"headingDirection\": \"\", \"street\":
		/// \"Promenadestrasse\", \"stopName\": \"Bamberg, ZOB\", \"longitude\":
		/// \"10.891812 \" }, { \"costWaitingTime\": 5, \"cost\": 7, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0 }, {
		/// \"scheduleName\": \"Bamberg ZOB - Bahnhof - Gartenstadt\", \"serviceLine\":
		/// \"901\", \"saturday\": [ 630, 700, 730, 800, 830, 900, 915, 930, 945, 1000,
		/// 1015, 1030, 1045, 1100, 1115, 1130, 1145, 1200, 1215, 1230, 1245, 1300,
		/// 1315, 1330, 1345, 1400, 1415, 1430, 1445, 1500, 1530, 1600, 1630, 1700,
		/// 1730, 1800, 1830, 1900, 1930 ], \"gtype\": 1, \"bbox\": [ 10.891812,
		/// 49.893757, 10.891812, 49.893757 ], \"postalCode\": \"96047\", \"latitude\":
		/// \"49.893757\", \"validFrom\": 20151213, \"geom\": \"POINT (10.891812
		/// 49.893757)\", \"platform\": \"Steig A\", \"mode\": \"Bus\", \"number\":
		/// \"4-6\", \"headingDirection\": \"Bamberg Gartenstadt\", \"street\":
		/// \"Promenadestrasse\", \"friday\": [ 515, 550, 615, 630, 645, 700, 715, 730,
		/// 745, 800, 815, 830, 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100,
		/// 1115, 1130, 1145, 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400,
		/// 1415, 1430, 1445, 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700,
		/// 1715, 1730, 1745, 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ],
		/// \"wednesday\": [ 515, 550, 615, 630, 645, 700, 715, 730, 745, 800, 815, 830,
		/// 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100, 1115, 1130, 1145,
		/// 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400, 1415, 1430, 1445,
		/// 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700, 1715, 1730, 1745,
		/// 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ], \"stopName\": \"Bamberg,
		/// ZOB\", \"longitude\": \"10.891812 \", \"monday\": [ 515, 550, 615, 630, 645,
		/// 700, 715, 730, 745, 800, 815, 830, 845, 900, 915, 930, 945, 1000, 1015,
		/// 1030, 1045, 1100, 1115, 1130, 1145, 1200, 1215, 1230, 1245, 1300, 1315,
		/// 1330, 1345, 1400, 1415, 1430, 1445, 1500, 1515, 1530, 1545, 1600, 1615,
		/// 1630, 1645, 1700, 1715, 1730, 1745, 1800, 1815, 1830, 1845, 1900, 1915,
		/// 1930, 1945 ], \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/ZOB/BusPlatforms/A/901/Bamberg_Gartenstadt\",
		/// \"thursday\": [ 515, 550, 615, 630, 645, 700, 715, 730, 745, 800, 815, 830,
		/// 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100, 1115, 1130, 1145,
		/// 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400, 1415, 1430, 1445,
		/// 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700, 1715, 1730, 1745,
		/// 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"sunday\": [
		/// 800, 830, 900, 930, 1000, 1030, 1100, 1130, 1200, 1230, 1300, 1330, 1400,
		/// 1430, 1500, 1530, 1600, 1630, 1700, 1730, 1800, 1830, 1900, 1930 ],
		/// \"tuesday\": [ 515, 550, 615, 630, 645, 700, 715, 730, 745, 800, 815, 830,
		/// 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100, 1115, 1130, 1145,
		/// 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400, 1415, 1430, 1445,
		/// 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700, 1715, 1730, 1745,
		/// 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ], \"validUntil\": -1,
		/// \"comment\": \"\" }, { \"costWaitingTime\": 0, \"cost\": 6.2, \"change\": 0,
		/// \"costPrice\": 0, \"fareStage\": \"D\", \"costTravelTime\": 5 }, {
		/// \"scheduleName\": \"Bamberg ZOB - Bahnhof - Gartenstadt\", \"saturday\": [
		/// 635, 705, 735, 805, 835, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105,
		/// 1120, 1135, 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405,
		/// 1420, 1435, 1450, 1505, 1535, 1605, 1635, 1705, 1735, 1805, 1835, 1905, 1935
		/// ], \"serviceLine\": \"901\", \"gtype\": 1, \"bbox\": [ 10.897798, 49.900962,
		/// 10.897798, 49.900962 ], \"postalCode\": \"96052\", \"latitude\":
		/// \"49.900962\", \"validFrom\": 20151213, \"geom\": \"POINT (10.897798
		/// 49.900962)\", \"platform\": \"Steig 5\", \"mode\": \"Bus\", \"number\":
		/// \"31\", \"headingDirection\": \"Bamberg Gartenstadt\", \"street\":
		/// \"Ludwigstrasse\", \"friday\": [ 520, 530, 555, 620, 635, 650, 705, 720,
		/// 735, 750, 805, 820, 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050,
		/// 1105, 1120, 1135, 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350,
		/// 1405, 1420, 1435, 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650,
		/// 1705, 1720, 1735, 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ],
		/// \"wednesday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"stopName\":
		/// \"Bamberg, Bahnhof/Ludwigstrasse\", \"longitude\": \"10.897798 \",
		/// \"monday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"town\":
		/// \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/Bahnhof/BusPlatforms/5/901/Bamberg_Gartenstadt\",
		/// \"thursday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\",
		/// \"sunday\": [ 805, 835, 905, 935, 1005, 1035, 1105, 1135, 1205, 1235, 1305,
		/// 1335, 1405, 1435, 1505, 1535, 1605, 1635, 1705, 1735, 1805, 1835, 1905, 1935
		/// ], \"tuesday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805,
		/// 820, 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"validUntil\": -1,
		/// \"comment\": \"\" }, { \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0 }, {
		/// \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/Bahnhof/BusPlatforms/5\",
		/// \"bbox\": [ 10.897798, 49.900962, 10.897798, 49.900962 ], \"latitude\":
		/// \"49.900962\", \"postalCode\": \"96052\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\", \"geom\":
		/// \"POINT (10.897798 49.900962)\", \"platform\": \"Steig 5\", \"mode\":
		/// \"Pedestrian\", \"number\": \"31\", \"headingDirection\": \"\", \"street\":
		/// \"Ludwigstrasse\", \"stopName\": \"Bamberg, Bahnhof/Ludwigstrasse\",
		/// \"longitude\": \"10.897798 \" }, { \"costWaitingTime\": 0, \"cost\": 4,
		/// \"change\": 0, \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 4
		/// }, { \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\",
		/// \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/Bahnhof/BusPlatforms/4\",
		/// \"bbox\": [ 10.899026, 49.900589, 10.899026, 49.900589 ], \"latitude\":
		/// \"49.900589\", \"postalCode\": \"96052\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\", \"geom\":
		/// \"POINT (10.899026 49.900589)\", \"platform\": \"Steig 4\", \"mode\":
		/// \"Pedestrian\", \"number\": \"6\", \"headingDirection\": \"\", \"street\":
		/// \"Ludwigstrasse\", \"stopName\": \"Bamberg, Bahnhof/Vorplatz\",
		/// \"longitude\": \"10.899026 \" }, { \"costWaitingTime\": 5, \"cost\": 7,
		/// \"change\": 1, \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0
		/// }, { \"scheduleName\": \"Bamberg - Schesslitz - Wuergau\", \"serviceLine\":
		/// \"963\", \"saturday\": [ 735, 930, 1640 ], \"gtype\": 1, \"bbox\": [
		/// 10.899026, 49.900589, 10.899026, 49.900589 ], \"postalCode\": \"96052\",
		/// \"latitude\": \"49.900589\", \"validFrom\": 20151213, \"geom\": \"POINT
		/// (10.899026 49.900589)\", \"platform\": \"Steig 4\", \"mode\": \"Bus\",
		/// \"number\": \"6\", \"headingDirection\": \"Schesslitz\", \"street\":
		/// \"Ludwigstrasse\", \"friday\": [ 620, 729, 930, 1117, 1245, 1320, 1358,
		/// 1535, 1618, 1858 ], \"wednesday\": [ 620, 729, 930, 1117, 1245, 1320, 1358,
		/// 1535, 1618, 1858 ], \"stopName\": \"Bamberg, Bahnhof/Vorplatz\",
		/// \"longitude\": \"10.899026 \", \"monday\": [ 620, 729, 930, 1117, 1245,
		/// 1320, 1358, 1535, 1618, 1858 ], \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/Bahnhof/BusPlatforms/4/963/Schesslitz\",
		/// \"thursday\": [ 620, 729, 930, 1117, 1245, 1320, 1358, 1535, 1618, 1858 ],
		/// \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\",
		/// \"sunday\": [ 1040, 1530 ], \"tuesday\": [ 620, 729, 930, 1117, 1245, 1320,
		/// 1358, 1535, 1618, 1858 ], \"validUntil\": -1, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 20, \"change\": 0, \"costPrice\": 0,
		/// \"fareStage\": \"3\", \"costTravelTime\": 20 }, { \"scheduleName\":
		/// \"Bamberg - Schesslitz - Wuergau\", \"serviceLine\": \"963\", \"saturday\":
		/// [ 756, 949, 1702 ], \"gtype\": 1, \"bbox\": [ 11.027739, 49.972342,
		/// 11.027739, 49.972342 ], \"postalCode\": \"96110\", \"latitude\":
		/// \"49.972342\", \"validFrom\": 20151213, \"geom\": \"POINT (11.027739
		/// 49.972342)\", \"platform\": \"-\", \"mode\": \"Bus\", \"number\": \"6\",
		/// \"headingDirection\": \"Schesslitz\", \"street\": \"Bamberger Strasse\",
		/// \"friday\": [ 640, 748, 951, 1136, 1259, 1338, 1419, 1554, 1636, 1914 ],
		/// \"wednesday\": [ 640, 748, 951, 1136, 1259, 1338, 1419, 1554, 1636, 1914 ],
		/// \"stopName\": \"Schesslitz, Bahnhof\", \"longitude\": \"11.027739 \",
		/// \"monday\": [ 640, 748, 951, 1136, 1259, 1338, 1419, 1554, 1636, 1914 ],
		/// \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Schesslitz/Bahnhof/BusPlatforms/-/963/Schesslitz\",
		/// \"thursday\": [ 640, 748, 951, 1136, 1259, 1338, 1419, 1554, 1636, 1914 ],
		/// \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"sunday\": [ 1103, 1553 ], \"tuesday\": [ 640, 748, 951, 1136, 1259, 1338,
		/// 1419, 1554, 1636, 1914 ], \"validUntil\": -1, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1, \"costPrice\": 0,
		/// \"fareStage\": \"\", \"costTravelTime\": 0 }, { \"serviceLine\": \"\",
		/// \"gtype\": 1, \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Schesslitz/Bahnhof/BusPlatforms/-\",
		/// \"bbox\": [ 11.027739, 49.972342, 11.027739, 49.972342 ], \"latitude\":
		/// \"49.972342\", \"postalCode\": \"96110\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"geom\": \"POINT (11.027739 49.972342)\", \"platform\": \"-\", \"mode\":
		/// \"Pedestrian\", \"number\": \"6\", \"headingDirection\": \"\", \"street\":
		/// \"Bamberger Strasse\", \"stopName\": \"Schesslitz, Bahnhof\", \"longitude\":
		/// \"11.027739 \" } ], 48.2 ], \"meta\": [ [ { \"id\": 33, \"type\": \"node\",
		/// \"deleted\": false }, { \"id\": 48, \"type\": \"relationship\", \"deleted\":
		/// false }, { \"id\": 5, \"type\": \"node\", \"deleted\": false }, { \"id\":
		/// 49, \"type\": \"relationship\", \"deleted\": false }, { \"id\": 4, \"type\":
		/// \"node\", \"deleted\": false }, { \"id\": 50, \"type\": \"relationship\",
		/// \"deleted\": false }, { \"id\": 30, \"type\": \"node\", \"deleted\": false
		/// }, { \"id\": 117, \"type\": \"relationship\", \"deleted\": false }, {
		/// \"id\": 32, \"type\": \"node\", \"deleted\": false }, { \"id\": 74,
		/// \"type\": \"relationship\", \"deleted\": false }, { \"id\": 16, \"type\":
		/// \"node\", \"deleted\": false }, { \"id\": 75, \"type\": \"relationship\",
		/// \"deleted\": false }, { \"id\": 17, \"type\": \"node\", \"deleted\": false
		/// }, { \"id\": 76, \"type\": \"relationship\", \"deleted\": false }, { \"id\":
		/// 38, \"type\": \"node\", \"deleted\": false } ], null ] }, { \"row\": [ [ {
		/// \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/ZOB/BusPlatforms/C\",
		/// \"bbox\": [ 10.891812, 49.893757, 10.891812, 49.893757 ], \"latitude\":
		/// \"49.893757\", \"postalCode\": \"96047\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"geom\":
		/// \"POINT (10.891812 49.893757)\", \"platform\": \"Steig C\", \"mode\":
		/// \"Pedestrian\", \"number\": \"4-6\", \"headingDirection\": \"\", \"street\":
		/// \"Promenadestrasse\", \"stopName\": \"Bamberg, ZOB\", \"longitude\":
		/// \"10.891812 \" }, { \"costWaitingTime\": 5, \"cost\": 7, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0 }, {
		/// \"scheduleName\": \"Bamberg ZOB - Bahnhof - Stadion\", \"serviceLine\":
		/// \"902\", \"saturday\": [ 619, 649, 719, 749, 819, 849, 905, 919, 935, 949,
		/// 1005, 1019, 1035, 1049, 1105, 1119, 1135, 1149, 1205, 1219, 1235, 1249,
		/// 1305, 1319, 1335, 1349, 1405, 1419, 1435, 1449, 1519, 1549, 1619, 1649,
		/// 1719, 1749, 1819, 1849, 1919, 1949 ], \"gtype\": 1, \"bbox\": [ 10.891812,
		/// 49.893757, 10.891812, 49.893757 ], \"postalCode\": \"96047\", \"latitude\":
		/// \"49.893757\", \"validFrom\": 20151213, \"geom\": \"POINT (10.891812
		/// 49.893757)\", \"platform\": \"Steig C\", \"mode\": \"Bus\", \"number\":
		/// \"4-6\", \"headingDirection\": \"Bamberg Kastanienstr.\", \"street\":
		/// \"Promenadestrasse\", \"friday\": [ 534, 604, 619, 634, 649, 704, 719, 734,
		/// 749, 804, 819, 834, 849, 904, 919, 934, 949, 1004, 1019, 1034, 1049, 1104,
		/// 1119, 1134, 1149, 1204, 1219, 1234, 1249, 1304, 1319, 1334, 1349, 1404,
		/// 1419, 1434, 1449, 1504, 1519, 1534, 1549, 1604, 1619, 1634, 1649, 1704,
		/// 1719, 1734, 1749, 1804, 1819, 1834, 1849, 1904, 1919, 1934, 1949 ],
		/// \"wednesday\": [ 534, 604, 619, 634, 649, 704, 719, 734, 749, 804, 819, 834,
		/// 849, 904, 919, 934, 949, 1004, 1019, 1034, 1049, 1104, 1119, 1134, 1149,
		/// 1204, 1219, 1234, 1249, 1304, 1319, 1334, 1349, 1404, 1419, 1434, 1449,
		/// 1504, 1519, 1534, 1549, 1604, 1619, 1634, 1649, 1704, 1719, 1734, 1749,
		/// 1804, 1819, 1834, 1849, 1904, 1919, 1934, 1949 ], \"stopName\": \"Bamberg,
		/// ZOB\", \"longitude\": \"10.891812 \", \"monday\": [ 534, 604, 619, 634, 649,
		/// 704, 719, 734, 749, 804, 819, 834, 849, 904, 919, 934, 949, 1004, 1019,
		/// 1034, 1049, 1104, 1119, 1134, 1149, 1204, 1219, 1234, 1249, 1304, 1319,
		/// 1334, 1349, 1404, 1419, 1434, 1449, 1504, 1519, 1534, 1549, 1604, 1619,
		/// 1634, 1649, 1704, 1719, 1734, 1749, 1804, 1819, 1834, 1849, 1904, 1919,
		/// 1934, 1949 ], \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/ZOB/BusPlatforms/C/902/Bamberg_Kastanienstr\",
		/// \"thursday\": [ 534, 604, 619, 634, 649, 704, 719, 734, 749, 804, 819, 834,
		/// 849, 904, 919, 934, 949, 1004, 1019, 1034, 1049, 1104, 1119, 1134, 1149,
		/// 1204, 1219, 1234, 1249, 1304, 1319, 1334, 1349, 1404, 1419, 1434, 1449,
		/// 1504, 1519, 1534, 1549, 1604, 1619, 1634, 1649, 1704, 1719, 1734, 1749,
		/// 1804, 1819, 1834, 1849, 1904, 1919, 1934, 1949 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"sunday\": [
		/// 819, 919, 1019, 1119, 1150, 1219, 1250, 1319, 1350, 1419, 1450, 1519, 1550,
		/// 1619, 1650, 1719, 1750, 1819, 1850, 1919, 1950 ], \"tuesday\": [ 534, 604,
		/// 619, 634, 649, 704, 719, 734, 749, 804, 819, 834, 849, 904, 919, 934, 949,
		/// 1004, 1019, 1034, 1049, 1104, 1119, 1134, 1149, 1204, 1219, 1234, 1249,
		/// 1304, 1319, 1334, 1349, 1404, 1419, 1434, 1449, 1504, 1519, 1534, 1549,
		/// 1604, 1619, 1634, 1649, 1704, 1719, 1734, 1749, 1804, 1819, 1834, 1849,
		/// 1904, 1919, 1934, 1949 ], \"validUntil\": -1, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 6.2, \"change\": 0, \"costPrice\": 0,
		/// \"fareStage\": \"D\", \"costTravelTime\": 5 }, { \"scheduleName\": \"Bamberg
		/// ZOB - Bahnhof - Stadion\", \"saturday\": [ 624, 654, 724, 754, 824, 854,
		/// 910, 924, 940, 954, 1010, 1024, 1040, 1054, 1110, 1124, 1140, 1154, 1210,
		/// 1224, 1240, 1254, 1310, 1324, 1340, 1354, 1410, 1424, 1440, 1454, 1524,
		/// 1554, 1624, 1654, 1724, 1754, 1824, 1854, 1924, 1954 ], \"serviceLine\":
		/// \"902\", \"gtype\": 1, \"bbox\": [ 10.897798, 49.900962, 10.897798,
		/// 49.900962 ], \"postalCode\": \"96052\", \"latitude\": \"49.900962\",
		/// \"validFrom\": 20151213, \"geom\": \"POINT (10.897798 49.900962)\",
		/// \"platform\": \"Steig 5\", \"mode\": \"Bus\", \"number\": \"31\",
		/// \"headingDirection\": \"Bamberg Kastanienstr.\", \"street\":
		/// \"Ludwigstrasse\", \"friday\": [ 539, 554, 609, 624, 639, 654, 709, 724,
		/// 739, 754, 809, 824, 839, 854, 909, 924, 939, 954, 1009, 1024, 1039, 1054,
		/// 1109, 1124, 1139, 1154, 1209, 1224, 1239, 1254, 1309, 1324, 1339, 1354,
		/// 1409, 1424, 1439, 1454, 1509, 1524, 1539, 1554, 1609, 1624, 1639, 1654,
		/// 1709, 1724, 1739, 1754, 1809, 1824, 1839, 1854, 1009, 1024, 1039, 1054 ],
		/// \"wednesday\": [ 539, 554, 609, 624, 639, 654, 709, 724, 739, 754, 809, 824,
		/// 839, 854, 909, 924, 939, 954, 1009, 1024, 1039, 1054, 1109, 1124, 1139,
		/// 1154, 1209, 1224, 1239, 1254, 1309, 1324, 1339, 1354, 1409, 1424, 1439,
		/// 1454, 1509, 1524, 1539, 1554, 1609, 1624, 1639, 1654, 1709, 1724, 1739,
		/// 1754, 1809, 1824, 1839, 1854, 1009, 1024, 1039, 1054 ], \"stopName\":
		/// \"Bamberg, Bahnhof/Ludwigstrasse\", \"longitude\": \"10.897798 \",
		/// \"monday\": [ 539, 554, 609, 624, 639, 654, 709, 724, 739, 754, 809, 824,
		/// 839, 854, 909, 924, 939, 954, 1009, 1024, 1039, 1054, 1109, 1124, 1139,
		/// 1154, 1209, 1224, 1239, 1254, 1309, 1324, 1339, 1354, 1409, 1424, 1439,
		/// 1454, 1509, 1524, 1539, 1554, 1609, 1624, 1639, 1654, 1709, 1724, 1739,
		/// 1754, 1809, 1824, 1839, 1854, 1009, 1024, 1039, 1054 ], \"town\":
		/// \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/Bahnhof/BusPlatforms/5/902/Bamberg_Kastanienstr\",
		/// \"thursday\": [ 539, 554, 609, 624, 639, 654, 709, 724, 739, 754, 809, 824,
		/// 839, 854, 909, 924, 939, 954, 1009, 1024, 1039, 1054, 1109, 1124, 1139,
		/// 1154, 1209, 1224, 1239, 1254, 1309, 1324, 1339, 1354, 1409, 1424, 1439,
		/// 1454, 1509, 1524, 1539, 1554, 1609, 1624, 1639, 1654, 1709, 1724, 1739,
		/// 1754, 1809, 1824, 1839, 1854, 1009, 1024, 1039, 1054 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\",
		/// \"sunday\": [ 824, 924, 1024, 1119, 1155, 1224, 1255, 1324, 1355, 1424,
		/// 1455, 1524, 1555, 1624, 1655, 1724, 1755, 1824, 1855, 1924, 1955 ],
		/// \"tuesday\": [ 539, 554, 609, 624, 639, 654, 709, 724, 739, 754, 809, 824,
		/// 839, 854, 909, 924, 939, 954, 1009, 1024, 1039, 1054, 1109, 1124, 1139,
		/// 1154, 1209, 1224, 1239, 1254, 1309, 1324, 1339, 1354, 1409, 1424, 1439,
		/// 1454, 1509, 1524, 1539, 1554, 1609, 1624, 1639, 1654, 1709, 1724, 1739,
		/// 1754, 1809, 1824, 1839, 1854, 1009, 1024, 1039, 1054 ], \"validUntil\": -1,
		/// \"comment\": \"\" }, { \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0 }, {
		/// \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/Bahnhof/BusPlatforms/5\",
		/// \"bbox\": [ 10.897798, 49.900962, 10.897798, 49.900962 ], \"latitude\":
		/// \"49.900962\", \"postalCode\": \"96052\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\", \"geom\":
		/// \"POINT (10.897798 49.900962)\", \"platform\": \"Steig 5\", \"mode\":
		/// \"Pedestrian\", \"number\": \"31\", \"headingDirection\": \"\", \"street\":
		/// \"Ludwigstrasse\", \"stopName\": \"Bamberg, Bahnhof/Ludwigstrasse\",
		/// \"longitude\": \"10.897798 \" }, { \"costWaitingTime\": 0, \"cost\": 4,
		/// \"change\": 0, \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 4
		/// }, { \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\",
		/// \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/Bahnhof/BusPlatforms/4\",
		/// \"bbox\": [ 10.899026, 49.900589, 10.899026, 49.900589 ], \"latitude\":
		/// \"49.900589\", \"postalCode\": \"96052\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\", \"geom\":
		/// \"POINT (10.899026 49.900589)\", \"platform\": \"Steig 4\", \"mode\":
		/// \"Pedestrian\", \"number\": \"6\", \"headingDirection\": \"\", \"street\":
		/// \"Ludwigstrasse\", \"stopName\": \"Bamberg, Bahnhof/Vorplatz\",
		/// \"longitude\": \"10.899026 \" }, { \"costWaitingTime\": 5, \"cost\": 7,
		/// \"change\": 1, \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0
		/// }, { \"scheduleName\": \"Bamberg - Schesslitz - Wuergau\", \"serviceLine\":
		/// \"963\", \"saturday\": [ 735, 930, 1640 ], \"gtype\": 1, \"bbox\": [
		/// 10.899026, 49.900589, 10.899026, 49.900589 ], \"postalCode\": \"96052\",
		/// \"latitude\": \"49.900589\", \"validFrom\": 20151213, \"geom\": \"POINT
		/// (10.899026 49.900589)\", \"platform\": \"Steig 4\", \"mode\": \"Bus\",
		/// \"number\": \"6\", \"headingDirection\": \"Schesslitz\", \"street\":
		/// \"Ludwigstrasse\", \"friday\": [ 620, 729, 930, 1117, 1245, 1320, 1358,
		/// 1535, 1618, 1858 ], \"wednesday\": [ 620, 729, 930, 1117, 1245, 1320, 1358,
		/// 1535, 1618, 1858 ], \"stopName\": \"Bamberg, Bahnhof/Vorplatz\",
		/// \"longitude\": \"10.899026 \", \"monday\": [ 620, 729, 930, 1117, 1245,
		/// 1320, 1358, 1535, 1618, 1858 ], \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/Bahnhof/BusPlatforms/4/963/Schesslitz\",
		/// \"thursday\": [ 620, 729, 930, 1117, 1245, 1320, 1358, 1535, 1618, 1858 ],
		/// \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\",
		/// \"sunday\": [ 1040, 1530 ], \"tuesday\": [ 620, 729, 930, 1117, 1245, 1320,
		/// 1358, 1535, 1618, 1858 ], \"validUntil\": -1, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 20, \"change\": 0, \"costPrice\": 0,
		/// \"fareStage\": \"3\", \"costTravelTime\": 20 }, { \"scheduleName\":
		/// \"Bamberg - Schesslitz - Wuergau\", \"serviceLine\": \"963\", \"saturday\":
		/// [ 756, 949, 1702 ], \"gtype\": 1, \"bbox\": [ 11.027739, 49.972342,
		/// 11.027739, 49.972342 ], \"postalCode\": \"96110\", \"latitude\":
		/// \"49.972342\", \"validFrom\": 20151213, \"geom\": \"POINT (11.027739
		/// 49.972342)\", \"platform\": \"-\", \"mode\": \"Bus\", \"number\": \"6\",
		/// \"headingDirection\": \"Schesslitz\", \"street\": \"Bamberger Strasse\",
		/// \"friday\": [ 640, 748, 951, 1136, 1259, 1338, 1419, 1554, 1636, 1914 ],
		/// \"wednesday\": [ 640, 748, 951, 1136, 1259, 1338, 1419, 1554, 1636, 1914 ],
		/// \"stopName\": \"Schesslitz, Bahnhof\", \"longitude\": \"11.027739 \",
		/// \"monday\": [ 640, 748, 951, 1136, 1259, 1338, 1419, 1554, 1636, 1914 ],
		/// \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Schesslitz/Bahnhof/BusPlatforms/-/963/Schesslitz\",
		/// \"thursday\": [ 640, 748, 951, 1136, 1259, 1338, 1419, 1554, 1636, 1914 ],
		/// \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"sunday\": [ 1103, 1553 ], \"tuesday\": [ 640, 748, 951, 1136, 1259, 1338,
		/// 1419, 1554, 1636, 1914 ], \"validUntil\": -1, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1, \"costPrice\": 0,
		/// \"fareStage\": \"\", \"costTravelTime\": 0 }, { \"serviceLine\": \"\",
		/// \"gtype\": 1, \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Schesslitz/Bahnhof/BusPlatforms/-\",
		/// \"bbox\": [ 11.027739, 49.972342, 11.027739, 49.972342 ], \"latitude\":
		/// \"49.972342\", \"postalCode\": \"96110\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"geom\": \"POINT (11.027739 49.972342)\", \"platform\": \"-\", \"mode\":
		/// \"Pedestrian\", \"number\": \"6\", \"headingDirection\": \"\", \"street\":
		/// \"Bamberger Strasse\", \"stopName\": \"Schesslitz, Bahnhof\", \"longitude\":
		/// \"11.027739 \" } ], 48.2 ], \"meta\": [ [ { \"id\": 34, \"type\": \"node\",
		/// \"deleted\": false }, { \"id\": 71, \"type\": \"relationship\", \"deleted\":
		/// false }, { \"id\": 9, \"type\": \"node\", \"deleted\": false }, { \"id\":
		/// 72, \"type\": \"relationship\", \"deleted\": false }, { \"id\": 8, \"type\":
		/// \"node\", \"deleted\": false }, { \"id\": 73, \"type\": \"relationship\",
		/// \"deleted\": false }, { \"id\": 30, \"type\": \"node\", \"deleted\": false
		/// }, { \"id\": 117, \"type\": \"relationship\", \"deleted\": false }, {
		/// \"id\": 32, \"type\": \"node\", \"deleted\": false }, { \"id\": 74,
		/// \"type\": \"relationship\", \"deleted\": false }, { \"id\": 16, \"type\":
		/// \"node\", \"deleted\": false }, { \"id\": 75, \"type\": \"relationship\",
		/// \"deleted\": false }, { \"id\": 17, \"type\": \"node\", \"deleted\": false
		/// }, { \"id\": 76, \"type\": \"relationship\", \"deleted\": false }, { \"id\":
		/// 38, \"type\": \"node\", \"deleted\": false } ], null ] }, { \"row\": [ [ {
		/// \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/ZOB/BusPlatforms/A\",
		/// \"bbox\": [ 10.891812, 49.893757, 10.891812, 49.893757 ], \"latitude\":
		/// \"49.893757\", \"postalCode\": \"96047\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"geom\":
		/// \"POINT (10.891812 49.893757)\", \"platform\": \"Steig A\", \"mode\":
		/// \"Pedestrian\", \"number\": \"4-6\", \"headingDirection\": \"\", \"street\":
		/// \"Promenadestrasse\", \"stopName\": \"Bamberg, ZOB\", \"longitude\":
		/// \"10.891812 \" }, { \"costWaitingTime\": 5, \"cost\": 7, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0 }, {
		/// \"scheduleName\": \"Bamberg ZOB - Bahnhof - Gartenstadt\", \"serviceLine\":
		/// \"901\", \"saturday\": [ 630, 700, 730, 800, 830, 900, 915, 930, 945, 1000,
		/// 1015, 1030, 1045, 1100, 1115, 1130, 1145, 1200, 1215, 1230, 1245, 1300,
		/// 1315, 1330, 1345, 1400, 1415, 1430, 1445, 1500, 1530, 1600, 1630, 1700,
		/// 1730, 1800, 1830, 1900, 1930 ], \"gtype\": 1, \"bbox\": [ 10.891812,
		/// 49.893757, 10.891812, 49.893757 ], \"postalCode\": \"96047\", \"latitude\":
		/// \"49.893757\", \"validFrom\": 20151213, \"geom\": \"POINT (10.891812
		/// 49.893757)\", \"platform\": \"Steig A\", \"mode\": \"Bus\", \"number\":
		/// \"4-6\", \"headingDirection\": \"Bamberg Gartenstadt\", \"street\":
		/// \"Promenadestrasse\", \"friday\": [ 515, 550, 615, 630, 645, 700, 715, 730,
		/// 745, 800, 815, 830, 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100,
		/// 1115, 1130, 1145, 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400,
		/// 1415, 1430, 1445, 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700,
		/// 1715, 1730, 1745, 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ],
		/// \"wednesday\": [ 515, 550, 615, 630, 645, 700, 715, 730, 745, 800, 815, 830,
		/// 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100, 1115, 1130, 1145,
		/// 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400, 1415, 1430, 1445,
		/// 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700, 1715, 1730, 1745,
		/// 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ], \"stopName\": \"Bamberg,
		/// ZOB\", \"longitude\": \"10.891812 \", \"monday\": [ 515, 550, 615, 630, 645,
		/// 700, 715, 730, 745, 800, 815, 830, 845, 900, 915, 930, 945, 1000, 1015,
		/// 1030, 1045, 1100, 1115, 1130, 1145, 1200, 1215, 1230, 1245, 1300, 1315,
		/// 1330, 1345, 1400, 1415, 1430, 1445, 1500, 1515, 1530, 1545, 1600, 1615,
		/// 1630, 1645, 1700, 1715, 1730, 1745, 1800, 1815, 1830, 1845, 1900, 1915,
		/// 1930, 1945 ], \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/ZOB/BusPlatforms/A/901/Bamberg_Gartenstadt\",
		/// \"thursday\": [ 515, 550, 615, 630, 645, 700, 715, 730, 745, 800, 815, 830,
		/// 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100, 1115, 1130, 1145,
		/// 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400, 1415, 1430, 1445,
		/// 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700, 1715, 1730, 1745,
		/// 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"sunday\": [
		/// 800, 830, 900, 930, 1000, 1030, 1100, 1130, 1200, 1230, 1300, 1330, 1400,
		/// 1430, 1500, 1530, 1600, 1630, 1700, 1730, 1800, 1830, 1900, 1930 ],
		/// \"tuesday\": [ 515, 550, 615, 630, 645, 700, 715, 730, 745, 800, 815, 830,
		/// 845, 900, 915, 930, 945, 1000, 1015, 1030, 1045, 1100, 1115, 1130, 1145,
		/// 1200, 1215, 1230, 1245, 1300, 1315, 1330, 1345, 1400, 1415, 1430, 1445,
		/// 1500, 1515, 1530, 1545, 1600, 1615, 1630, 1645, 1700, 1715, 1730, 1745,
		/// 1800, 1815, 1830, 1845, 1900, 1915, 1930, 1945 ], \"validUntil\": -1,
		/// \"comment\": \"\" }, { \"costWaitingTime\": 0, \"cost\": 6.2, \"change\": 0,
		/// \"costPrice\": 0, \"fareStage\": \"D\", \"costTravelTime\": 5 }, {
		/// \"scheduleName\": \"Bamberg ZOB - Bahnhof - Gartenstadt\", \"saturday\": [
		/// 635, 705, 735, 805, 835, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105,
		/// 1120, 1135, 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405,
		/// 1420, 1435, 1450, 1505, 1535, 1605, 1635, 1705, 1735, 1805, 1835, 1905, 1935
		/// ], \"serviceLine\": \"901\", \"gtype\": 1, \"bbox\": [ 10.897798, 49.900962,
		/// 10.897798, 49.900962 ], \"postalCode\": \"96052\", \"latitude\":
		/// \"49.900962\", \"validFrom\": 20151213, \"geom\": \"POINT (10.897798
		/// 49.900962)\", \"platform\": \"Steig 5\", \"mode\": \"Bus\", \"number\":
		/// \"31\", \"headingDirection\": \"Bamberg Gartenstadt\", \"street\":
		/// \"Ludwigstrasse\", \"friday\": [ 520, 530, 555, 620, 635, 650, 705, 720,
		/// 735, 750, 805, 820, 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050,
		/// 1105, 1120, 1135, 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350,
		/// 1405, 1420, 1435, 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650,
		/// 1705, 1720, 1735, 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ],
		/// \"wednesday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"stopName\":
		/// \"Bamberg, Bahnhof/Ludwigstrasse\", \"longitude\": \"10.897798 \",
		/// \"monday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"town\":
		/// \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/Bahnhof/BusPlatforms/5/901/Bamberg_Gartenstadt\",
		/// \"thursday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805, 820,
		/// 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\",
		/// \"sunday\": [ 805, 835, 905, 935, 1005, 1035, 1105, 1135, 1205, 1235, 1305,
		/// 1335, 1405, 1435, 1505, 1535, 1605, 1635, 1705, 1735, 1805, 1835, 1905, 1935
		/// ], \"tuesday\": [ 520, 530, 555, 620, 635, 650, 705, 720, 735, 750, 805,
		/// 820, 835, 850, 905, 920, 935, 950, 1005, 1020, 1035, 1050, 1105, 1120, 1135,
		/// 1150, 1205, 1220, 1235, 1250, 1305, 1320, 1335, 1350, 1405, 1420, 1435,
		/// 1450, 1505, 1520, 1535, 1550, 1605, 1620, 1635, 1650, 1705, 1720, 1735,
		/// 1750, 1805, 1820, 1835, 1850, 1905, 1920, 1935, 1950 ], \"validUntil\": -1,
		/// \"comment\": \"\" }, { \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0 }, {
		/// \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/Bahnhof/BusPlatforms/5\",
		/// \"bbox\": [ 10.897798, 49.900962, 10.897798, 49.900962 ], \"latitude\":
		/// \"49.900962\", \"postalCode\": \"96052\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\", \"geom\":
		/// \"POINT (10.897798 49.900962)\", \"platform\": \"Steig 5\", \"mode\":
		/// \"Pedestrian\", \"number\": \"31\", \"headingDirection\": \"\", \"street\":
		/// \"Ludwigstrasse\", \"stopName\": \"Bamberg, Bahnhof/Ludwigstrasse\",
		/// \"longitude\": \"10.897798 \" }, { \"costWaitingTime\": 0, \"cost\": 4,
		/// \"change\": 0, \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 4
		/// }, { \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\",
		/// \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/Bahnhof/BusPlatforms/4\",
		/// \"bbox\": [ 10.899026, 49.900589, 10.899026, 49.900589 ], \"latitude\":
		/// \"49.900589\", \"postalCode\": \"96052\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\", \"geom\":
		/// \"POINT (10.899026 49.900589)\", \"platform\": \"Steig 4\", \"mode\":
		/// \"Pedestrian\", \"number\": \"6\", \"headingDirection\": \"\", \"street\":
		/// \"Ludwigstrasse\", \"stopName\": \"Bamberg, Bahnhof/Vorplatz\",
		/// \"longitude\": \"10.899026 \" }, { \"costWaitingTime\": 5, \"cost\": 9,
		/// \"change\": 1, \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 2
		/// }, { \"scheduleName\": \"Bamberg, Bahnhof nach Schesslitz, Bahnhof\",
		/// \"serviceLine\": \"MO9000000001\", \"gtype\": 1, \"bbox\": [ 10.899026,
		/// 49.900589, 10.899026, 49.900589 ], \"postalCode\": \"96052\", \"latitude\":
		/// \"49.900589\", \"validFrom\": 20161001, \"geom\": \"POINT (10.899026
		/// 49.900589)\", \"platform\": \"\", \"mode\": \"Carpooling\", \"number\":
		/// \"6\", \"headingDirection\": \"Schesslitz, Bahnhof\", \"street\":
		/// \"Ludwigstrasse\", \"friday\": [ 630 ], \"wednesday\": [ 630 ],
		/// \"stopName\": \"Bamberg, Bahnhof/Vorplatz\", \"monday\": [ 630 ],
		/// \"longitude\": \"10.899026 \", \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Carpooling/DE/BY/Bamberg/Bahnhof/Vorplatz/MO9000000001\",
		/// \"thursday\": [ 630 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\",
		/// \"tuesday\": [ 630 ], \"validUntil\": 20170331, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 29, \"change\": 0, \"costPrice\": 2,
		/// \"fareStage\": \"\", \"costTravelTime\": 25 }, { \"scheduleName\":
		/// \"Bamberg, Bahnhof nach Schesslitz, Bahnhof\", \"serviceLine\":
		/// \"MO9000000001\", \"gtype\": 1, \"bbox\": [ 11.027739, 49.972342, 11.027739,
		/// 49.972342 ], \"postalCode\": \"96110\", \"latitude\": \"49.972342\",
		/// \"validFrom\": 20161001, \"geom\": \"POINT (11.027739 49.972342)\",
		/// \"platform\": \"\", \"mode\": \"Carpooling\", \"number\": \"6\",
		/// \"headingDirection\": \"Schesslitz, Bahnhof\", \"street\": \"Bamberger
		/// Strasse\", \"friday\": [ 655 ], \"wednesday\": [ 655 ], \"stopName\":
		/// \"Schesslitz, Bahnhof\", \"monday\": [ 655 ], \"longitude\": \"11.027739 \",
		/// \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Carpooling/DE/BY/Schesslitz/Bahnhof/MO9000000001\",
		/// \"thursday\": [ 655 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"tuesday\": [ 655 ], \"validUntil\": 20170331, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1, \"costPrice\": 0,
		/// \"fareStage\": \"\", \"costTravelTime\": 0 }, { \"serviceLine\": \"\",
		/// \"gtype\": 1, \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Schesslitz/Bahnhof/BusPlatforms/-\",
		/// \"bbox\": [ 11.027739, 49.972342, 11.027739, 49.972342 ], \"latitude\":
		/// \"49.972342\", \"postalCode\": \"96110\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"geom\": \"POINT (11.027739 49.972342)\", \"platform\": \"-\", \"mode\":
		/// \"Pedestrian\", \"number\": \"6\", \"headingDirection\": \"\", \"street\":
		/// \"Bamberger Strasse\", \"stopName\": \"Schesslitz, Bahnhof\", \"longitude\":
		/// \"11.027739 \" } ], 59.2 ], \"meta\": [ [ { \"id\": 33, \"type\": \"node\",
		/// \"deleted\": false }, { \"id\": 48, \"type\": \"relationship\", \"deleted\":
		/// false }, { \"id\": 5, \"type\": \"node\", \"deleted\": false }, { \"id\":
		/// 49, \"type\": \"relationship\", \"deleted\": false }, { \"id\": 4, \"type\":
		/// \"node\", \"deleted\": false }, { \"id\": 50, \"type\": \"relationship\",
		/// \"deleted\": false }, { \"id\": 30, \"type\": \"node\", \"deleted\": false
		/// }, { \"id\": 117, \"type\": \"relationship\", \"deleted\": false }, {
		/// \"id\": 32, \"type\": \"node\", \"deleted\": false }, { \"id\": 68,
		/// \"type\": \"relationship\", \"deleted\": false }, { \"id\": 42, \"type\":
		/// \"node\", \"deleted\": false }, { \"id\": 69, \"type\": \"relationship\",
		/// \"deleted\": false }, { \"id\": 44, \"type\": \"node\", \"deleted\": false
		/// }, { \"id\": 70, \"type\": \"relationship\", \"deleted\": false }, { \"id\":
		/// 38, \"type\": \"node\", \"deleted\": false } ], null ] }, { \"row\": [ [ {
		/// \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/ZOB/BusPlatforms/C\",
		/// \"bbox\": [ 10.891812, 49.893757, 10.891812, 49.893757 ], \"latitude\":
		/// \"49.893757\", \"postalCode\": \"96047\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"geom\":
		/// \"POINT (10.891812 49.893757)\", \"platform\": \"Steig C\", \"mode\":
		/// \"Pedestrian\", \"number\": \"4-6\", \"headingDirection\": \"\", \"street\":
		/// \"Promenadestrasse\", \"stopName\": \"Bamberg, ZOB\", \"longitude\":
		/// \"10.891812 \" }, { \"costWaitingTime\": 5, \"cost\": 7, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0 }, {
		/// \"scheduleName\": \"Bamberg ZOB - Bahnhof - Stadion\", \"serviceLine\":
		/// \"902\", \"saturday\": [ 619, 649, 719, 749, 819, 849, 905, 919, 935, 949,
		/// 1005, 1019, 1035, 1049, 1105, 1119, 1135, 1149, 1205, 1219, 1235, 1249,
		/// 1305, 1319, 1335, 1349, 1405, 1419, 1435, 1449, 1519, 1549, 1619, 1649,
		/// 1719, 1749, 1819, 1849, 1919, 1949 ], \"gtype\": 1, \"bbox\": [ 10.891812,
		/// 49.893757, 10.891812, 49.893757 ], \"postalCode\": \"96047\", \"latitude\":
		/// \"49.893757\", \"validFrom\": 20151213, \"geom\": \"POINT (10.891812
		/// 49.893757)\", \"platform\": \"Steig C\", \"mode\": \"Bus\", \"number\":
		/// \"4-6\", \"headingDirection\": \"Bamberg Kastanienstr.\", \"street\":
		/// \"Promenadestrasse\", \"friday\": [ 534, 604, 619, 634, 649, 704, 719, 734,
		/// 749, 804, 819, 834, 849, 904, 919, 934, 949, 1004, 1019, 1034, 1049, 1104,
		/// 1119, 1134, 1149, 1204, 1219, 1234, 1249, 1304, 1319, 1334, 1349, 1404,
		/// 1419, 1434, 1449, 1504, 1519, 1534, 1549, 1604, 1619, 1634, 1649, 1704,
		/// 1719, 1734, 1749, 1804, 1819, 1834, 1849, 1904, 1919, 1934, 1949 ],
		/// \"wednesday\": [ 534, 604, 619, 634, 649, 704, 719, 734, 749, 804, 819, 834,
		/// 849, 904, 919, 934, 949, 1004, 1019, 1034, 1049, 1104, 1119, 1134, 1149,
		/// 1204, 1219, 1234, 1249, 1304, 1319, 1334, 1349, 1404, 1419, 1434, 1449,
		/// 1504, 1519, 1534, 1549, 1604, 1619, 1634, 1649, 1704, 1719, 1734, 1749,
		/// 1804, 1819, 1834, 1849, 1904, 1919, 1934, 1949 ], \"stopName\": \"Bamberg,
		/// ZOB\", \"longitude\": \"10.891812 \", \"monday\": [ 534, 604, 619, 634, 649,
		/// 704, 719, 734, 749, 804, 819, 834, 849, 904, 919, 934, 949, 1004, 1019,
		/// 1034, 1049, 1104, 1119, 1134, 1149, 1204, 1219, 1234, 1249, 1304, 1319,
		/// 1334, 1349, 1404, 1419, 1434, 1449, 1504, 1519, 1534, 1549, 1604, 1619,
		/// 1634, 1649, 1704, 1719, 1734, 1749, 1804, 1819, 1834, 1849, 1904, 1919,
		/// 1934, 1949 ], \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/ZOB/BusPlatforms/C/902/Bamberg_Kastanienstr\",
		/// \"thursday\": [ 534, 604, 619, 634, 649, 704, 719, 734, 749, 804, 819, 834,
		/// 849, 904, 919, 934, 949, 1004, 1019, 1034, 1049, 1104, 1119, 1134, 1149,
		/// 1204, 1219, 1234, 1249, 1304, 1319, 1334, 1349, 1404, 1419, 1434, 1449,
		/// 1504, 1519, 1534, 1549, 1604, 1619, 1634, 1649, 1704, 1719, 1734, 1749,
		/// 1804, 1819, 1834, 1849, 1904, 1919, 1934, 1949 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/ZOB\", \"sunday\": [
		/// 819, 919, 1019, 1119, 1150, 1219, 1250, 1319, 1350, 1419, 1450, 1519, 1550,
		/// 1619, 1650, 1719, 1750, 1819, 1850, 1919, 1950 ], \"tuesday\": [ 534, 604,
		/// 619, 634, 649, 704, 719, 734, 749, 804, 819, 834, 849, 904, 919, 934, 949,
		/// 1004, 1019, 1034, 1049, 1104, 1119, 1134, 1149, 1204, 1219, 1234, 1249,
		/// 1304, 1319, 1334, 1349, 1404, 1419, 1434, 1449, 1504, 1519, 1534, 1549,
		/// 1604, 1619, 1634, 1649, 1704, 1719, 1734, 1749, 1804, 1819, 1834, 1849,
		/// 1904, 1919, 1934, 1949 ], \"validUntil\": -1, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 6.2, \"change\": 0, \"costPrice\": 0,
		/// \"fareStage\": \"D\", \"costTravelTime\": 5 }, { \"scheduleName\": \"Bamberg
		/// ZOB - Bahnhof - Stadion\", \"saturday\": [ 624, 654, 724, 754, 824, 854,
		/// 910, 924, 940, 954, 1010, 1024, 1040, 1054, 1110, 1124, 1140, 1154, 1210,
		/// 1224, 1240, 1254, 1310, 1324, 1340, 1354, 1410, 1424, 1440, 1454, 1524,
		/// 1554, 1624, 1654, 1724, 1754, 1824, 1854, 1924, 1954 ], \"serviceLine\":
		/// \"902\", \"gtype\": 1, \"bbox\": [ 10.897798, 49.900962, 10.897798,
		/// 49.900962 ], \"postalCode\": \"96052\", \"latitude\": \"49.900962\",
		/// \"validFrom\": 20151213, \"geom\": \"POINT (10.897798 49.900962)\",
		/// \"platform\": \"Steig 5\", \"mode\": \"Bus\", \"number\": \"31\",
		/// \"headingDirection\": \"Bamberg Kastanienstr.\", \"street\":
		/// \"Ludwigstrasse\", \"friday\": [ 539, 554, 609, 624, 639, 654, 709, 724,
		/// 739, 754, 809, 824, 839, 854, 909, 924, 939, 954, 1009, 1024, 1039, 1054,
		/// 1109, 1124, 1139, 1154, 1209, 1224, 1239, 1254, 1309, 1324, 1339, 1354,
		/// 1409, 1424, 1439, 1454, 1509, 1524, 1539, 1554, 1609, 1624, 1639, 1654,
		/// 1709, 1724, 1739, 1754, 1809, 1824, 1839, 1854, 1009, 1024, 1039, 1054 ],
		/// \"wednesday\": [ 539, 554, 609, 624, 639, 654, 709, 724, 739, 754, 809, 824,
		/// 839, 854, 909, 924, 939, 954, 1009, 1024, 1039, 1054, 1109, 1124, 1139,
		/// 1154, 1209, 1224, 1239, 1254, 1309, 1324, 1339, 1354, 1409, 1424, 1439,
		/// 1454, 1509, 1524, 1539, 1554, 1609, 1624, 1639, 1654, 1709, 1724, 1739,
		/// 1754, 1809, 1824, 1839, 1854, 1009, 1024, 1039, 1054 ], \"stopName\":
		/// \"Bamberg, Bahnhof/Ludwigstrasse\", \"longitude\": \"10.897798 \",
		/// \"monday\": [ 539, 554, 609, 624, 639, 654, 709, 724, 739, 754, 809, 824,
		/// 839, 854, 909, 924, 939, 954, 1009, 1024, 1039, 1054, 1109, 1124, 1139,
		/// 1154, 1209, 1224, 1239, 1254, 1309, 1324, 1339, 1354, 1409, 1424, 1439,
		/// 1454, 1509, 1524, 1539, 1554, 1609, 1624, 1639, 1654, 1709, 1724, 1739,
		/// 1754, 1809, 1824, 1839, 1854, 1009, 1024, 1039, 1054 ], \"town\":
		/// \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Bus/DE/BY/Bamberg/Bahnhof/BusPlatforms/5/902/Bamberg_Kastanienstr\",
		/// \"thursday\": [ 539, 554, 609, 624, 639, 654, 709, 724, 739, 754, 809, 824,
		/// 839, 854, 909, 924, 939, 954, 1009, 1024, 1039, 1054, 1109, 1124, 1139,
		/// 1154, 1209, 1224, 1239, 1254, 1309, 1324, 1339, 1354, 1409, 1424, 1439,
		/// 1454, 1509, 1524, 1539, 1554, 1609, 1624, 1639, 1654, 1709, 1724, 1739,
		/// 1754, 1809, 1824, 1839, 1854, 1009, 1024, 1039, 1054 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\",
		/// \"sunday\": [ 824, 924, 1024, 1119, 1155, 1224, 1255, 1324, 1355, 1424,
		/// 1455, 1524, 1555, 1624, 1655, 1724, 1755, 1824, 1855, 1924, 1955 ],
		/// \"tuesday\": [ 539, 554, 609, 624, 639, 654, 709, 724, 739, 754, 809, 824,
		/// 839, 854, 909, 924, 939, 954, 1009, 1024, 1039, 1054, 1109, 1124, 1139,
		/// 1154, 1209, 1224, 1239, 1254, 1309, 1324, 1339, 1354, 1409, 1424, 1439,
		/// 1454, 1509, 1524, 1539, 1554, 1609, 1624, 1639, 1654, 1709, 1724, 1739,
		/// 1754, 1809, 1824, 1839, 1854, 1009, 1024, 1039, 1054 ], \"validUntil\": -1,
		/// \"comment\": \"\" }, { \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1,
		/// \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 0 }, {
		/// \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/Bahnhof/BusPlatforms/5\",
		/// \"bbox\": [ 10.897798, 49.900962, 10.897798, 49.900962 ], \"latitude\":
		/// \"49.900962\", \"postalCode\": \"96052\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\", \"geom\":
		/// \"POINT (10.897798 49.900962)\", \"platform\": \"Steig 5\", \"mode\":
		/// \"Pedestrian\", \"number\": \"31\", \"headingDirection\": \"\", \"street\":
		/// \"Ludwigstrasse\", \"stopName\": \"Bamberg, Bahnhof/Ludwigstrasse\",
		/// \"longitude\": \"10.897798 \" }, { \"costWaitingTime\": 0, \"cost\": 4,
		/// \"change\": 0, \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 4
		/// }, { \"serviceLine\": \"\", \"gtype\": 1, \"town\": \"Bamberg\",
		/// \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Bamberg/Bahnhof/BusPlatforms/4\",
		/// \"bbox\": [ 10.899026, 49.900589, 10.899026, 49.900589 ], \"latitude\":
		/// \"49.900589\", \"postalCode\": \"96052\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\", \"geom\":
		/// \"POINT (10.899026 49.900589)\", \"platform\": \"Steig 4\", \"mode\":
		/// \"Pedestrian\", \"number\": \"6\", \"headingDirection\": \"\", \"street\":
		/// \"Ludwigstrasse\", \"stopName\": \"Bamberg, Bahnhof/Vorplatz\",
		/// \"longitude\": \"10.899026 \" }, { \"costWaitingTime\": 5, \"cost\": 9,
		/// \"change\": 1, \"costPrice\": 0, \"fareStage\": \"\", \"costTravelTime\": 2
		/// }, { \"scheduleName\": \"Bamberg, Bahnhof nach Schesslitz, Bahnhof\",
		/// \"serviceLine\": \"MO9000000001\", \"gtype\": 1, \"bbox\": [ 10.899026,
		/// 49.900589, 10.899026, 49.900589 ], \"postalCode\": \"96052\", \"latitude\":
		/// \"49.900589\", \"validFrom\": 20161001, \"geom\": \"POINT (10.899026
		/// 49.900589)\", \"platform\": \"\", \"mode\": \"Carpooling\", \"number\":
		/// \"6\", \"headingDirection\": \"Schesslitz, Bahnhof\", \"street\":
		/// \"Ludwigstrasse\", \"friday\": [ 630 ], \"wednesday\": [ 630 ],
		/// \"stopName\": \"Bamberg, Bahnhof/Vorplatz\", \"monday\": [ 630 ],
		/// \"longitude\": \"10.899026 \", \"town\": \"Bamberg\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Carpooling/DE/BY/Bamberg/Bahnhof/Vorplatz/MO9000000001\",
		/// \"thursday\": [ 630 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Bamberg/Bahnhof\",
		/// \"tuesday\": [ 630 ], \"validUntil\": 20170331, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 29, \"change\": 0, \"costPrice\": 2,
		/// \"fareStage\": \"\", \"costTravelTime\": 25 }, { \"scheduleName\":
		/// \"Bamberg, Bahnhof nach Schesslitz, Bahnhof\", \"serviceLine\":
		/// \"MO9000000001\", \"gtype\": 1, \"bbox\": [ 11.027739, 49.972342, 11.027739,
		/// 49.972342 ], \"postalCode\": \"96110\", \"latitude\": \"49.972342\",
		/// \"validFrom\": 20161001, \"geom\": \"POINT (11.027739 49.972342)\",
		/// \"platform\": \"\", \"mode\": \"Carpooling\", \"number\": \"6\",
		/// \"headingDirection\": \"Schesslitz, Bahnhof\", \"street\": \"Bamberger
		/// Strasse\", \"friday\": [ 655 ], \"wednesday\": [ 655 ], \"stopName\":
		/// \"Schesslitz, Bahnhof\", \"monday\": [ 655 ], \"longitude\": \"11.027739 \",
		/// \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Carpooling/DE/BY/Schesslitz/Bahnhof/MO9000000001\",
		/// \"thursday\": [ 655 ], \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"tuesday\": [ 655 ], \"validUntil\": 20170331, \"comment\": \"\" }, {
		/// \"costWaitingTime\": 0, \"cost\": 2, \"change\": 1, \"costPrice\": 0,
		/// \"fareStage\": \"\", \"costTravelTime\": 0 }, { \"serviceLine\": \"\",
		/// \"gtype\": 1, \"town\": \"Schesslitz\", \"stopUri\":
		/// \"http://simple.uni-bamberg.de/stops/Pedestrian/DE/BY/Schesslitz/Bahnhof/BusPlatforms/-\",
		/// \"bbox\": [ 11.027739, 49.972342, 11.027739, 49.972342 ], \"latitude\":
		/// \"49.972342\", \"postalCode\": \"96110\", \"poiUri\":
		/// \"http://simple.uni-bamberg.de/locations/DE/BY/Schesslitz/Bahnhof\",
		/// \"geom\": \"POINT (11.027739 49.972342)\", \"platform\": \"-\", \"mode\":
		/// \"Pedestrian\", \"number\": \"6\", \"headingDirection\": \"\", \"street\":
		/// \"Bamberger Strasse\", \"stopName\": \"Schesslitz, Bahnhof\", \"longitude\":
		/// \"11.027739 \" } ], 59.2 ], \"meta\": [ [ { \"id\": 34, \"type\": \"node\",
		/// \"deleted\": false }, { \"id\": 71, \"type\": \"relationship\", \"deleted\":
		/// false }, { \"id\": 9, \"type\": \"node\", \"deleted\": false }, { \"id\":
		/// 72, \"type\": \"relationship\", \"deleted\": false }, { \"id\": 8, \"type\":
		/// \"node\", \"deleted\": false }, { \"id\": 73, \"type\": \"relationship\",
		/// \"deleted\": false }, { \"id\": 30, \"type\": \"node\", \"deleted\": false
		/// }, { \"id\": 117, \"type\": \"relationship\", \"deleted\": false }, {
		/// \"id\": 32, \"type\": \"node\", \"deleted\": false }, { \"id\": 68,
		/// \"type\": \"relationship\", \"deleted\": false }, { \"id\": 42, \"type\":
		/// \"node\", \"deleted\": false }, { \"id\": 69, \"type\": \"relationship\",
		/// \"deleted\": false }, { \"id\": 44, \"type\": \"node\", \"deleted\": false
		/// }, { \"id\": 70, \"type\": \"relationship\", \"deleted\": false }, { \"id\":
		/// 38, \"type\": \"node\", \"deleted\": false } ], null ] } ] } ], \"errors\":
		/// [] }";
		// String json2 = "{\"name\":\"mkyong\",\"age\":33,\"messages\":[\"hello
		// jackson 1\",\"hello jackson 2\",\"hello jackson 3\"]}";

		String json1 = "{\"esp8266id\": \"1495817\", \"software_version\": \"NRZ-2017-092\", \"sensordatavalues\":[{\"value_type\":\"SDS_P1\",\"value\":\"10.50\"},{\"value_type\":\"SDS_P2\",\"value\":\"8.97\"},{\"value_type\":\"temperature\",\"value\":\"22.70\"},{\"value_type\":\"humidity\",\"value\":\"63.40\"},{\"value_type\":\"samples\",\"value\":\"787834\"},{\"value_type\":\"min_micro\",\"value\":\"179\"},{\"value_type\":\"max_micro\",\"value\":\"25422\"},{\"value_type\":\"signal\",\"value\":\"-45\"}]}";

		KeyValueObject<IMetaAttribute> kv = new KeyValueObject<>(json1);

		System.out.println(kv.toStringWithNewlines());

		List<String> liste = new ArrayList<>();
		liste.add("Eins");
		liste.add("Zwei");
		liste.add("Drei");

		kv.setAttribute("neu", liste);

		System.out.println(kv.toStringWithNewlines());

		Integer[] liste2 = new Integer[] { 1, 2, 3 };

		kv.setAttribute("neu2", liste2);

		System.out.println(kv.toStringWithNewlines());

		// Map<String, Object> map;
		// map = kv.getAsKeyValueMap();
		// for (Entry<String, Object> e : map.entrySet()) {
		// System.out.println(e.getKey() + " --> " + e.getValue());
		// }

		// List<Object> res = kv.path("$.results[0].data[*].row[0]");
		// for (Object kvo : res) {
		// System.out.println(kvo);
		// }

	}


}
