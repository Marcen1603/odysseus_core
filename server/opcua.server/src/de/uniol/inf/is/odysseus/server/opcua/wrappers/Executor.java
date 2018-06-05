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

import java.util.Map;

import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.Format;
import com.xafero.turjumaan.server.java.api.NotCacheable;
import com.xafero.turjumaan.server.java.api.ResponseFormat;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.server.opcua.binding.ExecutorServiceBinding;

/**
 * The executor of Odysseus.
 */
@Description("The executor for Odysseus")
public class Executor {

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@NotCacheable
	@Description("The name of the executor")
	public String getName() {
		return getExecutor().getName();
	}

	/**
	 * Gets the user-defined functions.
	 *
	 * @return the udfs
	 */
	@NotCacheable
	@Description("Something nice")
	public String[] getUdfs() {
		return getExecutor().getUdfs().toArray(new String[0]);
	}

	/**
	 * Gets the pre-transformation handler names.
	 *
	 * @return the pre-transformation handler names
	 */
	@NotCacheable
	@Description("Something awful")
	public String[] getPreTransformationHandlerNames() {
		return getExecutor().getPreTransformationHandlerNames().toArray(new String[0]);
	}

	/**
	 * Gets the query build configurations.
	 *
	 * @return the query build configurations
	 */
	@NotCacheable
	@ResponseFormat(Format.XML)
	@Description("All build configurations")
	public Map<String, IQueryBuildConfigurationTemplate> getQueryBuildConfigurations() {
		return getExecutor().getQueryBuildConfigurations();
	}

	/**
	 * Gets the rewrite rules.
	 *
	 * @return the rewrite rules
	 */
	@NotCacheable
	@Description("The list of rewrite rules that are available in the system")
	public String[] getRewriteRules() {
		return getExecutor().getRewriteRules().toArray(new String[0]);
	}

	/**
	 * Gets the executor.
	 *
	 * @return the executor
	 */
	private IServerExecutor getExecutor() {
		return (IServerExecutor) ExecutorServiceBinding.getExecutor();
	}
}