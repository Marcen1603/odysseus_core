package de.uniol.inf.is.odysseus.prediction.metadata;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * This interface represents a prediction function that is used
 * for predicting attribute values of elements in a data stream.
 * Usually a tuple in a data stream can carry multiple prediction
 * functions as metadata.
 * 
 * @author Andre Bolles
 *
 */
public interface IPredictionFunction<T extends IMetaAttribute<M>, M extends IClone> extends IClone{

	public T predictData(SDFAttributeList schema, T object, PointInTime t);
	
	public M predictMetadata(SDFAttributeList schema, T object, PointInTime t);
	
	public T predictAll(SDFAttributeList schema, T object, PointInTime t);
}
