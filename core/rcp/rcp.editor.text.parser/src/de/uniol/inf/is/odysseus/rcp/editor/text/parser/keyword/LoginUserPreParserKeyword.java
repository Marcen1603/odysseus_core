package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.ActiveUser;
import de.uniol.inf.is.odysseus.usermanagement.client.UserStack;

public class LoginUserPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(Map<String, String> variables, String parameter) throws QueryTextParseException {
		// kann hier nicht validieren, da es sein kann, dass in der gleichen anfrage zuvor
		// erst der Nutzer angelegt wurde.
	}

	@Override
	public Object execute(Map<String, String> variables, String parameter) throws QueryTextParseException {
		String[] para = getParameters(parameter);
		String userName = para[0];
		String password = para[1];
		
		User user = UserManagement.getInstance().login(userName, password, false);
		if( user == null ) 
			throw new QueryTextParseException("Login with user " + userName + " failed");
		
		// Alten Nutzer für LOGOUT merken
		UserStack.push(ActiveUser.getActiveUser());
		
		// Auch als aktiven User markieren
		ActiveUser.setActiveUser(user);
		
		return user;
	}

	// Liefert von einem gegebenen String userName und ggfs Password
	private String[] getParameters(String parameter) {
		if( parameter.contains(" ")) {
			// Password und Name gegeben
			return parameter.split("\\ ");
		} else {
			// Nur Nutzername (kein Password nötig?)
			return new String[] { parameter, "" };
		}
	}
}
