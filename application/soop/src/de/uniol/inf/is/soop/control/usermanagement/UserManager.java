/**
 * 
 */
package de.uniol.inf.is.soop.control.usermanagement;

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
	private HashMap<String, Usergroup> usergroups = new HashMap<String, Usergroup>();
	
	private final static String passwordSalt = "lorumHolyGraleIpsum";

	public static UserManager getInstance() {
		if (manager == null) {
			manager = new UserManager();
			
			
			/* Bad hardcoding for prototype*/
			
			HashMap<String, Usergroup> groupList = new HashMap<String, Usergroup>();
			groupList.put("users", new Usergroup("users"));
			groupList.put("admins", new Usergroup("admins"));
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
		// set user id
		if(u.getId() < 1){
			//get highes asigned nummber
			int maxId = 0;
			for (User i : users.values()){
				if (i.getId() > maxId) maxId = i.getId();
			}
			u.setId(maxId+1);
		}
		users.put(u.getUsername(), u);
	}

	public static String getPasswordSalt() {
		return passwordSalt;
	}

	/**
	 * @return the users
	 */
	public HashMap<String, User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(HashMap<String, User> users) {
		this.users = users;
	}

	/**
	 * @return the usergroups
	 */
	public HashMap<String, Usergroup> getUsergroups() {
		return usergroups;
	}
	
	/**
	 * @return a usergroup
	 */
	public Usergroup getUsergroup(String g) {
		return usergroups.get(g);
	}

	/**
	 * @param usergroups the usergroups to set
	 */
	public void setUsergroups(HashMap<String, Usergroup> usergroups) {
		this.usergroups = usergroups;
	}

}
