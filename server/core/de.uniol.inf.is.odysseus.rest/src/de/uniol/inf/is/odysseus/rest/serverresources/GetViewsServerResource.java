package de.uniol.inf.is.odysseus.rest.serverresources;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.planmanagement.ResourceInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformationWS;
import de.uniol.inf.is.odysseus.core.sdf.SDFSchemaInformation;
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
	public List<ViewInformationWS> getViews(SessionRequestDTO sessionRequestDTO){
		ISession session = this.loginWithToken(sessionRequestDTO.getToken());

		// View information is not serializable --> Reuse Web Service things
		List<ViewInformation> views = (ArrayList<ViewInformation>) ExecutorServiceBinding.getExecutor().getStreamsAndViewsInformation(session) ;


		return transform(views);
	}

	private List<ViewInformationWS> transform(List<ViewInformation> views){
		List<ViewInformationWS> ret = new ArrayList<>(views.size());

		for (ViewInformation viewInformation : views) {
			ViewInformationWS vi = new ViewInformationWS();
			vi.setName(new ResourceInformation(viewInformation.getName()));
			vi.setSchema(SDFSchemaInformation.createSchemaInformation(viewInformation.getOutputSchema()));
			ret.add(vi);
		}

		return ret;
	}

}
