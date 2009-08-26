package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AbstractTypeSafeMap;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter.ParameterParserID;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter.ParameterPriority;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter.ParameterTransformationConfiguration;

public class QueryBuildParameter extends
		AbstractTypeSafeMap<AbstractQueryBuildParameter<?>> {

	public QueryBuildParameter(AbstractQueryBuildParameter<?>[] parameters) {
		super(parameters);
			
		if (!contains(ParameterTransformationConfiguration.class)) {
			set(new ParameterTransformationConfiguration(null));
		}
		
		if (!contains(ParameterDefaultRoot.class)) {
			set(new ParameterDefaultRoot(null));
		}

		if (!contains(ParameterParserID.class)) {
			set(new ParameterParserID(""));
		}

		if (!contains(ParameterBufferPlacementStrategy.class)) {
			set(new ParameterBufferPlacementStrategy(null));
		}

		if (!contains(ParameterPriority.class)) {
			set(new ParameterPriority(0));
		}
	}

	public QueryBuildParameter() {
	}

	public IPhysicalOperator getDefaultRoot() {
		return (IPhysicalOperator) get(ParameterDefaultRoot.class).getValue();
	}

	public Integer getPriority() {
		return (Integer) get(ParameterPriority.class).getValue();
	}

	public TransformationConfiguration getTransformationConfiguration() {
		return (TransformationConfiguration) get(
				ParameterTransformationConfiguration.class).getValue();
	}

	public String getParserID() {
		return (String) get(ParameterParserID.class).getValue();
	}

	public IBufferPlacementStrategy getBufferPlacementStrategy() {
		return (IBufferPlacementStrategy) get(ParameterBufferPlacementStrategy.class).getValue();
	}
}
