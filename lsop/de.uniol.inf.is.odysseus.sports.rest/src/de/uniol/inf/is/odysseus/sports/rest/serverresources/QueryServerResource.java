package de.uniol.inf.is.odysseus.sports.rest.serverresources;

import java.util.Collection;

import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ServerResource;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.StandardExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.sports.rest.dao.DataTransferObject;
import de.uniol.inf.is.odysseus.sports.rest.dao.PeerSocket;
import de.uniol.inf.is.odysseus.sports.rest.resources.IQueryResource;
import de.uniol.inf.is.odysseus.sports.rest.socket.SocketService;

public class QueryServerResource extends ServerResource implements
		IQueryResource {

	@Override
	public void receiveQuery(String sportsQL) {

		Response r = getResponse();
		try {
			Collection<Integer> queryIDs = StandardExecutor.getInstance().addQuery(sportsQL, "SportsQL",
					OdysseusRCPPlugIn.getActiveSession(), "Standard", Context.empty());
			
			int firstQueryID = queryIDs.iterator().next();
			
			//login with default user: System
			ISession session = SocketService.getInstance().login();
			
			//get SocketInformation
			PeerSocket peerSocket = SocketService.getInstance().getConnectionInformation(session.getToken(), firstQueryID);
		
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
