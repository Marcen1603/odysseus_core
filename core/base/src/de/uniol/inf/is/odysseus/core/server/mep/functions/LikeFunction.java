package de.uniol.inf.is.odysseus.core.server.mep.functions;


public class LikeFunction extends AbstractBooleanStringFunction{

	private static final long serialVersionUID = 7705981108537461304L;

	@Override
	public String getSymbol() {
		return "strlike";
	}

	@Override
	public Boolean getValue() {
		return ((String)getInputValue(0)).matches((String)getInputValue(1));
	}

}
