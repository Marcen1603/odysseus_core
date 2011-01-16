package de.uniol.inf.is.odysseus.datamining.classification;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
/**
 * This interface defines a classification object used to wrap tuples for classification operators
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public interface IClassificationObject<U extends IMetaAttribute> {

	/**get the attribute values of the tuple
	 * @return the attribute values
	 */
	public Object[] getAttributes();
	
	/** get the number of attributes used for the classification
	 * @return the number of attributes
	 */
	public int getClassificationAttributeCount();
	
	/**get the values of the tuples classification attributes
	 * @return the attribute values
	 */
	public Object[] getClassificationAttributes();
	
	/**get the class label
	 * @return the class the tuple belongs to
	 */
	public Object getClassLabel();
	
	/**set the tuples class lable
	 * @param classLabel the class to set
	 */
	public void setClassLabel(Object classLabel);
	
	}
