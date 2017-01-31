/*******************************************************************************
 * Copyright 2016 Georg Berendt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.xafero.turjumaan.server.java;

import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.types.builtin.DateTime;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.QualifiedName;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UByte;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import com.inductiveautomation.opcua.stack.core.types.structured.Argument;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationListener;
import com.xafero.kitea.collections.impl.ObservableContainer;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.core.sdk.enums.ValueRanks;
import com.xafero.turjumaan.server.java.api.CallableMethod;
import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.Embed;
import com.xafero.turjumaan.server.java.api.NotCacheable;
import com.xafero.turjumaan.server.java.api.Options;
import com.xafero.turjumaan.server.java.api.ResponseFormat;
import com.xafero.turjumaan.server.java.api.out;
import com.xafero.turjumaan.server.sdk.api.INamespaceResolver;
import com.xafero.turjumaan.server.sdk.util.FullNodeInfo;

/**
 * The object translator for Java to OPC UA.
 */
public class ObjectTranslator {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(ObjectTranslator.class);

	/** The object name space. */
	private final int objNs;

	/** The object URI. */
	@SuppressWarnings("unused")
	private final String objUri;

	/** The object id. */
	private final AtomicInteger objId;

	/** The resolver. */
	private final INamespaceResolver resolver;

	/** The nodes. */
	private final Map<NodeId, FullNodeInfo> nodes;

	/** The mapping of class to node. */
	private final Map<Class<?>, NodeId> classToOpc;

	/** The discoverer. */
	private final ParameterNameDiscoverer disco;

	/**
	 * Instantiates a new object translator.
	 *
	 * @param objNamespace
	 *            the object's name space
	 * @param objNamespaceUri
	 *            the object name space's URI
	 * @param objStartId
	 *            the object start id
	 * @param resolver
	 *            the resolver
	 * @param nodes
	 *            the nodes
	 */
	public ObjectTranslator(int objNamespace, String objNamespaceUri, int objStartId, INamespaceResolver resolver,
			Map<NodeId, FullNodeInfo> nodes) {
		objNs = objNamespace;
		objUri = objNamespaceUri;
		objId = new AtomicInteger(objStartId);
		this.resolver = resolver;
		this.nodes = nodes;
		disco = new DefaultParameterNameDiscoverer();
		// Java class to OPC UA id
		classToOpc = new HashMap<Class<?>, NodeId>();
		classToOpc.put(boolean.class, Identifiers.Boolean);
		classToOpc.put(Boolean.class, Identifiers.Boolean);
		classToOpc.put(byte.class, Identifiers.SByte);
		classToOpc.put(Byte.class, Identifiers.SByte);
		classToOpc.put(int.class, Identifiers.Int32);
		classToOpc.put(Integer.class, Identifiers.Int32);
		classToOpc.put(long.class, Identifiers.Int64);
		classToOpc.put(Long.class, Identifiers.Int64);
		classToOpc.put(float.class, Identifiers.Float);
		classToOpc.put(Float.class, Identifiers.Float);
		classToOpc.put(double.class, Identifiers.Double);
		classToOpc.put(Double.class, Identifiers.Double);
		classToOpc.put(String.class, Identifiers.String);
		classToOpc.put(DateTime.class, Identifiers.DateTime);
	}

	/**
	 * Extract input and output arguments.
	 *
	 * @param meth
	 *            the method
	 * @param inputs
	 *            the inputs
	 * @param outputs
	 *            the outputs
	 */
	private void extractInAndOutputs(Method meth, List<Argument> inputs, List<Argument> outputs) {
		// Get some reflection done
		Type[] argTypes = extend(meth.getGenericParameterTypes(), meth.getGenericReturnType());
		String[] argNames = extend(disco.getParameterNames(meth), "ReturnType");
		Annotation[][] argAnnots = extend(meth.getParameterAnnotations(), new Annotation[][] { meth.getAnnotations() });
		// Loop for every type
		for (int i = 0; i < argTypes.length; i++) {
			// Basic stuff
			Class<?>[] reflArgType = getClasses(argTypes[i]);
			String argName = toCamelCase(getArgName(argNames, i));
			Description argDesc = find(argAnnots[i], Description.class);
			// Check if out parameter
			boolean isOut = (reflArgType[0] == out.class) || (i == 0);
			Class<?> argType = isOut ? (i == 0 ? reflArgType[0] : reflArgType[1]) : reflArgType[0];
			// Skip if void
			if (argType == void.class)
				continue;
			// Get value rank and data type
			SimpleEntry<Class<?>, ValueRanks> info = getValueRank(argType);
			int argRank = info.getValue().getValue();
			NodeId argDataType = toOpc(info.getKey());
			// Add to lists
			LocalizedText desc = argDesc == null ? LocalizedText.NULL_VALUE : LocalizedText.english(argDesc.value());
			Argument arg = new Argument(argName, argDataType, argRank, null, desc);
			if (isOut)
				outputs.add(arg);
			else
				inputs.add(arg);
		}
	}

