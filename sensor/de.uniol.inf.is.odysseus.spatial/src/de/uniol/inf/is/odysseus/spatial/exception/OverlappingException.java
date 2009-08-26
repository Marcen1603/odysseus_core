package de.uniol.inf.is.odysseus.spatial.exception;

/**
 * This exception will be thrown, if
 * something is wrong at testing overlapping
 * of 3D items.
 * @author Andre Bolles
 *
 */
public class OverlappingException extends SpatialException{

	public OverlappingException(){
		super();
	}
	
	public OverlappingException(String msg){
		super(msg);
	}
}
