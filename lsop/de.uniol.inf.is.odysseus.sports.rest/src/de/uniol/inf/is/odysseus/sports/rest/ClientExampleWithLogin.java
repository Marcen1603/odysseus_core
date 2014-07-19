package de.uniol.inf.is.odysseus.sports.rest;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class ClientExampleWithLogin {

	public static void main(String[] args) throws ResourceException, IOException {
		
		String url = "http://localhost:8182/sports/";
		String query = "{\"statisticType\": \"global\", \"gameType\": \"soccer\", \"name\": \"passes\"}";
		String loginInfo = "{\"username\" : \"System\" , \"password\" : \"manager\"}";
				
		
		
		StringRepresentation loginRep = new StringRepresentation(loginInfo);
		loginRep.setMediaType(MediaType.APPLICATION_JSON);		
		new ClientResource(url+"login").post(loginRep).write(System.out);
		
		
		StringRepresentation queryRep = new StringRepresentation(query);
		queryRep.setMediaType(MediaType.APPLICATION_JSON);		
		new ClientResource(url+"querywithlogin").post(queryRep).write(System.out);
	}
}
