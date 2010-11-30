package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.ActiveUser;

public class LogoutUserPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(Map<String, String> variables, String parameter) throws QueryTextParseException {
		// Eine Validierung ist hier nicht sinnvoll (vorerst). Da bei der Validierung keine
		// User eingeloggt werden...
		
		// TODO: Später LOGOUT nocheinmal anschauen
	}

	@Override
	public void execute(Map<String, String> variables, String parameter) throws QueryTextParseException {
		
		if( UserStack.isEmpty())
			throw new QueryTextParseException("Too many LOGOUTs or too few LOGINs");
		
		// User holen
		User user = ActiveUser.getActiveUser();
		try {
			UserManagement.getInstance().logout(user, user.getUsername());
			
			// Vorherigen aktiven User als aktiv markieren
			ActiveUser.setActiveUser(UserStack.pop());
			
		} catch( Exception ex ) {
			ex.printStackTrace();
			throw new QueryTextParseException("Cannot logout user " + user.getUsername());
		}
	}

}
