package de.uniol.inf.is.odysseus.core.metadata;

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

public final class GenericCombinedMetaAttribute extends AbstractCombinedMetaAttribute implements InvocationHandler {

	private static final long serialVersionUID = 8008635741301906632L;

	final private Class<? extends IMetaAttribute>[] classes;
	final private IMetaAttribute[] metaAttributes;
	final private Method[] methodRetrieveValues;
	final private String name;
	final private Map<Method, Integer> methodAttributeMap;
	final private List<SDFMetaSchema> schema;
	
	final private Set<Method> gcmMethods;

	public GenericCombinedMetaAttribute(Class<? extends IMetaAttribute>[] classes, IMetaAttribute[] metaAttributes,
			String name, List<SDFMetaSchema> schema) {
		this.classes = classes;
		this.metaAttributes = metaAttributes;

		if (classes.length != metaAttributes.length) {
			throw new IllegalArgumentException("Currently, generic types are only supported for base meta types!");
		}

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

		this.methodRetrieveValues = new Method[classes.length];
		int i = 0;
		for (Class<? extends IMetaAttribute> c : classes) {
			try {
				methodRetrieveValues[i] = c.getMethod("retrieveValues", List.class);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		this.name = name;
		this.schema = schema;

		gcmMethods = new HashSet<>();

		for (Method m : IMetaAttribute.class.getMethods()) {
			this.gcmMethods.add(m);
		}

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
	public IMetaAttribute clone() {
		Object v = Proxy.newProxyInstance(new GenericClassLoader(metaAttributes), classes,
				new GenericCombinedMetaAttribute(this));
		return (IMetaAttribute) v;
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

class GenericClassLoader extends ClassLoader {
	private List<ClassLoader> classLoader = new ArrayList<>();

	public GenericClassLoader(IMetaAttribute[] metaAttribute) {
		for (IMetaAttribute m : metaAttribute) {
			classLoader.add(m.getClass().getClassLoader());
		}
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		try {
			return super.loadClass(name);
		} catch (ClassNotFoundException e) {
		}

		for (ClassLoader c : classLoader) {
			try {
				return c.loadClass(name);
			} catch (ClassNotFoundException e) {
			}
		}
		return super.loadClass(name);
	}
}