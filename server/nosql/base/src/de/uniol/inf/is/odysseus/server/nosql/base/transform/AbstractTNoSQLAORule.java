package de.uniol.inf.is.odysseus.server.nosql.base.transform;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.IPhysicalNoSQLOperator;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * AbstractTNoSQLAORule is protected, because AbstractTNoSQLSinkAORule or
 * AbstractTNoSQLSourceAORule should be used instead
 *
 * AbstractTNoSQLAORule helps creating transformation rules by outsourcing
 * recurring operations
 *
 * @param <L>
 *            the concrete logical operator class
 * @param <P>
 *            AbstractNoSQLSinkPO or AbstractNoSQLSourcePO (P don't have to be
 *            more specified)
 */
abstract class AbstractTNoSQLAORule<L extends AbstractNoSQLAO, P extends IPhysicalOperator>
		extends AbstractTransformationRule<L> {

	@SuppressWarnings("rawtypes")
	private Class logicalOperatorClass;
	@SuppressWarnings("rawtypes")
	private Class physicalOperatorClass;

	public AbstractTNoSQLAORule() {
		logicalOperatorClass = getLogicalOperatorClass();
		physicalOperatorClass = getPhysicalOperatorClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void execute(AbstractNoSQLAO logicalOperator,
			TransformationConfiguration config) throws RuleException {

		P physicalOperator;

		try {
			// creates a new instance of the specified physicalOperatorClass
			// with the logicalOperatorClass as parameter
			// noinspection unchecked
			physicalOperator = (P) physicalOperatorClass
					.getDeclaredConstructor(logicalOperatorClass).newInstance(
							logicalOperator);
		} catch (Exception e) {
			throw new RuleException(e);
		}

		defaultExecute(logicalOperator, physicalOperator, config, true, true);
	}

	@Override
	public boolean isExecutable(L operator, TransformationConfiguration config) {
		// All NoSQL operator currently require key value objects?
		return (operator.isSourceOperator() || ((operator.getInputSchema(0).getType() == KeyValueObject.class || operator
				.getInputSchema(0).getType() == NestedKeyValueObject.class)
				&& operator.isAllPhysicalInputSet()));
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public String getName() {
		return logicalOperatorClass.getSimpleName() + " -> "
				+ physicalOperatorClass.getSimpleName();
	}

	/**
	 * @return the class of the logical operator
	 */
	protected abstract Class<? extends AbstractNoSQLAO> getLogicalOperatorClass();

	/**
	 * @return the class of the physical operator
	 */
	protected abstract Class<? extends IPhysicalNoSQLOperator> getPhysicalOperatorClass();

	@SuppressWarnings("unchecked")
	@Override
	public Class<? super L> getConditionClass() {
		return logicalOperatorClass;
	}
}
