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
package de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;

/**
 * Since a query can have more than one root,
 * this class encapsulates strategies for
 * subscribing the default root to
 * the multiple roots of a query. For example:
 * 
 * A query has 3 roots. The default root of a
 * query should be an operator that prints
 * the results of the query to console. However,
 * should there be 3 different console printing
 * operators, one for each root of the query, or
 * should there be a single printing operator,
 * so that all the roots run into the same output
 * operator.
 * 
 * @author André Bolles
 *
 */
public class ParameterDefaultRootStrategy extends Setting<IDefaultRootStrategy> implements IQueryBuildSetting<IDefaultRootStrategy> {

	public ParameterDefaultRootStrategy(IDefaultRootStrategy strategy){
		super(strategy);
	}
}
