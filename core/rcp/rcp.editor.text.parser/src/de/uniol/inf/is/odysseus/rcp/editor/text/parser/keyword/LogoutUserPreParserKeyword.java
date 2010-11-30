package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.editor.text.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

public class LogoutUserPreParserKeyword extends AbstractPreParserKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter) throws QueryTextParseException {
		// Eine Validierung ist hier nicht sinnvoll (vorerst). Da bei der Validierung keine
		// User eingeloggt werden...
		
		// TODO: Später LOGOUT nocheinmal anschauen
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter) throws QueryTextParseException {
		
		User caller = getCurrentUser(variables);
		
		try {
			UserManagement.getInstance().logout(caller,parameter);
			
		} catch( Exception ex ) {
			ex.printStackTrace();
			throw new QueryTextParseException("Cannot logout user " + parameter);
		}
		return null;
	}

}
