package de.uniol.inf.is.odysseus.objecttracking.metadata;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;

/**
 * This interface will be implemented by
 * probability prediction functions, since
 * they have to use a noise matrix for calculating
 * the new covariance matrix.
 * 
 * @author Andre Bolles
 *
 */
public interface IProbabilityPredictionFunction <T extends MetaAttributeContainer<M>, M extends IMetaAttribute> extends IPredictionFunction<T, M>{

	public void setNoiseMatrix(double[][] noiseMatrix);
	
	/**
	 * The restrict list contains the positions of the measurement
	 * attributes in a schema. Since the first measurement attribute
	 * has not necessarily to be the first attribute in the schema,
	 * these positions must be stored explicitly.
	 * 
	 * @param restrictList contains the positions of the measurement attributes in a schema
	 */
	public void setRestrictList(int[] restrictList);
}
