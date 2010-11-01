package de.uniol.inf.is.odysseus.usermanagement;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryAction;

public class UserActionFactory {
	
	static public IUserAction valueOf(String value){
		IUserAction action = DataDictionaryAction.valueOf(value);
		if (action != null){
			return action;
		}else{
			action = UserManagementAction.valueOf(value);
			return action;
		}
	}

	public static boolean needsNoObject(IUserAction action) {
		if (action instanceof DataDictionaryAction){
			return DataDictionaryAction.needsNoObject(action);
		}
		if (action instanceof UserManagementAction){
			return UserManagementAction.needsNoObject(action);
		}
		return false;
	}

	public static String getAliasObject(IUserAction action) {
		if (action instanceof DataDictionaryAction){
			return DataDictionaryAction.alias;
		}
		if (action instanceof UserManagementAction){
			return UserManagementAction.alias;
		}		
		
		return null;
	}
	
	
	
	
}
