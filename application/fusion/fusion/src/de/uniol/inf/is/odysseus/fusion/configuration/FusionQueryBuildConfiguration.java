package de.uniol.inf.is.odysseus.fusion.configuration;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AbstractQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterAllowRestructuringOfCurrentPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterPerformQuerySharing;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterShareSimilarOperators;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.fusion.metadata.IFusionProbability;

/**
 * @author Kai Pancratz
 */
public class FusionQueryBuildConfiguration extends AbstractQueryBuildConfiguration {
    
    @SuppressWarnings("unchecked")
	public FusionQueryBuildConfiguration() {
		settings.add(new ParameterTransformationConfiguration(
				new TransformationConfiguration(
						"relational", 
						IFusionProbability.class,ITimeInterval.class)));
		settings.add(ParameterDoRewrite.TRUE);
		settings.add(ParameterPerformQuerySharing.TRUE);
		settings.add(ParameterAllowRestructuringOfCurrentPlan.TRUE);
		settings.add(ParameterShareSimilarOperators.FALSE);
    }


    public FusionQueryBuildConfiguration(List<IQueryBuildSetting<?>> settings) {
        settings.addAll(settings);
    }


    @Override
    public String getName() {
        return "Fusion";
    }
    
    @Override
    public IQueryBuildConfiguration clone() {
        return new FusionQueryBuildConfiguration(settings);
    }
}
