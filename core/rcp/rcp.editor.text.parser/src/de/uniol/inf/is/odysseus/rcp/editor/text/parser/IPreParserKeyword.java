package de.uniol.inf.is.odysseus.rcp.editor.text.parser;

import java.util.Map;


/**
 * Schnittstelle, welches ein Schlüsselwort für den Preparser darstellt. Wird
 * vom QueryTextParser verwendet. Dadurch kann der Nutzer eigene Befehle
 * definieren.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IPreParserKeyword {

	public void validate(Map<String, String> variables, String parameter) throws QueryTextParseException;
	public Object execute(Map<String, String> variables, String parameter) throws QueryTextParseException;
}
