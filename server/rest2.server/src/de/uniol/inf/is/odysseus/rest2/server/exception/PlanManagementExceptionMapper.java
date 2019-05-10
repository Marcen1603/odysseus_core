package de.uniol.inf.is.odysseus.rest2.server.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;

public class PlanManagementExceptionMapper implements ExceptionMapper<PlanManagementException> {

	@Override
	public Response toResponse(PlanManagementException ex) {
		return Response.status(422).entity(ex.getMessage()).type("text/plain").build();
	}

}
