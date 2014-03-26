package de.uniol.inf.is.odysseus.rcp.editor.text;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class OdysseusScriptStructureProvider {

	private static final String ODYSSEUS_SCRIPT = "OdysseusScript";
	public static final String PARAMETER_KEY = "#";
	public static final String REPLACEMENT_START_KEY = "${";
	public static final String REPLACEMENT_END_KEY = "}";
	public static final String SINGLE_LINE_COMMENT_KEY = "///";

	public static List<String> getKeywords() {
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		IExecutor executor = OdysseusRCPEditorTextPlugIn.getExecutor();
		Map<String, List<String>> values = executor.getQueryParserTokens(
				ODYSSEUS_SCRIPT, caller);
		return values.get("KEYWORDS");
	}

	public static List<String> getParametersForKeyword(String keyword) {
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		IExecutor executor = OdysseusRCPEditorTextPlugIn.getExecutor();
		return executor.getQueryParserSuggestions(ODYSSEUS_SCRIPT, keyword,
				caller);
	}

	public static List<String> getStaticWords() {
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		IExecutor executor = OdysseusRCPEditorTextPlugIn.getExecutor();
		Map<String, List<String>> values = executor.getQueryParserTokens(
				ODYSSEUS_SCRIPT, caller);
		return values.get("STATIC");
	}

	public static boolean isDeprecated(String key) {
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		IExecutor executor = OdysseusRCPEditorTextPlugIn.getExecutor();
		Map<String, List<String>> values = executor.getQueryParserTokens(
				ODYSSEUS_SCRIPT, caller);
		if (values.get("DEPRECATED").contains(key)) {
			return true;
		}
		return false;
		
	}
}
