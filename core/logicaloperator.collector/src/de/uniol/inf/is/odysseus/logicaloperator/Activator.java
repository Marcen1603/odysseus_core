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

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.GenericOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;

public class Activator implements BundleActivator, BundleListener {

	Logger logger = LoggerFactory.getLogger(Activator.class);

	//
	// private static final String SPLIT = "SPLIT";
	// private static final String ACCESS = "ACCESS";
	// private static final String FILEACCESS = "FILEACCESS";
	// private static final String SELECT = "SELECT";
	// private static final String JOIN = "JOIN";
	// private static final String MAP = "MAP";
	// private static final String PROJECT = "PROJECT";
	// private static final String UNION = "UNION";
	// private static final String RENAME = "RENAME";
	// private static final String WINDOW = "WINDOW";
	// private static final String DIFFERENCE = "DIFFERENCE";
	// private static final String EXISTENCE = "EXISTENCE";
	// private static final String AGGREGATION = "AGGREGATION";
	// private static final String FILESINK = "FILESINK";

	private static final Map<String, GenericOperatorBuilder> operatorBuilders = new HashMap<String, GenericOperatorBuilder>();

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

	@SuppressWarnings("unchecked")
	private void removeBundle(Bundle bundle) {
		Enumeration<URL> entries = bundle.findEntries(
				"de.uniol.inf.is.odysseus", "*.class", true);

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
				operatorBuilders.remove(operatorName);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends ILogicalOperator> loadLogicalOperatorClass(
			Bundle bundle, URL curURL) {
		String file = curURL.getFile();
		try {
			// remove '/bin' and 'class' and change path to package name
			Class<?> classObject = bundle.loadClass(file.substring(5,
					file.length() - 6).replace('/', '.'));
			if (classObject.isAnnotationPresent(LogicalOperator.class)
					&& ILogicalOperator.class.isAssignableFrom(classObject)) {
				return (Class<? extends ILogicalOperator>) classObject;
			}
		} catch (Exception e) {
			// TODO anstaendige exception bauen
			throw new RuntimeException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void searchBundle(Bundle bundle) {
		Enumeration<URL> entries = bundle.findEntries(
				"/bin/de/uniol/inf/is/odysseus", "*.class",
				true);
		// collect logical operators and register parameters first
		// add logical operators afterwards, because they may need the newly
		// registered parameters
		if (entries == null) {
			return;
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
//			logger.debug("Create GenericOperatorBuilder Builder for " + curOp
//					+ " with parameters " + parameters);
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
