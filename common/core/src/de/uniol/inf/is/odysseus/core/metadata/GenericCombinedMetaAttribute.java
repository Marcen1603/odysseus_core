package de.uniol.inf.is.odysseus.core.metadata;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;

/*
 * This class combines different meta data classes at runtime.
 *
 * Important: Only one of these meta data classes is allowed to implement Comparable
 *
 * @author: Marco Grawunder
 */

public final class GenericCombinedMetaAttribute extends AbstractCombinedMetaAttribute
		implements InvocationHandler, Serializable, Comparable<IMetaAttribute> {

	private static final long serialVersionUID = 8008635741301906632L;

	final private Class<? extends IMetaAttribute>[] classes;
	final private IMetaAttribute[] metaAttributes;
	final private String name;
	final private List<SDFMetaSchema> schema;

	transient private Map<Method, Integer> methodAttributeMap;
	transient private Method[] methodRetrieveValues;
	transient private Set<Method> gcmMethods;

	transient private Method compareMethod;

	private int compareMethodMetadataPos;

	public GenericCombinedMetaAttribute(Class<? extends IMetaAttribute>[] classes, IMetaAttribute[] metaAttributes,
			String name, List<SDFMetaSchema> schema) {
		this.classes = classes;
		this.metaAttributes = metaAttributes;

		if (classes.length != metaAttributes.length) {
			throw new IllegalArgumentException("Currently, generic types are only supported for base meta types!");
		}

		initMethodAttributeMap(classes);
		initMethodRetrieveValues(classes);
		initCompareableMethod(classes);

		this.name = name;
		this.schema = schema;

		initGcmMethods();

	}

	public GenericCombinedMetaAttribute(GenericCombinedMetaAttribute other) {

		this.metaAttributes = new IMetaAttribute[other.metaAttributes.length];
		for (int i = 0; i < metaAttributes.length; i++) {
			this.metaAttributes[i] = other.metaAttributes[i].clone();
		}
		// These fields are have immutable values, so reference copy is enough

		this.classes = other.classes;
		this.methodRetrieveValues = other.methodRetrieveValues;
		this.name = other.name;

		this.methodAttributeMap = other.methodAttributeMap;
		this.schema = other.schema;

		this.gcmMethods = other.gcmMethods;
		this.compareMethod = other.compareMethod;

	}


	private void initCompareableMethod(Class<? extends IMetaAttribute>[] classes) {
		int i = 0;
		for (Class<? extends IMetaAttribute> c : classes) {
			Method[] methods = c.getMethods();
			for (Method m:methods){
				if (m.getName().equals("compareTo")){
					if (this.compareMethod == null){
						this.compareMethod = m;
						this.compareMethodMetadataPos = i;
					}else{
						throw new RuntimeException("Cannot combine meta attributes with more than one compareTo method! Found "+compareMethod+" and "+m);
					}
				}
			}
			i++;
		}
	}

	private void initGcmMethods() {
		gcmMethods = new HashSet<>();

		for (Method m : IMetaAttribute.class.getMethods()) {
			this.gcmMethods.add(m);
		}

		// add comparable manually
		this.gcmMethods.add(compareMethod);
		try {
			this.gcmMethods.add(Object.class.getMethod("toString"));
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		try {
			this.gcmMethods.add(Object.class.getMethod("equals", Object.class));
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	private void initMethodRetrieveValues(Class<? extends IMetaAttribute>[] classes) {
		this.methodRetrieveValues = new Method[classes.length];
		int i = 0;
		for (Class<? extends IMetaAttribute> c : classes) {
			try {
				methodRetrieveValues[i++] = c.getMethod("retrieveValues", List.class);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	private void initMethodAttributeMap(Class<? extends IMetaAttribute>[] classes) {
		this.methodAttributeMap = new HashMap<>();
		for (int i = 0; i < classes.length; i++) {
			Method[] methods = classes[i].getDeclaredMethods();
			for (Method m : methods) {
				if (!methodAttributeMap.containsKey(m)) {
					methodAttributeMap.put(m, i);
				} else {
					throw new IllegalArgumentException(
							"Combined metadata cannot contain metadata with same method signatures!");
				}
			}
		}
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	private void readObject(ObjectInputStream ois) throws IOException {
		try {
			ois.defaultReadObject();
			this.initMethodAttributeMap(classes);
			this.initMethodRetrieveValues(classes);
			this.initGcmMethods();
		} catch (ClassNotFoundException e) {
			throw new IOException("No class found. HELP!!");
		}
	}

	@Override
	public int compareTo(IMetaAttribute o) {
		try {
			return (int) this.compareMethod.invoke(this.metaAttributes[compareMethodMetadataPos],o);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Cannot compare values", e);
		}
	}


	@Override
	public IMetaAttribute createInstance() {
		return this.clone();
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		for (int i = 0; i < methodRetrieveValues.length; i++) {
			try {
				methodRetrieveValues[i].invoke(metaAttributes[i], values);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void writeValues(List<Tuple<?>> values) {
		for (int i = 0; i < values.size(); i++) {
			this.metaAttributes[i].writeValue(values.get(i));
		}

	}

	@Override
	public <K> K getValue(int subtype, int index) {
		return metaAttributes[subtype].getValue(0, index);
	}

	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		for (IMetaAttribute a : metaAttributes) {
			list.addAll(a.getInlineMergeFunctions());
			list.addAll(a.getInlineMergeFunctions());
		}
		return list;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();

		if (metaAttributes.length > 0){
			ret.append(metaAttributes[0]);
		}
		for (int i=1;i<metaAttributes.length;i++) {
			ret.append(" | ").append(metaAttributes[i]);
		}
		return ret.toString();

	}

	@Override
	public IMetaAttribute clone() {
		Object v = Proxy.newProxyInstance(new GenericClassLoader(metaAttributes), classes,
				new GenericCombinedMetaAttribute(this));
		return (IMetaAttribute) v;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		} else if(!(obj instanceof GenericCombinedMetaAttribute)) {
			return false;
		}

		GenericCombinedMetaAttribute other = (GenericCombinedMetaAttribute) obj;
		if(metaAttributes.length != other.metaAttributes.length) {
			return false;
		}
		for(int i = 0; i < metaAttributes.length; i++) {
			if(!metaAttributes[i].equals(other.metaAttributes[i])) {
				return false;
			}
		}
		return true;
	}

	public static Object newInstance(List<Class<? extends IMetaAttribute>> classes,
			List<IMetaAttribute> metaAttribute) {
		StringBuilder name = new StringBuilder();
		List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(metaAttribute.size());
		IMetaAttribute[] mAttribute = new IMetaAttribute[metaAttribute.size()];
		for (int i = 0; i < metaAttribute.size(); i++) {
			name.append(metaAttribute.get(i).getName());
			schema.addAll(metaAttribute.get(i).getSchema());
			mAttribute[i] = metaAttribute.get(i);
		}

		@SuppressWarnings("unchecked")
		Class<? extends IMetaAttribute>[] mclasses = new Class[classes.size()];
		for (int i = 0; i < classes.size(); i++) {
			mclasses[i] = classes.get(i);
		}

		Object v = Proxy.newProxyInstance(new GenericClassLoader(mAttribute), mclasses,
				new GenericCombinedMetaAttribute(mclasses, mAttribute, name.toString(), schema));
		return v;
	}

	// @SuppressWarnings("unchecked")
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// Must test overwritten methods first!!

		if (gcmMethods.contains(method)) {
			return method.invoke(this, args);
		}

		// Now there should only be methods from the base metadata types remaing

		Integer pos = this.methodAttributeMap.get(method);
		if (pos != null) {
			return method.invoke(metaAttributes[pos], args);
		}

		throw new RuntimeException("Method " + method + " not defined for Generic meta data type!");
	}

}
