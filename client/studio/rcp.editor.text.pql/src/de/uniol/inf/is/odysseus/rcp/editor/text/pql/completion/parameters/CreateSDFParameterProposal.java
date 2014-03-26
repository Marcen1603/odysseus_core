package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

public class CreateSDFParameterProposal extends AbstractParameterProposal{
	
	
	@Override
	public List<String> createParameterTemplates(LogicalParameterInformation param) {
		String name = param.getName().toLowerCase();
		return Arrays.asList("['${"+name+"name}', '${"+name+"datatype}']");
	}


	@Override
	public List<String> getParameterValuesForPosition(LogicalParameterInformation param, int position) {
		List<String> values = new ArrayList<>();
		if(position==0){
			return values;
		}
		return OdysseusRCPEditorTextPlugIn.getDatatypeNames();			
		
	}


	@Override
	public List<String> createParameterTemplateParts(LogicalParameterInformation param, int position) {
		String name = param.getName().toLowerCase();
		if(position==0){
			return Arrays.asList("${"+name+"name}");
		}		
		return Arrays.asList("${"+name+"datatype}");
		
	}
	
	@Override
	public String formatPosition(int position, String value) {
		return formatString(value);
	}


}
