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

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;

/**
 * The binding for the operator service.
 */
public class OperatorServiceBinding {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(OperatorServiceBinding.class);

	/** The operator. */
	private static IOperatorBuilderFactory operator;

	/**
	 * Bind operator.
	 *
	 * @param op
	 *            the operator
	 */
	public void bindOperator(IOperatorBuilderFactory op) {
		log.info("Got operator ({})...", op.getClass().getSimpleName());
		operator = op;
	}

	/**
	 * Unbind operator.
	 *
	 * @param op
	 *            the operator
	 */
	public void unbindOperator(IOperatorBuilderFactory op) {
		log.info("Lost operator ({})...", op.getClass().getSimpleName());
		operator = null;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public static IOperatorBuilderFactory getOperator() {
		return operator;
	}
}