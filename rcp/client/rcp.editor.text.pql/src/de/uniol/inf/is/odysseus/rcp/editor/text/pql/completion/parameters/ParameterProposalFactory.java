package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.parameters;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

public class ParameterProposalFactory {

	public static IParameterProposal getProposal(LogicalParameterInformation parameter) {
		return getProposal(parameter, false);
	}

	private static IParameterProposal getProposal(LogicalParameterInformation parameter, boolean inList) {
		String typeName = parameter.getParameterClass().getName();
		if (parameter.isList() && !inList) {
			return new ListParameterProposal(getProposal(parameter, true));
		}

		if (typeName.endsWith("builder.TimeParameter")) {
			return new TimeParameterProposal();
		} else if (typeName.endsWith("builder.AggregateItemParameter")) {
			return new AggregateItemParameterProposal();
		} else if (typeName.endsWith("builder.CreateSDFAttributeParameter")) {
			return new CreateSDFParameterProposal();
		} else if (typeName.endsWith("builder.BooleanParameter")) {
			return new LongParameterProposal();
		} else if (typeName.endsWith("builder.IntegerParameter")) {
			return new LongParameterProposal();
		} else if (typeName.endsWith("builder.LongParameter")) {
			return new LongParameterProposal();
		} else if (typeName.endsWith("builder.DoubleParameter")) {
			return new LongParameterProposal();
		}
		return new StringParameterProposal();
	}

}
