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
package de.uniol.inf.is.odysseus.transform;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.ITransformationRule;

public class TransformationActivator implements BundleActivator, BundleListener {

	private enum Mode {
		ADD, REMOVE
	};

	Logger logger = LoggerFactory.getLogger(TransformationActivator.class);

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		TransformationActivator.context = bundleContext;

		// init rule flow (order is important)
		for (TransformRuleFlowGroup e : TransformRuleFlowGroup.values()) {
			TransformationInventory.getInstance().addRuleFlowGroup(e);
		}

		// TAccessAOGenericRule handles GenericPush and GenericPull-Wrapper
		WrapperRegistry.registerWrapper(Constants.GENERIC_PUSH);
		WrapperRegistry.registerWrapper(Constants.GENERIC_PULL);

		context.addBundleListener(this);
		final Bundle[] b = context.getBundles();
		searchBundles(b, Mode.ADD);	
	}

	private void searchBundles(Bundle[] bundles, Mode mode) {
		for (Bundle bundle : bundles) {
			if (bundle.getState() == Bundle.ACTIVE || bundle.getState() == Bundle.RESOLVED) {
				searchBundle(bundle, mode);
			}
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		searchBundles(bundleContext.getBundles(), Mode.REMOVE);
		TransformationActivator.context = null;
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		switch (event.getType()) {
		case BundleEvent.LAZY_ACTIVATION:
		case BundleEvent.STARTED:
			searchBundle(event.getBundle(), Mode.ADD);
			break;
		case BundleEvent.STOPPING:
			searchBundle(event.getBundle(), Mode.REMOVE);
			break;
		default:
			break;
		}
	}

	private synchronized void searchBundle(Bundle bundle, Mode mode) {
//		String[] pathes = new String[] { "/de/uniol/inf/is/odysseus" };
		String[] pathes = new String[] { "" };
		String[] prefixes = new String[] { "", "/bin" };
		for (String path : pathes) {
			for (String prefix : prefixes) {

				Enumeration<URL> entries;
				entries = bundle.findEntries(prefix + path, "*.class", true);

				if (entries == null) {
					continue;
				}

				while (entries.hasMoreElements()) {
					URL curURL = entries.nextElement();
					if (curURL.toString().contains("/rule")
							|| curURL.toString().contains("/transform")) {
						Class<? extends ITransformationRule> classObject = loadRuleClass(
								bundle, curURL);
						if (classObject != null
								&& !Modifier.isInterface(classObject
										.getModifiers())
								&& !Modifier.isAbstract(classObject
										.getModifiers()))
							try {
								@SuppressWarnings("rawtypes")
								IRule rule = (IRule) classObject.newInstance();
								if (mode == Mode.ADD) {
									TransformationInventory.getInstance()
											.addRule(rule);
									logger.trace("Added Rule " + classObject);
								} else {
									TransformationInventory.getInstance()
											.removeRule(rule);
									logger.trace("Removed Rule " + classObject);

								}

							} catch (Exception e) {
								logger.error("Error loading rule "
										+ classObject, e);
							}
					}
				}

			}
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends ITransformationRule> loadRuleClass(Bundle bundle,
			URL curURL) {
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
			//logger.trace("Trying to load class " + className);
			Class<?> classObject = bundle.loadClass(className);
			if (ITransformationRule.class.isAssignableFrom(classObject)) {
				//logger.trace(className+" loaded");
				return (Class<? extends ITransformationRule>) classObject;
			}
		} catch (Exception e) {
			// can be ignored here
		}
		return null;
	}

}
