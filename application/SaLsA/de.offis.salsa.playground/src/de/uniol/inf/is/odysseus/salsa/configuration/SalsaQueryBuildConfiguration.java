package de.uniol.inf.is.odysseus.salsa.configuration;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
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
