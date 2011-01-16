package de.uniol.inf.is.odysseus.datamining.classification;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This interface defines a classifier to clasify tuples whith the classify
 * operator
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public interface IClassifier<T extends IMetaAttribute> {

	/**
	 * get the predicted class for a given tuple
	 * 
	 * @param tuple
	 *            the tuple to predict the class for
	 * @return the predicted class
	 */
	public Object getClassLabel(RelationalClassificationObject<T> tuple);
}
