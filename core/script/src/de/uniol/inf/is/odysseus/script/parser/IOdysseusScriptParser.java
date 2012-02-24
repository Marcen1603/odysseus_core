package de.uniol.inf.is.odysseus.script.parser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IOdysseusScriptParser {

	public void parseAndExecute(String completeText,
			ISession caller, ISink<?> defaultSink) throws OdysseusScriptException;

	public void execute(List<PreParserStatement> statements,
			ISession caller, ISink<?> defaultSink) throws OdysseusScriptException;

	public List<PreParserStatement> parseScript(String completeText,
			ISession caller) throws OdysseusScriptException;

	public List<PreParserStatement> parseScript(String[] textToParse,
			ISession caller) throws OdysseusScriptException;

	public Map<String, String> getReplacements(String text)
			throws OdysseusScriptException;

	public Map<String, String> getReplacements(String[] text)
			throws OdysseusScriptException;

	public Set<String> getKeywordNames();

	public String getParameterKey();
	public String getReplacementStartKey();
	public String getReplacementEndKey();
	public String getSingleLineCommentKey();
}