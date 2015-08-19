package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.parser.pql.relational.RelationalPredicateBuilder;
import de.uniol.inf.is.odysseus.persistentqueries.DirectTransferArea;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OperatorTransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractCJoinTIPORule;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.server.intervalapproach.DefaultTIDummyDataCreation;
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
		
		String areaName = "TIJoinSA";	
		
		if(joinAO.getSweepAreaName() != null){
			areaName = joinAO.getSweepAreaName();
		}
		
		
		IPredicate<?> predicate = joinAO.getPredicate();
		
		String predicateValue = predicate.toString();
		String transferFuntion = "";
		
		if (joinAO.isAssureOrder()) {
			transferFuntion = "TITransferArea";
		
		} else {
			transferFuntion = "DirectTransferArea";
		}


		List<String> input0MetaAttributeNames = joinAO.getInputSchema(0).getMetaAttributeNames();
		List<String> input1MetaAttributeNames = joinAO.getInputSchema(1).getMetaAttributeNames();
		
		
		Collection<LogicalSubscription> sources =  joinAO.getSubscribedToSource();
		LogicalSubscription[] logicalSubscritions = new LogicalSubscription[2];
		sources.toArray(logicalSubscritions);
		
		
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
		joinTIPOTemplate.getSt().add("areaName", areaName);
		joinTIPOTemplate.getSt().add("predicateValue", predicateValue);
		joinTIPOTemplate.getSt().add("transferFuntion", transferFuntion);
		joinTIPOTemplate.getSt().add("input0",  OperatorTransformationInformation.getInstance().getVariable(logicalSubscritions[0].getTarget()));
		joinTIPOTemplate.getSt().add("input1",  OperatorTransformationInformation.getInstance().getVariable(logicalSubscritions[1].getTarget()));
		
		
		
	
		joinTIPO.addCode(joinTIPOTemplate.getSt().render());
		
		joinTIPO.addImport(IMetadataMergeFunction.class.getName());
		joinTIPO.addImport(JoinTIPO.class.getName());
		joinTIPO.addImport(ITimeIntervalSweepArea.class.getName());
		joinTIPO.addImport(DefaultTISweepArea.class.getName());
		joinTIPO.addImport(RelationalMergeFunction.class.getName());
		joinTIPO.addImport(ITimeInterval.class.getName());
		
	
		
		
		
		
		
		joinTIPO.addImport(DirectAttributeResolver.class.getName());
		joinTIPO.addImport(RelationalPredicateBuilder.class.getName());
		joinTIPO.addImport(RelationalPredicate.class.getName());
		joinTIPO.addImport(TITransferArea.class.getName());
		joinTIPO.addImport(DirectTransferArea.class.getName());
		joinTIPO.addImport(DefaultTIDummyDataCreation.class.getName());
		
		
		

		
		
		return joinTIPO;
	}


}
