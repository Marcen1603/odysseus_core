/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value.ParallelIntraOperatorSettingValue;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * Setting for intra operator parallelization. This setting is used in
 * transformation rules to create threaded version of physical operators if
 * exists. 
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ParallelIntraOperatorSetting extends
		Setting<ParallelIntraOperatorSettingValue> implements
		IQueryBuildSetting<ParallelIntraOperatorSettingValue> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7508795431000143064L;

	public ParallelIntraOperatorSetting(ParallelIntraOperatorSettingValue value) {
		super(value);
	}

	public ParallelIntraOperatorSetting() {
		super(new ParallelIntraOperatorSettingValue());
	}
}
