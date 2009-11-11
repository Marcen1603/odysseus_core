package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

/**
 * @author Jonas Jacobi
 */
public class SDFExpressionParseException extends RuntimeException {

	private static final long serialVersionUID = -5235744899812769717L;
	
	public SDFExpressionParseException() {
	}
	
	public SDFExpressionParseException(Throwable e) {
		super(e);
	}
	
	public SDFExpressionParseException(String message, Throwable t){
		super(message, t);
	}

	public SDFExpressionParseException(String string) {
		super(string);
	}

}
