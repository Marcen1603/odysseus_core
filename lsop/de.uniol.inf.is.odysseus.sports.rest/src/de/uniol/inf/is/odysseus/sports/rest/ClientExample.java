package de.uniol.inf.is.odysseus.sports.rest;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class ClientExample {

	public static void main(String[] args) throws ResourceException, IOException {
		String query = "{"
				 +	"\"statisticType\": \"team\","
				 + 	"\"gameType\": \"soccer\","
				 +  "\"entityId\": 16,"
				 +  "\"name\": \"passes\","
				 +  "\"parameters\": {"
				 +  	"\"time\": { "
				 +  		"\"start\": 100," 
				 +  	 	"\"end\": 10000" 
				 + 		"}, "
				 +  	"\"space\": { "
				 +  		"\"startx\": 100," 
				 +  	 	"\"starty\": 10000," 
				 +  		"\"endx\": 1001," 
				 +  	 	"\"endy\": 1221"
				 + 		"}, "
				 +  	"\"distance\": { "
				 +  		"\"minDistance\": 100," 
				 +  	 	"\"maxDistance\": 10000" 
				 + 		"}, "
				 +  	"\"entityIdIsPassReceiver\": true " 
				 +   "}"
				 + "}";
		StringRepresentation stringRep = new StringRepresentation(query);
		stringRep.setMediaType(MediaType.APPLICATION_JSON);
		
		ClientResource resource = new ClientResource("http://localhost:8182/sports/query");
		resource.post(stringRep).write(System.out);
	}
}
