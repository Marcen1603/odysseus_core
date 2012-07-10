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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;

/**
 * factory for building cost model instances
 * @author Thomas Vogelgesang
 *
 */
public class QuerySharingCostModelFactory {
	
	public static final String STATIC_COST_MODEL = "static";
	public static final String NONE = "none";

	/**
	 * builds a cost model matching the given name
	 * @param costModelName the name of the cost model to be built
	 * @return a new cost model instance
	 */
	public IQuerySharingCostModel buildCostModel(String costModelName) {
		if (STATIC_COST_MODEL.equals(costModelName)) {
			return new StaticQuerySharingCostModel();
		} else if (NONE.equals(costModelName)) {
			return null;
		} else {
			throw new RuntimeException("unknown cost model: " + costModelName);
		}
	}

}
