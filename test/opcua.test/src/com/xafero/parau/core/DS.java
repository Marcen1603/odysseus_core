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
package com.xafero.parau.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.xafero.parau.model.Component;

/**
 * The container for declarative services.
 */
public class DS {

	/** The registered components. */
	public final Map<Class<?>, Object> Registered = new HashMap<Class<?>, Object>();

	/** The loader. */
	private final ClassLoader loader;

	/**
	 * Instantiates a new DS container.
	 *
	 * @param loader
	 *            the loader
	 */
	public DS(ClassLoader loader) {
		if (loader == null)
			loader = DS.class.getClassLoader();
		this.loader = loader;
	}

	/**
	 * Gets the implementation class.
	 *
	 * @param cmp
	 *            the component
	 * @return the implementation class
	 */
	public Class<?> getImplClass(Component cmp) {
		String implClName = cmp.impl().className();
		return getClass(implClName);
	}

	/**
	 * Gets the provided interface.
	 *
	 * @param cmp
	 *            the component
	 * @return the provided interface
	 */
	public Class<?> getProvidedIntf(Component cmp) {
		String provIntfName = cmp.svc().provide().interfaceName();
		return getClass(provIntfName);
	}

	/**
	 * Creates the implementation.
	 *
	 * @param cmp
	 *            the component
	 * @return the object
	 */
	public Object createImpl(Component cmp) {
		try {
			return getImplClass(cmp).newInstance();
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the class.
	 *
	 * @param className
	 *            the class name
	 * @return the class
	 */
	private Class<?> getClass(String className) {
		try {
			return Class.forName(className, true, loader);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates the implementation as.
	 *
	 * @param <T>
	 *            the generic type
	 * @param cmp
	 *            the component
	 * @param clazz
	 *            the class
	 * @return the t
	 */
	public <T> T createImplAs(Component cmp, Class<T> clazz) {
		return clazz.cast(createImpl(cmp));
	}

	/**
	 * Gets the referenced interface.
	 *
	 * @param cmp
	 *            the component
	 * @return the referenced interface
	 */
	public Class<?> getReferencedIntf(Component cmp) {
		String refIntfName = cmp.ref().interfaceName();
		return getClass(refIntfName);
	}

	/**
	 * Bind.
	 *
	 * @param cmp
	 *            the component
	 */
	public void bind(Component cmp) {
		// Standard binding
		if (cmp.ref() != null) {
			String bindMth = cmp.ref().bind();
			invoke(cmp, bindMth);
			return;
		}
		// Provider setup
		Object inst = createImpl(cmp);
		Class<?> provIntf = getProvidedIntf(cmp);
		Object casted = provIntf.cast(inst);
		Registered.put(provIntf, casted);
	}

	/**
	 * Unbind.
	 *
	 * @param cmp
	 *            the component
	 */
	public void unbind(Component cmp) {
		// Standard binding
		if (cmp.ref() != null) {
			String unbindMth = cmp.ref().unbind();
			invoke(cmp, unbindMth);
			return;
		}
		// Provider setup
		Class<?> provIntf = getProvidedIntf(cmp);
		Registered.remove(provIntf);
	}

	/**
	 * Invoke.
	 *
	 * @param cmp
	 *            the component
	 * @param methodName
	 *            the method name
	 */
	private void invoke(Component cmp, String methodName) {
		try {
			Object impl = createImpl(cmp);
			Class<?> refIntf = getReferencedIntf(cmp);
			Method method = impl.getClass().getMethod(methodName, refIntf);
			Object regImpl = Registered.get(refIntf);
			if (regImpl == null)
				throw new UnsupportedOperationException("No provider for '" + refIntf.getName() + "' found!");
			method.invoke(impl, regImpl);
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}