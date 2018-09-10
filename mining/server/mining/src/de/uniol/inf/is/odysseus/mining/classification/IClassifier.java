package de.uniol.inf.is.odysseus.mining.classification;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IClassifier<M extends ITimeInterval>{
	
	
	
	/**
	 * Can be used to initialize the classifier
	 * @param schema The schema of the tuple that should be classified
	 * @param classAttribute the attribute of the class (additionally to the schema)
	 */
	public void init(SDFSchema schema, SDFAttribute classAttribute);
	
	/**
	 * Classifies the given tuple
	 * @param tuple the tuple to be classified
	 * @return the class
	 */
	public Object classify(Tuple<M> tuple);

}
