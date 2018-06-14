package de.uniol.inf.is.odysseus.relational.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.GroupSplitFileWriterAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.GroupSplitFileWriter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TGroupSplitFileWriter extends
		AbstractTransformationRule<GroupSplitFileWriterAO> {

	@Override
	public void execute(GroupSplitFileWriterAO operator,
			TransformationConfiguration config) throws RuleException {
		IGroupProcessor<Tuple<IMetaAttribute>, Tuple<IMetaAttribute>> gp = new RelationalGroupProcessor<>(operator.getInputSchema(), operator.getInputSchema(), operator.getGroupAttributes(), null, false);
		@SuppressWarnings("rawtypes")
		IStreamObjectDataHandler dataHandler = getDataDictionary().getDataHandlerRegistry(getCaller()).getStreamObjectDataHandler(
				operator.getDataHandler(), operator.getOutputSchema());
		@SuppressWarnings("unchecked")
		GroupSplitFileWriter<Tuple<IMetaAttribute>> physical = new GroupSplitFileWriter<>(operator.getPath(), gp, dataHandler);
		defaultExecute(operator, physical , config, true, true);
	}

	@Override
	public boolean isExecutable(GroupSplitFileWriterAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet()
				&& operator.getInputSchema().getType()
						.isAssignableFrom(Tuple.class);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super GroupSplitFileWriterAO> getConditionClass() {
		return GroupSplitFileWriterAO.class;
	}

}
