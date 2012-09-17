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
package de.uniol.inf.is.odysseus.salsa.configuration;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AbstractQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterPerformQuerySharing;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterShareSimilarOperators;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SalsaQueryBuildConfiguration extends AbstractQueryBuildConfiguration {
    
    public SalsaQueryBuildConfiguration() {
        @SuppressWarnings("unchecked")
        TransformationConfiguration trafoconfig = new TransformationConfiguration("relational",
                ITimeInterval.class);
        trafoconfig.setOption("NO_METADATA", true);
        settings.add(new ParameterTransformationConfiguration(trafoconfig));
        settings.add(ParameterDoRewrite.TRUE);
        settings.add(ParameterPerformQuerySharing.TRUE);
        settings.add(ParameterShareSimilarOperators.FALSE);
    }


    public SalsaQueryBuildConfiguration(List<IQueryBuildSetting<?>> settings) {
        settings.addAll(settings);
    }


    @Override
    public String getName() {
        return "salsa";
    }
    
    @Override
    public IQueryBuildConfiguration clone() {
        return new SalsaQueryBuildConfiguration(settings);
    }
}
