package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OperatorTransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractCJoinTIPORule;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class CJoinTIPORule extends  AbstractCJoinTIPORule{

	public CJoinTIPORule() {
		super(CJoinTIPORule.class.getName(), "java");
	}

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		
		CodeFragmentInfo joinTIPO = new CodeFragmentInfo();
		
		String operatorVariable = OperatorTransformationInformation.getInstance().getVariable(operator);
		
		JoinAO joinAO = (JoinAO) operator;
		
		List<String> input0MetaAttributeNames = joinAO.getInputSchema(0).getMetaAttributeNames();
		List<String> input1MetaAttributeNames = joinAO.getInputSchema(1).getMetaAttributeNames();
		
		
		StringTemplate stringList0Template = new StringTemplate("utils","stringList");
		stringList0Template.getSt().add("listVariable", operatorVariable+"JoinInput0MetaAttributes");
		stringList0Template.getSt().add("list", input0MetaAttributeNames);
	
		
		StringTemplate stringList1Template = new StringTemplate("utils","stringList");
		stringList1Template.getSt().add("listVariable", operatorVariable+"JoinInput1MetaAttributes");
		stringList1Template.getSt().add("list", input1MetaAttributeNames);
		
		
		StringTemplate joinTIPOTemplate = new StringTemplate("operator","joinTIPO");
		joinTIPOTemplate.getSt().add("operatorVariable", operatorVariable);
		joinTIPOTemplate.getSt().add("input0MetaAttributeNamesCode", stringList0Template.getSt().render());
		joinTIPOTemplate.getSt().add("input1MetaAttributeNamesCode", stringList1Template.getSt().render());
		joinTIPOTemplate.getSt().add("input0MetaAttributeNamesVariable", operatorVariable+"JoinInput0MetaAttributes");
		joinTIPOTemplate.getSt().add("input1MetaAttributeNamesVariable", operatorVariable+"JoinInput1MetaAttributes");
		
		
		
		
		joinTIPO.addCode(joinTIPOTemplate.getSt().render());
		
		joinTIPO.addImport(IMetadataMergeFunction.class.getName());
		joinTIPO.addImport(JoinTIPO.class.getName());
		joinTIPO.addImport(ITimeIntervalSweepArea.class.getName());
		
		
		return joinTIPO;
	}


}
