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
package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration.setting;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration.IExecutionSetting;

/**
 * Setting which indicates if the data latency should be calculated.
 * 
 * @author Wolf Bauer
 * 
 */
public class SettingCalculateLatency extends Setting<Boolean> implements IExecutionSetting<Boolean> {

	/**
	 * Calculate the data latency.
	 */
	public static final SettingCalculateLatency TRUE = new SettingCalculateLatency(
			true);

	/**
	 * Do not calculate the data latency.
	 */
	public static final SettingCalculateLatency FALSE = new SettingCalculateLatency(
			false);

	/**
	 * Creates a new SettingCalculateLatency setting. This is private because
	 * only TRUE and FALSE should be used.
	 * 
	 * @param value
	 *            new value of this setting
	 */
	private SettingCalculateLatency(Boolean value) {
		super(value);
	}
}
