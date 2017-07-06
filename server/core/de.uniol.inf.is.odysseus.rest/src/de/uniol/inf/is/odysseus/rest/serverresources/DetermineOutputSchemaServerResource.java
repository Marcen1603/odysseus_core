package de.uniol.inf.is.odysseus.rest.serverresources;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.sdf.SDFSchemaInformation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.DetermineOutputSchemaRequestDTO;

public class DetermineOutputSchemaServerResource extends AbstractSessionServerResource {

	public static final String PATH = "determineOutputSchema";

	@Post
	public SDFSchemaInformation determineOutputSchema(DetermineOutputSchemaRequestDTO command) {
		ISession session = this.loginWithToken(command.getToken());
		String query = command.getQuery();
		String parserID = command.getParser();
		int port = Integer.parseInt(command.getPort());

		SDFSchema res = null;
		try {
			res = ExecutorServiceBinding.getExecutor().determineOutputSchema(query, parserID, session, port,
					Context.empty());
		} catch (Exception e) {
			e.printStackTrace();

			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}

		return SDFSchemaInformation.createSchemaInformation(res);
	}

}
