package de.uniol.inf.is.odysseus.base;

import java.io.Reader;
import java.util.List;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;

public interface IQueryParser {
	public String getLanguage();
	public List<IQuery> parse(String query) throws QueryParseException;
	public List<IQuery> parse(Reader reader) throws QueryParseException;
}