	/**
	 * Gets the argument name.
	 *
	 * @param argNames
	 *            the argument names
	 * @param index
	 *            the index
	 * @return the argument name
	 */
	private String getArgName(String[] argNames, int index) {
		if (index < argNames.length)
			return argNames[index];
		return "arg" + index;
	}

	/**
	 * Extend the old items with the new ones.
	 *
	 * @param <T>
	 *            the generic type
	 * @param oldItems
	 *            the old items
	 * @param newItems
	 *            the new items
	 * @return the array with all items
	 */
	@SuppressWarnings("unchecked")
	private static <T> T[] extend(T[] oldItems, T... newItems) {
		Class<?> itemType = newItems.getClass().getComponentType();
		if (oldItems == null)
			oldItems = (T[]) Array.newInstance(itemType, 0);
		Object array = Array.newInstance(itemType, oldItems.length + newItems.length);
		for (int i = 0; i < newItems.length; i++)
			Array.set(array, i, newItems[i]);
		for (int i = 0; i < oldItems.length; i++)
			Array.set(array, newItems.length + i, oldItems[i]);
		return (T[]) array;
	}

	/**
	 * Gets the classes.
	 *
	 * @param type
	 *            the type
	 * @return the classes
	 */
	private Class<?>[] getClasses(Type type) {
		if (type instanceof Class<?>)
			return new Class<?>[] { (Class<?>) type };
		ParameterizedType pt = (ParameterizedType) type;
		Type[] actTypes = pt.getActualTypeArguments();
		Class<?>[] classes = new Class<?>[1 + actTypes.length];
		classes[0] = (Class<?>) pt.getRawType();
		for (int i = 0; i < actTypes.length; i++)
			classes[i + 1] = actTypes[i] instanceof Class<?> ? (Class<?>) actTypes[i] : Object.class;
		return classes;
	}

	/**
	 * Creates a node from an object.
	 *
	 * @param obj
	 *            the object
	 * @return the full node info
	 */
	public FullNodeInfo createNodeFrom(Object obj) {
		return createNodeFrom(obj, EnumSet.noneOf(Options.class));
	}

