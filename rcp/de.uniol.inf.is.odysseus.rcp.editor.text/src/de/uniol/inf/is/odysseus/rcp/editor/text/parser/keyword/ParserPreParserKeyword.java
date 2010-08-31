package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParser;

/**
 * Realisiert das PARSER-Schlüsselwort für den PreParser. Wenn ein Parser
 * angegeben wird, muss dieser dem <code>IAdvancedExecutor</code> bekannt sein.
 * 
 * @author Timo Michelsen
 * 
 */
public class ParserPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(QueryTextParser parser, String parameter) throws QueryTextParseException {
		if (parameter.length() == 0)
			throw new QueryTextParseException("Parameter needed for #PARSER");

		IAdvancedExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
		try {
			if (!executor.getSupportedQueryParser().contains(parameter)) {
				throw new QueryTextParseException("Parser " + parameter + " does not exist");
			}
			parser.setVariable("PARSER", parameter);
		} catch (QueryTextParseException ex ) {
			throw ex;
		} catch (Exception ex) {
			throw new QueryTextParseException("Unknown error", ex);
		}
	}

	@Override
	public void execute(QueryTextParser parser, String parameter, User user) throws QueryTextParseException {
		parser.setVariable("PARSER", parameter);
	}
}
