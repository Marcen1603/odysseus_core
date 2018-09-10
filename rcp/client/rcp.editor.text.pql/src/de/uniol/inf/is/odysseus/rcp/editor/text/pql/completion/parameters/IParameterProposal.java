package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.parameters;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

public interface IParameterProposal {	
	public List<String> createParameterTemplates(LogicalParameterInformation param);
	public List<String> createParameterTemplateParts(LogicalParameterInformation param, int position);
	public List<String> getParameterValuesForPosition(LogicalParameterInformation param, int position);	
	public String formatPosition(int position, String value);
}
