package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public interface IPredictionFunction<M extends IProbability> {
	
	public MVRelationalTuple<M> predictData(MVRelationalTuple<M> object, PointInTime currentTime, PointInTime timeToPredict);
	
	public M predictMetadata(M metadata, PointInTime currentTime, PointInTime timeToPredict);
	
	public void setExpressions(SDFExpression[] expressions);
	
	public void setNoiseMatrix(double[][] noiseMatrix);

}
