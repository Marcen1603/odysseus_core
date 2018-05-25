package de.uniol.inf.is.odysseus.parser.cql2.generator.builder;

public class PQLOperatorBuilderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -548011886279313159L;
	
	private String message;
	
	public PQLOperatorBuilderException(String message) {
		super();
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
	
}
