package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.LogicalOperatorTypeInfo;
import de.uniol.inf.is.odysseus.rest2.common.model.LogicalOperatorTypeInfoParameters;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.api.OperatorsApiService;

public class OperatorsApiServiceImpl extends OperatorsApiService {

	@Override
	public Response operatorsGet(Optional<ISession> session) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		List<LogicalOperatorInformation> operatorInformations = ExecutorServiceBinding.getExecutor()
				.getOperatorInformations(session.get());

		List<LogicalOperatorTypeInfo> result = operatorInformations.stream().map(OperatorsApiServiceImpl::transform)
				.collect(Collectors.toList());

		return Response.ok().entity(result).build();
	}

	static LogicalOperatorTypeInfo transform(LogicalOperatorInformation logicalOperatorInformation) {
		final LogicalOperatorTypeInfo result = new LogicalOperatorTypeInfo();
		result.setOperatorName(logicalOperatorInformation.getOperatorName());
		result.setDoc(logicalOperatorInformation.getDoc());
		result.setUrl(logicalOperatorInformation.getUrl());
		logicalOperatorInformation.getParameters().forEach(parameter -> {
			final LogicalOperatorTypeInfoParameters parametersItem = new LogicalOperatorTypeInfoParameters();
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
