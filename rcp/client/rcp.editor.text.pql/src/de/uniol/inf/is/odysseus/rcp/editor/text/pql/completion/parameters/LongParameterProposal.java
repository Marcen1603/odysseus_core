package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.parameters;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

public class LongParameterProposal extends AbstractParameterProposal{
	
	@Override
	public List<String> createParameterTemplates(LogicalParameterInformation param) {
		return Arrays.asList("${"+param.getName().toLowerCase()+"}");
	}

	@Override
	public List<String> getParameterValuesForPosition(LogicalParameterInformation param, int position) {
		return Arrays.asList("");
	}

	@Override
	public List<String> createParameterTemplateParts(LogicalParameterInformation param, int position) {
		return createParameterTemplates(param);
	}

}
