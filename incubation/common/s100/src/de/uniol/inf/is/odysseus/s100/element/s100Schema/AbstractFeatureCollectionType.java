package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * A feature collection contains zero or more features.
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:05
 */
public abstract class AbstractFeatureCollectionType extends AbstractFeatureType {

	public featureMember m_featureMember;

	public AbstractFeatureCollectionType(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}