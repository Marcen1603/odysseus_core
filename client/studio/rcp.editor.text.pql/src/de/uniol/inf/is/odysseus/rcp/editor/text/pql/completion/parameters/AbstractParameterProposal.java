package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.parameters;

public abstract class AbstractParameterProposal implements IParameterProposal{

	public AbstractParameterProposal() {
		super();
	}

	protected String formatString(String value) {
		return "'"+value+"'";
	}
	
	@Override
	public String formatPosition(int position, String value) {	
		return value;
	}

}