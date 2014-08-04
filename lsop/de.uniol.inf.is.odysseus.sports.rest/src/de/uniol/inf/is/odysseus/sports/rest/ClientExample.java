package de.uniol.inf.is.odysseus.sports.rest;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class ClientExample {

	public static void main(String[] args) throws ResourceException, IOException {
		// Passes for team
				String statisticsType = "team";
				String gameType = "soccer";
				String name = "passes";
				
				// Mileage for player
				statisticsType = "player";
				gameType = "soccer";
				name = "mileage";
				
				// Mileage for team
				statisticsType = "team";
				gameType = "soccer";
				name = "mileage";
				
				// Global game
//				statisticsType = "global";
//				gameType = "soccer";
//				name = "game";
				
				// Shots on goal for player
//				statisticsType = "player";
//				gameType = "soccer";
//				name = "shotongoal";
				
				// Game time
				statisticsType = "global";
				gameType = "soccer";
				name = "gameTime";
				
				// Test select
				statisticsType = "global";
				gameType = "soccer";
				name = "testSelect";
				
				
				String query = "{"
						 +	"\"statisticType\": \"" + statisticsType + "\","
						 + 	"\"gameType\": \"" + gameType + "\","
						 +  "\"entityId\": 16,"
						 +  "\"name\": \"" + name + "\","
						 +  "\"parameters\": {"
						 +  	"\"time\": { "
						 +  		"\"start\": 100," 
						 +  	 	"\"end\": 10000,"
						 + 			"\"time\": 90"
						 + 		"}, "
						 +  	"\"space\": { "
						 +  		"\"startx\": 100," 
						 +  	 	"\"starty\": 10000," 
						 +  		"\"endx\": 1001," 
						 +  	 	"\"endy\": 1221,"
						 + 			"\"space\": \"all\""
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
