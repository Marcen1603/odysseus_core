package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

public class AggregateItemParameterProposal extends AbstractParameterProposal implements IParameterProposal {

	@Override
	public List<String> createParameterTemplates(LogicalParameterInformation param) {
		return Arrays.asList("['${aggregatefunction}', '${attribute}', '${aggregatename}']", "['${aggregatefunction}', '${attribute}', '${aggregatename}', '${aggreatedatatype}']");
	}

	@Override
	public List<String> createParameterTemplateParts(LogicalParameterInformation param, int position) {
		switch (position) {
		case 0:
			return Arrays.asList("'${aggregatefunction}'");
		case 1:
			return Arrays.asList("'${attribute}'");
		case 2:
			return Arrays.asList("'${aggregatename}'");
		case 3:
			return Arrays.asList("'${aggreatedatatype}'");
		default:
			return new ArrayList<>();
		}
	}

	@Override
	public List<String> getParameterValuesForPosition(LogicalParameterInformation param, int position) {
		switch (position) {
		case 0:
			@SuppressWarnings("rawtypes")
			Class<? extends IStreamObject> datamodel = Tuple.class;
			return OdysseusRCPEditorTextPlugIn.getDefault().getInstalledAggregateFunctions(datamodel);
		case 1:
			return Arrays.asList("'${attribute}'");
		case 2:
			return Arrays.asList("'${aggregatename}'");
		case 3:
			return OdysseusRCPEditorTextPlugIn.getDatatypeNames();
		default:
			return new ArrayList<>();
		}
	}

	@Override
	public String formatPosition(int position, String value) {
		return formatString(value);
	}

}
