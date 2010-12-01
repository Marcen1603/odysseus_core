package de.uniol.inf.is.odysseus.rcp.editor.text.parser;

import java.util.Map;

import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.ActiveUser;


public abstract class AbstractPreParserKeyword implements IPreParserKeyword {
	
	protected User getCurrentUser(Map<String, Object> variables){
		Object user =  variables.get("USER");
		if (user == null){
			user = ActiveUser.getActiveUser();
		}
		return (User)user;
	}
	
	/**
	 * Get Parameters with blanc as delimmiter
	 * @param parameter
	 * @return
	 */
	protected String[] getSimpleParameters(String parameter) {
		if( parameter.contains(" ")) {
			return parameter.split("\\ ");
		} else {
			//only one parameter
			return new String[] { parameter, "" };
		}
	}



}
