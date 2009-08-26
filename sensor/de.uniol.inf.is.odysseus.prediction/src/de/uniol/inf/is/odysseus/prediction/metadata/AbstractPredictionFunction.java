package de.uniol.inf.is.odysseus.prediction.metadata;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AbstractPredictionFunction<T extends IMetaAttribute<M>, M extends IClone> implements IPredictionFunction<T, M>{
	
	public T predictAll(SDFAttributeList schema, T object, PointInTime t){
		T predictedData = this.predictData(schema, object, t);
		predictedData.setMetadata(this.predictMetadata(schema, object, t));
		
		return predictedData;
	}
	
	public abstract IClone clone();

}
