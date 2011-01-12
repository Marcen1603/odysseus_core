package de.uniol.inf.is.odysseus.planmanagement;

import java.io.Reader;
import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.usermanagement.User;

public interface IQueryParser {
	public String getLanguage();
	public List<IQuery> parse(String query, User user, IDataDictionary dd) throws QueryParseException;
	public List<IQuery> parse(Reader reader, User user, IDataDictionary dd) throws QueryParseException;
}
