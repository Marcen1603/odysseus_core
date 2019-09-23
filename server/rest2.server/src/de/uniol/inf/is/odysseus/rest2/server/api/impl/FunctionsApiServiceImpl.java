package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.uniol.inf.is.odysseus.core.mep.IFunctionSignatur;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.Datatype;
import de.uniol.inf.is.odysseus.rest2.common.model.Function;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.api.FunctionsApi;
import de.uniol.inf.is.odysseus.rest2.server.api.FunctionsApiService;

/**
 * This class provides the implementation for the REST service
 * {@link FunctionsApi} that returns the available {@link MEP} functions.
 * 
 * @author Cornelius A. Ludmann
 */
public class FunctionsApiServiceImpl extends FunctionsApiService {

	@Override
	public Response functionsGet(Optional<ISession> session) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		List<Function> result = ExecutorServiceBinding.getExecutor().getMepFunctions().stream()
				.map(FunctionsApiServiceImpl::transform).collect(Collectors.toList());

		return Response.ok().entity(result).build();
	}

	/**
	 * Transforms a {@link IFunctionSignature} object to the DTO {@link Function}.
	 * 
	 * @param function The source {@link IFunctionSignature} object.
	 * @return The resulting {@link Function} object.
	 */
	static Function transform(IFunctionSignatur function) {
		Objects.requireNonNull(function);
		final Function result = new Function();
		result.setSymbol(function.getSymbol());
		if (function.getParameters() != null) {
			List<SDFDatatype[]> params = function.getParameters();
			for (SDFDatatype[] sdfDatatypes : params) {
				List<Datatype> datatypes = Arrays.stream(sdfDatatypes).map(DatatypesApiServiceImpl::transform)
						.collect(Collectors.toList());
				result.addParametersItem(datatypes);
			}
		}
		return result;
	}
}
