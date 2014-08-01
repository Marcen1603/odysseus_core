package de.uniol.inf.is.odysseus.sports.rest.serverresources;

import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ServerResource;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.StandardExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.sports.rest.dao.DataTransferObject;
import de.uniol.inf.is.odysseus.sports.rest.dao.PeerSocket;
import de.uniol.inf.is.odysseus.sports.rest.resources.IQueryResource;

public class QueryServerResource extends ServerResource implements
		IQueryResource {

	@Override
	public void receiveQuery(String sportsQL) {

		Response r = getResponse();
		try {
			StandardExecutor.getInstance().addQuery(sportsQL, "SportsQL",
					OdysseusRCPPlugIn.getActiveSession(), "Standard", Context.empty());

			// handle Object ...get SocketInfos
			PeerSocket s = new PeerSocket("127.0.1", "8080");

			DataTransferObject dto = new DataTransferObject("SocketInfo", s);
			r.setEntity(new JacksonRepresentation<DataTransferObject>(dto));
			r.setStatus(Status.SUCCESS_OK);

		} catch (Exception e) {
			e.printStackTrace();
			r.setStatus(Status.SERVER_ERROR_INTERNAL);
		}
	}

}
