/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.developer.testcase.model;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class OperatorNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4126324464879459405L;
	private static final String MESSAGE = "Operator '%s' not found. Is the bundle containing the operator registered and started?";

	/**
	 * @param name
	 */
	public OperatorNotFoundException(String name) {
		super(String.format(MESSAGE, name));
	}

	/**
	 * @param cause
	 */
	public OperatorNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param name
	 * @param cause
	 */
	public OperatorNotFoundException(String name, Throwable cause) {
		super(String.format(MESSAGE, name), cause);
	}

	/**
	 * @param name
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public OperatorNotFoundException(String name, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(String.format(MESSAGE, name), cause, enableSuppression, writableStackTrace);
	}

}
