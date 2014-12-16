package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:06
 */
public abstract class AbstractS100FeatureType extends AbstractFeatureType {

	public featureAssociation m_featureAssociation;

	public AbstractS100FeatureType(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}