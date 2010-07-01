package de.uniol.inf.is.odysseus.base.usermanagement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Tenant {
	private List<User> users = new LinkedList<User>();
	private String name;
	private int id;
	private IServiceLevelAgreement sla;
	
	public Tenant(int id, String name, IServiceLevelAgreement sla){
		this.id = id;
		this.name = name;
		this.sla = sla;
	}
	
	public void addUser(User user){
		users.add(user);
	}
	
	public void removeUser(User user){
		users.remove(user);
	}
	
	public List<User> getUsers(){
		return Collections.unmodifiableList(users);
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public IServiceLevelAgreement getServiceLevelAgreement() {
		return sla;
	}

}
