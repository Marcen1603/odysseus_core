/**
 * 
 */
package de.uniol.inf.is.soop.webApp.session;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jbrode
 * 
 */
public class UserManager {

	private static UserManager manager = null;

	private HashMap<String, User> users = new HashMap<String, User>();
	
	private final static String passwordSalt = "lorumHolyGraleIpsum";

	public static UserManager getInstance() {
		if (manager == null) {
			manager = new UserManager();
			
			
			/* Bad hardcoding for prototype*/
			
			List<Usergroup> groupList = new LinkedList<Usergroup>();
			groupList.add(new Usergroup("user"));
			groupList.add(new Usergroup("admins"));
			/* build hash to compare */
			byte[] bytesOfMessage;
			try {
				bytesOfMessage = ("flut" + getPasswordSalt()).getBytes("UTF-8");
				String hash =  MessageDigest.getInstance("MD5").digest(bytesOfMessage).toString();
				User u = new User(1, "admin", "Capitain Ahab", hash, groupList);
				manager.addUser(u);
			} 
			catch (UnsupportedEncodingException e) { 
				e.printStackTrace();}
			catch (NoSuchAlgorithmException e) { 
				e.printStackTrace();}
			
		}
		return manager;
	}

	private UserManager() {
	}


	public User getUserByName(String name) throws Exception {
		User u = null;
		
		if (users.containsKey(name)) {
			u = users.get(name);
		}
		
		if ( !(u instanceof User) ) {
			throw new Exception("Unknown User: " + name);
		}
		
		return u;
	}

	public void addUser(User u) {
		users.put(u.getUsername(), u);
	}

	public static String getPasswordSalt() {
		return passwordSalt;
	}

}
