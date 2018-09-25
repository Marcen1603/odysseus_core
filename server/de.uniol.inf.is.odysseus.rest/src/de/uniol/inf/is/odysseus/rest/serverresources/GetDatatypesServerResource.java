package de.uniol.inf.is.odysseus.rest.serverresources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.SessionRequestDTO;

/**
 * Retrieve all datatypes
 * @author Marco Grawunder
 *
 */

public class GetDatatypesServerResource extends AbstractSessionServerResource {

	public static final String PATH = "getDatatypes";

	@Post
	public List<String> getDatatypes(SessionRequestDTO sessionRequestDTO){
		ISession session = this.loginWithToken(sessionRequestDTO.getToken());

		Set<SDFDatatype> datatypes = ExecutorServiceBinding.getExecutor().getRegisteredDatatypes(session) ;
				
		List<String> ret = new ArrayList<>();
		
		for(SDFDatatype dt: datatypes) {
			ret.add(dt.getQualName());
		}
		
		Collections.sort(ret);
		
		return ret;
	}


}
