package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCJoinTIPORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
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
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.server.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * This rule generate from a JoinAO the code for the 
 * JoinTIPO operator. 
 * 
 * template: operator/joinTIPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CJoinTIPORule extends  AbstractCJoinTIPORule<JoinAO>{

	public CJoinTIPORule() {
		super(CJoinTIPORule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(JoinAO operator) {
		
		CodeFragmentInfo joinTIPO = new CodeFragmentInfo();
		
		//get unique operator variable
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator);
		
		JoinAO joinAO = (JoinAO) operator;
		
		//set default areaName to TIJoinSA
		String areaName = "TIJoinSA";	
		
		if(joinAO.getSweepAreaName() != null){
			areaName = joinAO.getSweepAreaName();
		}
		
		//get join predicate
		IPredicate<?> predicate = joinAO.getPredicate();
		
		String predicateValue = "";
		
		if(predicate != null){
			predicateValue =predicate.toString();
		}
		
		String transferFuntion = "";
		
		//detect the transferArea
		if (joinAO.isAssureOrder()) {
			transferFuntion = "TITransferArea";
		} else {
			transferFuntion = "DirectTransferArea";
		}

		//get metaAttribute names from the inputSchema 0 and 1
		List<String> input0MetaAttributeNames = joinAO.getInputSchema(0).getMetaAttributeNames();
		List<String> input1MetaAttributeNames = joinAO.getInputSchema(1).getMetaAttributeNames();
		
		
		Collection<LogicalSubscription> sources =  joinAO.getSubscribedToSource();
		LogicalSubscription[] logicalSubscritions = new LogicalSubscription[2];
		sources.toArray(logicalSubscritions);
		
		//generate code for the input0MetaAttributeNames
		StringTemplate stringList0Template = new StringTemplate("utils","stringList");
		stringList0Template.getSt().add("listVariable", operatorVariable+"JoinInput0MetaAttributes");
		stringList0Template.getSt().add("list", input0MetaAttributeNames);
	
		//generate code for the input1MetaAttributeNames
		StringTemplate stringList1Template = new StringTemplate("utils","stringList");
		stringList1Template.getSt().add("listVariable", operatorVariable+"JoinInput1MetaAttributes");
		stringList1Template.getSt().add("list", input1MetaAttributeNames);
		
		//generate code for joinTIPO
		StringTemplate joinTIPOTemplate = new StringTemplate("operator","joinTIPO");
		joinTIPOTemplate.getSt().add("operatorVariable", operatorVariable);
		joinTIPOTemplate.getSt().add("input0MetaAttributeNamesCode", stringList0Template.getSt().render());
		joinTIPOTemplate.getSt().add("input1MetaAttributeNamesCode", stringList1Template.getSt().render());
		joinTIPOTemplate.getSt().add("input0MetaAttributeNamesVariable", operatorVariable+"JoinInput0MetaAttributes");
		joinTIPOTemplate.getSt().add("input1MetaAttributeNamesVariable", operatorVariable+"JoinInput1MetaAttributes");
		joinTIPOTemplate.getSt().add("areaName", areaName);
		joinTIPOTemplate.getSt().add("predicateValue", predicateValue);
		joinTIPOTemplate.getSt().add("transferFuntion", transferFuntion);
		joinTIPOTemplate.getSt().add("input0",  DefaultCodegeneratorStatus.getInstance().getVariable(logicalSubscritions[0].getTarget()));
		joinTIPOTemplate.getSt().add("input1",  DefaultCodegeneratorStatus.getInstance().getVariable(logicalSubscritions[1].getTarget()));
		
		//generate code for the left and right schema
		joinTIPO.addCodeFragmentInfo((CreateJreDefaultCode.getCodeForSDFSchema(logicalSubscritions[0].getSchema(), operatorVariable+"leftSchema")));
		joinTIPO.addCodeFragmentInfo((CreateJreDefaultCode.getCodeForSDFSchema(logicalSubscritions[1].getSchema(), operatorVariable+"rightSchema")));
		
		//render the joinTIPO template
		joinTIPO.addCode(joinTIPOTemplate.getSt().render());
		
		//add framework imports
		joinTIPO.addFrameworkImport(IMetadataMergeFunction.class.getName());
		joinTIPO.addFrameworkImport(JoinTIPO.class.getName());
		joinTIPO.addFrameworkImport(ITimeIntervalSweepArea.class.getName());
		joinTIPO.addFrameworkImport(DefaultTISweepArea.class.getName());
		joinTIPO.addFrameworkImport(RelationalMergeFunction.class.getName());
		joinTIPO.addFrameworkImport(ITimeInterval.class.getName());
		joinTIPO.addFrameworkImport(DirectAttributeResolver.class.getName());
		joinTIPO.addFrameworkImport(RelationalPredicateBuilder.class.getName());
		joinTIPO.addFrameworkImport(RelationalPredicate.class.getName());
		joinTIPO.addFrameworkImport(TITransferArea.class.getName());
		joinTIPO.addFrameworkImport(DirectTransferArea.class.getName());
		joinTIPO.addFrameworkImport(DefaultTIDummyDataCreation.class.getName());
		
	
		return joinTIPO;
	}


}
