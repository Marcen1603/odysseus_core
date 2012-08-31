package de.uniol.inf.is.odysseus.fusion.metadata.merge;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.fusion.metadata.IFusionProbability;

public class FusionProbabilityMetadataMergeFunction implements IInlineMetadataMergeFunction<IFusionProbability> {
	
	
	public FusionProbabilityMetadataMergeFunction(
			FusionProbabilityMetadataMergeFunction timeIntervalInlineMetadataMergeFunction) {
	}
	
	public FusionProbabilityMetadataMergeFunction(){}

	@Override
	public void mergeInto(IFusionProbability result, IFusionProbability inLeft,IFusionProbability inRight) {
		result.setStart(PointInTime.max(inLeft.getStart(), inRight.getStart()));
		result.setEnd(PointInTime.min(inLeft.getEnd(), inRight.getEnd()));
		
		result.setError_cov_post(inLeft.getError_cov_post());
		result.setError_cov_pre(inLeft.getError_cov_pre());	
	}

	@Override
	public FusionProbabilityMetadataMergeFunction clone()  {
		return new FusionProbabilityMetadataMergeFunction(this);
	}
	
}
