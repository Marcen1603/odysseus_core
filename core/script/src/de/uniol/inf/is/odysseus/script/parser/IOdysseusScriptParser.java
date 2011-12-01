package de.uniol.inf.is.odysseus.script.parser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.usermanagement.User;

public interface IOdysseusScriptParser {

	public void parseAndExecute(String completeText,
			User caller, ISink<?> defaultSink) throws OdysseusScriptParseException;

	public void execute(List<PreParserStatement> statements,
			User caller, ISink<?> defaultSink) throws OdysseusScriptParseException;

	public List<PreParserStatement> parseScript(String completeText,
			User caller) throws OdysseusScriptParseException;

	public List<PreParserStatement> parseScript(String[] textToParse,
			User caller) throws OdysseusScriptParseException;

	public Map<String, String> getReplacements(String text)
			throws OdysseusScriptParseException;

	public Map<String, String> getReplacements(String[] text)
			throws OdysseusScriptParseException;

	public Set<String> getKeywordNames();

	public String getParameterKey();
	public String getReplacementStartKey();
	public String getReplacementEndKey();
	public String getSingleLineCommentKey();
}