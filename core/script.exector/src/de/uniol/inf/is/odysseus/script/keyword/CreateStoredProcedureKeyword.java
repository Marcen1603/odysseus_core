package de.uniol.inf.is.odysseus.script.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class CreateStoredProcedureKeyword extends AbstractPreParserExecutorKeyword {

	public static final String STORED_PROCEDURE = "PROCEDURE";
	private static final String KEY_BEGIN = "BEGIN";
	private static final String KEY_END = "END";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {

	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		System.out.println(parameter);
		int start = parameter.indexOf(KEY_BEGIN)+KEY_BEGIN.length();		
		int end = parameter.indexOf(KEY_END);
		String headLine = parameter.split(System.lineSeparator())[0].trim();
		String name = headLine.substring(STORED_PROCEDURE.length()+1).trim();
		String procedure = parameter.substring(start, end);
		getServerExecutor().addStoredProcedure(name, procedure, caller);
		return null;
	}

}
