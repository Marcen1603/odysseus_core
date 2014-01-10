/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.test.runner;

import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.wiring.BundleWiring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.test.StatusCode;
import de.uniol.inf.is.odysseus.test.component.ITestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;

/**
 * Application for running all TestComponents. Recieves all registered
 * {@code TestComponents} over Declarative Services and executes them.
 * 
 * @author Timo Michelsen, Dennis Geesen
 * 
 */
public class TestRunnerApplication implements IApplication {

	private static List<ITestComponent<BasicTestContext>> components = Lists.newArrayList();

	private static final Logger LOG = LoggerFactory.getLogger(TestRunnerApplication.class);

	@Override
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("Starting Odysseus...");
		startBundles(context.getBrandingBundle().getBundleContext());
		trackAllTestComponentServices(context);
		boolean oneFailed = false;

		System.out.println("Odysseus is up and running!");
		System.out.println("Starting component tests...");
		for (ITestComponent<BasicTestContext> component : components) {
			TestComponentRunner<BasicTestContext> runner = new TestComponentRunner<BasicTestContext>(component);
			LOG.debug("Scheduled a component test: " + component.getName());
			List<StatusCode> results = runner.run();
			LOG.debug("Total results for component " + component.getName());
			for (StatusCode code : results) {
				LOG.debug(code.name());
				if (code != StatusCode.OK) {
					oneFailed = true;
				}
			}
			LOG.debug("-------------------");
		}
		System.out.println("All tests were run.");
		if (oneFailed) {
			System.out.println("At least one test failed!");
			return -1;
		} else {
			System.out.println("All tests finished with no errors.");
			return IApplication.EXIT_OK;
		}

	}

	private void trackAllTestComponentServices(IApplicationContext context) {
//		BundleContext bc = context.getBrandingBundle().getBundleContext();
//		ServiceTracker st = new ServiceTracker(bc, ITestComponent.class.getName(), null);
//		ServiceReference[] map = st.getServiceReferences();
//
//		System.out.println("...");
	}

	@Override
	public void stop() {

	}

	public void addTestComponent(ITestComponent<BasicTestContext> component) {
		System.out.println("ADD COMPONENT:" + component);
		components.add(component);
	}

	public void removeTestComponent(ITestComponent<BasicTestContext> component) {
		components.remove(component);
	}

	private void startBundles(final BundleContext context) {
		for (Bundle bundle : context.getBundles()) {
			boolean isFragment = bundle.getHeaders().get(Constants.FRAGMENT_HOST) != null;
			if (bundle != context.getBundle() && !isFragment && bundle.getState() == Bundle.INSTALLED) {
				try {
					ClassLoader classLoader = bundle.adapt(BundleWiring.class).getClassLoader();
					Enumeration<URL> entries = bundle.findEntries("/de/uniol/inf/is/odysseus", "*.class", true);
					while (entries.hasMoreElements()) {
						String file = entries.nextElement().getFile();
						int start = 1;
						if (file.startsWith("/bin/")) {
							start = "/bin/".length();
						}
						String className = file.substring(start, file.length() - 6).replace('/', '.');
						Class<?> clazz = classLoader.loadClass(className);
						if(ITestComponent.class.isAssignableFrom(clazz)){
							System.out.println("FOUND COMPONENT: "+clazz);
						}

					}

					bundle.start();
				} catch (Exception e) {
				}
			}
		}
	}
}
