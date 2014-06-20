package de.uniol.inf.is.odysseus.sports.rest.clientexample;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

public class ClientExample {
	
	public static void main(String[] args) throws IOException {
		ClientResource res = new ClientResource("http://localhost:8182/sports/postexample");
		res.setMethod(Method.POST);
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", "Thomas Mueller");
			StringRepresentation repr = new StringRepresentation(obj.toString());
			repr.setMediaType(MediaType.APPLICATION_JSON);
			res.post(repr).write(System.out);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
