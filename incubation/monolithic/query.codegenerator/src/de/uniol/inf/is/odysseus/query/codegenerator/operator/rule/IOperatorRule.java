package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;

public interface IOperatorRule {

	public String getName();

	public int getPriority();

	public boolean isExecutable(ILogicalOperator operator,
			TransformationConfiguration transformationConfiguration);

	public String getTargetPlatform();

	public Class<?> getConditionClass();

	public CodeFragmentInfo getCode(ILogicalOperator operator);

	public void analyseOperator(ILogicalOperator logicalOperator,
			QueryAnalyseInformation transformationInformation);

	public void addDataHandlerFromSDFSchema(ILogicalOperator logicalOperator,
			QueryAnalyseInformation transformationInformation);

	public void addOperatorConfiguration(ILogicalOperator logicalOperator,
			QueryAnalyseInformation transformationInformation);

}
