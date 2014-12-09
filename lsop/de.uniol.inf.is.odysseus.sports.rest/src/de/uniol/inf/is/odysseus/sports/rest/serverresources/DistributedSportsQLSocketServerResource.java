package de.uniol.inf.is.odysseus.sports.rest.serverresources;





import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractServerResource;
import de.uniol.inf.is.odysseus.rest.socket.SocketInfo;
import de.uniol.inf.is.odysseus.rest.socket.SocketService;
import de.uniol.inf.is.odysseus.sports.distributor.webservice.DistributedQueryHelper;
import de.uniol.inf.is.odysseus.sports.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.sports.rest.dto.request.DistributedSportsQLSocketRequestDTO;
import de.uniol.inf.is.odysseus.sports.rest.dto.response.DistributedSportsQLSocketResponseDTO;

public class DistributedSportsQLSocketServerResource extends AbstractServerResource  {

	public static final String PATH = "distributeSportsQLSocket";

	@Post
	public DistributedSportsQLSocketResponseDTO addQuery(DistributedSportsQLSocketRequestDTO distributedSportsQLSocketRequestDTO) {
		String username = distributedSportsQLSocketRequestDTO.getUsername();
		String password = distributedSportsQLSocketRequestDTO.getPassword();
		String sharedQueryId = distributedSportsQLSocketRequestDTO.getSharedQueryId();
		
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		ISession session = UserManagementProvider.getSessionmanagement().login(username, password.getBytes(), tenant);
		Integer queryId = DistributedQueryHelper.getQueryIdWithTopOperator(sharedQueryId, session);
		ExecutorServiceBinding.getExecutor().startQuery(queryId, session);
		IPhysicalOperator operator = DistributedQueryHelper.getTopOperatorOfQuery(queryId, session);
		SocketInfo peerSocket = SocketService.getInstance().getConnectionInformation(session,queryId, operator);

		DistributedSportsQLSocketResponseDTO resp = new DistributedSportsQLSocketResponseDTO(session.getToken(),peerSocket,queryId);
		return resp;	
		
	}

	

}
