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

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * With this parameter the recursion connection handling can be change. Default is false.
 */
public class ParameterRecursionConnectSink extends Setting<Boolean> implements IOptimizationSetting<Boolean>, IQueryBuildSetting<Boolean> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7829864548893164461L;

	/**
	 * Es soll Query-Sharing genutzt werden.
	 */
	public static final ParameterRecursionConnectSink TRUE = new ParameterRecursionConnectSink(true);
	
	/**
	 * Es soll kein Query-Sharing genutzt werden.
	 */
	public static final ParameterRecursionConnectSink FALSE = new ParameterRecursionConnectSink(false);

	/**
	 * Initialisiert ein neues ParamterPerformQuerySharing-Objekt
	 * 
	 * @param value
	 *            der Wert dieses Parameters
	 */
	public ParameterRecursionConnectSink(Boolean value) {
		super(value);
	}
}
