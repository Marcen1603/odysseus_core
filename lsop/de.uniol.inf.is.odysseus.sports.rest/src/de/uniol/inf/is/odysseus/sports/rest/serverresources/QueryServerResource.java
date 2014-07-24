package de.uniol.inf.is.odysseus.sports.rest.serverresources;

import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ServerResource;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.sports.rest.dao.DataTransferObject;
import de.uniol.inf.is.odysseus.sports.rest.dao.PeerSocket;
import de.uniol.inf.is.odysseus.sports.rest.resources.IQueryResource;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.registry.SportsQLParserRegistry;

public class QueryServerResource extends ServerResource implements
		IQueryResource {

	@Override
	public void receiveQuery(String sportsQL) {

		Response r = getResponse();
		try {
			SportsQLQuery query = SportsQLParserRegistry.createSportsQLQuery(sportsQL);
			ISportsQLParser parser = SportsQLParserRegistry.getSportsQLParser(query);
			ILogicalQuery logicalQuery = parser.parse(query);
			System.out.println(logicalQuery.getName());

			// TODO Install query
			// TODO Talk with Marco about StandardExecutor
//			int test = StandardExecutor.getInstance().addQuery(
//					logicalQuery.getLogicalPlan(),
//					OdysseusRCPPlugIn.getActiveSession(),
//					"Standard");
			
			//StandardExecutor.getInstance().startQuery(test, OdysseusRCPPlugIn.getActiveSession());

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
