package de.uniol.inf.is.odysseus.objecttracking.metadata;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;


public interface IProbability extends IMetaAttribute, IClone{

	/**
	 * Sets the covarians matrix used in the objecttracking operators
	 * 
	 *    s1 s2 s3 s4
	 * s1 00 01 02 03
	 * s2 10 11 12 13
	 * s3 20 21 22 23
	 * s4 30 31 32 33
	 * @param sigma
	 */
	public void setCovariance(double[][] sigma);
	
	/**
	 * 
	 * @return
	 */
	public double[][] getCovariance();
	

	/**
	 * Sets the schema attribute indices corresponding to the covarians matrix, reflecting the MVAttributes of the schema
	 * 		    	   s1 s2 s3 s4
	 * idxAttr1[0]	s1 00 01 02 03
	 * idxAttr2[1]	s2 10 11 12 13
	 * idxAttr3[2]	s3 20 21 22 23
	 * idxAttr4[3]	s4 30 31 32 33
	 * 
	 * So there is a mapping between the indices of the attribute values inside the tuple and their probability values inside the matrix
	 * 
	 * The covariance matrix only references to measurement attributes.
	 * However, the first measurement attribute has not necessarily to be the
	 * first attribute in the schema and so on. So, we have to find the measurement
	 * attribute positions.
	 * 
	 * This array contains the positions of the measurement values.
	 * restrictList[0] contains the position of the first measurement attribute in the
	 * schema, restrictList[1] contains the position of the second measurement attribute
	 * in the schema and so on.
	 * 
	 * @param indices
	 */
	public void setMVAttributeIndices(int[] indices);
	
	/**
	 * 
	 * @return
	 */
	public int[] getMVAttributeIndices();

	public ArrayList<int[]> getAttributePaths();
	
	public void setAttributePaths(ArrayList<int[]> paths);
	
	public int getIndexOfKovMatrix(int[] path);
}
