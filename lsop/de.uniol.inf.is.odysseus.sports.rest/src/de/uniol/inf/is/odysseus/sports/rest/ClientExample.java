package de.uniol.inf.is.odysseus.sports.rest;

import java.io.IOException;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class ClientExample {

	public static void main(String[] args) throws ResourceException, IOException {
		String query = "{'teamstatistics' : [{'gametype' : 'soccer', 'entity_id' : 1, 'statisticname' : 'mileage', 'parameters' : {'time': {'start' : 0, 'end' : 90}, 'space' : {'start    x' : 0, 'endx' : 90, 'starty' : 80, 'endy' : 120} }}]}";
		
		ClientResource resource = new ClientResource("http://localhost:8182/sports/query");

		resource.post(query).write(System.out);
	}

}