	/**
	 * Creates the node from object and options.
	 *
	 * @param obj
	 *            the object
	 * @param opts
	 *            the options
	 * @return the full node info
	 */
	public FullNodeInfo createNodeFrom(Object obj, EnumSet<Options> opts) {
		int currentObjId = objId.incrementAndGet();
		Class<?> type = obj.getClass();
		String name = type.getSimpleName();
		NodeId nid = new NodeId(objNs, currentObjId);
		FullNodeInfo node = new FullNodeInfo(nid, NodeClass.Object, name);
		node._abstract(Modifier.isAbstract(type.getModifiers()));
		node.eventNotifier(UByte.valueOf(1));
		Description desc = type.getAnnotation(Description.class);
		if (desc != null)
			node.desc(LocalizedText.english(desc.value()));
		nodes.put(nid, node);
		BeanInfo info = getBeanInfo(type);
		// Create method blacklist
		Set<Method> blacklist = new HashSet<Method>();
		// Go for each property
		for (PropertyDescriptor pd : opts.contains(Options.SkipProperties) ? new PropertyDescriptor[0]
				: info.getPropertyDescriptors()) {
			Method reader = pd.getReadMethod();
			if (reader == null)
				continue;
			blacklist.add(reader);
			if (pd.getName().equals("class"))
				continue;
			NodeId pid = new NodeId(objNs, currentObjId + "_" + pd.getName());
			String pn = toCamelCase(pd.getDisplayName());
			FullNodeInfo prop = new FullNodeInfo(pid, NodeClass.Variable, pn);
			// Read description
			Description pdesc = reader.getAnnotation(Description.class);
			if (pdesc != null)
				prop.desc(LocalizedText.english(pdesc.value()));
			// Get embed settings
			Embed pemb = reader.getAnnotation(Embed.class);
			if (pemb != null) {
				// Get special options
				EnumSet<Options> pembo = EnumSet.copyOf(Arrays.asList(pemb.how()));
				// Retrieve sub-object
				Object subObj = invoke(pd, obj, null);
				// Check if it is an observable container
				if (subObj instanceof ObservableContainer<?>) {
					@SuppressWarnings("unchecked")
					ObservableContainer<Object> oc = (ObservableContainer<Object>) subObj;
					addDynamicListener(oc, prop, pdesc, pembo);
				}
				// If array is found, convert it to a list
				if (subObj.getClass().isArray())
					subObj = Arrays.asList((Object[]) subObj);
				// If map is found, extract its entries
				if (subObj instanceof Map<?, ?>)
					subObj = ((Map<?, ?>) subObj).entrySet();
				// If enumerable is found, only add its children
				if (subObj instanceof Iterable<?>) {
					AtomicInteger subIndex = new AtomicInteger(1);
					for (final Object subItem : (Iterable<?>) subObj)
						createSubItem(subIndex, subItem, prop, pdesc, pembo);
					// Change type of property to be a collection object
					prop.set(AttributeIds.NodeClass, NodeClass.Object);
					node.addReference(Identifiers.Organizes, false, prop, resolver);
					prop.addReference(Identifiers.Organizes, true, node, resolver);
					nodes.put(pid, prop);
					continue;
				}
				// It's a single object
				createNodeAndAddIt(node, pn, subObj, pdesc, pembo);
				continue;
			}
			// Set sampling
			prop.minSamplingInterval(1000.0);
			// Read format setting
			ResponseFormat fset = reader.getAnnotation(ResponseFormat.class);
			// Read cache setting
			NotCacheable nocache = reader.getAnnotation(NotCacheable.class);
			if (nocache == null)
				prop.value(invoke(pd, obj, fset));
			else
				prop.value(callInvoke(pd, obj, fset));
			// Find property type
			Class<?> pt = pd.getPropertyType();
			if (fset != null)
				applyOpcType(prop, fset.value().getRank(), fset.value().getType());
			else if (pt.isEnum())
				applyOpcType(prop, ValueRanks.Scalar, createOrGetEnum(pt));
			else
				applyOpcType(prop, pt);
			// Set property type
			prop.addReference(Identifiers.HasTypeDefinition, false, Identifiers.PropertyType, resolver);
			// Add property to parent and map
			node.addReference(Identifiers.HasProperty, false, prop, resolver);
			prop.addReference(Identifiers.HasProperty, true, node, resolver);
			nodes.put(pid, prop);
		}
		// Go for each method
		for (MethodDescriptor md : opts.contains(Options.SkipMethods) ? new MethodDescriptor[0]
				: info.getMethodDescriptors()) {
			if (blacklist.contains(md.getMethod()))
				continue;
			if (md.getMethod().getDeclaringClass() == Object.class)
				continue;
			if (md.getMethod().getReturnType().getName().startsWith("java.util.stream."))
				continue;
			String methIdStr = currentObjId + "-" + md.getName();
			NodeId mid = new NodeId(objNs, methIdStr);
			String mn = toCamelCase(md.getDisplayName());
			FullNodeInfo meth = new FullNodeInfo(mid, NodeClass.Method, mn);
			// Set value
			createMethodImpl(meth, obj, md.getMethod());
			// Set executable
			meth.executable(true).userExecutable(true);
			// Read description
			Description mdesc = md.getMethod().getAnnotation(Description.class);
			if (mdesc != null)
				meth.desc(LocalizedText.english(mdesc.value()));
			// Add method to parent and map
			node.addReference(Identifiers.HasComponent, false, meth, resolver);
			meth.addReference(Identifiers.HasComponent, true, node, resolver);
			nodes.put(mid, meth);
			// Get inputs and outputs
			List<Argument> inputs = new LinkedList<Argument>();
			List<Argument> outputs = new LinkedList<Argument>();
			extractInAndOutputs(md.getMethod(), inputs, outputs);
			// Set input arguments
			NodeId ian = new NodeId(objNs, methIdStr + "_in");
			FullNodeInfo inArgs = new FullNodeInfo(ian, NodeClass.Variable, "?");
			inArgs.value(inputs.toArray(new Argument[inputs.size()]));
			applyMethodCmpDefaults(inArgs, "InputArguments");
			// Add inputs to method
			meth.addReference(Identifiers.HasProperty, false, inArgs, resolver);
			inArgs.addReference(Identifiers.HasProperty, true, meth, resolver);
			nodes.put(ian, inArgs);
			// Set output arguments
			NodeId oan = new NodeId(objNs, methIdStr + "_out");
			FullNodeInfo outArgs = new FullNodeInfo(oan, NodeClass.Variable, "?");
			outArgs.value(outputs.toArray(new Argument[outputs.size()]));
			applyMethodCmpDefaults(outArgs, "OutputArguments");
			// Add outputs to method
			meth.addReference(Identifiers.HasProperty, false, outArgs, resolver);
			outArgs.addReference(Identifiers.HasProperty, true, meth, resolver);
			nodes.put(oan, outArgs);
		}
		// Finish it!
		return node;
	}

