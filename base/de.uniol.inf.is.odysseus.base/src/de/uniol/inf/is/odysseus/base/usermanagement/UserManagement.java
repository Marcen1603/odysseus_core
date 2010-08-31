package de.uniol.inf.is.odysseus.base.usermanagement;

import java.util.HashMap;
import java.util.Map;

public class UserManagement {
	
	static private UserManagement instance = null;
	
	public synchronized static UserManagement getInstance(){
		if (instance == null){
			instance = new UserManagement();
			// TODO: Später entfernen 
			try {
				instance.registerUser("Console","");
				instance.registerUser("Benchmark","");
				instance.registerUser("RCP","");
} catch (UsernameAlreadyUsedException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	private void registerUser(String username, String password) throws UsernameAlreadyUsedException{
		User user = registeredUsers.get(username);
		if (user == null){
			user = new User(username,password);
			// TODO: Persistieren des Users
			registeredUsers.put(username, user);
		}else{
			throw new UsernameAlreadyUsedException();
		}
		
	}

	private Map<String, User> registeredUsers = new HashMap<String, User>();
		
	public User login(String username, String password){
		// TODO: Ausbauen für ein richtiges UserManagement
		User user = registeredUsers.get(username);
		if (user != null){
			if (!user.validatePassword(password, false)){
				user = null;
			}
		}
		
		
		return null;
	}

	public User getUser(String username, String password) {
		User user = registeredUsers.get(username);
		if (user != null){
			if (!user.validatePassword(password, true)){
				user = null;
			}
		}
		return null;
	}
	

}
