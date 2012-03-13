/** Copyright [2011] [The Odysseus Team]
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

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;

/**
 * {@link IQueryBuildSetting} which provides a physical root for the
 * physical plan of a query.
 * 
 * @author Wolf Bauer
 * 
 */
public final class ParameterDefaultRoot extends Setting<IPhysicalOperator> implements
		IQueryBuildSetting<IPhysicalOperator> {

	private int port;

	/**
	 * Creates a ParameterDefaultRoot.
	 * 
	 * @param value
	 *            physical root for the physical plan of a query.
	 */
	public ParameterDefaultRoot(IPhysicalOperator value) {
		super(value);
		this.port = 0;
	}

	public ParameterDefaultRoot(IPhysicalOperator value, int sinkInPort) {
		super(value);
		this.port = sinkInPort;
	}
	
	@Override
	public IPhysicalOperator getValue() {
		return super.getValue();
	}
	
	public int getPort() {
		return port;
	}
}
