package de.uniol.inf.is.odysseus.base.usermanagement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Tenant {
	private List<User> users = new LinkedList<User>();
	private String name;
	private IServiceLevelAgreement sla;
	
	Tenant(String name, IServiceLevelAgreement sla){
		this.name = name;
		this.sla = sla;
	}
	
	void addUser(User user) throws TooManyUsersException {
		if (users.size() < sla.getMaxUsers()){
			users.add(user);
		}else{
			throw new TooManyUsersException("Current limit: "+sla.getMaxUsers());
		}
	}
	
	void removeUser(User user){
		users.remove(user);
	}
	
	public List<User> getUsers(){
		return Collections.unmodifiableList(users);
	}
	
	public String getName() {
		return name;
	}
	
	public IServiceLevelAgreement getServiceLevelAgreement() {
		return sla;
	}

}
