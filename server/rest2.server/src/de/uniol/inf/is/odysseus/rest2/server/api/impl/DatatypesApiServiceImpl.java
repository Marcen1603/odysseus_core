package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import static de.uniol.inf.is.odysseus.rest2.server.api.impl.Auxiliary.session;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.wso2.msf4j.Request;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.api.DatatypesApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;
import de.uniol.inf.is.odysseus.rest2.server.model.Datatype;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class DatatypesApiServiceImpl extends DatatypesApiService {

	@Override
	public Response datatypesGet(@Context Request request) throws NotFoundException {

		Optional<ISession> session = session(request);

		if (session.isPresent()) {
			if (ExecutorServiceBinding.getExecutor() != null) {
				IDataDictionaryWritable dd = ExecutorServiceBinding.getExecutor().getDataDictionary(session.get());
				List<Datatype> datatypes = dd.getDatatypes().stream().map(datatype -> transform(datatype))
						.sorted(Comparator.comparing(Datatype::getUri)).collect(Collectors.toList());
				return Response.ok().entity(datatypes).build();
			}
			return Response.serverError().build();
		}

		return Response.status(Status.FORBIDDEN).build();
	}

	private static Datatype transform(SDFDatatype datatype) {
		Objects.requireNonNull(datatype);
		Datatype result = new Datatype();
		result.setUri(datatype.getURI());
		if (datatype.getSubType() != null) {
			result.setSubtype(transform(datatype.getSubType()));
		}
//		TODO
//		result.setSubschema(datatype.getSchema().get);
		return result;
	}
}
