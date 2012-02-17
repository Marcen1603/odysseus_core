package de.uniol.inf.is.odysseus.mep.functions;


public class ContainsFunction extends AbstractBooleanStringFunction {

	private static final long serialVersionUID = -2241632788238873550L;

	@Override
	public String getSymbol() {
		return "strcontains";
	}

	@Override
	public Boolean getValue() {
		return ((String)getInputValue(0)).contains((String)getInputValue(1));
	}


}
