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

import java.util.List;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
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
	private final Object lock = new Object();
	private long lastadded = System.currentTimeMillis();

	private static final Logger LOG = LoggerFactory.getLogger(TestRunnerApplication.class);

	@Override
	public Object start(IApplicationContext context) throws Exception {
		boolean oneFailed = false;
		LOG.debug("Starting Odysseus...");
		startBundles(context.getBrandingBundle().getBundleContext());

		LOG.debug("Odysseus is up and running!");
		LOG.debug("Starting component tests...");

		oneFailed = executeComponents(context);
		LOG.debug("All tests were run.");
		if (oneFailed) {
			LOG.debug("At least one test failed!");
			return -1;
		}
		LOG.debug("All tests finished with no errors.");
		return IApplication.EXIT_OK;
	}

	private boolean executeComponents(IApplicationContext context) {
		boolean oneFailed = false;
		long current = System.currentTimeMillis();
		while ((current - lastadded) < 10000) {
			synchronized (components) {
				for (ITestComponent<BasicTestContext> component : components) {
					TestComponentRunner<BasicTestContext> runner = new TestComponentRunner<BasicTestContext>(component);					
					LOG.debug("Starting a new component test: " + component.getName());		
					LOG.debug("#######################################################################################");
					List<StatusCode> results = runner.run();
					LOG.debug("Total results for component \"" + component.getName()+"\"");
					int i=1;
					for (StatusCode code : results) {
						LOG.debug("Sub test "+i+": "+code.name());
						if (code != StatusCode.OK) {
							oneFailed = true;
						}
						i++;
					}					
					LOG.debug("#######################################################################################");
				}
				components.clear();
			}
			synchronized (lock) {
				LOG.debug("no components were added since "+(current - lastadded) + "ms. Waiting... " + Thread.currentThread().getName());
				try {
					lock.wait(1000);
					current = System.currentTimeMillis();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return oneFailed;
	}

	@Override
	public void stop() {

	}

	public void addTestComponent(ITestComponent<BasicTestContext> component) {		
		synchronized (components) {
			components.add(component);
			lastadded = System.currentTimeMillis();
		}
		synchronized (lock) {
			lock.notify();
		}

	}

	public void removeTestComponent(ITestComponent<BasicTestContext> component) {
		components.remove(component);
	}

	private void startBundles(final BundleContext context) {
		for (Bundle bundle : context.getBundles()) {
			boolean isFragment = bundle.getHeaders().get(Constants.FRAGMENT_HOST) != null;
			if (bundle != context.getBundle() && !isFragment && bundle.getState() == Bundle.RESOLVED) {
				try {
					if (bundle.getSymbolicName().startsWith("de.uniol.inf.is.odysseus")) {
						bundle.start();
					}
				} catch (BundleException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
