package de.uniol.inf.is.odysseus.interval.transform.join;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.metadata.base.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TJoinAORule extends AbstractTransformationRule<JoinAO> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void transform(JoinAO joinAO, TransformationConfiguration transformConfig) {
		JoinTIPO joinPO = new JoinTIPO();
		IPredicate pred = joinAO.getPredicate();
		joinPO.setJoinPredicate(pred == null ? new TruePredicate() : pred.clone());
		joinPO.setTransferFunction(new TITransferArea());
		joinPO.setMetadataMerge(new CombinedMergeFunction());
		joinPO.setOutputSchema(joinAO.getOutputSchema() == null?null:joinAO.getOutputSchema().clone());
		joinPO.setCreationFunction(new DefaultTIDummyDataCreation());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(joinAO, joinPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(joinPO);
		retract(joinAO);		
	}

	@Override
	public boolean isExecutable(JoinAO operator, TransformationConfiguration transformConfig) {
		if(operator.isAllPhysicalInputSet()){
			if(transformConfig.getMetaTypes().contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "JoinAO -> JoinTIPO";
	}

}
