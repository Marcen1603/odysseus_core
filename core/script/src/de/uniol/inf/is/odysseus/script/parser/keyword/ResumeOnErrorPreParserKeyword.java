package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class ResumeOnErrorPreParserKeyword extends AbstractPreParserKeyword {

	public static final String RESUME_ON_ERROR_FLAG = "__resumeOnError__";
	public static final String KEYWORD = "RESUMEONERROR";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		try {
			Boolean.valueOf(parameter);
		} catch( Throwable t ) {
			throw new OdysseusScriptException("Could not parse value '" + parameter + "' for ResumeOnError", t);
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		variables.put(RESUME_ON_ERROR_FLAG, Boolean.valueOf(parameter));
		
		return null;
	}

}
