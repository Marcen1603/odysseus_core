/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.GenericOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;

public class LogicalOperatorBuilder implements BundleActivator, BundleListener {

	Logger logger = LoggerFactory.getLogger(LogicalOperatorBuilder.class);

	private List<Class<?>> loadedOperatorClasses = new ArrayList<>();

	private Map<String, Bundle> treatedBundles = new HashMap<>();
	private static int missingGetterMethodCount = 0;

	// private static final Map<String, GenericOperatorBuilder> operatorBuilders
	// = new HashMap<String, GenericOperatorBuilder>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		context.addBundleListener(this);
		searchBundles(context.getBundles());
	}

	private void searchBundles(Bundle[] bundles) {
		
		if (logger.isTraceEnabled()) {
			logger.trace("----------------------------------------------------------------------------------------");
			logger.trace("Handling the following bundles");
			for (Bundle bundle: bundles) {
				logger.trace("-->"+bundle.getSymbolicName());
			}
			logger.trace("----------------------------------------------------------------------------------------");
		}
		
		for (Bundle bundle : bundles) {
			if (bundle.getState() == Bundle.ACTIVE  || bundle.getState() == Bundle.RESOLVED) {
				searchBundle(bundle);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		logger.trace("Bundle changed "+event.getBundle().getSymbolicName()+" "+event.getType());		
		switch (event.getType()) {
		case BundleEvent.RESOLVED:
		case BundleEvent.STARTED:
			searchBundle(event.getBundle());
			break;
		case BundleEvent.STOPPING:
			removeBundle(event.getBundle());
			break;
		default:
			break;
		}
		
	}

	private void removeBundle(Bundle bundle) {
		
		treatedBundles.remove(bundle);
		
		Enumeration<URL> entries = bundle.findEntries("", "*.class", true);

		while (entries.hasMoreElements()) {
			URL curURL = entries.nextElement();
			// do not read targets from maven
			if (curURL.toString().contains("target/")) {
				continue;
			}
			if (curURL.toString().contains(".logicaloperator")) {
				Class<? extends ILogicalOperator> classObject = loadLogicalOperatorClass(bundle, curURL);
				if (classObject == null) {
					continue;
				}
				String operatorName = classObject.getAnnotation(LogicalOperator.class).name();
				OperatorBuilderFactory.removeOperatorBuilderByName(operatorName);
			} else if (curURL.toString().contains("/udf")) {
				@SuppressWarnings("rawtypes")
				Class<? extends IUserDefinedFunction> classObject = loadUDFClass(bundle, curURL);
				if (classObject != null) {
					String nameToRemove = classObject.getName();

					if (classObject.isAnnotationPresent(UserDefinedFunction.class)) {
						UserDefinedFunction annotation = classObject.getAnnotation(UserDefinedFunction.class);
						nameToRemove = annotation.name();
					}
					OperatorBuilderFactory.removeUdf(nameToRemove);
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	private Class<? extends ILogicalOperator> loadLogicalOperatorClass(Bundle bundle, URL curURL) {
		String file = curURL.getFile();
		int start = 1;
		String className = "";
		try {
			if (file.startsWith("/bin/")) {
				start = "/bin/".length();
			}
			// remove potential '/bin' and 'class' and change path to package
			// name
			className = file.substring(start, file.length() - 6).replace('/', '.');
			logger.trace("Trying to load class " + className);
			Class<?> classObject = bundle.loadClass(className);
			if (classObject.isAnnotationPresent(LogicalOperator.class) && ILogicalOperator.class.isAssignableFrom(classObject)) {
				return (Class<? extends ILogicalOperator>) classObject;
			}
		} catch (Exception e) {
			logger.error("Failed to load Class " + className + " Reason: " + e.getMessage());
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Class<? extends IUserDefinedFunction> loadUDFClass(Bundle bundle, URL curURL) {
		String file = curURL.getFile();
		int start = 1;
		try {
			if (file.startsWith("/bin/")) {
				start = "/bin/".length();
			}
			// remove potential '/bin' and 'class' and change path to package
			// name
			String className = file.substring(start, file.length() - 6).replace('/', '.');
			Class<?> classObject = bundle.loadClass(className);
			logger.debug("Loading UDF " + className);

			if (IUserDefinedFunction.class.isAssignableFrom(classObject)) {
				return (Class<? extends IUserDefinedFunction>) classObject;
			}
		} catch (Exception e) {
			// TODO anstaendige exception bauen
			throw new RuntimeException(e);
		}
		return null;
	}

	private synchronized void searchBundle(Bundle bundle) {
		
		if (treatedBundles.get(bundle.getSymbolicName()) != null) {
			return;
		}
		
		treatedBundles.put(bundle.getSymbolicName(), bundle);
		
		Enumeration<URL> entries = bundle.findEntries("/bin/", "*.class", true);
		// collect logical operators and register parameters first
		// add logical operators afterwards, because they may need the newly
		// registered parameters
		logger.trace("Looking for logical operators in "+bundle.getSymbolicName());
		if (entries == null) {
			entries = bundle.findEntries("/", "*.class", true);
			if (entries == null) {
				return;
			}
		}
		while (entries.hasMoreElements()) {
			URL curURL = entries.nextElement();
			if (curURL.toString().contains("/logicaloperator")) {
				Class<? extends ILogicalOperator> classObject = loadLogicalOperatorClass(bundle, curURL);
				if (classObject != null) {
					addLogicalOperator(classObject);
				}
			} else if (curURL.toString().contains("/udf")) {
				@SuppressWarnings("rawtypes")
				Class<? extends IUserDefinedFunction> classObject = loadUDFClass(bundle, curURL);
				if (classObject != null) {
					String nameToRegister = classObject.getName();

					if (classObject.isAnnotationPresent(UserDefinedFunction.class)) {
						UserDefinedFunction annotation = classObject.getAnnotation(UserDefinedFunction.class);
						nameToRegister = annotation.name();
					}
					OperatorBuilderFactory.putUdf(nameToRegister, classObject);
				}
			}

		}

	}

	private synchronized void addLogicalOperator(Class<? extends ILogicalOperator> curOp) {
		// avoid double loading of the same operator
		if (loadedOperatorClasses.contains(curOp)) {
			return;
		}
		loadedOperatorClasses.add(curOp);
		Map<Parameter, Method> parameters = new HashMap<Parameter, Method>();

		LogicalOperator logicalOperatorAnnotation = curOp.getAnnotation(LogicalOperator.class);

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(curOp, Object.class);
			for (PropertyDescriptor curProperty : beanInfo.getPropertyDescriptors()) {
				Method writeMethod = curProperty.getWriteMethod();
				if (writeMethod != null && writeMethod.isAnnotationPresent(Parameter.class)) {
					Parameter parameterAnnotation = writeMethod.getAnnotation(Parameter.class);
					parameters.put(parameterAnnotation, writeMethod);
					
					// security check
					if( curProperty.getReadMethod() == null ) {
						logger.trace("[Missing Get #{}] There is no getter-method for parameter '{}' of operator {}", new Object[]{++missingGetterMethodCount, parameterAnnotation.name(), curOp.getName()});
					}
				}
			}
			logger.debug("Create GenericOperatorBuilder Builder for " + curOp );//+ " with parameters " + parameters);
			String doc = logicalOperatorAnnotation.doc();
			if (doc == null || doc.isEmpty()) {
				logger.warn("Documentation for {} not available!", logicalOperatorAnnotation.name());
			}
            String url = logicalOperatorAnnotation.url();
            if (url == null || url.isEmpty()) {
                logger.warn("URL for {} not available!", logicalOperatorAnnotation.name());
            }
            GenericOperatorBuilder builder = new GenericOperatorBuilder(curOp, logicalOperatorAnnotation.name(), parameters, logicalOperatorAnnotation.minInputPorts(),
                    logicalOperatorAnnotation.maxInputPorts(), doc, url, logicalOperatorAnnotation.category());
			OperatorBuilderFactory.addOperatorBuilder(builder);
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
			logger.error("LogicalOperator " + curOp.getCanonicalName() + " not found. Check if core and core-server plugins were added as dependencies in its bundle");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
