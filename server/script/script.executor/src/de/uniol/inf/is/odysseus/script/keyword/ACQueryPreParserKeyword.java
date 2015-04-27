package de.uniol.inf.is.odysseus.script.keyword;

public class ACQueryPreParserKeyword extends AbstractQueryPreParserKeyword {

	public static final String NAME = "ACQUERY";
	
	@Override
	protected boolean startQuery() {
		return true;
	}

	@Override
	protected boolean isACQuery() {
		return true;
	}
}
