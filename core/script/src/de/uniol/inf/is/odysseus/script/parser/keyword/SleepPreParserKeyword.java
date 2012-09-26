package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * Keyword to delay processing for some time (e.g. needed if a set of queries
 * should be send to odysseus with some delay
 * 
 * Delay in milliseconds
 * 
 * @author Marco Grawunder
 *
 */
 
public class SleepPreParserKeyword extends AbstractPreParserKeyword {

	public static final String SLEEP = "SLEEP";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller) throws OdysseusScriptException {
		if (parameter.length() == 0)
			throw new OdysseusScriptException("#"+SLEEP+" time needed");
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter,
			ISession caller) throws OdysseusScriptException {
		try {
			Thread.sleep(Long.parseLong(parameter));
		} catch (NumberFormatException | InterruptedException e) {
			throw new OdysseusScriptException(e);
		}		
		return null;
	}

}
