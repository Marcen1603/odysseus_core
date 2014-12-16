package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * An abstract feature provides a set of common properties, including id,
 * metaDataProperty, name and description inherited from AbstractS100Type, plus
 * box.    A concrete feature type must derive from this type and specify
 * additional  properties in an application schema. A feature must possess an
 * identifying attribute ('id').
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:05
 */
public abstract class AbstractFeatureType extends AbstractS100Type {

	public AbstractFeatureType(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}