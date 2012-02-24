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
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * Parameter der bestimmt, ob der bereits laufende Plan durch Query-Sharing umstrukturiert werden soll,
 * oder nur neu hinzugekommene Queries ihre Operatoren getauscht bekommen dürfen.
 */
public class ParameterAllowRestructuringOfCurrentPlan extends Setting<Boolean> implements IOptimizationSetting<Boolean>, IQueryBuildSetting<Boolean> {
	/**
	 * Auch der alte Plan darf umstrukturiert werden.
	 */
	public static final ParameterAllowRestructuringOfCurrentPlan TRUE = new ParameterAllowRestructuringOfCurrentPlan(true);
	
	/**
	 * Nur neue Queries werden durch Query-Sharing optimiert.
	 */
	public static final ParameterAllowRestructuringOfCurrentPlan FALSE = new ParameterAllowRestructuringOfCurrentPlan(false);

	/**
	 * Initialisiert ein neues ParamterAllowRestructuringOfCurrentPlan-Objekt
	 * 
	 * @param value
	 *            der Wert dieses Parameters
	 */
	public ParameterAllowRestructuringOfCurrentPlan(Boolean value) {
		super(value);
	}
}
