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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Wrapper class for an implementation of ITestComponent.
 * 
 * @author Timo Michelsen, Alexander Funk
 *
 */
public class TestComponentRunner extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(TestComponentRunner.class);
	private final ITestComponent component;
	private final String[] args;
	private Object testResult;
	
	public TestComponentRunner( ITestComponent component, String[] args ) {
		Preconditions.checkNotNull(component, "Component must not be null!");
		Preconditions.checkNotNull(args, "Args must not be null");
		
		setName("TestComponentRunner:" + component);
		this.component = component;
		this.args = args;
		
		component.setUp();
	}
	
	@Override
	public void run() {
		LOG.debug("Start Testcomponent '" + component + "'. Arguments = '" + argsToString(args) + "'");
		long startTime = System.nanoTime();
		testResult = component.startTesting(args);
		long elapsedTime = System.nanoTime() - startTime;
		LOG.debug("End Testcomponent '" + component + "'. Duration = " + toMillis(elapsedTime) + " ms");
	}
	
	private static long toMillis(long elapsedTime) {
        return elapsedTime / 1000000;
    }

    public final Object getResult() {
		if(testResult == null ) {
			throw new IllegalStateException("TestComponent " + component + " not finished.");
		}
		return testResult;
	}
	
	public final ITestComponent getTestComponent() {
		return component;
	}
	
	private static String argsToString( String[] args ) {
	    StringBuilder sb = new StringBuilder();
	    for( String str: args ) {
	        sb.append(str).append(" ");
	    }
	    return sb.toString();
	}
}
