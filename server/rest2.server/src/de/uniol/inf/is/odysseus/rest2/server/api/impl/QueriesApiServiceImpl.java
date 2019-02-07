package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import javax.ws.rs.core.Response;

import de.uniol.inf.is.odysseus.rest2.server.api.ApiResponseMessage;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;
import de.uniol.inf.is.odysseus.rest2.server.api.QueriesApiService;
import de.uniol.inf.is.odysseus.rest2.server.model.Query;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class QueriesApiServiceImpl extends QueriesApiService {
	@Override
	public Response queriesGet() throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response queriesIdDelete(Integer id) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response queriesIdGet(Integer id) throws NotFoundException {
		// do some magic!
		Query query = new Query();
		query.setName("query" + String.valueOf(id));
		query.setState("Running");
		return Response.ok().entity(query).build();
	}

	@Override
	public Response queriesIdPatch(Integer id, Query query) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response queriesNameDelete(String name) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response queriesNameGet(String name) throws NotFoundException {
		try {
			int id = Integer.valueOf(name);
			return queriesIdGet(id);
		} catch (NumberFormatException e) {
			// do some magic!
			Query query = new Query();
			query.setName(name);
			return Response.ok().entity(query).build();
		}
	}

	@Override
	public Response queriesNamePatch(String name, Query query) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}

	@Override
	public Response queriesPost(Query query) throws NotFoundException {
		// do some magic!
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
}
