package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.codegenerator.model.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.model.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * Interface for the codegeneration operatorrule engine
 * 
 * @author MarcPreuschaft
 *
 * @param <T>
 */
public interface ICOperatorRule<T extends ILogicalOperator> {

	/**
	 * return the operator rule name
	 * @return
	 */
	public String getName();

	/**
	 * return the priority of the rule
	 * @return
	 */
	public int getPriority();

	/**
	 * checks if the rule can executable on the given operator
	 * @param operator
	 * @param transformationConfiguration
	 * @return
	 */
	public boolean isExecutable(T operator,
			TransformationConfiguration transformationConfiguration);

	/**
	 * return the targetplatform name 
	 * @return
	 */
	public String getTargetPlatform();

	/**
	 * return the condition class for this rule
	 * @return
	 */
	public Class<T> getConditionClass();

	/**
	 * return the code for the operator
	 * @param operator
	 * @return
	 */
	public CodeFragmentInfo getCode(T operator);

	/**
	 * analyse function to detect transprothandler, protocolhandler, mep-functions
	 * 
	 * @param logicalOperator
	 * @param transformationInformation
	 */
	public void analyseOperator(T logicalOperator,
			QueryAnalyseInformation transformationInformation);

	/**
	 * detect all dataHandler from the operator SDFSchema
	 * @param logicalOperator
	 * @param transformationInformation
	 */
	public void addDataHandlerFromSDFSchema(T logicalOperator,
			QueryAnalyseInformation transformationInformation);

	/**
	 * 
	 * @param logicalOperator
	 * @param transformationInformation
	 */
	public void addOperatorConfiguration(T logicalOperator,
			QueryAnalyseInformation transformationInformation);

}
