package de.uniol.inf.is.odysseus.logicaloperator.builder;

public class MissingInputsException extends Exception {

	private int minPortCount;

	public int getMinPortCount() {
		return this.minPortCount;
	}
	
	public MissingInputsException(int minPortCount) {
		super("need at least " + minPortCount + " input operators");
		this.minPortCount = minPortCount;
	}

	private static final long serialVersionUID = 6522687973588913632L;

}