	/**
	 * Adds the dynamic listener.
	 *
	 * @param oc
	 *            the observable container
	 * @param prop
	 *            the properties
	 * @param pdesc
	 *            the property description
	 * @param pembo
	 *            the property options
	 */
	private void addDynamicListener(ObservableContainer<Object> oc, final FullNodeInfo prop, final Description pdesc,
			final EnumSet<Options> pembo) {
		oc.addModificationListener(new ModificationListener<Object>() {
			private final Map<Object, FullNodeInfo> map = new HashMap<Object, FullNodeInfo>();

			@Override
			public void onModification(ModificationEvent<Object> e) {
				// Extract base information
				Collection<?> modCol = as(e.getSource(), Collection.class);
				Object modObj = e.getItem();
				// Act upon modification's kind
				switch (e.getKind()) {
				case Add:
					int size = modCol == null ? 1 : modCol.size();
					AtomicInteger index = new AtomicInteger(size);
					FullNodeInfo modNode = createSubItem(index, modObj, prop, pdesc, pembo);
					map.put(modObj, modNode);
					break;
				case Remove:
					FullNodeInfo removed = map.get(modObj);
					destroyMemoryOf(removed);
					break;
				}
			}
		});
	}

	/**
	 * Destroy memory of the given node.
	 *
	 * @param removed
	 *            the node to remove
	 */
	protected void destroyMemoryOf(FullNodeInfo removed) {
		NodeId remId = (NodeId) removed.get(AttributeIds.NodeId);
		for (ReferenceNode remRef : removed.getReferences())
			if (remRef.getIsInverse()) {
				NodeId targetId = remRef.getTargetId().local().get();
				FullNodeInfo target = nodes.get(targetId);
				List<ReferenceNode> refs = target.getReferences();
				for (ReferenceNode ref : refs.toArray(new ReferenceNode[0])) {
					NodeId refTargetId = ref.getTargetId().local().get();
					if (!refTargetId.equals(remId))
						continue;
					refs.remove(ref);
				}
			}
		nodes.remove(remId);
	}

	/**
	 * Casts one object as the given type.
	 *
	 * @param <T>
	 *            the generic type
	 * @param obj
	 *            the object to cast
	 * @param type
	 *            the type to cast to
	 * @return the finished object
	 */
	private static <T> T as(Object obj, Class<T> type) {
		if (type.isInstance(obj))
			return type.cast(obj);
		return null;
	}

	/**
	 * Creates the sub item.
	 *
	 * @param subIndex
	 *            the sub index
	 * @param subItem
	 *            the sub item
	 * @param prop
	 *            the prop
	 * @param pdesc
	 *            the property description
	 * @param pembo
	 *            the property options
	 * @return the full node info
	 */
	private FullNodeInfo createSubItem(AtomicInteger subIndex, Object subItem, FullNodeInfo prop, Description pdesc,
			EnumSet<Options> pembo) {
		String key;
		Object value;
		if (subItem instanceof Entry<?, ?>) {
			Entry<?, ?> e = (Entry<?, ?>) subItem;
			key = e.getKey() + "";
			value = e.getValue();
		} else {
			String name = subItem.getClass().getSimpleName();
			key = name + subIndex.getAndIncrement();
			value = subItem;
		}
		return createNodeAndAddIt(prop, key, value, pdesc, pembo);
	}

