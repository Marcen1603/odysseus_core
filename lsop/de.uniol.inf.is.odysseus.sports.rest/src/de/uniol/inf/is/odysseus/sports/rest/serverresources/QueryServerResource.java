package de.uniol.inf.is.odysseus.sports.rest.serverresources;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ServerResource;

import dao.DataTransferObject;
import dao.PeerSocket;
import de.uniol.inf.is.odysseus.sports.rest.resources.IQueryResource;

public class QueryServerResource extends ServerResource implements IQueryResource{

	@Override
	public void receiveQuery(String jsonString) {
		
		Response r = getResponse();
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			System.out.println(jsonObj);
			
			//handle Object ...get SocketInfos
			PeerSocket s = new PeerSocket("127.0.1", "8080");
			
			DataTransferObject dto = new DataTransferObject("SocketInfo", s);
			r.setEntity(new JacksonRepresentation<DataTransferObject>(dto));
			r.setStatus(Status.SUCCESS_OK);
			
		} catch (JSONException e) {
			e.printStackTrace();
			r.setStatus(Status.SERVER_ERROR_INTERNAL);
		}
		
		
	}

}
