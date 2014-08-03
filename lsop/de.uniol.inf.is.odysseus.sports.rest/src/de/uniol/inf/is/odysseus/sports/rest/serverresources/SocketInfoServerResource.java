package de.uniol.inf.is.odysseus.sports.rest.serverresources;


import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ServerResource;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.sports.rest.dao.DataTransferObject;
import de.uniol.inf.is.odysseus.sports.rest.dao.SocketInfo;
import de.uniol.inf.is.odysseus.sports.rest.resources.ISocketInfoResource;
import de.uniol.inf.is.odysseus.sports.rest.socket.SocketService;

public class SocketInfoServerResource extends ServerResource implements ISocketInfoResource {

	@Override
	public void getSocketInfoOfQuery(int queryId) {

		Response r = getResponse();
		try {
			
			//login with default user: System
			ISession session = SocketService.getInstance().login();
			
			//get SocketInformation
			SocketInfo peerSocket = SocketService.getInstance().getConnectionInformation(session.getToken(), queryId);
		
			if(peerSocket != null){
				DataTransferObject dto = new DataTransferObject("SocketInfo", peerSocket);
				r.setEntity(new JacksonRepresentation<DataTransferObject>(dto));
				r.setStatus(Status.SUCCESS_OK);
			}else{
				r.setStatus(Status.SERVER_ERROR_INTERNAL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			r.setStatus(Status.SERVER_ERROR_INTERNAL);
		}
	}
	
	
	
	
	
	




}
