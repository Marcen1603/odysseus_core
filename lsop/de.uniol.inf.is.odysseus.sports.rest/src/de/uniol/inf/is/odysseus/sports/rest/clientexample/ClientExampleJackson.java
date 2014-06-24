package de.uniol.inf.is.odysseus.sports.rest.clientexample;

import java.io.IOException;

import org.restlet.resource.ClientResource;

import dto.UserDAO;

public class ClientExampleJackson {
	
	public static void main(String[] args) throws IOException {
		ClientResource res = new ClientResource("http://localhost:8182/sports/user");
		UserDAO user = res.get(UserDAO.class);
		System.out.println(user.getUsername());
		
	//	UserDAO user1 = new UserDAO("Thomas Mueller");
	//	res.put(user1);
	}
}
