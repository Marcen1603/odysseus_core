package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public interface ICOperatorRule<T extends ILogicalOperator> {

	public String getName();

	public int getPriority();

	public boolean isExecutable(T operator,
			TransformationConfiguration transformationConfiguration);

	public String getTargetPlatform();

	public Class<T> getConditionClass();

	public CodeFragmentInfo getCode(T operator);

	public void analyseOperator(T logicalOperator,
			QueryAnalyseInformation transformationInformation);

	public void addDataHandlerFromSDFSchema(T logicalOperator,
			QueryAnalyseInformation transformationInformation);

	public void addOperatorConfiguration(T logicalOperator,
			QueryAnalyseInformation transformationInformation);

}
