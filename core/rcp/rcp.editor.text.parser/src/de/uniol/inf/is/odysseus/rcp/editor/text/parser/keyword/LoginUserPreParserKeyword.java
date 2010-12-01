package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.editor.text.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

public class LoginUserPreParserKeyword extends AbstractPreParserKeyword{

	@Override
	public void validate(Map<String, Object> variables, String parameter) throws QueryTextParseException {
		// kann hier nicht validieren, da es sein kann, dass in der gleichen anfrage zuvor
		// erst der Nutzer angelegt wurde.
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter) throws QueryTextParseException {
		String[] para = getSimpleParameters(parameter);
		String userName = para[0];
		String password = para[1];
		
		User user = UserManagement.getInstance().login(userName, password, false);
		if( user == null ) 
			throw new QueryTextParseException("Login with user " + userName + " failed");
				
		// In den Variablen als aktiven User merken
		variables.put("USER", user);
		
		return user;
	}

}
