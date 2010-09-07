package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;

/**
 * Realisiert das PARSER-Schlüsselwort für den PreParser. Wenn ein Parser
 * angegeben wird, muss dieser dem <code>IAdvancedExecutor</code> bekannt sein.
 * 
 * @author Timo Michelsen
 * 
 */
public class ParserPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(Map<String, String> variables, String parameter) throws QueryTextParseException {
		if (parameter.length() == 0)
			throw new QueryTextParseException("Parameter needed for #PARSER");

		IAdvancedExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
		try {
			if (!executor.getSupportedQueryParser().contains(parameter)) {
				throw new QueryTextParseException("Parser " + parameter + " does not exist");
			}
			variables.put("PARSER", parameter);
		} catch (QueryTextParseException ex ) {
			throw ex;
		} catch (Exception ex) {
			throw new QueryTextParseException("Unknown error", ex);
		}
	}

	@Override
	public void execute(Map<String, String> variables, String parameter) throws QueryTextParseException {
		variables.put("PARSER", parameter);
	}
}
