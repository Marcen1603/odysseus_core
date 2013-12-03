/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

public class RewriteConfiguration implements IOptimizationSetting<Set<String>>{

	private Set<String> rulesToApply = new HashSet<String>();
	private QueryBuildConfiguration cb;

	public RewriteConfiguration(){
		
	}
	
	public RewriteConfiguration(Set<String> rulesToApply) {
		this.rulesToApply = rulesToApply;
	}

	public Set<String> getRulesToApply() {
		return this.rulesToApply;
	}

	@Override
	public Set<String> getValue() {		
		return getRulesToApply();
	}

	public void setQueryBuildConfiguration(QueryBuildConfiguration cb) {
		this.cb = cb;
	}
	
	public QueryBuildConfiguration getQueryBuildConfiguration() {
		return cb;
	}
}
