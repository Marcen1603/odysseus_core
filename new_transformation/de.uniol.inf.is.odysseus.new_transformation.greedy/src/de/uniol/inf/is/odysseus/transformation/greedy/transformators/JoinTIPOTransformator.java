package de.uniol.inf.is.odysseus.transformation.greedy.transformators;

import java.util.Set;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TIMergeFunction;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferFunction;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.metadata.base.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;

public class JoinTIPOTransformator implements IPOTransformator<JoinAO> {
	@Override
	public boolean canExecute(JoinAO logicalOperator, TransformationConfiguration config) {
		Set<String> metaTypes = config.getMetaTypes();
		return metaTypes.contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval");
	}

	@SuppressWarnings("unchecked")
	@Override
	public TransformedPO transform(JoinAO joinAO, TransformationConfiguration config, ITransformation transformation) {
		JoinTIPO joinPO = new JoinTIPO();
		joinPO.setOutputSchema(joinAO.getOutputSchema());
		IPredicate pred = joinAO.getPredicate();
		joinPO.setJoinPredicate(pred == null ? new TruePredicate() : pred);

		joinPO.setTransferFunction(new TITransferFunction());
		joinPO.setMetadataMerge(new CombinedMergeFunction());
		joinPO.setOutputSchema(joinAO.getOutputSchema());
		joinPO.setCreationFunction(new DefaultTIDummyDataCreation());

		Set<String> metaTypes = config.getMetaTypes();
		
		if (metaTypes.equals(TransformationConfiguration
				.toSet("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"))
				&& joinPO.getMetadataMerge() != null && joinPO.getMetadataMerge() instanceof CombinedMergeFunction) {
			joinPO.setMetadataMerge(TIMergeFunction.getInstance());
		}
		
		if (metaTypes.contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval") && metaTypes.size() > 1
				&& joinPO.getMetadataMerge() != null && joinPO.getMetadataMerge() instanceof CombinedMergeFunction) {
			((CombinedMergeFunction) joinPO.getMetadataMerge()).add(new TimeIntervalInlineMetadataMergeFunction());
		}
		
		if (metaTypes.contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval")) {
			ISweepArea[] areas = new ISweepArea[2];
			areas[0] = new JoinTISweepArea();
			areas[1] = new JoinTISweepArea();

			joinPO.setAreas(areas);
		}
		
		return new TransformedPO(joinPO);
	}
	
	@Override
	public int getPriority() {
		return 0;
	}
}
