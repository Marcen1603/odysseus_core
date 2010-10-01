package de.uniol.inf.is.odysseus.transformation.greedy.transformators;

import java.util.Set;

import de.uniol.inf.is.odysseus.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TIMergeFunction;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.ITemporalSweepArea;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.TruePredicate;

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

		joinPO.setTransferFunction(new TITransferArea());
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
			ITemporalSweepArea[] areas = new ITemporalSweepArea[2];
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

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator("JoinTIPO");
		return to;
	}
}
