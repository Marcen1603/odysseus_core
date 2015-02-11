package de.uniol.inf.is.odysseus.sports.rest.serverresources;



import java.util.Collection;

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractSessionServerResource;
import de.uniol.inf.is.odysseus.rest.service.RestService;
import de.uniol.inf.is.odysseus.rest.socket.SocketInfo;
import de.uniol.inf.is.odysseus.rest.socket.SocketService;
import de.uniol.inf.is.odysseus.sports.distributor.registry.SportsQLDistributorRegistry;
import de.uniol.inf.is.odysseus.sports.distributor.webservice.DistributedQueryHelper;
import de.uniol.inf.is.odysseus.sports.distributor.webservice.DistributedQueryInfo;
import de.uniol.inf.is.odysseus.sports.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.sports.rest.dto.request.DistributedSportsQLSocketRequestDTO;
import de.uniol.inf.is.odysseus.sports.rest.dto.request.ExecuteSportsQLRequestDTO;
import de.uniol.inf.is.odysseus.sports.rest.dto.response.DistributedSportsQLSocketResponseDTO;
import de.uniol.inf.is.odysseus.sports.rest.dto.response.ExecuteSportsQLResponseDTO;

public class ExecuteSportsQLServerResource extends AbstractSessionServerResource  {

	public static final String PATH = "distributeSportsQL";

	@Post
	public ExecuteSportsQLResponseDTO executeSportsQL(ExecuteSportsQLRequestDTO executeSportsQLRequestDTO) {
				ISession session = loginWithToken(executeSportsQLRequestDTO.getToken());
		
		if (DistributedQueryHelper.isDistributionPossible() && executeSportsQLRequestDTO.getDistributor() != null && !executeSportsQLRequestDTO.getDistributor().toLowerCase().equals("no_distribution") ) {
			String sportsQL = SportsQLDistributorRegistry.addSportsQLDistributorConfig(executeSportsQLRequestDTO.getSportsQL(), executeSportsQLRequestDTO.getDistributor());	
			DistributedQueryInfo info = DistributedQueryHelper.executeQuery(sportsQL, "OdysseusScript", session, executeSportsQLRequestDTO.getTransformationConfig());
			
			String url = "http://"+info.getTopOperatorPeerIP()+":"+info.getTopOperatorPeerRestPort()+"/sports/"+DistributedSportsQLSocketServerResource.PATH;
			try {
				Client client = new Client(new org.restlet.Context(), Protocol.HTTP);
				ClientResource res = new ClientResource(url);
				res.setNext(client);
				DistributedSportsQLSocketRequestDTO req = new DistributedSportsQLSocketRequestDTO(info.getSharedQueryId(), executeSportsQLRequestDTO.getUsername(), executeSportsQLRequestDTO.getPassword());
				DistributedSportsQLSocketResponseDTO resp = res.post(req, DistributedSportsQLSocketResponseDTO.class);
				return new ExecuteSportsQLResponseDTO(resp.getToken(), resp.getSocketInfo(), resp.getQueryId(),info.getTopOperatorPeerRestPort());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			StringBuilder sportsQL = new StringBuilder();
			sportsQL.append("#PARSER SportsQL\n");
			sportsQL.append("#DOREWRITE false\n");
			sportsQL.append("#ADDQUERY \n");
			sportsQL.append(executeSportsQLRequestDTO.getSportsQL());			
			
			Collection<Integer> queryIDs = ExecutorServiceBinding.getExecutor().addQuery(sportsQL.toString(), "OdysseusScript", session, Context.empty());
			int queryId = queryIDs.iterator().next();	
			ExecutorServiceBinding.getExecutor().startQuery(queryId, session);
			SocketInfo peerSocket = SocketService.getInstance().getConnectionInformation(session, queryId,0);
			return new ExecuteSportsQLResponseDTO(session.getToken(), peerSocket, queryIDs.iterator().next(), RestService.getPort());
		}
	}	

}
