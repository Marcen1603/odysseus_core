package de.uniol.inf.is.odysseus.script.keyword;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;

public class CreateStoredProcedureKeyword extends AbstractPreParserKeyword {

	public static final String STORED_PROCEDURE = "PROCEDURE";
	private static final String KEY_BEGIN = "BEGIN";
	private static final String KEY_END = "END";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {

	}

	@Override
	public List<IExecutorCommand>  execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {	
		int start = parameter.indexOf(KEY_BEGIN) + KEY_BEGIN.length();
		int end = parameter.indexOf(KEY_END);
		String[] lines = parameter.split(System.lineSeparator());
		String headLine = lines[0].trim();
		String name = headLine.substring(STORED_PROCEDURE.length() + 1).trim();
		String procedureText = parameter.substring(start, end);
		// fetch variables
		List<String> procVars = new ArrayList<>();
		for (int i = 1; i < lines.length; i++) {
			if (lines[i].indexOf(KEY_BEGIN) != -1) {
				break;
			}
			// each line should be a variable name
			procVars.add(lines[i].trim().toUpperCase());
		}

		// next, we check, if they are variables within the procedure that are not defined
		Set<String> existingVars = new HashSet<>();
		for (String line : lines) {
			existingVars.addAll(OdysseusScriptParser.findReplacements(line));
		}
		if(!procVars.containsAll(existingVars)){
			existingVars.removeAll(procVars);
			throw new OdysseusScriptException("Missing parameter definitions in stored procedure "+name+": "+existingVars);
		}

		StoredProcedure sp = new StoredProcedure(name, procedureText, procVars);
		executor.addStoredProcedure(name, sp, caller);
		return null;
	}

}
