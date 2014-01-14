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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.test.StatusCode;
import de.uniol.inf.is.odysseus.test.component.ITestComponent;
import de.uniol.inf.is.odysseus.test.context.ITestContext;

/**
 * Wrapper class for an implementation of ITestComponent.
 * 
 * @author Dennis Geesen
 * 
 */
public class TestComponentRunner<T extends ITestContext> {

	private static final Logger LOG = LoggerFactory.getLogger(TestComponentRunner.class);
	private final ITestComponent<T> component;
	private T context;

	public TestComponentRunner(ITestComponent<T> component) {
		Preconditions.checkNotNull(component, "Component must not be null!");
		this.component = component;
		this.context = component.createTestContext();
		component.setupTest(context);
	}

	public List<StatusCode> run() {
		LOG.debug("Start Testcomponent '" + component + "'");
		long startTime = System.nanoTime();
		List<StatusCode> testResult = new ArrayList<>();
		try {
			if (component.isActivated()) {
				testResult = component.runTest(context);
			}else{
				LOG.debug("Component is deactivated, skipping...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Arrays.asList(StatusCode.EXCEPTION_DURING_TEST);
		}
		long elapsedTime = System.nanoTime() - startTime;
		LOG.debug("End Testcomponent '" + component + "'. Duration = " + (elapsedTime / 1000000) + " ms");
		return testResult;
	}

	public final ITestComponent<T> getTestComponent() {
		return component;
	}
}
