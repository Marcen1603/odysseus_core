package de.uniol.inf.is.odysseus.spatial.exception;

/**
 * This exception will be thrown when a testing or modifying method
 * (like intersection) will be called on a Facet3D object, that
 * has not been correctly initialized.
 *  
 * @author Andre Bolles
 *
 */
public class InvalidFacetException extends SpatialException{

	public InvalidFacetException(){
		super();
	}
	
	public InvalidFacetException(String msg){
		super(msg);
	}
}
