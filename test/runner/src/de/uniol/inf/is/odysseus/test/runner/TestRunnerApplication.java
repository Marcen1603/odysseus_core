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
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Application for running all TestComponents. Recieves all registered
 * {@code TestComponents} over Declarative Services and executes them.
 * 
 * @author Timo Michelsen
 * 
 */
public class TestRunnerApplication implements IApplication {

	private static final List<TestComponentRunner> RUNNERS = Lists.newArrayList();
	private static final List<ITestComponent> COMPONENT_CACHE = Lists.newArrayList();

	private static final Logger LOG = LoggerFactory.getLogger(TestRunnerPlugIn.class);
	private static String[] args;

	private static boolean applicationStarted;

	@Override
	public Object start(IApplicationContext context) throws Exception {
		args = (String[]) context.getArguments().get("application.args");
		System.out.println("Starting Odysseus...");
		boolean result = startBundles(context.getBrandingBundle().getBundleContext());
		if (result) {
			System.out.println("Odysseus is up and running!");
			applicationStarted = true;
			System.out.println("Starting component tests...");
			if (!COMPONENT_CACHE.isEmpty()) {
				for (ITestComponent component : COMPONENT_CACHE) {
					startTestComponentImpl(component, args);
				}
				COMPONENT_CACHE.clear();
			}
			System.out.println("Component test finished.");
			return IApplication.EXIT_OK;
		}else{
			System.out.println("Odysseus could not be started! Test failed!");
		}
		return -1;
	}

	@Override
	public void stop() {
		LOG.debug("Test results");
		for (TestComponentRunner runner : RUNNERS) {
			String result;
			try {
				result = runner.getResult() != null ? runner.getResult().toString() : "null";
			} catch (IllegalStateException ignore) {
				result = "Not finished";
			}

			LOG.debug(runner.getTestComponent() + " : " + result);
		}
	}

	public void startTestComponent(ITestComponent component) {
		if (applicationStarted) {
			startTestComponentImpl(component, args);
		} else {
			COMPONENT_CACHE.add(component);
		}
	}

	private static void startTestComponentImpl(ITestComponent component, String[] args) {
		Preconditions.checkNotNull(component, "Component must not be null!");
		Preconditions.checkNotNull(args, "Args are not set here!");

		TestComponentRunner runner = new TestComponentRunner(component, args);
		RUNNERS.add(runner);

		LOG.debug("Start TestComponent" + component);
		runner.start();
	}

	private static boolean startBundles(final BundleContext context) {
		for (Bundle bundle : context.getBundles()) {
			boolean isFragment = bundle.getHeaders().get(Constants.FRAGMENT_HOST) != null;
			if (bundle != context.getBundle() && !isFragment && bundle.getState() == Bundle.RESOLVED) {
				try {
					bundle.start();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
}
