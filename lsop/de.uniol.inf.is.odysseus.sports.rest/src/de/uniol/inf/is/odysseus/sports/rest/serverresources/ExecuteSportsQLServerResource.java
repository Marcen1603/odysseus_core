package de.uniol.inf.is.odysseus.sports.rest.serverresources;

import java.util.Collection;

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

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

public class ExecuteSportsQLServerResource extends AbstractSessionServerResource {

	public static final String PATH = "distributeSportsQL";

	@Post
	public ExecuteSportsQLResponseDTO executeSportsQL(ExecuteSportsQLRequestDTO executeSportsQLRequestDTO) {
		ISession session = loginWithToken(executeSportsQLRequestDTO.getToken());

		String address = getRequest().getClientInfo().getAddress();

		if (DistributedQueryHelper.isDistributionPossible() && executeSportsQLRequestDTO.getDistributor() != null && !executeSportsQLRequestDTO.getDistributor().toLowerCase().equals("no_distribution")) {
			String displayName = SportsQLDistributorRegistry.getDisplayName(executeSportsQLRequestDTO.getSportsQL());

			try {
				String sportsQL = SportsQLDistributorRegistry.addSportsQLDistributorConfig(executeSportsQLRequestDTO.getSportsQL(), executeSportsQLRequestDTO.getDistributor());

				DistributedQueryInfo info = DistributedQueryHelper.executeQuery(displayName, sportsQL, "OdysseusScript", session, executeSportsQLRequestDTO.getTransformationConfig(), executeSportsQLRequestDTO.isAddQuery(), executeSportsQLRequestDTO.getTimeToWait());
				if (info == null) {
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while waiting of DistributedQueryInfo of + " + displayName);
				}
				String url = "http://" + info.getTopOperatorPeerIP() + ":" + info.getTopOperatorPeerRestPort() + "/sports/" + DistributedSportsQLSocketServerResource.PATH;

				Client client = new Client(new org.restlet.Context(), Protocol.HTTP);
				ClientResource res = new ClientResource(url);
				res.setNext(client);
				DistributedSportsQLSocketRequestDTO req = new DistributedSportsQLSocketRequestDTO(info.getSharedQueryId(), executeSportsQLRequestDTO.getUsername(), executeSportsQLRequestDTO.getPassword(), address, executeSportsQLRequestDTO.isStartQuery());
				DistributedSportsQLSocketResponseDTO resp = res.post(req, DistributedSportsQLSocketResponseDTO.class);
				return new ExecuteSportsQLResponseDTO(resp.getToken(), resp.getSocketInfo(), resp.getQueryId(), info.getTopOperatorPeerRestPort());
			} catch (Exception e) {
				if (!(e instanceof ResourceException)) {
					e.printStackTrace();
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while executing distributed query+ " + displayName, e);
				}
				throw e;
			}
		}
		String displayName = SportsQLDistributorRegistry.getDisplayName(executeSportsQLRequestDTO.getSportsQL());

		try {
			StringBuilder sportsQL = new StringBuilder();
			sportsQL.append("#PARSER SportsQL\n");
			sportsQL.append("#DOREWRITE false\n");
			sportsQL.append("#ADDQUERY \n");
			sportsQL.append(executeSportsQLRequestDTO.getSportsQL());

			Collection<Integer> queryIDs = ExecutorServiceBinding.getExecutor().addQuery(sportsQL.toString(), "OdysseusScript", session, Context.empty());
			int queryId = queryIDs.iterator().next();
			ExecutorServiceBinding.getExecutor().startQuery(queryId, session);
			SocketInfo peerSocket = SocketService.getInstance().getConnectionInformation(session, queryId, 0);

			// Save connection information in backup-information for recovery
			SocketService.getInstance().informAboutNewClient(queryId, address, peerSocket.getIp(), peerSocket.getPort());

			return new ExecuteSportsQLResponseDTO(session.getToken(), peerSocket, queryIDs.iterator().next(), RestService.getPort());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while executing query+ " + displayName, e);
		}

	}

}
