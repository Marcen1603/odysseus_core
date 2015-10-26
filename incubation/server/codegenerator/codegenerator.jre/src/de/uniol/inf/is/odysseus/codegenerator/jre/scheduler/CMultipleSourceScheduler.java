package de.uniol.inf.is.odysseus.codegenerator.jre.scheduler;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.scheduler.AbstractCScheduler;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class CMultipleSourceScheduler extends AbstractCScheduler{

	public CMultipleSourceScheduler() {
		super("MultipleSourceScheduler");
	}

	@Override
	public CodeFragmentInfo getStartCode(List<ILogicalOperator> operatorList) {
		
		CodeFragmentInfo code = new CodeFragmentInfo();
		
		List<String> sourceOpList = new ArrayList<String>();
		
		for(ILogicalOperator sourceOp : operatorList){
			sourceOpList.add(DefaultCodegeneratorStatus.getInstance().getVariable(sourceOp));
		}
																		  
	
		StringTemplate startCodeTemplate = new StringTemplate("scheduler","multipleSourceSchedulerStartCode");
		startCodeTemplate.getSt().add("operatorList", sourceOpList);
		
		code.addCode(startCodeTemplate.getSt().render());
		
		return code;
	}

	@Override
	public String getSchedulerCode() {
		
		StringTemplate multipleSourceSchedulerImplTemplate = new StringTemplate("scheduler","multipleSourceSchedulerImpl");
		multipleSourceSchedulerImplTemplate.getSt().add("packageName", getPackageName());
	
		return multipleSourceSchedulerImplTemplate.getSt().render();
	
	}
	

}
