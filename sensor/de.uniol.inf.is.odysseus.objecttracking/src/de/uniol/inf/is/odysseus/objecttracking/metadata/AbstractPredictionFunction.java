package de.uniol.inf.is.odysseus.objecttracking.metadata;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public abstract class AbstractPredictionFunction<T extends MetaAttributeContainer<M>, M extends IMetaAttribute> implements IPredictionFunction<T, M>{
	
	public T predictAll(SDFAttributeList schema, T object, PointInTime t){
		T predictedData = this.predictData(schema, object, t);
		predictedData.setMetadata(this.predictMetadata(schema, object, t));
		
		return predictedData;
	}
	
	@Override
	public abstract AbstractPredictionFunction<T, M> clone();

}
