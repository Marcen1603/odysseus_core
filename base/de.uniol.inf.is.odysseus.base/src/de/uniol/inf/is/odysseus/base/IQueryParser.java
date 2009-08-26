package de.uniol.inf.is.odysseus.base;

import java.io.Reader;
import java.util.List;

public interface IQueryParser {
	public String getLanguage();
	public List<ILogicalOperator> parse(String query) throws QueryParseException;
	public List<ILogicalOperator> parse(Reader reader) throws QueryParseException;
}
