package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

public class TimeParameterProposal extends AbstractParameterProposal{
	
	@Override
	public List<String> createParameterTemplates(LogicalParameterInformation param) {
		String name = param.getName().toLowerCase();
		return Arrays.asList("[${"+name+"value}, '${"+name+"unit}']");
	}


	@Override
	public List<String> getParameterValuesForPosition(LogicalParameterInformation param, int position) {
		List<String> params = new ArrayList<>();
		if(position==1){
			params.add("");
			for(TimeUnit t : TimeUnit.values()){
				params.add(t.toString());
			}					
		}
		return params;		
	}


	@Override
	public List<String> createParameterTemplateParts(LogicalParameterInformation param, int position) {
		String name = param.getName().toLowerCase();
		if(position==0){
			return Arrays.asList("${"+name+"value}");
		}
		return Arrays.asList("'${"+name+"unit}'");
		
	}
	
	@Override
	public String formatPosition(int position, String value) {
		if(position==1){
			return formatString(value);
		}
		return value;
	}
	
	
	

}
