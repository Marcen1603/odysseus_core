package de.uniol.inf.is.odysseus.logicaloperator.builder;

@SuppressWarnings("rawtypes")
public class EnumParameter extends AbstractParameter<Enum> {

	private static final long serialVersionUID = 2756499659470510812L;
	private Class<? extends Enum> enumeration;

	public void setEnum(Class<? extends Enum> e) {
		this.enumeration = e;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		setValue(Enum.valueOf(enumeration, ((String) this.inputValue).toUpperCase()));
	}

}
