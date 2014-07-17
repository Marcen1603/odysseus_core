package de.uniol.inf.is.odysseus.sports.rest;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class ClientExample {

	public static void main(String[] args) throws ResourceException, IOException {
		String query = "{\"parameters\" : {"
				+ "\"start\" : 0 , \"end\" : 999999999999999,"
				+ "\"startx\" : 10753295594424116, \"starty\" : -33960, \"endx\" : 52489, \"endy\" : 33965},"
				+ "\"statisticType\": \" player \", "
				+ "\"entityId\": 16, "
				+ "\"gameType\": \" soccer \", "
				+ "\"name\": \"pathwithball\"}";
		/*
		query = "{"
		 +"\"statisticType\": \"player\","
		 + "\"gameType\": \"soccer\","
		 +  "\"entityId\": 16,"
		 +   "\"name\": \"pathwithball\","
		 +   "\"parameters\": {"
		 +    	"\"start\": 10753295594424116,"
		 +    	"\"ende\" : 9999999999999999,"
		 +      "\"startx\":-50,"
		 +      "\"starty\":-33960"
		 +      "\"endx\":52489"
		 +      "\"endy\":33965"
		 +   "}"
		 + "}";
		*/
		StringRepresentation stringRep = new StringRepresentation(query);
		stringRep.setMediaType(MediaType.APPLICATION_JSON);
		
		// "http://localhost:8182/sports/query"
		ClientResource resource = new ClientResource("http://localhost:8182/sports/query");
		resource.post(stringRep).write(System.out);
	}
}
