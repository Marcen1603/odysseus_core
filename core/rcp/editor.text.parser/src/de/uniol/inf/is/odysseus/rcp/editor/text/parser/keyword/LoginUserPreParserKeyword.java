package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.user.ActiveUser;
import de.uniol.inf.is.odysseus.usermanagement.HasNoPermissionException;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

public class LoginUserPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(Map<String, String> variables, String parameter) throws QueryTextParseException {
		// Parameter beinhaltet Name und Password (in Klartext!)
		// TODO: Annahme, Name und Password sind einzelne Wörter!
		
		String[] para = getParameters(parameter);
		String userName = para[0];
		String password = para[1];
		
		// Prüfen, ob mit User eingeloggt werden könnte
		User user = UserManagement.getInstance().login(userName, password, false);
		if( user == null ) {
			throw new QueryTextParseException("Login with user " + userName + " failed");
		} else {
			// da nur zur Validierung --> wieder ausloggen
			try {
				UserManagement.getInstance().logout(user, userName);
			} catch( HasNoPermissionException ex ) {
				ex.printStackTrace();
				throw new QueryTextParseException("Cannot logout with " + userName, ex);
			}
		}
	}

	@Override
	public void execute(Map<String, String> variables, String parameter) throws QueryTextParseException {
		String[] para = getParameters(parameter);
		String userName = para[0];
		String password = para[1];
		
		User user = UserManagement.getInstance().login(userName, password, false);
		
		// Alten Nutzer für LOGOUT merken
		UserStack.push(ActiveUser.getActiveUser());
		
		// Auch als aktiven User markieren
		ActiveUser.setActiveUser(user);
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
