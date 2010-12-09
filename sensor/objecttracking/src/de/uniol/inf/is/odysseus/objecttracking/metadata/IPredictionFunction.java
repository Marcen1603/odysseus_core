package de.uniol.inf.is.odysseus.objecttracking.metadata;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * This interface represents a prediction function that is used
 * for predicting attribute values of elements in a data stream.
 * Usually a tuple in a data stream can carry multiple prediction
 * functions as metadata.
 * 
 * @author Andre Bolles
 *
 */
public interface IPredictionFunction<T extends MetaAttributeContainer<M>, M extends IMetaAttribute> extends IMetaAttribute, IClone{

	public T predictData(SDFAttributeList schema, T object, PointInTime t);
	
	public M predictMetadata(SDFAttributeList schema, T object, PointInTime t);
	
	public T predictAll(SDFAttributeList schema, T object, PointInTime t);
	
	public void setExpressions(SDFExpression[] expressions);
	
	public void setVariables(int[][] variables);
	
	public void setTimeInterval(ITimeInterval timeInterval);
	
	public SDFExpression[] getExpressions();
	
	public int[][] getVariables();
	
	public void initVariables();
	
	@Override
	public IPredictionFunction<T, M> clone();
}
