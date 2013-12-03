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

import de.uniol.inf.is.odysseus.core.server.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration.IExecutionSetting;

/**
 * {@link IQueryBuildSetting} which provides an
 * {@link IBufferPlacementStrategy} for creating the query.
 * 
 * @author Wolf Bauer
 * 
 */
public class ParameterBufferPlacementStrategy extends Setting<IBufferPlacementStrategy> implements
		IQueryBuildSetting<IBufferPlacementStrategy>, IExecutionSetting<IBufferPlacementStrategy> {

	private String name;

	/**
	 * Creates a ParameterBufferPlacementStrategy.
	 * 
	 * @param object
	 *            {@link IBufferPlacementStrategy} for creating the query.
	 */
	public ParameterBufferPlacementStrategy(IBufferPlacementStrategy object) {
		super(object);
	}
	
	public ParameterBufferPlacementStrategy(){
		super(null);
	}
	
	public ParameterBufferPlacementStrategy(String name){
		super(null);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return super.toString()+" name "+name;
	}
	
}
