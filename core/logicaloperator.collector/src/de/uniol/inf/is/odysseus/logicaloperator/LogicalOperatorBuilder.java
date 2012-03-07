/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.logicaloperator;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
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
		for (Bundle bundle : bundles) {
			if (bundle.getState() == Bundle.ACTIVE) {
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
		switch (event.getType()) {
		case BundleEvent.STARTED:
			searchBundle(event.getBundle());
			break;
		case BundleEvent.STOPPING:
			removeBundle(event.getBundle());
			break;
		default:
			;
		}
	}

	private void removeBundle(Bundle bundle) {
		Enumeration<URL> entries = bundle.findEntries(
				"de.uniol.inf.is.odysseus.server", "*.class", true);

		while (entries.hasMoreElements()) {
			URL curURL = entries.nextElement();
			if (curURL.toString().contains(".logicaloperator")) {
				Class<? extends ILogicalOperator> classObject = loadLogicalOperatorClass(
						bundle, curURL);
				if (classObject == null) {
					continue;
				}
				String operatorName = classObject.getAnnotation(
						LogicalOperator.class).name();
				OperatorBuilderFactory.removeOperatorBuilderType(operatorName);
			}else if (curURL.toString().contains("/udf")) {
				@SuppressWarnings("rawtypes")
				Class<? extends IUserDefinedFunction> classObject = loadUDFClass(
						bundle, curURL);
				if (classObject == null) {
					continue;
				} else {
					String nameToRemove = classObject.getName();

					if (classObject
							.isAnnotationPresent(UserDefinedFunction.class)) {
						UserDefinedFunction annotation = classObject
								.getAnnotation(UserDefinedFunction.class);
						nameToRemove = annotation.name();
					}
					OperatorBuilderFactory.removeUdf(nameToRemove);
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	private Class<? extends ILogicalOperator> loadLogicalOperatorClass(
			Bundle bundle, URL curURL) {
		String file = curURL.getFile();
		int start = 1;
		String className = "";
		try {
			if (file.startsWith("/bin/")) {
				start = "/bin/".length();
			}
			// remove potential '/bin' and 'class' and change path to package
			// name
			className = file.substring(start, file.length() - 6).replace('/',
					'.');
			logger.trace("Trying to load class " + className);
			Class<?> classObject = bundle.loadClass(className);
			if (classObject.isAnnotationPresent(LogicalOperator.class)
					&& ILogicalOperator.class.isAssignableFrom(classObject)) {
				return (Class<? extends ILogicalOperator>) classObject;
			}
		} catch (Exception e) {
			logger.error("Failed to load Class " + className + " Reason: "
					+ e.getMessage());
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Class<? extends IUserDefinedFunction> loadUDFClass(Bundle bundle,
			URL curURL) {
		String file = curURL.getFile();
		int start = 1;
		try {
			if (file.startsWith("/bin/")) {
				start = "/bin/".length();
			}
			// remove potential '/bin' and 'class' and change path to package
			// name
			String className = file.substring(start, file.length() - 6)
					.replace('/', '.');
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

	private void searchBundle(Bundle bundle) {
		Enumeration<URL> entries = bundle.findEntries(
				"/bin/de/uniol/inf/is/odysseus", "*.class", true);
		// collect logical operators and register parameters first
		// add logical operators afterwards, because they may need the newly
		// registered parameters
		if (entries == null) {
			entries = bundle.findEntries("/de/uniol/inf/is/odysseus",
					"*.class", true);
			if (entries == null) {
				return;
			}
		}
		while (entries.hasMoreElements()) {
			URL curURL = entries.nextElement();
			if (curURL.toString().contains("/logicaloperator")) {
				Class<? extends ILogicalOperator> classObject = loadLogicalOperatorClass(
						bundle, curURL);
				if (classObject == null) {
					continue;
				} else {
					addLogicalOperator(classObject);
				}
			} else if (curURL.toString().contains("/udf")) {
				@SuppressWarnings("rawtypes")
				Class<? extends IUserDefinedFunction> classObject = loadUDFClass(
						bundle, curURL);
				if (classObject == null) {
					continue;
				} else {
					String nameToRegister = classObject.getName();

					if (classObject
							.isAnnotationPresent(UserDefinedFunction.class)) {
						UserDefinedFunction annotation = classObject
								.getAnnotation(UserDefinedFunction.class);
						nameToRegister = annotation.name();
					}
					OperatorBuilderFactory.putUdf(nameToRegister, classObject);
				}
			}

		}

	}

	private void addLogicalOperator(Class<? extends ILogicalOperator> curOp) {
		Map<Parameter, Method> parameters = new HashMap<Parameter, Method>();

		LogicalOperator logicalOperatorAnnotation = curOp
				.getAnnotation(LogicalOperator.class);

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(curOp, Object.class);
			for (PropertyDescriptor curProperty : beanInfo
					.getPropertyDescriptors()) {
				Method writeMethod = curProperty.getWriteMethod();
				if (writeMethod != null
						&& writeMethod.isAnnotationPresent(Parameter.class)) {
					Parameter parameterAnnotation = writeMethod
							.getAnnotation(Parameter.class);
					parameters.put(parameterAnnotation, writeMethod);
				}
			}
			logger.debug("Create GenericOperatorBuilder Builder for " + curOp
					+ " with parameters " + parameters);
			GenericOperatorBuilder builder = new GenericOperatorBuilder(curOp,
					parameters, logicalOperatorAnnotation.minInputPorts(),
					logicalOperatorAnnotation.maxInputPorts());

			OperatorBuilderFactory.putOperatorBuilderType(
					logicalOperatorAnnotation.name(), builder);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
