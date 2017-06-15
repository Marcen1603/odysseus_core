package de.uniol.inf.is.odysseus.rest.serverresources;

import java.util.List;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.SessionRequestDTO;

/**
 * Retrieve all query ids
 * @author Marco Grawunder
 *
 */

public class GetViewsServerResource extends AbstractSessionServerResource {

	public static final String PATH = "getViews";

	@Post
	public List<ViewInformation> getViews(SessionRequestDTO sessionRequestDTO){
		ISession session = this.loginWithToken(sessionRequestDTO.getToken());

		List<ViewInformation> views = ExecutorServiceBinding.getExecutor().getStreamsAndViewsInformation(session) ;
		// TODO: Seems to be problems with JSON and ViewInformation ...


		return views;
	}
}
