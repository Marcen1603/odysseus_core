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
package de.uniol.inf.is.odysseus.server.opcua.wrappers;

import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.Embed;

/**
 * The Odysseus system.
 */
@Description("This is a core information point")
public class System {

	/**
	 * Gets the bundle.
	 *
	 * @return the bundle
	 */
	@Embed
	@Description("The bundle of the plugin")
	public Bundle getBundle() {
		return new Bundle();
	}

	/**
	 * Gets the executor.
	 *
	 * @return the executor
	 */
	@Embed
	@Description("The currently used executor")
	public Executor getExecutor() {
		return new Executor();
	}

	/**
	 * Gets the compiler.
	 *
	 * @return the compiler
	 */
	@Embed
	@Description("The currently used compiler")
	public Compiler getCompiler() {
		return new Compiler();
	}


	/**
	 * Gets the user manager.
	 *
	 * @return the user manager
	 */
	@Embed
	@Description("The user manager")
	public UserManager getUserManager() {
		return new UserManager();
	}

	/**
	 * Gets the security.
	 *
	 * @return the security
	 */
	@Embed
	@Description("The currently used security")
	public Security getSecurity() {
		return new Security();
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	@Embed
	@Description("The currently used operator")
	public Operator getOperator() {
		return new Operator();
	}
}