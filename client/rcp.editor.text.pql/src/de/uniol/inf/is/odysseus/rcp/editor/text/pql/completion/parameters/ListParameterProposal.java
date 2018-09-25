package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.parameters;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

public class ListParameterProposal extends AbstractParameterProposal {

	private IParameterProposal inner;

	public ListParameterProposal(IParameterProposal inner) {
		this.inner = inner;
	}

	@Override
	public List<String> createParameterTemplates(LogicalParameterInformation param) {		
		List<String> values = new ArrayList<>();
		for(String val : inner.createParameterTemplates(param)){
			values.add("["+val+"]");
		}
		return values;
	}

	public IParameterProposal getInnerProposal() {
		return inner;
	}

	
	@Override
	public List<String> getParameterValuesForPosition(LogicalParameterInformation param, int position) {		
		return inner.getParameterValuesForPosition(param, position);					
	}

	@Override
	public List<String> createParameterTemplateParts(LogicalParameterInformation param, int position) {
		return inner.createParameterTemplateParts(param, position);
	}
	
	@Override
	public String formatPosition(int position, String value) {	
		return inner.formatPosition(position, value);
	}

}
