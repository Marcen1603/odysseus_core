package de.uniol.inf.is.odysseus.sports.rest.serverresources;

import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.sports.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.sports.rest.dao.DataTransferObject;
import de.uniol.inf.is.odysseus.sports.rest.dao.PeerSocket;
import de.uniol.inf.is.odysseus.sports.rest.resources.IQueryResource;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.ISportsQLParser;
import de.uniol.inf.is.odysseus.sports.sportsql.parser.SportsQLQuery;
import de.uniol.inf.is.odysseus.sports.sportsql.registry.SportsQLParserRegistry;

public class QueryWithLoginServerResource extends ServerResource implements
		IQueryResource {
	
	public static String sToken;

	@Override
	public void receiveQuery(String sportsQL) {
		Response response = getResponse();
		try {
			// is always null :(
			String securityToken = response.getCookieSettings().getValues("securityToken");
			
			ISession session = UserManagementProvider.getSessionmanagement().login(securityToken);
			if (securityToken  == null || session == null) {
		       throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Please log in");
			} else {
				SportsQLQuery query = SportsQLParserRegistry.createSportsQLQuery(sportsQL);
				ISportsQLParser parser = SportsQLParserRegistry.getSportsQLParser(query);

				ILogicalQuery logicalQuery = parser.parse(query);			
				IServerExecutor executor = ExecutorServiceBinding.getExecutor();
				executor.addQuery(logicalQuery.getLogicalPlan(), session, "Standard");
			
				PeerSocket s = new PeerSocket("127.0.1", "8080");

				DataTransferObject dto = new DataTransferObject("SocketInfo", s);
				response.setEntity(new JacksonRepresentation<DataTransferObject>(dto));
				response.setStatus(Status.SUCCESS_OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(Status.SERVER_ERROR_INTERNAL);
		}		
	}
}
