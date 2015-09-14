package de.uniol.inf.is.odysseus.query.codegenerator.queryBuildSetting;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;

public class TestSetting extends Setting<TransformationParameter> implements IQueryBuildSetting<TransformationParameter> {



	public TestSetting(TransformationParameter transformationParameter) {
		super(transformationParameter);
	}

}
