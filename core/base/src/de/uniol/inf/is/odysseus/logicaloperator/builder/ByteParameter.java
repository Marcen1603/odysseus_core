package de.uniol.inf.is.odysseus.logicaloperator.builder;

public class ByteParameter extends AbstractParameter<Byte> {

	private static final long serialVersionUID = 1482434794682621670L;

	@Override
	protected void internalAssignment() {
		setValue(((Long) this.inputValue).byteValue());
	}

}
