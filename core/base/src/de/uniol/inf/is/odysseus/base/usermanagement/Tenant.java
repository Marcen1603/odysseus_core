package de.uniol.inf.is.odysseus.base.usermanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tenant implements Serializable{

	private static final long serialVersionUID = -4491105336941528822L;

	private List<User> users = new ArrayList<User>();
	private String name;
	private IServiceLevelAgreement sla;
	
	Tenant(String name, IServiceLevelAgreement sla){
		this.name = name;
		this.sla = sla;
	}
	
	void addUser(User user) throws TooManyUsersException {
		if (sla.getMaxUsers() == -1 || users.size() < sla.getMaxUsers()){
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

	@Override
	public String toString() {
		return name+" "+sla+" "+users;
	}
	
}
