package de.uniol.inf.is.odysseus.base;

import java.io.Reader;
import java.util.List;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.usermanagement.User;

public interface IQueryParser {
	public String getLanguage();
	public List<IQuery> parse(String query, User user) throws QueryParseException;
	public List<IQuery> parse(Reader reader, User user) throws QueryParseException;
}
