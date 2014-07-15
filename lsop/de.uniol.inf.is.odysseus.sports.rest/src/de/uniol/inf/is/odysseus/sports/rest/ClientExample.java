package de.uniol.inf.is.odysseus.sports.rest;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;


public class ClientExample {

	public static void main(String[] args) throws ResourceException, IOException {
		String query = "{\"parameters\" : {\"start\" : 0 , \"ende\" : \"test\"},  \"statisticType\": \" player \", \"entityId\": 16, \"gameType\": \" soccer \", \"name\": \"mileageplayer\"}";
		
		StringRepresentation stringRep = new StringRepresentation(query);
		stringRep.setMediaType(MediaType.APPLICATION_JSON);
		
		// "http://localhost:8182/sports/query"
		ClientResource resource = new ClientResource("http://localhost:8182/sports/query");
		resource.post(stringRep).write(System.out);
	}

}
