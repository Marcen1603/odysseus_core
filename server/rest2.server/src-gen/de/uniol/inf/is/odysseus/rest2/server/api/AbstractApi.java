package de.uniol.inf.is.odysseus.rest2.server.api;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.wso2.msf4j.Request;

public class AbstractApi {

	@OPTIONS
	@Path("/")
	public Response queriesOptions(@Context Request request) throws NotFoundException {
		return Response.ok()
				.header("Access-Control-Allow-Origin", request.getHeader("Origin")).header("Vary", "Origin")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, OPTIONS")
				.header("Access-Control-Max-Age", "3600")
				.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, Authorization").build();
	}
}
