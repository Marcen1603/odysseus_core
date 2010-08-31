package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParser;
import de.uniol.inf.is.odysseus.rcp.viewer.query.ParameterTransformationConfigurationRegistry;

/**
 * Realisiert das TRANSCFG-Schlüsselwort für den PreParser. Wenn eine
 * Transformationskonfiguration angegeben wird, muss dieser dem
 * <code>IAdvancedExecutor</code> bekannt sein.
 * 
 * @author Timo Michelsen
 * 
 */
public class TransCfgPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(QueryTextParser parser, String parameter) throws QueryTextParseException {
		if (parameter.length() == 0)
			throw new QueryTextParseException("Parameter needed for #TRANCFG");

		ParameterTransformationConfigurationRegistry registry = ParameterTransformationConfigurationRegistry.getInstance();
		if (!registry.getTransformationConfigurationNames().contains(parameter)) {
			throw new QueryTextParseException("TransformationCfg " + parameter + " not found");
		}
		parser.setVariable("TRANSCFG", parameter);
	}

	@Override
	public void execute(QueryTextParser parser, String parameter, User user) throws QueryTextParseException {
		parser.setVariable("TRANSCFG", parameter);
	}

}
