package de.uniol.inf.is.odysseus.rest.serverresources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.SessionRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;

public class GetQueryIdsServerResource extends AbstractSessionServerResource {

	public static final String PATH = "getQueryIds";
	
	@Post
	public GenericResponseDTO<List<Integer>> getQueryIds(SessionRequestDTO sessionRequestDTO){
		ISession session = this.loginWithToken(sessionRequestDTO.getToken());
		Collection<Integer> quids = ExecutorServiceBinding.getExecutor().getLogicalQueryIds(session) ;
//		// First simple test with String
//		StringBuilder result = new StringBuilder();
//		Iterator<Integer> iter = quids.iterator();
//		if (iter.hasNext()){
//			result.append(iter.next());
//		}
//		while(iter.hasNext()){
//			result.append(",").append(iter.next());
//		}
		
		return new GenericResponseDTO<List<Integer>>(new ArrayList<Integer>(quids));
	}
}
