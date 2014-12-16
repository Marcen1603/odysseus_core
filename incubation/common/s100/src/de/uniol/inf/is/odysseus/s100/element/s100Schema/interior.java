package de.uniol.inf.is.odysseus.s100.element.s100Schema;

/**
 * A boundary of a surface consists of a number of rings. In the normal 2D case,
 * one of these rings is distinguished as being the exterior boundary. In a
 * general manifold this is not always possible, in which case all boundaries
 * shall be listed as interior boundaries, and the exterior will be empty.
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:08
 */
public class interior extends AbstractRingPropertyType {

	public interior(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}