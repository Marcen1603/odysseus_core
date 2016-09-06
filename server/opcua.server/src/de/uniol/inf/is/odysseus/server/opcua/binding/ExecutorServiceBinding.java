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
package de.uniol.inf.is.odysseus.server.opcua.binding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;

/**
 * The binding for the executor service.
 */
public class ExecutorServiceBinding {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(ExecutorServiceBinding.class);

	/** The executor. */
	private static IExecutor executor;

	/**
	 * Bind executor.
	 *
	 * @param exec
	 *            the executor
	 */
	public void bindExecutor(IExecutor exec) {
		log.info("Got executor ({})...", exec.getClass().getSimpleName());
		executor = exec;
	}

	/**
	 * Unbind executor.
	 *
	 * @param exec
	 *            the executor
	 */
	public void unbindExecutor(IExecutor exec) {
		log.info("Lost executor ({})...", exec.getClass().getSimpleName());
		executor = null;
	}

	/**
	 * Gets the executor.
	 *
	 * @return the executor
	 */
	public static IExecutor getExecutor() {
		return executor;
	}
}