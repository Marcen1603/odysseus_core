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
 * Parameter to control predicate optimization
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ParameterPredicateOptimizer extends Setting<Boolean> implements IOptimizationSetting<Boolean>, IQueryBuildSetting<Boolean> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5192907613431740776L;

	/**
     * Optimize predicates
     */
    public static final ParameterPredicateOptimizer TRUE = new ParameterPredicateOptimizer(true);

    /**
     * Do not optimize predicates.
     */
    public static final ParameterPredicateOptimizer FALSE = new ParameterPredicateOptimizer(false);

    /**
     * 
     * Class constructor.
     *
     * @param value
     */
    public ParameterPredicateOptimizer(Boolean value) {
        super(value);
    }
}
