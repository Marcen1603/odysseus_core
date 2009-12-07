package de.uniol.inf.is.odysseus.parser.eca;

import java.io.Reader;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;

public class ECAParser implements IQueryParser{

	@Override
	public String getLanguage() {
		return "ECA";
	}

	@Override
	public List<ILogicalOperator> parse(String query)
			throws QueryParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ILogicalOperator> parse(Reader reader)
			throws QueryParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
