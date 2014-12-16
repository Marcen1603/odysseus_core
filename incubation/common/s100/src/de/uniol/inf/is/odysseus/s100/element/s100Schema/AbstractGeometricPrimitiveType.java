package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * This is the abstract root type of the geometric primitives. A geometric
 * primitive is a geometric object that is not decomposed further into other
 * primitives in the system. All primitives are oriented in the direction implied
 * by the sequence of their coordinate tuples.
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:05
 */
public abstract class AbstractGeometricPrimitiveType extends AbstractGeometryType {

	public AbstractGeometricPrimitiveType(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}