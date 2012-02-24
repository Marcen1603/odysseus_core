package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

public class DoubleParameter extends AbstractParameter<Double> {

	private static final long serialVersionUID = 5501094816405644498L;

	@Override
	protected void internalAssignment() {
		setValue((Double)this.inputValue);
	}

}
