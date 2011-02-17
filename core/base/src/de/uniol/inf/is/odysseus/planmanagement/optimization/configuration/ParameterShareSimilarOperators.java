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
package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * Parameter der bestimmt, ob beim Query-Sharing nur identische Operatoren ersetzt werden,
 * oder auch die Zwischenergebnisse anderer ÄHNLICHER aber nicht identischer Operatoren bei der Optimierung
 * berücksichtigt werden sollen.
 */
public class ParameterShareSimilarOperators extends Setting<Boolean> implements IOptimizationSetting<Boolean>, IQueryBuildSetting<Boolean> {
	/**
	 * Auch teilweise gleiche Operatoren werden - wenn möglich - geteilt.
	 */
	public static final ParameterShareSimilarOperators TRUE = new ParameterShareSimilarOperators(true);
	
	/**
	 * Die Query-Sharing-Optimierung belässt es bei einem Austausch von identischen Operatoren
	 */
	public static final ParameterShareSimilarOperators FALSE = new ParameterShareSimilarOperators(false);

	/**
	 * Initialisiert ein neues ParamterAllowRestructuringOfCurrentPlan-Objekt
	 * 
	 * @param value
	 *            der Wert dieses Parameters
	 */
	public ParameterShareSimilarOperators(Boolean value) {
		super(value);
	}
}