	/**
	 * Creates the node and add it.
	 *
	 * @param parent
	 *            the parent
	 * @param name
	 *            the name
	 * @param childObj
	 *            the child object
	 * @param desc
	 *            the description
	 * @param opts
	 *            the opts
	 * @return the full node info
	 */
	private FullNodeInfo createNodeAndAddIt(FullNodeInfo parent, String name, Object childObj, Description desc,
			EnumSet<Options> opts) {
		FullNodeInfo subNode = createNodeFrom(childObj, opts);
		if (desc != null)
			subNode.desc(LocalizedText.english(desc.value()));
		if (name != null) {
			subNode.displayName(new LocalizedText(null, name));
			subNode.browseName(new QualifiedName(objNs, name));
		}
		parent.addReference(Identifiers.Organizes, false, subNode, resolver);
		subNode.addReference(Identifiers.Organizes, true, parent, resolver);
		return subNode;
	}

	/**
	 * Creates or get an existing enumeration.
	 *
	 * @param pt
	 *            the type
	 * @return the node id
	 */
	private NodeId createOrGetEnum(Class<?> pt) {
		int currentObjId = objId.incrementAndGet();
		// Get base information
		String name = pt.getSimpleName();
		Object[] consts = pt.getEnumConstants();
		// Create enumeration type
		NodeId enumId = new NodeId(objNs, currentObjId);
		FullNodeInfo ent = new FullNodeInfo(enumId, NodeClass.DataType, name);
		// Read description
		Description edesc = pt.getAnnotation(Description.class);
		if (edesc != null)
			ent.desc(LocalizedText.english(edesc.value()));
		// Create string node
		NodeId enumStrId = new NodeId(objNs, currentObjId + "_strings");
		FullNodeInfo esv = new FullNodeInfo(enumStrId, NodeClass.Variable, "?");
		applyMethodCmpDefaults(esv, "EnumStrings");
		// Convert Java enumeration values to OPC UA ones
		LocalizedText[] texts = new LocalizedText[consts.length];
		for (int i = 0; i < texts.length; i++)
			texts[i] = new LocalizedText(null, consts[i] + "");
		esv.value(texts);
		// Set correct meta data
		esv.dataType(Identifiers.LocalizedText);
		esv.valueRank(ValueRanks.OneDimension.getValue());
		esv.arrayDimensions(new UInteger[] { uint(consts.length) });
		// Add string node to its parent and vice-versa
		ent.addReference(Identifiers.HasProperty, false, esv, resolver);
		esv.addReference(Identifiers.HasProperty, true, ent, resolver);
		// Inject into nodes
		nodes.put(enumId, ent);
		nodes.put(enumStrId, esv);
		// Set parent to be the enumeration type
		ent.addReference(Identifiers.HasSubtype, true, Identifiers.Enumeration, resolver);
		// Only return id for enumeration type
		return enumId;
	}

	/**
	 * Creates the method implementation.
	 *
	 * @param meth
	 *            the method
	 * @param obj
	 *            the object
	 * @param method
	 *            the method
	 */
	private void createMethodImpl(FullNodeInfo meth, final Object obj, final Method method) {
		meth.value(new CallableMethod() {
			@Override
			public void call(Variant[] args, List<Object> outs) throws Exception {
				// Create a container for the references
				List<out<?>> outRefs = new LinkedList<out<?>>();
				// Get raw parameters
				Object[] rawParams = new Object[method.getParameterCount()];
				for (int i = 0; i < rawParams.length; i++)
					if (i < args.length)
						rawParams[i] = args[i].getValue();
					else {
						out<?> ref = new out<Object>();
						rawParams[i] = ref;
						outRefs.add(ref);
					}
				// Invoke method
				Object retVal = method.invoke(obj, rawParams);
				// Build out list
				if (method.getReturnType() != void.class)
					outs.add(retVal);
				for (out<?> ref : outRefs)
					outs.add(ref.v);
			}
		});
	}

