package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;

/**
 * Realisiert das TRANSCFG-Schlüsselwort für den PreParser. Wenn eine
 * Transformationskonfiguration angegeben wird, muss dieser dem
 * <code>IExecutor</code> bekannt sein.
 * 
 * @author Timo Michelsen
 * 
 */
public class TransCfgPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(Map<String, String> variables, String parameter) throws QueryTextParseException {
		if (parameter.length() == 0)
			throw new QueryTextParseException("Parameter needed for #TRANCFG");

		if (!ExecutorHandler.getExecutor().getQueryBuildConfigurationNames().contains(parameter)) {
			throw new QueryTextParseException("TransformationCfg " + parameter + " not found");
		}
		variables.put("TRANSCFG", parameter);
	}

	@Override
	public void execute(Map<String, String> variables, String parameter) throws QueryTextParseException {
		variables.put("TRANSCFG", parameter);
	}

}
