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
import com.xafero.turjumaan.server.java.api.NotCacheable;

import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.server.opcua.binding.ExecutorServiceBinding;

/**
 * The query compiler of Odysseus.
 */
@Description("The compiler for Odysseus")
public class Compiler {

	/**
	 * Gets the query parsers.
	 *
	 * @return the query parsers
	 */
	@NotCacheable
	@Description("The supported query parsers")
	public String[] getQueryParsers() {
		return getCompiler().getSupportedQueryParser().toArray(new String[0]);
	}

	/**
	 * Gets the rewrite rules.
	 *
	 * @return the rewrite rules
	 */
	@NotCacheable
	@Description("The rewrite rules")
	public String[] getRewriteRules() {
		return getCompiler().getRewriteRules().toArray(new String[0]);
	}

	/**
	 * Gets the infos.
	 *
	 * @return the infos
	 */
	@NotCacheable
	@Description("Informations of the registered modules and the current state of this object")
	public String getInfos() {
		return getCompiler().getInfos();
	}

	/**
	 * Gets the compiler.
	 *
	 * @return the compiler
	 */
	private ICompiler getCompiler() {
		return ((IServerExecutor) ExecutorServiceBinding.getExecutor()).getCompiler();
	}
}