	/**
	 * Apply OPC UA type.
	 *
	 * @param prop
	 *            the property
	 * @param pt
	 *            the type
	 */
	private void applyOpcType(FullNodeInfo prop, Class<?> pt) {
		SimpleEntry<Class<?>, ValueRanks> info = getValueRank(pt);
		applyOpcType(prop, info.getValue(), toOpc(info.getKey()));
	}

	/**
	 * Apply OPC UA type.
	 *
	 * @param prop
	 *            the property
	 * @param rank
	 *            the rank
	 * @param dataType
	 *            the data type
	 */
	private void applyOpcType(FullNodeInfo prop, ValueRanks rank, NodeId dataType) {
		// Set value rank
		prop.valueRank(rank.getValue());
		// Set data type
		prop.dataType(dataType);
	}

	/**
	 * Gets the value rank.
	 *
	 * @param pt
	 *            the type
	 * @return the value rank
	 */
	private SimpleEntry<Class<?>, ValueRanks> getValueRank(Class<?> pt) {
		ValueRanks rank = ValueRanks.Scalar;
		if (pt.isArray()) {
			pt = pt.getComponentType();
			rank = ValueRanks.OneDimension;
		}
		return new SimpleEntry<Class<?>, ValueRanks>(pt, rank);
	}

	/**
	 * Apply method component defaults.
	 *
	 * @param node
	 *            the node
	 * @param title
	 *            the title
	 * @return the full node info
	 */
	private FullNodeInfo applyMethodCmpDefaults(FullNodeInfo node, String title) {
		node.addReference(Identifiers.HasTypeDefinition, false, Identifiers.PropertyType, resolver);
		node.displayName(new LocalizedText("", title));
		node.browseName(new QualifiedName(0, title));
		return node.desc(LocalizedText.NULL_VALUE).dataType(Identifiers.Argument)
				.valueRank(ValueRanks.OneDimension.getValue());
	}

	/**
	 * Find the annotation by type.
	 *
	 * @param <T>
	 *            the generic type
	 * @param annotations
	 *            the annotations
	 * @param type
	 *            the type
	 * @return the annotation
	 */
	private static <T> T find(Annotation[] annotations, Class<T> type) {
		for (Annotation annotation : annotations)
			if (annotation.annotationType() == type)
				return type.cast(annotation);
		return null;
	}

	/**
	 * Convert to camel case.
	 *
	 * @param text
	 *            the text
	 * @return the string
	 */
	private String toCamelCase(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}

	/**
	 * Call invoke on the property descriptor.
	 *
	 * @param pd
	 *            the property descriptor
	 * @param obj
	 *            the object
	 * @param resp
	 *            the response
	 * @return the object
	 */
	private Object callInvoke(final PropertyDescriptor pd, final Object obj, final ResponseFormat resp) {
		return new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				return invoke(pd, obj, resp);
			}
		};
	}

	/**
	 * Invoke the property.
	 *
	 * @param pd
	 *            the property descriptor
	 * @param obj
	 *            the object
	 * @param resp
	 *            the response
	 * @return the object
	 */
	private Object invoke(PropertyDescriptor pd, Object obj, ResponseFormat resp) {
		try {
			Object raw = pd.getReadMethod().invoke(obj);
			if (resp != null)
				raw = resp.value().convert(raw);
			if (raw instanceof Enum)
				raw = ((Enum<?>) raw).ordinal();
			return raw;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Convert to OPC UA node.
	 *
	 * @param type
	 *            the type
	 * @return the node id
	 */
	private NodeId toOpc(Class<?> type) {
		if (!classToOpc.containsKey(type))
			throw new UnsupportedOperationException("'" + type.getName() + "' is not mapped!");
		return classToOpc.get(type);
	}

	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	public Map<NodeId, FullNodeInfo> getNodes() {
		return nodes;
	}

	/**
	 * Gets the bean info.
	 *
	 * @param type
	 *            the type
	 * @return the bean info
	 */
	private BeanInfo getBeanInfo(Class<?> type) {
		try {
			return Introspector.getBeanInfo(type);
		} catch (IntrospectionException e) {
			log.error("Didn't get info from '" + type.getName() + "'!", e);
			return null;
		}
	}
}