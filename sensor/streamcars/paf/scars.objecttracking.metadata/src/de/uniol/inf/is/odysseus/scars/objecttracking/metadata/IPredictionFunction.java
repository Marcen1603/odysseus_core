package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface IPredictionFunction<M extends IProbability> {
	
	public void init(SDFAttributeList scanSchema, SDFAttributeList timeSchema);
	
	public void predictData(MVRelationalTuple<M> scanRootTuple, MVRelationalTuple<M> timeTuple, int index);

	public void predictMetadata(M metadata, MVRelationalTuple<M> scanRootTuple, MVRelationalTuple<M> timeTuple, int index);

	public void setExpressions(PredictionExpression[] expressions);
	
	public void setNoiseMatrix(double[][] noiseMatrix);
	
	public PredictionExpression[] getExpressions();
}
