package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.OperatorInfo;
import de.uniol.inf.is.odysseus.rest2.common.model.OperatorInfoParameters;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.api.OperatorsApiService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class OperatorsApiServiceImpl extends OperatorsApiService {

	@Override
	public Response operatorsGet(Optional<ISession> session) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		List<LogicalOperatorInformation> operatorInformations = ExecutorServiceBinding.getExecutor()
				.getOperatorInformations(session.get());

		List<OperatorInfo> result = operatorInformations.stream().map(OperatorsApiServiceImpl::transform)
				.collect(Collectors.toList());

		return Response.ok().entity(result).build();
	}

	static OperatorInfo transform(LogicalOperatorInformation logicalOperatorInformation) {
		final OperatorInfo result = new OperatorInfo();
		result.setOperatorName(logicalOperatorInformation.getOperatorName());
		result.setDoc(logicalOperatorInformation.getDoc());
		result.setUrl(logicalOperatorInformation.getUrl());
		logicalOperatorInformation.getParameters().forEach(parameter -> {
			final OperatorInfoParameters parametersItem = new OperatorInfoParameters();
			parametersItem.setParameterType(parameter.getParameterClass().getName());
			parametersItem.setParameterName(parameter.getName());
			parametersItem.setList(parameter.isList());
			parametersItem.setDoc(parameter.getDoc());
			parametersItem.setMandatory(parameter.isMandatory());
			// TODO: do we need or handle parameter.arePossibleValuesDynamic()?
			// save copy:
			parametersItem.setPossibleValues(parameter.getPossibleValues().stream().collect(Collectors.toList()));
			parametersItem.setDeprecated(parameter.isDeprecated());
			result.addParametersItem(parametersItem);
		});
		result.setMaxPorts(logicalOperatorInformation.getMaxPorts());
		result.setMinPorts(logicalOperatorInformation.getMinPorts());
		result.setCategories(Arrays.asList(logicalOperatorInformation.getCategories()));
		result.setHidden(logicalOperatorInformation.isHidden());
		result.setDeprecated(logicalOperatorInformation.isDeprecated());
		return result;
	}
}
